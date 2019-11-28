package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import im.vector.BuildConfig
import im.vector.Matrix
import im.vector.R
import im.vector.activity.*
import im.vector.extensions.getFingerprintHumanReadable
import im.vector.fragments.VectorMessagesFragment
import im.vector.fragments.VectorReadReceiptsDialogFragment
import im.vector.fragments.VectorUserGroupsDialogFragment
import im.vector.listeners.IMessagesAdapterActionsListener
import im.vector.listeners.YesNoListener
import im.vector.receiver.VectorUniversalLinkReceiver
import im.vector.util.*
import im.vector.widgets.WidgetsManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.adapters.AbstractMessagesAdapter
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.MXPatterns
import org.matrix.androidsdk.core.PermalinkUtils
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo
import org.matrix.androidsdk.db.MXMediaCache
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.fragments.MatrixMessagesFragment
import org.matrix.androidsdk.listeners.MXMediaDownloadListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.message.FileMessage
import org.matrix.androidsdk.rest.model.message.ImageMessage
import org.matrix.androidsdk.rest.model.message.Message
import org.matrix.androidsdk.rest.model.message.VideoMessage
import vmodev.clearkeep.activities.ProfileActivity
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.activities.UserInformationActivity
import vmodev.clearkeep.activities.ViewUserProfileActivity
import vmodev.clearkeep.adapters.MessagesAdapter
import vmodev.clearkeep.fragments.BaseMessageListFragment.Companion.VERIF_REQ_CODE
import vmodev.clearkeep.ultis.SharedPreferencesUtils
import java.io.File
import java.util.*

class MessageListFragment : MatrixMessageListFragment<MessagesAdapter>(), IMessagesAdapterActionsListener {

    // Data to wait for permission
    private var mPendingMenuAction: Int = 0
    private var mPendingMediaUrl: String? = null
    private var mPendingMediaMimeType: String? = null
    private var mPendingFilename: String? = null
    private var mPendingEncryptedFileInfo: EncryptedFileInfo? = null

    private var mListener: VectorMessageListFragmentListener? = null

    private var mVectorImageGetter: VectorImageGetter? = null

    // Dialog displayed after sending the re-request of e2e key
    private var mReRequestKeyDialog: AlertDialog? = null

    /**
     * Get the message list view
     *
     * @return message list view
     */
    val messageListView: ListView
        get() = mMessageListView

    /**
     * Get the message adapter
     *
     * @return message adapter
     */
    val messageAdapter: AbstractMessagesAdapter
        get() = mAdapter

    /**
     * Get the current selected event, or null if no event is selected.
     */
    val currentSelectedEvent: Event?
        get() {
            return if (null != mAdapter) {
                mAdapter.currentSelectedEvent
            } else null

        }

