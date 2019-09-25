package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkBoundSourceWithParamsOneTime<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        fetchFromNetwork();
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall();
        result.addSource(apiResponse) { responseData ->
            result.removeSource(apiResponse)
            if (responseData != null) {
                Observable.create<V> { emitter ->
                    saveCallResult(responseData)
                    emitter.onNext(responseData);
                    emitter.onComplete();
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val dbSource = loadFromDb(it);
                    result.addSource(dbSource) { d ->
                        result.removeSource(dbSource);
                        setValue(Resource.success(d))
                    }
                }
            } else {
                setValue(Resource.error("Error", null))
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @WorkerThread
    protected abstract fun saveCallResult(item: V)

    @MainThread
    protected abstract fun loadFromDb(param: V): LiveData<T>

    @MainThread
    protected abstract fun createCall(): LiveData<V>
}