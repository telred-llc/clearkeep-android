package vmodev.clearkeep.matrixsdk

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import im.vector.BuildConfig
import im.vector.LoginHandler
import im.vector.Matrix
import im.vector.RegistrationManager
import im.vector.R
import im.vector.activity.util.WaitingViewData
import im.vector.util.HomeRoomsViewModel
import im.vector.util.RoomUtils
import im.vector.util.VectorUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.internal.operators.observable.ObservableAll
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.HomeServerConnectionConfig
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.MXCRYPTO_ALGORITHM_MEGOLM
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrust
import org.matrix.androidsdk.crypto.keysbackup.MegolmBackupCreationInfo
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.data.RoomTag
import org.matrix.androidsdk.listeners.IMXMediaUploadListener
import org.matrix.androidsdk.listeners.ProgressListener
import org.matrix.androidsdk.listeners.StepProgressListener
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.callback.SuccessCallback
import org.matrix.androidsdk.rest.callback.SuccessErrorCallback
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.keys.KeysVersion
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms
import org.matrix.androidsdk.rest.model.pid.ThreePid
import org.matrix.androidsdk.rest.model.keys.KeysVersionResult
import org.matrix.androidsdk.rest.model.login.LoginFlow
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse
import org.matrix.androidsdk.rest.model.search.SearchResponse
import org.matrix.androidsdk.rest.model.search.SearchResult
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse
import org.matrix.androidsdk.util.BingRulesManager
import org.matrix.androidsdk.util.JsonUtils
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.fragments.SignUpFragment
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.ultis.ListRoomAndRoomUserJoinReturn
import vmodev.clearkeep.ultis.RoomAndRoomUserJoin
import vmodev.clearkeep.ultis.SearchMessageByTextResult
import vmodev.clearkeep.viewmodelobjects.*
import java.io.InputStream
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixServiceImplement @Inject constructor(private val application: ClearKeepApplication) : MatrixService {

    //    @Inject
    private var session: MXSession? = null;
    private var homeRoomsViewModel: HomeRoomsViewModel? = null;
    private val funcs: Array<Function<HomeRoomsViewModel.Result, List<Room>>> = Array(255, init = { Function { t: HomeRoomsViewModel.Result -> t.directChats } })
    private fun setMXSession() {
        if (session != null) {
            if (session!!.isAlive)
                return;
        }


        session = Matrix.getInstance(application).defaultSession;
        homeRoomsViewModel = HomeRoomsViewModel(session!!);
        funcs[1] = Function { t: HomeRoomsViewModel.Result -> t.directChats };
        funcs[2] = Function { t: HomeRoomsViewModel.Result -> t.otherRooms };
        funcs[65] = Function { t: HomeRoomsViewModel.Result -> getListDirectMessageInvite() };
        funcs[66] = Function { t: HomeRoomsViewModel.Result -> getListRoomMessageInvite() };
        funcs[129] = Function { t: HomeRoomsViewModel.Result -> t.getDirectChatsWithFavorites() };
        funcs[130] = Function { t: HomeRoomsViewModel.Result -> t.getOtherRoomsWithFavorites() };
    }

    @SuppressLint("CheckResult")
    override fun getListDirectMessageConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.directChats);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            kotlin.run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListRoomConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.otherRooms);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            kotlin.run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListFavouriteConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.favourites);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListContact(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.getDirectChatsWithFavorites());
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? -> kotlin.run { liveData.value = t; } };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getUser(): Observable<User> {
        setMXSession();
        val myUser = session!!.myUser;
        return Observable.create { emitter ->
            run {
                if (myUser != null) {
                    var avatar = "";
                    var result = session!!.contentManager.getDownloadableUrl(myUser.avatarUrl);
                    result?.let { avatar = result }
                    val user = User(name = myUser.displayname, id = myUser.user_id, avatarUrl = avatar, status = if (myUser.isActive) 1 else 0, roomId = "");
                    emitter.onNext(user);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }
    }

    override fun getListRoom(filters: Array<Int>): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>> {
        setMXSession();
        return Observable.create<List<vmodev.clearkeep.viewmodelobjects.Room>> { emitter ->
            kotlin.run {
                val listRoom = ArrayList<vmodev.clearkeep.viewmodelobjects.Room>();
                if (homeRoomsViewModel != null && homeRoomsViewModel!!.result != null) {
                    homeRoomsViewModel!!.update();
                    val rooms = ArrayList<Room>();
                    for (filter in filters) {
                        rooms.addAll(funcs[filter].apply(homeRoomsViewModel!!.result))
                    }
                    var currentIndex: Int = 0;
                    rooms.forEach { t: Room? ->
                        t?.let {
                            asyncUpdateRoomMember(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ mrId ->
                                kotlin.run {
                                    currentIndex++;
                                    listRoom.add(matrixRoomToRoom(t, if (mrId.isNotEmpty()) if (mrId[0].id.isNullOrEmpty()) "" else mrId[0].id else ""));
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(listRoom);
                                        emitter.onComplete();
                                    }
                                }
                            }
                                    , { e ->
                                kotlin.run {
                                    currentIndex++;
                                    listRoom.add(matrixRoomToRoom(t));
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(listRoom);
                                        emitter.onComplete();
                                    }
                                }
                            });
                        }
                    }
//                    emitter.onNext(listRoom);
//                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }
    }

    override fun getRoomWithId(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return ObservableAll.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            kotlin.run {
                val room = session!!.dataHandler!!.getRoom(id);
                if (room != null) {
                    emitter.onNext(matrixRoomToRoom(room));
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }
    }

    override fun getListRoomWithTwoFilter(filterOne: Int, filterTwo: Int): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>> {
        setMXSession();
        return Observable.create<List<vmodev.clearkeep.viewmodelobjects.Room>> { emitter ->
            kotlin.run {

            }
        }
    }

    private fun getListDirectMessageInvite(): List<Room> {
        val roomSummaries = session!!.dataHandler.store.summaries;
        val rooms = ArrayList<Room>();
        roomSummaries.forEach { t: RoomSummary? ->
            kotlin.run {
                val room = session!!.dataHandler.store.getRoom(t?.roomId);
                if (room != null && !room!!.isConferenceUserRoom && room!!.isInvited && room.isDirectChatInvitation) {
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }

    private fun getListRoomMessageInvite(): List<Room> {
        val roomSummaries = session!!.dataHandler.store.summaries;
        val rooms = ArrayList<Room>();
        roomSummaries.forEach { t: RoomSummary? ->
            kotlin.run {
                val room = session!!.dataHandler.store.getRoom(t?.roomId);
                if (room != null && !room!!.isConferenceUserRoom && room!!.isInvited && !room.isDirectChatInvitation) {
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }

    override fun joinRoom(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            kotlin.run {
                val room = session!!.dataHandler.store.getRoom(id);
                if (room != null) {
                    session!!.joinRoom(room.roomId, object : ApiCallback<String> {
                        override fun onSuccess(p0: String?) {
                            emitter.onNext(matrixRoomToRoom(room));
                            emitter.onComplete();
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(message = p0?.message));
                            emitter.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(message = p0?.message));
                            emitter.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(message = p0?.message));
                            emitter.onComplete();
                        }
                    });
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete();
                }
            }
        };
    }

    override fun leaveRoom(id: String): Observable<String> {
        setMXSession();
        return Observable.create<String> { emitter ->
            val room = session!!.dataHandler.getRoom(id);
            room?.let { room ->
                room.leave(object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        val roomAfter = session!!.dataHandler.getRoom(id);
                        roomAfter?.let { r ->
                            r.leave(object : ApiCallback<Void> {
                                override fun onSuccess(p0: Void?) {
                                    emitter.onNext(id);
                                    emitter.onComplete();
                                }

                                override fun onUnexpectedError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }

                                override fun onMatrixError(p0: MatrixError?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }

                                override fun onNetworkError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }
                            })
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                })
            }
        }
    }

    private fun matrixRoomToRoom(room: Room, roomMemberId: String = ""): vmodev.clearkeep.viewmodelobjects.Room {
        var sourcePrimary = 1;// = if (room.isDirect) 0b00000001 else 0b00000010;
        if (room.isInvited) {
            sourcePrimary = if (room.isDirectChatInvitation) 0b00000001 else 0b00000010;
        } else {
            sourcePrimary = if (room.isDirect) 0b00000001 else 0b00000010;
        }
        val sourceSecondary = if (room.isInvited) 0b01000000 else 0b00000000;
        val sourceThird = if ((room.accountData?.keys
                        ?: emptySet()).contains(RoomTag.ROOM_TAG_FAVOURITE)) 0b10000000 else 0b00000000;
        var timeUpdateLong: Long = 0;
        var lastMessage: String = "";
        room.roomSummary?.let { roomSummary ->
            timeUpdateLong = roomSummary.latestReceivedEvent.originServerTs
            lastMessage = RoomUtils.getRoomMessageToDisplay(application, session!!, roomSummary).toString();
            if (lastMessage.compareTo("** Unable to decrypt: The sender's device has not sent us the keys for this message. **") == 0) {
                lastMessage = "";
            }
        }
        var notificationState: Byte = 0x02;
        when (session!!.dataHandler.bingRulesManager.getRoomNotificationState(room.roomId)) {
            BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY -> notificationState = 0x01;
            BingRulesManager.RoomNotificationState.ALL_MESSAGES -> notificationState = 0x02;
            BingRulesManager.RoomNotificationState.MENTIONS_ONLY -> notificationState = 0x04;
            BingRulesManager.RoomNotificationState.MUTE -> notificationState = 0x08;
        }
        val avatar: String? = if (room.avatarUrl.isNullOrEmpty()) "" else session!!.contentManager.getDownloadableUrl(room.avatarUrl);

        val rooMemberOnlineStatus: Byte = if (roomMemberId.isNullOrEmpty()) 0 else if (VectorUtils.getUserOnlineStatus(application, session!!, roomMemberId, null).compareTo("Online now") == 0) 1 else 0;
//        Log.d("Room Type: ", (sourcePrimary or sourceSecondary or sourceThird).toString() + "-----" + room.getRoomDisplayName(application))
        val roomObj: vmodev.clearkeep.viewmodelobjects.Room = vmodev.clearkeep.viewmodelobjects.Room(id = room.roomId, name = room.getRoomDisplayName(application)
                , type = (sourcePrimary or sourceSecondary or sourceThird), avatarUrl = avatar!!, notifyCount = room.notificationCount
                , updatedDate = timeUpdateLong, roomMemberId = roomMemberId, roomMemberStatus = rooMemberOnlineStatus, topic = if (room.topic.isNullOrEmpty()) "" else room.topic, version = 1, highlightCount = room.highlightCount, lastMessage = lastMessage
                , encrypted = if (room.isEncrypted) 1 else 0, status = if (room.isLeaving || room.isLeft) 0 else 1, notificationState = notificationState);
        return roomObj;
    }

    @SuppressLint("CheckResult")
    private fun asyncUpdateRoomMember(room: Room): Observable<List<User>> {
        return Observable.create<List<User>> { emitter ->
            val users = ArrayList<User>();
//            if (!room.isDirect || room.isInvited) {
//                emitter.onError(Throwable("No need check"))
//                emitter.onComplete();
//            }
            room.getMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    p0?.forEach { t: RoomMember? ->
                        t?.userId?.let {
                            //                            if (t.userId.compareTo(session!!.myUserId) != 0) {
                            var avatar = "";
                            if (t?.avatarUrl.isNullOrEmpty() || t == null) {
                                avatar = "";
                            } else {
                                var result = session!!.contentManager.getDownloadableUrl(t?.avatarUrl);
                                result?.let { avatar = result }
                            };
                            users.add(User(name = t?.name, roomId = "", status = 0, avatarUrl = avatar, id = t?.userId));
//                            }
                        }
                    }
                    emitter.onNext(users);
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }
            })
        };
    }

    override fun findListUser(keyword: String): Observable<List<User>> {
        setMXSession();
        val filter: Set<String> = HashSet<String>();

        return Observable.create<List<User>> { emitter ->
            kotlin.run {
                session!!.searchUsers(keyword, 100, filter, object : ApiCallback<SearchUsersResponse> {
                    val users = ArrayList<User>();
                    override fun onSuccess(p0: SearchUsersResponse?) {
                        if (p0 != null) {
                            p0?.results?.forEach { t: org.matrix.androidsdk.rest.model.User? ->
                                kotlin.run {
                                    var avatar = "";
                                    if (t?.avatarUrl.isNullOrEmpty() || t == null) {
                                        avatar = "";
                                    } else {
                                        var result = session!!.contentManager.getDownloadableUrl(t?.avatarUrl);
                                        result?.let { avatar = result }
                                    };
                                    t?.let { user ->
                                        users.add(User(id = user.user_id, name = if (user.displayname.isNullOrEmpty()) "Riot-bot" else user.displayname, avatarUrl = avatar, status = if (user.isActive) 1 else 0, roomId = ""))
                                    }
                                }
                            }
                            emitter.onNext(users);
                            emitter.onComplete();
                        } else {
                            emitter.onNext(users);
                            emitter.onComplete();
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onNext(users);
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onNext(users);
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onNext(users);
                        emitter.onComplete();
                    }
                });
            }
        }
    }

    override fun <T> findListMessageText(keyword: String, typeOfClass: Class<T>): Observable<List<T>> {
        setMXSession();
        return Observable.create<List<T>> { emitter ->
            session!!.searchMessagesByText(keyword, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val searchResults = ArrayList<T>();
                    p0?.searchCategories?.roomEvents?.results?.forEach { t: SearchResult? ->
                        if (typeOfClass == MessageSearchText::class.java) {
                            if (t?.result?.type?.compareTo("m.room.message") == 0) {
                                val result: SearchMessageByTextResult = Gson().fromJson(t?.result?.content, SearchMessageByTextResult::class.java);
                                val room = matrixRoomToRoom(session!!.dataHandler.getRoom(t?.result.roomId));

                                searchResults.add(MessageSearchText(name = room.name, avatarUrl = room.avatarUrl, content = result.body, roomId = room.id, updatedDate = room.updatedDate) as T);
                            }
                        } else {
                            if (t?.result?.type?.compareTo("m.room.name") == 0) {
//                                val result: SearchMessageByTextResult = Gson().fromJson(t?.result?.content, SearchMessageByTextResult::class.java);
                                val room = matrixRoomToRoom(session!!.dataHandler.getRoom(t?.result.roomId));

                                searchResults.add(room as T);
                            }
                        }
                    }
                    emitter.onNext(searchResults);
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun createNewDirectMessage(userId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            directChatRoomExist(userId).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe({ t ->
                kotlin.run {
                    t?.let { s ->
                        val room = session!!.dataHandler.getRoom(s);
                        emitter.onNext(matrixRoomToRoom(room));
                        emitter.onComplete();
                    }
                }
            }, { t ->
                kotlin.run {
                    session!!.createDirectMessageRoom(userId, MXCRYPTO_ALGORITHM_MEGOLM, object : ApiCallback<String> {
                        override fun onSuccess(p0: String?) {
                            p0?.let { s ->
                                val room = session!!.dataHandler.getRoom(s);
                                emitter.onNext(matrixRoomToRoom(room));
                                emitter.onComplete();
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }
                    })
                }
            })
        }
    }

    private fun directChatRoomExist(userId: String): Observable<String> {
        return Observable.create<String> { emitter ->
            kotlin.run {
                val store = session!!.dataHandler.store;
                val directChatRoomDict: Map<String, List<String>>;
                if (store.directChatRoomsDict != null) {
                    store.directChatRoomsDict?.let { mutableMap ->
                        kotlin.run {
                            directChatRoomDict = HashMap(mutableMap);
                            if (directChatRoomDict.containsKey(userId)) {
                                val roomsList = ArrayList(directChatRoomDict[userId]);
                                var findedRoom = false;
                                roomsList.forEach { rl: String? ->
                                    kotlin.run {
                                        rl?.let { s ->
                                            val room = session!!.dataHandler.getRoom(rl, false);
                                            if (room != null) {
                                                room?.let { r ->
                                                    kotlin.run {
                                                        if (r.isReady && !r.isInvited && !r.isLeaving) {
                                                            findedRoom = true;
                                                            room.getActiveMembersAsync(object : SimpleApiCallback<List<RoomMember>>() {
                                                                override fun onSuccess(p0: List<RoomMember>?) {
                                                                    p0?.let { list ->
                                                                        list.forEach { t: RoomMember? ->
                                                                            t?.let { roomMember ->
                                                                                if (TextUtils.equals(roomMember.userId, userId)) {
                                                                                    emitter.onNext(s);
                                                                                    emitter.onComplete();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    emitter.onError(Throwable("Room is not exist"))
                                                                    emitter.onComplete();
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
                                    emitter.onComplete();
                                }
                            } else {
                                emitter.onError(Throwable("Room is not exist"))
                                emitter.onComplete();
                            }
                        }
                    }
                } else {
                    emitter.onError(Throwable("Room don't exist"))
                    emitter.onComplete();
                }
            }
        }
    }

    override fun createNewRoom(name: String, topic: String, visibility: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            session!!.createRoom(name, topic, visibility, null, MXCRYPTO_ALGORITHM_MEGOLM, object : ApiCallback<String> {
                override fun onSuccess(p0: String?) {
                    p0?.let { s ->
                        val room = session!!.dataHandler.getRoom(s)
                        emitter.onNext(matrixRoomToRoom(room));
                        emitter.onComplete();
                    }
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun inviteUsersToRoom(roomId: String, userIds: List<String>): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId);
            room.invite(userIds, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext(matrixRoomToRoom(room));
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun findMediaFiles(keyword: String): Observable<List<String>> {
        setMXSession();
        return Observable.create<List<String>> { emitter ->
            session!!.searchMediaByName(keyword, null, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val results = ArrayList<String>();

                    val result = p0?.searchCategories?.roomEvents?.results;
                    Log.d("Result Size: ", result?.size.toString());

                    emitter.onNext(results);
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message))
                    emitter.onComplete();
                }
            });
        }
    }

    override fun addToFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId);
            room?.let { r ->
                var oldTag: String? = null;
                val roomAccount = r.accountData;
                roomAccount?.let { roomAccountData ->
                    if (roomAccountData.hasTags())
                        oldTag = roomAccountData.keys?.iterator()?.next();
                }
                var tagOrder: Double = 0.0;
                tagOrder = session!!.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, "m.favourite")
                r.replaceTag(oldTag, "m.favourite", tagOrder, object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        r.replaceTag(oldTag, "m.favourite", tagOrder, object : ApiCallback<Void> {
                            override fun onSuccess(p0: Void?) {
                                emitter.onNext(matrixRoomToRoom(r));
                                emitter.onComplete();
                            }

                            override fun onUnexpectedError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }

                            override fun onMatrixError(p0: MatrixError?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }

                            override fun onNetworkError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }
                        })
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                })
            } ?: kotlin.run {
                emitter.onError(Throwable("NullPointer"))
                emitter.onComplete();
            }
        }
    }

    override fun removeFromFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room> {
        setMXSession();
        return Observable.create<vmodev.clearkeep.viewmodelobjects.Room> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId);
            room?.let { r ->
                var oldTag: String? = null;
                val roomAccount = r.accountData;
                roomAccount?.let { roomAccountData ->
                    if (roomAccountData.hasTags())
                        oldTag = roomAccountData.keys?.iterator()?.next();
                }
                var tagOrder: Double = 0.0;
                tagOrder = session!!.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, null)
                r.replaceTag(oldTag, null, tagOrder, object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        r.replaceTag(oldTag, null, tagOrder, object : ApiCallback<Void> {
                            override fun onSuccess(p0: Void?) {
                                emitter.onNext(matrixRoomToRoom(r));
                                emitter.onComplete();
                            }

                            override fun onUnexpectedError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }

                            override fun onMatrixError(p0: MatrixError?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }

                            override fun onNetworkError(p0: Exception?) {
                                emitter.onError(Throwable(p0?.message));
                                emitter.onComplete();
                            }
                        })
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                })
            } ?: kotlin.run {
                emitter.onError(Throwable("NullPointer"))
                emitter.onComplete();
            }
        }
    }

    private fun mxUrlToUrl(avatarUrl: String?): String {
        var avatar: String = "";
        if (avatarUrl.isNullOrEmpty()) {
            avatar = "";
        } else {
            var url = session!!.contentManager.getDownloadableUrl(avatarUrl);
            url?.let { avatar = it }
        };
        return avatar;
    }

    override fun getUsersInRoom(roomId: String): Observable<List<User>> {
        return Observable.create<List<User>> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId);
            room.getActiveMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    val users = ArrayList<User>();
                    p0?.forEach { t: RoomMember? ->
                        t?.let { roomMember ->
                            users.add(User(id = roomMember.userId, avatarUrl = mxUrlToUrl(roomMember.avatarUrl), name = roomMember.name, status = 0, roomId = roomId))
                            Log.d("Add User: ", roomMember.name);
                        }
                    }
                    emitter.onNext(users);
                    emitter.onComplete();
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

    override fun getListRoomAndAddUser(filters: Array<Int>): Observable<ListRoomAndRoomUserJoinReturn> {
        setMXSession();
        return Observable.create<ListRoomAndRoomUserJoinReturn> { emitter ->
            val listRoom = ArrayList<vmodev.clearkeep.viewmodelobjects.Room>();
            val listUser = ArrayList<User>();
            val listRoomUserJoin = ArrayList<RoomUserJoin>();
            val listObject = ArrayList<ListRoomAndRoomUserJoinReturn>();
            if (homeRoomsViewModel != null && homeRoomsViewModel!!.result != null) {
                homeRoomsViewModel!!.update();
                val rooms = ArrayList<Room>();
                for (filter in filters) {
                    rooms.addAll(funcs[filter].apply(homeRoomsViewModel!!.result))
                }
                var currentIndex: Int = 0;
                if (rooms.size == 0) {
                    emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin));
                    emitter.onComplete();
                } else {
                    rooms.forEach { t: Room? ->
                        t?.let { it ->
                            asyncUpdateRoomMember(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ mrId ->
                                kotlin.run {
                                    currentIndex++;
                                    listRoom.add(matrixRoomToRoom(t, if (mrId.isNotEmpty()) if (mrId[0].id.isNullOrEmpty()) "" else mrId[0].id else ""));
                                    mrId.forEach { u: User? ->
                                        u?.let { user ->
                                            listUser.add(user);
                                            listRoomUserJoin.add(RoomUserJoin(t.roomId, user.id))
                                        }
                                    }
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin));
                                        emitter.onComplete();
                                    }
                                }
                            }
                                    , { e ->
                                kotlin.run {
                                    currentIndex++;
                                    listRoom.add(matrixRoomToRoom(t));
                                    if (currentIndex == rooms.size) {
                                        emitter.onNext(ListRoomAndRoomUserJoinReturn(listRoom, listUser, listRoomUserJoin));
                                        emitter.onComplete();
                                    }
                                }
                            });
                        }
                    }
                }
            } else {
                emitter.onError(NullPointerException());
                emitter.onComplete();
            }
        }
    }

    override fun getUsersInRoomAndAddToRoomUserJoin(roomId: String): Observable<RoomAndRoomUserJoin> {
        setMXSession();
        return Observable.create<RoomAndRoomUserJoin> { emitter ->
            val room = session!!.dataHandler.getRoom(roomId);
//            Log.d("UpdateRoom", room.avatarUrl)
            val users = ArrayList<User>();
            val roomUserJoin = ArrayList<RoomUserJoin>();
            room.getMembersAsync(object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(p0: List<RoomMember>?) {
                    p0?.forEach { t: RoomMember? ->
                        t?.let { roomMember ->
                            users.add(User(id = roomMember.userId, avatarUrl = mxUrlToUrl(roomMember.avatarUrl), name = roomMember.name, status = 0, roomId = roomId))
                            roomUserJoin.add(RoomUserJoin(room.roomId, roomMember.userId))
                        }
                    }
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin));
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onNext(RoomAndRoomUserJoin(room = matrixRoomToRoom(room), users = users, roomUserJoins = roomUserJoin));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun updateUser(name: String, avatar: InputStream?): Observable<User> {
        if (avatar == null) {
            return Observable.create<User> {
                updateUser(name).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({ sn ->
                    it.onNext(User(name = sn, id = session!!.myUserId, roomId = "", status = 0, avatarUrl = ""))
                    it.onComplete();
                }, { en ->
                    it.onError(en);
                    it.onComplete();
                })
            }
        } else {
            return Observable.create<User> {
                updateUser(name).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({ sn ->
                    updateUser(avatar).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe({ sa ->
                        it.onNext(User(name = sn, id = session!!.myUserId, avatarUrl = sa, status = 0, roomId = ""));
                        it.onComplete();
                    }, { ea ->
                        it.onError(ea);
                        it.onComplete();
                    })
                }, { en ->
                    it.onError(en);
                    it.onComplete();
                })
            }
        }
    }

    override fun updateUser(name: String): Observable<String> {
        return Observable.create<String> {
            session!!.myUser.updateDisplayName(name, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    it.onNext(name);
                    it.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    it.onError(Throwable(p0?.message));
                    it.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    it.onError(Throwable(p0?.message));
                    it.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    it.onError(Throwable(p0?.message));
                    it.onComplete();
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
                            var avatar = "";
                            var result = session!!.contentManager.getDownloadableUrl(session!!.myUser.avatarUrl);
                            result?.let { avatar = result }
                            it.onNext(avatar);
                            it.onComplete();
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            it.onError(Throwable(p0?.message))
                            it.onComplete();
                        }
                    })
                }

                override fun onUploadError(p0: String?, p1: Int, p2: String?) {
                    it.onError(Throwable(p2));
                    it.onComplete();
                }
            })
        }
    }

    override fun exportNewBackupKey(passphrase: String): Observable<String> {
        setMXSession();
        return Observable.create<String> { emitter ->
            session!!.dataHandler.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.prepareKeysBackupVersion(passphrase, object : ProgressListener {
                    override fun onProgress(progress: Int, total: Int) {

                    }
                }, object : SuccessErrorCallback<MegolmBackupCreationInfo> {
                    override fun onSuccess(p0: MegolmBackupCreationInfo?) {
                        val keyBackup = mxCrypto.keysBackup;
                        p0?.let { info ->
                            keyBackup.createKeysBackupVersion(info, object : ApiCallback<KeysVersion> {
                                override fun onSuccess(kv: KeysVersion?) {
                                    kv?.let {
                                        it.version?.let {
                                            emitter.onNext(info.recoveryKey);
                                            emitter.onComplete();
                                        } ?: run {
                                            emitter.onError(Throwable("KeysVersion Value is null"));
                                            emitter.onComplete();
                                        }
                                    } ?: run {
                                        emitter.onError(Throwable("KeysVersion is null"));
                                        emitter.onComplete();
                                    }
                                }

                                override fun onUnexpectedError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }

                                override fun onMatrixError(p0: MatrixError?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }

                                override fun onNetworkError(p0: Exception?) {
                                    emitter.onError(Throwable(p0?.message));
                                    emitter.onComplete();
                                }
                            });
                        } ?: run {
                            emitter.onError(Throwable("MegolmBackupCreationInfo is null"));
                            emitter.onComplete();
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                });
            } ?: run {
                emitter.onError(Throwable("Crypto is null"));
                emitter.onComplete();
            }
        }
    }

    override fun exportRoomKey(passphrase: String): Observable<String> {
        setMXSession();
        return Observable.create<String> { emitter ->
            session!!.crypto?.let {
                it.exportRoomKeys(passphrase, object : ApiCallback<ByteArray> {
                    override fun onSuccess(p0: ByteArray?) {
                        p0?.let {
                            val info = String(it);
                            emitter.onNext(info);
                            emitter.onComplete();
                        } ?: run {
                            emitter.onError(NullPointerException());
                            emitter.onComplete();
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                })
            } ?: kotlin.run {
                emitter.onError(NullPointerException());
                emitter.onComplete();
            }
        }
    }

    override fun sendTextMessage(roomId: String, content: String): Observable<Int> {
        setMXSession();
        return Observable.create<Int> { emmiter ->
            val room = session!!.dataHandler.getRoom(roomId);
            room.sendTextMessage(content, null, org.matrix.androidsdk.rest.model.message.Message.FORMAT_MATRIX_HTML, null, object : RoomMediaMessage.EventCreationListener {
                override fun onEventCreated(roomMediaMessage: RoomMediaMessage?) {
                    roomMediaMessage?.setEventSendingCallback(object : ApiCallback<Void> {
                        override fun onSuccess(info: Void?) {
                            Log.d("Message", roomMediaMessage.event.contentAsJsonObject.toString());
                            emmiter.onNext(1);
                            emmiter.onComplete();
                        }

                        override fun onUnexpectedError(e: Exception?) {
                            Log.d("Message", e?.message);
                            emmiter.onError(Throwable(e?.message));
                            emmiter.onComplete();
                        }

                        override fun onNetworkError(e: Exception?) {
                            Log.d("Message", e?.message)
                            emmiter.onError(Throwable(e?.message));
                            emmiter.onComplete();
                        }

                        override fun onMatrixError(e: MatrixError?) {
                            Log.d("Message", e?.message)
                            emmiter.onError(Throwable(e?.message));
                            emmiter.onComplete();
                        }
                    })
                }

                override fun onEventCreationFailed(roomMediaMessage: RoomMediaMessage?, errorMessage: String?) {
                    emmiter.onError(Throwable(errorMessage));
                    emmiter.onComplete();
                }

                override fun onEncryptionFailed(roomMediaMessage: RoomMediaMessage?) {
                    emmiter.onError(Throwable("Encrypt Fail"));
                    emmiter.onComplete();
                }
            })

        }
    }

    override fun getListFileInRoom(roomId: String): Observable<List<String>> {
        setMXSession();
        return Observable.create<List<String>> { emmiter ->
            val roomIds = ArrayList<String>()
            roomIds.add(roomId);
            session!!.searchMediaByName("img", roomIds, null, object : ApiCallback<SearchResponse> {
                override fun onSuccess(p0: SearchResponse?) {
                    val result = ArrayList<String>();
                    p0?.searchCategories?.roomEvents?.let {
                        it.results.forEach {
                            result.add(it.result.content.toString());
                        }
                    }
                    emmiter.onNext(result);
                    emmiter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emmiter.onError(Throwable(p0?.message));
                    emmiter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emmiter.onError(Throwable(p0?.message));
                    emmiter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emmiter.onError(Throwable(p0?.message));
                    emmiter.onComplete();
                }
            });
        }
    }

    override fun getListSignature(id: String): Observable<List<Signature>> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypt ->
                mxCrypt?.keysBackup.mKeysBackupVersion?.let {
                    mxCrypt.keysBackup.getKeysBackupTrust(it, SuccessCallback { p0 ->
                        p0?.let {
                            val signatures = ArrayList<Signature>();
                            it.signatures.forEach {
                                var deviceId = "";
                                it.deviceId?.let {
                                    deviceId = it;
                                }
                                val signature = Signature(id = deviceId, status = if (it.valid) 1 else 0
                                        , description = if (deviceId.compareTo(mxCrypt.myDevice.deviceId) == 0) application.resources.getString(im.vector.R.string.keys_backup_settings_valid_signature_from_this_device)
                                else application.resources.getString(im.vector.R.string.keys_backup_settings_valid_signature_from_verified_device), keyBackup = id)
                                signatures.add(signature);
                            }
                            emitter.onNext(signatures);
                            emitter.onComplete();
                        } ?: kotlin.run {
                            emitter.onError(NullPointerException());
                            emitter.onComplete();
                        }
                    })

                } ?: kotlin.run {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException());
                emitter.onComplete();
            }
        }
    }

    override fun getKeyBackUpData(userId: String): Observable<KeyBackup> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let {
                    var algorithm = "";
                    var count = 0;
                    var version = "";
                    it.algorithm?.let {
                        algorithm = it;
                    }
                    it.version?.let {
                        version = it;
                    }
                    it.count?.let {
                        count = it;
                    }
                    val keyBackup = KeyBackup(id = userId, algorithm = algorithm, version = version, count = count, state = mxCrypto.keysBackup.state.ordinal)
                    emitter.onNext(keyBackup);
                    emitter.onComplete();
                } ?: kotlin.run {
                    val keyBackup = KeyBackup(id = userId, algorithm = "", version = "", count = 0, state = mxCrypto.keysBackup.state.ordinal);
                    emitter.onNext(keyBackup);
                    emitter.onComplete();
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException());
                emitter.onComplete();
            }
        }
    }

    override fun restoreBackupFromPassphrase(password: String): Observable<ImportRoomKeysResult> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let { keyBackupResult ->
                    mxCrypto.keysBackup.restoreKeyBackupWithPassword(keyBackupResult, password, null, session!!.myUserId, object : StepProgressListener {
                        var isComputingKey = false;
                        override fun onStepProgress(step: StepProgressListener.Step) {
                            when (step) {
                                is StepProgressListener.Step.ComputingKey -> {
                                    if (isComputingKey)
                                        return;
                                    isComputingKey = true;
                                    Toast.makeText(application, "Computing Key", Toast.LENGTH_SHORT).show();
                                }
                                is StepProgressListener.Step.DownloadingKey -> {
                                    Toast.makeText(application, "Downloading Key", Toast.LENGTH_SHORT).show();
                                }
                                is StepProgressListener.Step.ImportingKey -> {
                                    Toast.makeText(application, "Importing Key", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, object : ApiCallback<ImportRoomKeysResult> {
                        override fun onSuccess(p0: ImportRoomKeysResult?) {
                            p0?.let {
                                Toast.makeText(application, "Successfully", Toast.LENGTH_SHORT).show();
                                mxCrypto.keysBackup.trustKeysBackupVersion(keyBackupResult, true, object : ApiCallback<Void> {
                                    override fun onSuccess(p0: Void?) {
                                        emitter.onNext(it);
                                        emitter.onComplete();
                                    }

                                    override fun onUnexpectedError(p0: Exception?) {
                                        emitter.onError(Throwable(p0?.message));
                                        emitter.onComplete();
                                    }

                                    override fun onMatrixError(p0: MatrixError?) {
                                        emitter.onError(Throwable(p0?.message));
                                        emitter.onComplete();
                                    }

                                    override fun onNetworkError(p0: Exception?) {
                                        emitter.onError(Throwable(p0?.message));
                                        emitter.onComplete();
                                    }
                                })

                            } ?: kotlin.run {
                                emitter.onError(Throwable("No Import Room Key Result"));
                                emitter.onComplete();
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }
                    })
                } ?: kotlin.run {
                    emitter.onError(Throwable("No Key Backup Version"));
                    emitter.onComplete();
                }
            } ?: kotlin.run {
                emitter.onError(Throwable("No crypto"));
                emitter.onComplete();
            }
        }
    }

    override fun restoreBackupKeyFromRecoveryKey(key: String): Observable<ImportRoomKeysResult> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.mKeysBackupVersion?.let {
                    mxCrypto.keysBackup.restoreKeysWithRecoveryKey(it, key, null, session!!.myUserId, object : StepProgressListener {
                        var isComputingKey = false;
                        override fun onStepProgress(step: StepProgressListener.Step) {
                            when (step) {
                                is StepProgressListener.Step.ComputingKey -> {
                                    if (isComputingKey)
                                        return;
                                    isComputingKey = true;
                                    Toast.makeText(application, "Computing Key", Toast.LENGTH_SHORT).show();
                                }
                                is StepProgressListener.Step.DownloadingKey -> {
                                    Toast.makeText(application, "Downloading Key", Toast.LENGTH_SHORT).show();
                                }
                                is StepProgressListener.Step.ImportingKey -> {
                                    Toast.makeText(application, "Importing Key", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, object : ApiCallback<ImportRoomKeysResult> {
                        override fun onSuccess(p0: ImportRoomKeysResult?) {
                            p0?.let {
                                Toast.makeText(application, "Success", Toast.LENGTH_SHORT).show();
                                emitter.onNext(it);
                                emitter.onComplete();
                            } ?: kotlin.run {
                                Toast.makeText(application, "No Import Room Key Result", Toast.LENGTH_LONG).show();
                                emitter.onComplete();
                            }
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show();
                            emitter.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show();
                            emitter.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            Toast.makeText(application, p0?.message, Toast.LENGTH_SHORT).show();
                            emitter.onComplete();
                        }
                    })
                } ?: kotlin.run {
                    Toast.makeText(application, "No Key Backup Version", Toast.LENGTH_SHORT).show();
                    emitter.onComplete();
                }
            } ?: kotlin.run {
                Toast.makeText(application, "No crypto", Toast.LENGTH_SHORT).show();
                emitter.onComplete();
            }
        }
    }

    override fun getAuthDataAsMegolmBackupAuthData(): Observable<String> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto?.keysBackup.getCurrentVersion(object : ApiCallback<KeysVersionResult?> {
                    override fun onSuccess(p0: KeysVersionResult?) {
                        p0?.let {
                            it.getAuthDataAsMegolmBackupAuthData().privateKeySalt?.let {
                                emitter.onNext(it);
                                emitter.onComplete();
                            } ?: run {
                                emitter.onError(Throwable("Null Private Key Salt"));
                                emitter.onComplete();
                            }
                        } ?: run {
                            emitter.onError(Throwable("Null Keys Version Result"));
                            emitter.onComplete();
                        }
                    }

                    override fun onUnexpectedError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }

                    override fun onNetworkError(p0: Exception?) {
                        emitter.onError(Throwable(p0?.message));
                        emitter.onComplete();
                    }
                })
            } ?: kotlin.run {
                Toast.makeText(application, "No crypto", Toast.LENGTH_SHORT).show();
                emitter.onComplete();
            }
        }
    }

    override fun deleteKeyBackup(userId: String): Observable<KeyBackup> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                mxCrypto.keysBackup.currentBackupVersion?.let {
                    mxCrypto.keysBackup.deleteBackup(it, object : ApiCallback<Void> {
                        override fun onSuccess(p0: Void?) {
                            val keyBackup = KeyBackup(id = userId, algorithm = "", version = "", count = 0, state = mxCrypto.keysBackup.state.ordinal);
                            emitter.onNext(keyBackup);
                            emitter.onComplete();
                        }

                        override fun onUnexpectedError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onMatrixError(p0: MatrixError?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }

                        override fun onNetworkError(p0: Exception?) {
                            emitter.onError(Throwable(p0?.message));
                            emitter.onComplete();
                        }
                    })
                } ?: kotlin.run {
                    emitter.onError(NullPointerException("Current backup version is null"));
                    emitter.onComplete();
                }
            } ?: kotlin.run {
                emitter.onError(NullPointerException("Crypto is null"));
                emitter.onComplete();
            }
        }
    }

    override fun checkNeedBackupWhenSignOut(): Observable<Int> {
        setMXSession();
        return Observable.create { emitter ->
            var valueNext: Int = 1;
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
                valueNext = 2;

                session!!.crypto?.let {
                    it.keysBackup.state?.let {

                        if (it == KeysBackupStateManager.KeysBackupState.NotTrusted)
                            valueNext = 4;
                    }
                }
            }
            emitter.onNext(valueNext);
            emitter.onComplete();
        }
    }

    override fun checkBackupKeyTypeWhenSignIn(): Observable<Int> {
        setMXSession();
        return Observable.create { emitter ->
            session!!.crypto?.let { mxCrypto ->
                var valueNext: Int = 1;
                var listener: KeysBackupStateManager.KeysBackupStateListener? = null;
                listener = object : KeysBackupStateManager.KeysBackupStateListener {
                    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
                        if (newState != KeysBackupStateManager.KeysBackupState.CheckingBackUpOnHomeserver) {
                            when (newState) {
                                KeysBackupStateManager.KeysBackupState.NotTrusted -> valueNext = 4;
                                KeysBackupStateManager.KeysBackupState.Disabled -> valueNext = 2;
                            }
                            emitter.onNext(valueNext);
                            emitter.onComplete();
                            listener?.let { mxCrypto.keysBackup.removeListener(it) }
                        }
                    }
                }
                mxCrypto.keysBackup.addListener(listener);
            } ?: run {
                emitter.onError(Throwable("Crypto is null"))
                emitter.onComplete();
            }

        }
    }

    override fun changeRoomNotificationState(roomId: String, state: Byte): Observable<Byte> {
        setMXSession();
        return Observable.create { emitter ->
            var enumState: BingRulesManager.RoomNotificationState? = BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY;
            when (state) {
                0x01.toByte() -> enumState = BingRulesManager.RoomNotificationState.ALL_MESSAGES_NOISY;
                0x02.toByte() -> enumState = BingRulesManager.RoomNotificationState.ALL_MESSAGES;
                0x04.toByte() -> enumState = BingRulesManager.RoomNotificationState.MENTIONS_ONLY;
                0x08.toByte() -> enumState = BingRulesManager.RoomNotificationState.MUTE;
            }
            session!!.dataHandler.bingRulesManager.updateRoomNotificationState(roomId, enumState, object : BingRulesManager.onBingRuleUpdateListener {
                override fun onBingRuleUpdateSuccess() {
                    emitter.onNext(state);
                    emitter.onComplete();
                }

                override fun onBingRuleUpdateFailure(p0: String?) {
                    emitter.onError(Throwable(p0));
                    emitter.onComplete();
                }
            })
        };
    }
}