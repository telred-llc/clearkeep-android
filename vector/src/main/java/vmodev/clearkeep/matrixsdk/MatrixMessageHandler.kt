package vmodev.clearkeep.matrixsdk

import android.content.Context
import android.util.Log
import im.vector.Matrix
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.data.timeline.EventTimeline
import org.matrix.androidsdk.data.timeline.EventTimelineFactory
import org.matrix.androidsdk.fragments.MatrixMessagesFragment
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.sync.RoomResponse
import org.matrix.androidsdk.rest.model.sync.RoomSync
import org.matrix.androidsdk.rest.model.sync.RoomSyncState
import org.matrix.androidsdk.rest.model.sync.RoomSyncTimeline
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixMessageHandler
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import java.lang.Exception

class MatrixMessageHandler constructor(private val roomId: String, context: Context, messageDao: AbstractMessageDao) : IMatrixMessageHandler {

    private lateinit var session: MXSession;
    private lateinit var eventTimeline: EventTimeline;
    private lateinit var room: Room;
    private val eventTimeLineListener = object : EventTimeline.Listener {
        override fun onEvent(p0: Event?, p1: EventTimeline.Direction?, p2: RoomState?) {
//            Log.d("Message Event", p0?.contentAsJsonObject.toString())
//            Log.d("Message Raw", p0?.contentAsJsonObject.toString());
//            val result = session.dataHandler.crypto.decryptEvent(p0, null);
//            Log.d("Message Decrypt", result?.mClearEvent.toString());

            p0?.let {
                val message = Message(messageId = it.eventId, roomId = it.roomId, userId = it.sender, messageType = it.type, encryptedContent = it.contentAsJsonObject.toString())
                Observable.create<Int> { emitter ->
                    messageDao.insert(message);
                    emitter.onNext(1);
                    Log.d("Message Raw", message.messageId);
                    emitter.onComplete();
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        }
    }

    init {
        session = Matrix.getInstance(context).defaultSession;
        room = session.dataHandler.getRoom(roomId);
        eventTimeline = room.timeline;
        eventTimeline.addEventTimelineListener(eventTimeLineListener);
        session.roomsApiClient.initialSync(roomId, object : ApiCallback<RoomResponse> {
            override fun onSuccess(info: RoomResponse?) {
                val roomSync = RoomSync();
                roomSync.state = RoomSyncState();
                roomSync.state.events = info?.state;
                roomSync.timeline = RoomSyncTimeline();
                roomSync.timeline.events = info?.messages?.chunk;

                eventTimeline.handleJoinedRoomSync(roomSync, true);
            }

            override fun onUnexpectedError(e: Exception?) {
                Log.d("Init Sync Error", e?.message);
            }

            override fun onNetworkError(e: Exception?) {
                Log.d("Init Sync Error", e?.message);
            }

            override fun onMatrixError(e: MatrixError?) {
                Log.d("Init Sync Error", e?.message);
            }
        })
        eventTimeline.resetPaginationAroundInitialEvent(60, object : ApiCallback<Void> {
            override fun onSuccess(p0: Void?) {
                Log.d("Reset", "Success")
            }

            override fun onUnexpectedError(p0: Exception?) {
                Log.d("Reset", "Error")
            }

            override fun onMatrixError(p0: MatrixError?) {
                Log.d("Reset", "Error")
            }

            override fun onNetworkError(p0: Exception?) {
                Log.d("Reset", "Error")
            }
        })
    }

    override fun getHistoryMessage(): Observable<List<Message>> {
        return Observable.create<List<Message>> { emitter ->
            val listMessage = ArrayList<Message>();
            emitter.onNext(listMessage);
            emitter.onComplete();
        }
    }
}