package vmodev.clearkeep.repositories.wayloads

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkBoundSourceWithParamsOneTimeRx<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        fetchFromNetwork();
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    @SuppressLint("CheckResult")
    private fun fetchFromNetwork() {
        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Observable.create<V> { emitter ->
                saveCallResult(it)
                emitter.onNext(it);
                emitter.onComplete();
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                val dbSource = loadFromDb(it);
                result.addSource(dbSource) { d ->
                    result.removeSource(dbSource);
                    setValue(Resource.success(d))
                }
            }
        }, {
            setValue(Resource.error(it.message, null));
        })
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @WorkerThread
    protected abstract fun saveCallResult(item: V)

    @MainThread
    protected abstract fun loadFromDb(param: V): LiveData<T>

    @MainThread
    protected abstract fun createCall(): Observable<V>
}