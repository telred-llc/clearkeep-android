package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import im.vector.Matrix
import im.vector.PublicRoomsManager
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorPublicRoomsActivity
import im.vector.activity.VectorRoomActivity
import im.vector.adapters.VectorRoomSummaryAdapter
import im.vector.fragments.VectorRecentsListFragment
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.activities.PreviewInviteRoomActivity
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.RoomSummaryAdapter

class SearchRoomsListFragment : RecentsListFragment() {
    // the session
    private var mSession: MXSession? = null

    override fun getLayoutResId(): Int {
        val args = arguments
        return args!!.getInt(ARG_LAYOUT_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments

        mMatrixId = args!!.getString(ARG_MATRIX_ID)
        mSession = Matrix.getInstance(activity)!!.getSession(mMatrixId)

        if (null == mSession) {
            throw RuntimeException("Must have valid default MXSession.")
        }

        // the chevron is managed in the header view
        mRecentsListView?.setGroupIndicator(null)
        // create the adapter
        mAdapter = RoomSummaryAdapter(this.context!!,
                mSession,
                true,
                false,
                R.layout.item_adapter_recent_room,
                R.layout.adapter_item_vector_recent_header,
                this,
                this)
        mRecentsListView?.setAdapter(mAdapter)
        mRecentsListView?.visibility = View.VISIBLE

        // Set rooms click listener:
        // - reset the unread count
        // - start the corresponding room activity
        mRecentsListView?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (mAdapter.isRoomByIdGroupPosition(groupPosition)) {
                val roomIdOrAlias = mAdapter.searchedPattern

                // detect if it is a room id
                if (roomIdOrAlias!!.startsWith("!")) {
                    previewRoom(roomIdOrAlias, null)
                } else {
                    showWaitingView()

                    // test if the room Id / alias exists
                    mSession!!.dataHandler.roomIdByAlias(roomIdOrAlias, object : ApiCallback<String> {
                        override fun onSuccess(roomId: String) {
                            previewRoom(roomId, roomIdOrAlias)
                        }

                        private fun onError(errorMessage: String) {
                            hideWaitingView()
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
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
            } else if (mAdapter.isDirectoryGroupPosition(groupPosition)) {
                if (TextUtils.isEmpty(mAdapter.searchedPattern) || mAdapter.matchedPublicRoomsCount > 0) {
                    val intent = Intent(activity, VectorPublicRoomsActivity::class.java)
                    intent.putExtra(VectorPublicRoomsActivity.EXTRA_MATRIX_ID, mSession!!.myUserId)

                    if (!TextUtils.isEmpty(mAdapter.searchedPattern)) {
                        intent.putExtra(VectorPublicRoomsActivity.EXTRA_SEARCHED_PATTERN, mAdapter.searchedPattern)
                    }
                    activity!!.startActivity(intent)
                }
            } else {
                // open the dedicated room activity
                val roomSummary = mAdapter.getRoomSummaryAt(groupPosition, childPosition)
                val session = Matrix.getInstance(activity)!!.getSession(roomSummary?.userId)

                var roomId: String = "";
                roomSummary?.roomId?.let {
                    roomId = it;
                }
                val room = session.dataHandler.getRoom(roomId)
                // cannot join a leaving room
                if (null == room || room.isLeaving) {
                    roomId = "";
                }

                // update the unread messages count
                if (mAdapter.resetUnreadCount(groupPosition, childPosition)) {
                    session.dataHandler.store.flushSummary(roomSummary)
                }

                // launch corresponding room activity
                if (null != roomId) {
                    val intent = Intent(activity, RoomActivity::class.java)
                    intent.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId)
                    intent.putExtra(RoomActivity.EXTRA_MATRIX_ID, mSession!!.credentials.userId)
                    activity!!.startActivity(intent)
                }
            }// display the public rooms list

            // click is handled
            true
        }

        // disable the collapse
        mRecentsListView?.setOnGroupClickListener { parent, v, groupPosition, id ->
            // Doing nothing
            true
        }
    }

    /**
     * Preview the dedicated room if it was not joined.
     *
     * @param roomId    the roomId
     * @param roomAlias the room alias
     */
    private fun previewRoom(roomId: String, roomAlias: String?) {
//        CommonActivityUtils.previewRoom(activity, mSession, roomId, roomAlias, object : ApiCallback<Void> {
//            override fun onSuccess(info: Void) {
//                hideWaitingView()
//            }
//
//            override fun onNetworkError(e: Exception) {
//                hideWaitingView()
//            }
//
//            override fun onMatrixError(e: MatrixError) {
//                hideWaitingView()
//            }
//
//            override fun onUnexpectedError(e: Exception) {
//                hideWaitingView()
//            }
//        })
        val intent = Intent(this.activity, PreviewInviteRoomActivity::class.java);
        intent.putExtra(PreviewInviteRoomActivity.ROOM_ID, roomAlias)
        startActivity(intent);
    }

    /**
     * Search a pattern in the room
     *
     * @param pattern                the pattern to search
     * @param onSearchResultListener the search listener.
     */
    fun searchPattern(pattern: String, onSearchResultListener: MatrixMessageListFragment.OnSearchResultListener) {
        // will be done while resuming
        if (null == mRecentsListView) {
            return
        }

        super.applyFilter(pattern)

        if (!TextUtils.isEmpty(mAdapter.searchedPattern)) {
            PublicRoomsManager.getInstance().startPublicRoomsSearch(null, null, false, mAdapter.searchedPattern, object : ApiCallback<List<PublicRoom>> {

                private fun onDone(size: Int) {
                    mAdapter.setMatchedPublicRoomsCount(size)
                }

                override fun onSuccess(info: List<PublicRoom>) {
                    onDone(info.size)
                }

                override fun onNetworkError(e: Exception) {
                    onDone(0)
                }

                override fun onMatrixError(e: MatrixError) {
                    onDone(0)
                }

                override fun onUnexpectedError(e: Exception) {
                    onDone(0)
                }
            })
        }

        mRecentsListView?.post { onSearchResultListener.onSearchSucceed(1) }
    }

    override fun isDragAndDropSupported(): Boolean {
        return false
    }

    /**
     * Update the group visibility preference.
     *
     * @param aGroupPosition the group position
     * @param aValue         the new value.
     */
    override fun updateGroupExpandStatus(aGroupPosition: Int, aValue: Boolean) {
        // do nothing
        // the expandable preferences are not updated because all the groups are expanded.
    }

    /**
     * Refresh the summaries list.
     * It also expands or collapses the section according to the latest known user preferences.
     */
    override fun notifyDataSetChanged() {
        // the groups are always expanded.
        mAdapter.notifyDataSetChanged()
    }

    companion object {

        /**
         * Static constructor
         *
         * @param matrixId the matrix id
         * @return a VectorRoomsSearchResultsListFragment instance
         */
        fun newInstance(matrixId: String, layoutResId: Int): SearchRoomsListFragment {
            val f = SearchRoomsListFragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutResId)
            args.putString(ARG_MATRIX_ID, matrixId)
            f.arguments = args
            return f
        }
    }
}
