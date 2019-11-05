package vmodev.clearkeep.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityPreviewInviteRoomBinding
import kotlinx.android.synthetic.main.activity_preview_invite_room.view.*
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.*
import javax.inject.Inject

class PreviewInviteRoomActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPreviewInviteRoomBinding>(this, R.layout.activity_preview_invite_room, dataBinding.getDataBindingComponent());
        window?.statusBarColor = ContextCompat.getColor(this, R.color.primary_hint_text_color_light)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        val roomId: String = intent.getStringExtra(ROOM_ID) ?: ""
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        var index: Int = 0;
        val roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.room = roomViewModel.getRoom();
        roomViewModel.getRoom().observe(this, Observer { t ->
            kotlin.run {
                t?.let { resource ->
                    if (resource.status == Status.SUCCESS) {
                        resource.data?.let { room ->
                            if (room.type == 1 || room.type == 2) {
                                if (index > 0)
                                    return@run;
                                index++;
                                val params = HashMap<String, Any>()

                                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.myUserId
                                params[VectorRoomActivity.EXTRA_ROOM_ID] = room.id

                                CommonActivityUtils.goToRoomPage(this@PreviewInviteRoomActivity, mxSession, params)
                                finish();
                            }
                        }
                    }
                }
            }
        })
        roomViewModel.getLeaveRoom().observe(this, Observer { t ->
            t?.let { resource ->
                if (resource.status == Status.SUCCESS)
                    finish()
            }
        })
        binding.lifecycleOwner = this;

        roomViewModel.setRoomId(roomId);

        binding.buttonJoin.setOnClickListener { v ->
            roomViewModel.joinRoom(roomId)
        }
        binding.buttonDecline.setOnClickListener { v ->
            roomViewModel.setLeaveRoom(roomId)
        }
        binding.roomListUser = roomViewModel.getRoomListUserFindByID(roomId)
        binding.toolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
