package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    fun loadUser(userId : String): LiveData<Resource<User>> {
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
}