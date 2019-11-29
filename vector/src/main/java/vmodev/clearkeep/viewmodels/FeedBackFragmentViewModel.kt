package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import vmodev.clearkeep.repositories.FeedbackRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.rests.models.responses.FeedbackResponse
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractFeedBackFragmentViewModel
import javax.inject.Inject

class FeedBackFragmentViewModel @Inject constructor(private val feedbackRepository: FeedbackRepository) : AbstractFeedBackFragmentViewModel() {
    override fun submitFeedback(content: String, stars: Int): LiveData<Resource<FeedbackResponse>> {
        return feedbackRepository.feedBackApp(content, stars)
    }


}