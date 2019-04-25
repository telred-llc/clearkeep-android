package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Resource

/**
 *
 * @param T Generic result type
 * @param V Generic request type
 * @property executors AppExecutors
 * @property result MediatorLiveData<Resource<T>>
 * @constructor
 */
abstract class MatrixBoundSource<T, V> @MainThread constructor(private val executors: AppExecutors, private val typeLoad: Int = 0) {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        @Suppress("LeakingThis")
        val dbSource = loadFromDb();
        result.addSource(dbSource) { t ->
            run {
                result.removeSource(dbSource);
                if (shouldFetch(t)) {
                    if (typeLoad == 0)
                        fetchFromNetwork(dbSource);
                    else
                        fetchFromNetwork();
                } else {
                    result.addSource(dbSource) { t ->
                        kotlin.run {
                            setValue(Resource.success(t));
                        }
                    }
                }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<T>) {
        val apiResponse = createCall();
        result.addSource(dbSource) { t ->
            kotlin.run {
                setValue(Resource.loading(t));
            }
        };
        result.addSource(apiResponse) { t ->
            kotlin.run {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                if (t != null) {
                    executors.diskIO().execute {
                        saveCallResult(t);
                        executors.mainThread().execute {
                            result.addSource(loadFromDb()) { t ->
                                run {
                                    setValue(Resource.success(t));
                                }
                            }
                        }
                    }
                } else {
                    executors.mainThread().execute {
                        result.addSource(loadFromDb()) { t ->
                            kotlin.run {
                                setValue(Resource.success(t));
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCallAsReesult();
        result.addSource(apiResponse) { t ->
            kotlin.run {
                if (t != null) {
                    executors.mainThread().execute {
                        setValue(Resource.success(t))
                    }
                }
            }
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
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

    @MainThread
    protected abstract fun createCallAsReesult(): LiveData<T>
}