package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLocalLoadSource<T> @MainThread constructor(private val executor: AppExecutors) {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDB();
        result.addSource(dbSource) { db ->
            if (db == null) {
                executor.mainThread().execute {
                    setValue(Resource.error("Cannot find data", null));
                }
            } else {
                executor.mainThread().execute {
                    setValue(Resource.success(db));
                }
            }
        }
    }

    private fun setValue(newValue: Resource<T>) {
        if (result.value != newValue)
            result.value = newValue;
    }

    abstract fun loadFromDB(): LiveData<T>;
    fun asLiveData() = result as LiveData<Resource<T>>
}