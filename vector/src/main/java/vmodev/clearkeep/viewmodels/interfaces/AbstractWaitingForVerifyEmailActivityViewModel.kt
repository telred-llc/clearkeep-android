package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractWaitingForVerifyEmailActivityViewModel : ViewModel() {
    abstract fun getWaitingForVerifyEmailResult(): LiveData<Resource<String>>;
}