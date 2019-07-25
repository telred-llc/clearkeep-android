package vmodev.clearkeep.fragments

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import im.vector.R
import im.vector.activity.VectorRoomActivity
import im.vector.adapters.VectorMessagesAdapter
import im.vector.adapters.VectorSearchMessagesListAdapter
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.data.timeline.EventTimeline
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.activities.RoomActivity
import java.util.*

open class SearchMessagesListFragment : BaseMessageListFragment() {

    // parameters
    private var mSearchingPattern: String? = null
    internal val mSearchListeners = ArrayList<OnSearchResultListener>()

    private var mProgressView: View? = null

    override fun createMessagesAdapter(): VectorMessagesAdapter {
        return VectorSearchMessagesListAdapter(mSession, activity, null == mRoomId, mxMediaCache)
    }

    override fun onPause() {
        super.onPause()

        if (mSession.isAlive) {
            cancelSearch()

            if (mIsMediaSearch) {
                mSession.cancelSearchMediaByText()
            } else {
                mSession.cancelSearchMessagesByText()
            }
            mSearchingPattern = null
        }
    }

    /**
     * Called when a fragment is first attached to its activity.
     * [.onCreate] will be called after this.
     *
     * @param aHostActivity parent activity
     */
    override fun onAttach(aHostActivity: Activity?) {
        super.onAttach(aHostActivity)
        mProgressView = activity!!.findViewById(R.id.search_load_oldest_progress)
    }

    /**
     * The user scrolls the list.
     * Apply an expected behaviour
     *
     * @param event the scroll event
     */
    override fun onListTouch(event: MotionEvent?) {}

    /**
     * return true to display all the events.
     * else the unknown events will be hidden.
     */
    override fun isDisplayAllEvents(): Boolean {
        return true
    }

    /**
     * Display a global spinner or any UI item to warn the user that there are some pending actions.
     */
    override fun showLoadingBackProgress() {
        if (null != mProgressView) {
            mProgressView!!.visibility = View.VISIBLE
        }
    }

    /**
     * Dismiss any global spinner.
     */
    override fun hideLoadingBackProgress() {
        if (null != mProgressView) {
            mProgressView!!.visibility = View.GONE
        }
    }

    /**
     * Scroll the fragment to the bottom
     */
    override fun scrollToBottom() {
        if (0 != mAdapter.count) {
            mMessageListView.setSelection(mAdapter.count - 1)
        }
    }

    /**
     * Tell if the search is allowed for a dedicated pattern
     *
     * @param pattern the searched pattern.
     * @return true if the search is allowed.
     */
    internal fun allowSearch(pattern: String): Boolean {
        // ConsoleMessageListFragment displays the list of unfiltered messages when there is no pattern
        // in the search case, clear the list and hide it
        return !TextUtils.isEmpty(pattern)
    }

    override fun onInitialMessagesLoaded() {
        // ensure that the list don't try to fill itself
        // if the search is not allowed with the provided pattern.
        mPattern?.let {
            if (!allowSearch(mPattern)) {
                Log.e(LOG_TAG, "## onInitialMessagesLoaded() : history filling is cancelled")
            } else {
                super.onInitialMessagesLoaded()
            }
        }
    }

