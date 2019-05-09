package vmodev.clearkeep.ultis

import android.content.ClipDescription
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
import im.vector.R
import im.vector.activity.VectorRoomActivity
import im.vector.fragments.ImageSizeSelectionDialogFragment
import im.vector.fragments.VectorMessageListFragment
import im.vector.util.PreferencesManager
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.db.MXMediaCache
import org.matrix.androidsdk.rest.model.message.Message
import org.matrix.androidsdk.util.ImageUtils
import org.matrix.androidsdk.util.Log
import org.matrix.androidsdk.util.ResourceUtils
import vmodev.clearkeep.activities.RoomActivity
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.ArrayList

class RoomMediasSender constructor(val roomActivity: RoomActivity, val vectorMessageListFragment: VectorMessageListFragment, val mediasCache: MXMediaCache) {
    private val LOG_TAG = im.vector.util.VectorRoomMediasSender::class.java.simpleName

    private val TAG_FRAGMENT_IMAGE_SIZE_DIALOG = "TAG_FRAGMENT_IMAGE_SIZE_DIALOG"

    /**
     * This listener is displayed when the image has been resized.
     */
    private interface OnImageUploadListener {
        // the image has been successfully resized and the upload starts
        fun onDone()

        // the resize has been cancelled
        fun onCancel()
    }

    private var mImageSizesListDialog: AlertDialog? = null

    // the linked room activity
    private var mVectorRoomActivity: RoomActivity? = null;

    // the room fragment
    private var mVectorMessageListFragment: VectorMessageListFragment? = null;

    // the medias cache
    private var mMediasCache: MXMediaCache? = null;

    // the background thread
    private var mHandlerThread: HandlerThread? = null
    private var mMediasSendingHandler: android.os.Handler? = null

    // pending
    private var mSharedDataItems: MutableList<RoomMediaMessage>? = null
    private var mImageCompressionDescription: String? = null

    // media compression
    private val MEDIA_COMPRESSION_CHOOSE = 0

    /**
     * Constructor
     *
     * @param roomActivity the room activity.
     */
    init {
        mVectorRoomActivity = roomActivity
        mVectorMessageListFragment = vectorMessageListFragment
        mMediasCache = mediasCache

        if (null == mHandlerThread) {
            mHandlerThread = HandlerThread("VectorRoomMediasSender", Thread.MIN_PRIORITY)
            mHandlerThread!!.start()

            mMediasSendingHandler = android.os.Handler(mHandlerThread!!.looper)
        }
    }

    /**
     * Resume any camera image upload that could have been in progress and
     * stopped due to activity lifecycle event.
     */
    fun resumeResizeMediaAndSend() {
        if (null != mSharedDataItems) {
            mVectorRoomActivity!!.runOnUiThread { sendMedias() }
        }
    }

    /**
     * Send a list of images from their URIs
     *
     * @param sharedDataItems the media URIs
     */
    fun sendMedias(sharedDataItems: List<RoomMediaMessage>?) {
        if (null != sharedDataItems) {
            mSharedDataItems = ArrayList(sharedDataItems)
            sendMedias()
        }
    }

