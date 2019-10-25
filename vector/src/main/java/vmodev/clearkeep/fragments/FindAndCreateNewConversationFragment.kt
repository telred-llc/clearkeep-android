package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityFindAndCreateNewConversationBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.NewRoomActivity
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FindAndCreateNewConversationFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var roomUserJoinDao: AbstractRoomUserJoinDao;
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: ActivityFindAndCreateNewConversationBinding;
    private var listUserSuggested: List<User>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_find_and_create_new_conversation, container, false, dataBinding.getDataBindingComponent());
        binding.lifecycleOwner = viewLifecycleOwner;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel();
        (activity as NewRoomActivity).setNameTitle(resources.getString(R.string.new_conversation))
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
                val intentRoom = Intent(this.activity, RoomActivity::class.java);
                intentRoom.putExtra(RoomActivity.EXTRA_MATRIX_ID, application.getUserId());
                intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                startActivity(intentRoom);
                this.activity?.finish();
            }
        })
        binding.recyclerViewUsers.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewUsers.adapter = listUserAdapter;
        binding.recyclerViewUsers.setHasFixedSize(true)
        binding.recyclerViewUsers.isNestedScrollingEnabled = false
        val listUser = ArrayList<User>()
        viewModelFactory.getViewModel().getListUserSuggested(1, application.getUserId()).observe(this, Observer { t ->
            kotlin.run {
                listUserAdapter.submitList(t?.data);
                listUserSuggested = t?.data
            }
        });

        viewModelFactory.getViewModel().getUsers().observe(this, Observer { t ->
            kotlin.run {
                if (!TextUtils.isEmpty(binding.editQuery.toString().trim()))
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
                            Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show();
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
                    .subscribe { time: Long? ->
                        t?.let { charSequence ->
                            if (TextUtils.isEmpty(charSequence.toString())) {
                                listUserAdapter.submitList(listUserSuggested)
                            } else {
                                viewModelFactory.getViewModel().setQuery(charSequence.toString())
                            }

                        }
                    };
        }
        binding.newRoom.setOnClickListener { v ->
            findNavController().navigate(FindAndCreateNewConversationFragmentDirections.createNewRoom());
        }
        binding.newCall.setOnClickListener {
            findNavController().navigate(FindAndCreateNewConversationFragmentDirections.createNewCall());
        }
        binding.nestedScrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            hideKeyboard()
        }
        binding.editQuery.setOnEditorActionListener { p0, p1, p2 ->
            hideKeyboard()
            false;
        };
    }

    override fun getFragment(): Fragment {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
