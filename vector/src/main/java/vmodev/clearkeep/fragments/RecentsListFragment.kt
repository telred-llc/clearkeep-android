package vmodev.clearkeep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import butterknife.BindView
import im.vector.Matrix
import im.vector.PublicRoomsManager
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorPublicRoomsActivity
import im.vector.activity.VectorRoomActivity
import im.vector.fragments.VectorBaseFragment
import im.vector.services.EventStreamService
import im.vector.ui.badge.BadgeProxy
import im.vector.util.RoomUtils
import im.vector.view.RecentsExpandableListView
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.BingRulesManager
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.data.RoomTag
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.activities.PreviewInviteRoomActivity
import vmodev.clearkeep.adapters.RoomSummaryAdapter
import java.util.*

open class RecentsListFragment : VectorBaseFragment(), RoomSummaryAdapter.RoomEventListener, RecentsExpandableListView.DragAndDropEventsListener, RoomUtils.MoreActionListener {

    internal var mMatrixId: String? = null
    private var mSession: MXSession? = null
    private var mEventsListener: MXEventListener? = null
    @BindView(R.id.fragment_recents_list)
    lateinit var mRecentsListView: RecentsExpandableListView
    lateinit var mAdapter: RoomSummaryAdapter
    @BindView(R.id.listView_spinner_views)
    lateinit var mWaitingView: View

    // drag and drop management
    @BindView(R.id.fragment_recents_selected_cell_layout)
    lateinit var mSelectedCellLayout: RelativeLayout
    private var mDraggedView: View? = null
    private var mIgnoreScrollEvent: Boolean = false
    private var mOriginGroupPosition = -1
    private var mOriginChildPosition = -1
    private var mDestGroupPosition = -1
    private var mDestChildPosition = -1
    private var mIsWaitingTagOrderEcho: Boolean = false
    private var mIsWaitingDirectChatEcho: Boolean = false

    private var mFirstVisibleIndex = 0

    private var mIsPaused = false

    // set to true to force refresh when an events chunk has been processed.
    private var refreshOnChunkEnd = false

    // public room management
    private var mIsLoadingPublicRooms = false
    private var mLatestPublicRoomsRefresh = System.currentTimeMillis()

    private var mScrollToIndex = -1

    // scroll events listener
    private var mScrollEventListener: IVectorRecentsScrollEventListener? = null

    private val mPublicRoomsListener = PublicRoomsManager.PublicRoomsManagerListener { publicRoomsCount ->
        if (null != activity) {
            activity!!.runOnUiThread {
                // statuses
                mLatestPublicRoomsRefresh = System.currentTimeMillis()
                mIsLoadingPublicRooms = false

                mAdapter.setPublicRoomsCount(publicRoomsCount)
            }
        }
    }

    /**
     * Test if the attached activity managed IVectorRecentsScrollEventListener.
     *
     * @return the listener, null if it is not suppported
     */
    private val listener: IVectorRecentsScrollEventListener?
        get() {
            if (null == mScrollEventListener) {
                if (activity is IVectorRecentsScrollEventListener) {
                    mScrollEventListener = activity as IVectorRecentsScrollEventListener?
                }
            }

            return mScrollEventListener
        }

    internal val isDragAndDropSupported: Boolean = false;

    protected open fun isDragAndDropSupported(): Boolean {
        return isDragAndDropSupported;
    }

    /**
     * warns the activity when there is a scroll in the recents
     */
    interface IVectorRecentsScrollEventListener {
        // warn the user over scrolls up
        fun onRecentsListOverScrollUp()

        // warn the user scrolls up
        fun onRecentsListScrollUp()

        // warn when the user scrolls downs
        fun onRecentsListScrollDown()

        // warn when the list content can be fully displayed without scrolling
        fun onRecentsListFitsScreen()
    }

