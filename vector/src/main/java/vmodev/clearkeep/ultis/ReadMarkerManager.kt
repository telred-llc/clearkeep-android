package vmodev.clearkeep.ultis

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.support.annotation.IntDef
import android.text.TextUtils
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.activity.VectorRoomActivity
import im.vector.adapters.VectorMessagesAdapter
import im.vector.fragments.VectorMessageListFragment
import im.vector.util.ReadMarkerManager
import im.vector.util.ReadMarkerManager.LIVE_MODE
import im.vector.util.ReadMarkerManager.PREVIEW_MODE
import org.matrix.androidsdk.MXPatterns
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.adapters.MessageRow
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.util.Log
import vmodev.clearkeep.activities.RoomActivity
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.ArrayList

class ReadMarkerManager constructor(val activity: RoomActivity, val messageListFragment: VectorMessageListFragment,
                                    val session: MXSession, val room: Room?, @UpdateMode val updateMode: Int,
                                    val jumpToFirstUnreadView: View?) : VectorMessagesAdapter.ReadMarkerListener {
    private val LOG_TAG = ReadMarkerManager::class.java.simpleName

    // number of messages from the store that we allow to load for the "jump to first unread message"
    private val UNREAD_BACK_PAGINATE_EVENT_COUNT = 100

    @IntDef(LIVE_MODE, PREVIEW_MODE)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class UpdateMode

    val LIVE_MODE = 0
    val PREVIEW_MODE = 1

    private var mUpdateMode = -1

    // To track when a scroll finished
    private var mScrollState = -1

    // Views of the "Jump to..." banner
    private var mJumpToUnreadView: View? = null
    private var mCloseJumpToUnreadView: View? = null
    private var mJumpToUnreadViewSpinner: View? = null

    private var mActivity: RoomActivity? = null
    private var mVectorMessageListFragment: VectorMessageListFragment? = null
    private var mSession: MXSession? = null
    private var mRoom: Room? = null
    private var mRoomSummary: RoomSummary? = null

    private var mReadMarkerEventId: String? = null

    // Visible events from the listview
    private var mFirstVisibleEvent: Event? = null
    private var mLastVisibleEvent: Event? = null

    // Set to true when user jumped to first unread message, false otherwise
    // Used to know whether we need to update the read marker while scrolling up or down
    private var mHasJumpedToFirstUnread: Boolean = false
    // Set to true after used jumped to bottom, false otherwise
    // Used to make sure we check if the read marker has to be updated after reaching the bottom
    private var mHasJumpedToBottom: Boolean = false

    /*
     * *********************************************************************************************
     * Constructor
     * *********************************************************************************************
     */

    init {
        room?.let {

            mActivity = activity
            mVectorMessageListFragment = messageListFragment
            mSession = session

            mRoom = room
            mRoomSummary = mRoom?.dataHandler?.store?.getSummary(mRoom?.roomId)

            mReadMarkerEventId = mRoomSummary?.readMarkerEventId
            Log.d(LOG_TAG, "Create ReadMarkerManager instance id:" + mReadMarkerEventId + " for room:" + mRoom?.roomId)

            mUpdateMode = updateMode

            if (jumpToFirstUnreadView != null) {
                mJumpToUnreadView = jumpToFirstUnreadView
                val jumpToUnreadLabel = jumpToFirstUnreadView.findViewById<TextView>(R.id.jump_to_first_unread_label)
                jumpToUnreadLabel.paintFlags = jumpToUnreadLabel.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                // Actions views
                mCloseJumpToUnreadView = jumpToFirstUnreadView.findViewById(R.id.close_jump_to_first_unread)
                mJumpToUnreadViewSpinner = jumpToFirstUnreadView.findViewById(R.id.jump_to_read_spinner)

                if (isLiveMode()) {
                    jumpToUnreadLabel.setOnClickListener {
                        // force to dismiss the keyboard
                        // on some devices, it is not closed
                        activity.dismissKeyboard()

                        // Make sure read marker didn't change
                        updateReadMarkerValue()

                        if (!TextUtils.isEmpty(mReadMarkerEventId)) {
                            val lastReadEvent = mRoom?.dataHandler?.store?.getEvent(mReadMarkerEventId, mRoom?.roomId)
                            if (lastReadEvent == null) {
                                // Event is not in store, open preview
                                openPreviewToGivenEvent(mReadMarkerEventId)
                            } else {
                                // Event is in memory, scroll up to it
                                scrollUpToGivenEvent(lastReadEvent)
                            }
                        }
                    }
                    mCloseJumpToUnreadView!!.setOnClickListener { forgetReadMarker() }
                }
            }
        }
    }

    /*
     * *********************************************************************************************
     * Public methods
     * *********************************************************************************************
     */

    /**
     * Called after the activity/fragment resumed
     */
    fun onResume() {
        mVectorMessageListFragment?.let { vectorMessageListFragment ->
            (vectorMessageListFragment.messageAdapter as VectorMessagesAdapter).setReadMarkerListener(this)
            updateJumpToBanner()
        }
    }

    /**
     * Called after the activity/fragment paused
     */
    fun onPause() {
        if (!isLiveMode() || mHasJumpedToFirstUnread) {
            setReadMarkerToLastVisibleRow()
        }
    }

    /**
     * Called during scroll on the listview
     *
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     * @param eventAtTop
     * @param eventAtBottom
     */
    fun onScroll(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int,
                 eventAtTop: Event, eventAtBottom: Event) {
        mFirstVisibleEvent = eventAtTop
        mLastVisibleEvent = eventAtBottom

        if (isLiveMode()) {
            updateJumpToBanner()
        } else {
            mVectorMessageListFragment?.let {
                if (mVectorMessageListFragment!!.eventTimeLine.hasReachedHomeServerForwardsPaginationEnd()) {
                    // Display "You've caught up" message if necessary
                    val messageListView = mVectorMessageListFragment!!.messageListView
                    if (messageListView != null && firstVisibleItem + visibleItemCount == totalItemCount
                            && messageListView.getChildAt(messageListView.childCount - 1).bottom == messageListView.bottom) {
                        mActivity!!.setResult(Activity.RESULT_OK)
                        mActivity!!.finish()
                    }
                }
            }
        }
    }

    /**
     * Called at the end of a scroll action
     *
     * @param scrollState the scroll state
     */
    fun onScrollStateChanged(scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && (mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING || mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)) {
            checkUnreadMessage()
        }

        mScrollState = scrollState
    }

    /**
     * Called when we received a new read marker for the room we are monitoring
     * I.e. when read marker has been changed from another client
     *
     * @param roomId
     */
    fun onReadMarkerChanged(roomId: String) {
        if (TextUtils.equals(mRoom?.roomId, roomId)) {
            val newReadMarkerEventId = mRoomSummary?.readMarkerEventId
            if (!TextUtils.equals(newReadMarkerEventId, mReadMarkerEventId)) {
                Log.d(LOG_TAG, "onReadMarkerChanged$newReadMarkerEventId")
                refresh()
            }
        }
    }

    /**
     * Handle jump to bottom action
     */
    fun handleJumpToBottom() {
        // Set flag to be sure we check unread messages after updating the "Jump to" view
        // since on onScrollStateChanged will not be triggered
        mHasJumpedToBottom = true

        if (isLiveMode() && mHasJumpedToFirstUnread) {
            // Update read marker to the last visible event before jumping down
            setReadMarkerToLastVisibleRow()
            mHasJumpedToFirstUnread = false
        }
        mVectorMessageListFragment!!.messageAdapter.updateReadMarker(mReadMarkerEventId, mRoomSummary?.readReceiptEventId)
        mVectorMessageListFragment!!.scrollToBottom(0)
    }

    /*
     * *********************************************************************************************
     * Private methods
     * *********************************************************************************************
     */

    /**
     * Check if the read marker value has to be updated
     */
    private fun checkUnreadMessage() {
        Log.d(LOG_TAG, "checkUnreadMessage")
        mJumpToUnreadView?.let {
            if (mJumpToUnreadView!!.visibility != View.VISIBLE) {
                val readReceiptEventId = mRoomSummary?.readReceiptEventId
                if (mReadMarkerEventId != null && mReadMarkerEventId != readReceiptEventId) {
                    if (isLiveMode() && !mHasJumpedToFirstUnread) {
                        // We are catching up as scrolling up
                        // Check if the first unread has been reached by scrolling up
                        val unreadRow = mVectorMessageListFragment!!.messageAdapter.getMessageRow(mReadMarkerEventId)
                        if (unreadRow != null && unreadRow.event != null && mFirstVisibleEvent != null
                                && unreadRow.event.getOriginServerTs() >= mFirstVisibleEvent!!.getOriginServerTs()) {
                            Log.d(LOG_TAG, "checkUnreadMessage: first unread has been reached by scrolling up")
                            forgetReadMarker()
                        }
                    } else if (mLastVisibleEvent != null) {
                        // We are catching up as scrolling down
                        // Check if the last received event has been reached by scrolling down
                        if (mLastVisibleEvent!!.eventId == mRoomSummary?.latestReceivedEvent?.eventId) {
                            Log.d(LOG_TAG, "checkUnreadMessage: last received event has been reached by scrolling down")
                            markAllAsRead()
                        } else if (!isLiveMode()) {
                            Log.d(LOG_TAG, "checkUnreadMessage: preview mode, set read marker to last visible row")
                            setReadMarkerToLastVisibleRow()
                        }
                    }
                }
            }
        }
    }

    /**
     * Make sure we have the correct read marker event id
     */
    private fun updateReadMarkerValue() {
        mReadMarkerEventId = mRoomSummary?.readMarkerEventId
        mVectorMessageListFragment!!.messageAdapter.updateReadMarker(mReadMarkerEventId, mRoomSummary?.readReceiptEventId)
    }

    /**
     * Refresh the current read marker event id and make all the checks again
     */
    private fun refresh() {
        Log.d(LOG_TAG, "refresh")
        updateReadMarkerValue()
        updateJumpToBanner()
        checkUnreadMessage()
    }

    /**
     * Check if we display "Jump to" banner
     */
    @Synchronized
    private fun updateJumpToBanner() {
        //Log.d(LOG_TAG, "updateJumpToBanner");
        var showJumpToView = false

        mReadMarkerEventId = mRoomSummary?.readMarkerEventId
        if (mRoomSummary != null && mReadMarkerEventId != null && !mHasJumpedToFirstUnread) {
            val readReceiptEventId = mRoomSummary?.readReceiptEventId

            if (mReadMarkerEventId != readReceiptEventId) {
                if (!MXPatterns.isEventId(mReadMarkerEventId)) {
                    // Read marker is invalid, ignore it as it should not occur
                    Log.e(LOG_TAG, "updateJumpToBanner: Read marker event id is invalid, ignore it as it should not occur")
                } else {
                    val readMarkerEvent = getEvent(mReadMarkerEventId)
                    if (readMarkerEvent == null) {
                        // Event is not in store so we assume it is further in the past
                        // Note: preview will be opened to the last read since we have no way to
                        // determine the event id of the first unread
                        showJumpToView = true
                    } else {
                        // Last read event is in the store
                        val roomMessagesCol = mRoom?.dataHandler?.store?.getRoomMessages(mRoom?.roomId)
                        if (roomMessagesCol == null) {
                            Log.e(LOG_TAG, "updateJumpToBanner getRoomMessages returned null instead of collection with event " + readMarkerEvent.eventId)
                        } else {
                            val roomMessages = ArrayList(roomMessagesCol)
                            val lastReadEventIndex = roomMessages.indexOf(readMarkerEvent)
                            val firstUnreadEventIndex = if (lastReadEventIndex != -1) lastReadEventIndex + 1 else -1
                            if (firstUnreadEventIndex != -1 && firstUnreadEventIndex < roomMessages.size) {
                                val firstUnreadEvent = roomMessages[firstUnreadEventIndex]
                                if (mFirstVisibleEvent != null && firstUnreadEvent != null) {
                                    if (firstUnreadEvent.getOriginServerTs() > mFirstVisibleEvent!!.getOriginServerTs()) {
                                        // Beginning of first unread message is visible
                                        showJumpToView = false
                                    } else if (firstUnreadEvent.getOriginServerTs() == mFirstVisibleEvent!!.getOriginServerTs()) {
                                        // Check if beginning of first unread message is visible
                                        val listView = mVectorMessageListFragment!!.messageListView
                                        val firstUnreadView = listView?.getChildAt(0)
                                        showJumpToView = firstUnreadView != null && firstUnreadView.top < 0
                                        if (mHasJumpedToFirstUnread && !showJumpToView) {
                                            forgetReadMarker()
                                        }
                                    } else {
                                        // Beginning of first unread message is hidden
                                        showJumpToView = true
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mVectorMessageListFragment!!.messageAdapter != null) {
                mVectorMessageListFragment!!.messageAdapter.updateReadMarker(mReadMarkerEventId, readReceiptEventId)
            }
        }

        // Update "jump to" view's visibility
        if (isLiveMode() && showJumpToView) {
            mJumpToUnreadViewSpinner!!.visibility = View.GONE
            mCloseJumpToUnreadView!!.visibility = View.VISIBLE
            mJumpToUnreadView!!.visibility = View.VISIBLE
        } else {
            mJumpToUnreadView!!.visibility = View.GONE
        }

        if (mHasJumpedToBottom) {
            mHasJumpedToBottom = false
            checkUnreadMessage()
        }
    }

    /**
     * Try to retrieve an event from its id
     *
     * @param eventId
     * @return event if found
     */
    private fun getEvent(eventId: String?): Event? {
        val readMarkerRow = mVectorMessageListFragment!!.messageAdapter.getMessageRow(eventId)
        var readMarkerEvent: Event? = readMarkerRow?.event
        if (readMarkerEvent == null) {
            readMarkerEvent = mVectorMessageListFragment!!.eventTimeLine.store.getEvent(mReadMarkerEventId, mRoom?.roomId)
        }
        return readMarkerEvent
    }

    /**
     * Get whether we are in live mode or not
     *
     * @return
     */
    private fun isLiveMode(): Boolean {
        return mUpdateMode == LIVE_MODE
    }

    /**
     * Open the room in preview mode to the given event id
     *
     * @param eventId
     */
    private fun openPreviewToGivenEvent(eventId: String?) {
        if (!TextUtils.isEmpty(eventId)) {
            val intent = Intent(mActivity, VectorRoomActivity::class.java)
            intent.putExtra(VectorRoomActivity.EXTRA_ROOM_ID, mRoom?.roomId)
            intent.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, mSession!!.myUserId)
            intent.putExtra(VectorRoomActivity.EXTRA_EVENT_ID, eventId)
            intent.putExtra(VectorRoomActivity.EXTRA_IS_UNREAD_PREVIEW_MODE, true)
            mActivity!!.startActivityForResult(intent, VectorRoomActivity.UNREAD_PREVIEW_REQUEST_CODE)
        }
    }

    /**
     * Scroll up to the given event id or open preview as a last resort
     *
     * @param event event we want to scroll up to
     */
    private fun scrollUpToGivenEvent(event: Event?) {
        if (event != null) {
            mCloseJumpToUnreadView!!.visibility = View.GONE
            mJumpToUnreadViewSpinner!!.visibility = View.VISIBLE
            Log.d(LOG_TAG, "scrollUpToGivenEvent " + event.eventId)
            if (!scrollToAdapterEvent(event)) {
                // use the cached events list
                mRoom?.timeline?.backPaginate(UNREAD_BACK_PAGINATE_EVENT_COUNT, true, object : ApiCallback<Int> {
                    override fun onSuccess(info: Int?) {
                        if (!mActivity!!.isFinishing) {
                            mVectorMessageListFragment!!.messageAdapter.notifyDataSetChanged()
                            if (!scrollToAdapterEvent(event)) {
                                openPreviewToGivenEvent(event.eventId)
                            }
                        }
                    }

                    override fun onNetworkError(e: Exception) {
                        openPreviewToGivenEvent(event.eventId)
                    }

                    override fun onMatrixError(e: MatrixError) {
                        openPreviewToGivenEvent(event.eventId)
                    }

                    override fun onUnexpectedError(e: Exception) {
                        openPreviewToGivenEvent(event.eventId)
                    }
                })
            }
        }
    }

    /**
     * Try to scroll to the given event
     *
     * @param event
     * @return true if event was in adapter and have been scrolled to
     */
    private fun scrollToAdapterEvent(event: Event?): Boolean {
        val lastReadRow = if (mVectorMessageListFragment!!.messageAdapter != null)
            mVectorMessageListFragment!!.messageAdapter.getMessageRow(event!!.eventId)
        else
            null
        if (lastReadRow != null) {
            scrollToRow(lastReadRow, true)
            return true
        } else {
            Log.d(LOG_TAG, "scrollToAdapterEvent: need to load more events in adapter or eventId is not displayed")

            if (mVectorMessageListFragment!!.messageAdapter.count > 0) {
                val firstRow = mVectorMessageListFragment!!.messageAdapter
                        .getItem(0)
                val firstEvent = firstRow?.event
                val lastRow = mVectorMessageListFragment!!.messageAdapter
                        .getItem(mVectorMessageListFragment!!.messageAdapter.count - 1)
                val lastEvent = lastRow?.event
                if (firstEvent != null && lastEvent != null && event!!.getOriginServerTs() > firstEvent.getOriginServerTs()
                        && event.getOriginServerTs() < lastEvent.getOriginServerTs()) {
                    // Event should be in adapter
                    val closestRowFromEvent = mVectorMessageListFragment!!.messageAdapter.getClosestRow(event)
                    if (closestRowFromEvent != null) {
                        scrollToRow(closestRowFromEvent, closestRowFromEvent.event.eventId == event.eventId)
                        return true
                    }
                    return false
                } else {
                    return false
                }
            } else {
                return false
            }
        }
    }

    /**
     * Scroll to the given message row or the row right after the given one if it is the last read
     *
     * @param messageRow
     * @param isLastRead
     */
    private fun scrollToRow(messageRow: MessageRow, isLastRead: Boolean) {
        mVectorMessageListFragment!!.messageListView.post {
            mVectorMessageListFragment!!.scrollToRow(messageRow, isLastRead)
            mHasJumpedToFirstUnread = true
        }
    }

    /**
     * Update the read marker position to put it on the last visible row
     */
    private fun setReadMarkerToLastVisibleRow() {
        Log.d(LOG_TAG, "setReadMarkerToLastVisibleRow")
        // Update the read marker to the last message completely displayed
        mVectorMessageListFragment?.let {
            val messageListView = mVectorMessageListFragment!!.messageListView
            if (messageListView != null && messageListView.childCount != 0 && mVectorMessageListFragment!!.messageAdapter != null) {
                val newReadMarkerEvent: Event
                val lastVisiblePos = messageListView.lastVisiblePosition
                val lastVisibleRowView = messageListView.getChildAt(messageListView.childCount - 1)
                if (lastVisibleRowView.bottom <= messageListView.bottom) {
                    // Last visible message is entirely displayed, move read marker to that message
                    newReadMarkerEvent = mVectorMessageListFragment!!.getEvent(lastVisiblePos)
                } else {
                    // Move read marker to the message before the last visible one
                    newReadMarkerEvent = mVectorMessageListFragment!!.getEvent(lastVisiblePos - 1)
                }

                // Update read marker
                // In preview mode, check events from adapter and only update if new read marker is more recent
                val currentReadMarkerEvent = getEvent(mReadMarkerEventId)
                if (currentReadMarkerEvent != null) {
                    val currentReadMarkerTs = currentReadMarkerEvent.getOriginServerTs()
                    val closestRow = mVectorMessageListFragment!!.messageAdapter.getClosestRow(newReadMarkerEvent)

                    if (null != closestRow) {
                        val closestEvent = closestRow.event
                        val newReadMarkerTs = closestEvent.getOriginServerTs()
                        Log.v(LOG_TAG, "setReadMarkerToLastVisibleRow currentReadMarkerEvent:" + currentReadMarkerEvent.eventId
                                + " TS:" + currentReadMarkerTs + " closestEvent:" + closestEvent.eventId + " TS:" + closestEvent.getOriginServerTs())
                        if (newReadMarkerTs > currentReadMarkerTs) {
                            Log.d(LOG_TAG, "setReadMarkerToLastVisibleRow update read marker to:" + newReadMarkerEvent.eventId
                                    + " isEventId:" + MXPatterns.isEventId(newReadMarkerEvent.eventId))
                            mRoom?.setReadMakerEventId(newReadMarkerEvent.eventId)
                            mRoom?.let { room -> onReadMarkerChanged(room?.roomId) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Mark all as read
     */
    private fun markAllAsRead() {
        Log.d(LOG_TAG, "markAllAsRead")
        mRoom?.markAllAsRead(null)
    }

    /**
     * Forget the current read marker (read marker event will be same as read receipt event)
     */
    private fun forgetReadMarker() {
        Log.d(LOG_TAG, "forgetReadMarker")
        mRoom?.let { room ->
            mRoom?.forgetReadMarker(object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    updateJumpToBanner()
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    updateJumpToBanner()
                }

                override fun onMatrixError(p0: MatrixError?) {
                    updateJumpToBanner()
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    updateJumpToBanner()
                }
            })
        }
    }


    /*
     * *********************************************************************************************
     * Listener
     * *********************************************************************************************
     */

    override fun onReadMarkerDisplayed(event: Event, view: View) {
        Log.d(LOG_TAG, "onReadMarkerDisplayed for " + event.eventId)
        if (!mActivity!!.isFinishing) {
            if (mLastVisibleEvent == null) {
                // In case it is triggered before any onScroll callback
                // crash reported by rage shake
                try {
                    mLastVisibleEvent = mVectorMessageListFragment!!.getEvent(mVectorMessageListFragment!!.messageListView.lastVisiblePosition)
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "## onReadMarkerDisplayed() : crash while retrieving mLastVisibleEvent " + e.message, e)
                }

            }

            if (mFirstVisibleEvent == null) {
                // In case it is triggered before any onScroll callback
                // crash reported by rage shake
                try {
                    mFirstVisibleEvent = mVectorMessageListFragment!!.getEvent(mVectorMessageListFragment!!.messageListView.firstVisiblePosition)
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "## onReadMarkerDisplayed() : crash while retrieving mFirstVisibleEvent " + e.message, e)
                }

            }

            checkUnreadMessage()
        }
    }
}