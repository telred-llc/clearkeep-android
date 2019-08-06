package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.interfaces.IUserRepository
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

class UserRepository @Inject constructor(private val executors: AppExecutors
                                         , private val abstractUserDao: AbstractUserDao
                                         , private val matrixService: MatrixService) : IUserRepository {

    override fun loadUser(userId: String): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSourceRx<User, User>() {
            override fun saveCallResult(item: User) {
                abstractUserDao.insert(item);
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<User> {
                return abstractUserDao.findById(userId);
            }

            override fun createCall(): Observable<User> {
                return matrixService.getUser();
            }
        }.asLiveData();
    }

    override fun updateUser(userId: String, name: String, avatarUrl: String) {
        Completable.fromAction {
            abstractUserDao.updateUser(userId, name, avatarUrl);
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    override fun findUserFromNetwork(keyword: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkNonBoundSource<List<User>>() {
            override fun createCall(): LiveData<List<User>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListUser(keyword).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData()
    }

    override fun updateUser(userId: String, name: String, avatarImage: InputStream?): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSource<User, User>() {
            override fun saveCallResult(item: User) {
                if (item.avatarUrl.isNullOrEmpty())
                    abstractUserDao.updateUserName(item.id, item.name)
                else
                    abstractUserDao.updateUserNameAndAvatarUrl(item.id, item.name, item.avatarUrl)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<User> {
                return abstractUserDao.findById(userId)
            }

            override fun createCall(): LiveData<User> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.updateUser(name, avatarImage)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    override fun updateUserStatus(userId: String, status: Byte) {
        Observable.create<Int> { emitter ->
            val value = abstractUserDao.updateStatus(userId, status);
            emitter.onNext(value);
            emitter.onComplete();
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    override fun getListUserInRoomFromNetwork(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLoadFromNetworkRx<List<User>>() {
            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId);
            }

            override fun saveCallResult(item: List<User>) {
                abstractUserDao.insertUsers(item);
            }
        }.asLiveData();
    }

    override fun updateOrCreateNewUserFromRemote(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkCreateAndUpdateSourceRx<List<User>, List<User>>() {
            override fun insertResult(item: List<User>) {
                abstractUserDao.insertUsers(item);
            }

            override fun updateResult(item: List<User>) {
                abstractUserDao.updateUsers(item);
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return abstractUserDao.getUsersWithRoomId(roomId);
            }

            override fun createCall(): Observable<List<User>> {
                return matrixService.getUsersInRoom(roomId);
            }

            override fun getItemInsert(localData: List<User>?, remoteData: List<User>?): List<User> {
                val users = ArrayList<User>();
                remoteData?.let { r ->
                    localData?.let { l ->
                        users.addAll(r);
                    } ?: run {
                        users.addAll(r);
                    }
                } ?: run {
                    localData?.let {
                        users.addAll(it);
                    }
                }
                return users;
            }

            override fun getItemUpdate(localData: List<User>?, remoteData: List<User>?): List<User> {
                val users = ArrayList<User>();
                remoteData?.let { r ->
                    localData?.let { l ->
                        users.addAll(r);
                    } ?: run {
                        users.addAll(r);
                    }
                } ?: run {
                    localData?.let {
                        users.addAll(it);
                    }
                }
                return users;
            }
        }.asLiveData();
    }

    override fun getUsersWithId(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSouce<List<User>>() {
            override fun loadFromDB(): LiveData<List<User>> {
                return abstractUserDao.getUsersWithId(userIds);
            }
        }.asLiveData();
    }
}