    /**
     * Update the searched pattern.
     *
     * @param pattern the pattern to find out. null to disable the search mode
     */
    override fun searchPattern(pattern: String, onSearchResultListener: OnSearchResultListener?) {
        // add the listener to list to warn when the search is done.
        if (null != onSearchResultListener) {
            mSearchListeners.add(onSearchResultListener)
        }

        // wait that the fragment is displayed
        if (null == mMessageListView) {
            return
        }

        // please wait
        if (TextUtils.equals(mSearchingPattern, pattern)) {
            onSearchResultListener?.let { mSearchListeners.add(it) }

            return
        }

        if (!allowSearch(pattern)) {
            mPattern = null
            mMessageListView.visibility = View.GONE

            activity!!.runOnUiThread {
                for (listener in mSearchListeners) {
                    try {
                        listener.onSearchSucceed(0)
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "## searchPattern() : failed " + e.message, e)
                    }

                }
                mSearchListeners.clear()
                mSearchingPattern = null
            }
        } else {
            // the search on this pattern is just ended
            if (TextUtils.equals(mPattern, pattern)) {
                Handler(Looper.getMainLooper()).post {
                    for (listener in mSearchListeners) {
                        try {
                            listener.onSearchSucceed(mAdapter.count)
                        } catch (e: Exception) {
                            Log.e(LOG_TAG, "## searchPattern() : failed " + e.message, e)
                        }

                    }
                    mSearchListeners.clear()
                }
            } else {
                // start a new search
                mAdapter.clear()
                mSearchingPattern = pattern

                if (mAdapter is VectorSearchMessagesListAdapter) {
                    (mAdapter as VectorSearchMessagesListAdapter).setTextToHighlight(pattern)
                }

                super.searchPattern(pattern, mIsMediaSearch, object : OnSearchResultListener {
                    override fun onSearchSucceed(nbrMessages: Int) {
                        // the pattern has been updated while search
                        if (!TextUtils.equals(pattern, mSearchingPattern)) {
                            mAdapter.clear()
                            mMessageListView.visibility = View.GONE
                        } else {

                            mIsInitialSyncing = false
                            mMessageListView.setOnScrollListener(mScrollListener)
                            mMessageListView.adapter = mAdapter
                            mMessageListView.visibility = View.VISIBLE

                            // scroll to the bottom
                            scrollToBottom()

                            for (listener in mSearchListeners) {
                                try {
                                    listener.onSearchSucceed(nbrMessages)
                                } catch (e: Exception) {
                                    Log.e(LOG_TAG, "## searchPattern() : failed " + e.message, e)
                                }

                            }
                            mSearchListeners.clear()
                            mSearchingPattern = null

                            // trigger a back pagination to fill the screen
                            // the request could contain only a few items.
                            backPaginate(true)
                        }
                    }

                    override fun onSearchFailed() {
                        mMessageListView.visibility = View.GONE

                        // clear the results list if teh search fails
                        mAdapter.clear()

                        for (listener in mSearchListeners) {
                            try {
                                listener.onSearchFailed()
                            } catch (e: Exception) {
                                Log.e(LOG_TAG, "## searchPattern() : onSearchFailed failed " + e.message, e)
                            }

                        }
                        mSearchListeners.clear()
                        mSearchingPattern = null
                    }
                })
            }
        }
    }

    override fun onRowLongClick(position: Int): Boolean {
        onContentClick(position)
        return true
    }

    override fun onContentClick(position: Int) {
        val event = mAdapter.getItem(position)!!.event

        val intent = Intent(activity, RoomActivity::class.java)
        intent.putExtra(VectorRoomActivity.EXTRA_MATRIX_ID, mSession.myUserId)
        intent.putExtra(VectorRoomActivity.EXTRA_ROOM_ID, event.roomId)
        intent.putExtra(VectorRoomActivity.EXTRA_EVENT_ID, event.eventId)

        activity!!.startActivity(intent)
    }

    /**
     * Called when a long click is performed on the message content
     *
     * @param position the cell position
     * @return true if managed
     */
    override fun onContentLongClick(position: Int): Boolean {
        return false
    }

    //==============================================================================================================
    // rooms events management : ignore any update on the adapter while searching
    //==============================================================================================================

    override fun onEvent(event: Event, direction: EventTimeline.Direction?, roomState: RoomState) {}

    override fun onLiveEventsChunkProcessed() {}

    override fun onReceiptEvent(senderIds: List<String>) {}

    companion object {
        private val LOG_TAG = SearchMessagesListFragment::class.java.simpleName

        /**
         * static constructor
         *
         * @param matrixId    the session Id.
         * @param layoutResId the used layout.
         */
        fun newInstance(matrixId: String, roomId: String, layoutResId: Int): SearchMessagesListFragment {
            val frag = SearchMessagesListFragment()
            frag.arguments = MatrixMessageListFragment.getArguments(matrixId, roomId, layoutResId)
            return frag
        }
    }
}
