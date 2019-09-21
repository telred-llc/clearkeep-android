package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLocalBoundSource<T> @MainThread constructor() {

    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        result.addSource(loadFromDb()) {
            if (it != null) {
                setValue(Resource.success(it));
            } else {
                setValue(Resource.error("Cannot find this object", null))
            }
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @MainThread
    protected abstract fun loadFromDb(): LiveData<T>
}