package vmodev.clearkeep.matrixsdk

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.util.Log
import im.vector.Matrix
import im.vector.util.HomeRoomsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.internal.operators.observable.ObservableAll
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.data.RoomTag
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse
import vmodev.clearkeep.viewmodelobjects.User
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixServiceImplmenmt @Inject constructor(private val application: Application) : MatrixService {

    //    @Inject
    private var session: MXSession? = null;
    private var homeRoomsViewModel: HomeRoomsViewModel? = null;
    private val funcs: Array<Function<HomeRoomsViewModel.Result, List<Room>>> = Array(255, init = { Function { t: HomeRoomsViewModel.Result -> t.directChats } })
    private fun setMXSession() {
        if (session != null)
            return;
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
        return Observable.create<User> { emitter ->
            kotlin.run {
                val myUser = session!!.myUser;

                if (myUser != null) {
                    val avatarUrl = session!!.contentManager.getDownloadableUrl(myUser.avatarUrl);
                    val user = User(name = myUser.displayname, id = myUser.user_id, avatarUrl = avatarUrl);
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
                    rooms.forEach { t: Room? ->
                        t?.let { matrixRoomToRoom(it) }?.let { listRoom.add(it) }
                    }
                    emitter.onNext(listRoom);
                    emitter.onComplete();
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

    private fun matrixRoomToRoom(room: Room): vmodev.clearkeep.viewmodelobjects.Room {
        val sourcePrimary = if (room.isDirect) 0b00000001 else 0b00000010;
        val sourceSecondary = if (room.isInvited) 0b01000000 else 0b00000000;
        val sourceThird = if ((room.accountData?.keys
                        ?: emptySet()).contains(RoomTag.ROOM_TAG_FAVOURITE)) 0b10000000 else 0b00000000;
        val avatar: String? = if (room.avatarUrl.isNullOrEmpty()) "" else session!!.contentManager.getDownloadableUrl(room.avatarUrl);
        Log.d("Room Type: ", (sourcePrimary or sourceSecondary or sourceThird).toString())
        val roomObj: vmodev.clearkeep.viewmodelobjects.Room = vmodev.clearkeep.viewmodelobjects.Room(id = room.roomId, name = room.getRoomDisplayName(application)
                , type = (sourcePrimary or sourceSecondary or sourceThird), avatarUrl = avatar!!, notifyCount = room.notificationCount
                , updatedDate = 0);
        return roomObj;
    }

    override fun findListUser(keyword: String): Observable<List<User>> {
        setMXSession();
        val filter: Set<String> = HashSet<String>();

        return Observable.create<List<User>> { emitter ->
            kotlin.run {
                session!!.searchUsers(keyword, 100, filter, object : ApiCallback<SearchUsersResponse> {
                    override fun onSuccess(p0: SearchUsersResponse?) {
                        if (p0 != null) {
                            val users = ArrayList<User>();
                            p0?.results?.forEach { t: org.matrix.androidsdk.rest.model.User? ->
                                kotlin.run {
                                    val avatar = if (t?.avatarUrl.isNullOrEmpty()) "" else session!!.contentManager.getDownloadableUrl(t?.avatarUrl);
                                    t?.let { user -> users.add(User(id = user.user_id, name = user.displayname, avatarUrl = avatar)) }
                                }
                            }
                            emitter.onNext(users);
                            emitter.onComplete();
                        } else {
                            emitter.onError(NullPointerException());
                            emitter.onComplete();
                        }
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
            }
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
                    session!!.createDirectMessageRoom(userId, object : ApiCallback<String> {
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
            session!!.createRoom(name, topic, visibility, null, null, object : ApiCallback<String> {
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
}
