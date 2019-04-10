package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class MatrixBoundSource<T, V> @MainThread constructor(private val executors: AppExecutors) {
    private val result = MediatorLiveData<Resource<T>>();
    init {
        result.value = Resource.loading(null);
    }

    protected abstract fun saveCallResult(item : V)
    protected abstract fun shouldFetch(data : T?) : Boolean
    protected abstract fun loadFromDb() : LiveData<T>
}