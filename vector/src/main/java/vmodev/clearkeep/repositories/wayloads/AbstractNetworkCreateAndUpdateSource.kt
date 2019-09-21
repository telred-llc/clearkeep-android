package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkCreateAndUpdateSource<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDb();
        val remoteSource = createCall();

        result.addSource(dbSource) { dbData ->
            result.addSource(remoteSource) { remoteData ->
                result.removeSource(dbSource);
                result.removeSource(remoteSource);
                Observable.create<Int> { emitter ->
                    insertResult(getItemInsert(dbData, remoteData));
                    updateResult(getItemUpdate(dbData, remoteData));
                    emitter.onNext(1);
                    emitter.onComplete();
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    result.addSource(loadFromDb()) {
                        setValue(Resource.success(it));
                    }
                }
            }
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
    abstract fun createCall(): LiveData<V>;
    abstract fun getItemInsert(localData: T?, remoteData: V?): V;
    abstract fun getItemUpdate(localData: T?, remoteData: V?): V;
}