package vmodev.clearkeep.activities

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityPreviewInviteRoomBinding
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class PreviewInviteRoomActivity : DaggerAppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPreviewInviteRoomBinding>(this, R.layout.activity_preview_invite_room, dataBindingComponent);
        val roomId: String = intent.getStringExtra("ROOM_ID") ?: ""
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.profile);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        val roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java)
        binding.room = roomViewModel.getRoom()
        binding.lifecycleOwner = this;

        roomViewModel.setRoomId(roomId);
    }
}
