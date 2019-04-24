package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
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
    fun loadListRoom(filer: Int): LiveData<Resource<List<Room>>> {
        return object : MatrixBoundSource<List<Room>, List<Room>>(appExecutors) {
            override fun saveCallResult(item: List<Room>) {
                roomDao.insertRooms(item);
            }

            override fun shouldFetch(data: List<Room>?): Boolean {
                return data == null || data.isEmpty();
            }

            override fun loadFromDb(): LiveData<List<Room>> {
                return roomDao.loadWithType(filer,2);
            }

            override fun createCall(): LiveData<List<Room>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getListRoom(filer)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun loadRoom(id: String): LiveData<Resource<Room>> {
        return object : MatrixBoundSource<Room, Room>(appExecutors) {
            override fun saveCallResult(item: Room) {
                roomDao.findById(id);
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

    fun updateRoomFromRemote(id: String) {
        matrixService.getRoomWithId(id).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).subscribe { t: Room? ->
            run {
                roomDao.updateRoom(id = t!!.id, updatedDate = t!!.updatedDate
                        , notifyCount = t!!.notifyCount, avatarUrl = t!!.avatarUrl, type = t!!.type, name = t!!.name);
            }
        };
    }
}