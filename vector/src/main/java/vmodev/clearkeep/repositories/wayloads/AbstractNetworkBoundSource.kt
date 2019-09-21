package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.viewmodelobjects.Resource
import java.lang.NullPointerException

abstract class AbstractNetworkBoundSource<T, V> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDb();
        result.addSource(dbSource) { db ->
            result.removeSource(dbSource)
            if (shouldFetch(db)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { dbs ->
                    setValue(Resource.success(dbs))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    private fun fetchFromNetwork(dbSource: LiveData<T>) {
        val apiResponse = createCall();
        result.addSource(dbSource) {
            setValue(Resource.loading(null));
        }
        result.addSource(apiResponse) { responseData ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            if (responseData != null) {
                Observable.create<Int> { emitter ->
                    saveCallResult(responseData)
                    emitter.onNext(1);
                    emitter.onComplete();
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    result.addSource(loadFromDb()) {
                        setValue(Resource.success(it))
                    }
                }
            } else {
                Observable.create<T> { emitter ->
                    result.addSource(loadFromDb()) {
                        it?.let {
                            emitter.onNext(it);
                            emitter.onComplete();
                        }?.run {
                            emitter.onError(NullPointerException());
                            emitter.onComplete();
                        }

                    }
                }.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    setValue(Resource.success(it));
                }, {
                    setValue(Resource.error(it.message, null))
                });
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<T>>
    @WorkerThread
    protected abstract fun saveCallResult(item: V)

    @MainThread
    protected abstract fun shouldFetch(data: T?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<T>

    @MainThread
    protected abstract fun createCall(): LiveData<V>
}