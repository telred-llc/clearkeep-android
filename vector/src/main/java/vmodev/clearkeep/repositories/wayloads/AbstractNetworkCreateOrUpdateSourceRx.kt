package vmodev.clearkeep.repositories.wayloads

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkCreateOrUpdateSourceRx<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDb();
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource);
            createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Completable.fromAction {
                    if (data != null) {
                        updateItem(it);
                    }
                    else{
                        createNewItem(it)
                    }
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    result.addSource(loadFromDb()) {
                        setValue(Resource.success(it));
                    }
                }
            }, {
                setValue(Resource.error(it.message, null));
            })
        }

    }

    fun asLiveData() = result as LiveData<Resource<T>>
    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    abstract fun loadFromDb(): LiveData<T>;
    abstract fun createNewItem(item: V);
    abstract fun updateItem(item: V);
    abstract fun createCall(): Observable<V>
}