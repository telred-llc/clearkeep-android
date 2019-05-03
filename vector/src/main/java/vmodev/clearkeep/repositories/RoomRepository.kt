package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.RoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val roomDao: RoomDao,
        private val matrixService: MatrixService
) {
    fun loadListRoom(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return object : MatrixBoundSource<List<Room>, List<Room>>(appExecutors) {
            override fun saveCallResult(item: List<Room>) {
                roomDao.insertRooms(item);
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return data == null || data.isEmpty();
                return true;
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.loadWithType(filters);
//                return MutableLiveData<List<Room>>();
            }

            override fun createCall(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoom(filters)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun createCallAsReesult(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoom(filters)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun saveCallResultType(item: List<Room>) {
                roomDao.insertRooms(item);
            }
        }.asLiveData();
    }

    fun loadRoom(id: String): LiveData<Resource<Room>> {
        return object : MatrixBoundSource<Room, Room>(appExecutors) {
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

            override fun createCallAsReesult(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getRoomWithId(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }

            override fun saveCallResultType(item: Room) {
            }
        }.asLiveData();
    }

    @SuppressLint("CheckResult")
    fun updateRoomFromRemote(id: String) {
        matrixService.getRoomWithId(id).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe { t: Room? ->
            run {
                roomDao.updateRoom(id = t!!.id, updatedDate = t!!.updatedDate
                        , notifyCount = t!!.notifyCount, avatarUrl = t!!.avatarUrl, type = t!!.type, name = t!!.name);
            }
        };
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
        return object : MatrixBoundSource<String, String>(appExecutors, 1) {
            override fun saveCallResult(item: String) {
            }

            override fun shouldFetch(data: String?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<String> {
                return roomDao.findNameById("--");
            }

            override fun createCall(): LiveData<String> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.leaveRoom(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun createCallAsReesult(): LiveData<String> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.leaveRoom(id).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun saveCallResultType(item: String) {
                roomDao.deleteRoom(item);
            }
        }.asLiveData()
    }

    fun createDirectChatRoom(userId: String): LiveData<Resource<Room>> {
        return object : MatrixBoundSource<Room, Room>(appExecutors, 1) {
            override fun saveCallResult(item: Room) {
                roomDao.insert(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById("--");
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewDirectMessage(userId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun createCallAsReesult(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewDirectMessage(userId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun saveCallResultType(item: Room) {
            }
        }.asLiveData();
    }

    fun createNewRoom(data: CreateNewRoomObject): LiveData<Resource<Room>> {
        return object : MatrixBoundSource<Room, Room>(appExecutors, 1) {
            override fun saveCallResult(item: Room) {
                roomDao.insert(item);
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById("--");
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewRoom(data.name, data.topic, data.visibility)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST))
            }

            override fun createCallAsReesult(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.createNewRoom(data.name, data.topic, data.visibility)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST))
            }

            override fun saveCallResultType(item: Room) {
            }
        }.asLiveData();
    }

    fun inviteUsersToRoom(data: InviteUsersToRoomObject): LiveData<Resource<Room>> {
        return object : MatrixBoundSource<Room, Room>(appExecutors, 1) {
            override fun saveCallResult(item: Room) {
                //Do something
            }

            override fun shouldFetch(data: Room?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<Room> {
                return roomDao.findById("--")
            }

            override fun createCall(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.inviteUsersToRoom(data.roomId, data.userIds).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun createCallAsReesult(): LiveData<Room> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.inviteUsersToRoom(data.roomId, data.userIds).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun saveCallResultType(item: Room) {
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
        return object : MatrixBoundSource<List<Room>, List<Room>>(appExecutors, 1) {
            override fun saveCallResult(item: List<Room>) {
                // Do something
            }

            override fun saveCallResultType(item: List<Room>) {
                // Do something
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.loadWithType(0);
            }

            override fun createCall(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, Room::class.java)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun createCallAsReesult(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, Room::class.java)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    class CreateNewRoomObject constructor(val name: String, val topic: String, val visibility: String);
    class InviteUsersToRoomObject constructor(val roomId: String, val userIds: List<String>);
}