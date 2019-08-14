package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import im.vector.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractLocalSettingsDao
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkBoundSourceRx
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSettingsRepository @Inject constructor(private val localSettingsDao: AbstractLocalSettingsDao) {
    fun getLocalSettingsDao(): LiveData<Resource<LocalSettings>> {
        return object : AbstractNetworkBoundSourceRx<LocalSettings, LocalSettings>() {
            override fun saveCallResult(item: LocalSettings) {
                localSettingsDao.insert(item);
            }

            override fun shouldFetch(data: LocalSettings?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<LocalSettings> {
                return localSettingsDao.getItem();
            }

            override fun createCall(): Observable<LocalSettings> {
                return Observable.create { emitter ->
                    emitter.onNext(LocalSettings(id = 0x01, theme = R.style.LightTheme));
                    emitter.onComplete();
                }
            }
        }.asLiveData();
    }

    fun changeTheme(value: Int) {
        Observable.create<Int> { emitter ->
            val result = localSettingsDao.updateTheme(0x01, value);
            emitter.onNext(result);
            emitter.onComplete();
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}