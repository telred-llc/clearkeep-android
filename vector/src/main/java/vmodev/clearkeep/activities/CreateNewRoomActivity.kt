package vmodev.clearkeep.activities

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.text.Editable
import com.google.android.gms.common.util.DataUtils
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityCreateNewRoomBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class CreateNewRoomActivity : DaggerAppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    lateinit var mxSession: MXSession;

    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateNewRoomBinding>(this, R.layout.activity_create_new_room, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.new_room);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        mxSession = Matrix.getInstance(applicationContext).defaultSession;

        binding.lifecycleOwner = this;
        val roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.room = roomViewModel.createNewRoom();
        roomViewModel.createNewRoom().observe(this, Observer { t ->
            t?.data?.let { room ->
                val intent = Intent(this, InviteUsersToRoomActivity::class.java);
                intent.putExtra("ROOM_ID", room.id);
                startActivity(intent);
                finish();
            }
        })
        binding.textViewRightToolbar.setOnClickListener { v ->
            roomViewModel.setCreateNewRoom(binding.editTextRoomName.text.toString(), binding.editTextRoomTopic.text.toString(), if (binding.switchRoomVisibility.isChecked) "public" else "private")
        }
    }
}
