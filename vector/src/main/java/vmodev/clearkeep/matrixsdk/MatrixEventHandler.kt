package vmodev.clearkeep.matrixsdk

import android.app.Application
import android.util.Log
import com.google.gson.JsonParser
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.User
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
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
        val userAvatarUrl = if (user.avatarUrl.isNullOrEmpty()) "" else user.avatarUrl;
        userRepository.updateUser(user.user_id, user.displayname, userAvatarUrl);
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
        Log.d("Event Chunk:", fromToken + toToken);
    }

    override fun onLiveEvent(event: Event?, roomState: RoomState?) {
        super.onLiveEvent(event, roomState)

        Log.d("EventType:", event?.type);
//        event?.contentAsJsonObject?.let {
//            val content = it.toString();
//            val parser = JsonParser();
//            val contentJson = parser.parse(content).asJsonObject;
//            Log.d("Event Type Convert", contentJson.toString());
//        }


        if (event?.type?.compareTo("m.room.join_rules") == 0) {
            if (event?.roomId != null) {
                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
//                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
                Log.d("EventType", event?.userId + "----" +mxSession!!.myUserId);
            }
        }
        if (event?.type?.compareTo("m.room.name") == 0) {
            if (event?.roomId != null)
                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
//                roomRepository.insertRoom(event?.roomId);
        }
        if (event?.type?.compareTo("m.room.member") == 0) {
            if (event?.roomId != null)
                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
            Log.d("EventType", event?.userId + "----" +mxSession!!.myUserId);
//                roomRepository.insertRoom(event?.roomId);S
        }
        if (event?.type?.compareTo("m.room.message") == 0) {
            if (event?.roomId != null) {
                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
            }
        }
        if (event?.type?.compareTo("m.room.encrypted") == 0) {
            if (event?.roomId != null) {
                roomRepository.updateOrCreateRoomFromRemote(event?.roomId);
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