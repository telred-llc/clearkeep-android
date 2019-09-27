package vmodev.clearkeep.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityInviteUsersToRoomBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

class InviteUsersToRoomFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var application: IApplication;

    private val listSelected = HashMap<String, User>();
    private lateinit var binding: ActivityInviteUsersToRoomBinding;
    private val args: InviteUsersToRoomFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_invite_users_to_room, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(getString(R.string.add_members))
        binding.toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_green)
        binding.toolbar.setNavigationOnClickListener {
            this.activity?.finish()
        }
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, listSelected = listSelected, dataBindingComponent = dataBinding.getDataBindingComponent()
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }) { user, status ->
            if (listSelected.size > 0) {
                binding.cardViewInvite.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.app_green, null));
                binding.cardViewInvite.isClickable = true;
            } else {
                binding.cardViewInvite.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.button_disabled_text_color, null));
                binding.cardViewInvite.isClickable = false;
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner;
        binding.users = viewModelFactory.getViewModel().getUsers();
        binding.recyclerViewListUser.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListUser.adapter = listUserAdapter;
        viewModelFactory.getViewModel().getUsers().observe(this, Observer
        { t ->
            listUserAdapter.submitList(t?.data)
            listSelected.clear();
        });
        viewModelFactory.getViewModel().joinRoomResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(this.activity, it.message, Toast.LENGTH_SHORT).show();
                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            val roomIntent = Intent(this.activity, RoomActivity::class.java);
                            roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, application.getUserId());
                            roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                            startActivity(roomIntent);
//                            findNavController().navigate(InviteUsersToRoomFragmentDirections.done());
                            findNavController().popBackStack(R.id.action_home, false)

                        }
                    }
                    else -> {

                    }
                }
            }
        });
        viewModelFactory.getViewModel().getInviteUsersToRoomResult().observe(viewLifecycleOwner, Observer
        { t ->
            t?.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show();
                    }
                    Status.SUCCESS -> {
                        it.data?.let {
                            binding.room = viewModelFactory.getViewModel().joinRoomResult();
                            viewModelFactory.getViewModel().setJoinRoom(it.id);
                        }
                    }
                    else -> {

                    }
                }
            }
        })
        var disposable: Disposable? = null;
        binding.editTextQuery.textChanges().subscribe({
            disposable?.let { disposable -> disposable.dispose(); }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                    .mainThread()).subscribe { time: Long? ->
                viewModelFactory.getViewModel().setQuery(it.toString())
            };
        }, {

        });

        binding.cardViewInvite.setOnClickListener { v ->
            val userIds: ArrayList<String> = ArrayList();
            for ((key, value) in listSelected) {
                userIds.add(key);
            }
            args.roomId?.let {
                binding.room = viewModelFactory.getViewModel().getInviteUsersToRoomResult();
                viewModelFactory.getViewModel().setInviteUsersToRoom(it, userIds);
            }
        }
        binding.cardViewInvite.isClickable = false;
    }

    override fun getFragment(): Fragment {
        return this;
    }

}
