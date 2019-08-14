package vmodev.clearkeep.repositories.wayloads

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkCreateAndUpdateSourceRx<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDb();
        result.addSource(dbSource) { t ->
            result.removeSource(dbSource);
            createCall().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({
                Completable.fromAction {
                    insertResult(getItemInsert(t, it))
                    updateResult(getItemUpdate(t, it))
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    result.addSource(loadFromDb()) {
                        setValue(Resource.success(it))
                    }
                }
            }, {
                setValue(Resource.error(it.message, null))
            });
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    abstract fun insertResult(item: V);
    abstract fun updateResult(item: V);
    abstract fun loadFromDb(): LiveData<T>;
    abstract fun createCall(): Observable<V>;
    abstract fun getItemInsert(localData: T?, remoteData: V?): V;
    abstract fun getItemUpdate(localData: T?, remoteData: V?): V;
}