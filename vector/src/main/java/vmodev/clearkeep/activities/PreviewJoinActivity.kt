package vmodev.clearkeep.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import im.vector.databinding.ActivityPreviewJoinBinding
import kotlinx.android.synthetic.main.activity_preview_invite_room.view.*
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.ultis.FormatString
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.*
import javax.inject.Inject

class PreviewJoinActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mxSession: MXSession;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPreviewJoinBinding>(this, R.layout.activity_preview_join, dataBinding.getDataBindingComponent());
        val roomId: String = intent.getStringExtra(ROOM_ID) ?: ""
        val roomName: String = intent.getStringExtra(ROOM_NAME) ?: ""
        binding.tvRoomName.text = roomName
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        var index: Int = 0;
        val roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.room = roomViewModel.getRoom();

        roomViewModel.getLeaveRoom().observe(this, Observer { t ->
            t?.let { resource ->
                if (resource.status == Status.SUCCESS)
                    finish()
            }
        })

        roomViewModel.getRoomListUserFindByID(roomId).observe(this, Observer {
            binding.nameTopic= it.data?.room?.name?.let { it1 -> FormatString.formatName(it1) }
        })
        binding.lifecycleOwner = this;

        roomViewModel.setRoomId(roomId);

        binding.buttonJoin.setOnClickListener { v ->
            roomViewModel.joinRoom(roomId)
            roomViewModel.getRoom().observe(this, Observer { t ->
                kotlin.run {
                    t?.let { resource ->
                        if (resource.status == Status.SUCCESS) {
                            resource.data?.let { room ->
                                    if (index > 0)
                                        return@run;
                                    index++;
                                    val params = HashMap<String, Any>()

                                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.myUserId
                                    params[VectorRoomActivity.EXTRA_ROOM_ID] = room.id

                                    CommonActivityUtils.goToRoomPage(this@PreviewJoinActivity, mxSession, params)
                                    finish();
                            }
                        }
                    }
                }
            })
        }
        binding.buttonDecline.setOnClickListener { v ->
            finish()
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
        const val ROOM_NAME = "ROOM_NAME";
    }
}
