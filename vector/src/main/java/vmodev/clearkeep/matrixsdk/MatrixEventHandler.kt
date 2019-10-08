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
import vmodev.clearkeep.ultis.matrixUrlToRealUrl
import vmodev.clearkeep.ultis.toMessage
import vmodev.clearkeep.ultis.toRoomCreate
import vmodev.clearkeep.ultis.toRoomInvite
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.workermanager.interfaces.IUpdateDatabaseFromMatrixEvent
import javax.inject.Inject

class MatrixEventHandler @Inject constructor(
        private val application: ClearKeepApplication,
        private val userRepository: UserRepository
        , private val roomRepository: RoomRepository
        , private val signatureRepository: SignatureRepository
        , private val keyBackupRepository: KeyBackupRepository
        , private val roomUserJoinRepository: RoomUserJoinRepository
        , private val messageRepository: MessageRepository
        , private val roomDao: AbstractRoomDao
        , private val appExecutors: AppExecutors
        , private val updateDatabaseFromMatrixEvent: IUpdateDatabaseFromMatrixEvent)
    : MXEventListener(), IMatrixEventHandler, KeysBackupStateManager.KeysBackupStateListener {
    private var mxSession: MXSession? = null;
    override fun onAccountDataUpdated() {
        super.onAccountDataUpdated()
        val user = mxSession!!.myUser;
        val userAvatarUrl = mxSession!!.contentManager.getDownloadableUrl(user.avatarUrl, false);
        userRepository.updateUser(user.user_id, user.displayname, if (userAvatarUrl.isNullOrEmpty()) "" else userAvatarUrl);
    }

    override fun onAccountInfoUpdate(myUser: MyUser?) {
        super.onAccountInfoUpdate(myUser)
        myUser?.let {
            val avatarUrl = mxSession!!.contentManager.getDownloadableUrl(it.avatarUrl, false);
            userRepository.updateUser(it.user_id, myUser.displayname, if (avatarUrl.isNullOrEmpty()) "" else avatarUrl);
        }
    }

    override fun onDirectMessageChatRoomsListUpdate() {
        super.onDirectMessageChatRoomsListUpdate()
    }

    override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
        super.onLiveEventsChunkProcessed(fromToken, toToken)
        Log.d("Event Chunk:", fromToken + toToken);
    }

    override fun onLiveEvent(event: Event?, roomState: RoomState?) {
        Log.d("EventType", event?.type + "--" + event?.roomId);
        event?.let { e ->
            when (event.type) {
                IMatrixEventHandler.M_ROOM_CREATE -> {
                    roomState?.let { rs ->
                        rs.toRoomCreate(mxSession)?.let {
                            roomRepository.insertRoomInvite(it).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                        messageRepository.insertMessage(event.toMessage()).subscribeOn(Schedulers.io())
                                                .subscribe { roomRepository.updateLastMessage(event.roomId, event.eventId) }
                                        roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread()).subscribe()
                                    }
                        }
                    }
                }
                IMatrixEventHandler.M_ROOM_JOIN_RULES -> {
                    roomState?.let { rs ->
                        Log.d("UpdateRoom", event.contentJson.toString())
                        val selfMember = rs.getMember(mxSession!!.myUserId);
                        Log.d("UpdateRoom", selfMember.isDirect.toString());
                        rs.toRoomInvite(mxSession)?.let {
                            roomRepository.insertRoomInvite(it).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                        messageRepository.insertMessage(event.toMessage()).subscribeOn(Schedulers.io())
                                                .subscribe { roomRepository.updateLastMessage(event.roomId, event.eventId).subscribe() }
                                        roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread()).subscribe()
                                    }
                        }
                    }
                }
                IMatrixEventHandler.M_ROOM_MEMBER -> {
                    messageRepository.insertMessage(event.toMessage())
                            .subscribeOn(Schedulers.io()).subscribe {
                                roomRepository.updateLastMessage(e.roomId, e.eventId).subscribe();
                            };
                }
                IMatrixEventHandler.M_ROOM_NAME -> {
                    val name = event.contentJson.asJsonObject.get("name").asString;
                    roomRepository.updateRoomName(event.roomId, name).subscribeOn(Schedulers.io()).subscribe();
                }
                else -> {
                    messageRepository.insertMessage(event.toMessage())
                            .subscribeOn(Schedulers.io()).subscribe {
                                roomRepository.updateLastMessage(e.roomId, e.eventId).subscribe();
                            };
                }
            }
        }
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
        mxSession?.crypto?.keysBackup?.removeListener(this);
    }

    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState
    ) {
        keyBackupRepository.updateKeyBackup(mxSession!!.myUserId);
        signatureRepository.updateSignature(mxSession!!.myUserId);
    }
}