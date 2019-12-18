package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSource
import vmodev.clearkeep.rests.models.responses.FeedbackResponse
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject

class FeedbackRepository @Inject constructor(private val matrixService: MatrixService) {
    fun feedBackApp(content: String, stars: Int): LiveData<Resource<FeedbackResponse>> {
        return object : AbstractNetworkNonBoundSource<FeedbackResponse>() {
            override fun createCall(): LiveData<FeedbackResponse> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.feedBackApp(content, stars).observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData()

    }
}