    override fun getLayoutResId(): Int {
        val args = arguments
        return args!!.getInt(ARG_LAYOUT_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments

        mMatrixId = args!!.getString(ARG_MATRIX_ID)
        mSession = Matrix.getInstance(activity)!!.getSession(mMatrixId)

        // should never happen but it happened once.
        if (null == mSession) {
            if (null != activity) {
                CommonActivityUtils.logout(activity)
            }
            return
        }

        // the chevron is managed in the header view
        mRecentsListView!!.setGroupIndicator(null)
        // create the adapter
        mAdapter = RoomSummaryAdapter(this.context!!,
                mSession,
                false,
                true,
                R.layout.item_adapter_recent_room,
                R.layout.adapter_item_vector_recent_header,
                this,
                this)

        mRecentsListView!!.setAdapter(mAdapter)

        mRecentsListView!!.mDragAndDropEventsListener = this

        // Set rooms click listener:
        // - reset the unread count
        // - start the corresponding room activity
        mRecentsListView!!.setOnChildClickListener(ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (mAdapter.isDirectoryGroupPosition(groupPosition)) {
                val intent = Intent(activity, VectorPublicRoomsActivity::class.java)
                intent.putExtra(VectorPublicRoomsActivity.EXTRA_MATRIX_ID, mSession!!.myUserId)

                if (!TextUtils.isEmpty(mAdapter.searchedPattern)) {
                    intent.putExtra(VectorPublicRoomsActivity.EXTRA_SEARCHED_PATTERN, mAdapter.searchedPattern)
                }

                activity!!.startActivity(intent)

            } else {
                val roomSummary = mAdapter.getRoomSummaryAt(groupPosition, childPosition)
                val session = Matrix.getInstance(activity)!!.getSession(roomSummary?.userId)

                // sanity check : should never happen
                // but it happened.
                if ((null == session) || (null == session!!.dataHandler)) {
                    return@OnChildClickListener true
                }

                var roomId: String = "";
                roomSummary?.roomId?.let {
                    roomId = it;
                }
                val room = session!!.dataHandler.getRoom(roomId)

                // cannot join a leaving room
                if ((null == room) || room!!.isLeaving) {
                    roomId = "";
                }

                // update the unread messages count
                if (mAdapter.resetUnreadCount(groupPosition, childPosition)) {
                    session!!.dataHandler.store!!.flushSummary(roomSummary)
                }
                // update badge unread count in case device is offline
                BadgeProxy.specificUpdateBadgeUnreadCount(mSession, context)

                // launch corresponding room activity
                if (null != roomId) {
                    val params = HashMap<String, Any>()
                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = session!!.myUserId
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId!!

                    CommonActivityUtils.goToRoomPage(activity!!, session, params)
                } else {
                    if (null == room) {
                        Log.e(LOG_TAG, "Cannot open the room $roomId because there is no matched room.")
                    } else {
                        Log.e(LOG_TAG, "Cannot open the room $roomId because the user is leaving the room.")
                    }
                }
            }

            // click is handled
            true
        })

        mRecentsListView!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
                startDragAndDrop()
                return true
            }
        }

        mRecentsListView!!.setOnScrollListener(object : AbsListView.OnScrollListener {

            // latest cell offset Y
            private var mPrevOffset = 0

            private fun onScrollUp() {
                if (null != listener) {
                    mScrollEventListener!!.onRecentsListScrollUp()
                }
            }

            private fun onScrollDown() {
                if (null != listener) {
                    mScrollEventListener!!.onRecentsListScrollDown()
                }
            }

            private fun onFitScreen() {
                if (null != listener) {
                    mScrollEventListener!!.onRecentsListFitsScreen()
                }
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if ((0 == firstVisibleItem) && ((totalItemCount + 1) < visibleItemCount)) {
                    onFitScreen()
                } else if (firstVisibleItem < mFirstVisibleIndex) {
                    mFirstVisibleIndex = firstVisibleItem
                    mPrevOffset = 0
                    onScrollUp()
                } else if (firstVisibleItem > mFirstVisibleIndex) {
                    mFirstVisibleIndex = firstVisibleItem
                    mPrevOffset = 0
                    onScrollDown()
                } else {
                    // detect the cell has moved
                    val visibleCell = mRecentsListView!!.getChildAt(firstVisibleItem)

                    if (null != visibleCell) {
                        val off = visibleCell!!.top

                        if (off > mPrevOffset) {
                            onScrollDown()
                        } else if (off < mPrevOffset) {
                            onScrollUp()
                        }

                        mPrevOffset = off
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mIsPaused = true
        removeSessionListener()
        PublicRoomsManager.getInstance().removeListener(mPublicRoomsListener)
    }

    override fun onResume() {
        super.onResume()
        mIsPaused = false
        addSessionListener()
        if (PublicRoomsManager.getInstance() == null)
            mAdapter.setPublicRoomsCount(PublicRoomsManager.getInstance().publicRoomsCount)

        // some unsent messages could have been added
        // it does not trigger any live event.
        // So, it is safer to sort the messages when debackgrounding
        //mAdapter.sortSummaries();
        notifyDataSetChanged()

        mRecentsListView!!.post {
            // trigger a public room refresh if the list was not initialized or too old (5 mins)
            if (((((null == PublicRoomsManager.getInstance().publicRoomsCount) || ((System.currentTimeMillis() - mLatestPublicRoomsRefresh) < (5 * 60000)))) && (!mIsLoadingPublicRooms))) {
                PublicRoomsManager.getInstance().refreshPublicRoomsCount(mPublicRoomsListener)
            }

            if (-1 != mScrollToIndex) {
                mRecentsListView!!.setSelection(mScrollToIndex)
                mScrollToIndex = -1
            }
        }
    }


    override fun onDestroy() {
        mScrollEventListener = null

//        mRecentsListView?.let {
//        mRecentsListView!!.setOnChildClickListener(null)
//        mRecentsListView!!.onItemLongClickListener = null
//        mRecentsListView!!.setOnScrollListener(null)
//        mRecentsListView!!.mDragAndDropEventsListener = null
//        }

        super.onDestroy()
    }

    private fun findWaitingView() {
        if (null == mWaitingView) {
            mWaitingView = activity!!.findViewById(R.id.listView_spinner_views)
        }
    }

    internal fun showWaitingView() {
        findWaitingView()

        if (null != mWaitingView) {
            mWaitingView!!.visibility = View.VISIBLE
        }
    }

    internal fun hideWaitingView() {
        findWaitingView()

        if (null != mWaitingView) {
            mWaitingView!!.visibility = View.GONE
        }
    }

    /**
     * Apply a filter to the rooms list
     *
     * @param pattern the pattern to search
     */
    internal fun applyFilter(pattern: String) {
        // will be done while resuming
        if (null == mRecentsListView) {
            return
        }

        mAdapter.setSearchPattern(pattern)

        mRecentsListView!!.post { expandsAllSections() }
    }

    /**
     * Expands all existing sections.
     */
    private fun expandsAllSections() {
        val groupCount = mAdapter.groupCount

        for (groupIndex in 0 until groupCount) {
            mRecentsListView!!.expandGroup(groupIndex)
        }
    }

    /**
     * Refresh the summaries list.
     * It also expands or collapses the section according to the latest known user preferences.
     */
    internal open fun notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged()

        mRecentsListView!!.post {
            if (null != activity) {
                val groupCount = mRecentsListView!!.expandableListAdapter.groupCount
                var isExpanded: Boolean
                val preferences = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)

                for (groupIndex in 0 until groupCount) {

                    if (mAdapter.isInvitedRoomPosition(groupIndex)) {
                        isExpanded = preferences.getBoolean(KEY_EXPAND_STATE_INVITES_GROUP, CommonActivityUtils.GROUP_IS_EXPANDED)
                    } else if (mAdapter.isFavouriteRoomPosition(groupIndex)) {
                        isExpanded = preferences.getBoolean(KEY_EXPAND_STATE_FAVOURITE_GROUP, CommonActivityUtils.GROUP_IS_EXPANDED)
                    } else if (mAdapter.isNoTagRoomPosition(groupIndex)) { // "Rooms" group
                        isExpanded = preferences.getBoolean(KEY_EXPAND_STATE_ROOMS_GROUP, CommonActivityUtils.GROUP_IS_EXPANDED)
                    } else if (mAdapter.isLowPriorityRoomPosition(groupIndex)) {
                        isExpanded = preferences.getBoolean(KEY_EXPAND_STATE_LOW_PRIORITY_GROUP, CommonActivityUtils.GROUP_IS_EXPANDED)
                    } else if (mAdapter.isDirectoryGroupPosition(groupIndex)) { // public rooms (search mode)
                        isExpanded = preferences.getBoolean(KEY_EXPAND_STATE_LOW_PRIORITY_GROUP, CommonActivityUtils.GROUP_IS_EXPANDED)
                    } else {
                        // unknown group index, just skipp
                        break
                    }

                    if (CommonActivityUtils.GROUP_IS_EXPANDED == isExpanded) {
                        mRecentsListView!!.expandGroup(groupIndex)
                    } else {
                        mRecentsListView!!.collapseGroup(groupIndex)
                    }
                }
            }
        }
    }

    /**
     * Update the group visibility preference.
     *
     * @param aGroupPosition the group position
     * @param aValue         the new value.
     */
    internal open fun updateGroupExpandStatus(aGroupPosition: Int, aValue: Boolean) {
        if (null != activity) {
            var context: Context
            val groupKey: String

            if (mAdapter.isInvitedRoomPosition(aGroupPosition)) {
                groupKey = KEY_EXPAND_STATE_INVITES_GROUP
            } else if (mAdapter.isFavouriteRoomPosition(aGroupPosition)) {
                groupKey = KEY_EXPAND_STATE_FAVOURITE_GROUP
            } else if (mAdapter.isNoTagRoomPosition(aGroupPosition)) { // "Rooms" group
                groupKey = KEY_EXPAND_STATE_ROOMS_GROUP
            } else if (mAdapter.isLowPriorityRoomPosition(aGroupPosition)) {
                groupKey = KEY_EXPAND_STATE_LOW_PRIORITY_GROUP
            } else if (mAdapter.isDirectoryGroupPosition(aGroupPosition)) { // public rooms (search mode)
                groupKey = KEY_EXPAND_STATE_LOW_PRIORITY_GROUP
            } else {
                // unknown group position, just skip
                Log.w(LOG_TAG, "## updateGroupExpandStatus(): Failure - Unknown group: $aGroupPosition")
                return
            }

            if (null != (activity!!.applicationContext)) {
                context = activity!!.applicationContext;
                PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                        .putBoolean(groupKey, aValue)
                        .apply()
            }
        }
    }


    /**
     * Add a MXEventListener to the session listeners.
     */
    private fun addSessionListener() {
        mEventsListener = object : MXEventListener() {
            private var mInitialSyncComplete = false

            override fun onInitialSyncComplete(toToken: String?) {
                Log.d(LOG_TAG, "## onInitialSyncComplete()")
                activity!!.runOnUiThread {
                    mInitialSyncComplete = true
                    notifyDataSetChanged()
                }
            }

            override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
                activity!!.runOnUiThread {
                    Log.d(LOG_TAG, "onLiveEventsChunkProcessed")
                    if (!mIsPaused && refreshOnChunkEnd && !mIsWaitingTagOrderEcho && !mIsWaitingDirectChatEcho) {
                        notifyDataSetChanged()
                    }

                    refreshOnChunkEnd = false
                }
            }

            override fun onLiveEvent(event: Event?, roomState: RoomState?) {
                activity!!.runOnUiThread {
                    val eventType = event!!.getType()

                    // refresh the UI at the end of the next events chunk
                    refreshOnChunkEnd = refreshOnChunkEnd or (((event!!.roomId != null) && RoomSummary.isSupportedEvent(event!!))
                            || Event.EVENT_TYPE_STATE_ROOM_MEMBER == eventType
                            || Event.EVENT_TYPE_TAGS == eventType
                            || Event.EVENT_TYPE_REDACTION == eventType
                            || Event.EVENT_TYPE_RECEIPT == eventType
                            || Event.EVENT_TYPE_STATE_ROOM_AVATAR == eventType
                            || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE == eventType)
                }
            }

            override fun onReceiptEvent(roomId: String?, senderIds: List<String>?) {
                // refresh only if the current user read some messages (to update the unread messages counters)
                refreshOnChunkEnd = refreshOnChunkEnd or (senderIds!!.indexOf(mSession!!.credentials.userId) >= 0)
            }

            override fun onRoomTagEvent(roomId: String?) {
                mIsWaitingTagOrderEcho = false
                refreshOnChunkEnd = true
            }

            /**
             * These methods trigger an UI refresh asap because the user could have created / joined / left a room
             * but the server events echos are not yet received.
             *
             */
            private fun onForceRefresh() {
                if (mInitialSyncComplete) {
                    activity!!.runOnUiThread { notifyDataSetChanged() }
                }
            }

            override fun onStoreReady() {
                onForceRefresh()
            }

            override fun onLeaveRoom(roomId: String?) {
                // clear any pending notification for this room
                EventStreamService.cancelNotificationsForRoomId(mSession!!.myUserId, roomId)
                onForceRefresh()
            }

            override fun onNewRoom(roomId: String?) {
                onForceRefresh()
            }

            override fun onJoinRoom(roomId: String?) {
                onForceRefresh()
            }

            override fun onDirectMessageChatRoomsListUpdate() {
                mIsWaitingDirectChatEcho = false
                refreshOnChunkEnd = true
            }

            override fun onEventDecrypted(roomId: String?, eventId: String?) {
                val summary = mSession!!.dataHandler.store!!.getSummary(roomId)

                if (null != summary) {
                    // test if the latest event is refreshed
                    val latestReceivedEvent = summary!!.getLatestReceivedEvent()
                    if (null != latestReceivedEvent && TextUtils.equals(latestReceivedEvent!!.eventId, eventId)) {
                        activity!!.runOnUiThread { notifyDataSetChanged() }
                    }
                }
            }
        }

        mSession!!.dataHandler.addListener(mEventsListener)
    }

    /**
     * Remove the MXEventListener to the session listeners.
     */
    private fun removeSessionListener() {
        if (mSession!!.isAlive) {
            mSession!!.dataHandler.removeListener(mEventsListener)
        }
    }

    override fun onGroupCollapsedNotif(aGroupPosition: Int) {
        updateGroupExpandStatus(aGroupPosition, CommonActivityUtils.GROUP_IS_COLLAPSED)
    }

    override fun onGroupExpandedNotif(aGroupPosition: Int) {
        updateGroupExpandStatus(aGroupPosition, CommonActivityUtils.GROUP_IS_EXPANDED)
    }

    //    override fun onPreviewRoom(session: MXSession, roomId: String) {
//        var roomAlias: String? = null
//
////        val room = session.dataHandler.getRoom(roomId)
////        if ((null != room) && (null != room!!.state)) {
////            roomAlias = room!!.state.canonicalAlias
////        }
//
////        val roomPreviewData = RoomPreviewData(mSession, roomId, null, roomAlias, null)
////        CommonActivityUtils.previewRoom(activity, roomPreviewData)
//        val intent = Intent(this.activity, PreviewInviteRoomActivity::class.java);
//        intent.putExtra(PreviewInviteRoomActivity.ROOM_ID, roomId)
//        startActivity(intent);
//    }
//
//    override fun onRejectInvitation(session: MXSession, roomId: String) {
//        val room = session.dataHandler.getRoom(roomId)
//
//        if (null != room) {
//            showWaitingView()
//
//            room!!.leave(object : ApiCallback<Void> {
//                override fun onSuccess(info: Void) {
//                    if (null != activity) {
//                        activity!!.runOnUiThread {
//                            // clear any pending notification for this room
//                            EventStreamService.cancelNotificationsForRoomId(mSession!!.myUserId, roomId)
//                            hideWaitingView()
//                        }
//                    }
//                }
//
//                private fun onError(message: String) {
//                    if (null != activity) {
//                        activity!!.runOnUiThread {
//                            hideWaitingView()
//                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//                override fun onNetworkError(e: Exception) {
//                    onError(e.localizedMessage)
//                }
//
//                override fun onMatrixError(e: MatrixError) {
//                    onError(e.localizedMessage)
//                }
//
//                override fun onUnexpectedError(e: Exception) {
//                    onError(e.localizedMessage)
//                }
//            })
//        }
//    }
    override fun onPreviewRoom(session: MXSession?, roomId: String) {
        var roomAlias: String? = null

//        val room = session.dataHandler.getRoom(roomId)
//        if ((null != room) && (null != room!!.state)) {
//            roomAlias = room!!.state.canonicalAlias
//        }

//        val roomPreviewData = RoomPreviewData(mSession, roomId, null, roomAlias, null)
//        CommonActivityUtils.previewRoom(activity, roomPreviewData)
        val intent = Intent(this.activity, PreviewInviteRoomActivity::class.java);
        intent.putExtra(PreviewInviteRoomActivity.ROOM_ID, roomId)
        startActivity(intent)
    }

    override fun onRejectInvitation(session: MXSession?, roomId: String) {
        val room = session?.dataHandler?.getRoom(roomId)

        if (null != room) {
            showWaitingView()

            room!!.leave(object : ApiCallback<Void> {
                override fun onSuccess(info: Void?) {
                    if (null != activity) {
                        activity!!.runOnUiThread {
                            // clear any pending notification for this room
                            EventStreamService.cancelNotificationsForRoomId(mSession!!.myUserId, roomId)
                            hideWaitingView()
                        }
                    }
                }

                private fun onError(message: String) {
                    if (null != activity) {
                        activity!!.runOnUiThread {
                            hideWaitingView()
                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onNetworkError(e: Exception) {
                    onError(e.localizedMessage)
                }

                override fun onMatrixError(e: MatrixError) {
                    onError(e.localizedMessage)
                }

                override fun onUnexpectedError(e: Exception) {
                    onError(e.localizedMessage)
                }
            })
        }
    }

    override fun onLeaveRoom(session: MXSession, roomId: String) {
        RoomUtils.showLeaveRoomDialog(activity) { dialog, which -> onRejectInvitation(session, roomId) }
    }

    override fun onForgetRoom(session: MXSession, roomId: String) {
        val room = session.dataHandler.getRoom(roomId)

        if (null != room) {
            showWaitingView()

            room!!.forget(object : ApiCallback<Void> {
                override fun onSuccess(info: Void) {
                    if (null != activity) {
                        activity!!.runOnUiThread {
                            // clear any pending notification for this room
                            EventStreamService.cancelNotificationsForRoomId(mSession!!.myUserId, roomId)
                            hideWaitingView()
                        }
                    }
                }

                private fun onError(message: String) {
                    if (null != activity) {
                        activity!!.runOnUiThread {
                            hideWaitingView()
                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onNetworkError(e: Exception) {
                    onError(e.localizedMessage)
                }

                override fun onMatrixError(e: MatrixError) {
                    onError(e.localizedMessage)
                }

                override fun onUnexpectedError(e: Exception) {
                    onError(e.localizedMessage)
                }
            })
        }
    }

    override fun addHomeScreenShortcut(session: MXSession, roomId: String) {
        RoomUtils.addHomeScreenShortcut(activity, session, roomId)
    }

    override fun onUpdateRoomNotificationsState(session: MXSession, roomId: String, state: BingRulesManager.RoomNotificationState) {
        val bingRulesManager = session.dataHandler.bingRulesManager

        showWaitingView()

        bingRulesManager.updateRoomNotificationState(roomId, state, object : BingRulesManager.onBingRuleUpdateListener {
            override fun onBingRuleUpdateSuccess() {
                if (null != activity) {
                    activity!!.runOnUiThread { hideWaitingView() }
                }
            }

            override fun onBingRuleUpdateFailure(errorMessage: String) {
                if (null != activity) {
                    activity!!.runOnUiThread {
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                        hideWaitingView()
                    }
                }
            }
        })
    }

    override fun onToggleDirectChat(session: MXSession, roomId: String) {
        val room = session.dataHandler.getRoom(roomId)

        if (null != room) {
            // show a spinner
            showWaitingView()

            mIsWaitingDirectChatEcho = true
            mSession!!.dataHandler.addListener(mEventsListener)


            mSession!!.toggleDirectChatRoom(roomId, null, object : ApiCallback<Void> {
                override fun onSuccess(info: Void) {
                    if (null != activity) {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {
                                hideWaitingView()
                                stopDragAndDropMode()
                            }
                        })
                    }
                }

                private fun onFails(errorMessage: String) {
                    if (null != activity) {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {
                                mIsWaitingDirectChatEcho = false
                                hideWaitingView()
                                stopDragAndDropMode()

                                if (!TextUtils.isEmpty(errorMessage)) {
                                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                    }
                }

                override fun onNetworkError(e: Exception) {
                    onFails(e.localizedMessage)
                }

                override fun onMatrixError(e: MatrixError) {
                    onFails(e.localizedMessage)
                }

                override fun onUnexpectedError(e: Exception) {
                    onFails(e.localizedMessage)
                }
            })
        }
    }

    /**
     * Start the drag and drop mode
     */
    private fun startDragAndDrop() {
        mIsWaitingTagOrderEcho = false
        mIsWaitingDirectChatEcho = false

        if (isDragAndDropSupported && groupIsMovable(mRecentsListView!!.touchedGroupPosition)) {
            val groupPos = mRecentsListView!!.touchedGroupPosition
            val childPos = mRecentsListView!!.touchedChildPosition

            try {
                mDraggedView = mAdapter.getChildView(groupPos, childPos, false, null, null)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "## startDragAndDrop() : getChildView failed " + e.message, e)
                return
            }

            // enable the drag and drop mode
            mAdapter.setIsDragAndDropMode(true)
            mSession!!.dataHandler.removeListener(mEventsListener)

            mDraggedView!!.setBackgroundColor(ContextCompat.getColor(context!!, R.color.vector_silver_color))
            mDraggedView!!.alpha = 0.3f

            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            mSelectedCellLayout!!.addView(mDraggedView, params)

            mOriginGroupPosition = groupPos
            mDestGroupPosition = mOriginGroupPosition
            mOriginChildPosition = childPos
            mDestChildPosition = mOriginChildPosition

            onTouchMove(mRecentsListView!!.touchedY, groupPos, childPos)
        }
    }

    /**
     * Drag and drop managemnt
     *
     * @param y             the touch Y position
     * @param groupPosition the touched group position
     * @param childPosition the touched child position
     */
    override fun onTouchMove(y: Int, groupPosition: Int, childPosition: Int) {
        var y = y
        // check if the recents list is drag & drop mode
        if ((null != mDraggedView) && (!mIgnoreScrollEvent)) {

            // display the cell if it is not yet visible
            if (mSelectedCellLayout!!.visibility != View.VISIBLE) {
                mSelectedCellLayout!!.visibility = View.VISIBLE
            }

            // compute the next first cell postion
            var nextFirstVisiblePosition = -1

            // scroll over the screen top
            if (y < 0) {
                // scroll up
                if ((mRecentsListView!!.firstVisiblePosition > 0)) {
                    nextFirstVisiblePosition = mRecentsListView!!.firstVisiblePosition - 1
                }

                y = 0
            }

            // scroll over the screen bottom
            if ((y + mSelectedCellLayout!!.height) > mRecentsListView!!.height) {

                // scroll down
                if (mRecentsListView!!.lastVisiblePosition < mRecentsListView!!.count) {
                    nextFirstVisiblePosition = mRecentsListView!!.firstVisiblePosition + 2
                }

                y = mRecentsListView!!.height - mSelectedCellLayout!!.height
            }

            // move the overlay child view with the y position
            val layoutParams = RelativeLayout.LayoutParams(mSelectedCellLayout!!.layoutParams)
            layoutParams.topMargin = y
            mSelectedCellLayout!!.layoutParams = layoutParams

            // virtually insert the moving cell in the recents list
            if ((groupPosition != mDestGroupPosition) || (childPosition != mDestChildPosition)) {

                // move cell
                mAdapter.moveChildView(mDestGroupPosition, mDestChildPosition, groupPosition, childPosition)
                // refresh
                notifyDataSetChanged()

                // backup
                mDestGroupPosition = groupPosition
                mDestChildPosition = childPosition
            }

            // the first selected position has been updated
            if (-1 != nextFirstVisiblePosition) {
                mIgnoreScrollEvent = true

                mRecentsListView!!.setSelection(nextFirstVisiblePosition)

                // avoid moving to quickly i.e moving only each 100ms
                mRecentsListView!!.postDelayed(object : Runnable {
                    override fun run() {
                        mIgnoreScrollEvent = false
                    }
                }, 100)
            }
        }
    }

    /**
     * Called the list view is over scrolled
     *
     * @param isTop set to true when the list is top over scrolled.
     */
    override fun onOverScrolled(isTop: Boolean) {
        if (isTop && (null != listener)) {
            mScrollEventListener!!.onRecentsListOverScrollUp()
        }
    }

    /**
     * Retrieves the RoomTag.ROOM_TAG.XX value from the group position
     *
     * @param groupPosition the group position.
     * @return the room tag.
     */
    private fun roomTagAt(groupPosition: Int): String? {
        if (mAdapter.isFavouriteRoomPosition(groupPosition)) {
            return RoomTag.ROOM_TAG_FAVOURITE
        } else if (mAdapter.isLowPriorityRoomPosition(groupPosition)) {
            return RoomTag.ROOM_TAG_LOW_PRIORITY
        }

        return null
    }

    /**
     * Check if a group is movable.
     *
     * @param groupPosition the group position
     * @return true if the group is movable.
     */
    private fun groupIsMovable(groupPosition: Int): Boolean {
        return (mAdapter.isNoTagRoomPosition(groupPosition)
                || mAdapter.isFavouriteRoomPosition(groupPosition)
                || mAdapter.isLowPriorityRoomPosition(groupPosition))
    }

    /**
     * The drag ends.
     */
    override fun onDrop() {
        // check if the list wad in drag & drop mode
        if (null != mDraggedView) {

            // remove the overlay child view
            val viewParent = mDraggedView!!.parent as ViewGroup
            viewParent.removeView(mDraggedView)
            mDraggedView = null

            // hide the overlay layout
            mSelectedCellLayout!!.visibility = View.GONE

            // same place, nothing to do
            if ((mOriginGroupPosition == mDestGroupPosition) && (mOriginChildPosition == mDestChildPosition)) {
                stopDragAndDropMode()
            } else if (mAdapter.isNoTagRoomPosition(mOriginGroupPosition) && mAdapter.isNoTagRoomPosition(mDestGroupPosition)) {
                // nothing to do, there is no other
                stopDragAndDropMode()
            } else if (!groupIsMovable(mDestGroupPosition)) {
                // cannot move in the expected group
                stopDragAndDropMode()
            } else {
                // retrieve the moved summary
                val roomSummary = mAdapter.getRoomSummaryAt(mDestGroupPosition, mDestChildPosition)
                // its tag
                val dstRoomTag = roomTagAt(mDestGroupPosition)

                // compute the new tag order
                val oldPos = if ((mOriginGroupPosition == mDestGroupPosition)) mOriginChildPosition else Integer.MAX_VALUE
                val tagOrder = mSession!!.tagOrderToBeAtIndex(mDestChildPosition, oldPos, dstRoomTag)

                updateRoomTag(mSession!!, roomSummary!!.roomId, tagOrder, dstRoomTag)
            }// move in no tag sections
        }
    }

    /**
     * Stop the drag and drop mode.
     */
    private fun stopDragAndDropMode() {
        // in drag and drop mode
        // the events listener is unplugged while playing with the cell
        if (mAdapter.isInDragAndDropMode) {
            mSession!!.dataHandler.addListener(mEventsListener)
            mAdapter.setIsDragAndDropMode(false)
            if (!mIsWaitingTagOrderEcho && !mIsWaitingDirectChatEcho) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Update the room tag.
     *
     * @param session  the session
     * @param roomId   the room id.
     * @param tagOrder the tag order.
     * @param newtag   the new tag.
     */
    private fun updateRoomTag(session: MXSession, roomId: String, tagOrder: Double?, newtag: String?) {
        var tagOrder = tagOrder
        val room = session.dataHandler.getRoom(roomId)

        if (null != room) {
            var oldTag: String? = null

            // retrieve the tag from the room info
            val accountData = room!!.accountData

            if ((null != accountData) && accountData!!.hasTags()) {
                oldTag = accountData!!.keys!!.iterator().next()
            }

            // if the tag order is not provided, compute it
            if (null == tagOrder) {
                tagOrder = 0.0

                if (null != newtag) {
                    tagOrder = session.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, newtag)
                }
            }

            // show a spinner
            showWaitingView()

            // restore the listener because the room tag event could be sent before getting the replaceTag response.
            mIsWaitingTagOrderEcho = true
            mSession!!.dataHandler.addListener(mEventsListener)

            // and work
            room!!.replaceTag(oldTag, newtag, tagOrder, object : ApiCallback<Void> {

                override fun onSuccess(info: Void) {
                    if (null != activity) {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {
                                hideWaitingView()
                                stopDragAndDropMode()
                            }
                        })
                    }
                }

                private fun onReplaceFails(errorMessage: String) {
                    if (null != activity) {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {
                                mIsWaitingTagOrderEcho = false
                                hideWaitingView()
                                stopDragAndDropMode()

                                if (!TextUtils.isEmpty(errorMessage)) {
                                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                    }
                }

                override fun onNetworkError(e: Exception) {
                    onReplaceFails(e.localizedMessage)
                }

                override fun onMatrixError(e: MatrixError) {
                    onReplaceFails(e.localizedMessage)
                }

                override fun onUnexpectedError(e: Exception) {
                    onReplaceFails(e.localizedMessage)
                }
            })
        }
    }

    override fun moveToConversations(session: MXSession, roomId: String) {
        updateRoomTag(session, roomId, null, null)
    }

    override fun moveToFavorites(session: MXSession, roomId: String) {
        updateRoomTag(session, roomId, null, RoomTag.ROOM_TAG_FAVOURITE)
    }

    override fun moveToLowPriority(session: MXSession, roomId: String) {
        updateRoomTag(session, roomId, null, RoomTag.ROOM_TAG_LOW_PRIORITY)
    }

    companion object {

        private val KEY_EXPAND_STATE_INVITES_GROUP = "KEY_EXPAND_STATE_INVITES_GROUP"
        private val KEY_EXPAND_STATE_ROOMS_GROUP = "KEY_EXPAND_STATE_ROOMS_GROUP"
        private val KEY_EXPAND_STATE_LOW_PRIORITY_GROUP = "KEY_EXPAND_STATE_LOW_PRIORITY_GROUP"
        private val KEY_EXPAND_STATE_FAVOURITE_GROUP = "KEY_EXPAND_STATE_FAVOURITE_GROUP"

        private val LOG_TAG = RecentsListFragment::class.java!!.simpleName

        internal val ARG_LAYOUT_ID = "VectorRecentsListFragment.ARG_LAYOUT_ID"
        internal val ARG_MATRIX_ID = "VectorRecentsListFragment.ARG_MATRIX_ID"

        fun newInstance(matrixId: String, layoutResId: Int): RecentsListFragment {
            val f = RecentsListFragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutResId)
            args.putString(ARG_MATRIX_ID, matrixId)
            f.arguments = args
            return f
        }
    }
}