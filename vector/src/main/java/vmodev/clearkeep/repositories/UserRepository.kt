package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.databases.UserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val executors: AppExecutors
                                         , private val userDao: UserDao
                                         , private val matrixService: MatrixService) {

    private val handleUpdateUser: PublishSubject<UserHandleObject> = PublishSubject.create();

    fun loadUser(userId: String): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSource<User, User>() {
            override fun saveCallResult(item: User) {
                userDao.insert(item);
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<User> {
                return userDao.findById(userId);
            }

            override fun createCall(): LiveData<User> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getUser()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun updateUser(userId: String, name: String, avatarUrl: String) {
        handleUpdateUser.onNext(UserHandleObject(userId, name, avatarUrl))
    }

    init {
        handleUpdateUser.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread()).subscribe { t: UserHandleObject? ->
            run {
                userDao.updateUser(t!!.userId, t.name, t.avatarUrl);
            }
        };
    }

    protected fun finalize() {
        handleUpdateUser.onComplete();
    }

    fun findUserFromNetwork(keyword: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkNonBoundSource<List<User>>() {
            override fun createCall(): LiveData<List<User>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListUser(keyword).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData()
    }

    fun getUsersInRoom(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractNetworkBoundSource<List<User>, List<User>>() {
            override fun saveCallResult(item: List<User>) {
                userDao.insertUsers(item);
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data.isNullOrEmpty();
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return userDao.getUsersByRoomId(roomId);
            }

            override fun createCall(): LiveData<List<User>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getUsersInRoom(roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun updateUser(userId: String, name: String, avatarImage: InputStream?): LiveData<Resource<User>> {
        return object : AbstractNetworkBoundSource<User, User>() {
            override fun saveCallResult(item: User) {
                if (item.avatarUrl.isNullOrEmpty())
                    userDao.updateUserName(item.id, item.name)
                else
                    userDao.updateUserNameAndAvatarUrl(item.id, item.name, item.avatarUrl)
            }

            override fun shouldFetch(data: User?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<User> {
                return userDao.findById(userId)
            }

            override fun createCall(): LiveData<User> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.updateUser(name, avatarImage)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    class UserHandleObject constructor(val userId: String, val name: String, val avatarUrl: String);
}