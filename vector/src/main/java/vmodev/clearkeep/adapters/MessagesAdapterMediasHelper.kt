package vmodev.clearkeep.adapters

import android.content.Context
import android.graphics.Color
import android.media.ExifInterface
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonElement
import im.vector.R
import im.vector.adapters.VectorMessagesAdapter
import im.vector.listeners.IMessagesAdapterActionsListener
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo
import org.matrix.androidsdk.db.MXMediaCache
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener
import org.matrix.androidsdk.listeners.IMXMediaUploadListener
import org.matrix.androidsdk.listeners.MXMediaDownloadListener
import org.matrix.androidsdk.listeners.MXMediaUploadListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.message.*
import java.util.HashMap

internal class MessagesAdapterMediasHelper constructor(private val mContext: Context,
                                                 private val mSession: MXSession,
                                                 private val mMaxImageWidth: Int,
                                                 private val mMaxImageHeight: Int,
                                                 private val mNotSentMessageTextColor: Int,
                                                 private val mDefaultMessageTextColor: Int) {
    private val mMediasCache: MXMediaCache
    private var mVectorMessagesAdapterEventsListener: IMessagesAdapterActionsListener? = null

    // the image / video bitmaps are set to null if the matching URL is not the same
    // to avoid flickering
    private val mUrlByBitmapIndex = HashMap<String, String>()

    init {
        mMediasCache = mSession.mediaCache
    }

    /**
     * Define the events listener
     *
     * @param listener teh events listener
     */
    fun setVectorMessagesAdapterActionsListener(listener: IMessagesAdapterActionsListener) {
        mVectorMessagesAdapterEventsListener = listener
    }

    /**
     * Check if there is a linked upload.
     *
     * @param convertView the media view
     * @param event       teh related event
     * @param type        the media type
     * @param mediaUrl    the media url
     */
    fun managePendingUpload(convertView: View, event: Event, type: Int, mediaUrl: String) {
        val uploadProgressLayout = convertView.findViewById<View>(R.id.content_upload_progress_layout)
        val uploadSpinner = convertView.findViewById<ProgressBar>(R.id.upload_event_spinner)

        // the dedicated UI items are not found
        if (null == uploadProgressLayout || null == uploadSpinner) {
            return
        }

        // Mark the upload layout as
        uploadProgressLayout.tag = mediaUrl

        // no upload in progress
        if (mSession.myUserId != event.getSender() || !event.isSending) {
            uploadProgressLayout.visibility = View.GONE
            uploadSpinner.visibility = View.GONE
            showUploadFailure(convertView, type, event.isUndelivered)
            return
        }

        val uploadStats = mSession.mediaCache.getStatsForUploadId(mediaUrl)

        if (null != uploadStats) {
            mSession.mediaCache.addUploadListener(mediaUrl, object : MXMediaUploadListener() {
                override fun onUploadProgress(uploadId: String?, uploadStats: IMXMediaUploadListener.UploadStats?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        refreshUploadViews(event, uploadStats, uploadProgressLayout)
                    }
                }

                private fun onUploadStop(message: String?) {
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show()
                    }

                    showUploadFailure(convertView, type, true)
                    uploadProgressLayout.visibility = View.GONE
                    uploadSpinner.visibility = View.GONE
                }

                override fun onUploadCancel(uploadId: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        onUploadStop(null)
                    }
                }

                override fun onUploadError(uploadId: String?, serverResponseCode: Int, serverErrorMessage: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        onUploadStop(serverErrorMessage)
                    }
                }

                override fun onUploadComplete(uploadId: String?, contentUri: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        uploadSpinner.visibility = View.GONE
                    }
                }

            })
        }

        showUploadFailure(convertView, type, false)
        uploadSpinner.visibility = if (null == uploadStats) View.VISIBLE else View.GONE
        refreshUploadViews(event, uploadStats, uploadProgressLayout)
    }

    /**
     * Manage the image/video download.
     *
     * @param convertView the parent view.
     * @param event       the event
     * @param message     the image / video message
     * @param position    the message position
     */
    fun managePendingImageVideoDownload(convertView: View, event: Event, message: Message, position: Int) {
        val maxImageWidth = mMaxImageWidth
        val maxImageHeight = mMaxImageHeight
        var rotationAngle = 0
        var orientation = ExifInterface.ORIENTATION_NORMAL
        var thumbUrl: String? = null
        var thumbWidth = -1
        var thumbHeight = -1

        var encryptedFileInfo: EncryptedFileInfo? = null

        // retrieve the common items
        if (message is ImageMessage) {
            message.checkMediaUrls()

            // Backwards compatibility with events from before Synapse 0.6.0
            if (message.thumbnailUrl != null) {
                thumbUrl = message.thumbnailUrl

                if (null != message.info) {
                    encryptedFileInfo = message.info.thumbnail_file
                }

            } else if (message.getUrl() != null) {
                thumbUrl = message.getUrl()
                encryptedFileInfo = message.file
            }

            rotationAngle = message.rotation

            val imageInfo = message.info

            if (null != imageInfo) {
                if (null != imageInfo.w && null != imageInfo.h) {
                    thumbWidth = imageInfo.w
                    thumbHeight = imageInfo.h
                }

                if (null != imageInfo.orientation) {
                    orientation = imageInfo.orientation
                }
            }
        } else if (message is VideoMessage) {
            message.checkMediaUrls()

            thumbUrl = message.thumbnailUrl
            if (null != message.info) {
                encryptedFileInfo = message.info.thumbnail_file
            }

            val videoinfo = message.info

            if (null != videoinfo) {
                if (null != message.info.thumbnail_info
                        && null != message.info.thumbnail_info.w
                        && null != message.info.thumbnail_info.h) {
                    thumbWidth = message.info.thumbnail_info.w
                    thumbHeight = message.info.thumbnail_info.h
                }
            }
        }

        val imageView = convertView.findViewById<ImageView>(R.id.messagesAdapter_image)

        // reset the bitmap if the url is not the same than before
        if (null == thumbUrl || !TextUtils.equals(imageView.hashCode().toString() + "", mUrlByBitmapIndex[thumbUrl])) {
            imageView.setImageBitmap(null)
            if (null != thumbUrl) {
                mUrlByBitmapIndex[thumbUrl] = imageView.hashCode().toString() + ""
            }
        }

        val informationLayout = convertView.findViewById<RelativeLayout>(R.id.messagesAdapter_image_layout)
        val layoutParams = informationLayout.layoutParams as FrameLayout.LayoutParams

        // the thumbnails are always pre - rotated
        var downloadId: String? = null
        if (event.getType() != Event.EVENT_TYPE_STICKER) {
            downloadId = mMediasCache.loadBitmap(mSession.homeServerConfig,
                    imageView, thumbUrl, maxImageWidth, maxImageHeight, rotationAngle,
                    ExifInterface.ORIENTATION_UNDEFINED, "image/jpeg", encryptedFileInfo)
        }

        // test if the media is downloading when the thumbnail is not downloading
        if (null == downloadId) {
            if (message is VideoMessage) {
                downloadId = mMediasCache.downloadIdFromUrl(message.getUrl())
            } else if (message is ImageMessage) {
                downloadId = mMediasCache.downloadIdFromUrl(message.getUrl())
            }
        }

        // Use Glide library to display stickers into ImageView
        // Glide support animated gif
        if (event.getType() == Event.EVENT_TYPE_STICKER) {
            // Check whether the sticker url is a valid Matrix media content URI, and convert it in an actual url.
            val downloadableUrl = mSession.contentManager.getDownloadableUrl((message as StickerMessage).getUrl())
            if (null != downloadableUrl) {
                Glide.with(mContext)
                        .load(downloadableUrl)
                        .apply(RequestOptions()
                                .override(maxImageWidth, maxImageHeight)
                                .fitCenter()
                                .placeholder(R.drawable.sticker_placeholder)
                        )
                        .into(imageView)
            } else {
                // Display the placeholder
                imageView.setImageResource(R.drawable.sticker_placeholder)
            }
        }

        val downloadProgressLayout = convertView.findViewById<View>(R.id.content_download_progress_layout)
                ?: return

// the tag is used to detect if the progress value is linked to this layout
        downloadProgressLayout.tag = downloadId

        var frameHeight = -1
        var frameWidth = -1

        // if the image size is known
        // compute the expected thumbnail height
        if (thumbWidth > 0 && thumbHeight > 0) {

            // swap width and height if the image is side oriented
            if (rotationAngle == 90 || rotationAngle == 270) {
                val tmp = thumbWidth
                thumbWidth = thumbHeight
                thumbHeight = tmp
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                val tmp = thumbWidth
                thumbWidth = thumbHeight
                thumbHeight = tmp
            }

            frameHeight = Math.min(maxImageWidth * thumbHeight / thumbWidth, maxImageHeight)
            frameWidth = frameHeight * thumbWidth / thumbHeight
        }

        // ensure that some values are properly initialized
        if (frameHeight < 0) {
            frameHeight = mMaxImageHeight
        }

        if (frameWidth < 0) {
            frameWidth = mMaxImageWidth
        }

        // apply it the layout
        // it avoid row jumping when the image is downloaded
        layoutParams.height = frameHeight
        layoutParams.width = frameWidth

        // no download in progress
        if (null != downloadId) {
            downloadProgressLayout.visibility = View.VISIBLE

            mMediasCache.addDownloadListener(downloadId, object : MXMediaDownloadListener() {
                override fun onDownloadCancel(downloadId: String?) {
                    if (TextUtils.equals(downloadId, downloadProgressLayout.tag as String)) {
                        downloadProgressLayout.visibility = View.GONE
                    }
                }

                override fun onDownloadError(downloadId: String?, jsonElement: JsonElement?) {
                    if (TextUtils.equals(downloadId, downloadProgressLayout.tag as String)) {
                        var error: MatrixError? = null

                        try {
                            error = JsonUtils.toMatrixError(jsonElement)
                        } catch (e: Exception) {
                            Log.e(LOG_TAG, "Cannot cast to Matrix error " + e.localizedMessage, e)
                        }

                        downloadProgressLayout.visibility = View.GONE

                        if (null != error && error.isSupportedErrorCode) {
                            Toast.makeText(mContext, error.localizedMessage, Toast.LENGTH_LONG).show()
                        } else if (null != jsonElement) {
                            Toast.makeText(mContext, jsonElement.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onDownloadProgress(aDownloadId: String?, stats: IMXMediaDownloadListener.DownloadStats?) {
                    if (TextUtils.equals(aDownloadId, downloadProgressLayout.tag as String)) {
                        refreshDownloadViews(event, stats, downloadProgressLayout)
                    }
                }

                override fun onDownloadComplete(aDownloadId: String?) {
                    if (TextUtils.equals(aDownloadId, downloadProgressLayout.tag as String)) {
                        downloadProgressLayout.visibility = View.GONE

                        if (null != mVectorMessagesAdapterEventsListener) {
                            mVectorMessagesAdapterEventsListener!!.onMediaDownloaded(position)
                        }
                    }
                }
            })

            refreshDownloadViews(event, mMediasCache.getStatsForDownloadId(downloadId), downloadProgressLayout)
        } else {
            downloadProgressLayout.visibility = View.GONE
        }

        imageView.setBackgroundColor(Color.TRANSPARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    /**
     * Manage the video upload
     *
     * @param convertView the base view
     * @param event       the image or video event
     * @param message     the image or video message
     */
    fun managePendingImageVideoUpload(convertView: View, event: Event, message: Message) {
        val uploadProgressLayout = convertView.findViewById<View>(R.id.content_upload_progress_layout)
        val uploadSpinner = convertView.findViewById<ProgressBar>(R.id.upload_event_spinner)

        val isVideoMessage = message is VideoMessage

        // the dedicated UI items are not found
        if (null == uploadProgressLayout || null == uploadSpinner) {
            return
        }

        // refresh the progress only if it is the expected URL
        uploadProgressLayout.tag = null

        val hasContentInfo = null != if (isVideoMessage) (message as VideoMessage).info else (message as ImageMessage).info

        // not the sender ?
        if (mSession.myUserId != event.getSender() || event.isUndelivered || !hasContentInfo) {
            uploadProgressLayout.visibility = View.GONE
            uploadSpinner.visibility = View.GONE
            showUploadFailure(convertView,
                    if (isVideoMessage) MessagesAdapter.ROW_TYPE_VIDEO else MessagesAdapter.ROW_TYPE_IMAGE,
                    event.isUndelivered)
            return
        }

        var uploadingUrl: String?
        val isUploadingThumbnail: Boolean
        var isUploadingContent = false

        if (isVideoMessage) {
            uploadingUrl = (message as VideoMessage).thumbnailUrl
            isUploadingThumbnail = message.isThumbnailLocalContent
        } else {
            uploadingUrl = (message as ImageMessage).thumbnailUrl
            isUploadingThumbnail = message.isThumbnailLocalContent
        }

        var progress: Int

        if (isUploadingThumbnail) {
            progress = mSession.mediaCache.getProgressValueForUploadId(uploadingUrl)
        } else {
            if (message is VideoMessage) {
                uploadingUrl = message.getUrl()
                isUploadingContent = message.isLocalContent
            } else if (message is ImageMessage) {
                uploadingUrl = message.getUrl()
                isUploadingContent = message.isLocalContent
            }

            progress = mSession.mediaCache.getProgressValueForUploadId(uploadingUrl)
        }

        if (progress >= 0) {
            uploadProgressLayout.tag = uploadingUrl
            mSession.mediaCache.addUploadListener(uploadingUrl, object : MXMediaUploadListener() {
                override fun onUploadProgress(uploadId: String?, uploadStats: IMXMediaUploadListener.UploadStats?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        refreshUploadViews(event, uploadStats, uploadProgressLayout)

                        val progress: Int

                        if (!isUploadingThumbnail) {
                            progress = 10 + uploadStats!!.mProgress * 90 / 100
                        } else {
                            progress = uploadStats!!.mProgress * 10 / 100
                        }

                        updateUploadProgress(uploadProgressLayout, progress)
                    }
                }

                private fun onUploadStop(message: String?) {
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(mContext,
                                message,
                                Toast.LENGTH_LONG).show()
                    }

                    showUploadFailure(convertView, if (isVideoMessage) MessagesAdapter.ROW_TYPE_VIDEO else MessagesAdapter.ROW_TYPE_IMAGE, true)
                    uploadProgressLayout.visibility = View.GONE
                    uploadSpinner.visibility = View.GONE
                }

                override fun onUploadCancel(uploadId: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        onUploadStop(null)
                    }
                }

                override fun onUploadError(uploadId: String?, serverResponseCode: Int, serverErrorMessage: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        onUploadStop(serverErrorMessage)
                    }
                }

                override fun onUploadComplete(uploadId: String?, contentUri: String?) {
                    if (TextUtils.equals(uploadProgressLayout.tag as String, uploadId)) {
                        uploadSpinner.visibility = View.GONE
                    }
                }
            })
        }

        showUploadFailure(convertView, if (isVideoMessage) MessagesAdapter.ROW_TYPE_VIDEO else MessagesAdapter.ROW_TYPE_IMAGE, false)
        uploadSpinner.visibility = if (progress < 0 && event.isSending) View.VISIBLE else View.GONE
        refreshUploadViews(event, mSession.mediaCache.getStatsForUploadId(uploadingUrl), uploadProgressLayout)

        if (isUploadingContent) {
            progress = 10 + progress * 90 / 100
        } else if (isUploadingThumbnail) {
            progress = progress * 10 / 100
        }

        updateUploadProgress(uploadProgressLayout, progress)
        uploadProgressLayout.visibility = if (progress >= 0 && event.isSending) View.VISIBLE else View.GONE
    }

    /**
     * Refresh the upload views.
     *
     * @param event                the event
     * @param uploadStats          the upload stats
     * @param uploadProgressLayout the progress layout
     */
    private fun refreshUploadViews(event: Event, uploadStats: IMXMediaUploadListener.UploadStats?, uploadProgressLayout: View?) {
        if (null != uploadStats) {
            uploadProgressLayout!!.visibility = View.VISIBLE

            val uploadProgressStatsTextView = uploadProgressLayout.findViewById<TextView>(R.id.media_progress_text_view)
            val progressBar = uploadProgressLayout.findViewById<ProgressBar>(R.id.media_progress_view)

            if (null != uploadProgressStatsTextView) {
                uploadProgressStatsTextView.text = formatUploadStats(mContext, uploadStats)
            }

            if (null != progressBar) {
                progressBar.progress = uploadStats.mProgress
            }

            val cancelLayout = uploadProgressLayout.findViewById<View>(R.id.media_progress_cancel)

            if (null != cancelLayout) {
                cancelLayout.tag = event

                cancelLayout.setOnClickListener {
                    if (event === cancelLayout.tag) {
                        if (null != mVectorMessagesAdapterEventsListener) {
                            mVectorMessagesAdapterEventsListener!!.onEventAction(event, "", R.id.ic_action_vector_cancel_upload)
                        }
                    }
                }
            }
        } else {
            uploadProgressLayout!!.visibility = View.GONE
        }
    }

    /**
     * Manage the file download items.
     *
     * @param convertView the message cell view.
     * @param event       the event
     * @param fileMessage the file message.
     * @param position    the position in the listview.
     */
    fun managePendingFileDownload(convertView: View, event: Event, fileMessage: FileMessage, position: Int) {
        val downloadId = mMediasCache.downloadIdFromUrl(fileMessage.getUrl())
        val downloadProgressLayout = convertView.findViewById<View>(R.id.content_download_progress_layout)
                ?: return

        downloadProgressLayout.tag = downloadId

        // no download in progress
        if (null != downloadId) {
            downloadProgressLayout.visibility = View.VISIBLE

            mMediasCache.addDownloadListener(downloadId, object : MXMediaDownloadListener() {
                override fun onDownloadCancel(downloadId: String?) {
                    if (TextUtils.equals(downloadId, downloadProgressLayout.tag as String)) {
                        downloadProgressLayout.visibility = View.GONE
                    }
                }

                override fun onDownloadError(downloadId: String?, jsonElement: JsonElement?) {
                    if (TextUtils.equals(downloadId, downloadProgressLayout.tag as String)) {
                        var error: MatrixError? = null

                        try {
                            error = JsonUtils.toMatrixError(jsonElement)
                        } catch (e: Exception) {
                            Log.e(LOG_TAG, "Cannot cast to Matrix error " + e.localizedMessage, e)
                        }

                        downloadProgressLayout.visibility = View.GONE

                        if (null != error && error.isSupportedErrorCode) {
                            Toast.makeText(mContext, error.localizedMessage, Toast.LENGTH_LONG).show()
                        } else if (null != jsonElement) {
                            Toast.makeText(mContext, jsonElement.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onDownloadProgress(aDownloadId: String?, stats: IMXMediaDownloadListener.DownloadStats?) {
                    if (TextUtils.equals(aDownloadId, downloadProgressLayout.tag as String)) {
                        refreshDownloadViews(event, stats, downloadProgressLayout)
                    }
                }

                override fun onDownloadComplete(aDownloadId: String?) {
                    if (TextUtils.equals(aDownloadId, downloadProgressLayout.tag as String)) {
                        downloadProgressLayout.visibility = View.GONE

                        if (null != mVectorMessagesAdapterEventsListener) {
                            mVectorMessagesAdapterEventsListener!!.onMediaDownloaded(position)
                        }
                    }
                }
            })
            refreshDownloadViews(event, mMediasCache.getStatsForDownloadId(downloadId), downloadProgressLayout)
        } else {
            downloadProgressLayout.visibility = View.GONE
        }
    }

    /**
     * Show the upload failure items
     *
     * @param convertView the cell view
     * @param type        the media type
     * @param show        true to show the failure items
     */
    private fun showUploadFailure(convertView: View, type: Int, show: Boolean) {
        if (MessagesAdapter.ROW_TYPE_FILE == type) {
            val fileTextView = convertView.findViewById<TextView>(R.id.messagesAdapter_filename)

            fileTextView?.setTextColor(if (show) mNotSentMessageTextColor else mDefaultMessageTextColor)
        } else if (MessagesAdapter.ROW_TYPE_IMAGE == type || MessagesAdapter.ROW_TYPE_VIDEO == type) {
            val failedLayout = convertView.findViewById<View>(R.id.media_upload_failed)

            if (null != failedLayout) {
                failedLayout.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * Refresh the download views
     *
     * @param event                  the event
     * @param downloadStats          the download stats
     * @param downloadProgressLayout the download progress layout
     */
    private fun refreshDownloadViews(event: Event, downloadStats: IMXMediaDownloadListener.DownloadStats?, downloadProgressLayout: View?) {
        if (null != downloadStats && isMediaDownloading(event)) {
            downloadProgressLayout!!.visibility = View.VISIBLE

            val downloadProgressStatsTextView = downloadProgressLayout.findViewById<TextView>(R.id.media_progress_text_view)
            val progressBar = downloadProgressLayout.findViewById<ProgressBar>(R.id.media_progress_view)

            if (null != downloadProgressStatsTextView) {
                downloadProgressStatsTextView.text = formatDownloadStats(mContext, downloadStats)
            }

            if (null != progressBar) {
                progressBar.progress = downloadStats.mProgress
            }

            val cancelLayout = downloadProgressLayout.findViewById<View>(R.id.media_progress_cancel)

            if (null != cancelLayout) {
                cancelLayout.tag = event

                cancelLayout.setOnClickListener {
                    if (event === cancelLayout.tag) {
                        if (null != mVectorMessagesAdapterEventsListener) {
                            mVectorMessagesAdapterEventsListener!!.onEventAction(event, "", R.id.ic_action_vector_cancel_download)
                        }
                    }
                }
            }
        } else {
            downloadProgressLayout!!.visibility = View.GONE
        }
    }

    /**
     * Tells if the downloadId is the media download id.
     *
     * @param event the event
     * @return true if the media is downloading (not the thumbnail)
     */
    private fun isMediaDownloading(event: Event): Boolean {

        if (TextUtils.equals(event.getType(), Event.EVENT_TYPE_MESSAGE)) {
            val message = JsonUtils.toMessage(event.content)

            var url: String? = null

            if (TextUtils.equals(message.msgtype, Message.MSGTYPE_IMAGE)) {
                url = JsonUtils.toImageMessage(event.content).getUrl()
            } else if (TextUtils.equals(message.msgtype, Message.MSGTYPE_VIDEO)) {
                url = JsonUtils.toVideoMessage(event.content).getUrl()
            } else if (TextUtils.equals(message.msgtype, Message.MSGTYPE_FILE)) {
                url = JsonUtils.toFileMessage(event.content).getUrl()
            }

            if (!TextUtils.isEmpty(url)) {
                return null != mSession.mediaCache.downloadIdFromUrl(url)
            }
        }

        return false
    }

    companion object {
        private val LOG_TAG = MessagesAdapterMediasHelper::class.java.simpleName

        /**
         * Update the progress bar
         *
         * @param uploadProgressLayout the progress layout
         * @param progress             the progress value
         */
        private fun updateUploadProgress(uploadProgressLayout: View, progress: Int) {
            val progressBar = uploadProgressLayout.findViewById<ProgressBar>(R.id.media_progress_view)

            if (null != progressBar) {
                progressBar.progress = progress
            }
        }

        //==============================================================================================================
        // Download / upload progress management
        //==============================================================================================================

        /**
         * Format a second time range.
         *
         * @param seconds the seconds time
         * @return the formatted string
         */
        private fun vectorRemainingTimeToString(context: Context, seconds: Int): String {
            return if (seconds < 0) {
                ""
            } else if (seconds <= 1) {
                "< 1s"
            } else if (seconds < 60) {
                context.getString(R.string.attachment_remaining_time_seconds, seconds)
            } else if (seconds < 3600) {
                context.getString(R.string.attachment_remaining_time_minutes, seconds / 60, seconds % 60)
            } else {
                DateUtils.formatElapsedTime(seconds.toLong())
            }
        }

        /**
         * Format the download / upload stats.
         *
         * @param context          the context.
         * @param progressFileSize the upload / download media size.
         * @param fileSize         the expected media size.
         * @param remainingTime    the remaining time (seconds)
         * @return the formatted string.
         */
        private fun formatStats(context: Context, progressFileSize: Int, fileSize: Int, remainingTime: Int): String {
            var formattedString = ""

            if (fileSize > 0) {
                formattedString += android.text.format.Formatter.formatShortFileSize(context, progressFileSize.toLong())
                formattedString += " / " + android.text.format.Formatter.formatShortFileSize(context, fileSize.toLong())
            }

            if (remainingTime > 0) {
                if (!TextUtils.isEmpty(formattedString)) {
                    formattedString += " (" + vectorRemainingTimeToString(context, remainingTime) + ")"
                } else {
                    formattedString += vectorRemainingTimeToString(context, remainingTime)
                }
            }

            return formattedString
        }

        /**
         * Format the download stats.
         *
         * @param context the context.
         * @param stats   the download stats
         * @return the formatted string
         */
        private fun formatDownloadStats(context: Context, stats: IMXMediaDownloadListener.DownloadStats): String {
            return formatStats(context, stats.mDownloadedSize, stats.mFileSize, stats.mEstimatedRemainingTime)
        }

        /**
         * Format the upload stats.
         *
         * @param context the context.
         * @param stats   the upload stats
         * @return the formatted string
         */
        private fun formatUploadStats(context: Context, stats: IMXMediaUploadListener.UploadStats): String {
            return formatStats(context, stats.mUploadedSize, stats.mFileSize, stats.mEstimatedRemainingTime)
        }
    }
}