package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.ultis.Debug
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserJoinRepository @Inject constructor(private val roomUserJoinDao: AbstractRoomUserJoinDao
                                                 , private val matrixService: MatrixService, private val appExecutors: AppExecutors) {
    fun updateOrCreateRoomUserJoin(roomId: String, userId: String) {
        object : AbstractNetworkBoundSourceRx<RoomUserJoin, RoomUserJoin>() {
            override fun saveCallResult(item: RoomUserJoin) {
                roomUserJoinDao.insert(item)
            }

            override fun shouldFetch(data: RoomUserJoin?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<RoomUserJoin> {
                return roomUserJoinDao.getRoomUserJoinWithRoomIdAndUserId(roomId, userId)
            }

            override fun createCall(): Observable<RoomUserJoin> {
                return Observable.create { emitter ->
                    emitter.onNext(RoomUserJoin(roomId = roomId, userId = userId))
                    emitter.onComplete()
                }
            }
        }
    }

    fun updateOrCreateRoomUserJoinRx(roomId: String, userId: String): Observable<RoomUserJoin> {
        return object : AbstractNetworkBoundSourceReturnRx<RoomUserJoin, RoomUserJoin>() {
            override fun shouldFetch(data: RoomUserJoin?): Boolean {
                return data == null
            }

            override fun saveCallResult(item: RoomUserJoin) {
                roomUserJoinDao.insert(item)
                Debug.e("-----Room insert inviten  " + item.userId)
            }

            override fun loadFromDb(): Single<RoomUserJoin> {
                return roomUserJoinDao.getRoomUserJoinWithRoomIdAndUserIdRx(roomId, userId)
            }

            override fun createCall(): Observable<RoomUserJoin> {
                return Observable.create { emitter ->
                    emitter.onNext(RoomUserJoin(roomId = roomId, userId = userId))
                    emitter.onComplete()
                }
            }
        }.getObject()
    }

    fun insertRoomUserJoin(roomId: String, userId: String) {
        Completable.fromAction {
            roomUserJoinDao.insert(RoomUserJoin(roomId, userId))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    fun getRoomListUser(filters: Array<Int>): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalBoundSource<List<RoomListUser>>() {
            override fun loadFromDb(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomListUser(filters)
            }
        }.asLiveData()
    }

    fun getListRoomListUserWithFilterAndUserId(userId: String, filters: Array<Int>): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalBoundSource<List<RoomListUser>>() {
            override fun loadFromDb(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomWithFilterAndUserId(userId, filters)
            }
        }.asLiveData()
    }

    fun getListRoomListUserWithListRoomId(filters: List<Int>, query: String): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalLoadSource<List<RoomListUser>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.findRoomByNameContain(filters, "%" + query + "%")
            }
        }.asLiveData()
    }

//    fun getListRoomDirectoryWithListRoomId(filters: List<Int>, ecrypted: Byte, query: String): LiveData<Resource<List<RoomListUser>>> {
//        return object : AbstractLocalLoadSource<List<RoomListUser>>(appExecutors) {
//            override fun loadFromDB(): LiveData<List<RoomListUser>> {
//                return roomUserJoinDao.findRoomDirectoryByNameContain(filters, ecrypted, "%" + query + "%");
//            }
//        }.asLiveData();
//    }

    fun getRoomListUserSortWithName(filters: Array<Int>): LiveData<Resource<List<RoomListUser>>> {
        return object : AbstractLocalLoadSource<List<RoomListUser>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<RoomListUser>> {
                return roomUserJoinDao.getListRoomListUserWithFilterSortName(filters)
            }
        }.asLiveData()
    }

    fun getListUserSuggested(type: Int, userId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSource<List<User>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return roomUserJoinDao.getListUserSuggested(type, userId)
            }
        }.asLiveData()
    }

    fun getListUserMatrixContact(typeOne: Int, typeTwo: Int, userId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSource<List<User>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return roomUserJoinDao.getListUserMatrixContact(typeOne, typeTwo, userId)
            }
        }.asLiveData()
    }

    fun getRoomListUserFindByID(userId: String): LiveData<Resource<RoomListUser>> {
        return object : AbstractLocalLoadSource<RoomListUser>(appExecutors) {
            override fun loadFromDB(): LiveData<RoomListUser> {
                return roomUserJoinDao.getRoomListUserFindByID(userId)
            }
        }.asLiveData()
    }

    fun getListRoomDirectory(limit: Int, query: String): LiveData<Resource<List<PublicRoom>>> {
        return object : AbstractLoadFromNetworkRx<List<PublicRoom>>() {
            override fun createCall(): Observable<List<PublicRoom>> {
                return matrixService.getListRoomDirectory(limit, query)
            }

            override fun saveCallResult(item: List<PublicRoom>) {
                //Do something
            }
        }.asLiveData()
    }
}