package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityRoomMemberListBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Inject

class RoomMemberListActivity : DataBindingDaggerActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityRoomMemberListBinding;
    private lateinit var roomViewModel: AbstractRoomViewModel;
    private lateinit var listUserAdapter: ListUserRecyclerViewAdapter;

    private lateinit var session: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_member_list, dataBindingComponent);
        session = Matrix.getInstance(applicationContext).defaultSession;
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        listUserAdapter = ListUserRecyclerViewAdapter(appExecutors, object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.avatarUrl == p1.avatarUrl && p0.name == p1.name;
            }
        }, dataBindingComponent) { user ->
            if (session.myUserId.compareTo(user.id) == 0) {
                val userIntent = Intent(this@RoomMemberListActivity, ProfileActivity::class.java);
                startActivity(userIntent);
            } else {
                val otherUserIntent = Intent(this@RoomMemberListActivity, ViewUserProfileActivity::class.java);
                otherUserIntent.putExtra(ViewUserProfileActivity.USER_ID, user.id)
                startActivity(otherUserIntent);
            }
        }
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.users = roomViewModel.getGetUserFromRoomIdResult();
        binding.recyclerView.adapter = listUserAdapter;
        roomViewModel.getGetUserFromRoomIdResult().observe(this, Observer { t ->
            listUserAdapter.submitList(t?.data);
        });
        val roomId = intent.getStringExtra(ROOM_ID);
        binding.lifecycleOwner = this;
        roomViewModel.setGetUserFromRoomId(roomId);

    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
