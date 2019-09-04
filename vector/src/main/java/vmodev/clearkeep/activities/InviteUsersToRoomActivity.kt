package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityInviteUsersToRoomBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.collections.set

class InviteUsersToRoomActivity : DataBindingDaggerActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var roomId: String;
    private var createFromNewRoom: Boolean = true;
    private lateinit var mxSession: MXSession;
    private val listSelected = HashMap<String, User>();

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityInviteUsersToRoomBinding>(this, R.layout.activity_invite_users_to_room);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.add_members);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                if (createFromNewRoom)
                    joinRoom(roomId);
                else
                    finish();
            }
        }
        roomId = intent.getStringExtra(ROOM_ID);
        createFromNewRoom = intent.getBooleanExtra(CREATE_FROM_NEW_ROOM, true);
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, dataBindingComponent = dataBindingComponent, listSelected = listSelected
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }) { user, status ->
            if (listSelected.size > 0) {
                binding.textViewRightToolbar.setTextColor(Color.parseColor("#63CD9A"))
                binding.textViewRightToolbar.isClickable = true;
            } else {
                binding.textViewRightToolbar.setTextColor(Color.parseColor("#B8BDC7"))
                binding.textViewRightToolbar.isClickable = false;
            }
        }

        binding.lifecycleOwner = this;
        val userViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractUserViewModel::class.java);
        val roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.users = userViewModel.getUsers();
        binding.room = roomViewModel.getInviteUsersToRoomResult();
        binding.recyclerViewListUser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListUser.adapter = listUserAdapter;
        userViewModel.getUsers().observe(this, Observer { t ->
            listUserAdapter.submitList(t?.data)
            listSelected.clear();
        });
        roomViewModel.getInviteUsersToRoomResult().observe(this, Observer { t ->
            t?.let {
                when (it.status) {
                    Status.ERROR -> {
                        finish();
                    }
                    Status.SUCCESS -> {
                        it.data?.let { resource ->
                            if (createFromNewRoom)
                                joinRoom(resource.id)
                            else {
                                finish();
                            }
                        }
                    }
                    else -> {

                    }
                }
            }
        })
        var disposable: Disposable? = null;
        RxTextView.textChanges(binding.editTextQuery).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: CharSequence? ->
                    disposable?.let { disposable -> disposable.dispose(); }
                    disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                            .mainThread()).subscribe { time: Long? ->
                        run {
                            t?.let { charSequence ->
                                userViewModel.setQuery(charSequence.toString())
                            }
                        }
                    };
                }

        binding.textViewRightToolbar.setOnClickListener { v ->
            val userIds: ArrayList<String> = ArrayList<String>();
            for ((key, value) in listSelected) {
                userIds.add(key);
            }
            roomViewModel.setInviteUsersToRoom(roomId, userIds);
        }
        binding.textViewRightToolbar.isClickable = false;
    }

    private fun joinRoom(roomId: String) {
        val room = mxSession.dataHandler.store.getRoom(roomId);
        mxSession.joinRoom(room!!.roomId, object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()
                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.myUserId
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.roomId

                CommonActivityUtils.goToRoomPage(this@InviteUsersToRoomActivity, mxSession, params)
                finish();
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@InviteUsersToRoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
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

    companion object {
        const val ROOM_ID = "ROOM_ID";
        const val CREATE_FROM_NEW_ROOM = "CREATE_FROM_NEW_ROOM";
    }
}
