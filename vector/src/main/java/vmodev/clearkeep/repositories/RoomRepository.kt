package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
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
    fun updateOrCreateRoomFromRemote(id: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkCreateOrUpdateSourceRx<Room, Room>() {
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

    fun updateOrCreateRoomFromRemoteRx(id: String): Observable<Room> {
        return object : AbstractNetworkCreateOrUpdateSourceReturnRx<Room, Room>() {
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

    fun joinRoom(id: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSourceWithParamsOneTimeRx<Room, Room>() {
            override fun saveCallResult(item: Room) {
                abstractRoomDao.updateRoom(item.id, item.type);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return abstractRoomDao.findById(param.id);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.joinRoom(id);
            }
        }.asLiveData();
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
        return object : AbstractNetworkBoundSourceOneTimeWithParams<Room, Room>() {
            override fun saveCallResult(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return abstractRoomDao.findById(param.id);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.createNewDirectMessage(userId);
            }
        }.asLiveData()
    }

    fun createNewRoom(name: String, topic: String, visibility: String): LiveData<Resource<Room>> {
        return object : AbstractNetworkBoundSourceOneTimeWithParams<Room, Room>() {
            override fun saveCallResult(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun loadFromDb(param: Room): LiveData<Room> {
                return abstractRoomDao.findById(param.id);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.createNewRoom(name, topic, visibility);
            }
        }.asLiveData()
    }

    fun inviteUsersToRoom(data: InviteUsersToRoomObject): LiveData<Resource<Room>> {
        return object : AbstractNetworkNonBoundSourceRx<Room>() {
            override fun createCall(): Observable<Room> {
                return matrixService.inviteUsersToRoom(data.roomId, data.userIds);
            }
        }.asLiveData();
    }

    fun findListRoomWithText(keyword: String): LiveData<Resource<List<String>>> {
        return object : AbstractNetworkNonBoundSource<List<String>>() {
            override fun createCall(): LiveData<List<String>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, String::class.java)
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
                remoteData?.let {
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

    fun updateAndCreateListRoomReturnRx(filters: Array<Int>): Observable<List<Room>> {
        return object : AbstractNetworkCreateAndUpdateSourceReturnRx<List<Room>, List<Room>>() {
            override fun insertResult(item: List<Room>) {
                Log.d("InsertRoomBefore", item.size.toString());
                val result = abstractRoomDao.insertRooms(item);
                Log.d("InsertRoomAfter", result.size.toString());
            }

            override fun updateResult(item: List<Room>) {
//                Log.d("InsertRoom", item.size.toString());
//                abstractRoomDao.updateRooms(item);
            }

            override fun loadFromDb(): Single<List<Room>> {
                return abstractRoomDao.loadWithTypeRx(filters);
            }

            override fun createCall(): Observable<List<Room>> {
                return matrixService.getListRoom(filters);
            }

            override fun getInsertItem(remoteItem: List<Room>, localItem: List<Room>?): List<Room> {
                val rooms = ArrayList<Room>();
                rooms.addAll(remoteItem)
                return rooms;
            }

            override fun getUpdateItem(remoteItem: List<Room>, localItem: List<Room>?): List<Room> {
                val rooms = ArrayList<Room>();
                rooms.addAll(remoteItem);
                return rooms;
            }
        }.getObject();
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

    fun getDirectChatRoomByUserId(userId: String): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalLoadSouce<List<Room>>() {
            override fun loadFromDB(): LiveData<List<Room>> {
                return abstractRoomDao.getDirectChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }

    fun getRoomChatRoomByUserId(userId: String): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalLoadSouce<List<Room>>() {
            override fun loadFromDB(): LiveData<List<Room>> {
                return abstractRoomDao.getRoomChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }

    fun updateLastMessage(roomId: String, messageId: String) {
        Completable.fromAction { abstractRoomDao.updateRoomLastMessage(roomId, messageId) }.subscribeOn(Schedulers.io()).subscribe();
    }

    fun updateUserCreated(roomId: String, userId: String) {
        Completable.fromAction { abstractRoomDao.updateRoomCreatedUser(roomId, userId) }.subscribeOn(Schedulers.io()).subscribe();
    }

    fun insertRoomToDB(roomId: String): Observable<Room> {
        return object : AbstractNetworkBoundSourceReturnRx<Room, Room>(){
            override fun shouldFetch(data: Room?): Boolean {
                return data == null;
            }

            override fun saveCallResult(item: Room) {
                abstractRoomDao.insert(item);
            }

            override fun loadFromDb(): Single<Room> {
                return abstractRoomDao.findByIdRx(roomId);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.getRoomWithIdForCreate(roomId);
            }
        }.getObject();
    }

    fun updateRoomName(roomId: String): Observable<Room> {
        return object : AbstractNetworkCallAndSaveToDBReturnRx<Room, Room>() {
            override fun saveCallResult(item: Room) {
                Log.d("UpdateRoom", item.type.toString())
                abstractRoomDao.updateRoom(item);
            }

            override fun loadFromDb(item: Room): Single<Room> {
                return abstractRoomDao.findByIdRx(item.id);
            }

            override fun createCall(): Observable<Room> {
                return matrixService.getRoomWithIdForCreate(roomId);
            }
        }.getObject();
    }

    class CreateNewRoomObject constructor(val name: String, val topic: String, val visibility: String);
    class InviteUsersToRoomObject constructor(val roomId: String, val userIds: List<String>);
}