package vmodev.clearkeep.matrixsdk

import android.text.TextUtils
import android.util.Log
import im.vector.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.rest.model.bingrules.BingRule
import org.matrix.androidsdk.rest.model.sync.AccountDataElement
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.*
import vmodev.clearkeep.ultis.*
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
    private var mxSession: MXSession? = null
    private val KEY_MEMBERSHIP = "membership"
    override fun onAccountDataUpdated(accountDataElement: AccountDataElement?) {
        super.onAccountDataUpdated(accountDataElement)
        val user = mxSession!!.myUser
        val userAvatarUrl = mxSession!!.contentManager.getDownloadableUrl(user.avatarUrl, false)
        userRepository.updateUser(user.user_id, user.displayname, if (userAvatarUrl.isNullOrEmpty()) "" else userAvatarUrl)
    }

    override fun onAccountInfoUpdate(myUser: MyUser?) {
        super.onAccountInfoUpdate(myUser)
        myUser?.let {
            val avatarUrl = mxSession!!.contentManager.getDownloadableUrl(it.avatarUrl, false)
            userRepository.updateUser(it.user_id, myUser.displayname, if (avatarUrl.isNullOrEmpty()) "" else avatarUrl)
        }
    }

    override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
        super.onLiveEventsChunkProcessed(fromToken, toToken)
        Log.d("Event Chunk:", fromToken + toToken)
    }

    override fun onLiveEvent(event: Event?, roomState: RoomState?) {
        Debug.e("--- event: ${event?.type}\n--- Content ---: ${event?.toString()}")
        event?.let { e ->
            when (event.type) {
                Event.EVENT_TYPE_STATE_ROOM_CREATE -> {
                    roomState?.let { rs ->
                        rs.toRoomCreate(mxSession)?.let {
                            roomRepository.insertRoomInvite(it).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                        messageRepository.insertMessage(event.toMessage()).subscribeOn(Schedulers.io())
                                                .subscribe {
                                                    roomRepository.updateLastMessage(event.roomId, event.eventId).subscribeOn(Schedulers.io()).subscribe()
                                                }
                                        roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread()).subscribe()
                                    }
                        }
                    }
                }
                Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES -> {
                    roomState?.let { rs ->
                        rs.toRoomInvite(mxSession)?.let {
                            roomRepository.insertRoomInvite(it).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                        messageRepository.insertMessage(event.toMessage()).subscribeOn(Schedulers.io())
                                                .subscribe {
                                                    roomRepository.updateLastMessage(event.roomId, event.eventId).subscribe()
                                                }
                                        roomUserJoinRepository.updateOrCreateRoomUserJoinRx(event.roomId, mxSession!!.myUserId)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread()).subscribe()
                                    }
                        }
                    }
                }
                Event.EVENT_TYPE_STATE_ROOM_MEMBER -> {
                    Debug.e("--- event: m.room.member")
                    userRepository.updateUser(event.sender).subscribeOn(Schedulers.io()).subscribe {
                        messageRepository.insertMessage(event.toMessage())
                                .subscribeOn(Schedulers.io()).subscribe {
                                    val contentObject = event.contentJson.asJsonObject
                                    roomRepository.updateLastMessage(e.roomId, e.eventId).subscribeOn(Schedulers.io()).subscribe()
                                    if (contentObject.has(KEY_MEMBERSHIP) && TextUtils.equals(contentObject.get(KEY_MEMBERSHIP).asString, RoomMember.MEMBERSHIP_INVITE)
                                            && contentObject.has("is_direct") && contentObject.get("is_direct").asBoolean) {
                                        roomState?.let {
                                            val selfMember = it.getMember(mxSession!!.myUserId)
                                            if (selfMember != null) {
                                                roomRepository.updateRoomType(event.roomId, 1).subscribeOn(Schedulers.io()).subscribe()
                                                roomRepository.updateRoomName(event.roomId, contentObject.get("displayname").asString).subscribeOn(Schedulers.io()).subscribe()
                                                if (contentObject.has("avatar_url") && !contentObject.get("avatar_url").asString.isNullOrEmpty()) {
                                                    contentObject.get("avatar_url").asString.matrixUrlToRealUrl(mxSession)?.let {
                                                        roomRepository.updateRoomAvatar(event.roomId, it).subscribeOn(Schedulers.io()).subscribe()
                                                    }
                                                } else {

                                                }
                                            } else {
                                                val member = it.getMember(event.sender)
                                                roomRepository.updateRoomType(event.roomId, 1 or 64).subscribeOn(Schedulers.io()).subscribe()
                                                roomRepository.updateRoomName(event.roomId, String.format(application.resources.getString(R.string.room_displayname_invite_from), member.displayname))
                                                        .subscribeOn(Schedulers.io()).subscribe()
                                                member.avatarUrl.matrixUrlToRealUrl(mxSession)?.let {
                                                    roomRepository.updateRoomAvatar(event.roomId, it)
                                                            .subscribeOn(Schedulers.io()).subscribe()
                                                }
                                            }
                                        }
                                    }
                                }
                    }
                }
                Event.EVENT_TYPE_STATE_ROOM_NAME -> {
                    val name = event.contentJson.asJsonObject.get("name").asString
                    roomRepository.updateRoomName(event.roomId, name).subscribeOn(Schedulers.io()).subscribe({
                        Debug.e("--- Update room name success")
                    }, {
                        Debug.e("--- Error: ${it.message}")
                    })
                }
                IMatrixEventHandler.M_ROOM_POWER_LEVELS -> {

                }
                Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY -> {

                }
                Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS -> {

                }
                Event.EVENT_TYPE_TYPING -> {

                }
                Event.EVENT_TYPE_MESSAGE, Event.EVENT_TYPE_MESSAGE_ENCRYPTED -> {
                    messageRepository.insertMessage(event.toMessage())
                            .subscribeOn(Schedulers.io()).subscribe {
                                roomRepository.updateLastMessage(e.roomId, e.eventId).subscribe()
                            }
                    if (!event.sender.equals(mxSession!!.myUserId)) {
                        roomRepository.updateRoomNotificationCount(e.roomId).subscribeOn(Schedulers.io()).subscribe()
                    } else {
                        Debug.e("--- even owner")
                    }
                }
                else -> {
                    Debug.e("--- event khong ro: $event")
                }
            }
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
        Log.d("hang_call", event?.type + "--" + event?.roomId)
    }

    override fun getMXEventListener(mxSession: MXSession): MXEventListener {
        this.mxSession = mxSession
        mxSession.crypto?.keysBackup?.removeListener(this)
        mxSession.crypto?.keysBackup?.addListener(this)
        return this
    }

    protected fun finalize() {
        mxSession?.crypto?.keysBackup?.removeListener(this)
    }

    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
        keyBackupRepository.updateKeyBackup(mxSession!!.myUserId)
        signatureRepository.updateSignature(mxSession!!.myUserId)
    }

}