    override fun onTombstoneLinkClicked(roomId: String?, senderId: String?) {
        // Join the room and open it
        showInitLoading()

        // Extract the server name
        val serverName = MXPatterns.extractServerNameFromId(senderId)

        var viaServers: List<String>? = null

        if (serverName != null) {
            viaServers = listOf(serverName)
        }

        mSession.joinRoom(roomId, viaServers, object : ApiCallback<String> {
            override fun onNetworkError(e: Exception) {
                hideInitLoading()
                Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onMatrixError(e: MatrixError) {
                hideInitLoading()
                Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onUnexpectedError(e: Exception) {
                hideInitLoading()
                Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onSuccess(info: String) {
                hideInitLoading()

                // Open the room
                if (isAdded) {
                    val intent = Intent(activity, VectorRoomActivity::class.java)
                    intent.putExtra(VectorRoomActivity.EXTRA_ROOM_ID, info)
                    intent.putExtra(VectorRoomActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
                    activity!!.startActivity(intent)
                    activity!!.finish()
                }
            }
        })
    }

    private val mDeviceVerificationCallback = object : ApiCallback<Void> {
        override fun onSuccess(info: Void) {
            mAdapter.notifyDataSetChanged()
        }

        override fun onNetworkError(e: Exception) {
            mAdapter.notifyDataSetChanged()
        }

        override fun onMatrixError(e: MatrixError) {
            mAdapter.notifyDataSetChanged()
        }

        override fun onUnexpectedError(e: Exception) {
            mAdapter.notifyDataSetChanged()
        }
    }

    private val mYesNoListener = object : YesNoListener {
        override fun yes() {
            mAdapter.notifyDataSetChanged()
        }

        override fun no() {
            mAdapter.notifyDataSetChanged()
        }
    }

    private var mInvalidIndexesCount = 0

    private val mHighlightStatusByEventId = HashMap<String, Boolean>()

    interface VectorMessageListFragmentListener {
        /**
         * Display a spinner to warn the user that a back pagination is in progress.
         */
        fun showPreviousEventsLoadingWheel()

        /**
         * Dismiss the back pagination progress.
         */
        fun hidePreviousEventsLoadingWheel()

        /**
         * Display a spinner to warn the user that a forward pagination is in progress.
         */
        fun showNextEventsLoadingWheel()

        /**
         * Dismiss the forward pagination progress.
         */
        fun hideNextEventsLoadingWheel()

        /**
         * Display a spinner to warn the user that the initialization is in progress.
         */
        fun showMainLoadingWheel()

        /**
         * Dismiss the initialization spinner.
         */
        fun hideMainLoadingWheel()

        /**
         * User has selected/unselected an event
         *
         * @param currentSelectedEvent the current selected event, or null if no event is selected
         */
        fun onSelectedEventChange(currentSelectedEvent: Event?)
    }

    /**
     * Update the listener
     *
     * @param listener the new listener
     */
    fun setListener(listener: VectorMessageListFragmentListener?) {
        mListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val args = arguments
        // when an event id is defined, display a thick green line to its left
        if (args!!.containsKey(ARG_EVENT_ID)) {
            mAdapter.setSearchedEventId(args.getString(ARG_EVENT_ID, ""))
        }
        if (null != mRoom) {
            mAdapter.mIsRoomEncrypted = mRoom.isEncrypted
        }
        if (null != mSession) {
            mVectorImageGetter = VectorImageGetter(mSession)
            mAdapter.setImageGetter(mVectorImageGetter!!)
        }
        mMessageListView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                onRowClick(position)
            }
        }
        val isDarkMode = SharedPreferencesUtils.getBoolean(activity, "DARK_MODE")
        if (isDarkMode) {
            mMessageListView.setBackgroundColor(Color.parseColor("#101010"))
        } else {
            mMessageListView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        return view
    }

    override fun createMessagesFragmentInstance(roomId: String): MatrixMessagesFragment {
        return VectorMessagesFragment.newInstance(roomId)
    }

    /**
     * @return the fragment tag to use to restore the matrix messages fragment
     */
    override fun getMatrixMessagesFragmentTag(): String {
        return javaClass.name + ".MATRIX_MESSAGE_FRAGMENT_TAG"
    }

    override fun onPause() {
        super.onPause()
        mAdapter.setVectorMessagesAdapterActionsListener(null)
        mAdapter.onPause()
        mVectorImageGetter!!.setListener(null)
    }


    override fun onResume() {
        super.onResume()
        mAdapter.setVectorMessagesAdapterActionsListener(this)
        mVectorImageGetter!!.setListener { mAdapter.notifyDataSetChanged() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allGranted(grantResults)) {
                onMediaAction(mPendingMenuAction, mPendingMediaUrl, mPendingMediaMimeType, mPendingFilename, mPendingEncryptedFileInfo)
                mPendingMediaUrl = null
                mPendingMediaMimeType = null
                mPendingFilename = null
                mPendingEncryptedFileInfo = null
            }
        }
    }

    override fun getSession(matrixId: String): MXSession {
        return Matrix.getMXSession(activity!!, matrixId)
    }

    override fun getMXMediaCache(): MXMediaCache? {
        return Matrix.getInstance(activity)!!.mediaCache
    }

    override fun createMessagesAdapter(): MessagesAdapter {
        return MessagesAdapter(mSession, activity!!, mxMediaCache!!)
    }

    /**
     * The user scrolls the list.
     * Apply an expected behaviour
     *
     * @param event the scroll event
     */
    override fun onListTouch(event: MotionEvent?) {
        // the user scroll over the keyboard
        // hides the keyboard
        if (mCheckSlideToHide && (event!!.y > mMessageListView.height)) {
            mCheckSlideToHide = false
            MXCActionBarActivity.dismissKeyboard(activity!!)
        }
    }

    override fun canAddEvent(event: Event): Boolean {
        return TextUtils.equals(WidgetsManager.WIDGET_EVENT_TYPE, event.getType()) || super.canAddEvent(event)
    }

    /**
     * Update the encrypted status of the room
     *
     * @param isEncrypted true when the room is encrypted
     */
    fun setIsRoomEncrypted(isEncrypted: Boolean) {
        if (mAdapter.mIsRoomEncrypted != isEncrypted) {
            mAdapter.mIsRoomEncrypted = isEncrypted
            mAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Cancel the messages selection mode.
     */
    fun cancelSelectionMode() {
        if (null != mAdapter) {
            mAdapter.cancelSelectionMode()
        }
    }

    /**
     * the user taps on the e2e icon
     *
     * @param event      the event
     * @param deviceInfo the deviceinfo
     */
    @SuppressLint("SetTextI18n")
    override fun onE2eIconClick(event: Event, deviceInfo: MXDeviceInfo?) {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater

        val encryptedEventContent = JsonUtils.toEncryptedEventContent(event.wireContent.asJsonObject)

        val layout = inflater.inflate(R.layout.dialog_encryption_info, null)

        var textView: TextView

        textView = layout.findViewById(R.id.encrypted_info_user_id)
        textView.text = event.getSender()

        textView = layout.findViewById(R.id.encrypted_info_curve25519_identity_key)
        if (null != deviceInfo) {
            textView.text = encryptedEventContent.sender_key
        } else {
            textView.setText(R.string.encryption_information_none)
        }

        textView = layout.findViewById(R.id.encrypted_info_claimed_ed25519_fingerprint_key)
        if (null != deviceInfo) {
            textView.text = (deviceInfo).getFingerprintHumanReadable()
        } else {
            textView.setText(R.string.encryption_information_none)
        }

        textView = layout.findViewById(R.id.encrypted_info_algorithm)
        textView.text = encryptedEventContent.algorithm

        textView = layout.findViewById(R.id.encrypted_info_session_id)
        textView.text = encryptedEventContent.session_id

        val decryptionErrorLabelTextView = layout.findViewById<View>(R.id.encrypted_info_decryption_error_label)
        textView = layout.findViewById(R.id.encrypted_info_decryption_error)

        if (null != event.cryptoError) {
            decryptionErrorLabelTextView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            textView.text = "**" + event.cryptoError.localizedMessage + "**"
        } else {
            decryptionErrorLabelTextView.visibility = View.GONE
            textView.visibility = View.GONE
        }

        val noDeviceInfoLayout = layout.findViewById<View>(R.id.encrypted_info_no_device_information_layout)
        val deviceInfoLayout = layout.findViewById<View>(R.id.encrypted_info_sender_device_information_layout)

        if (null != deviceInfo) {
            noDeviceInfoLayout.visibility = View.GONE
            deviceInfoLayout.visibility = View.VISIBLE

            textView = layout.findViewById(R.id.encrypted_info_name)
            textView.text = deviceInfo.displayName()

            textView = layout.findViewById(R.id.encrypted_info_device_id)
            textView.text = deviceInfo.deviceId

            textView = layout.findViewById(R.id.encrypted_info_verification)

            if (deviceInfo.isUnknown || deviceInfo.isUnverified) {
                textView.setText(R.string.encryption_information_not_verified)
            } else if (deviceInfo.isVerified) {
                textView.setText(R.string.encryption_information_verified)
            } else {
                textView.setText(R.string.encryption_information_blocked)
            }

            textView = layout.findViewById(R.id.encrypted_ed25519_fingerprint)
            textView.text = (deviceInfo).getFingerprintHumanReadable()
        } else {
            noDeviceInfoLayout.visibility = View.VISIBLE
            deviceInfoLayout.visibility = View.GONE
        }
        builder.setView(layout)
        builder.setTitle(R.string.encryption_information_title)
        builder.setNeutralButton(R.string.ok) { dialog, id ->
            // nothing to do
        }

        // the current id cannot be blocked, verified...
        if (!TextUtils.equals(encryptedEventContent.device_id, mSession.credentials.deviceId)) {
            if ((null == event.cryptoError) && (null != deviceInfo)) {
                if (deviceInfo.isUnverified || deviceInfo.isUnknown) {
                    builder.setNegativeButton(R.string.encryption_information_verify) { dialog, id ->
                        CommonActivityUtils.displayDeviceVerificationDialog(deviceInfo,
                                event.getSender(), mSession, activity, this, VERIF_REQ_CODE)
                    }

                    builder.setPositiveButton(R.string.encryption_information_block) { dialog, id ->
                        mSession.crypto!!.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_BLOCKED,
                                deviceInfo.deviceId, event.getSender(), mDeviceVerificationCallback)
                    }
                } else if (deviceInfo.isVerified) {
                    builder.setNegativeButton(R.string.encryption_information_unverify) { dialog, id ->
                        mSession.crypto!!.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_UNVERIFIED,
                                deviceInfo.deviceId, event.getSender(), mDeviceVerificationCallback)
                    }

                    builder.setPositiveButton(R.string.encryption_information_block) { dialog, id ->
                        mSession.crypto!!.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_BLOCKED,
                                deviceInfo.deviceId, event.getSender(), mDeviceVerificationCallback)
                    }
                } else { // BLOCKED
                    builder.setNegativeButton(R.string.encryption_information_verify) { dialog, id ->
                        CommonActivityUtils.displayDeviceVerificationDialog(deviceInfo,
                                event.getSender(), mSession, activity, this, VERIF_REQ_CODE)
                    }

                    builder.setPositiveButton(R.string.encryption_information_unblock) { dialog, id ->
                        mSession.crypto!!.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_UNVERIFIED,
                                deviceInfo.deviceId, event.getSender(), mDeviceVerificationCallback)
                    }
                }
            }
        }

        val dialog = builder.show()

        if (null == deviceInfo) {
            mSession.crypto!!
                    .getDeviceList()
                    .downloadKeys(listOf(event.getSender()), true, object : ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> {
                        override fun onSuccess(info: MXUsersDevicesMap<MXDeviceInfo>) {
                            val activity = activity

                            if ((null != activity) && !activity.isFinishing && dialog.isShowing) {
                                val encryptedEventContent = JsonUtils.toEncryptedEventContent(event.wireContent.asJsonObject)

                                val deviceInfo = mSession.crypto!!
                                        .deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm)

                                if (null != deviceInfo) {
                                    dialog.cancel()
                                    onE2eIconClick(event, deviceInfo)
                                }
                            }
                        }

                        override fun onNetworkError(e: Exception) {}

                        override fun onMatrixError(e: MatrixError) {}

                        override fun onUnexpectedError(e: Exception) {}
                    })
        }
    }

    /**
     * An action has been  triggered on an event.
     *
     * @param event   the event.
     * @param textMsg the event text
     * @param action  an action ic_action_vector_XXX
     */
    override fun onEventAction(event: Event, textMsg: String?, action: Int) {
        if (action == R.id.ic_action_vector_resend_message) {
            activity!!.runOnUiThread { resend(event) }
        } else if (action == R.id.ic_action_vector_redact_message) {
            activity!!.runOnUiThread(object : Runnable {
                override fun run() {
                    AlertDialog.Builder(activity!!)
                            .setMessage(getString(R.string.redact) + " ?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok
                            ) { dialog, id ->
                                if (event.isUndelivered || event.isUnknownDevice) {
                                    // delete from the store
                                    mSession.dataHandler.deleteRoomEvent(event)

                                    // remove from the adapter
                                    mAdapter.removeEventById(event.eventId)
                                    mAdapter.notifyDataSetChanged()
                                    mEventSendingListener.onMessageRedacted(event)
                                } else {
                                    redactEvent(event.eventId)
                                }
                            }
                            .setNegativeButton(R.string.cancel, null)
                            .show()
                }
            })
        } else if (action == R.id.ic_action_vector_copy) {
            activity!!.runOnUiThread(object : Runnable {
                override fun run() {
                    if (textMsg.isNullOrEmpty())
                        return
                    copyToClipboard(activity!!, textMsg)
                }
            })
        } else if ((action == R.id.ic_action_vector_cancel_upload) || (action == R.id.ic_action_vector_cancel_download)) {
            activity!!.runOnUiThread(object : Runnable {
                override fun run() {
                    AlertDialog.Builder(activity!!)
                            .setMessage(if ((action == R.id.ic_action_vector_cancel_upload))
                                R.string.attachment_cancel_upload
                            else
                                R.string.attachment_cancel_download)
                            .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {
                                    mRoom.cancelEventSending(event)

                                    activity!!.runOnUiThread(object : Runnable {
                                        override fun run() {
                                            mAdapter.notifyDataSetChanged()
                                        }
                                    })
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show()
                }
            })
        } else if (action == R.id.ic_action_vector_quote) {
            val attachedActivity = activity

            if ((null != attachedActivity) && (attachedActivity is RoomActivity) && !textMsg.isNullOrEmpty()) {
                // Quote all paragraphs instead
                val messageParagraphs = textMsg.split(("\n\n").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var quotedTextMsg = ""
                for (i in messageParagraphs.indices) {
                    if (messageParagraphs[i].trim { it <= ' ' } != "") {
                        quotedTextMsg += "> " + messageParagraphs[i]
                    }

                    if ((i + 1) != messageParagraphs.size) {
                        quotedTextMsg += "\n\n"
                    }
                }
                attachedActivity.insertQuoteInTextEditor(quotedTextMsg + "\n\n")
            }
        } else if ((action == R.id.ic_action_vector_share) || (action == R.id.ic_action_vector_forward) || (action == R.id.ic_action_vector_save)) {
            //
            val message = JsonUtils.toMessage(event.content)

            var mediaUrl: String? = null
            var mediaMimeType: String? = null
            var encryptedFileInfo: EncryptedFileInfo? = null

            if (message is ImageMessage) {
                val imageMessage = message

                mediaUrl = imageMessage.getUrl()
                mediaMimeType = imageMessage.mimeType
                encryptedFileInfo = imageMessage.file
            } else if (message is VideoMessage) {
                val videoMessage = message

                mediaUrl = videoMessage.getUrl()
                encryptedFileInfo = videoMessage.file

                if (null != videoMessage.info) {
                    mediaMimeType = videoMessage.info.mimetype
                }
            } else if (message is FileMessage) {
                val fileMessage = message

                mediaUrl = fileMessage.getUrl()
                mediaMimeType = fileMessage.mimeType
                encryptedFileInfo = fileMessage.file
            }

            // media file ?
            if (null != mediaUrl) {
                onMediaAction(action, mediaUrl, mediaMimeType, message.body, encryptedFileInfo)
            } else if ((action == R.id.ic_action_vector_share) || (action == R.id.ic_action_vector_forward) || (action == R.id.ic_action_vector_quote)) {
                // use the body
                val sendIntent = Intent()

                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, textMsg)
                sendIntent.type = "text/plain"

                if (action == R.id.ic_action_vector_forward) {
                    CommonActivityUtils.sendFilesTo(activity, sendIntent)
                } else {
                    startActivity(sendIntent)
                }
            }
        } else if (action == R.id.ic_action_vector_permalink) {
            copyToClipboard(activity!!, PermalinkUtils.createPermalink(event)!!)
        } else if (action == R.id.ic_action_vector_report) {
            onMessageReport(event)
        } else if ((action == R.id.ic_action_view_source) || (action == R.id.ic_action_view_decrypted_source)) {
            activity!!.runOnUiThread(object : Runnable {
                override fun run() {
                    val view = activity!!.layoutInflater.inflate(R.layout.dialog_event_content, null)
                    val textview = view.findViewById<TextView>(R.id.event_content_text_view)

                    val gson = GsonBuilder()
                            .disableHtmlEscaping()
                            .setPrettyPrinting()
                            .create()

                    textview.text = gson.toJson(JsonUtils.toJson(if ((action == R.id.ic_action_view_source)) event else event.clearEvent))

                    AlertDialog.Builder(activity!!)
                            .setView(view)
                            .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, id: Int) {
                                    dialog.cancel()
                                }
                            })
                            .show()
                }
            })
        } else if (action == R.id.ic_action_device_verification) {
            onE2eIconClick(event, mAdapter.getDeviceInfo(event.eventId))
        } else if (action == R.id.ic_action_re_request_e2e_key) {
            mSession.crypto!!.reRequestRoomKeyForEvent(event)

            mReRequestKeyDialog = AlertDialog.Builder(activity!!)
                    .setTitle(R.string.e2e_re_request_encryption_key_dialog_title)
                    .setMessage(R.string.e2e_re_request_encryption_key_dialog_content)
                    .setPositiveButton(R.string.ok, null)
                    .setOnDismissListener(object : DialogInterface.OnDismissListener {
                        override fun onDismiss(dialog: DialogInterface) {
                            mReRequestKeyDialog = null
                        }
                    })
                    .show()
        } else if (action == R.id.ic_action_edit) {
            val attachedActivity = activity

            if ((null != attachedActivity) && (attachedActivity is RoomActivity) && !textMsg.isNullOrEmpty()) {
                // Quote all paragraphs instead
                attachedActivity.insertSelectedMessageInTextEditor(event, textMsg)
            }
        }
    }

    /**
     * The event for which the user asked again for the key is now decrypted
     */
    override fun onEventDecrypted() {
        // Auto dismiss this dialog when the keys are received
        if (mReRequestKeyDialog != null) {
            mReRequestKeyDialog!!.dismiss()
        }
    }

    override fun onSelectedEventChange(currentSelectedEvent: Event?) {
        if (mListener != null && isAdded) {
            mListener!!.onSelectedEventChange(currentSelectedEvent)
        }
    }

    /**
     * The user reports a content problem to the server
     *
     * @param event the event to report
     */
    private fun onMessageReport(event: Event) {
        // add a text input
        val input = EditText(activity)

        AlertDialog.Builder(activity!!)
                .setTitle(R.string.room_event_action_report_prompt_reason)
                .setView(input)
                .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        val reason = input.text.toString()

                        mRoom.report(event.eventId, -100, reason, object : SimpleApiCallback<Void>(activity) {
                            override fun onSuccess(info: Void) {
                                AlertDialog.Builder(activity!!)
                                        .setMessage(R.string.room_event_action_report_prompt_ignore_user)
                                        .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                                            override fun onClick(dialog: DialogInterface, which: Int) {
                                                val userIdsList = ArrayList<String>()
                                                userIdsList.add(event.sender)

                                                mSession.ignoreUsers(userIdsList, object : SimpleApiCallback<Void>() {
                                                    override fun onSuccess(info: Void) {}
                                                })
                                            }
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .show()
                            }
                        })
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    /***
     * Manage save / share / forward actions on a media file
     *
     * @param menuAction    the menu action ACTION_VECTOR__XXX
     * @param mediaUrl      the media URL (must be not null)
     * @param mediaMimeType the mime type
     * @param filename      the filename
     */
    internal fun onMediaAction(menuAction: Int,
                               mediaUrl: String?,
                               mediaMimeType: String?,
                               filename: String?,
                               encryptedFileInfo: EncryptedFileInfo?) {
        // Sanitize file name in case `m.body` contains a path.
        val trimmedFileName = File(filename).name

        val mediasCache = Matrix.getInstance(activity)!!.mediaCache
        // check if the media has already been downloaded
        if (mediasCache!!.isMediaCached(mediaUrl, mediaMimeType)) {
            mediasCache.createTmpDecryptedMediaFile(mediaUrl, mediaMimeType, encryptedFileInfo, object : SimpleApiCallback<File>() {
                override fun onSuccess(file: File?) {
                    var file: File? = file ?: return
                    // sanity check

                    if (menuAction == ACTION_VECTOR_SAVE || menuAction == ACTION_VECTOR_OPEN) {
                        if (checkPermissions(PERMISSIONS_FOR_WRITING_FILES,
                                        this@MessageListFragment, PERMISSION_REQUEST_CODE)) {
                            CommonActivityUtils.saveMediaIntoDownloads(activity, file, trimmedFileName, mediaMimeType, object : SimpleApiCallback<String>() {
                                override fun onSuccess(savedMediaPath: String?) {
                                    if (null != savedMediaPath) {
                                        if (menuAction == ACTION_VECTOR_SAVE) {
                                            Toast.makeText(activity, getText(R.string.media_slider_saved), Toast.LENGTH_LONG).show()
                                        } else {
                                            openMedia(activity!!, savedMediaPath, mediaMimeType!!)
                                        }
                                    }
                                }
                            })
                        } else {
                            mPendingMenuAction = menuAction
                            mPendingMediaUrl = mediaUrl
                            mPendingMediaMimeType = mediaMimeType
                            mPendingFilename = filename
                            mPendingEncryptedFileInfo = encryptedFileInfo
                        }
                    } else {
                        // Move the file to the Share folder, to avoid it to be deleted because the Activity will be paused while the
                        // user select an application to share the file
                        file = mediasCache.moveToShareFolder(file!!, trimmedFileName)

                        // shared / forward
                        var mediaUri: Uri? = null
                        try {
                            mediaUri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID + ".fileProvider", file)
                        } catch (e: Exception) {
                            Log.e(LOG_TAG, "onMediaAction VectorContentProvider.absolutePathToUri: " + e.message, e)
                        }

                        if (null != mediaUri) {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.type = mediaMimeType
                            sendIntent.putExtra(Intent.EXTRA_STREAM, mediaUri)

                            if (menuAction == ACTION_VECTOR_FORWARD) {
                                CommonActivityUtils.sendFilesTo(activity, sendIntent)
                            } else {
                                startActivity(sendIntent)
                            }
                        }
                    }
                }
            })
        } else {
            // else download it
            val downloadId = mediasCache.downloadMedia(activity!!.applicationContext,
                    mSession.homeServerConfig, mediaUrl, mediaMimeType, encryptedFileInfo)
            mAdapter.notifyDataSetChanged()

            if (null != downloadId) {
                mediasCache.addDownloadListener(downloadId, object : MXMediaDownloadListener() {
                    override fun onDownloadError(downloadId: String?, jsonElement: JsonElement?) {
                        val error = JsonUtils.toMatrixError(jsonElement)

                        if ((null != error) && error.isSupportedErrorCode && (null != activity)) {
                            Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onDownloadComplete(aDownloadId: String?) {
                        if (aDownloadId == downloadId) {

                            activity!!.runOnUiThread(object : Runnable {
                                override fun run() {
                                    onMediaAction(menuAction, mediaUrl, mediaMimeType, trimmedFileName, encryptedFileInfo)
                                }
                            })
                        }
                    }
                })
            }
        }
    }

    /**
     * return true to display all the events.
     * else the unknown events will be hidden.
     */
    override fun isDisplayAllEvents(): Boolean {
        return PreferencesManager.displayAllEvents(activity)
    }

    override fun showLoadingBackProgress() {
        if (mListener != null && isAdded) {
            mListener!!.showPreviousEventsLoadingWheel()
        }
    }

    override fun hideLoadingBackProgress() {
        if (mListener != null && isAdded) {
            mListener!!.hidePreviousEventsLoadingWheel()
        }
    }

    override fun showLoadingForwardProgress() {
        if (mListener != null && isAdded) {
            mListener!!.showNextEventsLoadingWheel()
        }
    }

    override fun hideLoadingForwardProgress() {
        if (mListener != null && isAdded) {
            mListener!!.hideNextEventsLoadingWheel()
        }
    }

    override fun showInitLoading() {
        if (mListener != null && isAdded) {
            mListener!!.showMainLoadingWheel()
        }
    }

    override fun hideInitLoading() {
        if (mListener != null && isAdded) {
            mListener!!.hideMainLoadingWheel()
        }
    }

    override fun onRowLongClick(position: Int): Boolean {
        return false
    }

    /**
     * @return the image and video messages list
     */
    internal fun listSlidableMessages(): List<SlidableMediaInfo> {
        val res = ArrayList<SlidableMediaInfo>()

        for (position in 0 until mAdapter.count) {
            val row = mAdapter.getItem(position)

            if (row!!.event is EventGroup) {
                // Ignore EventGroup
                continue
            }

            val message = JsonUtils.toMessage(row.event.content)

            if (Message.MSGTYPE_IMAGE == message.msgtype) {
                val imageMessage = message as ImageMessage
                val info = SlidableMediaInfo()
                info.mMessageType = Message.MSGTYPE_IMAGE
                info.mFileName = imageMessage.body
                info.mMediaUrl = imageMessage.getUrl()
                info.mRotationAngle = imageMessage.rotation
                info.mOrientation = imageMessage.orientation
                info.mMimeType = imageMessage.mimeType
                info.mEncryptedFileInfo = imageMessage.file
                res.add(info)
            } else if (Message.MSGTYPE_VIDEO == message.msgtype) {
                val videoMessage = message as VideoMessage
                val info = SlidableMediaInfo()
                info.mMessageType = Message.MSGTYPE_VIDEO
                info.mFileName = videoMessage.body
                info.mMediaUrl = videoMessage.getUrl()
                info.mThumbnailUrl = if ((null != videoMessage.info)) videoMessage.info.thumbnail_url else null
                info.mMimeType = videoMessage.mimeType
                info.mEncryptedFileInfo = videoMessage.file
                res.add(info)
            }
        }

        return res
    }

    /**
     * Returns the mediaMessage position in listMediaMessages.
     *
     * @param mediaMessagesList the media messages list
     * @param mediaMessage      the imageMessage
     * @return the imageMessage position. -1 if not found.
     */
    internal fun getMediaMessagePosition(mediaMessagesList: List<SlidableMediaInfo>, mediaMessage: Message): Int {
        var url: String? = null

        if (mediaMessage is ImageMessage) {
            url = mediaMessage.getUrl()
        } else if (mediaMessage is VideoMessage) {
            url = mediaMessage.getUrl()
        }

        // sanity check
        if (null == url) {
            return -1
        }

        for (index in mediaMessagesList.indices) {
            if (mediaMessagesList[index].mMediaUrl == url) {
                return index
            }
        }

        return -1
    }

    override fun onRowClick(position: Int) {
        try {
            val row = mAdapter.getItem(position)
            val event = row!!.event

            // toggle selection mode
            mAdapter.onEventTap(event)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onRowClick() failed " + e.message, e)
        }

    }

    override fun onContentClick(position: Int) {
        try {
            val row = mAdapter.getItem(position)
            val event = row!!.event

            if (mAdapter.isInSelectionMode) {
                // cancel the selection mode.
                mAdapter.onEventTap(null)
                return
            }

            val message = JsonUtils.toMessage(event.content)

            // video and images are displayed inside a medias slider.
            if (Message.MSGTYPE_IMAGE == message.msgtype || (Message.MSGTYPE_VIDEO == message.msgtype)) {
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
            } else if (Message.MSGTYPE_FILE == message.msgtype || Message.MSGTYPE_AUDIO == message.msgtype) {
                val fileMessage = JsonUtils.toFileMessage(event.content)

                if (null != fileMessage.getUrl()) {
                    onMediaAction(ACTION_VECTOR_OPEN, fileMessage.getUrl(), fileMessage.mimeType, fileMessage.body, fileMessage.file)
                }
            } else {
                // toggle selection mode
                mAdapter.onEventTap(event)
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onContentClick() failed " + e.message, e)
        }

    }

    override fun onContentLongClick(position: Int): Boolean {
        return onRowLongClick(position)
    }

    override fun onAvatarClick(userId: String) {
//        try {
//            val roomDetailsIntent = Intent(activity, VectorMemberDetailsActivity::class.java)
//            // in preview mode
//            // the room is stored in a temporary store
//            // so provide an handle to retrieve it
//            if (null != roomPreviewData) {
//                roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_STORE_ID,
//                        Matrix.getInstance(activity)!!.addTmpStore(mEventTimeLine.store))
//            }
//
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_ROOM_ID, mRoom.roomId)
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, userId)
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
//            activity!!.startActivityForResult(roomDetailsIntent, VectorRoomActivity.GET_MENTION_REQUEST_CODE)
//        } catch (e: Exception) {
//            Log.e(LOG_TAG, "## onAvatarClick() failed " + e.message, e)
//        }
        var intent: Intent? = null
        if (userId.compareTo(mSession.myUserId) != 0) {
            intent = Intent(this.context, ViewUserProfileActivity::class.java)
            intent.putExtra(ViewUserProfileActivity.USER_ID, userId)
        } else {
            intent = Intent(this.context, ProfileActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onAvatarLongClick(userId: String): Boolean {
        if (activity is VectorRoomActivity) {
            try {
                val state = mRoom.state

                if (null != state) {
                    val displayName = state.getMemberName(userId)
                    if (!TextUtils.isEmpty(displayName)) {
                        (activity as VectorRoomActivity).insertUserDisplayNameInTextEditor(displayName)
                    }
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "## onAvatarLongClick() failed " + e.message, e)
            }

        }
        return true
    }

    override fun onSenderNameClick(userId: String, displayName: String) {
        if (activity is VectorRoomActivity) {
            try {
                (activity as VectorRoomActivity).insertUserDisplayNameInTextEditor(displayName)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "## onSenderNameClick() failed " + e.message, e)
            }

        }
    }

    override fun onMediaDownloaded(position: Int) {}

    override fun onMoreReadReceiptClick(eventId: String) {
        try {
            val fm = activity!!.supportFragmentManager

            var fragment = fm.findFragmentByTag(TAG_FRAGMENT_RECEIPTS_DIALOG) as VectorReadReceiptsDialogFragment?
            if (fragment != null) {
                fragment.dismissAllowingStateLoss()
            }
            fragment = VectorReadReceiptsDialogFragment.newInstance(mSession.myUserId, mRoom.roomId, eventId)
            fragment.show(fm, TAG_FRAGMENT_RECEIPTS_DIALOG)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onMoreReadReceiptClick() failed " + e.message, e)
        }

    }

    override fun onGroupFlairClick(userId: String, groupIds: List<String>) {
        try {
            val fm = activity!!.supportFragmentManager

            var fragment = fm.findFragmentByTag(TAG_FRAGMENT_USER_GROUPS_DIALOG) as VectorUserGroupsDialogFragment?
            if (fragment != null) {
                fragment.dismissAllowingStateLoss()
            }
            fragment = VectorUserGroupsDialogFragment.newInstance(mSession.myUserId, userId, groupIds)
            fragment!!.show(fm, TAG_FRAGMENT_USER_GROUPS_DIALOG)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onGroupFlairClick() failed " + e.message, e)
        }

    }

    override fun onURLClick(uri: Uri?) {
        try {
            if (null != uri) {
                val universalParams = VectorUniversalLinkReceiver.parseUniversalLink(uri)

                if (null != universalParams) {
                    // open the member sheet from the current activity
                    if (universalParams.containsKey(PermalinkUtils.ULINK_MATRIX_USER_ID_KEY)) {
                        val roomDetailsIntent = Intent(activity, VectorMemberDetailsActivity::class.java)
                        roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID,
                                universalParams[PermalinkUtils.ULINK_MATRIX_USER_ID_KEY])
                        roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
                        activity!!.startActivityForResult(roomDetailsIntent, VectorRoomActivity.GET_MENTION_REQUEST_CODE)
                    } else {
                        // pop to the home activity
                        val intent = Intent(activity, VectorHomeActivity::class.java)
                        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_UNIVERSAL_LINK, uri)
                        activity!!.startActivity(intent)
                    }
                } else {
                    openUrlInExternalBrowser(activity!!, uri)
                }
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onURLClick() failed " + e.message, e)
        }

    }

    override fun onMatrixUserIdClick(userId: String) {
        try {
            // start member details UI
            val roomDetailsIntent = Intent(activity, UserInformationActivity::class.java)
            roomDetailsIntent.putExtra(UserInformationActivity.USER_ID, userId)
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_ROOM_ID, mRoom.roomId)
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, userId)
//            roomDetailsIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
            startActivity(roomDetailsIntent)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## onMatrixUserIdClick() failed " + e.message, e)
        }

    }

    override fun onRoomAliasClick(roomAlias: String) {
        try {
            onURLClick(Uri.parse(PermalinkUtils.createPermalink(roomAlias)))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onRoomAliasClick failed " + e.localizedMessage, e)
        }

    }

    override fun onRoomIdClick(roomId: String) {
        try {
            onURLClick(Uri.parse(PermalinkUtils.createPermalink(roomId)))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onRoomIdClick failed " + e.localizedMessage, e)
        }

    }

    override fun onEventIdClick(eventId: String) {
        try {
            onURLClick(Uri.parse(PermalinkUtils.createPermalink(mRoom.roomId, eventId)))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onRoomIdClick failed " + e.localizedMessage, e)
        }

    }

    override fun onGroupIdClick(groupId: String) {
        try {
            onURLClick(Uri.parse(PermalinkUtils.createPermalink(groupId)))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onRoomIdClick failed " + e.localizedMessage, e)
        }

    }

    override fun onInvalidIndexes() {
        mInvalidIndexesCount++

        // it should happen once
        // else we assume that the adapter is really corrupted
        // It seems better to close the linked activity to avoid infinite refresh.
        if (1 == mInvalidIndexesCount) {
            mMessageListView.post(object : Runnable {
                override fun run() {
                    mAdapter.notifyDataSetChanged()
                }
            })
        } else {
            mMessageListView.post(object : Runnable {
                override fun run() {
                    if (null != activity) {
                        activity!!.finish()
                    }
                }
            })
        }
    }

    override fun shouldHighlightEvent(event: Event?): Boolean {
        // sanity check
        if ((null == event) || (null == event.eventId)) {
            return false
        }

        val eventId = event.eventId
        val status = mHighlightStatusByEventId[eventId]

        if (null != status) {
            return status
        }

        val res = (null != mSession.dataHandler.bingRulesManager.fulfilledHighlightBingRule(event))
        mHighlightStatusByEventId[eventId] = res

        return res
    }

    companion object {
        private val LOG_TAG = MessageListFragment::class.java.simpleName

        private val TAG_FRAGMENT_RECEIPTS_DIALOG = "TAG_FRAGMENT_RECEIPTS_DIALOG"
        private val TAG_FRAGMENT_USER_GROUPS_DIALOG = "TAG_FRAGMENT_USER_GROUPS_DIALOG"

        // onMediaAction actions
        // private static final int ACTION_VECTOR_SHARE = R.id.ic_action_vector_share;
        private val ACTION_VECTOR_FORWARD = R.id.ic_action_vector_forward
        private val ACTION_VECTOR_SAVE = R.id.ic_action_vector_save
        internal val ACTION_VECTOR_OPEN = 123456

        fun newInstance(matrixId: String?, roomId: String?, eventId: String?, previewMode: String?, layoutResId: Int): MessageListFragment {
            val f = MessageListFragment()
            val args = getArguments(matrixId, roomId, layoutResId)

            args.putString(ARG_EVENT_ID, eventId)
            args.putString(ARG_PREVIEW_MODE_ID, previewMode)

            f.arguments = args
            return f
        }
    }
}