    /**
     * Send a list of images from their URIs
     */
    private fun sendMedias() {
        // sanity checks
        if (null == mVectorRoomActivity || null == mVectorMessageListFragment || null == mMediasCache) {
            Log.d(LOG_TAG, "sendMedias : null parameters")
            return
        }

        // detect end of messages sending
        if (null == mSharedDataItems || 0 == mSharedDataItems!!.size) {
            Log.d(LOG_TAG, "sendMedias : done")
            mImageCompressionDescription = null
            mSharedDataItems = null

            mVectorRoomActivity!!.runOnUiThread {
                mVectorMessageListFragment!!.scrollToBottom()
                mVectorRoomActivity!!.cancelSelectionMode()
                mVectorRoomActivity!!.hideWaitingView()
            }

            return
        }

        // display a spinner
        mVectorRoomActivity!!.cancelSelectionMode()
        mVectorRoomActivity!!.showWaitingView()

        Log.d(LOG_TAG, "sendMedias : " + mSharedDataItems!!.size + " items to send")

        mMediasSendingHandler!!.post(Runnable {
            val sharedDataItem = mSharedDataItems!![0]
            var mimeType: String? = sharedDataItem.getMimeType(mVectorRoomActivity)

            // avoid null case
            if (null == mimeType) {
                mimeType = ""
            }

            if (TextUtils.equals(ClipDescription.MIMETYPE_TEXT_INTENT, mimeType)) {
                Log.d(LOG_TAG, "sendMedias :  unsupported mime type")
                // don't know how to manage it -> skip it
                // GA issue
                if (mSharedDataItems!!.size > 0) {
                    mSharedDataItems!!.removeAt(0)
                }
                sendMedias()
            } else if (null == sharedDataItem.uri && (TextUtils.equals(ClipDescription.MIMETYPE_TEXT_PLAIN, mimeType) || TextUtils.equals(ClipDescription.MIMETYPE_TEXT_HTML, mimeType))) {
                sendTextMessage(sharedDataItem)
            } else {
                // check if it is an uri
                // else we don't know what to do
                if (null == sharedDataItem.uri) {
                    Log.e(LOG_TAG, "sendMedias : null uri")
                    // manage others
                    if (mSharedDataItems!!.size > 0) {
                        mSharedDataItems!!.removeAt(0)
                    }
                    sendMedias()
                    return@Runnable
                }

                val fFilename = sharedDataItem.getFileName(mVectorRoomActivity)

                val resource = ResourceUtils.openResource(mVectorRoomActivity, sharedDataItem.uri, sharedDataItem.getMimeType(mVectorRoomActivity))

                if (null == resource) {
                    Log.e(LOG_TAG, "sendMedias : $fFilename is not found")

                    mVectorRoomActivity!!.runOnUiThread {
                        Toast.makeText(mVectorRoomActivity,
                                mVectorRoomActivity!!.getString(R.string.room_message_file_not_found),
                                Toast.LENGTH_LONG).show()
                    }

                    // manage others
                    if (mSharedDataItems!!.size > 0) {
                        mSharedDataItems!!.removeAt(0)
                    }
                    sendMedias()

                    return@Runnable
                }

                if (mimeType.startsWith("image/") && (ResourceUtils.MIME_TYPE_JPEG == mimeType
                                || ResourceUtils.MIME_TYPE_JPG == mimeType
                                || ResourceUtils.MIME_TYPE_IMAGE_ALL == mimeType)) {
                    sendJpegImage(sharedDataItem, resource)
                } else {
                    resource.close()
                    mVectorRoomActivity!!.runOnUiThread { mVectorMessageListFragment!!.sendMediaMessage(sharedDataItem) }

                    // manage others
                    if (mSharedDataItems!!.size > 0) {
                        mSharedDataItems!!.removeAt(0)
                    }
                    sendMedias()
                }
            }
        })
    }

    //================================================================================
    // text messages management
    //================================================================================

    /**
     * Send a text message.
     *
     * @param sharedDataItem the media item.
     */
    private fun sendTextMessage(sharedDataItem: RoomMediaMessage) {
        val sequence = sharedDataItem.text
        val htmlText = sharedDataItem.htmlText

        // content only text -> insert it in the room editor
        // to let the user decides to send the message
        if (!TextUtils.isEmpty(sequence) && null == htmlText) {
            Handler(Looper.getMainLooper()).post { mVectorRoomActivity!!.insertTextInTextEditor(sequence!!.toString()) }
        } else {

            var text: String? = null

            if (null == sequence) {
                if (null != htmlText) {
                    text = Html.fromHtml(htmlText).toString()
                }
            } else {
                text = sequence.toString()
            }

            Log.d(LOG_TAG, "sendTextMessage " + text!!)

            val fText = text

            mVectorRoomActivity!!.runOnUiThread { mVectorRoomActivity!!.sendMessage(fText, htmlText, Message.FORMAT_MATRIX_HTML, false) }
        }

        // manage others
        if (mSharedDataItems!!.size > 0) {
            mSharedDataItems!!.removeAt(0)
        }
        sendMedias()
    }

    //================================================================================
    // image messages management
    //================================================================================

