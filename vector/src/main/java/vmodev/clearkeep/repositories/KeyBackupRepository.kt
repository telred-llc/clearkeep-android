package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import vmodev.clearkeep.databases.AbstractKeyBackupDao
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkBoundSourceRx
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSource
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSourceRx
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyBackupRepository @Inject constructor(private val matrixService: MatrixService, private val keyBackupDao: AbstractKeyBackupDao) {
    fun getKeyBackup(id: String): LiveData<Resource<KeyBackup>> {
        return object : AbstractNetworkBoundSourceRx<KeyBackup, KeyBackup>() {
            override fun saveCallResult(item: KeyBackup) {
                keyBackupDao.insert(item);
            }

            override fun shouldFetch(data: KeyBackup?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<KeyBackup> {
                return keyBackupDao.findById(id);
            }

            override fun createCall(): Observable<KeyBackup> {
                return matrixService.getKeyBackUpData(id);
            }
        }.asLiveData();
    }

    fun updateKeyBackup(id: String): LiveData<Resource<KeyBackup>> {
        return object : AbstractNetworkBoundSourceRx<KeyBackup, KeyBackup>() {
            override fun saveCallResult(item: KeyBackup) {
                keyBackupDao.update(item);
            }

            override fun shouldFetch(data: KeyBackup?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<KeyBackup> {
                return keyBackupDao.findById(id);
            }

            override fun createCall(): Observable<KeyBackup> {
                return matrixService.getKeyBackUpData(id);
            }
        }.asLiveData();
    }

    fun restoreWithPassphrase(passphrase: String): LiveData<Resource<ImportRoomKeysResult>> {
        return object : AbstractNetworkNonBoundSourceRx<ImportRoomKeysResult>() {
            override fun createCall(): Observable<ImportRoomKeysResult> {
                return matrixService.restoreBackupFromPassphrase(passphrase);
            }
        }.asLiveData();
    }

    fun restoreWithRecoveryKey(key: String): LiveData<Resource<ImportRoomKeysResult>> {
        return object : AbstractNetworkNonBoundSource<ImportRoomKeysResult>() {
            override fun createCall(): LiveData<ImportRoomKeysResult> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.restoreBackupKeyFromRecoveryKey(key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun getAuthDataAsMegolmBackupAuthData(): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixService.getAuthDataAsMegolmBackupAuthData();
            }
        }.asLiveData();
    }

    fun startBackupKey(passphrase: String): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixService.exportNewBackupKey(passphrase);
            }
        }.asLiveData();
    }

    fun deleteBackupKey(userId: String): LiveData<Resource<KeyBackup>> {
        return object : AbstractNetworkBoundSourceRx<KeyBackup, KeyBackup>() {
            override fun saveCallResult(item: KeyBackup) {
                keyBackupDao.update(item);
            }

            override fun shouldFetch(data: KeyBackup?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<KeyBackup> {
                return keyBackupDao.findById(userId);
            }

            override fun createCall(): Observable<KeyBackup> {
                return matrixService.deleteKeyBackup(userId);
            }
        }.asLiveData();
    }

    fun needBackupKeyWhenSignOut(): LiveData<Resource<Int>> {
        return object : AbstractNetworkNonBoundSourceRx<Int>() {
            override fun createCall(): Observable<Int> {
                return matrixService.checkNeedBackupWhenSignOut();
            }
        }.asLiveData();
    }

    fun getBackupKeyStatusWhenSignIn(): LiveData<Resource<Int>> {
        return object : AbstractNetworkNonBoundSourceRx<Int>() {
            override fun createCall(): Observable<Int> {
                return matrixService.checkBackupKeyTypeWhenSignIn();
            }
        }.asLiveData();
    }
}