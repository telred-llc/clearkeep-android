package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLoginActivityViewModel : ViewModel() {
    abstract fun getRegistrationFlowsResponseResult(): LiveData<Resource<String>>;
}