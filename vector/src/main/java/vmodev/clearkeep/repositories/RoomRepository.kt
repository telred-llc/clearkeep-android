package vmodev.clearkeep.repositories

import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.databases.RoomDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val clearKeepDatabase: ClearKeepDatabase,
        private val roomDao: RoomDao,
        private val matrixService: MatrixService
) {

}