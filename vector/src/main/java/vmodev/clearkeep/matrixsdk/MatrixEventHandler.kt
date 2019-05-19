package vmodev.clearkeep.matrixsdk

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log
import im.vector.Matrix
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.User
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixEventHandler @Inject constructor(private val application: Application,
                                             private val userRepository: UserRepository,
                                             private val roomRepository: RoomRepository)
    : MXEventListener(), IMatrixEventHandler {
    private var mxSession: MXSession? = null;
    override fun onAccountDataUpdated() {
        super.onAccountDataUpdated()
        val user = mxSession!!.myUser;
        Log.d("UserId:", user.user_id);
        userRepository.updateUser(user.user_id, user.displayname, user.avatarUrl);
    }

    override fun onAccountInfoUpdate(myUser: MyUser?) {
        super.onAccountInfoUpdate(myUser)
        val avatarUrl = mxSession!!.contentManager.getDownloadableUrl(myUser!!.avatarUrl);
        userRepository.updateUser(myUser!!.user_id, myUser.displayname, avatarUrl!!);
    }

    override fun onDirectMessageChatRoomsListUpdate() {
        super.onDirectMessageChatRoomsListUpdate()
        Log.d("List Direct: ", "Update");
    }

    override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
        super.onLiveEventsChunkProcessed(fromToken, toToken)
        Log.d("Event Chunk:" , fromToken + toToken);
    }

    override fun onLiveEvent(event: Event?, roomState: RoomState?) {
        super.onLiveEvent(event, roomState)

        Log.d("Event Type:" , event?.type);

        if (event?.type?.compareTo("m.room.join_rules") == 0) {
            if (event?.roomId != null) {
                roomRepository.insertRoom(event?.roomId);
            }
        }
        if (event?.type?.compareTo("m.room.name") == 0) {
            if (event?.roomId != null)
                roomRepository.insertRoom(event?.roomId);
        }
        if (event?.type?.compareTo("m.room.member") == 0) {
            if (event?.roomId != null)
                roomRepository.insertRoom(event?.roomId);
        }
        if (event?.type?.compareTo("m.room.message") == 0) {
            if (event?.roomId != null) {
                roomRepository.updateRoomFromRemote(event?.roomId);
            }
        }
    }

    /**
     * Handle user status
     */
    override fun onPresenceUpdate(event: Event?, user: User?) {
        super.onPresenceUpdate(event, user)
        user?.let { roomRepository.updateRoomMemberStatus(it.user_id, if (it.presence.compareTo("online") == 0) 1 else 0) }
    }

    override fun getMXEventListener(mxSession: MXSession): MXEventListener {
        this.mxSession = mxSession;
        return this;
    }
}