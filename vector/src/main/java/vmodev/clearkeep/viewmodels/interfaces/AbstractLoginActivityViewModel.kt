package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLoginActivityViewModel : ViewModel() {
    abstract fun getRegistrationFlowsResponseResult(): LiveData<Resource<String>>;
}