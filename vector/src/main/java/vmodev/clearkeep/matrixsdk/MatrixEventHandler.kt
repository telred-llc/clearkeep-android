package vmodev.clearkeep.matrixsdk

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonParser
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.rest.model.bingrules.BingRule
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.*
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixEventHandler @Inject constructor(private val application: ClearKeepApplication,
                                             private val userRepository: UserRepository,
                                             private val roomRepository: RoomRepository,
                                             private val signatureRepository: SignatureRepository,
                                             private val keyBackupRepository: KeyBackupRepository
                                             , private val roomUserJoinRepository: RoomUserJoinRepository
                                             , private val messageRepository: MessageRepository)
    : MXEventListener(), IMatrixEventHandler, KeysBackupStateManager.KeysBackupStateListener {
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

        if (event?.type?.compareTo("m.room.join_rules") == 0) {
            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
                        roomRepository.updateLastMessage(it.roomId, it.id);
                    }, {
                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
                    });
                }, {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                });
            };
        }
        if (event?.type?.compareTo("m.room.name") == 0) {
            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
                        roomRepository.updateLastMessage(it.roomId, it.id);
                    }, {
                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
                    });
                }, {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                });
            };
        }
        if (event?.type?.compareTo("m.room.member") == 0) {
            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
                        roomRepository.updateLastMessage(it.roomId, it.id);
                    }, {
                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
                    });
                }, {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                });
            };
        }
        if (event?.type?.compareTo("m.room.message") == 0) {
            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
                        roomRepository.updateLastMessage(it.roomId, it.id);
                    }, {
                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
                    });
                }, {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                });
            };
        }
        if (event?.type?.compareTo("m.room.encrypted") == 0) {
            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
                        roomRepository.updateLastMessage(it.roomId, it.id);
                    }, {
                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
                    });
                }, {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
                });
            };
        }
    }

    /**
     * Handle user status
     */
    override fun onPresenceUpdate(event: Event?, user: User?) {
        super.onPresenceUpdate(event, user)
        user?.let {
            userRepository.updateUserStatus(it.user_id, if (it.presence.compareTo("online") == 0) 1 else 0)
        }
    }

    override fun onBingEvent(event: Event?, roomState: RoomState?, bingRule: BingRule?) {
        super.onBingEvent(event, roomState, bingRule)
    }

    override fun getMXEventListener(mxSession: MXSession): MXEventListener {
        this.mxSession = mxSession;
        mxSession.crypto?.keysBackup?.addListener(this);
        return this;
    }

    protected fun finalize() {
        mxSession!!.crypto?.keysBackup?.removeListener(this);
    }

    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
        keyBackupRepository.updateKeyBackup(mxSession!!.myUserId);
        signatureRepository.updateSignature(mxSession!!.myUserId);
    }
}