    /**
     * Send  message.
     *
     * @param sharedDataItem the item to send
     * @param resource       the media resource
     */
    private fun sendJpegImage(sharedDataItem: RoomMediaMessage, resource: ResourceUtils.Resource) {
        val mimeType = sharedDataItem.getMimeType(mVectorRoomActivity)

        // save the file in the filesystem
        val mediaUrl = mMediasCache!!.saveMedia(resource.mContentStream, null, mimeType)
        resource.close()

        mVectorRoomActivity!!.runOnUiThread {
            if (null != mSharedDataItems && mSharedDataItems!!.size > 0) {
                sendJpegImage(sharedDataItem, mediaUrl, mimeType, object : OnImageUploadListener {
                    override fun onDone() {
                        // reported by GA
                        if (null != mSharedDataItems && mSharedDataItems!!.size > 0) {
                            mSharedDataItems!!.removeAt(0)
                        }
                        // go to the next item
                        sendMedias()
                    }

                    override fun onCancel() {
                        // cancel any media sending
                        // reported by GA
                        if (null != mSharedDataItems) {
                            mSharedDataItems!!.clear()
                        }
                        sendMedias()
                    }
                })
            }
        }
    }

    //================================================================================
    // Image resizing
    //================================================================================

    /**
     * Class storing the image information
     */
    private inner class ImageSize {
        var mWidth: Int = 0
        var mHeight: Int = 0

        constructor(width: Int, height: Int) {
            mWidth = width
            mHeight = height
        }

        constructor(anotherOne: ImageSize) {
            mWidth = anotherOne.mWidth
            mHeight = anotherOne.mHeight
        }

        /**
         * Compute the image size to fit in a square.
         *
         * @param maxSide the square to fit size
         * @return the image size
         */
        internal fun computeSizeToFit(maxSide: Float): ImageSize {
            if (0f == maxSide) {
                return ImageSize(0, 0)
            }

            val resized = ImageSize(this)

            if (mWidth > maxSide || mHeight > maxSide) {
                val ratioX = (maxSide / mWidth).toDouble()
                val ratioY = (maxSide / mHeight).toDouble()

                var scale = Math.min(ratioX, ratioY)

                // the ratio must a power of 2
                scale = 1.0 / Integer.highestOneBit(Math.floor(1.0 / scale).toInt())

                // apply the scale factor and padding to 2
                resized.mWidth = (Math.floor(resized.mWidth * scale / 2) * 2).toInt()
                resized.mHeight = (Math.floor(resized.mHeight * scale / 2) * 2).toInt()
            }

            return resized
        }

    }

    // max image sizes
    private val LARGE_IMAGE_SIZE = 2048
    private val MEDIUM_IMAGE_SIZE = 1024
    private val SMALL_IMAGE_SIZE = 512

    /**
     * Class storing an image compression size
     */
    private inner class ImageCompressionSizes {
        // high res image size
        var mFullImageSize: ImageSize? = null
        // large image size (i.e MEDIUM_IMAGE_SIZE < side < LARGE_IMAGE_SIZE)
        var mLargeImageSize: ImageSize? = null
        // medium  image size (i.e SMALL_IMAGE_SIZE < side < MEDIUM_IMAGE_SIZE)
        var mMediumImageSize: ImageSize? = null
        // small  image size (i.e side < SMALL_IMAGE_SIZE)
        var mSmallImageSize: ImageSize? = null

        /**
         * @return the image sizes list.
         */
        val imageSizesList: List<ImageSize>
            get() {
                val imagesSizesList = ArrayList<ImageSize>()

                if (null != mFullImageSize) {
                    imagesSizesList.add(mFullImageSize!!)
                }

                if (null != mLargeImageSize) {
                    imagesSizesList.add(mLargeImageSize!!)
                }

                if (null != mMediumImageSize) {
                    imagesSizesList.add(mMediumImageSize!!)
                }

                if (null != mSmallImageSize) {
                    imagesSizesList.add(mSmallImageSize!!)
                }

                return imagesSizesList
            }

        /**
         * Provides the defined compression description.
         *
         * @param context the context
         * @return the list of compression description
         */
        fun getImageSizesDescription(context: Context?): List<String> {
            val imagesSizesDescriptionList = ArrayList<String>()

            if (null != mFullImageSize) {
                imagesSizesDescriptionList.add(context!!.getString(R.string.compression_opt_list_original))
            }

            if (null != mLargeImageSize) {
                imagesSizesDescriptionList.add(context!!.getString(R.string.compression_opt_list_large))
            }

            if (null != mMediumImageSize) {
                imagesSizesDescriptionList.add(context!!.getString(R.string.compression_opt_list_medium))
            }

            if (null != mSmallImageSize) {
                imagesSizesDescriptionList.add(context!!.getString(R.string.compression_opt_list_small))
            }

            return imagesSizesDescriptionList
        }

        /**
         * Returns the scaled size from a compression description
         *
         * @param context                the context
         * @param compressionDescription the compression description
         * @return the scaled size.
         */
        fun getImageSize(context: Context?, compressionDescription: String): ImageSize? {
            val isGenuineDesc = TextUtils.equals(context!!.getString(R.string.compression_opt_list_original), compressionDescription)

            if (TextUtils.isEmpty(compressionDescription) || isGenuineDesc) {
                return mFullImageSize
            }

            val isSmallDesc = TextUtils.equals(context.getString(R.string.compression_opt_list_small), compressionDescription)
            val isMediumDesc = TextUtils.equals(context.getString(R.string.compression_opt_list_medium), compressionDescription)
            val isLargeDesc = TextUtils.equals(context.getString(R.string.compression_opt_list_large), compressionDescription)

            var size: ImageSize? = null

            // small size
            if (isSmallDesc) {
                size = mSmallImageSize
            }

            if (null == size && (isSmallDesc || isMediumDesc)) {
                size = mMediumImageSize
            }

            if (null == size && (isSmallDesc || isMediumDesc || isLargeDesc)) {
                size = mLargeImageSize
            }

            if (null == size) {
                size = mFullImageSize
            }

            return size
        }
    }

