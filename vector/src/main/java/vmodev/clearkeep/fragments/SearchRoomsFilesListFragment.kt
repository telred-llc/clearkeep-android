package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import im.vector.activity.VectorMediaViewerActivity
import im.vector.adapters.VectorMessagesAdapter
import im.vector.adapters.VectorSearchFilesListAdapter
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.rest.model.message.Message
import java.util.*

class SearchRoomsFilesListFragment : SearchMessagesListFragment() {

    override fun createMessagesAdapter(): VectorMessagesAdapter {
        mIsMediaSearch = true
        return VectorSearchFilesListAdapter(mSession, activity, null == mRoomId, mxMediaCache)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        mMessageListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val row = mAdapter.getItem(position)
            val event = row!!.event

            if (mAdapter.isInSelectionMode) {
                // cancel the selection mode.
                mAdapter.onEventTap(null)
                return@OnItemClickListener
            }

            val message = JsonUtils.toMessage(event.content)

            // video and images are displayed inside a medias slider.
            if (Message.MSGTYPE_IMAGE == message.msgtype || Message.MSGTYPE_VIDEO == message.msgtype) {
                val mediaMessagesList = listSlidableMessages()
                val listPosition = getMediaMessagePosition(mediaMessagesList, message)

                if (listPosition >= 0) {
                    val viewImageIntent = Intent(activity, VectorMediaViewerActivity::class.java)

                    viewImageIntent.putExtra(VectorMediaViewerActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
                    viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_THUMBNAIL_WIDTH, mAdapter.maxThumbnailWidth)
                    viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_THUMBNAIL_HEIGHT, mAdapter.maxThumbnailHeight)
                    viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_INFO_LIST, mediaMessagesList as ArrayList<*>)
                    viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_INFO_LIST_INDEX, listPosition)

                    activity!!.startActivity(viewImageIntent)
                }
            } else if (Message.MSGTYPE_FILE == message.msgtype) {
                val fileMessage = JsonUtils.toFileMessage(event.content)

                if (null != fileMessage.getUrl()) {
                    onMediaAction(ACTION_VECTOR_OPEN, fileMessage.getUrl(), fileMessage.mimeType, fileMessage.body, fileMessage.file)
                }
            }
        }

        return view
    }

    companion object {
        /**
         * static constructor
         *
         * @param matrixId    the session Id.
         * @param layoutResId the used layout.
         * @return the instance
         */
        fun newInstance(matrixId: String, roomId: String, layoutResId: Int): SearchRoomsFilesListFragment {
            val frag = SearchRoomsFilesListFragment()
            frag.arguments = getArguments(matrixId, roomId, layoutResId)
            return frag
        }
    }
}
