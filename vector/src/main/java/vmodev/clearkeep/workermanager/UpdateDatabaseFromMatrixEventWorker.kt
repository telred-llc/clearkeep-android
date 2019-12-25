package vmodev.clearkeep.workermanager

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Single
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.workermanager.interfaces.IWorkerFactory

sealed class UpdateDatabaseFromMatrixEventWorker {
    class UpdateRoomNameWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted params: WorkerParameters, private val roomDao: AbstractRoomDao) : RxWorker(appContext, params) {
        override fun createWork(): Single<Result> {
            return Single.create { emitter ->
                val roomId = inputData.getString(ROOM_ID)
                val roomName = inputData.getString(ROOM_NAME)
                roomId?.let { id ->
                    roomName?.let { name ->
                        roomDao.updateRoomName(id, name)
                    } ?: emitter.onError(Throwable("NULL"))
                } ?: emitter.onError(Throwable("NULL"))
            }
        }

        @AssistedInject.Factory
        interface Factory : IWorkerFactory

        companion object {
            const val ROOM_ID = "ROOM_ID"
            const val ROOM_NAME = "ROOM_NAME"
        }
    }

    class InsertNewEventWorker @AssistedInject constructor(@Assisted appContext: Context, @Assisted params: WorkerParameters, private val messageDao: AbstractMessageDao
                                                           , private val roomDao: AbstractRoomDao) : RxWorker(appContext, params) {
        override fun createWork(): Single<Result> {
            return Single.create { emitter ->
                val id = inputData.getString(ID)
                val content = inputData.getString(CONTENT)
                val type = inputData.getString(TYPE)
                val roomId = inputData.getString(ROOM_ID)
                val userId = inputData.getString(USER_ID)
                val createdAt = inputData.getLong(CREATED_AT, 0)
                id?.let { id ->
                    content?.let { content ->
                        type?.let { type ->
                            roomId?.let { roomId ->
                                userId?.let { userId ->
                                    val message = Message(id = id, encryptedContent = content, messageType = type, roomId = roomId, userId = userId, createdAt = createdAt)
                                    messageDao.insert(message)
                                    roomDao.updateRoomLastMessage(roomId, id)
                                }
                            }
                        }
                    }
                }
            }
        }

        @AssistedInject.Factory
        interface Factory : IWorkerFactory

        companion object {
            const val ID = "ID"
            const val CONTENT = "CONTENT"
            const val TYPE = "TYPE"
            const val ROOM_ID = "ROOM_ID"
            const val USER_ID = "USER_ID"
            const val CREATED_AT = "CREATED_AT"
        }
    }
}