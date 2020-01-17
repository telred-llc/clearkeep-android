package vmodev.clearkeep.workermanager

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.workermanager.interfaces.IUpdateDatabaseFromMatrixEvent
import javax.inject.Inject

class UpdateDatabaseFromMatrixEvent @Inject constructor(private val application: IApplication) : IUpdateDatabaseFromMatrixEvent {
    override fun updateRoomName(roomId: String, roomName: String) {
        val inputData = Data.Builder()
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.UpdateRoomNameWorker.ROOM_ID, roomId)
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.UpdateRoomNameWorker.ROOM_NAME, roomName)
        val updateRoomNameWorker = OneTimeWorkRequestBuilder<UpdateDatabaseFromMatrixEventWorker.UpdateRoomNameWorker>().setInputData(inputData.build()).build()
        WorkManager.getInstance(application.getApplication()).enqueue(updateRoomNameWorker)
    }

    override fun insertMessage(id: String, content: String, type: String, roomId: String, userId: String, createdAt: Long) {
        val inputData = Data.Builder()
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.ID, id)
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.CONTENT, content)
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.TYPE, type)
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.ROOM_ID, roomId)
        inputData.putString(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.USER_ID, userId)
        inputData.putLong(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.CREATED_AT, createdAt)
        val insertMessageWorker = OneTimeWorkRequestBuilder<UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker>()
                .setInputData(inputData.build()).build()
        WorkManager.getInstance(application.getApplication()).enqueue(insertMessageWorker)
    }
}