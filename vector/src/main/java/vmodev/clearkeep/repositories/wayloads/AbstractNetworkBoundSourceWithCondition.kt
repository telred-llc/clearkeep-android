package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNetworkBoundSourceWithCondition<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        loadDataFromNetwork();
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    private fun loadDataFromNetwork() {
        val dbSource = loadFromDB();
        val remoteSource = createCall();

        result.addSource(dbSource) { dbData ->
            if (dbData != null) {
                result.addSource(remoteSource) { remoteData ->
                    if (remoteData != null) {
                        result.removeSource(dbSource);
                        result.removeSource(remoteSource);
                        Observable.create<Int> { emitter ->
                            saveCallResult(checkRemoteSourceWithLocalSource(remoteData, dbData));
                            emitter.onNext(1);
                            emitter.onComplete();
                        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                            result.addSource(loadFromDB()) {
                                setValue(Resource.success(it))
                            }
                        }
                    } else {
                        result.removeSource(remoteSource);
                        setValue(Resource.error("Error Load Remote", null))
                    }
                }
            } else {
                result.removeSource(dbSource);
                setValue(Resource.error("Error Load DB", null))
            }
        }
    }

    fun asLiveData(): LiveData<Resource<T>> {
        return result;
    }

    @WorkerThread
    abstract fun saveCallResult(item: V)

    @MainThread
    abstract fun createCall(): LiveData<V>

    @MainThread
    abstract fun loadFromDB(): LiveData<T>

    @WorkerThread
    abstract fun checkRemoteSourceWithLocalSource(remoteData: V, localData: T): V
}