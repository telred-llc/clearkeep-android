package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.rests.models.responses.FeedbackResponse
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractFeedBackFragmentViewModel : ViewModel() {
    abstract fun submitFeedback(content: String, stars: Int): LiveData<Resource<FeedbackResponse>>

}