    /**
     * Compute the compressed image sizes.
     *
     * @param imageWidth  the image width
     * @param imageHeight the image height
     * @return the compression sizes
     */
    private fun computeImageSizes(imageWidth: Int, imageHeight: Int): ImageCompressionSizes {
        val imageCompressionSizes = ImageCompressionSizes()

        imageCompressionSizes.mFullImageSize = ImageSize(imageWidth, imageHeight)

        val maxSide = if (imageHeight > imageWidth) imageHeight else imageWidth

        // can be rescaled ?
        if (maxSide > SMALL_IMAGE_SIZE) {

            if (maxSide > LARGE_IMAGE_SIZE) {
                imageCompressionSizes.mLargeImageSize = imageCompressionSizes.mFullImageSize!!.computeSizeToFit(LARGE_IMAGE_SIZE.toFloat())

                // ensure that the computed is really smaller
                if (imageCompressionSizes.mLargeImageSize!!.mWidth == imageWidth && imageCompressionSizes.mLargeImageSize!!.mHeight == imageHeight) {
                    imageCompressionSizes.mLargeImageSize = null
                }
            }

            if (maxSide > MEDIUM_IMAGE_SIZE) {
                imageCompressionSizes.mMediumImageSize = imageCompressionSizes.mFullImageSize!!.computeSizeToFit(MEDIUM_IMAGE_SIZE.toFloat())

                // ensure that the computed is really smaller
                if (imageCompressionSizes.mMediumImageSize!!.mWidth == imageWidth && imageCompressionSizes.mMediumImageSize!!.mHeight == imageHeight) {
                    imageCompressionSizes.mMediumImageSize = null
                }
            }

            if (maxSide > SMALL_IMAGE_SIZE) {
                imageCompressionSizes.mSmallImageSize = imageCompressionSizes.mFullImageSize!!.computeSizeToFit(SMALL_IMAGE_SIZE.toFloat())

                // ensure that the computed is really smaller
                if (imageCompressionSizes.mSmallImageSize!!.mWidth == imageWidth && imageCompressionSizes.mSmallImageSize!!.mHeight == imageHeight) {
                    imageCompressionSizes.mSmallImageSize = null
                }
            }
        }

        return imageCompressionSizes
    }

    /**
     * @return the estimated file size (in bytes)
     */
    private fun estimateFileSize(imageSize: ImageSize?): Int {
        return if (null != imageSize) {
            // rounded the size in 1024 multiplier
            imageSize.mWidth * imageSize.mHeight * 2 / 10 / 1024 * 1024
        } else {
            0
        }
    }

