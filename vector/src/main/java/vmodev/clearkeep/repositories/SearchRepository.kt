package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.RoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject

class SearchRepository @Inject constructor(val roomDao: RoomDao, val matrixService: MatrixService, val appExecutors: AppExecutors) {
    fun findMessageByText(keyword: String): LiveData<Resource<List<MessageSearchText>>> {
        return object : MatrixBoundSource<List<MessageSearchText>, List<MessageSearchText>>(appExecutors, 1) {
            override fun saveCallResult(item: List<MessageSearchText>) {
                //Do something
            }

            override fun saveCallResultType(item: List<MessageSearchText>) {
                //Do Something
            }

            override fun shouldFetch(data: List<MessageSearchText>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<MessageSearchText>> {
                return MutableLiveData<List<MessageSearchText>>();
            }

            override fun createCall(): LiveData<List<MessageSearchText>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, MessageSearchText::class.java)
                        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }

            override fun createCallAsReesult(): LiveData<List<MessageSearchText>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, MessageSearchText::class.java)
                        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    fun findMediaFiles(keyword: String): LiveData<Resource<List<String>>> {
        return object : MatrixBoundSource<List<String>, List<String>>(appExecutors, 1) {
            override fun saveCallResult(item: List<String>) {
                //Do something
            }

            override fun saveCallResultType(item: List<String>) {
                //Do something
            }

            override fun shouldFetch(data: List<String>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<String>> {
                return MutableLiveData<List<String>>();
            }

            override fun createCall(): LiveData<List<String>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findMediaFiles(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }

            override fun createCallAsReesult(): LiveData<List<String>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findMediaFiles(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }
}