package vmodev.clearkeep.repositories.wayloads

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Status

abstract class AbstractLoadFromNetworkRx<T> @MainThread constructor() {
    private val result = MutableLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        createCall().observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer { t ->
            Completable.fromAction {
                saveCallResult(t);
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
                setValue(Resource.success(t));
            };
        }, Consumer { e ->
            setValue(Resource.error(e.message, null))
        })
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @MainThread
    abstract fun createCall(): Observable<T>;

    @WorkerThread
    abstract fun saveCallResult(item: T);
}