    /**
     * Add an entry in the dialog lists.
     *
     * @param context         the context.
     * @param textsList       the texts list.
     * @param descriptionText the image description text
     * @param imageSize       the image size.
     * @param fileSize        the file size (in bytes)
     */
    private fun addDialogEntry(context: Context, textsList: MutableList<String>?, descriptionText: String, imageSize: ImageSize?, fileSize: Int) {
        if (null != imageSize && null != textsList) {
            textsList.add(descriptionText + ": "
                    + android.text.format.Formatter.formatFileSize(context, fileSize.toLong()) + " (" + imageSize.mWidth + "x" + imageSize.mHeight + ")")
        }
    }

    /**
     * Create the image compression texts list.
     *
     * @param context       the context
     * @param imageSizes    the image compressions
     * @param imagefileSize the image file size
     * @return the texts list to display
     */
    private fun getImagesCompressionTextsList(context: Context, imageSizes: ImageCompressionSizes, imagefileSize: Int): Array<String> {
        val textsList = ArrayList<String>()

        addDialogEntry(context, textsList, context.getString(R.string.compression_opt_list_original), imageSizes.mFullImageSize,
                imagefileSize)
        addDialogEntry(context, textsList, context.getString(R.string.compression_opt_list_large), imageSizes.mLargeImageSize,
                Math.min(estimateFileSize(imageSizes.mLargeImageSize), imagefileSize))
        addDialogEntry(context, textsList, context.getString(R.string.compression_opt_list_medium), imageSizes.mMediumImageSize,
                Math.min(estimateFileSize(imageSizes.mMediumImageSize), imagefileSize))
        addDialogEntry(context, textsList, context.getString(R.string.compression_opt_list_small), imageSizes.mSmallImageSize,
                Math.min(estimateFileSize(imageSizes.mSmallImageSize), imagefileSize))

        return textsList.toTypedArray()
    }

    /**
     * Apply an image with an expected size.
     * A rotation might also be applied if provided.
     *
     * @param anImageUrl    the image URI.
     * @param filename      the image filename.
     * @param srcImageSize  the source image size
     * @param dstImageSize  the expected image size.
     * @param rotationAngle the rotation angle to apply.
     * @return the resized image.
     */
    private fun resizeImage(anImageUrl: String?, filename: String?, srcImageSize: ImageSize?, dstImageSize: ImageSize?, rotationAngle: Int): String {
        var imageUrl = anImageUrl

        try {
            // got a dst image size
            if (null != dstImageSize) {
                val imageStream = FileInputStream(File(filename))

                var resizeBitmapStream: InputStream? = null

                try {
                    resizeBitmapStream = ImageUtils.resizeImage(imageStream, -1, (srcImageSize!!.mWidth + dstImageSize.mWidth - 1) / dstImageSize.mWidth, 75)
                } catch (ex: OutOfMemoryError) {
                    Log.e(LOG_TAG, "resizeImage out of memory : " + ex.message, ex)
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "resizeImage failed : " + e.message, e)
                }

                if (null != resizeBitmapStream) {
                    val bitmapURL = mMediasCache!!.saveMedia(resizeBitmapStream, null, ResourceUtils.MIME_TYPE_JPEG)

                    if (null != bitmapURL) {
                        imageUrl = bitmapURL
                    }

                    resizeBitmapStream.close()
                }
            }

            // try to apply exif rotation
            if (0 != rotationAngle) {
                // rotate the image content
                ImageUtils.rotateImage(mVectorRoomActivity, imageUrl, rotationAngle, mMediasCache)
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "resizeImage " + e.message, e)
        }

