package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkNonBoundSource<T> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val apiResponse = createCall();

        result.addSource(apiResponse) { responseData ->
            result.removeSource(apiResponse);
            if (responseData != null) {
//                result.addSource(apiResponse) {
                    setValue(Resource.success(responseData))
//                }
            } else {
                setValue(Resource.error("Error", null))
            }
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @MainThread
    protected abstract fun createCall(): LiveData<T>
}