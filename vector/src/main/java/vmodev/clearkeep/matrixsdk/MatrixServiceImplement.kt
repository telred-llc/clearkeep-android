package vmodev.clearkeep.matrixsdk

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.util.HomeRoomsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.internal.operators.observable.ObservableAll
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.BingRulesManager
import org.matrix.androidsdk.core.Debug
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.callback.SuccessCallback
import org.matrix.androidsdk.core.callback.SuccessErrorCallback
import org.matrix.androidsdk.core.listeners.ProgressListener
import org.matrix.androidsdk.core.listeners.StepProgressListener
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXCRYPTO_ALGORITHM_MEGOLM
import org.matrix.androidsdk.crypto.MXDecryptionException
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import org.matrix.androidsdk.crypto.data.MXEncryptEventContentResult
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.crypto.keysbackup.MegolmBackupCreationInfo
import org.matrix.androidsdk.crypto.model.keys.KeysVersion
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.data.RoomTag
import org.matrix.androidsdk.listeners.IMXMediaUploadListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsResponse
import org.matrix.androidsdk.rest.model.search.SearchResponse
import org.matrix.androidsdk.rest.model.search.SearchResult
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse
import org.matrix.androidsdk.rest.model.sync.RoomResponse
import org.matrix.androidsdk.rest.model.sync.RoomSync
import org.matrix.androidsdk.rest.model.sync.RoomSyncState
import org.matrix.androidsdk.rest.model.sync.RoomSyncTimeline
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.enums.EventTypeEnum
import vmodev.clearkeep.jsonmodels.CallContent
import vmodev.clearkeep.jsonmodels.MessageContent
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.rests.ClearKeepApis
import vmodev.clearkeep.rests.IRetrofit
import vmodev.clearkeep.rests.models.requests.EditMessageRequest
import vmodev.clearkeep.rests.models.responses.EditMessageResponse
import vmodev.clearkeep.rests.models.responses.FeedbackResponse
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import vmodev.clearkeep.rests.models.responses.VersionAppInfoResponse
import vmodev.clearkeep.ultis.*
import vmodev.clearkeep.viewmodelobjects.*
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

