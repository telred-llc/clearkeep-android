package vmodev.clearkeep.activities

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityPreviewInviteRoomBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.HashMap
import javax.inject.Inject

class PreviewInviteRoomActivity : DaggerAppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);
    private lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPreviewInviteRoomBinding>(this, R.layout.activity_preview_invite_room, dataBindingComponent);
        val roomId: String = intent.getStringExtra("ROOM_ID") ?: ""
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.profile);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        var index: Int = 0;
        val roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.room = roomViewModel.getRoom();
        roomViewModel.getRoom().observe(this, Observer { t ->
            kotlin.run {
                if (t?.data?.type == 1 || t?.data?.type == 2) {
                    if (index > 0)
                        return@run;
                    index++;
                    val params = HashMap<String, Any>()

                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.getMyUserId()
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = t.data.id;

                    CommonActivityUtils.goToRoomPage(this@PreviewInviteRoomActivity, mxSession, params)
                    finish();
                }
            }
        })
        binding.lifecycleOwner = this;

        roomViewModel.setRoomId(roomId);

        binding.buttonJoin.setOnClickListener { v ->
            kotlin.run {
                roomViewModel.joinRoom(roomId);
            }
        }
    }
}
