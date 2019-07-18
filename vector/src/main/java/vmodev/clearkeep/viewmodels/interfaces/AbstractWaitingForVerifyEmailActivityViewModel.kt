package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractWaitingForVerifyEmailActivityViewModel : ViewModel() {
    abstract fun getWaitingForVerifyEmailResult(): LiveData<Resource<String>>;
}