@Singleton
class MatrixServiceImplement @Inject constructor(private val application: ClearKeepApplication
                                                 , @Named(value = IRetrofit.BASE_URL_HOME_SERVER) private val apis: ClearKeepApis
                                                 , @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER) private val apisClearKeep: ClearKeepApis
                                                 , private val messageDao: AbstractMessageDao, private val roomDao: AbstractRoomDao
                                                 , private val roomUserJoin: AbstractRoomUserJoinDao) : MatrixService {

    //    @Inject
    private var session: MXSession? = null
    private var homeRoomsViewModel: HomeRoomsViewModel? = null
    private val funcs: Array<Function<HomeRoomsViewModel.Result, List<Room>>> = Array(255, init = { Function { t: HomeRoomsViewModel.Result -> t.directChats } })
    private fun setMXSession() {
        if (session != null) {
            if (session!!.isAlive)
                return
        }


        session = Matrix.getInstance(application).defaultSession
        homeRoomsViewModel = HomeRoomsViewModel(session!!)
        funcs[1] = Function { t: HomeRoomsViewModel.Result -> t.directChats }
        funcs[2] = Function { t: HomeRoomsViewModel.Result -> t.otherRooms }
        funcs[65] = Function { t: HomeRoomsViewModel.Result -> getListDirectMessageInvite() }
        funcs[66] = Function { t: HomeRoomsViewModel.Result -> getListRoomMessageInvite() }
        funcs[129] = Function { t: HomeRoomsViewModel.Result -> t.getDirectChatsWithFavorites() }
        funcs[130] = Function { t: HomeRoomsViewModel.Result -> t.getOtherRoomsWithFavorites() }
    }

    @SuppressLint("CheckResult")
    override fun getListDirectMessageConversation(): LiveData<List<Room>> {
        setMXSession()
        val liveData = MutableLiveData<List<Room>>()
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update()
                val result = homeRoomsViewModel!!.result
                if (result != null) {
                    emitter.onNext(result.directChats)
                    emitter.onComplete()
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            kotlin.run {
                liveData.value = t
            }
        }
        return liveData
    }

    @SuppressLint("CheckResult")
    override fun getListRoomConversation(): LiveData<List<Room>> {
        setMXSession()
        val liveData = MutableLiveData<List<Room>>()
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update()
                val result = homeRoomsViewModel!!.result
                if (result != null) {
                    emitter.onNext(result.otherRooms)
                    emitter.onComplete()
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            kotlin.run {
                liveData.value = t
            }
        }
        return liveData
    }

    @SuppressLint("CheckResult")
    override fun getListFavouriteConversation(): LiveData<List<Room>> {
        setMXSession()
        val liveData = MutableLiveData<List<Room>>()
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update()
                val result = homeRoomsViewModel!!.result
                if (result != null) {
                    emitter.onNext(result.favourites)
                    emitter.onComplete()
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            run {
                liveData.value = t
            }
        }
        return liveData
    }

    @SuppressLint("CheckResult")
    override fun getListContact(): LiveData<List<Room>> {
        setMXSession()
        val liveData = MutableLiveData<List<Room>>()
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update()
                val result = homeRoomsViewModel!!.result
                if (result != null) {
                    emitter.onNext(result.getDirectChatsWithFavorites())
                    emitter.onComplete()
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? -> kotlin.run { liveData.value = t; } }
        return liveData
    }

    @SuppressLint("CheckResult")
    override fun getUser(): Observable<User> {
        setMXSession()
        val myUser = session!!.myUser
        return Observable.create { emitter ->
            run {
                if (myUser != null) {
                    var avatar = ""
                    var result = session!!.contentManager.getDownloadableThumbnailUrl(myUser.avatarUrl, 100, 100, "")
                    result?.let { avatar = result }
                    val user = User(name = myUser.displayname, id = myUser.user_id, avatarUrl = avatar, status = if (myUser.isActive) 1 else 0)
                    emitter.onNext(user)
                    emitter.onComplete()
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }
    }

    override fun getListRoom(filters: Array<Int>): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>> {
        setMXSession()
        return Observable.create { emitter ->
            val listRoom = mutableListOf<vmodev.clearkeep.viewmodelobjects.Room>()
            if (homeRoomsViewModel != null && homeRoomsViewModel!!.result != null) {
                homeRoomsViewModel!!.update()
                val rooms = ArrayList<Room>()
                for (filter in filters) {
                    rooms.addAll(funcs[filter].apply(homeRoomsViewModel!!.result))
                }
                for ((index, value) in rooms.withIndex()) {
//                    addLastMessage(value).subscribeOn(Schedulers.io()).subscribe({
                    listRoom.add(matrixRoomToRoomWithNonMessageAndUserCreated(value))
//                    addLastMessage(value.roomId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
//                        if (index >= rooms.size) {
//                            emitter.onNext(listRoom);
//                            emitter.onComplete();
//                        }
//                    }, {
//                        if (index >= rooms.size) {
//                            emitter.onNext(listRoom);
//                            emitter.onComplete();
//                        }
//                    })
                }
//                Observable.timer(5, TimeUnit.SECONDS).subscribe {
//                    messageDao.getMessageWithRoomId(rooms[0].roomId).forEach {
//                        Log.d("Message", it.encryptedContent)
//                        roomDao.getRoomWithMessageId(it.id).forEach {
//                            Log.d("Message", it.name)
//                        }
//                    }
//                    roomUserJoin.getListRoomListUserWithFilterTest(1, 65).forEach {
//                        Log.d("Message", it.room?.name)
//                    }
                emitter.onNext(listRoom)
                emitter.onComplete()
//                }
            } else {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun getRoomWithId(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return ObservableAll.create { emitter ->
            val room = session!!.dataHandler.getRoom(id)
            if (room != null) {
                emitter.onNext(matrixRoomToRoom(room))
                emitter.onComplete()
            } else {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun getRoomWithIdForCreate(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return ObservableAll.create { emitter ->
            var room: Room? = session!!.dataHandler.getRoom(roomId)
            room?.let {
                emitter.onNext(matrixRoomToRoomWithNonMessageAndUserCreated(room))
                emitter.onComplete()
            } ?: run {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun getListRoomWithTwoFilter(filterOne: Int, filterTwo: Int): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>> {
        setMXSession()
        return Observable.create<List<vmodev.clearkeep.viewmodelobjects.Room>> { emitter ->
            kotlin.run {

            }
        }
    }

    private fun getListDirectMessageInvite(): List<Room> {
        val roomSummaries = session!!.dataHandler.store!!.summaries
        val rooms = ArrayList<Room>()
        roomSummaries.forEach { t: RoomSummary? ->
            kotlin.run {
                val room = session!!.dataHandler.store!!.getRoom(t?.roomId)
                if (room != null && !room.isConferenceUserRoom && room.isInvited && room.isDirectChatInvitation) {
                    rooms.add(room)
                }
            }
        }
        return rooms
    }

    private fun getListRoomMessageInvite(): List<Room> {
        val roomSummaries = session!!.dataHandler.store!!.summaries
        val rooms = ArrayList<Room>()
        roomSummaries.forEach { t: RoomSummary? ->
            kotlin.run {
                val room = session!!.dataHandler.store!!.getRoom(t?.roomId)
                if (room != null && !room.isConferenceUserRoom && room.isInvited && !room.isDirectChatInvitation) {
                    rooms.add(room)
                }
            }
        }
        return rooms
    }

    override fun joinRoom(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            kotlin.run {
                val room = session!!.dataHandler.store!!.getRoom(id)
                if (room != null) {
                    session!!.joinRoom(room.roomId, object : ApiCallback<String> {
                        override fun onSuccess(p0: String?) {
                            emitter.onNext(matrixRoomToRoom(room))
                            emitter.onComplete()
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(message = p0?.message))
                            emitter.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(message = p0?.message))
                            emitter.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(message = p0?.message))
                            emitter.onComplete()
                        }
                    })
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            }
        }
    }

    override fun leaveRoom(id: String): Observable<String> {
        setMXSession()
        return Observable.create<String> { emitter ->
            val room = session!!.dataHandler.getRoom(id)
            room.let { room ->
                room.leave(object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        val roomAfter = session!!.dataHandler.getRoom(id)
                        roomAfter.let { r ->
                            r.leave(object : ApiCallback<Void> {
                                override fun onSuccess(p0: Void?) {
                                    emitter.onNext(id)
                                    emitter.onComplete()
                                }

                                override fun onUnexpectedError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }

                                override fun onMatrixError(p0: MatrixError?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }

                                override fun onNetworkError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }
                            })
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            }
        }
    }

    private fun matrixRoomToRoom(room: Room): vmodev.clearkeep.viewmodelobjects.Room {
        var sourcePrimary = 1// = if (room.isDirect) 0b00000001 else 0b00000010;
        if (room.isInvited) {
            sourcePrimary = if (room.isDirectChatInvitation) 0b00000001 else 0b00000010
        } else {
            sourcePrimary = if (room.isDirect) 0b00000001 else 0b00000010
        }
        val sourceSecondary = if (room.isInvited) 0b01000000 else 0b00000000
        val sourceThird = if ((room.accountData?.keys
                        ?: emptySet()).contains(RoomTag.ROOM_TAG_FAVOURITE)) 0b10000000 else 0b00000000
        var timeUpdateLong: Long = 0
        var messageId: String? = null
        room.roomSummary?.let { roomSummary ->
            val event = roomSummary.latestReceivedEvent
            timeUpdateLong = event.originServerTs
            messageId = event.eventId
        }

        var userCreated: String? = null
        room.state.roomCreateContent?.creator?.let { userCreated = it }

        val notificationState = when (session!!.dataHandler.bingRulesManager.getRoomNotificationState(room.roomId)) {
            BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY -> 0x01
            BingRulesManager.RoomNotificationState.ALL_MESSAGES -> 0x02
            BingRulesManager.RoomNotificationState.MENTIONS_ONLY -> 0x04
            BingRulesManager.RoomNotificationState.MUTE -> 0x08
        }
        val avatar: String? = if (room.avatarUrl.isNullOrEmpty()) "" else session!!.contentManager.getDownloadableThumbnailUrl(room.avatarUrl, 100, 100, "")
        val roomObj: vmodev.clearkeep.viewmodelobjects.Room = Room(id = room.roomId, name = room.getRoomDisplayName(application)
                , type = (sourcePrimary or sourceSecondary or sourceThird), avatarUrl = avatar!!, notifyCount = room.notificationCount
                , topic = if (room.topic.isNullOrEmpty()) "" else room.topic, version = 1, highlightCount = room.highlightCount, messageId = messageId
                , encrypted = if (room.isEncrypted) 1 else 0, notificationState = notificationState.toByte(), userCreated = userCreated)
        return roomObj
    }

    private fun matrixRoomToRoomWithNonMessageAndUserCreated(room: Room): vmodev.clearkeep.viewmodelobjects.Room {
        var sourcePrimary = 1// = if (room.isDirect) 0b00000001 else 0b00000010;
        if (room.isInvited) {
            sourcePrimary = if (room.isDirectChatInvitation) 0b00000001 else 0b00000010
        } else {
            sourcePrimary = if (room.isDirect) 0b00000001 else 0b00000010
        }
        val sourceSecondary = if (room.isInvited) 0b01000000 else 0b00000000
        val sourceThird = if ((room.accountData?.keys
                        ?: emptySet()).contains(RoomTag.ROOM_TAG_FAVOURITE)) 0b10000000 else 0b00000000
        var timeUpdateLong: Long = 0
//        var messageId: String? = null
//        room.roomSummary?.let { roomSummary ->
//            val event = roomSummary.latestReceivedEvent;
//            timeUpdateLong = event.originServerTs;
//            messageId = event.eventId;
//        }
//
//        var userCreated : String? = null;
//        room.state?.roomCreateContent?.creator?.let { userCreated = it }

//        val notificationState = when (session!!.dataHandler.bingRulesManager.getRoomNotificationState(room.roomId)) {
//            BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY -> 0x01;
//            BingRulesManager.RoomNotificationState.ALL_MESSAGES -> 0x02;
//            BingRulesManager.RoomNotificationState.MENTIONS_ONLY -> 0x04;
//            BingRulesManager.RoomNotificationState.MUTE -> 0x08;
//        }
        val avatar: String? = if (room.avatarUrl.isNullOrEmpty()) "" else session!!.contentManager.getDownloadableThumbnailUrl(room.avatarUrl, 100, 100, "")
        val roomObj: vmodev.clearkeep.viewmodelobjects.Room = Room(id = room.roomId, name = room.getRoomDisplayName(application)
                , type = (sourcePrimary or sourceSecondary or sourceThird), avatarUrl = avatar!!, notifyCount = room.notificationCount
                , topic = if (room.topic.isNullOrEmpty()) "" else room.topic, version = 1, highlightCount = room.highlightCount, messageId = null
                , encrypted = if (room.isEncrypted) 1 else 0, notificationState = 0x01, userCreated = null)
        return roomObj
    }

    override fun getLastMessageOfRoom(roomId: String): Observable<Message> {
        val room = session!!.dataHandler.getRoom(roomId)
        return Observable.create { emitter ->
            room.roomSummary?.let {
                val event = it.latestReceivedEvent
                val message = Message(id = event.eventId, userId = event.sender, roomId = event.roomId, encryptedContent = event.content.toString(), messageType = event.type, createdAt = event.originServerTs)
                emitter.onNext(message)
                emitter.onComplete()
            } ?: run {
                emitter.onError(Throwable("RoomSummary is null"))
                emitter.onComplete()
            }
        }
    }

    private fun getCurrentUser(): User {
        val myUser = session!!.myUser
        var avatar = ""
        if (myUser.avatarUrl.isNullOrEmpty() || myUser == null) {
            avatar = ""
        } else {
            var result = session!!.contentManager.getDownloadableThumbnailUrl(myUser.avatarUrl, 100, 100, "")
            result?.let { avatar = result }
        }
        return User(myUser.displayname, myUser.user_id, avatar, 0)
    }

    @SuppressLint("CheckResult")
    private fun asyncUpdateRoomMember(room: Room): Observable<List<User>> {
        return Observable.create { emitter ->
            val users = ArrayList<User>()
            room.getMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    p0?.forEach { t: RoomMember? ->
                        t?.userId?.let {
                            var avatar = ""
                            if (t.avatarUrl.isNullOrEmpty() || t == null) {
                                avatar = ""
                            } else {
                                var result = session!!.contentManager.getDownloadableThumbnailUrl(t.avatarUrl, 100, 100, "")
                                result?.let { avatar = result }
                            }
                            users.add(User(name = t.name, status = 0, avatarUrl = avatar, id = t.userId))
                        }
                    }
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }
            })
        }
    }

    override fun findListUser(keyword: String): Observable<List<User>> {
        setMXSession()
        val filter: Set<String> = HashSet<String>()

        return Observable.create<List<User>> { emitter ->
            kotlin.run {
                session!!.searchUsers(keyword, 100, filter, object : ApiCallback<SearchUsersResponse> {
                    val users = ArrayList<User>()
                    override fun onSuccess(p0: SearchUsersResponse?) {
                        if (p0 != null) {
                            p0.results?.forEach { t: org.matrix.androidsdk.rest.model.User? ->
                                kotlin.run {
                                    var avatar = ""
                                    if (t?.avatarUrl.isNullOrEmpty() || t == null) {
                                        avatar = ""
                                    } else {
                                        var result = session!!.contentManager.getDownloadableThumbnailUrl(t.avatarUrl, 100, 100, "")
                                        result?.let { avatar = result }
                                    }
                                    t?.let { user ->
                                        users.add(User(id = user.user_id, name = if (user.displayname.isNullOrEmpty()) "Riot-bot" else user.displayname, avatarUrl = avatar, status = if (user.isActive) 1 else 0))
                                    }
                                }
                            }
                            emitter.onNext(users)
                            emitter.onComplete()
                        } else {
                            emitter.onNext(users)
                            emitter.onComplete()
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onNext(users)
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onNext(users)
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onNext(users)
                        emitter.onComplete()
                    }
                })
            }
        }
    }

    override fun <T> findListMessageText(keyword: String, typeOfClass: Class<T>): Observable<List<T>> {
        setMXSession()
        return Observable.create<List<T>> { emitter ->
            session!!.searchMessagesByText(keyword, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val searchResults = ArrayList<T>()
                    p0?.searchCategories?.roomEvents?.results?.forEach { t: SearchResult? ->
                        if (typeOfClass == MessageSearchText::class.java) {
                            if (t?.result?.type?.compareTo("m.room.message") == 0) {
                                val result: SearchMessageByTextResult = Gson().fromJson(t.result?.content, SearchMessageByTextResult::class.java)
                                val room = matrixRoomToRoom(session!!.dataHandler.getRoom(t.result.roomId))

                                searchResults.add(MessageSearchText(name = room.name, avatarUrl = room.avatarUrl, content = result.body, roomId = room.id, updatedDate = 0) as T)
                            }
                        } else {
                            if (t?.result?.type?.compareTo("m.room.name") == 0) {
//                                val result: SearchMessageByTextResult = Gson().fromJson(t?.result?.content, SearchMessageByTextResult::class.java);
//                                val room = matrixRoomToRoom(session!!.dataHandler.getRoom(t?.result.roomId));

                                searchResults.add(t.result.roomId as T)
                            }
                        }
                    }
                    emitter.onNext(searchResults)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun createNewDirectMessage(userId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            directChatRoomExist(userId).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe({ t ->
                kotlin.run {
                    t?.let { s ->
                        val room = session!!.dataHandler.getRoom(s)
                        emitter.onNext(matrixRoomToRoom(room))
                        emitter.onComplete()
                    }
                }
            }, { t ->
                kotlin.run {
                    session!!.createDirectMessageRoom(userId, MXCRYPTO_ALGORITHM_MEGOLM, object : ApiCallback<String> {
                        override fun onSuccess(p0: String?) {
                            p0?.let { s ->
                                val room = session!!.dataHandler.getRoom(s)
                                emitter.onNext(matrixRoomToRoomWithNonMessageAndUserCreated(room))
                                emitter.onComplete()
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }
                    })
                }
            })
        }
    }

    private fun directChatRoomExist(userId: String): Observable<String> {
        return Observable.create<String> { emitter ->
            kotlin.run {
                val store = session!!.dataHandler.store!!
                val directChatRoomDict: Map<String, List<String>>
                if (store.directChatRoomsDict != null) {
                    store.directChatRoomsDict?.let { mutableMap ->
                        kotlin.run {
                            directChatRoomDict = HashMap(mutableMap)
                            if (directChatRoomDict.containsKey(userId)) {
                                var roomsList = ArrayList<String>()
                                directChatRoomDict[userId]?.let {
                                    roomsList = ArrayList(it)
                                }
                                var findedRoom = false
                                roomsList.forEach { rl: String? ->
                                    kotlin.run {
                                        rl?.let { s ->
                                            val room = session!!.dataHandler.getRoom(rl, false)
                                            if (room != null) {
                                                room.let { r ->
                                                    kotlin.run {
                                                        if (r.isReady && !r.isInvited && !r.isLeaving) {
                                                            findedRoom = true
                                                            room.getActiveMembersAsync(object : SimpleApiCallback<List<RoomMember>>() {
                                                                override fun onSuccess(p0: List<RoomMember>?) {
                                                                    p0?.let { list ->
                                                                        list.forEach { t: RoomMember? ->
                                                                            t?.let { roomMember ->
                                                                                if (TextUtils.equals(roomMember.userId, userId)) {
                                                                                    emitter.onNext(s)
                                                                                    emitter.onComplete()
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    emitter.onError(Throwable("Room is not exist"))
                                                                    emitter.onComplete()
                                                                }
                                                            })
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!findedRoom) {
                                    emitter.onError(Throwable("Room is not exist"))
                                    emitter.onComplete()
                                }
                            } else {
                                emitter.onError(Throwable("Room is not exist"))
                                emitter.onComplete()
                            }
                        }
                    }
                } else {
                    emitter.onError(Throwable("Room don't exist"))
                    emitter.onComplete()
                }
            }
        }
    }

    override fun createNewRoom(name: String, topic: String, visibility: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            session!!.createRoom(name, topic, visibility, null, MXCRYPTO_ALGORITHM_MEGOLM, object : ApiCallback<String> {
                override fun onSuccess(p0: String?) {
                    p0?.let { s ->
                        val room = session!!.dataHandler.getRoom(s)
                        emitter.onNext(matrixRoomToRoomWithNonMessageAndUserCreated(room))
                        emitter.onComplete()
                    }
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun inviteUsersToRoom(roomId: String, userIds: List<String>): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId)
            room.invite(session, userIds, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext(matrixRoomToRoom(room))
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun findMediaFiles(keyword: String): Observable<List<String>> {
        setMXSession()
        return Observable.create<List<String>> { emitter ->
            session!!.searchMediaByName(keyword, null, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val results = ArrayList<String>()

                    val result = p0?.searchCategories?.roomEvents?.results
                    Log.d("Result Size: ", result?.size.toString())

                    emitter.onNext(results)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun addToFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId)
            room.let { r ->
                var oldTag: String? = null
                val roomAccount = r.accountData
                roomAccount?.let { roomAccountData ->
                    if (roomAccountData.hasTags())
                        oldTag = roomAccountData.keys?.iterator()?.next()
                }
                var tagOrder: Double = 0.0
                tagOrder = session!!.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, "m.favourite")
                r.replaceTag(oldTag, "m.favourite", tagOrder, object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        r.replaceTag(oldTag, "m.favourite", tagOrder, object : ApiCallback<Void> {
                            override fun onSuccess(p0: Void?) {
                                emitter.onNext(matrixRoomToRoom(r))
                                emitter.onComplete()
                            }

                            override fun onUnexpectedError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }

                            override fun onMatrixError(p0: MatrixError?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }

                            override fun onNetworkError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }
                        })
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            }
        }
    }

    override fun removeFromFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession()
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId)
            room.let { r ->
                var oldTag: String? = null
                val roomAccount = r.accountData
                roomAccount?.let { roomAccountData ->
                    if (roomAccountData.hasTags())
                        oldTag = roomAccountData.keys?.iterator()?.next()
                }
                var tagOrder: Double = 0.0
                tagOrder = session!!.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, null)
                r.replaceTag(oldTag, null, tagOrder, object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        r.replaceTag(oldTag, null, tagOrder, object : ApiCallback<Void> {
                            override fun onSuccess(p0: Void?) {
                                emitter.onNext(matrixRoomToRoom(r))
                                emitter.onComplete()
                            }

                            override fun onUnexpectedError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }

                            override fun onMatrixError(p0: MatrixError?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }

                            override fun onNetworkError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message))
                                emitter.onComplete()
                            }
                        })
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            }
        }
    }

    private fun mxUrlToUrl(avatarUrl: String?): String {
        var avatar: String = ""
        if (avatarUrl.isNullOrEmpty()) {
            avatar = ""
        } else {
            var url = session!!.contentManager.getDownloadableThumbnailUrl(avatarUrl, 100, 100, "")
            url?.let { avatar = it }
        }
        return avatar
    }

    override fun getUsersInRoom(roomId: String): Observable<List<User>> {
        return Observable.create { emitter ->
            val users = ArrayList<User>()
            val room = session!!.dataHandler.getRoom(roomId)
            room.getActiveMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    users.clear()
                    val joinRoomMember = p0?.filterIndexed { index, roomMember -> RoomMember.MEMBERSHIP_JOIN.equals(roomMember.membership) }
                    joinRoomMember?.forEach { roomMember ->
                        users.add(User(id = roomMember.userId, avatarUrl = mxUrlToUrl(roomMember.avatarUrl), name = roomMember.name, status = 0))
                    }
                    Debug.e(" size: ${users.size} - roomID: ${roomId}")
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    users.add(getCurrentUser())
                    emitter.onNext(users)
                    emitter.onComplete()
                }
            })
        }
    }

    override fun getListRoomAndAddUser(filters: Array<Int>): Observable<ListRoomAndRoomUserJoinReturn> {
        setMXSession()
        return Observable.create<ListRoomAndRoomUserJoinReturn> { emitter ->
            val listRoom = ArrayList<vmodev.clearkeep.viewmodelobjects.Room>()
            val listUser = ArrayList<User>()
            val listRoomUserJoin = ArrayList<RoomUserJoin>()
            val listObject = ArrayList<ListRoomAndRoomUserJoinReturn>()
            if (homeRoomsViewModel != null && homeRoomsViewModel!!.result != null) {
                homeRoomsViewModel!!.update()
                val rooms = ArrayList<Room>()
                for (filter in filters) {
                    rooms.addAll(funcs[filter].apply(homeRoomsViewModel!!.result))
                }
                var currentIndex: Int = 0
                if (rooms.size == 0) {
                    emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin))
                    emitter.onComplete()
                } else {
                    rooms.forEach { t: Room? ->
                        t?.let { it ->
                            asyncUpdateRoomMember(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ mrId ->
                                kotlin.run {
                                    currentIndex++
                                    listRoom.add(matrixRoomToRoom(t))
                                    mrId.forEach { u: User? ->
                                        u?.let { user ->
                                            listUser.add(user)
                                            listRoomUserJoin.add(RoomUserJoin(t.roomId, user.id))
                                        }
                                    }
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin))
                                        emitter.onComplete()
                                    }
                                }
                            }
                                    , { e ->
                                kotlin.run {
                                    currentIndex++
                                    listRoom.add(matrixRoomToRoom(t))
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin))
                                        emitter.onComplete()
                                    }
                                }
                            })
                        }
                    }
                }
            } else {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun getUsersInRoomAndAddToRoomUserJoin(roomId: String): Observable<RoomAndRoomUserJoin> {
        setMXSession()
        return Observable.create<RoomAndRoomUserJoin> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId)
            val users = ArrayList<User>()
            val roomUserJoin = ArrayList<RoomUserJoin>()
            Debug.e("--- user join room: ${room.avatarUrl}")
            room.getMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    p0?.forEach { t: RoomMember? ->
                        t?.let { roomMember ->
                            users.add(User(id = roomMember.userId, avatarUrl = mxUrlToUrl(roomMember.avatarUrl), name = roomMember.name, status = 0))
                            roomUserJoin.add(RoomUserJoin(room.roomId, roomMember.userId))
                        }
                    }
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin))
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateUser(name: String, avatar: InputStream?): Observable<User> {
        if (avatar == null) {
            return Observable.create<User> {
                updateUser(name).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({ sn ->
                    it.onNext(User(name = sn, id = session!!.myUserId, status = 0, avatarUrl = ""))
                    it.onComplete()
                }, { en ->
                    it.onError(en)
                    it.onComplete()
                })
            }
        } else {
            return Observable.create<User> {
                updateUser(name).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({ sn ->
                    updateUser(avatar).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({ sa ->
                        it.onNext(User(name = sn, id = session!!.myUserId, avatarUrl = sa, status = 0))
                        it.onComplete()
                    }, { ea ->
                        it.onError(ea)
                        it.onComplete()
                    })
                }, { en ->
                    it.onError(en)
                    it.onComplete()
                })
            }
        }
    }

    override fun updateUser(name: String): Observable<String> {
        setMXSession()
        return Observable.create<String> {
            session!!.myUser.updateDisplayName(name, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    it.onNext(name)
                    it.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    it.onError(Throwable(p0?.message))
                    it.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    it.onError(Throwable(p0?.message))
                    it.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    it.onError(Throwable(p0?.message))
                    it.onComplete()
                }
            })
        }
    }

    override fun updateUser(avatar: InputStream): Observable<String> {
        return Observable.create<String> {
            session!!.mediaCache.uploadContent(avatar, null, "image/jpeg", null, object : IMXMediaUploadListener {
                override fun onUploadProgress(p0: String?, p1: IMXMediaUploadListener.UploadStats?) {
                    //Do something
                }

                override fun onUploadCancel(p0: String?) {
                    // Do Something
                }

                override fun onUploadStart(p0: String?) {
                    // Do Something
                }

                override fun onUploadComplete(p0: String?, p1: String?) {
                    session!!.myUser.updateAvatarUrl(p1, object : ApiCallback<Void> {
                        override fun onSuccess(p0: Void?) {
                            var avatar = ""
                            var result = session!!.contentManager.getDownloadableThumbnailUrl(session!!.myUser.avatarUrl, 100, 100, "")

                            result?.let { avatar = result }
                            it.onNext(avatar)
                            it.onComplete()
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }
                    })
                }

                override fun onUploadError(p0: String?, p1: Int, p2: String?) {
                    it.onError(Throwable(p2))
                    it.onComplete()
                }
            })
        }
    }

    override fun exportNewBackupKey(passphrase: String): Observable<String> {
        setMXSession()
        return Observable.create<String> { emitter ->
            session!!.dataHandler.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.prepareKeysBackupVersion(passphrase, object : ProgressListener {
                    override fun onProgress(progress: Int, total: Int) {

                    }
                }, object : SuccessErrorCallback<MegolmBackupCreationInfo> {
                    override fun onSuccess(p0: MegolmBackupCreationInfo?) {
                        val keyBackup = mxCrypto.keysBackup
                        p0?.let { info ->
                            keyBackup.createKeysBackupVersion(info, object : ApiCallback<KeysVersion> {
                                override fun onSuccess(kv: KeysVersion?) {
                                    kv?.let {
                                        it.version?.let {
                                            emitter.onNext(info.recoveryKey)
                                            emitter.onComplete()
                                        } ?: run {
                                            emitter.onError(Throwable("KeysVersion Value is null"))
                                            emitter.onComplete()
                                        }
                                    } ?: run {
                                        emitter.onError(Throwable("KeysVersion is null"))
                                        emitter.onComplete()
                                    }
                                }

                                override fun onUnexpectedError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }

                                override fun onMatrixError(p0: MatrixError?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }

                                override fun onNetworkError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message))
                                    emitter.onComplete()
                                }
                            })
                        } ?: run {
                            emitter.onError(Throwable("MegolmBackupCreationInfo is null"))
                            emitter.onComplete()
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete()
            }
        }
    }

    override fun exportRoomKey(passphrase: String): Observable<String> {
        setMXSession()
        return Observable.create<String> { emitter ->
            session!!.crypto?.let {
                it.exportRoomKeys(passphrase, object : ApiCallback<ByteArray> {
                    override fun onSuccess(p0: ByteArray?) {
                        p0?.let {
                            val info = String(it)
                            emitter.onNext(info)
                            emitter.onComplete()
                        } ?: run {
                            emitter.onError(NullPointerException())
                            emitter.onComplete()
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            } ?: kotlin.run {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun sendTextMessage(roomId: String, content: String): Observable<Int> {
        setMXSession()
        return Observable.create<Int> { emmiter ->
            val room = session!!.dataHandler.getRoom(roomId)
            room.sendTextMessage(content, null, org.matrix.androidsdk.rest.model.message.Message.FORMAT_MATRIX_HTML, null, object : RoomMediaMessage.EventCreationListener {
                override fun onEventCreated(roomMediaMessage: RoomMediaMessage?) {
                    roomMediaMessage?.setEventSendingCallback(object : ApiCallback<Void> {
                        override fun onSuccess(info: Void?) {
                            Log.d("Message", roomMediaMessage.event.contentAsJsonObject.toString())
                            emmiter.onNext(1)
                            emmiter.onComplete()
                        }

                        override fun onUnexpectedError(e: Exception?) {
                            Log.d("Message", e?.message)
                            emmiter.onError(Throwable(e?.message))
                            emmiter.onComplete()
                        }

                        override fun onNetworkError(e: Exception?) {
                            Log.d("Message", e?.message)
                            emmiter.onError(Throwable(e?.message))
                            emmiter.onComplete()
                        }

                        override fun onMatrixError(e: MatrixError?) {
                            Log.d("Message", e?.message)
                            emmiter.onError(Throwable(e?.message))
                            emmiter.onComplete()
                        }
                    })
                }

                override fun onEventCreationFailed(roomMediaMessage: RoomMediaMessage?, errorMessage: String?) {
                    emmiter.onError(Throwable(errorMessage))
                    emmiter.onComplete()
                }

                override fun onEncryptionFailed(roomMediaMessage: RoomMediaMessage?) {
                    emmiter.onError(Throwable("Encrypt Fail"))
                    emmiter.onComplete()
                }
            })

        }
    }

    override fun getListFileInRoom(roomId: String): Observable<List<String>> {
        setMXSession()
        return Observable.create<List<String>> { emmiter ->
            val roomIds = ArrayList<String>()
            roomIds.add(roomId)
            session!!.searchMediaByName("img", roomIds, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val result = ArrayList<String>()
                    p0?.searchCategories?.roomEvents?.let {
                        it.results.forEach {
                            result.add(it.result.content.toString())
                        }
                    }
                    emmiter.onNext(result)
                    emmiter.onComplete()
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emmiter.onError(Throwable(p0?.message))
                    emmiter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emmiter.onError(Throwable(p0?.message))
                    emmiter.onComplete()
                }

                override fun onNetworkError(p0: Exception?) {
                    emmiter.onError(Throwable(p0?.message))
                    emmiter.onComplete()
                }
            })
        }
    }

    override fun getListSignature(id: String): Observable<List<Signature>> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypt ->
                mxCrypt.keysBackup.mKeysBackupVersion?.let {
                    mxCrypt.keysBackup.getKeysBackupTrust(it, SuccessCallback { p0 ->
                        p0?.let {
                            val signatures = ArrayList<Signature>()
                            it.signatures.forEach {
                                var deviceId = ""
                                it.deviceId?.let {
                                    deviceId = it
                                }
                                val signature = Signature(id = deviceId, status = if (it.valid) 1 else 0
                                        , description = if (deviceId.compareTo(mxCrypt.myDevice.deviceId) == 0) application.resources.getString(R.string.keys_backup_settings_valid_signature_from_this_device)
                                else application.resources.getString(R.string.keys_backup_settings_valid_signature_from_verified_device), keyBackup = id)
                                signatures.add(signature)
                            }
                            emitter.onNext(signatures)
                            emitter.onComplete()
                        } ?: kotlin.run {
                            emitter.onError(NullPointerException())
                            emitter.onComplete()
                        }
                    })

                } ?: kotlin.run {
                    emitter.onError(NullPointerException())
                    emitter.onComplete()
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun getKeyBackUpData(userId: String): Observable<KeyBackup> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let {
                    var algorithm = ""
                    var count = 0
                    var version = ""
                    it.algorithm?.let {
                        algorithm = it
                    }
                    it.version?.let {
                        version = it
                    }
                    it.count?.let {
                        count = it
                    }
                    val keyBackup = KeyBackup(id = userId, algorithm = algorithm, version = version, count = count, state = mxCrypto.keysBackup.state.ordinal)
                    emitter.onNext(keyBackup)
                    emitter.onComplete()
                } ?: kotlin.run {
                    val keyBackup = KeyBackup(id = userId, algorithm = "", version = "", count = 0, state = mxCrypto.keysBackup.state.ordinal)
                    emitter.onNext(keyBackup)
                    emitter.onComplete()
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException())
                emitter.onComplete()
            }
        }
    }

    override fun restoreBackupFromPassphrase(password: String): Observable<ImportRoomKeysResult> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let { keyBackupResult ->
                    mxCrypto.keysBackup.restoreKeyBackupWithPassword(keyBackupResult, password, null, session!!.myUserId, object : StepProgressListener {
                        var isComputingKey = false
                        override fun onStepProgress(step: StepProgressListener.Step) {
                            when (step) {
                                is StepProgressListener.Step.ComputingKey -> {
                                    if (isComputingKey)
                                        return
                                    isComputingKey = true
                                    Toast.makeText(application, "Computing key", Toast.LENGTH_SHORT).show()
                                }
                                is StepProgressListener.Step.DownloadingKey -> {
                                    Toast.makeText(application, "Downloading key", Toast.LENGTH_SHORT).show()
                                }
                                is StepProgressListener.Step.ImportingKey -> {
//                                    Toast.makeText(application, "Importing Key", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, object : ApiCallback<ImportRoomKeysResult> {
                        override fun onSuccess(p0: ImportRoomKeysResult?) {
                            p0?.let {
                                Toast.makeText(application, "Restore backup key successfully", Toast.LENGTH_SHORT).show()
                                mxCrypto.keysBackup.trustKeysBackupVersion(keyBackupResult, true, object : ApiCallback<Void> {
                                    override fun onSuccess(p0: Void?) {
                                        emitter.onNext(it)
                                        emitter.onComplete()
                                    }

                                    override fun onUnexpectedError(p0: Exception?) {
                                        emitter.onError(Throwable(p0?.message))
                                        emitter.onComplete()
                                    }

                                    override fun onMatrixError(p0: MatrixError?) {
                                        emitter.onError(Throwable(p0?.message))
                                        emitter.onComplete()
                                    }

                                    override fun onNetworkError(p0: Exception?) {
                                        emitter.onError(Throwable(p0?.message))
                                        emitter.onComplete()
                                    }
                                })

                            } ?: kotlin.run {
                                emitter.onError(Throwable("No Import Room Key Result"))
                                emitter.onComplete()
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }
                    })
                } ?: kotlin.run {
                    emitter.onError(Throwable("No Key Backup Version"))
                    emitter.onComplete()
                }
            } ?: kotlin.run {
                emitter.onError(Throwable("No crypto"))
                emitter.onComplete()
            }
        }
    }

    override fun restoreBackupKeyFromRecoveryKey(key: String): Observable<ImportRoomKeysResult> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let {
                    mxCrypto.keysBackup.restoreKeysWithRecoveryKey(it, key, null, session!!.myUserId, object : StepProgressListener {
                        var isComputingKey = false
                        override fun onStepProgress(step: StepProgressListener.Step) {
                            when (step) {
                                is StepProgressListener.Step.ComputingKey -> {
                                    if (isComputingKey)
                                        return
                                    isComputingKey = true
                                    Toast.makeText(application, "Computing Key", Toast.LENGTH_SHORT).show()
                                }
                                is StepProgressListener.Step.DownloadingKey -> {
                                    Toast.makeText(application, "Downloading Key", Toast.LENGTH_SHORT).show()
                                }
                                is StepProgressListener.Step.ImportingKey -> {
                                    Toast.makeText(application, "Importing Key", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }, object : ApiCallback<ImportRoomKeysResult> {
                        override fun onSuccess(p0: ImportRoomKeysResult?) {
                            p0?.let {
                                Toast.makeText(application, "Success", Toast.LENGTH_SHORT).show()
                                emitter.onNext(it)
                                emitter.onComplete()
                            } ?: kotlin.run {
                                Toast.makeText(application, "No Import Room Key Result", Toast.LENGTH_LONG).show()
                                emitter.onComplete()
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show()
                            emitter.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show()
                            emitter.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show()
                            emitter.onComplete()
                        }
                    })
                } ?: kotlin.run {
                    Toast.makeText(application, "No Key Backup Version", Toast.LENGTH_SHORT).show()
                    emitter.onComplete()
                }
            } ?: kotlin.run {
                Toast.makeText(application, "No crypto", Toast.LENGTH_SHORT).show()
                emitter.onComplete()
            }
        }
    }

    override fun getAuthDataAsMegolmBackupAuthData(): Observable<String> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.getCurrentVersion(object : ApiCallback<KeysVersionResult?> {
                    override fun onSuccess(p0: KeysVersionResult?) {
                        p0?.let {
                            it.getAuthDataAsMegolmBackupAuthData().privateKeySalt?.let {
                                emitter.onNext(it)
                                emitter.onComplete()
                            } ?: run {
                                emitter.onError(Throwable("Null Private Key Salt"))
                                emitter.onComplete()
                            }
                        } ?: run {
                            emitter.onError(Throwable("Null Keys Version Result"))
                            emitter.onComplete()
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message))
                        emitter.onComplete()
                    }
                })
            } ?: kotlin.run {
                Toast.makeText(application, "No crypto", Toast.LENGTH_SHORT).show()
                emitter.onComplete()
            }
        }
    }

    override fun deleteKeyBackup(userId: String): Observable<KeyBackup> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.currentBackupVersion?.let {
                    mxCrypto.keysBackup.deleteBackup(it, object : ApiCallback<Void> {
                        override fun onSuccess(p0: Void?) {
                            val keyBackup = KeyBackup(id = userId, algorithm = "", version = "", count = 0, state = mxCrypto.keysBackup.state.ordinal)
                            emitter.onNext(keyBackup)
                            emitter.onComplete()
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message))
                            emitter.onComplete()
                        }
                    })
                } ?: kotlin.run {
                    emitter.onError(NullPointerException("Current backup version is null"))
                    emitter.onComplete()
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException("Crypto is null"))
                emitter.onComplete()
            }
        }
    }

    override fun checkNeedBackupWhenSignOut(): Observable<Int> {
        setMXSession()
        return Observable.create { emitter ->
            var valueNext: Int = 1
            val value = session
                    ?.crypto
                    ?.cryptoStore
                    ?.inboundGroupSessionsCount(false)
                    ?: 0 > 0
                    && session
                    ?.crypto
                    ?.keysBackup
                    ?.state != KeysBackupStateManager.KeysBackupState.ReadyToBackUp
            if (value) {
                valueNext = 2

                session!!.crypto?.let {
                    it.keysBackup.state.let {

                        if (it == KeysBackupStateManager.KeysBackupState.NotTrusted)
                            valueNext = 4
                    }
                }
            }
            emitter.onNext(valueNext)
            emitter.onComplete()
        }
    }

    override fun checkBackupKeyTypeWhenSignIn(): Observable<Int> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                var valueNext: Int = 1
                var listener: KeysBackupStateManager.KeysBackupStateListener? = null
                listener = object : KeysBackupStateManager.KeysBackupStateListener {
                    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
                        if (newState != KeysBackupStateManager.KeysBackupState.CheckingBackUpOnHomeserver) {
                            when (newState) {
                                KeysBackupStateManager.KeysBackupState.NotTrusted -> valueNext = 4
                                KeysBackupStateManager.KeysBackupState.Disabled -> valueNext = 2
                            }
                            emitter.onNext(valueNext)
                            emitter.onComplete()
                            listener?.let { mxCrypto.keysBackup.removeListener(it) }
                        }
                    }
                }
                mxCrypto.keysBackup.addListener(listener)
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete()
            }
        }
    }

    override fun checkBackupKeyStateWhenStart(): Observable<Int> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                var listener: KeysBackupStateManager.KeysBackupStateListener? = null
                listener = object : KeysBackupStateManager.KeysBackupStateListener {
                    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
                        emitter.onNext(newState.ordinal)
                        if (newState == KeysBackupStateManager.KeysBackupState.NotTrusted || newState == KeysBackupStateManager.KeysBackupState.Disabled
                                || newState == KeysBackupStateManager.KeysBackupState.ReadyToBackUp || newState == KeysBackupStateManager.KeysBackupState.WrongBackUpVersion) {
                            listener?.let { l ->
                                Log.d("AutoBackup", "Remove")
                                Observable.timer(1, TimeUnit.SECONDS).subscribe { mxCrypto.keysBackup.removeListener(l) }
                            }
                            emitter.onComplete()
                        }
                    }
                }
                Log.d("AutoBackup", "Add")
                mxCrypto.keysBackup.addListener(listener)
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete()
            }
        }
    }

    override fun changeRoomNotificationState(roomId: String, state: Byte): Observable<Byte> {
        setMXSession()
        return Observable.create { emitter ->
            var enumState: BingRulesManager.RoomNotificationState? = BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY
            when (state) {
                0x01.toByte() -> enumState = BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY
                0x02.toByte() -> enumState = BingRulesManager.RoomNotificationState.ALL_MESSAGES
                0x04.toByte() -> enumState = BingRulesManager.RoomNotificationState.MENTIONS_ONLY
                0x08.toByte() -> enumState = BingRulesManager.RoomNotificationState.MUTE
            }
            session!!.dataHandler.bingRulesManager.updateRoomNotificationState(roomId, enumState, object : BingRulesManager.onBingRuleUpdateListener {
                override fun onBingRuleUpdateSuccess() {
                    emitter.onNext(state)
                    emitter.onComplete()
                }

                override fun onBingRuleUpdateFailure(p0: String?) {
                    emitter.onError(Throwable(p0))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun getMessagesToSearch(): Observable<List<Message>> {
        setMXSession()
        return Observable.create { emitter ->
            var index = 0
            val rooms = session!!.getJoinedRoom()
            rooms.forEach { r ->
                session!!.roomsApiClient.initialSync(r.roomId, object : ApiCallback<RoomResponse> {
                    override fun onSuccess(info: RoomResponse?) {
                        val roomSync = RoomSync()
                        roomSync.state = RoomSyncState()
                        roomSync.state.events = info?.state
                        roomSync.timeline = RoomSyncTimeline()
                        roomSync.timeline.events = info?.messages?.chunk
                        val messages = ArrayList<Message>()
                        roomSync.timeline.events.forEach {
                            it?.let {
                                if (it.type.compareTo(Event.EVENT_TYPE_MESSAGE_ENCRYPTED) == 0) {
                                    val message = Message(id = it.eventId, roomId = it.roomId, userId = it.sender, messageType = it.type, encryptedContent = it.content.toString(), createdAt = it.originServerTs)
                                    messages.add(message)
                                }
                            }
                        }
                        emitter.onNext(messages)
                        index++
                        if (index >= rooms.size)
                            emitter.onComplete()
                    }

                    override fun onUnexpectedError(e: java.lang.Exception?) {
                        emitter.onError(Throwable(e?.message))
                        index++
                        if (index >= rooms.size)
                            emitter.onComplete()
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        emitter.onError(Throwable(e?.message))
                        index++
                        if (index >= rooms.size)
                            emitter.onComplete()
                    }

                    override fun onNetworkError(e: java.lang.Exception?) {
                        emitter.onError(Throwable(e?.message))
                        index++
                        if (index >= rooms.size)
                            emitter.onComplete()
                    }
                })
            }
        }
    }

    /**
     * Decrypt message content
     */
    override fun decryptListMessage(messages: List<MessageRoomUser>, msgType: String): Observable<List<MessageRoomUser>> {
        setMXSession()
        return Observable.create { emitter ->
            var messagesResult = ArrayList<MessageRoomUser>()
            val messageFilterHashMap: HashMap<String, MessageRoomUser> = HashMap()
            val parser = JsonParser()
            val gson = Gson()
            session!!.dataHandler.crypto?.let { mxCrypto ->
                messages.forEach { item ->
                    val event = Event(item.message?.messageType, parser.parse(item.message?.encryptedContent).asJsonObject, item.message?.userId, item.message?.roomId)

                    try {
                        if (event.type == Event.EVENT_TYPE_MESSAGE_ENCRYPTED) {
                            val result = mxCrypto.decryptEvent(event, null)
                            result.let {
                                val json = result.mClearEvent.asJsonObject
                                val type = json.get("type").asString
                                var messageResult: Message? = null
                                var messageRooUser: MessageRoomUser? = null
                                val message = gson.fromJson(result.mClearEvent, MessageContent::class.java)
                                if (!type.isNullOrEmpty() && type == Event.EVENT_TYPE_MESSAGE) {
                                    val messageType = message.content?.msgType
                                    if (msgType == EventTypeEnum.TEXT.value && messageType == EventTypeEnum.TEXT.value) {
                                        var messagesID: String = ""
                                        var contentMessage: String = ""
                                        item.message?.let {
                                            val data: JsonObject = event.contentJson.asJsonObject
                                            if (data.getAsJsonObject("m.relates_to") != null) {
                                                messagesID = data.getAsJsonObject("m.relates_to").get("event_id").asString
//                                                        contentMessage = String().formatMessageEdit(message?.content?.body)
                                                contentMessage = String().formatMessageEdit(message?.content?.body.toString())
                                            } else {
                                                messagesID = it.id
                                                contentMessage = message?.content?.body.toString()
                                            }
                                            messageResult = Message(id = it.id, roomId = it.roomId, userId = it.userId, messageType = Event.EVENT_TYPE_MESSAGE, encryptedContent = contentMessage, createdAt = item.message!!.createdAt)
                                        }
                                        messageRooUser = MessageRoomUser(message = messageResult, room = item.room, user = item.user)
                                        messageFilterHashMap.put(messagesID, messageRooUser)
                                        // messageRooUser.let { it1 -> messagesResult.add(it1) }
                                    } else if (msgType == EventTypeEnum.IMAGE.value && (messageType == EventTypeEnum.IMAGE.value || messageType == EventTypeEnum.VIDEO.value || messageType == EventTypeEnum.AUDIO.value || messageType == EventTypeEnum.FILE.value)) {
                                        item.message?.let {
                                            messageResult = Message(id = it.id, roomId = it.roomId, userId = it.userId, messageType = Event.EVENT_TYPE_MESSAGE, encryptedContent = json.getAsJsonObject("content").toString(), createdAt = item.message!!.createdAt)
                                            //                                                    messageResult = Message(id = it.id, roomId = it.roomId, userId = it.userId, messageType = Event.EVENT_TYPE_MESSAGE, encryptedContent = message.getContent().getBody(), createdAt = if (item.message != null) item.message!!.createdAt else 0)
                                        }
                                        messageRooUser = MessageRoomUser(message = messageResult, room = item.room, user = item.user)
                                        messageRooUser.let { it1 -> messagesResult.add(it1) }
                                    } else {
                                        Log.e("Tag", "--- Khong co gi hien ")
                                    }
                                }

//                                        Event.EVENT_TYPE_CALL_HANGUP -> {
//                                            if (msgType == Event.EVENT_TYPE_CALL_INVITE) {
//                                                val callContent = gson.fromJson(result.mClearEvent, CallContent::class.java)
//                                                var typeCall = ""
//                                                typeCall = if (!callContent.getContent()?.getReason().isNullOrBlank() && callContent.getContent()?.getReason() == EventTypeEnum.INVITE_TIMEOUT.value) {
//                                                    EventTypeEnum.MISS_CALL.value
//                                                } else if (callHistoryFilter.containsKey(callContent.getContent()?.getCallId()) && callHistoryFilter[callContent.getContent()?.getCallId()]?.message?.encryptedContent == EventTypeEnum.MISS_CALL.value) {
//                                                    EventTypeEnum.MISS_CALL.value
//                                                } else {
//                                                    callContent.getType().toString()
//                                                }
//                                                //
//                                                item.message?.let {
//                                                    messageResult = Message(id = it.id, roomId = it.roomId, userId = it.userId, messageType = Event.EVENT_TYPE_MESSAGE, encryptedContent = typeCall, createdAt = item.message!!.createdAt)
//                                                }
//                                                messageRooUser = MessageRoomUser(message = messageResult, room = item.room, user = item.user)
//                                                callContent.getContent()?.getCallId()?.let { it1 -> callHistoryFilter.put(it1, messageRooUser) }
//                                            }
//                                        }

                            }
                        }
                    } catch (e: MXDecryptionException) {
                        Log.d("DecryptError", e.message)
                    }
                }
                if (msgType == EventTypeEnum.TEXT.value) {
                    messagesResult = ArrayList<MessageRoomUser>(messageFilterHashMap.values)
                }
                emitter.onNext(messagesResult)
                emitter.onComplete()
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete()
            }
        }
    }

    override fun decryptEventCallHistory(messages: List<MessageRoomUser>): Observable<List<MessageRoomUser>> {
        setMXSession()
        return Observable.create { emitter ->
            var messagesResult = ArrayList<MessageRoomUser>()
            val callHistoryFilter: HashMap<String, MessageRoomUser> = HashMap()
            val parser = JsonParser()
            val gson = Gson()
            session!!.dataHandler.crypto?.let { mxCrypto ->
                messages.forEach { item ->
                    val event = Event(item.message?.messageType, parser.parse(item.message?.encryptedContent).asJsonObject, item.message?.userId, item.message?.roomId)
                    try {
                        if (event.type == Event.EVENT_TYPE_MESSAGE_ENCRYPTED) {
                            val result = mxCrypto.decryptEvent(event, null)
                            val json = result.mClearEvent.asJsonObject
                            val type = json.get("type").asString
                            var messageResult: Message? = null
                            var messageRooUser: MessageRoomUser? = null
                            val message = gson.fromJson(result.mClearEvent, MessageContent::class.java)
                            if (!type.isNullOrEmpty() && type == Event.EVENT_TYPE_CALL_HANGUP) {
                                val callContent = gson.fromJson(result.mClearEvent, CallContent::class.java)
                                var typeCall = ""
                                typeCall = if (!callContent.getContent()?.getReason().isNullOrBlank() && callContent.getContent()?.getReason() == EventTypeEnum.INVITE_TIMEOUT.value) {
                                    EventTypeEnum.MISS_CALL.value
                                } else if (callHistoryFilter.containsKey(callContent.getContent()?.getCallId()) && callHistoryFilter[callContent.getContent()?.getCallId()]?.message?.encryptedContent == EventTypeEnum.MISS_CALL.value) {
                                    EventTypeEnum.MISS_CALL.value
                                } else {
                                    callContent.getType().toString()
                                }
                                //
                                item.message?.let {
                                    messageResult = Message(id = it.id, roomId = it.roomId, userId = it.userId, messageType = Event.EVENT_TYPE_MESSAGE, encryptedContent = typeCall, createdAt = item.message!!.createdAt)
                                }
                                messageRooUser = MessageRoomUser(message = messageResult, room = item.room, user = item.user)
                                callContent.getContent()?.getCallId()?.let { it1 -> callHistoryFilter.put(it1, messageRooUser) }
                            }
                        }

                    } catch (e: MXDecryptionException) {
                        Log.d("DecryptError", e.message)
                    }
                }
                messagesResult = ArrayList<MessageRoomUser>(callHistoryFilter.values)
                emitter.onNext(messagesResult)
                emitter.onComplete()
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete()
            }
        }

    }


    override fun editMessage(event: Event): Observable<EditMessageResponse> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.crypto?.let { crypto ->
                val room = session!!.dataHandler.getRoom(event.roomId)
                crypto.encryptEventContent(event.contentJson, "m.room.message", room, object : ApiCallback<MXEncryptEventContentResult> {
                    override fun onSuccess(info: MXEncryptEventContentResult?) {
                        val relatesTo = HashMap<String, String>()
                        relatesTo.put("event_id", event.eventId)
                        relatesTo.put("rel_type", "m.replace")
                        val jsonObject = info!!.mEventContent.asJsonObject
                        val sessionId = jsonObject.get("session_id").asString
                        val ciphertext = jsonObject.get("ciphertext").asString
                        val deviceId = jsonObject.get("device_id").asString
                        val sender = jsonObject.get("sender_key").asString
                        val algorithm = jsonObject.get("algorithm").asString
                        val editMessageRequest = EditMessageRequest(sessionId, ciphertext, deviceId, algorithm
                                , sender, relatesTo)
                        val locaId = "local." + UUID.randomUUID()
                        apis.editMessage("Bearer " + session!!.credentials.accessToken, event.roomId, Event.EVENT_TYPE_MESSAGE_ENCRYPTED, locaId, editMessageRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io()).subscribe({
                                    emitter.onNext(it)
                                    emitter.onComplete()
                                }, {
                                    Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show()
                                    emitter.onError(it)
                                    emitter.onComplete()
                                })
                    }

                    override fun onUnexpectedError(e: java.lang.Exception?) {
                        Toast.makeText(application, e?.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        Toast.makeText(application, e?.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onNetworkError(e: java.lang.Exception?) {
                        Toast.makeText(application, e?.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
    }

    override fun getPassphrase(): Observable<PassphraseResponse> {
        setMXSession()
        return apisClearKeep.getPassphrase("Bearer " + session!!.credentials.accessToken)
    }

    override fun createPassphrase(passphrase: String): Observable<PassphraseResponse> {
        setMXSession()
        return apisClearKeep.createPassphrase("Bearer " + session!!.credentials.accessToken, passphrase)
    }

    override fun getUserProfile(userId: String): Observable<User> {
        return Observable.create { emitter ->
            apis.getUserProfile(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val user = User(id = userId, name = if (it.displayName.isNullOrEmpty()) "" else it.displayName, avatarUrl = if (it.avatarUrl.isNullOrEmpty()) "" else it.avatarUrl, status = 0)
                        emitter.onNext(user)
                        emitter.onComplete()
                    }, {
                        emitter.onError(it)
                        emitter.onComplete()
                    })
        }
    }

    override fun getListRoomDirectory(limit: Int, query: String): Observable<List<PublicRoom>> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.eventsApiClient.loadPublicRooms(null, null, false, query, null, limit, object : ApiCallback<PublicRoomsResponse> {
                override fun onSuccess(p0: PublicRoomsResponse?) {
                    emitter.onNext(p0!!.chunk)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateRoomName(roomId: String, roomName: String): Observable<String> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.roomsApiClient.updateRoomName(roomId, roomName, object : ApiCallback<Void?> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext(roomName)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateRoomTopic(roomId: String, roomTopic: String): Observable<String> {
        setMXSession()
        return Observable.create { emitter ->
            session!!.roomsApiClient.updateTopic(roomId, roomTopic, object : ApiCallback<Void?> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext(roomTopic)
                    emitter.onComplete()
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateRoomAvatar(roomId: String, inputStream: InputStream): Observable<String> {
        setMXSession()
        return Observable.create<String> {
            session!!.mediaCache.uploadContent(inputStream, null, "image/jpeg", null, object : IMXMediaUploadListener {
                override fun onUploadProgress(p0: String?, p1: IMXMediaUploadListener.UploadStats?) {
                    //Do something
                }

                override fun onUploadCancel(p0: String?) {
                    // Do Something
                }

                override fun onUploadStart(p0: String?) {
                    // Do Something
                }

                override fun onUploadComplete(p0: String?, p1: String?) {
                    session!!.roomsApiClient.updateAvatarUrl(roomId, p1, object : ApiCallback<Void?> {
                        override fun onSuccess(p0: Void?) {
                            var avatar = ""
                            var result = session!!.contentManager.getDownloadableUrl(p1, false)
                            result?.let { avatar = result }
                            it.onNext(avatar)
                            it.onComplete()
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }

                        override fun onNetworkError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete()
                        }
                    })
                }

                override fun onUploadError(p0: String?, p1: Int, p2: String?) {
                    it.onError(Throwable(p2))
                    it.onComplete()
                }
            })
        }
    }

    override fun feedBackApp(contents: String, start: Int): Observable<FeedbackResponse> {
        setMXSession()
        return apisClearKeep.feedBackApp("Bearer " + session!!.credentials.accessToken, contents, start)
    }

    override fun getVersionApp(): Observable<Resource<VersionAppInfoResponse>> {
        return apisClearKeep.getVersionApp("android")
    }
}