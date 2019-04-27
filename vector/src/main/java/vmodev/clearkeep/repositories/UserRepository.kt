package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.databases.RoomDao
import vmodev.clearkeep.databases.UserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val executors: AppExecutors
                                         , private val userDao: UserDao
                                         , private val matrixService: MatrixService) {

    private val handleUpdateUser: PublishSubject<UserHandleObject> = PublishSubject.create();

    fun loadUser(userId: String): LiveData<Resource<User>> {
        return object : MatrixBoundSource<User, User>(executors) {
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

            override fun createCallAsReesult(): LiveData<User> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.getUser()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun saveCallResultType(item: User) {
            }
        }.asLiveData();
    }

    fun updateUser(userId: String, name: String, avatarUrl: String) {
//        userDao.updateUser(userId, name, avatarUrl);
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
        return object : MatrixBoundSource<List<User>, List<User>>(executors, 1) {
            override fun saveCallResult(item: List<User>) {
                // Do not save to db
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                // Always return true to get from network
                return true;
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return userDao.findUsers("---")
            }

            override fun createCall(): LiveData<List<User>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListUser(keyword).subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }

            override fun createCallAsReesult(): LiveData<List<User>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListUser(keyword).subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread()).toFlowable(BackpressureStrategy.LATEST));
            }

            override fun saveCallResultType(item: List<User>) {
            }
        }.asLiveData();
    }
    fun updateUserStatus(){

    }

    class UserHandleObject constructor(val userId: String, val name: String, val avatarUrl: String);
}