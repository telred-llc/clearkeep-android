package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.databases.UserDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.Resource
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

    class UserHandleObject constructor(val userId: String, val name: String, val avatarUrl: String);
}