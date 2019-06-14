package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLocalLoadSouce<T> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<T>>();

    init {
        result.value = Resource.loading(null);
        val dbSource = loadFromDB();
        result.addSource(dbSource) { db ->
            if (db == null) {
                setValue(Resource.error("Cannot find data", null));
            } else {
                setValue(Resource.success(db));
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