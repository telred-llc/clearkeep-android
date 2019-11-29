package vmodev.clearkeep.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Pair
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnLongClick
import butterknife.OnTouch
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.*
import im.vector.activity.util.STICKER_PICKER_ACTIVITY_REQUEST_CODE
import im.vector.dialogs.DialogCallAdapter
import im.vector.dialogs.DialogListItem
import im.vector.dialogs.DialogSendItemAdapter
import im.vector.features.hhs.ResourceLimitEventListener
import im.vector.fragments.VectorMessageListFragment
import im.vector.fragments.VectorReadReceiptsDialogFragment
import im.vector.fragments.VectorUnknownDevicesFragment
import im.vector.listeners.IMessagesAdapterActionsListener
import im.vector.services.EventStreamService
import im.vector.ui.themes.ThemeUtils
import im.vector.util.*
import im.vector.util.ReadMarkerManager.LIVE_MODE
import im.vector.util.ReadMarkerManager.PREVIEW_MODE
import im.vector.view.*
import im.vector.widgets.Widget
import im.vector.widgets.WidgetManagerProvider
import im.vector.widgets.WidgetsManager
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.PermalinkUtils
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener
import org.matrix.androidsdk.core.listeners.ProgressListener
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.data.RoomPreviewData
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.db.MXLatestChatMessageCache
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.rest.model.message.Message
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.MessageListFragment
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.ultis.ReadMarkerManager
import vmodev.clearkeep.ultis.RoomMediasSender
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomActivityViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class RoomActivity : MXCActionBarActivity(), MatrixMessageListFragment.IRoomPreviewDataListener,
        MatrixMessageListFragment.IEventSendingListener,
        MatrixMessageListFragment.IOnScrollListener,
        MessageListFragment.VectorMessageListFragmentListener,
        VectorReadReceiptsDialogFragment.VectorReadReceiptsDialogFragmentListener, IActivity {

    override fun getActivity(): FragmentActivity {
        return this
    }

    companion object {
        // the session
        val EXTRA_MATRIX_ID = MXCActionBarActivity.EXTRA_MATRIX_ID
        // the room id (string)
        val EXTRA_ROOM_ID = "EXTRA_ROOM_ID"
        // the event id (universal link management - string)
        val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
        // whether the preview is to display unread messages
        val EXTRA_IS_UNREAD_PREVIEW_MODE = "EXTRA_IS_UNREAD_PREVIEW_MODE"
        // the forwarded data (list of media uris)
        val EXTRA_ROOM_INTENT = "EXTRA_ROOM_INTENT"
        // the room is opened in preview mode (string)
        val EXTRA_ROOM_PREVIEW_ID = "EXTRA_ROOM_PREVIEW_ID"
        // the room alias of the room in preview mode (string)
        val EXTRA_ROOM_PREVIEW_ROOM_ALIAS = "EXTRA_ROOM_PREVIEW_ROOM_ALIAS"
        // expand the room header when the activity is launched (boolean)
        val EXTRA_EXPAND_ROOM_HEADER = "EXTRA_EXPAND_ROOM_HEADER"

        // display the room information while joining a room.
        // until the join is done.
        val EXTRA_DEFAULT_NAME = "EXTRA_DEFAULT_NAME"
        val EXTRA_DEFAULT_TOPIC = "EXTRA_DEFAULT_TOPIC"

        private val SHOW_ACTION_BAR_HEADER = true
        private val HIDE_ACTION_BAR_HEADER = false

        // the room is launched but it expects to start the dedicated call activity
        val EXTRA_START_CALL_ID = "EXTRA_START_CALL_ID"

        const val WAITING_INFORMATION_ACTIVITY = 13275

        const val RESULT_ROOM_ID = "RESULT_ROOM_ID";
    }

    private val TAG_FRAGMENT_MATRIX_MESSAGE_LIST = "TAG_FRAGMENT_MATRIX_MESSAGE_LIST"

    private val LOG_TAG = VectorRoomActivity::class.java!!.simpleName
    private val TYPING_TIMEOUT_MS = 10000

    private val FIRST_VISIBLE_ROW = "FIRST_VISIBLE_ROW"

    // activity result request code
    private val REQUEST_FILES_REQUEST_CODE = 0
    private val TAKE_IMAGE_REQUEST_CODE = 1
    val GET_MENTION_REQUEST_CODE = 2
    private val INVITE_USER_REQUEST_CODE = 4
    val UNREAD_PREVIEW_REQUEST_CODE = 5
    private val RECORD_AUDIO_REQUEST_CODE = 6

    // media selection
    private val MEDIA_SOURCE_FILE = 1
    private val MEDIA_SOURCE_VOICE = 2
    private val MEDIA_SOURCE_STICKER = 3
    private val MEDIA_SOURCE_PHOTO = 4
    private val MEDIA_SOURCE_VIDEO = 5

    private val CAMERA_VALUE_TITLE = "attachment" // Samsung devices need a filepath to write to or else won't return a Uri (!!!)
    private var mLatestTakePictureCameraUri: String? = null // has to be String not Uri because of Serializable

    val CONFIRM_MEDIA_REQUEST_CODE = 7

    private var mVectorMessageListFragment: MessageListFragment? = null
    private var mxSession: MXSession? = null

    private var currentRoom: Room? = null

    private var mMyUserId: String? = null
    // the parameter is too big to be sent by the intent
    // so use a static variable to send it
    // FIXME Remove this static variable. The VectorRoomActivity should load the RoomPreviewData itself
    var sRoomPreviewData: RoomPreviewData? = null
    private var mEventId: String? = null
    private var mDefaultRoomName: String? = null
    private var mDefaultTopic: String? = null
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomActivityViewModel>;

    private var mLatestChatMessageCache: MXLatestChatMessageCache? = null

    private var isEditedMode = false;
    private var currentEvent: Event? = null;
    private var currentMessageEdit: String? = null;

    @BindView(R.id.room_sending_message_layout)
    lateinit var mSendingMessagesLayout: View

    @BindView(R.id.imgFile)
    lateinit var mSendImageView: ImageView

    @BindView(R.id.editText_messageBox)
    lateinit var mEditText: VectorAutoCompleteTextView

    @BindView(R.id.room_self_avatar)
    lateinit var mAvatarImageView: ImageView

    @BindView(R.id.bottom_separator)
    lateinit var mBottomSeparator: View

    @BindView(R.id.room_cannot_post_textview)
    lateinit var mCanNotPostTextView: View

    @BindView(R.id.room_bottom_layout)
    lateinit var mBottomLayout: View

    @BindView(R.id.room_encrypted_image_view)
    lateinit var mE2eImageView: ImageView

//    // call
//    @BindView(R.id.room_start_call_image_view)
//    lateinit var mStartCallLayout: View

    @BindView(R.id.room_end_call_image_view)
    lateinit var mStopCallLayout: View

    // action bar header
    @BindView(R.id.room_action_bar_title)
    lateinit var mActionBarCustomTitle: TextView

    @BindView(R.id.room_action_bar_topic)
    lateinit var mActionBarCustomTopic: TextView

//    @BindView(R.id.open_chat_header_arrow)
//    lateinit var mActionBarCustomArrowImageView: ImageView

    // The room header view is displayed by clicking on the title of the action bar
//    @BindView(R.id.action_bar_header)
//    lateinit var mRoomHeaderView: ViewGroup

//    @BindView(R.id.action_bar_header_room_title)
//    lateinit var mActionBarHeaderRoomName: TextView

//    @BindView(R.id.action_bar_header_room_members_layout)
//    lateinit var mActionBarHeaderActiveMembersLayout: View

//    @BindView(R.id.action_bar_header_room_members_text_view)
//    lateinit var mActionBarHeaderActiveMembersTextView: TextView
//
//    @BindView(R.id.action_bar_header_room_members_invite_view)
//    lateinit var mActionBarHeaderActiveMembersInviteButton: View
//
//    @BindView(R.id.action_bar_header_room_members_settings_view)
//    lateinit var mActionBarHeaderActiveMembersListButton: View
//
//    @BindView(R.id.action_bar_header_room_topic)
//    lateinit var mActionBarHeaderRoomTopic: TextView
//
//    @BindView(R.id.room_header_avatar)
//    lateinit var mActionBarHeaderRoomAvatar: ImageView

    // notifications area
    @BindView(R.id.room_notifications_area)
    lateinit var mNotificationsArea: NotificationAreaView

    private var mLatestTypingMessage: String? = null
    private var mIsScrolledToTheBottom: Boolean? = null
    private var mLatestDisplayedEvent: Event? = null // the event at the bottom of the list

    private var mReadMarkerManager: ReadMarkerManager? = null

    // room preview
    @BindView(R.id.room_preview_info_layout)
    lateinit var mRoomPreviewLayout: View

    // medias sending helper
    var mVectorRoomMediasSender: RoomMediasSender? = null

    // pending call
    @BindView(R.id.room_pending_call_view)
    lateinit var mVectorPendingCallView: VectorPendingCallView

    // outgoing call
    @BindView(R.id.room_ongoing_conference_call_view)
    lateinit var mVectorOngoingConferenceCallView: VectorOngoingConferenceCallView

    // pending active view
    @BindView(R.id.room_pending_widgets_view)
    lateinit var mActiveWidgetsBanner: ActiveWidgetsBanner

    // spinners
    @BindView(R.id.loading_room_paginate_back_progress)
    lateinit var mBackProgressView: View
    @BindView(R.id.loading_room_paginate_forward_progress)
    lateinit var mForwardProgressView: View
    @BindView(R.id.main_progress_layout)
    lateinit var mMainProgressView: View

    @BindView(R.id.room_preview_invitation_textview)
    lateinit var invitationTextView: TextView
    @BindView(R.id.room_preview_subinvitation_textview)
    lateinit var subInvitationTextView: TextView
    @BindView(R.id.button_send)
    lateinit var buttonSend: Button;
    @BindView(R.id.image_view_video_call)
    lateinit var imageViewVideoCall: ImageView;
    @BindView(R.id.image_view_voice_call)
    lateinit var imageViewVoiceCall: ImageView;
    @BindView(R.id.image_view_cancel_edit)
    lateinit var imageViewCancelEdit: ImageView;

    @Inject
    lateinit var messageRepository: MessageRepository;

    // network events
    private val mNetworkEventListener = IMXNetworkEventListener {
        runOnUiThread {
            refreshNotificationsArea()
            refreshCallButtons(true)
        }
    }

    private var mCallId: String? = null

    // typing event management
    private var mTypingTimer: Timer? = null
    private var mTypingTimerTask: TimerTask? = null
    private var mLastTypingDate: Long = 0

    // scroll to a dedicated index
    private var mScrollToIndex = -1

    private var mIgnoreTextUpdate = false

    // https://github.com/vector-im/vector-android/issues/323
    // on some devices, the toolbar background is set to transparent
    // when an activity is opened from this one.
    // It should not but it does.
    private var mIsHeaderViewDisplayed = false

    // True if we are in preview mode to display unread message
    private var mIsUnreadPreviewMode: Boolean = false

    // True when this room has unsent event(s)
    private var mHasUnsentEvents: Boolean = false

    // progress bar to warn that the sync is not yet done
    @BindView(R.id.room_sync_in_progress)
    lateinit var mSyncInProgressView: View

    private val mDirectMessageListener = object : SimpleApiCallback<Void>(this) {
        override fun onMatrixError(e: MatrixError) {
            if (MatrixError.FORBIDDEN == e.errcode) {
                Toast.makeText(this@RoomActivity, e.error, Toast.LENGTH_LONG).show()
            }
        }

        override fun onSuccess(info: Void) {}

        override fun onNetworkError(e: Exception) {
            Toast.makeText(this@RoomActivity, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onUnexpectedError(e: Exception) {
            Toast.makeText(this@RoomActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private var mResourceLimitEventListener: ResourceLimitEventListener? = null

    /**
     * Presence and room preview listeners
     */
    private val mGlobalEventListener = object : MXEventListener() {

        override fun onSyncError(matrixError: MatrixError?) {
            mSyncInProgressView!!.visibility = View.GONE

            checkSendEventStatus()
            refreshNotificationsArea()
        }

        override fun onPresenceUpdate(event: Event?, user: User?) {
            // the header displays active members
            updateRoomHeaderMembersStatus()
        }

        override fun onLeaveRoom(roomId: String?) {
            // test if the user reject the invitation
            if ((null != sRoomPreviewData) && TextUtils.equals(sRoomPreviewData!!.roomId, roomId)) {
                Log.d(LOG_TAG, "The room invitation has been declined from another client")
                onDeclined()
            }
        }

        override fun onJoinRoom(roomId: String?) {
            // test if the user accepts the invitation
            if ((null != sRoomPreviewData) && TextUtils.equals(sRoomPreviewData!!.roomId, roomId)) {
                runOnUiThread {
                    Log.d(LOG_TAG, "The room invitation has been accepted from another client")
                    onJoined()
                }
            }
        }

        override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
            mSyncInProgressView!!.visibility = View.GONE

            checkSendEventStatus()
            refreshNotificationsArea()
        }
    }

    /**
     * The room events listener
     */
    private val mRoomEventListener = object : MXEventListener() {
        override fun onRoomFlush(roomId: String?) {
            runOnUiThread {
                updateActionBarTitleAndTopic()
                updateRoomHeaderMembersStatus()
                updateRoomHeaderAvatar()
            }
        }

        override fun onLeaveRoom(roomId: String?) {
            runOnUiThread { finish() }
        }

        override fun onRoomKick(roomId: String?) {
            val params = HashMap<String, Any>()

            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession!!.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = currentRoom!!.roomId

            // clear the activity stack to home activity
            val intent = Intent(this@RoomActivity, VectorHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, params as HashMap<*, *>)
            startActivity(intent)
        }

        override fun onLiveEvent(event: Event?, roomState: RoomState?) {
            runOnUiThread {
                val eventType = event!!.getType()
                Log.d(LOG_TAG, "Received event type: $eventType")

                when (eventType) {
                    Event.EVENT_TYPE_STATE_ROOM_NAME, Event.EVENT_TYPE_STATE_ROOM_ALIASES, Event.EVENT_TYPE_STATE_ROOM_MEMBER -> {
                        setTitle()
                        updateRoomHeaderMembersStatus()
                        updateRoomHeaderAvatar()
                    }
                    Event.EVENT_TYPE_STATE_ROOM_TOPIC -> {
                        val stateEvent = JsonUtils.toStateEvent(event!!.content)
                        setTopic(stateEvent.topic)
                    }
                    Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS -> checkSendEventStatus()
                    Event.EVENT_TYPE_TYPING -> onRoomTyping()
                    Event.EVENT_TYPE_STATE_ROOM_AVATAR -> updateRoomHeaderAvatar()
                    Event.EVENT_TYPE_MESSAGE_ENCRYPTION -> {
                        val canSendEncryptedEvent = currentRoom!!.isEncrypted() && mxSession!!.isCryptoEnabled()
                        mE2eImageView!!.setImageResource(if (canSendEncryptedEvent) R.drawable.e2e_verified else R.drawable.e2e_unencrypted)
                        mVectorMessageListFragment!!.setIsRoomEncrypted(currentRoom!!.isEncrypted())
                    }
                    Event.EVENT_TYPE_STATE_ROOM_TOMBSTONE -> checkSendEventStatus()
                    else -> Log.d(LOG_TAG, "Ignored event type: $eventType")
                }
                if (!VectorApp.isAppInBackground()) {
                    // do not send read receipt for the typing events
                    // they are ephemeral ones.
                    if (Event.EVENT_TYPE_TYPING != eventType) {
                        if (null != currentRoom) {
                            refreshNotificationsArea()
                        }
                    }
                }
            }
        }

        override fun onBingRulesUpdate() {
            runOnUiThread {
                updateActionBarTitleAndTopic()
                mVectorMessageListFragment!!.onBingRulesUpdate()
            }
        }

        override fun onEventSentStateUpdated(event: Event?) {
            runOnUiThread { refreshNotificationsArea() }
        }

        override fun onEventSent(event: Event?, prevEventId: String?) {
            runOnUiThread { refreshNotificationsArea() }
        }

        override fun onReceiptEvent(roomId: String?, senderIds: List<String>?) {
            runOnUiThread { refreshNotificationsArea() }
        }

        override fun onReadMarkerEvent(roomId: String?) {
            if (mReadMarkerManager != null) {
                roomId?.let { mReadMarkerManager!!.onReadMarkerChanged(it) }
            }
        }
    }

    private val mCallListener = object : MXCallListener() {
        override fun onCallError(error: String?) {
            refreshCallButtons(true)
        }

        override fun onCallAnsweredElsewhere() {
            refreshCallButtons(true)
        }

        override fun onCallEnd(aReasonId: Int) {
            refreshCallButtons(true)
        }

        override fun onPreviewSizeChanged(width: Int, height: Int) {}
    }

    //================================================================================
    // Activity classes
    //================================================================================

    private lateinit var application: IApplication;

    override fun getLayoutRes(): Int {
        val edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString("SETTINGS_SHOW_INFO_AREA_KEY", "messages_and_errors");
        edit.commit();
        application = applicationContext as IApplication;
        setTheme(application.getCurrentTheme());
        return R.layout.activity_room
    }

    override fun initUiAndData() {
        waitingView = findViewById(R.id.main_progress_layout)

        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "onCreate : Restart the application.")
            CommonActivityUtils.restartApp(this)
            return
        }

        val intent = intent
        if (!intent.hasExtra(EXTRA_ROOM_ID)) {
            Log.e(LOG_TAG, "No room ID extra.")
            finish()
            return
        }

        mxSession = getSession(intent)
        val mRoomId = intent.getStringExtra(EXTRA_ROOM_ID)
        mRoom = mxSession?.dataHandler?.store?.getRoom(mRoomId)

        if ((mxSession == null) || !mxSession!!.isAlive()) {
            Log.e(LOG_TAG, "No MXSession.")
            finish()
            return
        }
        mResourceLimitEventListener = ResourceLimitEventListener(mxSession!!.dataHandler, object : ResourceLimitEventListener.Callback {
            override fun onResourceLimitStateChanged() {
                refreshNotificationsArea()
            }
        })

        val roomId = intent.getStringExtra(EXTRA_ROOM_ID)
        // ensure that the preview mode is really expected
        if (!intent.hasExtra(EXTRA_ROOM_PREVIEW_ID)) {
            sRoomPreviewData = null
            Matrix.getInstance(this)!!.clearTmpStoresList()
        }

        if (CommonActivityUtils.isGoingToSplash(this, mxSession!!.myUserId, roomId)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen")
            return
        }

        //setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        // hide the header room as soon as the bottom layout (text edit zone) is touched
        mBottomLayout!!.setOnTouchListener { view, motionEvent ->
            enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
            false
        }

        mNotificationsArea!!.delegate = object : NotificationAreaView.Delegate {
            override fun providesMessagesActionListener(): IMessagesAdapterActionsListener {
                return mVectorMessageListFragment as IMessagesAdapterActionsListener
            }

            override fun resendUnsentEvents() {
                mVectorMessageListFragment!!.resendUnsentMessages()
            }

            override fun deleteUnsentEvents() {
                mVectorMessageListFragment!!.deleteUnsentEvents()
            }

            override fun closeScreen() {
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun jumpToBottom() {
                if (mReadMarkerManager != null) {
                    mReadMarkerManager!!.handleJumpToBottom()
                } else {
                    mVectorMessageListFragment!!.scrollToBottom(0)
                }
            }
        }

        // use a toolbar instead of the actionbar
        // to be able to display an expandable header
        configureToolbar()
//        toolbar.setBackgroundResource(android.R.color.white)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_green)
        toolbar.setNavigationOnClickListener {
//            viewModelFactory.getViewModel().setIdForUpdateRoomNotifyCount(currentRoom!!.roomId).subscribeOn(Schedulers.io()).subscribe();
            finishResult();
            finish()
        }
        mCallId = intent.getStringExtra(EXTRA_START_CALL_ID)
        mEventId = intent.getStringExtra(EXTRA_EVENT_ID)
        mDefaultRoomName = intent.getStringExtra(EXTRA_DEFAULT_NAME)
        mDefaultTopic = intent.getStringExtra(EXTRA_DEFAULT_TOPIC)
        mIsUnreadPreviewMode = intent.getBooleanExtra(EXTRA_IS_UNREAD_PREVIEW_MODE, false)

        if (mIsUnreadPreviewMode) {
            Log.d(LOG_TAG, "Displaying $roomId in unread preview mode")
        } else if (!TextUtils.isEmpty(mEventId) || (null != sRoomPreviewData)) {
            Log.d(LOG_TAG, "Displaying $roomId in preview mode")
        } else {
            Log.d(LOG_TAG, "Displaying $roomId")
        }

        if (PreferencesManager.sendMessageWithEnter(this)) {
            // imeOptions="actionSend" only works with single line, so we remove multiline inputType
            mEditText!!.inputType = mEditText!!.inputType and EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE.inv()
            mEditText!!.imeOptions = EditorInfo.IME_ACTION_SEND
        }

        // IME's DONE and SEND button is treated as a send action
        mEditText!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            val imeActionId = actionId and EditorInfo.IME_MASK_ACTION

            if (EditorInfo.IME_ACTION_DONE == imeActionId || EditorInfo.IME_ACTION_SEND == imeActionId) {
                sendTextMessage(mEditText.text.toString())
                return@OnEditorActionListener true
            }

            if (((null != keyEvent) && !keyEvent!!.isShiftPressed && keyEvent!!.keyCode == KeyEvent.KEYCODE_ENTER
                            && resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS)) {
                sendTextMessage(mEditText.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        currentRoom = mxSession!!.dataHandler.getRoom(roomId, false)
        if(!currentRoom?.roomId.isNullOrEmpty()){
            viewModelFactory.getViewModel().setIdForUpdateRoomNotifyCount(currentRoom!!.roomId).subscribeOn(Schedulers.io()).subscribe();
        }
        mEditText!!.setAddColonOnFirstItem(true)
        mEditText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable) {
                if (null != currentRoom) {
                    val latestChatMessageCache = mLatestChatMessageCache
                    val textInPlace = latestChatMessageCache!!.getLatestText(this@RoomActivity, currentRoom!!.roomId)

                    // check if there is really an update
                    // avoid useless updates (initializations..)
                    if (!mIgnoreTextUpdate && textInPlace != mEditText!!.text.toString()) {
                        latestChatMessageCache!!.updateLatestMessage(this@RoomActivity, currentRoom!!.roomId, mEditText!!.text.toString())
                        handleTypingNotification(mEditText!!.text.length != 0)
                    }

                    manageSendMoreButtons()
//                    refreshCallButtons(true)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Auto completion mode management
                // The auto completion mode depends on the first character of the message
                mEditText!!.updateAutoCompletionMode(false)
            }
        })

        mMyUserId = mxSession!!.getCredentials().userId

        val fm = supportFragmentManager
        mVectorMessageListFragment = fm.findFragmentByTag(TAG_FRAGMENT_MATRIX_MESSAGE_LIST) as MessageListFragment?
        if (mVectorMessageListFragment == null) {
            Log.d(LOG_TAG, "Create VectorMessageListFragment")

            // this fragment displays messages and handles all message logic
            val previewMode = if ((null == sRoomPreviewData))
                (if (mIsUnreadPreviewMode)
                    VectorMessageListFragment.PREVIEW_MODE_UNREAD_MESSAGE
                else
                    null)
            else
                VectorMessageListFragment.PREVIEW_MODE_READ_ONLY
            mVectorMessageListFragment = MessageListFragment.newInstance(mMyUserId, roomId, mEventId,
                    previewMode,
                    org.matrix.androidsdk.R.layout.fragment_matrix_message_list_fragment)
            fm.beginTransaction().add(R.id.anchor_fragment_messages, mVectorMessageListFragment!!, TAG_FRAGMENT_MATRIX_MESSAGE_LIST).commit()
        } else {
            Log.d(LOG_TAG, "Reuse VectorMessageListFragment")
        }

        mVectorMessageListFragment!!.setListener(this)

        mVectorRoomMediasSender = RoomMediasSender(this, mVectorMessageListFragment!!, Matrix.getInstance(this)!!.mediaCache)

        manageRoomPreview()

        if (currentRoom != null) {
            // Ensure menu and UI is up to date (ignore any error)
            currentRoom!!.getMembersAsync(object : SimpleApiCallback<List<RoomMember>>() {
                override fun onSuccess(info: List<RoomMember>) {
                    refreshNotificationsArea()

                    checkIfUserHasBeenKicked()
                }
            })
        }

        checkIfUserHasBeenKicked()

        mLatestChatMessageCache = Matrix.getInstance(this)!!.defaultLatestChatMessageCache

        // some medias must be sent while opening the chat
        if (intent.hasExtra(EXTRA_ROOM_INTENT)) {
            // fix issue #1276
            // if there is a saved instance, it means that onSaveInstanceState has been called.
            // theses parameters must only be used at activity creation.
            // The activity might have been created after being killed by android while the application is in background
            if (isFirstCreation()) {
                val mediaIntent = intent.getParcelableExtra<Intent>(EXTRA_ROOM_INTENT)

                // sanity check
                if (null != mediaIntent) {
                    mEditText!!.postDelayed({
                        intent.removeExtra(EXTRA_ROOM_INTENT)
                        sendMediasIntent(mediaIntent)
                    }, 1000)
                }
            } else {
                intent.removeExtra(EXTRA_ROOM_INTENT)
                Log.e(LOG_TAG, "## onCreate() : ignore EXTRA_ROOM_INTENT because savedInstanceState != null")
            }
        }

        mActiveWidgetsBanner!!.initRoomInfo(mxSession, currentRoom)

        mActiveWidgetsBanner!!.setOnUpdateListener(object : ActiveWidgetsBanner.onUpdateListener {
            override fun onCloseWidgetClick(widget: Widget) {

                AlertDialog.Builder(this@RoomActivity)
                        .setMessage(R.string.widget_delete_message_confirmation)
                        .setPositiveButton(R.string.remove) { dialog, which ->
                            showWaitingView()

                            val wm = WidgetManagerProvider.getWidgetManager(this@RoomActivity)
                            if (wm != null) {
                                showWaitingView()

                                wm.closeWidget(mSession, mRoom, widget.widgetId, object : ApiCallback<Void> {
                                    override fun onSuccess(info: Void) {
                                        hideWaitingView()
                                    }

                                    private fun onError(errorMessage: String?) {
                                        hideWaitingView()
                                        Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
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
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            }

            override fun onActiveWidgetsListUpdate() {
                // something todo ?
            }

            private fun displayWidget(widget: Widget) {
                val intent = WidgetActivity.getIntent(this@RoomActivity, widget)

                startActivity(intent)
            }

            override fun onClick(widgets: List<Widget>) {
                if (widgets.size == 1) {
                    displayWidget(widgets[0])
                } else if (widgets.size > 1) {
                    val widgetNames = ArrayList<CharSequence>()
                    val CharSequences = arrayOfNulls<CharSequence>(widgetNames.size)

                    for (widget in widgets) {
                        widgetNames.add(widget.humanName)
                    }

                    AlertDialog.Builder(this@RoomActivity)
                            .setSingleChoiceItems(widgetNames.toTypedArray(), 0) { d, n ->
                                d.cancel()
                                displayWidget(widgets[n])
                            }
                            .setNegativeButton(R.string.cancel, null)
                            .show()
                }
            }
        })

        mVectorOngoingConferenceCallView!!.initRoomInfo(mxSession, currentRoom)
        mVectorOngoingConferenceCallView!!.setCallClickListener(object : VectorOngoingConferenceCallView.ICallClickListener {
            private fun startCall(isVideo: Boolean) {
                if (checkPermissions(if (isVideo)
                            PERMISSIONS_FOR_VIDEO_IP_CALL
                        else
                            PERMISSIONS_FOR_AUDIO_IP_CALL,
                                this@RoomActivity,
                                if (isVideo) PERMISSION_REQUEST_CODE_VIDEO_CALL else PERMISSION_REQUEST_CODE_AUDIO_CALL)) {
                    startIpCall(false, isVideo)
                }
            }

            private fun onCallClick(widget: Widget?, isVideo: Boolean) {
                if (null != widget) {
                    launchJitsiActivity(widget, isVideo)
                } else {
                    startCall(isVideo)
                }
            }

            override fun onVoiceCallClick(widget: Widget) {
                onCallClick(widget, false)
            }

            override fun onVideoCallClick(widget: Widget) {
                onCallClick(widget, true)
            }

            override fun onCloseWidgetClick(widget: Widget) {
                val wm = WidgetManagerProvider.getWidgetManager(this@RoomActivity)
                if (wm != null) {
                    showWaitingView()

                    wm.closeWidget(mSession, mRoom, widget.widgetId, object : ApiCallback<Void> {
                        override fun onSuccess(info: Void) {
                            hideWaitingView()
                        }

                        private fun onError(errorMessage: String?) {
                            hideWaitingView()
                            Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
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

            override fun onActiveWidgetUpdate() {
                refreshCallButtons(false)
            }
        })

        refreshSelfAvatar()

        // in case a "Send as" dialog was in progress when the activity was destroyed (life cycle)
        mVectorRoomMediasSender!!.resumeResizeMediaAndSend()

        // header visibility has launched
        enableActionBarHeader(if (intent.getBooleanExtra(EXTRA_EXPAND_ROOM_HEADER, false)) SHOW_ACTION_BAR_HEADER else HIDE_ACTION_BAR_HEADER)

        // the both flags are only used once
        intent.removeExtra(EXTRA_EXPAND_ROOM_HEADER)

        // Init read marker manager
        if ((mIsUnreadPreviewMode || (currentRoom != null && currentRoom!!.getTimeline() != null && currentRoom!!.getTimeline().isLiveTimeline() && TextUtils.isEmpty(mEventId)))) {
            if (null == currentRoom) {
                Log.e(LOG_TAG, "## onCreate() : null room")
            } else if (null == mxSession!!.dataHandler.store!!.getSummary(currentRoom!!.roomId)) {
                Log.e(LOG_TAG, "## onCreate() : there is no summary for this room")
            } else {
                mReadMarkerManager = ReadMarkerManager(this, mVectorMessageListFragment!!, mxSession!!, currentRoom,
                        if (mIsUnreadPreviewMode) PREVIEW_MODE else LIVE_MODE,
                        findViewById(R.id.jump_to_first_unread))
            }
        }

        Log.d(LOG_TAG, "End of create")
        mxSession?.crypto?.let {
            it.keysBackup.maybeBackupKeys();
            it.keysBackup.backupAllGroupSessions(object : ProgressListener{
                override fun onProgress(progress: Int, total: Int) {
                    android.util.Log.d("KeysBackup", progress.toString());
                }
            }, object : ApiCallback<Void?>{
                override fun onSuccess(info: Void?) {
                    android.util.Log.d("KeysBackup", "Success");
                }

                override fun onUnexpectedError(e: java.lang.Exception?) {
                    android.util.Log.d("KeysBackup", e?.message);
                }

                override fun onNetworkError(e: java.lang.Exception?) {
                    android.util.Log.d("KeysBackup", e?.message);
                }

                override fun onMatrixError(e: MatrixError?) {
                    android.util.Log.d("KeysBackup", e?.message);
                }
            })
        }
    }

    private fun checkIfUserHasBeenKicked() {
        val member = if ((null != currentRoom)) currentRoom!!.getMember(mMyUserId) else null
        val hasBeenKicked = (null != member) && member!!.kickedOrBanned()

        // in timeline mode (i.e search in the forward and backward room history)
        // or in room preview mode
        // the edition items are not displayed
        if ((!TextUtils.isEmpty(mEventId) || (null != sRoomPreviewData)) || hasBeenKicked) {
            if (!mIsUnreadPreviewMode || hasBeenKicked) {
                mNotificationsArea!!.visibility = View.GONE
                mBottomSeparator!!.visibility = View.GONE
                findViewById<View>(R.id.room_notification_separator).visibility = View.GONE
            }

            mBottomLayout!!.layoutParams.height = 0
        }

        if ((null == sRoomPreviewData) && hasBeenKicked) {
            manageBannedHeader(member!!)
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(FIRST_VISIBLE_ROW, mVectorMessageListFragment!!.mMessageListView.firstVisiblePosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // the listView will be refreshed so the offset might be lost.
        mScrollToIndex = savedInstanceState.getInt(FIRST_VISIBLE_ROW, -1)
    }

    public override fun onDestroy() {
        if (null != mVectorMessageListFragment) {
            mVectorMessageListFragment!!.onDestroy()
        }

        if (null != mVectorOngoingConferenceCallView) {
            mVectorOngoingConferenceCallView!!.setCallClickListener(null)
        }

        if (null != mActiveWidgetsBanner) {
            mActiveWidgetsBanner!!.setOnUpdateListener(null)
        }

        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()

        if (mReadMarkerManager != null) {
            mReadMarkerManager!!.onPause()
        }

        // warn other member that the typing is ended
        cancelTypingNotification()

        if (null != currentRoom) {
            // listen for room name or topic changes
            currentRoom!!.removeEventListener(mRoomEventListener)
        }

        Matrix.getInstance(this)!!.removeNetworkEventListener(mNetworkEventListener)

        if (mxSession!!.isAlive()) {
            // GA reports a null dataHandler instance event if it seems impossible
            if (null != mxSession!!.dataHandler) {
                mxSession!!.dataHandler.removeListener(mGlobalEventListener)
                mxSession!!.dataHandler.removeListener(mResourceLimitEventListener)
            }
        }

        mVectorOngoingConferenceCallView!!.onActivityPause()
        mActiveWidgetsBanner!!.onActivityPause()

        // to have notifications for this room
        VectorApp.getInstance().notificationDrawerManager.setCurrentRoom(null)
    }

    override fun onResume() {
        Log.d(LOG_TAG, "++ Resume the activity")
        super.onResume()

        if (null != currentRoom) {
            // check if the room has been left from another client.
            if (currentRoom!!.isReady()) {
                if (!currentRoom!!.isMember()) {
                    Log.e(LOG_TAG, "## onResume() : the user is not anymore a member of the room.")
                    finish()
                    return
                }

                if (!mxSession!!.dataHandler.doesRoomExist(currentRoom!!.roomId)) {
                    Log.e(LOG_TAG, "## onResume() : the user is not anymore a member of the room.")
                    finish()
                    return
                }

                if (currentRoom!!.isLeaving()) {
                    Log.e(LOG_TAG, "## onResume() : the user is leaving the room.")
                    finish()
                    return
                }
            }

            // to do not trigger notifications for this room
            // because it is displayed.
            VectorApp.getInstance().notificationDrawerManager.setCurrentRoom(currentRoom!!.roomId)

            // listen for room name or topic changes
            currentRoom!!.addEventListener(mRoomEventListener)

            setEditTextHint(mVectorMessageListFragment!!.currentSelectedEvent)

            mSyncInProgressView!!.visibility = if (VectorApp.isSessionSyncing(mxSession)) View.VISIBLE else View.GONE
        } else {
            mSyncInProgressView!!.visibility = View.GONE
        }

        mxSession!!.dataHandler.addListener(mGlobalEventListener)
        mxSession!!.dataHandler.addListener(mResourceLimitEventListener)

        Matrix.getInstance(this)!!.addNetworkEventListener(mNetworkEventListener)

        // sanity checks
        if ((null != currentRoom) && (null != Matrix.getInstance(this)!!.defaultLatestChatMessageCache)) {
            val cachedText = Matrix.getInstance(this)!!.defaultLatestChatMessageCache!!.getLatestText(this, currentRoom!!.roomId)

            if (cachedText != mEditText!!.text.toString()) {
                mIgnoreTextUpdate = true
                mEditText!!.setText("")
                mEditText!!.append(cachedText)
                mIgnoreTextUpdate = false
            }

            val canSendEncryptedEvent = currentRoom!!.isEncrypted() && mxSession!!.isCryptoEnabled()
            mE2eImageView!!.setImageResource(if (canSendEncryptedEvent) R.drawable.e2e_verified else R.drawable.e2e_unencrypted)
            mVectorMessageListFragment!!.setIsRoomEncrypted(currentRoom!!.isEncrypted())
        }

        manageSendMoreButtons()

        updateActionBarTitleAndTopic()

        sendReadReceipt()

        refreshCallButtons(true)

        updateRoomHeaderMembersStatus()

        checkSendEventStatus()

        enableActionBarHeader(mIsHeaderViewDisplayed)

        // refresh the UI : the timezone could have been updated
        mVectorMessageListFragment!!.refresh()

        // the list automatically scrolls down when its top moves down
        if (null != mVectorMessageListFragment!!.mMessageListView) {
            mVectorMessageListFragment!!.mMessageListView.lockSelectionOnResize()
        }

        // the device has been rotated
        // so try to keep the same top/left item;
        if (mScrollToIndex > 0) {
            mVectorMessageListFragment!!.scrollToIndexWhenLoaded(mScrollToIndex)
            mScrollToIndex = -1
        }

        if (null != mCallId) {
            val call = CallsManager.getSharedInstance().activeCall

            // can only manage one call instance.
            // either there is no active call or resume the active one
            if ((null == call) || call!!.callId == mCallId) {
                val intent = Intent(this, VectorCallViewActivity::class.java)
                intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, mxSession!!.getCredentials().userId)
                intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, mCallId)

                enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
                runOnUiThread { startActivity(intent) }
            }

            mCallId = null
        }

        // the pending call view is only displayed with "active " room
        if ((null == sRoomPreviewData) && (null == mEventId)) {
            mVectorPendingCallView!!.checkPendingCall()
            mVectorOngoingConferenceCallView!!.onActivityResume()
            mActiveWidgetsBanner!!.onActivityResume()
        }

        // init the auto-completion list from the room members
        mEditText!!.initAutoCompletions(mxSession!!, currentRoom)

        if (mReadMarkerManager != null) {
            mReadMarkerManager!!.onResume()
        }

        Log.d(LOG_TAG, "-- Resume the activity")
    }

    /**
     * Update the edit text hint. It depends on the encryption and on the currently selected event
     *
     * @param selectedEvent the currently selected event or null if no event is selected
     */
    private fun setEditTextHint(selectedEvent: Event?) {
        if (currentRoom == null) {
            return
        }

        if (currentRoom!!.canReplyTo(selectedEvent)) {
            // User can reply to this event
            mEditText!!.setHint(if ((currentRoom!!.isEncrypted() && mxSession!!.isCryptoEnabled()))
                R.string.room_message_placeholder_reply_to_encrypted
            else
                R.string.room_message_placeholder_reply_to_not_encrypted)
        } else {
            // default hint
            mEditText!!.setHint(if ((currentRoom!!.isEncrypted() && mxSession!!.isCryptoEnabled()))
                R.string.room_message_placeholder_encrypted
            else
                R.string.room_message_placeholder_not_encrypted)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FILES_REQUEST_CODE, TAKE_IMAGE_REQUEST_CODE, RECORD_AUDIO_REQUEST_CODE -> sendMediasIntent(data)
                STICKER_PICKER_ACTIVITY_REQUEST_CODE -> sendSticker(data)
                GET_MENTION_REQUEST_CODE -> insertUserDisplayNameInTextEditor(data!!.getStringExtra(VectorMemberDetailsActivity.RESULT_MENTION_ID))
                INVITE_USER_REQUEST_CODE -> onActivityResultRoomInvite(data!!)
                UNREAD_PREVIEW_REQUEST_CODE -> mVectorMessageListFragment!!.scrollToBottom(0)
                CONFIRM_MEDIA_REQUEST_CODE -> {
                    val sharedDataItems = ArrayList(RoomMediaMessage.listRoomMediaMessages(data))
                    if (0 == sharedDataItems.size) {
                        sharedDataItems.add(RoomMediaMessage(Uri.parse(data!!.getStringExtra(MediaPreviewerActivity.EXTRA_CAMERA_PICTURE_URI))))
                    }
                    mVectorRoomMediasSender!!.sendMedias(sharedDataItems)
                }
                WAITING_INFORMATION_ACTIVITY -> finish();
            }
        }
    }

    //================================================================================
    // IEventSendingListener
    //================================================================================

    override fun onMessageSendingSucceeded(event: Event) {
        refreshNotificationsArea()
    }

    override fun onMessageSendingFailed(event: Event) {
        refreshNotificationsArea()
    }

    override fun onMessageRedacted(event: Event) {
        refreshNotificationsArea()
    }

    override fun onUnknownDevices(event: Event, error: MXCryptoError) {
        refreshNotificationsArea()
//        CommonActivityUtils.displayUnknownDevicesDialog(mxSession,
//                this,
//                error.mExceptionData as MXUsersDevicesMap<MXDeviceInfo>,
//                false
//        ) {
//            mVectorMessageListFragment!!.resendUnsentMessages()
//            refreshNotificationsArea()
//        }
        val devicesInfo = error.mExceptionData as MXUsersDevicesMap<MXDeviceInfo>;
        devicesInfo?.let {
            val deviceList = getDevicesList(it);
            deviceList.forEach { t: Pair<String, List<MXDeviceInfo>>? ->
                t?.second?.forEach { d: MXDeviceInfo? ->
                    d?.let { mxDeviceInfo ->
                        if (mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNVERIFIED || mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNKNOWN) {
                            mxSession!!.crypto?.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, mxDeviceInfo.deviceId, mxDeviceInfo.userId, object : SimpleApiCallback<Void>() {
                                override fun onSuccess(p0: Void?) {
                                    android.util.Log.d("Verify device success", mxDeviceInfo.deviceId.toString())
                                }
                            })
                        }
                    }
                }
            }
        }
        mVectorMessageListFragment!!.resendUnsentMessages()
        refreshNotificationsArea()
    }

    /**
     * Convert a MXUsersDevicesMap to a list of List
     *
     * @return the list of list
     */
    private fun getDevicesList(devicesInfo: MXUsersDevicesMap<MXDeviceInfo>): List<Pair<String, List<MXDeviceInfo>>> {
        val res = ArrayList<Pair<String, List<MXDeviceInfo>>>()

        // sanity check
        if (null != devicesInfo) {
            val userIds = devicesInfo.getUserIds()

            for (userId in userIds) {
                val deviceInfos = ArrayList<MXDeviceInfo>()
                val deviceIds = devicesInfo.getUserDeviceIds(userId)

                for (deviceId in deviceIds) {
                    deviceInfos.add(devicesInfo.getObject(deviceId, userId))
                }
                res.add(Pair(userId, deviceInfos))
            }
        }

        return res
    }

    override fun onConsentNotGiven(event: Event, matrixError: MatrixError) {
        refreshNotificationsArea()
        consentNotGivenHelper.displayDialog(matrixError)
    }

    //================================================================================
    // IOnScrollListener
    //================================================================================

    /**
     * Send a read receipt to the latest displayed event.
     */
    private fun sendReadReceipt() {
        if ((null != currentRoom) && (null == sRoomPreviewData)) {
            val latestDisplayedEvent = mLatestDisplayedEvent

            // send the read receipt
            currentRoom!!.sendReadReceipt(latestDisplayedEvent, object : ApiCallback<Void> {
                override fun onSuccess(info: Void?) {
                    // reported by a rageshake that mLatestDisplayedEvent.evenId was null whereas it was tested before being used
                    // use a final copy of the event
                    try {
                        if (!isFinishing && (null != latestDisplayedEvent) && mVectorMessageListFragment!!.messageAdapter != null) {

                            mVectorMessageListFragment!!.messageAdapter.updateReadMarker(currentRoom!!.getReadMarkerEventId(), latestDisplayedEvent!!.eventId)
                            viewModelFactory.getViewModel().setIdForUpdateRoomNotifyCount(currentRoom!!.roomId).subscribeOn(Schedulers.io()).subscribe();

                        }
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "## sendReadReceipt() : failed " + e.message, e)
                    }

                }

                override fun onNetworkError(e: Exception) {
                    Log.e(LOG_TAG, "## sendReadReceipt() : failed " + e.message, e)
                }

                override fun onMatrixError(e: MatrixError) {
                    Log.e(LOG_TAG, "## sendReadReceipt() : failed " + e.message)
                }

                override fun onUnexpectedError(e: Exception) {
                    Log.e(LOG_TAG, "## sendReadReceipt() : failed " + e.message, e)
                }
            })
            refreshNotificationsArea()
        }
    }

    override fun onBackPressed() {
        finishResult();
        finish();
        super.onBackPressed()
    }

    private fun finishResult() {

        val intentResult = Intent();
        intentResult.putExtra(RESULT_ROOM_ID, currentRoom!!.roomId);
        setResult(Activity.RESULT_OK, intentResult);
    }

    override fun onScroll(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        val eventAtBottom = mVectorMessageListFragment!!.getEvent(firstVisibleItem + visibleItemCount - 1)
        val eventAtTop = mVectorMessageListFragment!!.getEvent(firstVisibleItem)

        if ((null != eventAtBottom) && ((null == mLatestDisplayedEvent) || !TextUtils.equals(eventAtBottom!!.eventId, mLatestDisplayedEvent!!.eventId))) {

            Log.d(LOG_TAG, ("## onScroll firstVisibleItem " + firstVisibleItem
                    + " visibleItemCount " + visibleItemCount
                    + " totalItemCount " + totalItemCount))
            mLatestDisplayedEvent = eventAtBottom

            // don't send receive if the app is in background
            if (!VectorApp.isAppInBackground()) {
                sendReadReceipt()
            } else {
                Log.d(LOG_TAG, "## onScroll : the app is in background")
            }
        }

        if (mReadMarkerManager != null) {
            mReadMarkerManager!!.onScroll(firstVisibleItem, visibleItemCount, totalItemCount, eventAtTop, eventAtBottom)
        }
    }

    override fun onScrollStateChanged(scrollState: Int) {
        if (mReadMarkerManager != null) {
            mReadMarkerManager!!.onScrollStateChanged(scrollState)
        }

        if (mNotificationsArea != null) {
            mNotificationsArea!!.scrollState = scrollState
        }
    }

    override fun onLatestEventDisplay(isDisplayed: Boolean) {
        // not yet initialized or a new value
        if ((null == mIsScrolledToTheBottom) || (isDisplayed != mIsScrolledToTheBottom)) {
            Log.d(LOG_TAG, "## onLatestEventDisplay : isDisplayed $isDisplayed")

            if (isDisplayed && (null != currentRoom)) {
                mLatestDisplayedEvent = currentRoom!!.dataHandler.store!!.getLatestEvent(currentRoom!!.roomId)
                // ensure that the latest message is displayed
                currentRoom!!.sendReadReceipt()
            }

            mIsScrolledToTheBottom = isDisplayed
            refreshNotificationsArea()
        }
    }

    //================================================================================
    // Menu management
    //================================================================================


//    override fun getMenuRes(): Int {
//        return R.menu.vector_room
//    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        val searchInRoomMenuItem = menu.findItem(R.id.ic_action_search_in_room)
//        val useMatrixAppsMenuItem = menu.findItem(R.id.ic_action_matrix_apps)
//        val resendUnsentMenuItem = menu.findItem(R.id.ic_action_room_resend_unsent)
//        val deleteUnsentMenuItem = menu.findItem(R.id.ic_action_room_delete_unsent)
//        val settingsMenuItem = menu.findItem(R.id.ic_action_room_settings)
//        val leaveRoomMenuItem = menu.findItem(R.id.ic_action_room_leave)
//
//        // the application is in a weird state
//        // GA : mxSession is null, currentRoom is null
//        // This is the case in the room preview for public rooms
//        if (CommonActivityUtils.shouldRestartApp(this) || null == mxSession || null == currentRoom) {
//            // Hide all items
//            if (searchInRoomMenuItem != null) {
//                searchInRoomMenuItem!!.isVisible = false
//            }
//            if (useMatrixAppsMenuItem != null) {
//                useMatrixAppsMenuItem!!.isVisible = false
//            }
//            if (resendUnsentMenuItem != null) {
//                resendUnsentMenuItem!!.isVisible = false
//            }
//            if (deleteUnsentMenuItem != null) {
//                deleteUnsentMenuItem!!.isVisible = false
//            }
//            if (settingsMenuItem != null) {
//                settingsMenuItem!!.isVisible = false
//            }
//            if (leaveRoomMenuItem != null) {
//                leaveRoomMenuItem!!.isVisible = false
//            }
//
//            return true
//        }
//
//        // the menu is only displayed when the current activity does not display a timeline search
//        if (TextUtils.isEmpty(mEventId) && (null == sRoomPreviewData)) {
//            val member = currentRoom!!.getMember(mxSession!!.myUserId)
//
//            // the server search does not work on encrypted rooms.
//            if (searchInRoomMenuItem != null) {
//                searchInRoomMenuItem!!.isVisible = !currentRoom!!.isEncrypted()
//            }
//            if (useMatrixAppsMenuItem != null) {
//                useMatrixAppsMenuItem!!.isVisible = TextUtils.isEmpty(mEventId) && null == sRoomPreviewData
//            }
//            if (resendUnsentMenuItem != null) {
//                resendUnsentMenuItem!!.isVisible = mHasUnsentEvents
//            }
//            if (deleteUnsentMenuItem != null) {
//                deleteUnsentMenuItem!!.isVisible = mHasUnsentEvents
//            }
//            if (settingsMenuItem != null) {
//                settingsMenuItem!!.isVisible = true
//            }
//            // kicked / banned room
//            if (leaveRoomMenuItem != null) {
//                leaveRoomMenuItem!!.isVisible = member != null && !member!!.kickedOrBanned()
//            }
//        } else {
//            // Hide all items
//            if (searchInRoomMenuItem != null) {
//                searchInRoomMenuItem!!.isVisible = false
//            }
//            if (useMatrixAppsMenuItem != null) {
//                useMatrixAppsMenuItem!!.isVisible = false
//            }
//            if (resendUnsentMenuItem != null) {
//                resendUnsentMenuItem!!.isVisible = false
//            }
//            if (deleteUnsentMenuItem != null) {
//                deleteUnsentMenuItem!!.isVisible = false
//            }
//            if (settingsMenuItem != null) {
//                settingsMenuItem!!.isVisible = false
//            }
//            if (leaveRoomMenuItem != null) {
//                leaveRoomMenuItem!!.isVisible = false
//            }
//        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.ic_action_matrix_apps -> {
//                openIntegrationManagerActivity(null)
//                return true
//            }
//            R.id.ic_action_search_in_room -> {
//                try {
//                    enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
//
//                    val searchIntent = Intent(this, VectorUnifiedSearchActivity::class.java)
//                    searchIntent.putExtra(VectorUnifiedSearchActivity.EXTRA_ROOM_ID, currentRoom!!.roomId)
//                    startActivity(searchIntent)
//                } catch (e: Exception) {
//                    Log.i(LOG_TAG, "## onOptionsItemSelected(): ")
//                }
//
//                return true
//            }
//            R.id.ic_action_room_settings -> {
//                launchRoomDetails(VectorRoomDetailsActivity.PEOPLE_TAB_INDEX)
//                return true
//            }
//            R.id.ic_action_room_resend_unsent -> {
//                mVectorMessageListFragment!!.resendUnsentMessages()
//                refreshNotificationsArea()
//                return true
//            }
//            R.id.ic_action_room_delete_unsent -> {
//                mVectorMessageListFragment!!.deleteUnsentEvents()
//                refreshNotificationsArea()
//                return true
//            }
//            R.id.ic_action_room_leave -> {
//                if (null != currentRoom) {
//                    Log.d(LOG_TAG, "Leave the room " + currentRoom!!.roomId)
//                    AlertDialog.Builder(this)
//                            .setTitle(R.string.room_participants_leave_prompt_title)
//                            .setMessage(R.string.room_participants_leave_prompt_msg)
//                            .setPositiveButton(R.string.leave, object : DialogInterface.OnClickListener {
//                                override fun onClick(dialog: DialogInterface, which: Int) {
//                                    showWaitingView()
//
//                                    currentRoom!!.leave(object : ApiCallback<Void> {
//                                        override fun onSuccess(info: Void) {
//                                            Log.d(LOG_TAG, "The room " + currentRoom!!.roomId + " is left")
//                                            // close the activity
//                                            finish()
//                                        }
//
//                                        private fun onError(errorMessage: String) {
//                                            hideWaitingView()
//                                            Log.e(LOG_TAG, "Cannot leave the room " + currentRoom!!.roomId + " : " + errorMessage)
//                                        }
//
//                                        override fun onNetworkError(e: Exception) {
//                                            onError(e.localizedMessage)
//                                        }
//
//                                        override fun onMatrixError(e: MatrixError) {
//                                            if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
//                                                hideWaitingView()
//                                                consentNotGivenHelper.displayDialog(e)
//                                            } else {
//                                                onError(e.localizedMessage)
//                                            }
//                                        }
//
//                                        override fun onUnexpectedError(e: Exception) {
//                                            onError(e.localizedMessage)
//                                        }
//                                    })
//                                }
//                            })
//                            .setNegativeButton(R.string.cancel, null)
//                            .show()
//                }
//                return true
//            }
//        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Open Integration Manager activity
     *
     * @param screenId to open a specific screen. Can be null
     */
    private fun openIntegrationManagerActivity(screenId: String?) {
        if (currentRoom == null) {
            return
        }

        val intent = IntegrationManagerActivity.getIntent(this, mMyUserId!!, currentRoom!!.roomId, null, screenId)
        startActivity(intent)
    }

    /**
     * Check if the current user is allowed to perform a conf call.
     * The user power level is checked against the invite power level.
     *
     * To start a conf call, the user needs to invite the CFU to the room.
     *
     * @return true if the user is allowed, false otherwise
     */
    private fun isUserAllowedToStartConfCall(): Boolean {
        var isAllowed = false

        if (currentRoom != null && currentRoom!!.isOngoingConferenceCall()) {
            // if a conf is in progress, the user can join the established conf anyway
            Log.d(LOG_TAG, "## isUserAllowedToStartConfCall(): conference in progress")
            isAllowed = true
        } else if ((null != currentRoom) && (currentRoom!!.getNumberOfMembers() > 2)) {
            val powerLevels = currentRoom!!.state.getPowerLevels()

            if (null != powerLevels) {
                // to start a conf call, the user MUST have the power to invite someone (CFU)
                isAllowed = powerLevels!!.getUserPowerLevel(mxSession!!.myUserId) >= powerLevels!!.invite
            }
        } else {
            // 1:1 call
            isAllowed = true
        }

        Log.d(LOG_TAG, "## isUserAllowedToStartConfCall(): isAllowed=$isAllowed")
        return isAllowed
    }

    /**
     * Display a dialog box to indicate that the conf call can no be performed.
     *
     * See [.isUserAllowedToStartConfCall]
     */
    private fun displayConfCallNotAllowed() {
        // display the dialog with the info text
        AlertDialog.Builder(this)
                .setTitle(R.string.missing_permissions_title_to_start_conf_call)
                .setMessage(R.string.missing_permissions_to_start_conf_call)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.ok, null)
                .show()
    }

    /**
     * Start an IP call with the management of the corresponding permissions.
     * According to the IP call, the corresponding permissions are asked: [im.vector.util.PermissionsToolsKt.PERMISSIONS_FOR_AUDIO_IP_CALL]
     * or [im.vector.util.PermissionsToolsKt.PERMISSIONS_FOR_VIDEO_IP_CALL].
     */
    private fun displayVideoCallIpDialog() {
        // hide the header room
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        AlertDialog.Builder(this)
                .setAdapter(DialogCallAdapter(this), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        onCallItemClicked(which)
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    /**
     * @param which 0 for voice call, 1 for video call
     */
    private fun onCallItemClicked(which: Int) {
        val isVideoCall: Boolean
        val permissions: Int
        val requestCode: Int

        if (which == 0) {
            isVideoCall = false
            permissions = PERMISSIONS_FOR_AUDIO_IP_CALL
            requestCode = PERMISSION_REQUEST_CODE_AUDIO_CALL
        } else {
            isVideoCall = true
            permissions = PERMISSIONS_FOR_VIDEO_IP_CALL
            requestCode = PERMISSION_REQUEST_CODE_VIDEO_CALL
        }

//        val builder = AlertDialog.Builder(this@RoomActivity)
//                .setTitle(R.string.dialog_title_confirmation)
//
//        if (isVideoCall) {
//            builder.setMessage(getString(R.string.start_video_call_prompt_msg))
//        } else {
//            builder.setMessage(getString(R.string.start_voice_call_prompt_msg))
//        }
//
//        builder
//                .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface, which: Int) {
//                        if (checkPermissions(permissions, this@RoomActivity, requestCode)) {
//                            startIpCall(PreferencesManager.useJitsiConfCall(this@RoomActivity), isVideoCall)
//                        }
//                    }
//                })
//                .setNegativeButton(R.string.cancel, null)
//                .show()
        if (checkPermissions(permissions, this@RoomActivity, requestCode)) {
            startIpCall(PreferencesManager.useJitsiConfCall(this@RoomActivity), isVideoCall)
        }
    }

    /**
     * Manage widget
     *
     * @param widget       the widget
     * @param aIsVideoCall true if it is a video call
     */
    private fun launchJitsiActivity(widget: Widget?, aIsVideoCall: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Display a error dialog for old API
            AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_error)
                    .setMessage(R.string.error_jitsi_not_supported_on_old_device)
                    .setPositiveButton(R.string.ok, null)
                    .show()
        } else {
            val intent = Intent(this, JitsiCallActivity::class.java)
            intent.putExtra(JitsiCallActivity.EXTRA_WIDGET_ID, widget)
            intent.putExtra(JitsiCallActivity.EXTRA_ENABLE_VIDEO, aIsVideoCall)
            startActivity(intent)
        }
    }

    /**
     * Start a jisti call
     *
     * @param aIsVideoCall true if the call is a video one
     */
    private fun startJitsiCall(aIsVideoCall: Boolean) {
        val wm = WidgetManagerProvider.getWidgetManager(this)
        if (wm != null) {
            enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
            showWaitingView()

            wm.createJitsiWidget(mxSession, mRoom, aIsVideoCall, object : ApiCallback<Widget> {
                override fun onSuccess(widget: Widget) {
                    hideWaitingView()

                    launchJitsiActivity(widget, aIsVideoCall)
                }

                private fun onError(errorMessage: String?) {
                    hideWaitingView()
                    Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
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

    /**
     * Start an IP call: audio call if aIsVideoCall is false or video call if aIsVideoCall
     * is true.
     *
     * @param useJitsiCall true to use jitsi calls
     * @param aIsVideoCall true to video call, false to audio call
     */
    private fun startIpCall(useJitsiCall: Boolean, aIsVideoCall: Boolean) {
        if (currentRoom == null) {
            return
        }

        if ((currentRoom!!.getNumberOfMembers() > 2) && useJitsiCall) {
            startJitsiCall(aIsVideoCall)
            return
        }

        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
        showWaitingView()

        // create the call object
        mxSession!!.mCallsManager.createCallInRoom(currentRoom!!.roomId, aIsVideoCall, object : ApiCallback<IMXCall> {
            override fun onSuccess(call: IMXCall) {
                Log.d(LOG_TAG, "## startIpCall(): onSuccess")
                runOnUiThread(object : Runnable {
                    override fun run() {
                        hideWaitingView()

//                        val intent = Intent(this@RoomActivity, CallViewActivity::class.java)
//
//                        intent.putExtra(CallViewActivity.EXTRA_MATRIX_ID, mxSession!!.getCredentials().userId)
//                        intent.putExtra(CallViewActivity.EXTRA_CALL_ID, call.callId)
//
//                        startActivity(intent)

                        val intent = Intent(this@RoomActivity, OutgoingCallActivity::class.java);
                        startActivity(intent);
                    }
                })
            }

            private fun onError(errorMessage: String) {
                runOnUiThread(object : Runnable {
                    override fun run() {
                        hideWaitingView()
                        Toast.makeText(this@RoomActivity,
                                getString(R.string.cannot_start_call) + " (" + errorMessage + ")", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onNetworkError(e: Exception) {
                Log.e(LOG_TAG, "## startIpCall(): onNetworkError Msg=" + e.message, e)
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                Log.e(LOG_TAG, "## startIpCall(): onMatrixError Msg=" + e.localizedMessage)

                if (e is MXCryptoError) {
                    val cryptoError = e as MXCryptoError
                    if (MXCryptoError.UNKNOWN_DEVICES_CODE == cryptoError.errcode) {
                        hideWaitingView()
//                        CommonActivityUtils.displayUnknownDevicesDialog(mxSession,
//                                this@RoomActivity,
//                                cryptoError.mExceptionData as MXUsersDevicesMap<MXDeviceInfo>,
//                                true,
//                                object : VectorUnknownDevicesFragment.IUnknownDevicesSendAnywayListener {
//                                    override fun onSendAnyway() {
//                                        startIpCall(useJitsiCall, aIsVideoCall)
//                                    }
//                                })
                        val devicesInfo = cryptoError.mExceptionData as MXUsersDevicesMap<MXDeviceInfo>;
                        devicesInfo?.let {
                            val deviceList = getDevicesList(it);
                            deviceList.forEach { t: Pair<String, List<MXDeviceInfo>>? ->
                                t?.second?.forEach { d: MXDeviceInfo? ->
                                    d?.let { mxDeviceInfo ->
                                        if (mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNVERIFIED || mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNKNOWN) {
                                            mxSession!!.crypto?.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, mxDeviceInfo.deviceId, mxDeviceInfo.userId, object : SimpleApiCallback<Void>() {
                                                override fun onSuccess(p0: Void?) {
                                                    android.util.Log.d("Verify device success", mxDeviceInfo.deviceId.toString())
                                                }
                                            })
                                        }
                                    }
                                }
                            }
                        }

                        return
                    }
                }

                onError(e.localizedMessage)
            }

            override fun onUnexpectedError(e: Exception) {
                Log.e(LOG_TAG, "## startIpCall(): onUnexpectedError Msg=" + e.localizedMessage, e)
                onError(e.localizedMessage)
            }
        })
    }

    //================================================================================
    // messages sending
    //================================================================================

    /**
     * Cancels the room selection mode.
     */
    fun cancelSelectionMode() {
        mVectorMessageListFragment!!.cancelSelectionMode()
    }

    private var mIsMarkDowning: Boolean = false

    /**
     * Send the editText text.
     */
    fun sendTextMessage(textMsg: String?) {
        if (mIsMarkDowning) {
            return
        }

        // ensure that a message is not sent twice
        // markdownToHtml seems being slow in some cases
//        mSendImageView!!.isEnabled = false
        mIsMarkDowning = true

        var textToSend = textMsg?.trim { it <= ' ' }

        val handleSlashCommand: Boolean
        if (textToSend!!.startsWith("\\/")) {
            handleSlashCommand = false
            textToSend = textToSend.substring(1)
        } else {
            handleSlashCommand = true
        }

        VectorApp.markdownToHtml(textToSend, object : VectorMarkdownParser.IVectorMarkdownParserListener {
            override fun onMarkdownParsed(text: String, htmlText: String) {
                runOnUiThread(object : Runnable {
                    override fun run() {
//                        mSendImageView!!.isEnabled = true
                        mIsMarkDowning = false
                        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
                        sendMessage(text, if (TextUtils.equals(text, htmlText)) null else htmlText, Message.FORMAT_MATRIX_HTML, handleSlashCommand)
                        mEditText!!.setText("")
                    }
                })
            }
        })
    }

    private fun editTextMessage() {
        if (mIsMarkDowning) {
            return
        }

        // ensure that a message is not sent twice
        // markdownToHtml seems being slow in some cases
//        mSendImageView!!.isEnabled = false
        mIsMarkDowning = true

        var textToSend = mEditText!!.text.toString().trim { it <= ' ' }

        val handleSlashCommand: Boolean
        if (textToSend.startsWith("\\/")) {
            handleSlashCommand = false
            textToSend = textToSend.substring(1)
        } else {
            handleSlashCommand = true
        }



        VectorApp.markdownToHtml(textToSend) { text, htmlText ->
            runOnUiThread {
                mIsMarkDowning = false
                enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
                currentEvent?.let {
                    if (textToSend.isNullOrEmpty())
                        return@let;
                    val mapNewContent = HashMap<String, String>();
                    mapNewContent.put("msgtype", Message.MSGTYPE_TEXT);
                    mapNewContent.put("body", textToSend);
                    textToSend = "* $textToSend";
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("body", textToSend);
                    jsonObject.addProperty("msgtype", "m.text");
                    val gson = Gson();
                    jsonObject.add("m.new_content", gson.toJsonTree(mapNewContent));
                    val event = Event("m.room.message", jsonObject, it.sender, it.roomId);
                    event.eventId = it.eventId;
                    messageRepository.editMessageRx(event).subscribe({
                        Toast.makeText(this@RoomActivity, "Edited", Toast.LENGTH_SHORT).show();
                    }, {
                        Toast.makeText(this@RoomActivity, it.message, Toast.LENGTH_SHORT).show();
                    })
                }
                mEditText!!.setText("")
                currentEvent = null;
                isEditedMode = false;
                currentMessageEdit = null;
            }
        }
    }

    /**
     * Send a text message with its formatted format
     *
     * @param body               the text message.
     * @param formattedBody      the formatted message
     * @param format             the message format
     * @param handleSlashCommand true to try to handle a Slash command
     */
    fun sendMessage(body: String, formattedBody: String?, format: String, handleSlashCommand: Boolean) {
        if (!TextUtils.isEmpty(body)) {
            if ((!handleSlashCommand || !vmodev.clearkeep.ultis.SlashCommandsParser.manageSplashCommand(this, mxSession, currentRoom, body, formattedBody, format))) {
                val currentSelectedEvent = mVectorMessageListFragment!!.currentSelectedEvent

                cancelSelectionMode()

                mVectorMessageListFragment!!.sendTextMessage(body, formattedBody, currentSelectedEvent, format)
            }
        }
    }

    /**
     * Send an emote in the opened room
     *
     * @param emote the emote
     */
    fun sendEmote(emote: String, formattedEmote: String, format: String) {
        if (null != mVectorMessageListFragment) {
            mVectorMessageListFragment!!.sendEmote(emote, formattedEmote, format)
        }
    }

    /**
     * Send the medias defined in the intent.
     * They are listed, checked and sent when it is possible.
     */
    @SuppressLint("NewApi")
    private fun sendMediasIntent(intent: Intent?) {
        var intent = intent
        // sanity check
        if ((null == intent) && (null == mLatestTakePictureCameraUri)) {
            return
        }

        var sharedDataItems: MutableList<RoomMediaMessage> = ArrayList()

        if (null != intent) {
            sharedDataItems = ArrayList(RoomMediaMessage.listRoomMediaMessages(intent))
        }

        // check the extras
        if ((0 == sharedDataItems.size) && (null != intent)) {
            val bundle = intent!!.extras
            // sanity checks
            if (null != bundle) {
                if (bundle!!.containsKey(Intent.EXTRA_TEXT)) {
                    mEditText!!.append(bundle!!.getString(Intent.EXTRA_TEXT))

                    mEditText!!.post(object : Runnable {
                        override fun run() {
                            mEditText!!.setSelection(mEditText!!.text.length)
                        }
                    })
                }
            }
        }
        val hasItemToShare = !sharedDataItems.isEmpty()
        val isTextOnly = (sharedDataItems.size == 1 && "text/plain" == sharedDataItems[0].getMimeType(this))
        val shouldPreviewMedia = PreferencesManager.previewMediaWhenSending(this)

        if (hasItemToShare && !isTextOnly && shouldPreviewMedia) {
            if (null != intent) {
                intent!!.setClass(this, MediaPreviewerActivity::class.java!!)
            } else {
                intent = Intent(this, MediaPreviewerActivity::class.java)
            }
            intent!!.setExtrasClassLoader(RoomMediaMessage::class.java!!.classLoader)
            if (currentRoom != null) {
                intent!!.putExtra(MediaPreviewerActivity.EXTRA_ROOM_TITLE, currentRoom!!.getRoomDisplayName(this))
            }
            if (null != mLatestTakePictureCameraUri) {
                intent!!.putExtra(MediaPreviewerActivity.EXTRA_CAMERA_PICTURE_URI, mLatestTakePictureCameraUri)
            }
            startActivityForResult(intent, CONFIRM_MEDIA_REQUEST_CODE)
        } else {
            if (null != mLatestTakePictureCameraUri) {
                if (0 == sharedDataItems.size) {
                    sharedDataItems.add(RoomMediaMessage(Uri.parse(mLatestTakePictureCameraUri)))
                }
            }
            mVectorRoomMediasSender!!.sendMedias(sharedDataItems)
        }

        mLatestTakePictureCameraUri = null
    }

    /**
     * Send a sticker
     *
     * @param data
     */
    private fun sendSticker(data: Intent?) {
        if (currentRoom == null) {
            return
        }

        val contentStr = StickerPickerActivity.getResultContent(data!!)

        val event = Event(Event.EVENT_TYPE_STICKER,
                JsonParser().parse(contentStr).asJsonObject,
                mxSession!!.getCredentials().userId,
                currentRoom!!.roomId)

        mVectorMessageListFragment!!.sendStickerMessage(event)
    }

    //================================================================================
    // typing
    //================================================================================

    /**
     * send a typing event notification
     *
     * @param isTyping typing param
     */
    private fun handleTypingNotification(isTyping: Boolean) {
        var isTyping = isTyping
        // the typing notifications are disabled ?
        if (!PreferencesManager.sendTypingNotifs(this)) {
            Log.d(LOG_TAG, "##handleTypingNotification() : the typing notifications are disabled")
            return
        }

        if (currentRoom == null) {
            return
        }

        Log.d(LOG_TAG, "##handleTypingNotification() : isTyping $isTyping")

        var notificationTimeoutMS = -1
        if (isTyping) {
            // Check whether a typing event has been already reported to server (We wait for the end of the local timeout before considering this new event)
            if (null != mTypingTimer) {
                // Refresh date of the last observed typing
                System.currentTimeMillis()
                mLastTypingDate = System.currentTimeMillis()
                return
            }

            var timerTimeoutInMs = TYPING_TIMEOUT_MS

            if (0L != mLastTypingDate) {
                val lastTypingAge = System.currentTimeMillis() - mLastTypingDate
                if (lastTypingAge < timerTimeoutInMs) {
                    // Subtract the time interval since last typing from the timer timeout
                    timerTimeoutInMs -= lastTypingAge.toInt()
                } else {
                    timerTimeoutInMs = 0
                }
            } else {
                // Keep date of this typing event
                mLastTypingDate = System.currentTimeMillis()
            }

            if (timerTimeoutInMs > 0) {

                try {
                    mTypingTimerTask = object : TimerTask() {
                        override fun run() {
                            synchronized(LOG_TAG) {
                                if (mTypingTimerTask != null) {
                                    mTypingTimerTask!!.cancel()
                                    mTypingTimerTask = null
                                }

                                if (mTypingTimer != null) {
                                    mTypingTimer!!.cancel()
                                    mTypingTimer = null
                                }

                                Log.d(LOG_TAG, "##handleTypingNotification() : send end of typing")

                                // Post a new typing notification
                                handleTypingNotification(0L != mLastTypingDate)
                            }
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.e(LOG_TAG, "## mTypingTimerTask creation failed " + throwable.message)
                    return
                }

                try {
                    synchronized(LOG_TAG) {
                        mTypingTimer = Timer()
                        mTypingTimer!!.schedule(mTypingTimerTask, TYPING_TIMEOUT_MS.toLong())
                    }
                } catch (throwable: Throwable) {
                    Log.e(LOG_TAG, "fails to launch typing timer " + throwable.message)
                    mTypingTimer = null
                    mTypingTimerTask = null
                }

                // Compute the notification timeout in ms (consider the double of the local typing timeout)
                notificationTimeoutMS = TYPING_TIMEOUT_MS * 2
            } else {
                // This typing event is too old, we will ignore it
                isTyping = false
            }
        } else {
            // Cancel any typing timer
            if (mTypingTimerTask != null) {
                mTypingTimerTask!!.cancel()
                mTypingTimerTask = null
            }

            if (mTypingTimer != null) {
                mTypingTimer!!.cancel()
                mTypingTimer = null
            }
            // Reset last typing date
            mLastTypingDate = 0
        }

        val typingStatus = isTyping
        currentRoom?.let { room ->
            room.sendTypingNotification(typingStatus, notificationTimeoutMS, object : SimpleApiCallback<Void>(this) {
                override fun onSuccess(p0: Void?) {
                    mLastTypingDate = 0
                }

                override fun onNetworkError(e: java.lang.Exception?) {
                    super.onNetworkError(e)
                    mTypingTimerTask?.let { timerTask ->
                        timerTask.cancel();
                        mTypingTimerTask = null;
                    }
                    mTypingTimer?.let { timer ->
                        timer.cancel();
                        mTypingTimer = null;
                    }
                }
            })
        }
    }

    private fun cancelTypingNotification() {
        if (currentRoom == null) {
            return
        }

        if (0L != mLastTypingDate) {
            if (mTypingTimerTask != null) {
                mTypingTimerTask!!.cancel()
                mTypingTimerTask = null
            }
            if (mTypingTimer != null) {
                mTypingTimer!!.cancel()
                mTypingTimer = null
            }

            mLastTypingDate = 0

            currentRoom!!.sendTypingNotification(false, -1, object : SimpleApiCallback<Void>(this) {
                override fun onSuccess(info: Void) {
                    // Ignore
                }
            })
        }
    }

    //================================================================================
    // Actions
    //================================================================================

    /**
     * Launch the room details activity with a selected tab.
     *
     * @param selectedTab the selected tab index.
     */
    private fun launchRoomDetails(selectedTab: Int) {
        if (mxSession != null && currentRoom != null) {
            enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

            // pop to the home activity
            val intent = Intent(this, VectorRoomDetailsActivity::class.java)
            intent.putExtra(VectorRoomDetailsActivity.EXTRA_ROOM_ID, currentRoom!!.roomId)
            intent.putExtra(VectorRoomDetailsActivity.EXTRA_MATRIX_ID, mxSession!!.getCredentials().userId)
            intent.putExtra(VectorRoomDetailsActivity.EXTRA_SELECTED_TAB_ID, selectedTab)
            startActivityForResult(intent, GET_MENTION_REQUEST_CODE)
        }
    }

    /**
     * Launch the invite people activity
     */
    private fun launchInvitePeople() {
        if ((null != mxSession) && (null != currentRoom)) {
            val intent = Intent(this, VectorRoomInviteMembersActivity::class.java)
            intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_MATRIX_ID, mxSession!!.myUserId)
            intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_ROOM_ID, currentRoom!!.roomId)
            intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_ADD_CONFIRMATION_DIALOG, true)
            startActivityForResult(intent, INVITE_USER_REQUEST_CODE)
        }
    }

    /**
     * Launch audio recorder intent
     */
    private fun launchAudioRecorderIntent() {
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        openSoundRecorder(this, RECORD_AUDIO_REQUEST_CODE)
    }

    /**
     * Launch the files selection intent
     */
    private fun launchFileSelectionIntent() {
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        openFileSelection(this, null, true, REQUEST_FILES_REQUEST_CODE)
    }

    private fun startStickerPickerActivity() {
        // Search for the sticker picker widget in the user account
        val userWidgets = mxSession!!.getUserWidgets()

        var stickerWidgetUrl: String? = null
        var stickerWidgetId: String? = null

        for (o in userWidgets.values) {
            if (o is Map<*, *>) {
                val content = (o as Map<*, *>)["content"]
                if (content != null && content is Map<*, *>) {
                    val type = (content as Map<*, *>)["type"]
                    if (type != null && type is String && type == StickerPickerActivity.WIDGET_NAME) {
                        stickerWidgetUrl = (content as Map<*, *>)["url"] as String?
                        stickerWidgetId = (o as Map<*, *>)["id"] as String?
                        break
                    }
                }
            }
        }

        if (TextUtils.isEmpty(stickerWidgetUrl)) {
            // The Sticker picker widget is not installed yet. Propose the user to install it
            val builder = AlertDialog.Builder(this)

            // Use the builder context
            val v = LayoutInflater.from(builder.context).inflate(R.layout.dialog_no_sticker_pack, null)

            builder
                    .setView(v)
                    .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            // Open integration manager, to the sticker installation page
                            openIntegrationManagerActivity("type_" + StickerPickerActivity.WIDGET_NAME)
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show()
        } else {
            if (currentRoom == null) {
                return
            }

            val intent = StickerPickerActivity.getIntent(this, mMyUserId!!, currentRoom!!.roomId, stickerWidgetUrl!!, stickerWidgetId!!)

            startActivityForResult(intent, STICKER_PICKER_ACTIVITY_REQUEST_CODE)
        }
    }

    /**
     * Launch the camera
     */
    private fun launchNativeVideoRecorder() {
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        openVideoRecorder(this, TAKE_IMAGE_REQUEST_CODE)
    }

    /**
     * Launch the camera
     */
    private fun launchNativeCamera() {
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        mLatestTakePictureCameraUri = openCamera(this, CAMERA_VALUE_TITLE, TAKE_IMAGE_REQUEST_CODE)
    }

    /**
     * Launch the camera
     */
    private fun launchCamera() {
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        val intent = Intent(this@RoomActivity, VectorMediaPickerActivity::class.java)
        intent.putExtra(VectorMediaPickerActivity.EXTRA_VIDEO_RECORDING_MODE, true)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (0 == permissions.size) {
            Log.d(LOG_TAG, "## onRequestPermissionsResult(): cancelled $requestCode")
        } else if ((requestCode == PERMISSION_REQUEST_CODE_LAUNCH_CAMERA
                        || requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA
                        || requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA)) {
            var isCameraPermissionGranted = false
            var isWritePermissionGranted = false

            for (i in permissions.indices) {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): " + permissions[i] + "=" + grantResults[i])

                if (Manifest.permission.CAMERA == permissions[i]) {
                    if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission granted")
                        isCameraPermissionGranted = true
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission not granted")
                    }
                }

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE == permissions[i]) {
                    if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission granted")
                        isWritePermissionGranted = true
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission not granted")
                    }
                }
            }

            // Because external storage permission is not mandatory to launch the camera,
            // external storage permission is not tested.
            if (isCameraPermissionGranted) {
                if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_CAMERA) {
                    launchCamera()
                } else if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA) {
                    if (isWritePermissionGranted) {
                        launchNativeCamera()
                    } else {
                        Toast.makeText(this, getString(R.string.missing_permissions_error), Toast.LENGTH_SHORT).show()
                    }
                } else if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA) {
                    if (isWritePermissionGranted) {
                        launchNativeVideoRecorder()
                    } else {
                        Toast.makeText(this, getString(R.string.missing_permissions_error), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.missing_permissions_warning), Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_AUDIO_CALL) {
            if (onPermissionResultAudioIpCall(this, grantResults)) {
                startIpCall(PreferencesManager.useJitsiConfCall(this), false)
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_VIDEO_CALL) {
            if (onPermissionResultVideoIpCall(this, grantResults)) {
                startIpCall(PreferencesManager.useJitsiConfCall(this), true)
            }
        } else {
            // Transmit to Fragment
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Display UI buttons according to user input text.
     */
    private fun manageSendMoreButtons() {
        if (!isEditedMode) {
            var img = R.drawable.ic_material_file
            if (!PreferencesManager.sendMessageWithEnter(this) && mEditText!!.text.length > 0) {
                img = R.drawable.ic_material_send_green
                buttonSend.setBackgroundResource(R.drawable.background_radius_green)
                buttonSend.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                buttonSend.setBackgroundResource(R.drawable.background_border_radius_gray)
                val typeValue = TypedValue();
                theme.resolveAttribute(R.attr.text_not_available_send, typeValue, true);
//            buttonSend.setTextColor(Color.parseColor("#000000"))
                buttonSend.setTextColor(typeValue.data);
                when (PreferencesManager.getSelectedDefaultMediaSource(this)) {
                    MEDIA_SOURCE_VOICE -> if (PreferencesManager.isSendVoiceFeatureEnabled(this)) {
                        img = R.drawable.vector_micro_green
                    }
                    MEDIA_SOURCE_STICKER -> img = R.drawable.ic_send_sticker
                    MEDIA_SOURCE_PHOTO -> img = R.drawable.ic_material_camera
                    MEDIA_SOURCE_VIDEO -> img = R.drawable.ic_material_videocam
                }
            }
//            mSendImageView!!.setImageResource(img)
        } else {
            if (!PreferencesManager.sendMessageWithEnter(this) && mEditText!!.text.length > 0 && !TextUtils.equals(mEditText.text.toString(), currentMessageEdit)) {
                buttonSend.setBackgroundResource(R.drawable.background_radius_green)
                buttonSend.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                buttonSend.setBackgroundResource(R.drawable.background_border_radius_gray)
                val typeValue = TypedValue();
                theme.resolveAttribute(R.attr.text_not_available_send, typeValue, true);
                buttonSend.setTextColor(typeValue.data);
                when (PreferencesManager.getSelectedDefaultMediaSource(this)) {
                    MEDIA_SOURCE_VOICE -> if (PreferencesManager.isSendVoiceFeatureEnabled(this)) {

                    }
                }
            }
        }
    }

    /**
     * Refresh the Account avatar
     */
    private fun refreshSelfAvatar() {
        // sanity check
        if (null != mAvatarImageView) {
            VectorUtils.loadUserAvatar(this, mxSession, mAvatarImageView, mxSession!!.getMyUser())
        }
    }

    /**
     * Sanitize the display name.
     *
     * @param displayName the display name to sanitize
     * @return the sanitized display name
     */
    fun sanitizeDisplayname(displayName: String): String {
        var displayName = displayName
        // sanity checks
        if (!TextUtils.isEmpty(displayName)) {
            val ircPattern = " (IRC)"

            if (displayName.endsWith(ircPattern)) {
                displayName = displayName.substring(0, displayName.length - ircPattern.length)
            }
        }

        return displayName
    }

    /**
     * Insert a text in the text editor
     *
     * @param text the text
     */
    fun insertTextInTextEditor(text: String) {
//        // another user
//        if (TextUtils.isEmpty(mEditText!!.text)) {
//            mEditText!!.append(text)
//        } else {
//            mEditText!!.text.insert(mEditText!!.selectionStart, "$text ")
//        }
        sendTextMessage(text)
    }

    /**
     * Insert an user displayname  in the message editor.
     *
     * @param text the text to insert.
     */
    fun insertUserDisplayNameInTextEditor(text: String?) {
        if (null != text) {
            var vibrate = false

            if (TextUtils.equals(mxSession!!.getMyUser().displayname, text)) {
                // current user
                if (TextUtils.isEmpty(mEditText!!.text)) {
                    mEditText!!.append(SlashCommandsParser.SlashCommand.EMOTE.command + " ")
                    mEditText!!.setSelection(mEditText!!.text.length)
                    vibrate = true
                }
            } else {
                // another user
                if (TextUtils.isEmpty(mEditText!!.text)) {
                    // Ensure displayName will not be interpreted as a Slash command
                    if (text!!.startsWith("/")) {
                        mEditText!!.append("\\")
                    }
                    mEditText!!.append(sanitizeDisplayname(text) + ": ")
                } else {
                    mEditText!!.text.insert(mEditText!!.selectionStart, sanitizeDisplayname(text) + " ")
                }

                vibrate = true
            }

            if (vibrate && PreferencesManager.vibrateWhenMentioning(this)) {
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if ((null != v) && v!!.hasVibrator()) {
                    v!!.vibrate(100)
                }
            }
        }
    }

    /**
     * Insert a quote  in the message editor.
     *
     * @param quote the quote to insert.
     */
    fun insertQuoteInTextEditor(quote: String) {
        if (!TextUtils.isEmpty(quote)) {
            if (TextUtils.isEmpty(mEditText!!.text)) {
                mEditText!!.setText("")
                mEditText!!.append(quote)
            } else {
                mEditText!!.text.insert(mEditText!!.selectionStart, "\n" + quote)
            }
        }
    }

    fun insertSelectedMessageInTextEditor(event: Event, textMsg: String?) {
        currentEvent = event;
        isEditedMode = true;
        imageViewCancelEdit.visibility = View.VISIBLE;
        mEditText.setText(textMsg);
        textMsg?.let { mEditText.setSelection(it.length); }
    }

    @OnClick(R.id.image_view_cancel_edit)
    internal fun onClickCancelEdit() {
        isEditedMode = false;
        currentEvent = null;
        mEditText.setText("");
        imageViewCancelEdit.visibility = View.GONE;
    }

    /* ==========================================================================================
     * Implement VectorMessageListFragmentListener
     * ========================================================================================== */

    override fun showPreviousEventsLoadingWheel() {
        mBackProgressView!!.visibility = View.VISIBLE
    }

    override fun hidePreviousEventsLoadingWheel() {
        mBackProgressView!!.visibility = View.GONE
    }

    override fun showNextEventsLoadingWheel() {
        mForwardProgressView!!.visibility = View.VISIBLE
    }

    override fun hideNextEventsLoadingWheel() {
        mForwardProgressView!!.visibility = View.GONE
    }

    override fun showMainLoadingWheel() {
        mMainProgressView!!.visibility = View.VISIBLE
    }

    override fun hideMainLoadingWheel() {
        mMainProgressView!!.visibility = View.GONE
    }

    override fun onSelectedEventChange(currentSelectedEvent: Event?) {
        // Update hint
        setEditTextHint(currentSelectedEvent)
    }

    //================================================================================
    // Notifications area management (... is typing and so on)
    //================================================================================

    /**
     * Refresh the notifications area.
     */
    private fun refreshNotificationsArea() {
        // sanity check
        // might happen when the application is logged out
        if ((null == mxSession!!.dataHandler) || (null == currentRoom) || (null != sRoomPreviewData)) {
            return
        }
        val limitResourceState = mResourceLimitEventListener!!.limitResourceState
        val hardResourceLimitExceededError = mxSession!!.dataHandler.getResourceLimitExceededError()
        val softResourceLimitExceededError = limitResourceState.softErrorOrNull()

        var state: NotificationAreaView.State = NotificationAreaView.State.Default
        mHasUnsentEvents = false
        if (!mIsUnreadPreviewMode && !TextUtils.isEmpty(mEventId)) {
            state = NotificationAreaView.State.Hidden
        } else if (hardResourceLimitExceededError != null) {
            state = NotificationAreaView.State.ResourceLimitExceededError(false, hardResourceLimitExceededError!!)
        } else if (softResourceLimitExceededError != null) {
            state = NotificationAreaView.State.ResourceLimitExceededError(true, softResourceLimitExceededError!!)
        } else if (!Matrix.getInstance(this)!!.isConnected) {
            state = NotificationAreaView.State.ConnectionError
        } else if (mIsUnreadPreviewMode) {
            state = NotificationAreaView.State.UnreadPreview
        } else {
            val undeliveredEvents = mxSession!!.dataHandler.store!!.getUndeliveredEvents(currentRoom!!.roomId)
            val unknownDeviceEvents = mxSession!!.dataHandler.store!!.getUnknownDeviceEvents(currentRoom!!.roomId)
            val hasUndeliverableEvents = (undeliveredEvents != null) && (undeliveredEvents!!.size > 0)
            val hasUnknownDeviceEvents = (unknownDeviceEvents != null) && (unknownDeviceEvents!!.size > 0)
            if (hasUndeliverableEvents || hasUnknownDeviceEvents) {
                mHasUnsentEvents = true
                state = NotificationAreaView.State.UnsentEvents(hasUndeliverableEvents, hasUnknownDeviceEvents)
            } else if ((null != mIsScrolledToTheBottom) && (!mIsScrolledToTheBottom!!)) {
                var unreadCount = 0
                val summary = currentRoom!!.dataHandler.store!!.getSummary(currentRoom!!.roomId)
                if (summary != null) {
                    unreadCount = currentRoom!!.dataHandler.store!!.eventsCountAfter(currentRoom!!.roomId, summary!!.getReadReceiptEventId())
                }
                state = NotificationAreaView.State.ScrollToBottom(unreadCount, mLatestTypingMessage)
            } else if (!TextUtils.isEmpty(mLatestTypingMessage)) {
                state = NotificationAreaView.State.Typing(mLatestTypingMessage!!)
            } else if (currentRoom!!.state.isVersioned()) {
                val roomTombstoneContent = currentRoom!!.state.roomTombstoneContent
                val events = mRoom!!.state.getStateEvents(HashSet<String>(Arrays.asList(Event.EVENT_TYPE_STATE_ROOM_TOMBSTONE)))

                var sender = ""
                if (events != null && !events!!.isEmpty()) {
                    sender = events!!.get(0).sender
                }

                state = NotificationAreaView.State.Tombstone(roomTombstoneContent!!, sender)
            }
        }
        mNotificationsArea!!.render(state)

        supportInvalidateOptionsMenu()
    }

    /**
     * Refresh the call buttons display.
     */
    private fun refreshCallButtons(refreshOngoingConferenceCallView: Boolean) {
        if (currentRoom == null) {
            return
        }

        if ((null == sRoomPreviewData) && (null == mEventId) && canSendMessages(currentRoom!!.state)) {
            val isCallSupported = currentRoom!!.canPerformCall() && mxSession!!.isVoipCallSupported()
            val call = CallsManager.getSharedInstance().activeCall
            val activeWidget = mVectorOngoingConferenceCallView!!.activeWidget

            if ((null == call) && (null == activeWidget)) {
                /*mStartCallLayout!!.visibility =*/ if (((isCallSupported && ((mEditText!!.text.length == 0 || PreferencesManager.sendMessageWithEnter(this))))))
                    View.VISIBLE
                else
                    View.GONE
                mStopCallLayout!!.visibility = View.GONE
                imageViewVideoCall.visibility = View.VISIBLE
                imageViewVoiceCall.visibility = View.VISIBLE
            } else if (null != activeWidget) {
//                mStartCallLayout!!.visibility = View.GONE
                mStopCallLayout!!.visibility = View.GONE
                imageViewVideoCall.visibility = View.VISIBLE
                imageViewVoiceCall.visibility = View.VISIBLE
            } else {
                val roomCall = mxSession!!.mCallsManager.getCallWithRoomId(currentRoom!!.roomId)

                // ensure that the listener is defined once
                call!!.removeListener(mCallListener)
                call!!.addListener(mCallListener)

//                mStartCallLayout!!.visibility = View.GONE
                if ((call === roomCall)) {
                    mStopCallLayout!!.visibility = View.VISIBLE
                    imageViewVideoCall.visibility = View.GONE
                    imageViewVoiceCall.visibility = View.GONE
                } else {
                    mStopCallLayout!!.visibility = View.GONE
                    imageViewVideoCall.visibility = View.VISIBLE
                    imageViewVoiceCall.visibility = View.VISIBLE
                }
            }

            if (refreshOngoingConferenceCallView) {
                mVectorOngoingConferenceCallView!!.refresh()
            }
        }
    }

    /**
     * Display the typing status in the notification area.
     */
    private fun onRoomTyping() {
        mLatestTypingMessage = null

        if (currentRoom == null) {
            return
        }

        val typingUsers = currentRoom!!.getTypingUsers()

        if (!typingUsers.isEmpty()) {
            val myUserId = mxSession!!.myUserId

            // get the room member names
            val names = ArrayList<String>()

            for (i in typingUsers.indices) {
                val member = currentRoom!!.getMember(typingUsers.get(i))

                // check if the user is known and not oneself
                if ((null != member) && !TextUtils.equals(myUserId, member!!.userId) && (null != member!!.displayname)) {
                    names.add(member!!.displayname)
                }
            }

            if (names.isEmpty()) {
                // nothing to display
                mLatestTypingMessage = null
            } else if (names.size == 1) {
                mLatestTypingMessage = getString(R.string.room_one_user_is_typing, names[0])
            } else if (names.size == 2) {
                mLatestTypingMessage = getString(R.string.room_two_users_are_typing, names[0], names[1])
            } else {
                mLatestTypingMessage = getString(R.string.room_many_users_are_typing, names[0], names[1])
            }
        }

        refreshNotificationsArea()
    }

    //================================================================================
    // expandable header management command
    //================================================================================

    /**
     * Refresh the collapsed or the expanded headers
     */
    private fun updateActionBarTitleAndTopic() {
        setTitle()
        setTopic()
    }

    /**
     * Set the topic
     */
    private fun setTopic() {
        var topic: String? = null

        if (null != currentRoom) {
            topic = currentRoom!!.getTopic()
        } else if ((null != sRoomPreviewData) && (null != sRoomPreviewData!!.roomState)) {
            topic = sRoomPreviewData!!.roomState!!.topic
        } else if ((null != sRoomPreviewData) && (null != sRoomPreviewData!!.publicRoom)) {
            topic = sRoomPreviewData!!.publicRoom!!.topic
        }

        setTopic(topic)
    }

    /**
     * Set the topic.
     *
     * @param aTopicValue the new topic value
     */
    private fun setTopic(aTopicValue: String?) {
        // in search mode, the topic is not displayed
        if (!TextUtils.isEmpty(mEventId)) {
            mActionBarCustomTopic!!.visibility = View.GONE
        } else {
            // update the topic of the room header
            updateRoomHeaderTopic()

            // update the action bar topic anyway
            mActionBarCustomTopic!!.text = aTopicValue

//             set the visibility of topic on the custom action bar only
//             if header room view is gone, otherwise skipp it
//            if (View.GONE == mRoomHeaderView!!.visibility) {
//                // topic is only displayed if its content is not empty
//                if (TextUtils.isEmpty(aTopicValue)) {
//                    mActionBarCustomTopic!!.visibility = View.GONE
//                } else {
//                    mActionBarCustomTopic!!.visibility = View.VISIBLE
//                }
//            }
        }
    }

    /**
     * Refresh the room avatar.
     */
    private fun updateRoomHeaderAvatar() {
//        if (null != currentRoom) {
//            VectorUtils.loadRoomAvatar(this, mxSession, mActionBarHeaderRoomAvatar, currentRoom)
//        } else if (null != sRoomPreviewData) {
//            var roomName = sRoomPreviewData!!.roomName
//            if (TextUtils.isEmpty(roomName)) {
//                roomName = " "
//            }
//            VectorUtils.loadUserAvatar(this,
//                    sRoomPreviewData!!.session, mActionBarHeaderRoomAvatar, sRoomPreviewData!!.roomAvatarUrl, sRoomPreviewData!!.roomId, roomName)
//        }
    }


    /**
     * Create a custom action bar layout to process the room header view.
     *
     *
     * This action bar layout will contain a title, a topic and an arrow.
     * The arrow is updated (down/up) according to if the room header is
     * displayed or not.
     */
//    @OnClick(R.id.open_chat_header_arrow)
    internal fun OnOpenHeaderClick() {
        // display/hide the header view
//        if (null != mRoomHeaderView) {
//            if (View.GONE == mRoomHeaderView!!.visibility) {
//                enableActionBarHeader(SHOW_ACTION_BAR_HEADER)
//            } else {
//                enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
//            }
//        }
    }

    //    @OnClick(R.id.header_texts_container)
    internal fun onTextsContainerClick() {
//        if (TextUtils.isEmpty(mEventId) && (null == sRoomPreviewData)) {
//            enableActionBarHeader(SHOW_ACTION_BAR_HEADER)
//        }
    }

    // last position
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()

    // add touch listener on the header view itself
//    @OnTouch(R.id.action_bar_header)
    internal fun onRoomHeaderTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mStartX = event.x
            mStartY = event.y
        } else if (event.action == MotionEvent.ACTION_UP) {
            val curX = event.x
            val curY = event.y

            val deltaX = curX - mStartX
            val deltaY = curY - mStartY

            // swipe up to hide room header
            if ((Math.abs(deltaY) > Math.abs(deltaX)) && (deltaY < 0)) {
                enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
            } else {
                // wait the touch up to display the room settings page
                launchRoomDetails(VectorRoomDetailsActivity.SETTINGS_TAB_INDEX)
            }
        }
        return true
    }

    /**
     * Set the title value in the action bar and in the
     * room header layout
     */
    private fun setTitle() {
        var titleToApply = mDefaultRoomName
        if ((null != mxSession) && (null != currentRoom)) {
            titleToApply = currentRoom!!.getRoomDisplayName(this)

            if (TextUtils.isEmpty(titleToApply)) {
                titleToApply = mDefaultRoomName
            }

            // in context mode, add search to the title.
            if (!TextUtils.isEmpty(mEventId) && !mIsUnreadPreviewMode) {
                titleToApply = (getText(R.string.search)).toString() + " : " + titleToApply
            }
        } else if (null != sRoomPreviewData) {
            titleToApply = sRoomPreviewData!!.roomName
        }

        // set action bar title
        if (null != mActionBarCustomTitle) {
            mActionBarCustomTitle!!.text = titleToApply
        } else {
            title = titleToApply
        }

        // set title in the room header (no matter if not displayed)
//        if (null != mActionBarHeaderRoomName) {
//            mActionBarHeaderRoomName!!.text = titleToApply
//        }
    }

    /**
     * Update the UI content of the action bar header view
     */
    private fun updateActionBarHeaderView() {
        // update room avatar content
        updateRoomHeaderAvatar()

//        // update the room name
//        if (null != sRoomPreviewData) {
//            mActionBarHeaderRoomName!!.text = sRoomPreviewData!!.roomName
//        } else if (null != currentRoom) {
//            mActionBarHeaderRoomName!!.setText(currentRoom!!.getRoomDisplayName(this))
//        } else {
//            mActionBarHeaderRoomName!!.text = ""
//        }

        // update topic and members status
        updateRoomHeaderTopic()
        updateRoomHeaderMembersStatus()
    }

    private fun updateRoomHeaderTopic() {
//        if (null != mActionBarCustomTopic) {
//            var value: String? = null
//
//            if ((null != sRoomPreviewData) && (null != sRoomPreviewData!!.roomState)) {
//                value = sRoomPreviewData!!.roomState!!.topic
//            } else if ((null != sRoomPreviewData) && (null != sRoomPreviewData!!.publicRoom)) {
//                value = sRoomPreviewData!!.publicRoom!!.topic
//            } else if (null != currentRoom) {
//                value = if (currentRoom!!.isReady()) currentRoom!!.getTopic() else mDefaultTopic
//            }

        // if topic value is empty, just hide the topic TextView
//            if (TextUtils.isEmpty(value)) {
//                mActionBarHeaderRoomTopic!!.visibility = View.GONE
//            } else {
//                mActionBarHeaderRoomTopic!!.visibility = View.VISIBLE
//
//                val strBuilder = SpannableStringBuilder(value)
//                MatrixURLSpan.refreshMatrixSpans(strBuilder, mVectorMessageListFragment)
//                mActionBarHeaderRoomTopic!!.text = strBuilder
//
//                val urls = strBuilder.getSpans(0, value!!.length, URLSpan::class.java)
//
//                if ((null != urls) && (urls!!.size > 0)) {
//                    for (span in urls!!) {
//                        makeLinkClickable(strBuilder, span, value)
//                    }
//                }
//            }
//        }
    }

    /**
     * Trap the clicked URL.
     *
     * @param strBuilder the input string
     * @param span       the URL
     * @param value      roomAlias, roomId, groupId, eventId, etc.
     */
    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, value: String) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)

        if (start >= 0 && end >= 0) {
            val flags = strBuilder.getSpanFlags(span)

            val clickable = object : ClickableSpan() {
                override fun onClick(view: View) {
                    if (null != mVectorMessageListFragment) {
                        mVectorMessageListFragment!!.onURLClick(Uri.parse(PermalinkUtils.createPermalink(value)))
                    }
                }
            }

            strBuilder.setSpan(clickable, start, end, flags)
            strBuilder.removeSpan(span)
        }
    }

    /**
     * Tell if the user can send a message in this room.
     *
     * @return true if the user is allowed to send messages in this room.
     */
    private fun canSendMessages(state: RoomState): Boolean {
        var canSendMessage = !state.isVersioned
        if (canSendMessage) {
            val powerLevels = state.powerLevels
            canSendMessage = (powerLevels != null && powerLevels!!.maySendMessage(mMyUserId))
        }
        if (canSendMessage) {
            canSendMessage = mxSession!!.dataHandler.getResourceLimitExceededError() == null
        }
        return canSendMessage
    }

    /**
     * Check if the user can send a message in this room
     */
    private fun checkSendEventStatus() {
        if ((null != currentRoom) && (null != currentRoom!!.state)) {
            val state = currentRoom!!.state

            if (canSendMessages(state)) {
                mBottomLayout!!.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                mBottomSeparator!!.visibility = View.VISIBLE
                mSendingMessagesLayout!!.visibility = View.VISIBLE
                mCanNotPostTextView!!.visibility = View.GONE
            } else if (state.isVersioned() || mxSession!!.dataHandler.getResourceLimitExceededError() != null) {
                mBottomSeparator!!.visibility = View.GONE
                mBottomLayout!!.layoutParams.height = 0
            } else {
                mBottomSeparator!!.visibility = View.GONE
                mSendingMessagesLayout!!.visibility = View.GONE
                mCanNotPostTextView!!.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Display the active members count / members count in the expendable header.
     */
    private fun updateRoomHeaderMembersStatus() {
//        if (null != mActionBarHeaderActiveMembersLayout) {
//            // refresh only if the action bar is hidden
//            if (mActionBarCustomTitle!!.visibility == View.GONE) {
//                if (currentRoom != null || sRoomPreviewData != null) {
//                    // update the members status: "active members"/"members"
//
//                    val roomState = if ((null != sRoomPreviewData)) sRoomPreviewData!!.roomState else currentRoom!!.state
//
//                    if (null != roomState) {
//                        // display the both action buttons only when it makes sense
//                        // i.e not a room preview
//                        val hideMembersButtons = (null == currentRoom) || !TextUtils.isEmpty(mEventId) || (null != sRoomPreviewData)
//                        mActionBarHeaderActiveMembersListButton!!.visibility = if (hideMembersButtons) View.INVISIBLE else View.VISIBLE
//                        mActionBarHeaderActiveMembersInviteButton!!.visibility = if (hideMembersButtons) View.INVISIBLE else View.VISIBLE
//
//                        // Display what we have synchronously first. Use 0 as active members number
//                        if (currentRoom != null) {
//                            setMemberHeaderText(0, currentRoom!!.getNumberOfJoinedMembers())
//
//                            // Then request the list of members asynchronously
//                            currentRoom!!.getDisplayableMembersAsync(object : ApiCallback<List<RoomMember>> {
//                                override fun onSuccess(members: List<RoomMember>) {
//                                    var joinedMembersCount = 0
//                                    var activeMembersCount = 0
//
//                                    for (member in members) {
//                                        if (TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_JOIN)) {
//                                            joinedMembersCount++
//
//                                            val user = mxSession!!.dataHandler.store!!.getUser(member.userId)
//
//                                            if ((null != user) && user!!.isActive()) {
//                                                activeMembersCount++
//                                            }
//                                        }
//                                    }
//
//                                    setMemberHeaderText(activeMembersCount, joinedMembersCount)
//
//                                    if (joinedMembersCount == 0) {
//                                        checkPublicRoom()
//                                    }
//                                }
//
//                                override fun onNetworkError(e: Exception) {
//                                    checkPublicRoom()
//                                }
//
//                                override fun onMatrixError(e: MatrixError) {
//                                    checkPublicRoom()
//                                }
//
//                                override fun onUnexpectedError(e: Exception) {
//                                    checkPublicRoom()
//                                }
//                            })
//                        } else if (sRoomPreviewData != null) {
//                            checkPublicRoom()
//                        }
//                    } else if (sRoomPreviewData != null && sRoomPreviewData!!.publicRoom != null) {
//                        checkPublicRoom()
//                    } else {
//                        mActionBarHeaderActiveMembersLayout!!.visibility = View.GONE
//                    }
//                } else {
//                    mActionBarHeaderActiveMembersLayout!!.visibility = View.GONE
//                }
//            } else {
//                mActionBarHeaderActiveMembersLayout!!.visibility = View.GONE
//            }
//        }
    }

    private fun checkPublicRoom() {
        if (sRoomPreviewData != null && sRoomPreviewData!!.publicRoom != null) {
            setMemberHeaderText(0, sRoomPreviewData!!.publicRoom!!.numJoinedMembers)
        } else {
            // Should not happen
            setMemberHeaderText(0, 0)
        }
    }

    /**
     * Set the text in Room header about active/joined member count
     *
     * @param activeMembersCount number of active members
     * @param joinedMembersCount number of joined members
     */
    private fun setMemberHeaderText(activeMembersCount: Int, joinedMembersCount: Int) {
        val text: String

        if (joinedMembersCount == 1) {
            text = getString(R.string.room_title_one_member)
        } else if (null != sRoomPreviewData) {
            text = resources.getQuantityString(R.plurals.room_title_members, joinedMembersCount, joinedMembersCount)
        } else {
            text = ((activeMembersCount).toString() + "/" +
                    resources.getQuantityString(R.plurals.room_header_active_members_count, joinedMembersCount, joinedMembersCount))
        }

//        mActionBarHeaderActiveMembersTextView!!.text = text
//        mActionBarHeaderActiveMembersLayout!!.visibility = View.VISIBLE
    }

    /**
     * Dismiss the keyboard
     */
    fun dismissKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText!!.windowToken, 0)
    }

    /**
     * Show or hide the action bar header view according to aIsHeaderViewDisplayed
     *
     * @param aIsHeaderViewDisplayed true to show the header view, false to hide
     */
    private fun enableActionBarHeader(aIsHeaderViewDisplayed: Boolean) {
        mIsHeaderViewDisplayed = aIsHeaderViewDisplayed
        if (SHOW_ACTION_BAR_HEADER == aIsHeaderViewDisplayed) {
            dismissKeyboard()

            // hide the name and the topic in the action bar.
            // these items are hidden when the header view is opened
//            mActionBarCustomTitle!!.visibility = View.GONE
            mActionBarCustomTopic!!.visibility = View.GONE

            // update the UI content of the action bar header
            updateActionBarHeaderView()
            // set the arrow to up
//            mActionBarCustomArrowImageView!!.setImageResource(R.drawable.ic_arrow_drop_up_white)
            // enable the header view to make it visible
//            mRoomHeaderView!!.visibility = View.VISIBLE
            toolbar!!.setBackgroundColor(Color.TRANSPARENT)
        } else {
            // hide the room header only if it is displayed
//            if (View.VISIBLE == mRoomHeaderView!!.visibility) {
//                // show the name and the topic in the action bar.
//                mActionBarCustomTitle!!.visibility = View.VISIBLE
//                // if the topic is empty, do not show it
//                if (!TextUtils.isEmpty(mActionBarCustomTopic!!.text)) {
//                    mActionBarCustomTopic!!.visibility = View.VISIBLE
//                }
//
//                // update title and topic (action bar)
//                updateActionBarTitleAndTopic()
//
//                // hide the action bar header view and reset the arrow image (arrow reset to down)
////                mActionBarCustomArrowImageView!!.setImageResource(R.drawable.ic_arrow_drop_down_white)
////                mRoomHeaderView!!.visibility = View.GONE
//                toolbar!!.setBackgroundColor(ThemeUtils.getColor(this, R.attr.colorPrimary))
//            }
        }
    }

    //================================================================================
    // Kick / ban mode management
    //================================================================================

    /**
     * Manage the room preview buttons area
     */
    private fun manageBannedHeader(member: RoomMember) {
        mRoomPreviewLayout!!.visibility = View.VISIBLE

        val invitationTextView = findViewById<TextView>(R.id.room_preview_invitation_textview)

        if (TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_BAN)) {
            invitationTextView.text = getString(R.string.has_been_banned, currentRoom!!.getRoomDisplayName(this), currentRoom!!.state.getMemberName(member.mSender))
        } else {
            invitationTextView.text = getString(R.string.has_been_kicked, currentRoom!!.getRoomDisplayName(this), currentRoom!!.state.getMemberName(member.mSender))
        }

        // On mobile side, the modal to allow to add a reason to ban/kick someone isn't yet implemented
        // That's why, we don't display the TextView "Motif :" for now.
        val subInvitationTextView = findViewById<TextView>(R.id.room_preview_subinvitation_textview)
        if (!TextUtils.isEmpty(member.reason)) {
            val reason = getString(R.string.reason_colon, member.reason)
            subInvitationTextView.text = reason
        } else {
            subInvitationTextView.text = null
        }

        val joinButton = findViewById<Button>(R.id.button_join_room)

        if (TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_BAN)) {
            joinButton.visibility = View.INVISIBLE
        } else {
            joinButton.text = getString(R.string.rejoin)

            joinButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    showWaitingView()
                    mxSession!!.joinRoom(currentRoom!!.roomId, object : ApiCallback<String> {
                        override fun onSuccess(roomId: String) {
                            hideWaitingView()

                            val params = HashMap<String, Any>()

                            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession!!.myUserId
                            params[VectorRoomActivity.EXTRA_ROOM_ID] = currentRoom!!.roomId

                            // clear the activity stack to home activity
                            val intent = Intent(this@RoomActivity, VectorHomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, params as HashMap<*, *>)
                            startActivity(intent)
                        }

                        private fun onError(errorMessage: String) {
                            Log.d(LOG_TAG, "re join failed $errorMessage")
                            Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            hideWaitingView()
                        }

                        override fun onNetworkError(e: Exception) {
                            onError(e.localizedMessage)
                        }

                        override fun onMatrixError(e: MatrixError) {
                            if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                                hideWaitingView()
                                consentNotGivenHelper.displayDialog(e)
                            } else {
                                onError(e.localizedMessage)
                            }
                        }

                        override fun onUnexpectedError(e: Exception) {
                            onError(e.localizedMessage)
                        }
                    })
                }
            })
        }

        val forgetRoomButton = findViewById<Button>(R.id.button_decline)
        forgetRoomButton.text = getString(R.string.forget_room)

        forgetRoomButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                currentRoom!!.forget(object : ApiCallback<Void> {
                    override fun onSuccess(info: Void) {
                        finish()
                    }

                    private fun onError(errorMessage: String) {
                        Log.d(LOG_TAG, "forget failed $errorMessage")
                        Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        hideWaitingView()
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
        })

        enableActionBarHeader(SHOW_ACTION_BAR_HEADER)
    }

    //================================================================================
    // Room preview management
    //================================================================================

    override fun getRoomPreviewData(): RoomPreviewData? {
        return sRoomPreviewData
    }

    /**
     * Manage the room preview buttons area
     */
    private fun manageRoomPreview() {
        if (null != sRoomPreviewData) {
            mRoomPreviewLayout!!.visibility = View.VISIBLE

            val joinButton = findViewById<Button>(R.id.button_join_room)
            val declineButton = findViewById<Button>(R.id.button_decline)

            val roomEmailInvitation = sRoomPreviewData!!.roomEmailInvitation

            var roomName = sRoomPreviewData!!.roomName
            if (TextUtils.isEmpty(roomName)) {
                roomName = " "
            }

            Log.d(LOG_TAG, "Preview the room " + sRoomPreviewData!!.roomId)


            // if the room already exists
            if (null != currentRoom) {
                Log.d(LOG_TAG, "manageRoomPreview : The room is known")

                var inviter = ""

                if (null != roomEmailInvitation) {
                    inviter = roomEmailInvitation!!.inviterName
                }

                if (TextUtils.isEmpty(inviter)) {
                    currentRoom!!.getActiveMembersAsync(object : SimpleApiCallback<List<RoomMember>>(this) {
                        override fun onSuccess(members: List<RoomMember>) {
                            var inviter = ""

                            for (member in members) {
                                if (TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_JOIN)) {
                                    inviter = if (TextUtils.isEmpty(member.displayname)) member.userId else member.displayname
                                }
                            }

                            invitationTextView!!.text = getString(R.string.room_preview_invitation_format, inviter)
                        }
                    })
                } else {
                    invitationTextView!!.text = getString(R.string.room_preview_invitation_format, inviter)
                }

                declineButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        Log.d(LOG_TAG, "The user clicked on decline.")

                        showWaitingView()

                        currentRoom!!.leave(object : ApiCallback<Void> {
                            override fun onSuccess(info: Void) {
                                Log.d(LOG_TAG, "The invitation is rejected")
                                onDeclined()
                            }

                            private fun onError(errorMessage: String) {
                                Log.d(LOG_TAG, "The invitation rejection failed $errorMessage")
                                Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                hideWaitingView()
                            }

                            override fun onNetworkError(e: Exception) {
                                onError(e.localizedMessage)
                            }

                            override fun onMatrixError(e: MatrixError) {
                                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                                    hideWaitingView()
                                    consentNotGivenHelper.displayDialog(e)
                                } else {
                                    onError(e.localizedMessage)
                                }
                            }

                            override fun onUnexpectedError(e: Exception) {
                                onError(e.localizedMessage)
                            }
                        })
                    }
                })

            } else {
                if ((null != roomEmailInvitation) && !TextUtils.isEmpty(roomEmailInvitation!!.email)) {
                    invitationTextView!!.text = getString(R.string.room_preview_invitation_format, roomEmailInvitation!!.inviterName)
                    subInvitationTextView!!.text = getString(R.string.room_preview_unlinked_email_warning, roomEmailInvitation!!.email)
                } else {
                    invitationTextView!!.text = getString(R.string.room_preview_try_join_an_unknown_room,
                            if (TextUtils.isEmpty(sRoomPreviewData!!.roomName)) getString(R.string.room_preview_try_join_an_unknown_room_default) else roomName)

                    // the room preview has some messages
                    if ((null != sRoomPreviewData!!.roomResponse) && (null != sRoomPreviewData!!.roomResponse.messages)) {
                        subInvitationTextView!!.text = getString(R.string.room_preview_room_interactions_disabled)
                    }
                }

                declineButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        Log.d(LOG_TAG, "The invitation is declined (unknown room)")

                        sRoomPreviewData = null
                        finish()
                    }
                })
            }

            joinButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    Log.d(LOG_TAG, "The user clicked on Join.")

                    if (null != sRoomPreviewData) {
                        val room = sRoomPreviewData!!.session.dataHandler.getRoom(sRoomPreviewData!!.roomId)

                        var signUrl: String? = null

                        if (null != roomEmailInvitation) {
                            signUrl = roomEmailInvitation!!.signUrl
                        }

                        showWaitingView()

                        room.joinWithThirdPartySigned(sRoomPreviewData!!.roomIdOrAlias, signUrl, object : ApiCallback<Void> {
                            override fun onSuccess(info: Void) {
                                onJoined()
                            }

                            private fun onError(errorMessage: String) {
                                Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                hideWaitingView()
                            }

                            override fun onNetworkError(e: Exception) {
                                onError(e.localizedMessage)
                            }

                            override fun onMatrixError(e: MatrixError) {
                                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                                    hideWaitingView()
                                    consentNotGivenHelper.displayDialog(e)
                                } else {
                                    onError(e.localizedMessage)
                                }
                            }

                            override fun onUnexpectedError(e: Exception) {
                                onError(e.localizedMessage)
                            }
                        })
                    } else {
                        finish()
                    }
                }
            })

            enableActionBarHeader(SHOW_ACTION_BAR_HEADER)

        } else {
            mRoomPreviewLayout!!.visibility = View.GONE
        }
    }

    /**
     * The room invitation has been declined
     */
    private fun onDeclined() {
        if (null != sRoomPreviewData) {
            finish()
            sRoomPreviewData = null
        }
    }

    /**
     * the room has been joined
     */
    private fun onJoined() {
        if (null != sRoomPreviewData) {
            val params = HashMap<String, Any>()

            processDirectMessageRoom()

            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession!!.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = sRoomPreviewData!!.roomId

            if (null != sRoomPreviewData!!.eventId) {
                params[VectorRoomActivity.EXTRA_EVENT_ID] = sRoomPreviewData!!.eventId
            }

            // clear the activity stack to home activity
            val intent = Intent(this, VectorHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, params as HashMap<*, *>)
            startActivity(intent)

            sRoomPreviewData = null
        }
    }

    /**
     * If the joined room was tagged as "direct chat room", it is required to update the
     * room as a "direct chat room" (account_data)
     */
    private fun processDirectMessageRoom() {
        val room = sRoomPreviewData!!.session.dataHandler.getRoom(sRoomPreviewData!!.roomId)
        if ((null != room) && (room!!.isDirectChatInvitation)) {
            if (currentRoom == null) {
                return
            }

            if (currentRoom!!.getNumberOfMembers() == 2) {
                // test if room is already seen as "direct message"
                if (!RoomUtils.isDirectChat(mxSession, sRoomPreviewData!!.roomId)) {
                    currentRoom!!.getMembersAsync(object : SimpleApiCallback<List<RoomMember>>(this) {
                        override fun onSuccess(members: List<RoomMember>) {
                            val myUserId = mxSession!!.myUserId
                            val participantUserId: String

                            for (member in members) {
                                // search for the second participant
                                if (member.userId != myUserId) {
                                    participantUserId = member.userId
                                    CommonActivityUtils.setToggleDirectMessageRoom(mxSession,
                                            sRoomPreviewData!!.roomId, participantUserId, this@RoomActivity, mDirectMessageListener)
                                    break
                                }
                            }
                        }
                    })
                } else {
                    Log.d(LOG_TAG, "## processDirectMessageRoom(): attempt to add an already direct message room")
                }
            }
        }
    }

    //================================================================================
    // Room header clicks management.
    //================================================================================

    /**
     * Invite a user from the data provided by the invite activity.
     *
     * @param aData the provider date
     */
    private fun onActivityResultRoomInvite(aData: Intent) {
        val userIds = aData.getSerializableExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS) as List<String>

        if (currentRoom != null && (null != userIds) && (userIds!!.size > 0)) {
            showWaitingView()

            currentRoom!!.invite(mxSession,userIds, object : ApiCallback<Void?> {

                private fun onDone(errorMessage: String?) {
                    if (!TextUtils.isEmpty(errorMessage)) {
                        Toast.makeText(this@RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    hideWaitingView()
                }

                override fun onSuccess(info: Void?) {
                    onDone(null)
                }

                override fun onNetworkError(e: Exception) {
                    onDone(e.message)
                }

                override fun onMatrixError(e: MatrixError) {
                    onDone(e.message)
                }

                override fun onUnexpectedError(e: Exception) {
                    onDone(e.message)
                }
            })
        }
    }

    /**
     * The user clicks on the room title.
     * Assume he wants to update it.
     */
    private fun onRoomTitleClick() {
        if (currentRoom == null) {
            return
        }

        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_base_edit_text, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder
                .setTitle(R.string.room_info_room_name)
                .setView(dialogView)

        val textInput = dialogView.findViewById<EditText>(R.id.edit_text)
        textInput.setText(currentRoom!!.state.name)

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, id: Int) {
                                showWaitingView()

                                currentRoom!!.updateName(textInput.text.toString(), object : ApiCallback<Void> {

                                    private fun onDone(message: String?) {
                                        if (!TextUtils.isEmpty(message)) {
                                            Toast.makeText(this@RoomActivity, message, Toast.LENGTH_SHORT).show()
                                        }

                                        hideWaitingView()
                                        updateActionBarTitleAndTopic()
                                    }

                                    override fun onSuccess(info: Void) {
                                        onDone(null)
                                    }

                                    override fun onNetworkError(e: Exception) {
                                        onDone(e.localizedMessage)
                                    }

                                    override fun onMatrixError(e: MatrixError) {
                                        onDone(e.localizedMessage)
                                    }

                                    override fun onUnexpectedError(e: Exception) {
                                        onDone(e.localizedMessage)
                                    }
                                })
                            }
                        })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    /**
     * The user clicks on the room topic.
     * Assume he wants to update it.
     */
    private fun onRoomTopicClick() {
        if (currentRoom == null) {
            return
        }

        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_base_edit_text, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder
                .setTitle(R.string.room_info_room_topic)
                .setView(dialogView)

        val textInput = dialogView.findViewById<EditText>(R.id.edit_text)
        textInput.setText(currentRoom!!.state.topic)

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, id: Int) {
                                showWaitingView()

                                currentRoom!!.updateTopic(textInput.text.toString(), object : ApiCallback<Void> {

                                    private fun onDone(message: String?) {
                                        if (!TextUtils.isEmpty(message)) {
                                            Toast.makeText(this@RoomActivity, message, Toast.LENGTH_SHORT).show()
                                        }

                                        hideWaitingView()
                                        updateActionBarTitleAndTopic()
                                    }

                                    override fun onSuccess(info: Void) {
                                        onDone(null)
                                    }

                                    override fun onNetworkError(e: Exception) {
                                        onDone(e.localizedMessage)
                                    }

                                    override fun onMatrixError(e: MatrixError) {
                                        onDone(e.localizedMessage)
                                    }

                                    override fun onUnexpectedError(e: Exception) {
                                        onDone(e.localizedMessage)
                                    }
                                })
                            }
                        })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    //    @OnClick(R.id.room_header_avatar)
    internal fun onRoomAvatarClick() {
        if (currentRoom != null && !TextUtils.isEmpty(currentRoom!!.getAvatarUrl()))
        // Display the avatar in fullscreen with animation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                val options = ActivityOptions.makeSceneTransitionAnimation(this, mActionBarHeaderRoomAvatar, "vector_transition_avatar")
//                startActivity(VectorAvatarViewerActivity.getIntent(this, mxSession!!.myUserId, currentRoom!!.getAvatarUrl()!!), options.toBundle())
            } else {
                // No transition
                startActivity(VectorAvatarViewerActivity.getIntent(this, mxSession!!.myUserId, currentRoom!!.getAvatarUrl()!!))
            }
    }

    //    @OnClick(R.id.action_bar_header_room_title)
    internal fun onRoomHeaderTitleClick() {
        // sanity checks : reported by GA
        if ((null != currentRoom) && (null != currentRoom!!.state)) {
            var canUpdateTitle = false
            val powerLevels = currentRoom!!.state.getPowerLevels()

            if (null != powerLevels) {
                val powerLevel = powerLevels!!.getUserPowerLevel(mxSession!!.myUserId)
                canUpdateTitle = powerLevel >= powerLevels!!.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_NAME)
            }

            if (canUpdateTitle) {
                onRoomTitleClick()
            } else {
                launchRoomDetails(VectorRoomDetailsActivity.SETTINGS_TAB_INDEX)
            }
        }
    }

    //    @OnClick(R.id.action_bar_header_room_topic)
    internal fun onRoomHeaderTopicClick() {
        // sanity checks : reported by GA
        if ((null != currentRoom) && (null != currentRoom!!.state)) {
            var canUpdateTopic = false
            val powerLevels = currentRoom!!.state.getPowerLevels()

            if (null != powerLevels) {
                val powerLevel = powerLevels!!.getUserPowerLevel(mxSession!!.myUserId)
                canUpdateTopic = powerLevel >= powerLevels!!.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_NAME)
            }

            if (canUpdateTopic) {
                onRoomTopicClick()
            } else {
                launchRoomDetails(VectorRoomDetailsActivity.SETTINGS_TAB_INDEX)
            }
        }
    }

    //    @OnClick(R.id.action_bar_header_room_members_settings_view)
    internal fun onRoomMembersSettingClick() {
        launchRoomDetails(VectorRoomDetailsActivity.PEOPLE_TAB_INDEX)
    }

    //    @OnClick(R.id.action_bar_header_room_members_text_view)
    internal fun onRoomMembersClick() {
        launchRoomDetails(VectorRoomDetailsActivity.PEOPLE_TAB_INDEX)
    }

    //    @OnClick(R.id.action_bar_header_room_members_invite_view)
    internal fun onRoomMemberInviteClick() {
        launchInvitePeople()
    }

    /* ==========================================================================================
     * Interface VectorReadReceiptsDialogFragmentListener
     * ========================================================================================== */

    override fun onMemberClicked(userId: String) {
        if (currentRoom != null) {
            val vectorMemberDetailIntent = Intent(this, VectorMemberDetailsActivity::class.java)
            vectorMemberDetailIntent.putExtra(VectorMemberDetailsActivity.EXTRA_ROOM_ID, currentRoom!!.roomId)
            vectorMemberDetailIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, userId)
            vectorMemberDetailIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MATRIX_ID, mxSession!!.getCredentials().userId)
            startActivityForResult(vectorMemberDetailIntent, VectorRoomActivity.GET_MENTION_REQUEST_CODE)
        }
    }

    /* ==========================================================================================
     * UI Event
     * ========================================================================================== */

    //    @OnClick(R.id.editText_messageBox)
    internal fun onEditTextClick() {
        // hide the header room as soon as the message input text area is touched
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)
    }

    private fun chooseMediaSource(useNativeCamera: Boolean, isVoiceFeatureEnabled: Boolean) {
        // hide the header room
        enableActionBarHeader(HIDE_ACTION_BAR_HEADER)

        val items = ArrayList<DialogListItem>()

        // Send file
        items.add(DialogListItem.SendFile)

        // Send voice
        if (isVoiceFeatureEnabled) {
            items.add(DialogListItem.SendVoice)
        }

        // Send sticker
        items.add(DialogListItem.SendSticker)

        // Camera
        if (useNativeCamera) {
            items.add(DialogListItem.TakePhoto)
            items.add(DialogListItem.TakeVideo)
        } else {
            items.add(DialogListItem.TakePhotoVideo)
        }

        AlertDialog.Builder(this)
                .setAdapter(DialogSendItemAdapter(this, items), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        onSendChoiceClicked(items[which])
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    //    @OnClick(R.id.room_send_image_view)
    internal fun onSendClick() {
        if (!TextUtils.isEmpty(mEditText!!.text) && !PreferencesManager.sendMessageWithEnter(this)) {
            sendTextMessage(mEditText.text.toString())
        } else {
            val useNativeCamera = PreferencesManager.useNativeCamera(this)
            val isVoiceFeatureEnabled = PreferencesManager.isSendVoiceFeatureEnabled(this)

            when (PreferencesManager.getSelectedDefaultMediaSource(this)) {
                MEDIA_SOURCE_FILE -> {
                    onSendChoiceClicked(DialogListItem.SendFile)
                    return
                }
                MEDIA_SOURCE_VOICE -> if (isVoiceFeatureEnabled) {
                    onSendChoiceClicked(DialogListItem.SendVoice)
                    return
                }
                MEDIA_SOURCE_STICKER -> {
                    onSendChoiceClicked(DialogListItem.SendSticker)
                    return
                }
                MEDIA_SOURCE_PHOTO -> if (useNativeCamera) {
                    onSendChoiceClicked(DialogListItem.TakePhoto)
                    return
                } else {
                    onSendChoiceClicked(DialogListItem.TakePhotoVideo)
                    return
                }
                MEDIA_SOURCE_VIDEO -> if (useNativeCamera) {
                    onSendChoiceClicked(DialogListItem.TakeVideo)
                    return
                } else {
                    onSendChoiceClicked(DialogListItem.TakePhotoVideo)
                    return
                }
            }// show all options if voice feature is disabled

            chooseMediaSource(useNativeCamera, isVoiceFeatureEnabled)
        }
    }

    @OnClick(R.id.room_action_bar_title)
    internal fun onClickRoomTitle() {
        val intent = Intent(this@RoomActivity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, currentRoom?.roomId);
        startActivityForResult(intent, WAITING_INFORMATION_ACTIVITY);
    }

    @OnClick(R.id.button_send)
    internal fun onClickSend() {
        imageViewCancelEdit.visibility = View.GONE;
        if (!isEditedMode)
            sendTextMessage(mEditText.text.toString());
        else
            editTextMessage();
    }

    @OnClick(R.id.imgFile)
    internal fun onClickSendFile() {

        val items = ArrayList<DialogListItem>()

        // Send file
        onSendChoiceClicked(DialogListItem.SendFile)

//        if (!TextUtils.isEmpty(mEditText.text) && !PreferencesManager.sendMessageWithEnter(this)) {
//            sendTextMessage()
//        } else {
//            val useNativeCamera = PreferencesManager.useNativeCamera(this)
//            val isVoiceFeatureEnabled = PreferencesManager.isSendVoiceFeatureEnabled(this)
//
//            when (PreferencesManager.getSelectedDefaultMediaSource(this)) {
//                MEDIA_SOURCE_FILE -> {
//                    onSendChoiceClicked(DialogListItem.SendFile)
//                    return
//                }
//                MEDIA_SOURCE_VOICE -> if (isVoiceFeatureEnabled) {
//                    onSendChoiceClicked(DialogListItem.SendVoice)
//                    return
//                }
//                MEDIA_SOURCE_STICKER -> {
//                    onSendChoiceClicked(DialogListItem.SendSticker)
//                    return
//                }
//                MEDIA_SOURCE_PHOTO -> if (useNativeCamera) {
//                    onSendChoiceClicked(DialogListItem.TakePhoto)
//                    return
//                } else {
//                    onSendChoiceClicked(DialogListItem.TakePhotoVideo)
//                    return
//                }
//                MEDIA_SOURCE_VIDEO -> if (useNativeCamera) {
//                    onSendChoiceClicked(DialogListItem.TakeVideo)
//                    return
//                } else {
//                    onSendChoiceClicked(DialogListItem.TakePhotoVideo)
//                    return
//                }
//            }// show all options if voice feature is disabled
//
//            chooseMediaSource(useNativeCamera, isVoiceFeatureEnabled)
//        }
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.button_special_symbol)
    internal fun onClickSpecialSymbol() {
        mEditText.setText(mEditText.text.toString() + "@");
        mEditText.setSelection(mEditText.text.length);
    }

    @OnClick(R.id.image_view_send_image)
    internal fun onClickSendImage() {
        if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                        this@RoomActivity, PERMISSION_REQUEST_CODE_LAUNCH_CAMERA)) {
            launchCamera()
        }
    }

    //    @OnLongClick(R.id.room_send_image_view)
    internal fun onLongClick(): Boolean {
        if (!TextUtils.isEmpty(mEditText!!.text) && !PreferencesManager.sendMessageWithEnter(this)) {
            return false
        }
        val useNativeCamera = PreferencesManager.useNativeCamera(this)
        val isVoiceFeatureEnabled = PreferencesManager.isSendVoiceFeatureEnabled(this)
        chooseMediaSource(useNativeCamera, isVoiceFeatureEnabled)

        return true
    }

    private fun onSendChoiceClicked(dialogListItem: DialogListItem) {
        if (dialogListItem is DialogListItem.SendFile) {
            launchFileSelectionIntent()
        } else if (dialogListItem is DialogListItem.SendVoice) {
            launchAudioRecorderIntent()
        } else if (dialogListItem is DialogListItem.SendSticker) {
            startStickerPickerActivity()
        } else if (dialogListItem is DialogListItem.TakePhotoVideo) {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                            this@RoomActivity, PERMISSION_REQUEST_CODE_LAUNCH_CAMERA)) {
                launchCamera()
            }
        } else if (dialogListItem is DialogListItem.TakePhoto) {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                            this@RoomActivity, PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA)) {
                launchNativeCamera()
            }
        } else if (dialogListItem is DialogListItem.TakeVideo) {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                            this@RoomActivity, PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA)) {
                launchNativeVideoRecorder()
            }
        }
    }

    @OnClick(R.id.room_pending_call_view)
    internal fun onPendingCallClick() {
        val call = CallsManager.getSharedInstance().activeCall
        if (null != call) {
            val intent = Intent(this, OutgoingCallActivity::class.java)
            intent.putExtra(CallViewActivity.EXTRA_MATRIX_ID, call!!.session.credentials.userId)
            intent.putExtra(CallViewActivity.EXTRA_CALL_ID, call!!.callId)
            startActivity(intent)
        } else {
            // if the call is no more active, just remove the view
            mVectorPendingCallView!!.onCallTerminated()
        }
    }

    // notifications area
    // increase the clickable area to open the keyboard.
    // when there is no text, it is quite small and some user thought the edition was disabled.
    @OnClick(R.id.room_sending_message_layout)
    internal fun onSendingMessageLayoutClick() {
        if (mEditText!!.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

//    @OnClick(R.id.room_start_call_image_view)
//    internal fun onStartCallClick() {
//        if ((null != currentRoom) && currentRoom!!.isEncrypted() && (currentRoom!!.getNumberOfMembers() > 2)) {
//            // display the dialog with the info text
//            AlertDialog.Builder(this)
//                    .setMessage(R.string.room_no_conference_call_in_encrypted_rooms)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton(R.string.ok, null)
//                    .show()
//        } else if (isUserAllowedToStartConfCall()) {
//            if (currentRoom!!.getNumberOfMembers() > 2) {
//                AlertDialog.Builder(this)
//                        .setTitle(R.string.conference_call_warning_title)
//                        .setMessage(R.string.conference_call_warning_message)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
//                            override fun onClick(dialog: DialogInterface, which: Int) {
//                                if (PreferencesManager.useJitsiConfCall(this@RoomActivity)) {
//                                    startJitsiCall(true)
//                                } else {
//                                    displayVideoCallIpDialog()
//                                }
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, null)
//                        .show()
//            } else {
//                displayVideoCallIpDialog()
//            }
//        } else {
//            displayConfCallNotAllowed()
//        }
//    }

    @OnClick(R.id.image_view_video_call)
    internal fun onStartVideoCall() {
        onCallItemClicked(1);
    }

    @OnClick(R.id.image_view_voice_call)
    internal fun onStartVoiceCall() {
        onCallItemClicked(0);
    }

    @OnClick(R.id.image_view_search)
    internal fun onRoomSearch() {
        val intentSearch = Intent(this, UnifiedSearchActivity::class.java);
        intentSearch.putExtra(UnifiedSearchActivity.EXTRA_ROOM_ID, currentRoom?.roomId);
        startActivity(intentSearch);
    }

    @OnClick(R.id.room_end_call_image_view)
    internal fun onStopCallClick() {
        CallsManager.getSharedInstance().onHangUp(null)
    }

    @OnClick(R.id.room_button_margin_right)
    internal fun onMarginRightClick() {
        // extend the right side of right button
        // to avoid clicking in the void
        if (mStopCallLayout!!.visibility == View.VISIBLE) {
            mStopCallLayout!!.performClick()
        }
//        else if (mStartCallLayout!!.visibility == View.VISIBLE) {
//            mStartCallLayout!!.performClick()
//        }
        else if (mSendImageView!!.visibility == View.VISIBLE) {
            mSendImageView!!.performClick()
        }
    }
}
