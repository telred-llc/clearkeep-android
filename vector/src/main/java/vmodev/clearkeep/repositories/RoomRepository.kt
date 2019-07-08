package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.databases.RoomDao
import vmodev.clearkeep.databases.UserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.ultis.ListRoomAndRoomUserJoinReturn
import vmodev.clearkeep.ultis.RoomAndRoomUserJoin
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val roomDao: RoomDao,
        private val roomUserJoinDao: AbstractRoomUserJoinDao,
        private val userDao: UserDao,
        private val matrixService: MatrixService
) {
    fun loadListRoom(filters: Array<Int>, type: Int = 0): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkBoundSource<List<Room>, List<Room>>() {
            override fun saveCallResult(item: List<Room>) {
                roomDao.insertRooms(item);
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return data == null || data.isEmpty();
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                if (type == 0) {
                    return roomDao.loadWithType(filters);
                } else {
                    return roomDao.loadWithTypeOnlyTime(filters);
                }
            }

            override fun createCall(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoom(filters)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun loadListRoomUserJoin(filters: Array<Int>, type: Int = 0): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkBoundSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
            override fun saveCallResult(item: ListRoomAndRoomUserJoinReturn) {
                roomDao.insertRooms(item.rooms);
                userDao.insertUsers(item.users);
                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return data.isNullOrEmpty();
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                if (type == 0) {
                    return roomDao.loadWithType(filters);
                } else {
                    return roomDao.loadWithTypeOnlyTime(filters);
                }
            }

            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun loadRoom(id: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.insert(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById(id);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getRoomWithId(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    @SuppressLint("CheckResult")
    fun updateOrCreateRoomFromRemote(id: String) {
//        matrixService.getRoomWithId(id).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe { t: Room? ->
//            run {
//                roomDao.updateRoom(id = t!!.id, updatedDate = t!!.updatedDate
//                        , notifyCount = t!!.notifyCount, avatarUrl = t!!.avatarUrl, type = t!!.type, name = t!!.name);
//            }
//        };
//        object : AbstractNetworkCreateOrUpdateSource<Room, RoomAndRoomUserJoin>() {
//            override fun loadFromDb(): LiveData<Room> {
//                return roomDao.findById(id);
//            }
//
//            override fun createNewItem(item: RoomAndRoomUserJoin) {
//                roomDao.insert(item.room);
//                userDao.insertUsers(item.users);
//                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins);
//            }
//
//            override fun updateItem(item: RoomAndRoomUserJoin) {
//                roomDao.updateRoom(item.room);
//                userDao.updateUsers(item.users);
//                roomUserJoinDao.updateRoomUserJoin(item.roomUserJoins);
//            }
//
//            override fun createCall(): LiveData<RoomAndRoomUserJoin> {
//                return LiveDataReactiveStreams.fromPublisher(matrixService.getUsersInRoomAndAddToRoomUserJoin(id)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .toFlowable(BackpressureStrategy.LATEST));
//            }
//        }.asLiveData();
        matrixService.getUsersInRoomAndAddToRoomUserJoin(id).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe { t: RoomAndRoomUserJoin? ->
            val room = roomDao.findByIdSync(id);
            t?.let { r ->
                if (r.room.status.compareTo(0) == 0 || r.room.updatedDate.compareTo(0) == 0)
                    return@let;
                if (room == null) {
                    Observable.create<Int> {
                        roomDao.insert(r.room);
                        userDao.insertUsers(r.users);
                        roomUserJoinDao.insertRoomUserJoins(r.roomUserJoins);
                        it.onNext(1);
                        it.onComplete();
                    }.subscribeOn(Schedulers.io()).subscribe();
                } else {
                    Observable.create<Int> {
                        roomDao.updateRoom(r.room);
                        userDao.updateUsers(r.users);
                        roomUserJoinDao.updateRoomUserJoin(r.roomUserJoins);
                        Log.d("UpdateRoom", room.id + "---" + room.avatarUrl)
                        it.onNext(1);
                        it.onComplete();
                    }.subscribeOn(Schedulers.io()).subscribe();
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun insertRoom(id: String) {
        matrixService.getRoomWithId(id).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe({ t ->
            t?.let { roomDao.insert(it) }
        }, { t -> Log.d("Error: ", t.message) });
    }

    @SuppressLint("CheckResult")
    fun joinRoom(id: String): LiveData<Resource<Room>> {
        matrixService.joinRoom(id).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe { t: Room? ->
            run {
                roomDao.updateRoom(t!!.id, t!!.type);
            }
        };
        return loadRoom(id);
    }

    fun leaveRoom(id: String): LiveData<Resource<String>> {
        return object : AbstractNetworkBoundSource<String, String>() {
            override fun saveCallResult(item: String) {
                roomDao.deleteRoom(item)
            }

            override fun shouldFetch(data: String?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<String> {
                return roomDao.findNameById(id);
            }

            override fun createCall(): LiveData<String> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.leaveRoom(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    fun createDirectChatRoom(userId: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSourceWithParams<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return roomDao.findById(param.id);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewDirectMessage(userId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    fun createNewRoom(name: String, topic: String, visibility: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSourceWithParams<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return roomDao.findById(param.id);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewRoom(name, topic, visibility)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    fun inviteUsersToRoom(data: InviteUsersToRoomObject): LiveData<Resource<Room>> {
        return object : AbstractNetworkNonBoundSource<Room>() {
            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.inviteUsersToRoom(data.roomId, data.userIds).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun updateRoomMemberStatus(roomMemberId: String, status: Byte) {
        Observable.create<Int> { emitter ->
            kotlin.run {
                roomDao.updateRoomMemberStatus(roomMemberId, status);
                emitter.onComplete();
            }
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
    }

    fun findListRoomWithText(keyword: String): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkNonBoundSource<List<Room>>() {
            override fun createCall(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, Room::class.java)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    fun addToFavourite(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.updateType(item.id, item.type);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById(roomId);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.addToFavourite(roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    fun removeFromFavourite(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.updateType(item.id, item.type);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById(roomId);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.removeFromFavourite(roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

    fun loadUsersWithRoomId(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkBoundSource<List<User>, RoomAndRoomUserJoin>() {
            override fun saveCallResult(item: RoomAndRoomUserJoin) {
                roomDao.insert(item.room);
                userDao.insertUsers(item.users);
                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins);
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return roomUserJoinDao.getUsersWithRoomId(roomId);
            }

            override fun createCall(): LiveData<RoomAndRoomUserJoin> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getUsersInRoomAndAddToRoomUserJoin(roomId)
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun updateAllRoomWhenStartApp(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkBoundSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
            override fun saveCallResult(item: ListRoomAndRoomUserJoinReturn) {
                roomDao.insertRooms(item.rooms);
                userDao.insertUsers(item.users);
                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.loadWithType(filters);

            }

            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun updateAndCreateListRoom(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkCreateAndUpdateSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
            override fun insertResult(item: ListRoomAndRoomUserJoinReturn) {
                roomDao.insertRooms(item.rooms);
                userDao.insertUsers(item.users);
                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
            }

            override fun updateResult(item: ListRoomAndRoomUserJoinReturn) {
                roomDao.updateRooms(item.rooms);
                userDao.updateUsers(item.users);
                roomUserJoinDao.updateRoomUserJoin(item.roomUserJoins);
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.loadWithType(filters);
            }

            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun getItemInsert(localData: List<Room>?, remoteData: ListRoomAndRoomUserJoinReturn?): ListRoomAndRoomUserJoinReturn {
                val rooms = ArrayList<Room>();
                var users = ArrayList<User>();
                var roomUserJoins = ArrayList<RoomUserJoin>();
                remoteData?.let { remote ->
                    users.addAll(remote.users);
                    roomUserJoins.addAll(remote.roomUserJoins);
                    rooms.addAll(remote.rooms);
                }
                return ListRoomAndRoomUserJoinReturn(rooms, users, roomUserJoins);
            }

            override fun getItemUpdate(localData: List<Room>?, remoteData: ListRoomAndRoomUserJoinReturn?): ListRoomAndRoomUserJoinReturn {
                val rooms = ArrayList<Room>();
                var users = ArrayList<User>();
                var roomUserJoins = ArrayList<RoomUserJoin>();
                var hashMapRemoteRoom = HashMap<String, Room>();
                remoteData?.let { remote ->
                    users.addAll(remote.users);
                    roomUserJoins.addAll(remote.roomUserJoins);
                    remote.rooms.forEach { hashMapRemoteRoom.put(it.id, it) }
                    localData?.let { local ->
                        local.forEach { r ->
                            hashMapRemoteRoom[r.id]?.let {
                                if (r.updatedDate != it.updatedDate)
                                    rooms.add(it);
                            };
                        }
                    }
                }
                Log.d("ListSize", rooms.size.toString());
                return ListRoomAndRoomUserJoinReturn(rooms, users, roomUserJoins);
            }
        }.asLiveData();
    }

    fun getListRoomWithListId(ids: List<String>): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalBoundSource<List<Room>>() {
            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.getListRoomWithListId(ids);
            }
        }.asLiveData();
    }

    fun setRoomNotify(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                roomDao.updateRoom(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById(roomId);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getRoomWithId(roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    fun searchRoomByDisplayName(filters: Array<Int>, query: String): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalLoadSouce<List<Room>>() {
            override fun loadFromDB(): LiveData<List<Room>> {
                return roomDao.searchWithDisplayName(filters[0], filters[1], query);
            }
        }.asLiveData();
    }

    fun getListFileInRoom(roomId: String): LiveData<Resource<List<String>>> {
        return object : AbstractNetworkNonBoundSource<List<String>>() {
            override fun createCall(): LiveData<List<String>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListFileInRoom(roomId)
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    class CreateNewRoomObject constructor(val name: String, val topic: String, val visibility: String);
    class InviteUsersToRoomObject constructor(val roomId: String, val userIds: List<String>);
}