package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityFindAndCreateNewConversationBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.util.Log
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FindAndCreateNewConversationActivity : DataBindingDaggerActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    lateinit var userViewModel: AbstractUserViewModel;
    lateinit var roomViewModel: AbstractRoomViewModel;

    private lateinit var mxSession: MXSession;

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityFindAndCreateNewConversationBinding>(this, R.layout.activity_find_and_create_new_conversation, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.new_conversation);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractUserViewModel::class.java);
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);

        binding.lifecycleOwner = this;

        val listUserAdapter = ListUserRecyclerViewAdapter(appExecutors, dataBindingComponent = dataBindingComponent, diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }) { user ->
            roomViewModel.setInviteUserToDirectChat(user.id);
        }
        binding.users = userViewModel.getUsers();
        binding.inviteUser = roomViewModel.getInviteUserToDirectChat();
        binding.recyclerViewUsers.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerViewUsers.adapter = listUserAdapter;
        userViewModel.getUsers().observe(this, Observer { t ->
            kotlin.run {
                t?.data?.let {
                    Log.d("User Size", it.size.toString())
                }
                listUserAdapter.submitList(t?.data);
            }
        });
        roomViewModel.getInviteUserToDirectChat().observe(this, Observer { t ->
            kotlin.run {
                t?.data?.let { s ->
                    joinRoom(s.id);
                }
            }
        })
        var disposable: Disposable? = null;
        RxTextView.textChanges(binding.editQuery).subscribe { t: CharSequence? ->
            kotlin.run {
                disposable?.let { disposable -> disposable.dispose() }
                disposable = Observable.timer(100, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe { time: Long? -> t?.let { charSequence -> userViewModel.setQuery(charSequence.toString()) } };
            }
        };
        binding.newRoom.setOnClickListener { v ->
            val intent = Intent(this, CreateNewRoomActivity::class.java);
            startActivity(intent);
        }
    }

    private fun joinRoom(roomId: String) {
        val room = mxSession.dataHandler.store.getRoom(roomId);
        mxSession.joinRoom(room!!.getRoomId(), object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()
                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.getMyUserId()
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.getRoomId()

                CommonActivityUtils.goToRoomPage(this@FindAndCreateNewConversationActivity, mxSession, params)
                finish();
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@FindAndCreateNewConversationActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {

                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }
}
