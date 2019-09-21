package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkNonBoundSourceRx<T> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            setValue(Resource.success(it))
        }, {
            setValue(Resource.error(it.message, null))
        });
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @MainThread
    protected abstract fun createCall(): Observable<T>
}