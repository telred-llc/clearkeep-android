package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityFindAndCreateNewConversationBinding
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FindAndCreateNewConversationActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var roomUserJoinDao: AbstractRoomUserJoinDao;
    private lateinit var binding: ActivityFindAndCreateNewConversationBinding;
    private var userId: String? = null;

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_and_create_new_conversation, dataBinding.getDataBindingComponent());
        userId = intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.new_conversation);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        binding.lifecycleOwner = this;
        setUpViewModel();
    }

    @SuppressLint("CheckResult")
    private fun setUpViewModel() {
        val listUserAdapter = ListUserRecyclerViewAdapter(appExecutors, diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }, dataBinding = dataBinding) { user ->
            viewModelFactory.getViewModel().setInviteUserToDirectChat(user.id);
        }
        binding.users = viewModelFactory.getViewModel().getUsers();
        binding.inviteUser = viewModelFactory.getViewModel().getInviteUserToDirectChat();
        viewModelFactory.getViewModel().joinRoomResult().observe(this, Observer {
            it?.data?.let {
                val intentRoom = Intent(this@FindAndCreateNewConversationActivity, RoomActivity::class.java);
                intentRoom.putExtra(RoomActivity.EXTRA_MATRIX_ID, userId);
                intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                startActivity(intentRoom);
            }
        })
        binding.recyclerViewUsers.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerViewUsers.adapter = listUserAdapter;
        viewModelFactory.getViewModel().getUsers().observe(this, Observer { t ->
            kotlin.run {
                listUserAdapter.submitList(t?.data);
            }
        });
        viewModelFactory.getViewModel().getInviteUserToDirectChat().observe(this, Observer { t ->
            kotlin.run {
                t?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { viewModelFactory.getViewModel().setJoinRoom(it.id) }
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                        }
                        else -> {

                        }
                    }
                }
            }
        })
        var disposable: Disposable? = null;
        binding.editQuery.textChanges().skipInitialValue().subscribe { t ->
            disposable?.let { disposable -> disposable.dispose() }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { time: Long? -> t?.let { charSequence -> viewModelFactory.getViewModel().setQuery(charSequence.toString()) } };
        }
        binding.newRoom.setOnClickListener { v ->
            val intent = Intent(this, CreateNewRoomActivity::class.java);
            startActivity(intent);
        }
        binding.newCall.setOnClickListener {
            userId?.let {
                val intent = Intent(this, CreateNewCallActivity::class.java);
                intent.putExtra(CreateNewCallActivity.USER_ID, it);
                startActivity(intent);
            }
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
