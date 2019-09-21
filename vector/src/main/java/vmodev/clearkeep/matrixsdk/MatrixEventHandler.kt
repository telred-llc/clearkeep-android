package vmodev.clearkeep.matrixsdk

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.rest.model.bingrules.BingRule
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.*
import javax.inject.Inject

class MatrixEventHandler @Inject constructor(
        private val application: ClearKeepApplication,
        private val userRepository: UserRepository
        , private val roomRepository: RoomRepository
        , private val signatureRepository: SignatureRepository
        , private val keyBackupRepository: KeyBackupRepository
        , private val roomUserJoinRepository: RoomUserJoinRepository
        , private val messageRepository: MessageRepository
//        , private val roomUserJoinDao: AbstractRoomUserJoinDao
//        , private val matrixService: MatrixService
        , private val roomDao: AbstractRoomDao
        , private val appExecutors: AppExecutors)
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
    }

    override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
        super.onLiveEventsChunkProcessed(fromToken, toToken)
        Log.d("Event Chunk:", fromToken + toToken);
    }

    override fun onLiveEvent(event: Event?, roomState: RoomState?) {

        Log.d("EventType:", event?.type + "--" + event?.roomId);

        when (event?.type) {
            IMatrixEventHandler.M_ROOM_CREATE -> {
                roomRepository.insertRoomToDB(event.roomId).subscribe {
                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribeOn(Schedulers.io()).subscribe();
                };
            }
            IMatrixEventHandler.M_ROOM_JOIN_RULES -> {
                roomRepository.insertRoomToDB(event.roomId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                        }

            }
            IMatrixEventHandler.M_ROOM_MEMBER -> {
                roomRepository.updateRoomName(event.roomId).subscribeOn(Schedulers.io()).subscribe ();
            }
            else -> {
                event?.let {
                    roomRepository.updateRoomName(event.roomId).subscribeOn(Schedulers.io()).subscribe ();
                }
            }
        }

//        if (event?.type?.compareTo("m.room.join_rules") == 0) {
//            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
//                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
//                        roomRepository.updateLastMessage(it.roomId, it.id);
//                    }, {
//                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
//                    });
//                }, {
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//
//                });
//            };
//        }
//        if (event?.type?.compareTo("m.room.name") == 0) {
//            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
//                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
//                        roomRepository.updateLastMessage(it.roomId, it.id);
//                    }, {
//                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
//                    });
//                }, {
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                });
//            };
//        }
//        if (event?.type?.compareTo("m.room.member") == 0) {
//            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
//                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
//                        roomRepository.updateLastMessage(it.roomId, it.id);
//                    }, {
//                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
//                    });
//                }, {
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                });
//            };
//        }
//        if (event?.type?.compareTo("m.room.message") == 0) {
//            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
//                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
//                        roomRepository.updateLastMessage(it.roomId, it.id);
//                    }, {
//                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
//                    });
//                }, {
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                });
//            };
//        }
//        if (event?.type?.compareTo("m.room.encrypted") == 0) {
//            roomRepository.updateOrCreateRoomFromRemoteRx(event.roomId).subscribe {
//                userRepository.updateOrCreateNewUserFromRemoteRx(event.roomId).subscribe({
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                    messageRepository.getLastMessageOfRoom(event.roomId).subscribe({
//                        roomRepository.updateLastMessage(it.roomId, it.id);
//                    }, {
//                        Toast.makeText(application, "Can't get last message", Toast.LENGTH_SHORT).show();
//                    });
//                }, {
//                    roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId).subscribe();
//                });
//            };
//        }
    }

    /**
     * Handle user status
     */
    override fun onPresenceUpdate(event: Event?
                                  , user: User
            ?
    ) {
        super.onPresenceUpdate(event, user)
        user?.let {
            userRepository.updateUserStatus(it.user_id, if (it.presence.compareTo("online") == 0) 1 else 0)
        }
    }

    override fun onBingEvent(event: Event?
                             , roomState: RoomState
            ?
                             , bingRule: BingRule
            ?
    ) {
        super.onBingEvent(event, roomState, bingRule)
    }

    override fun getMXEventListener(mxSession: MXSession)
            : MXEventListener {
        this.mxSession = mxSession;
        mxSession.crypto?.keysBackup?.removeListener(this);
        mxSession.crypto?.keysBackup?.addListener(this);
        return this;
    }

    protected fun finalize() {
        mxSession!!.crypto?.keysBackup?.removeListener(this);
    }

    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState
    ) {
        keyBackupRepository.updateKeyBackup(mxSession!!.myUserId);
        signatureRepository.updateSignature(mxSession!!.myUserId);
    }
}