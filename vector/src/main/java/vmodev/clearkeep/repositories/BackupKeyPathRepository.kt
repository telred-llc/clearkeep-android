package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractBackupKeyPathDao
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSource
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupKeyPathRepository @Inject constructor(private val backupKeyPathDao: AbstractBackupKeyPathDao, private val matrixService: MatrixService) {
    fun createNewBackupKey(passphrase: String): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSource<String>() {
            override fun createCall(): LiveData<String> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.exportRoomKey(passphrase)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }
}