        return imageUrl!!
    }

    /**
     * Offer to resize the image before sending it.
     *
     * @param anImageUrl      the image url.
     * @param anImageMimeType the image mimetype
     * @param aListener       the listener
     */
    private fun sendJpegImage(roomMediaMessage: RoomMediaMessage,
                              anImageUrl: String?,
                              anImageMimeType: String,
                              aListener: OnImageUploadListener?) {
        // sanity check
        if (null == anImageUrl || null == aListener) {
            return
        }

        var isManaged = false

        // check if the media could be resized
        if (ResourceUtils.MIME_TYPE_JPEG == anImageMimeType
                || ResourceUtils.MIME_TYPE_JPG == anImageMimeType
                || ResourceUtils.MIME_TYPE_IMAGE_ALL == anImageMimeType) {
            System.gc()
            val imageStream: FileInputStream

            try {
                val uri = Uri.parse(anImageUrl)
                val filename = uri.path

                val rotationAngle = ImageUtils.getRotationAngleForBitmap(mVectorRoomActivity, uri)

                imageStream = FileInputStream(File(filename))

                val fileSize = imageStream.available()

                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                options.outWidth = -1
                options.outHeight = -1

                // retrieve the image size
                try {
                    BitmapFactory.decodeStream(imageStream, null, options)
                } catch (e: OutOfMemoryError) {
                    Log.e(LOG_TAG, "sendImageMessage out of memory error : " + e.message, e)
                }

                val imageSizes = computeImageSizes(options.outWidth, options.outHeight)

                imageStream.close()

                val prefResize = PreferencesManager.getSelectedDefaultMediaCompressionLevel(mVectorRoomActivity)
                if (prefResize > MEDIA_COMPRESSION_CHOOSE) {
                    // subtract "choose" option
                    var opt = prefResize - 1
                    // get highest index
                    val sizesIdx = imageSizes.imageSizesList.size - 1

                    // adjust selection if there are less than 4 sizes available
                    if (opt > 0 && sizesIdx < 3) {
                        opt -= 3 - sizesIdx
                    }
                    // bounds check
                    if (opt > sizesIdx)
                        opt = sizesIdx
                    else if (opt < 0) opt = 0

                    mImageCompressionDescription = imageSizes.getImageSizesDescription(mVectorRoomActivity)[opt]
                }

                // the user already selects a compression
                if (null != mImageCompressionDescription) {
                    isManaged = true

                    val expectedSize = imageSizes.getImageSize(mVectorRoomActivity, mImageCompressionDescription!!)
                    val fImageUrl = resizeImage(anImageUrl, filename, imageSizes.mFullImageSize, expectedSize, rotationAngle)

                    mVectorRoomActivity!!.runOnUiThread {
                        mVectorMessageListFragment!!.sendMediaMessage(RoomMediaMessage(Uri.parse(fImageUrl),
                                roomMediaMessage.getFileName(mVectorRoomActivity)))
                        aListener.onDone()
                    }
                } else if (null != imageSizes.mSmallImageSize) {
                    isManaged = true

                    val fm = mVectorRoomActivity!!.supportFragmentManager
                    val fragment = fm.findFragmentByTag(TAG_FRAGMENT_IMAGE_SIZE_DIALOG) as ImageSizeSelectionDialogFragment?

                    fragment?.dismissAllowingStateLoss()

                    val stringsArray = getImagesCompressionTextsList(mVectorRoomActivity!!, imageSizes, fileSize)

                    mImageSizesListDialog = AlertDialog.Builder(mVectorRoomActivity!!)
                            .setTitle(im.vector.R.string.compression_options)
                            .setSingleChoiceItems(stringsArray, -1) { dialog, which ->
                                mImageSizesListDialog!!.dismiss()

                                mVectorRoomActivity!!.runOnUiThread {
                                    mVectorRoomActivity!!.showWaitingView()

                                    val thread = Thread(Runnable {
                                        var expectedSize: ImageSize? = null

                                        // full size
                                        if (0 != which) {
                                            expectedSize = imageSizes.imageSizesList[which]
                                        }

                                        // stored the compression selected by the user
                                        mImageCompressionDescription = imageSizes.getImageSizesDescription(mVectorRoomActivity)[which]

                                        val fImageUrl = resizeImage(anImageUrl, filename, imageSizes.mFullImageSize, expectedSize, rotationAngle)

                                        mVectorRoomActivity!!.runOnUiThread {
                                            mVectorMessageListFragment!!.sendMediaMessage(RoomMediaMessage(Uri.parse(fImageUrl),
                                                    roomMediaMessage.getFileName(mVectorRoomActivity)))
                                            aListener.onDone()
                                        }
                                    })

                                    thread.priority = Thread.MIN_PRIORITY
                                    thread.start()
                                }
                            }
                            .setOnCancelListener {
                                mImageSizesListDialog = null
                                aListener?.onCancel()
                            }
                            .show()
                }// can be rescaled ?
            } catch (e: Exception) {
                Log.e(LOG_TAG, "sendImageMessage failed " + e.message, e)
            }

        }

        // cannot resize, let assumes that it has been done
        if (!isManaged) {
            mVectorRoomActivity!!.runOnUiThread {
                mVectorMessageListFragment!!.sendMediaMessage(roomMediaMessage)
                aListener?.onDone()
            }
        }
    }
}