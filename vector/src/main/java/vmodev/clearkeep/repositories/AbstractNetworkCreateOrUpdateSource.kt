package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject

abstract class AbstractNetworkCreateOrUpdateSource<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val remoteSource = createCall();
        result.addSource(createCall()) { remoteData ->
            result.removeSource(remoteSource);
            if (remoteData != null) {
                val dbSource = loadFromDb();
                result.addSource(dbSource) { dbData ->
                    result.removeSource(dbSource);
                    Observable.create<Int> { emitter ->
                        if (dbData != null)
                            updateItem(remoteData);
                        else
                            createNewItem(remoteData);
                        emitter.onNext(1);
                        emitter.onComplete();
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        result.addSource(loadFromDb()) {
                            setValue(Resource.success(it));
                        }
                    };
                }
            } else {
                setValue(Resource.error("Data is null", null))
            }
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
    abstract fun createCall(): LiveData<V>;
}