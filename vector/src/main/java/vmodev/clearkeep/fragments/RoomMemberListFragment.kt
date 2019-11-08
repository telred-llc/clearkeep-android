package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import im.vector.databinding.FragmentRoomMemberListBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.ProfileActivity
import vmodev.clearkeep.activities.ViewUserProfileActivity
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class RoomMemberListFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: FragmentRoomMemberListBinding;
    private lateinit var roomViewModel: AbstractRoomViewModel;
    private lateinit var listUserAdapter: ListUserRecyclerViewAdapter;

    private val args: RoomMemberListFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_member_list, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        listUserAdapter = ListUserRecyclerViewAdapter(appExecutors, object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.avatarUrl == p1.avatarUrl && p0.name == p1.name;
            }
        }, dataBinding) { user ->
            if (application.getUserId().compareTo(user.id) == 0) {
                val userIntent = Intent(this@RoomMemberListFragment.activity, ProfileActivity::class.java);
                startActivity(userIntent);
            } else {
                val otherUserIntent = Intent(this@RoomMemberListFragment.activity, ViewUserProfileActivity::class.java);
                otherUserIntent.putExtra(ViewUserProfileActivity.USER_ID, user.id)
                startActivity(otherUserIntent);
            }
        }
        roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.users = roomViewModel.getGetUserFromRoomIdResult();
        binding.recyclerView.adapter = listUserAdapter;
        roomViewModel.getGetUserFromRoomIdResult().observe(viewLifecycleOwner, Observer { t ->
            listUserAdapter.submitList(t?.data);
        });
        binding.lifecycleOwner = viewLifecycleOwner;
        args.roomId?.let {
            roomViewModel.setGetUserFromRoomId(it)
        }

    }

    override fun getFragment(): Fragment {
        return this;
    }
}
