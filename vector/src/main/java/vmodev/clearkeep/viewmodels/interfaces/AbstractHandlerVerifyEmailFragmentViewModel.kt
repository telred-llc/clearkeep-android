package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractHandlerVerifyEmailFragmentViewModel : ViewModel() {
    abstract fun getRegistrationFlowsResponseResult(): LiveData<Resource<String>>;
}