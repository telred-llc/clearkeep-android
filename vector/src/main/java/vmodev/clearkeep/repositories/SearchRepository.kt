package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSource
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject

class SearchRepository @Inject constructor(val abstractRoomDao: AbstractRoomDao, val matrixService: MatrixService, val appExecutors: AppExecutors) {
    fun findMessageByText(keyword: String): LiveData<Resource<List<MessageSearchText>>> {
        return object : AbstractNetworkNonBoundSource<List<MessageSearchText>>() {
            override fun createCall(): LiveData<List<MessageSearchText>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findListMessageText(keyword, MessageSearchText::class.java)
                        .observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    fun findMediaFiles(keyword: String): LiveData<Resource<List<String>>> {
        return object : AbstractNetworkNonBoundSource<List<String>>() {
            override fun createCall(): LiveData<List<String>> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.findMediaFiles(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }
}