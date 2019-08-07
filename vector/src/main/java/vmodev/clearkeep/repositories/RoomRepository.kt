package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.*
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
        private val abstractRoomDao: AbstractRoomDao,
        private val matrixService: MatrixService
) {
    fun loadListRoom(filters: Array<Int>, type: Int = 0): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkBoundSourceRx<List<Room>, List<Room>>() {
            override fun saveCallResult(item: List<Room>) {
                abstractRoomDao.insertRooms(item);
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return data == null || data.isEmpty();
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                if (type == 0) {
                    return abstractRoomDao.loadWithType(filters);
                } else {
                    return abstractRoomDao.loadWithTypeOnlyTime(filters);
                }
            }

            override fun createCall(): Observable<List<Room>> {
                return matrixService.getListRoom(filters);
            }
        }.asLiveData();
    }

//    fun loadListRoomUserJoin(filters: Array<Int>, type: Int = 0): LiveData<Resource<List<Room>>> {
//        return object : AbstractNetworkBoundSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
//            override fun saveCallResult(item: ListRoomAndRoomUserJoinReturn) {
//                abstractRoomDao.insertRooms(item.rooms);
//                abstractUserDao.insertUsers(item.users);
//                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
//            }
//
//            override fun shouldFetch(data: List<Room>?): Boolean {
//                return data.isNullOrEmpty();
//            }
//
//            override fun loadFromDb(): LiveData<List<Room>> {
//                if (type == 0) {
//                    return abstractRoomDao.loadWithType(filters);
//                } else {
//                    return abstractRoomDao.loadWithTypeOnlyTime(filters);
//                }
//            }
//
//            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
//                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .toFlowable(BackpressureStrategy.LATEST));
//            }
//        }.asLiveData();
//    }

    fun loadRoom(id: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(id);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getRoomWithId(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    @SuppressLint("CheckResult")
    fun updateOrCreateRoomFromRemote(id: String) : LiveData<Resource<Room>> {
        return object : AbstractNetworkCreateOrUpdateSourceRx<Room, Room>(){
            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(id);
            }

            override fun createNewItem(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun updateItem(item: Room) {
                abstractRoomDao.updateRoom(item);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.getRoomWithId(id);
            }
        }.asLiveData();
    }

    fun updateOrCreateRoomFromRemoteRx(id : String) : Observable<Room>{
        return object : AbstractNetworkCreateOrUpdateSourceReturnRx<Room, Room>(){
            override fun loadFromDb(): Single<Room> {
                return abstractRoomDao.findByIdRx(id);
            }

            override fun updateOrCreate(item: Room): Boolean {
                return item == null;
            }

            override fun createNewItem(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun updateItem(item: Room) {
                abstractRoomDao.updateRoom(item);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.getRoomWithId(id);
            }
        }.getObject();
    }

    @SuppressLint("CheckResult")
    fun joinRoom(id: String): LiveData<Resource<Room>> {
        matrixService.joinRoom(id).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe { t: Room? ->
            run {
                abstractRoomDao.updateRoom(t!!.id, t!!.type);
            }
        };
        return loadRoom(id);
    }

    fun leaveRoom(id: String): LiveData<Resource<String>> {
        return object : AbstractNetworkBoundSource<String, String>() {
            override fun saveCallResult(item: String) {
                abstractRoomDao.deleteRoom(item)
            }

            override fun shouldFetch(data: String?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<String> {
                return abstractRoomDao.findNameById(id);
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
                abstractRoomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return abstractRoomDao.findById(param.id);
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
                abstractRoomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return abstractRoomDao.findById(param.id);
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
                abstractRoomDao.updateType(item.id, item.type);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(roomId);
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
                abstractRoomDao.updateType(item.id, item.type);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(roomId);
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.removeFromFavourite(roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()
    }

//    fun loadUsersWithRoomId(roomId: String): LiveData<Resource<List<User>>> {
//        return object : AbstractNetworkBoundSource<List<User>, RoomAndRoomUserJoin>() {
//            override fun saveCallResult(item: RoomAndRoomUserJoin) {
//                abstractRoomDao.insert(item.room);
//                abstractUserDao.insertUsers(item.users);
//                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins);
//            }
//
//            override fun shouldFetch(data: List<User>?): Boolean {
//                return true;
//            }
//
//            override fun loadFromDb(): LiveData<List<User>> {
//                return roomUserJoinDao.getUsersWithRoomId(roomId);
//            }
//
//            override fun createCall(): LiveData<RoomAndRoomUserJoin> {
//                return LiveDataReactiveStreams.fromPublisher(matrixService.getUsersInRoomAndAddToRoomUserJoin(roomId)
//                        .observeOn(Schedulers.io())
//                        .subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
//            }
//        }.asLiveData();
//    }

//    fun updateAllRoomWhenStartApp(filters: Array<Int>): LiveData<Resource<List<Room>>> {
//        return object : AbstractNetworkBoundSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
//            override fun saveCallResult(item: ListRoomAndRoomUserJoinReturn) {
//                abstractRoomDao.insertRooms(item.rooms);
//                abstractUserDao.insertUsers(item.users);
//                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
//            }
//
//            override fun shouldFetch(data: List<Room>?): Boolean {
//                return true;
//            }
//
//            override fun loadFromDb(): LiveData<List<Room>> {
//                return abstractRoomDao.loadWithType(filters);
//
//            }
//
//            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
//                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .toFlowable(BackpressureStrategy.LATEST));
//            }
//        }.asLiveData();
//    }

//    fun updateAndCreateListRoom(filters: Array<Int>): LiveData<Resource<List<Room>>> {
//        return object : AbstractNetworkCreateAndUpdateSource<List<Room>, ListRoomAndRoomUserJoinReturn>() {
//            override fun insertResult(item: ListRoomAndRoomUserJoinReturn) {
//                abstractRoomDao.insertRooms(item.rooms);
//                abstractUserDao.insertUsers(item.users);
//                roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins)
//            }
//
//            override fun updateResult(item: ListRoomAndRoomUserJoinReturn) {
//                abstractRoomDao.updateRooms(item.rooms);
//                abstractUserDao.updateUsers(item.users);
//                roomUserJoinDao.updateRoomUserJoin(item.roomUserJoins);
//            }
//
//            override fun loadFromDb(): LiveData<List<Room>> {
//                return abstractRoomDao.loadWithType(filters);
//            }
//
//            override fun createCall(): LiveData<ListRoomAndRoomUserJoinReturn> {
//                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoomAndAddUser(filters)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .toFlowable(BackpressureStrategy.LATEST));
//            }
//
//            override fun getItemInsert(localData: List<Room>?, remoteData: ListRoomAndRoomUserJoinReturn?): ListRoomAndRoomUserJoinReturn {
//                val rooms = ArrayList<Room>();
//                var users = ArrayList<User>();
//                var roomUserJoins = ArrayList<RoomUserJoin>();
//                remoteData?.let { remote ->
//                    users.addAll(remote.users);
//                    roomUserJoins.addAll(remote.roomUserJoins);
//                    rooms.addAll(remote.rooms);
//                }
//                return ListRoomAndRoomUserJoinReturn(rooms, users, roomUserJoins);
//            }
//
//            override fun getItemUpdate(localData: List<Room>?, remoteData: ListRoomAndRoomUserJoinReturn?): ListRoomAndRoomUserJoinReturn {
//                val rooms = ArrayList<Room>();
//                var users = ArrayList<User>();
//                var roomUserJoins = ArrayList<RoomUserJoin>();
//                var hashMapRemoteRoom = HashMap<String, Room>();
//                remoteData?.let { remote ->
//                    users.addAll(remote.users);
//                    roomUserJoins.addAll(remote.roomUserJoins);
//                    remote.rooms.forEach { hashMapRemoteRoom.put(it.id, it) }
//                    localData?.let { local ->
//                        local.forEach { r ->
//                            hashMapRemoteRoom[r.id]?.let {
//                                if (r.updatedDate != it.updatedDate)
//                                    rooms.add(it);
//                            };
//                        }
//                    }
//                }
//                Log.d("ListSize", rooms.size.toString());
//                return ListRoomAndRoomUserJoinReturn(rooms, users, roomUserJoins);
//            }
//        }.asLiveData();
//    }

    fun updateAndCreateListRoomRx(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return object : AbstractNetworkCreateAndUpdateSourceRx<List<Room>, List<Room>>() {
            override fun insertResult(item: List<Room>) {
                abstractRoomDao.insertRooms(item);
            }

            override fun updateResult(item: List<Room>) {
                abstractRoomDao.updateRooms(item);
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return abstractRoomDao.loadWithType(filters);
            }

            override fun createCall(): Observable<List<Room>> {
                return matrixService.getListRoom(filters);
            }

            override fun getItemInsert(localData: List<Room>?, remoteData: List<Room>?): List<Room> {
                val rooms = ArrayList<Room>();
                remoteData?.let{
                    rooms.addAll(it)
                }
                return rooms;
            }

            override fun getItemUpdate(localData: List<Room>?, remoteData: List<Room>?): List<Room> {
                val rooms = ArrayList<Room>();
                remoteData?.let {
                    rooms.addAll(it);
                }
                return rooms;
            }
        }.asLiveData();
    }

    fun getListRoomWithListId(ids: List<String>): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalBoundSource<List<Room>>() {
            override fun loadFromDb(): LiveData<List<Room>> {
                return abstractRoomDao.getListRoomWithListId(ids);
            }
        }.asLiveData();
    }

    fun setRoomNotify(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSource<Room, Room>() {
            override fun saveCallResult(item: Room) {
                abstractRoomDao.updateRoom(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(roomId);
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
                return abstractRoomDao.searchWithDisplayName(filters[0], filters[1], query);
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

    fun changeNotificationState(roomId: String, state: Byte): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSourceRx<Room, Byte>() {
            override fun saveCallResult(item: Byte) {
                abstractRoomDao.updateNotificationState(roomId, item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return abstractRoomDao.findById(roomId);
            }

            override fun createCall(): Observable<Byte> {
                return matrixService.changeRoomNotificationState(roomId, state);
            }
        }.asLiveData();
    }

    fun getDirectChatRoomByUserId(userId: String) : LiveData<Resource<List<Room>>>{
        return object : AbstractLocalLoadSouce<List<Room>>(){
            override fun loadFromDB(): LiveData<List<Room>> {
                return abstractRoomDao.getDirectChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }
    fun getRoomChatRoomByUserId(userId: String) : LiveData<Resource<List<Room>>>{
        return object : AbstractLocalLoadSouce<List<Room>>(){
            override fun loadFromDB(): LiveData<List<Room>> {
                return abstractRoomDao.getRoomChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }

    class CreateNewRoomObject constructor(val name: String, val topic: String, val visibility: String);
    class InviteUsersToRoomObject constructor(val roomId: String, val userIds: List<String>);
}