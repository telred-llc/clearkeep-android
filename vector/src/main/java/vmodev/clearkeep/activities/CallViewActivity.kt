package vmodev.clearkeep.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import im.vector.Matrix
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorAppCompatActivity
import im.vector.activity.VectorCallViewActivity
import im.vector.settings.VectorLocale
import im.vector.ui.themes.ActivityOtherThemes
import im.vector.util.*
import im.vector.view.VectorPendingCallView
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import vmodev.clearkeep.applications.IApplication
import java.util.*

class CallViewActivity : VectorAppCompatActivity(), SensorEventListener {

    private val LOG_TAG = VectorCallViewActivity::class.java.simpleName

    companion object {
        const val EXTRA_MATRIX_ID = "CallViewActivity.EXTRA_MATRIX_ID"
        const val EXTRA_CALL_ID = "CallViewActivity.EXTRA_CALL_ID"
        const val EXTRA_UNKNOWN_DEVICES = "CallViewActivity.EXTRA_UNKNOWN_DEVICES"
    }

    private val EXTRA_LOCAL_FRAME_LAYOUT = "EXTRA_LOCAL_FRAME_LAYOUT"

    private var mCallView: View? = null

    // account info
    private var mMatrixId: String? = null
    private var mSession: MXSession? = null

    // graphical items
    private var mHangUpImageView: ImageView? = null
    private var mSpeakerSelectionView: ImageView? = null
    private var mAvatarView: ImageView? = null
    private var mMuteMicImageView: ImageView? = null
    private var mSwitchRearFrontCameraImageView: ImageView? = null
    private var mMuteLocalCameraView: ImageView? = null
    private var mHeaderPendingCallView: VectorPendingCallView? = null
    private var mButtonsContainerView: View? = null

    private var mIncomingCallTabbar: View? = null
    private var mAcceptIncomingCallButton: View? = null

    // video screen management
    private var mVideoFadingEdgesTimer: Timer? = null
    private var mVideoFadingEdgesTimerTask: TimerTask? = null
    private val FADE_IN_DURATION = 250
    private val FADE_OUT_DURATION = 2000
    private val VIDEO_FADING_TIMER = 5000

    // video display size
    private var mLocalVideoLayoutConfig: VideoLayoutConfiguration? = null

    // true when the user moves the preview
    private var mIsCustomLocalVideoLayoutConfig: Boolean = false

    // hard coded values are taken from specs:
    // - 585 as screen height reference
    // - 18 as space between the local video and the container view containing the setting buttons
    //private static final float RATIO_TOP_MARGIN_LOCAL_USER_VIDEO = (float)(462.0/585.0);
    private val VIDEO_TO_BUTTONS_VERTICAL_SPACE = (18.0 / 585.0).toFloat()
    /**
     * local user video height is set as percent of the total screen height
     */
    private val PERCENT_LOCAL_USER_VIDEO_SIZE = 25

    private var mSourceVideoWidth = 0
    private var mSourceVideoHeight = 0

    // sensor
    private var mSensorMgr: SensorManager? = null
    private var mProximitySensor: Sensor? = null

    private var mCall: IMXCall? = null
    private var mCallsManager: CallsManager? = null

    // on Samsung devices, the application is suspended when the screen is turned off
    // so the call must not be suspended
    private var mIsScreenOff = false
    private val mListener = object : MXCallListener() {
        override fun onStateDidChange(state: String?) {
//            runOnUiThread {
//                Log.d(LOG_TAG, "## onStateDidChange(): new state=" + state!!)
//
//                manageSubViews()
//
//                if (null != mCall && mCall!!.isVideo && mCall!!.callState == IMXCall.CALL_STATE_CONNECTED) {
////                    mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)
//                }
//            }
        }

        override fun onCallViewCreated(callView: View?) {
            android.util.Log.d("CallView", "Created call view")
            Log.d(LOG_TAG, "## onViewLoading():")
            mCallView = callView
            insertCallView()
        }

        override fun onReady() {
            // update UI before displaying the video
//            computeVideoUiLayout()
            if (!mCall!!.isIncoming) {
                Log.d(LOG_TAG, "## onReady(): placeCall()")
                mCall!!.placeCall(mLocalVideoLayoutConfig)
            } else {
                Log.d(LOG_TAG, "## onReady(): launchIncomingCall()")
                mCall!!.launchIncomingCall(mLocalVideoLayoutConfig)
            }
        }

        override fun onPreviewSizeChanged(width: Int, height: Int) {
//            Log.d(LOG_TAG, "## onPreviewSizeChanged : $width * $height")
//
//            mSourceVideoWidth = width
//            mSourceVideoHeight = height
//
//            if (null != mCall && mCall!!.isVideo && mCall!!.callState == IMXCall.CALL_STATE_CONNECTED) {
//                computeVideoUiLayout()
//                mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)
//            }
        }
    }

    // track the audio config updates
    private val mAudioConfigListener = CallSoundsManager.OnAudioConfigurationUpdateListener {
        refreshSpeakerButton()
        refreshMuteMicButton()
    }

    // to drag the local video preview
    private val mMainViewTouchListener = object : View.OnTouchListener {

        // fields
        private var mPreviewRect: Rect? = null
        private var mStartX = 0
        private var mStartY = 0

        /**
         * @return the local preview rect in pixels
         */
        private fun computePreviewRect(): Rect {
            // get the height of the screen
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val screenHeight = metrics.heightPixels
            val screenWidth = metrics.widthPixels

            val left = mLocalVideoLayoutConfig!!.mX * screenWidth / 100
            val right = (mLocalVideoLayoutConfig!!.mX + mLocalVideoLayoutConfig!!.mWidth) * screenWidth / 100
            val top = mLocalVideoLayoutConfig!!.mY * screenHeight / 100
            val bottom = (mLocalVideoLayoutConfig!!.mY + mLocalVideoLayoutConfig!!.mHeight) * screenHeight / 100

            return Rect(left, top, right, bottom)
        }

        private fun updatePreviewFrame(deltaX: Int, deltaY: Int) {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val screenHeight = metrics.heightPixels
            val screenWidth = metrics.widthPixels
            val width = mPreviewRect!!.width()
            val height = mPreviewRect!!.height()

            // top left
            mPreviewRect!!.left = Math.max(0, mPreviewRect!!.left + deltaX)
            mPreviewRect!!.right = mPreviewRect!!.left + width
            mPreviewRect!!.top = Math.max(0, mPreviewRect!!.top + deltaY)
            mPreviewRect!!.bottom = mPreviewRect!!.top + height

            // right margin
            if (mPreviewRect!!.right > screenWidth) {
                mPreviewRect!!.right = screenWidth
                mPreviewRect!!.left = mPreviewRect!!.right - width
            }

            if (mPreviewRect!!.bottom > screenHeight) {
                mPreviewRect!!.bottom = screenHeight
                mPreviewRect!!.top = screenHeight - height
            }

            mLocalVideoLayoutConfig!!.mX = mPreviewRect!!.left * 100 / screenWidth
            mLocalVideoLayoutConfig!!.mY = mPreviewRect!!.top * 100 / screenHeight
            mLocalVideoLayoutConfig!!.mDisplayWidth = screenWidth
            mLocalVideoLayoutConfig!!.mDisplayHeight = screenHeight

            mIsCustomLocalVideoLayoutConfig = true
            android.util.Log.d("UpdatePreview", mLocalVideoLayoutConfig!!.mDisplayHeight.toString())
            mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            // call management
            if (null != mCall && mCall!!.isVideo && TextUtils.equals(IMXCall.CALL_STATE_CONNECTED, mCall!!.callState)) {
                val action = event.action
                val x = event.x.toInt()
                val y = event.y.toInt()

                if (action == MotionEvent.ACTION_DOWN) {
                    val rect = computePreviewRect()

                    if (rect.contains(x, y)) {
                        mPreviewRect = rect
                        mStartX = x
                        mStartY = y
                        return true
                    }
                } else if (null != mPreviewRect && action == MotionEvent.ACTION_MOVE) {
                    updatePreviewFrame(x - mStartX, y - mStartY)
                    mStartX = x
                    mStartY = y
                    return true
                } else {
                    mPreviewRect = null
                }
            }
            return false
        }
    }

    /**
     * Insert the callView in the activity (above the other room member).
     * The callView is setup in the SDK, and provided via dispatchOnViewLoading() in [.mListener].
     */
    private fun insertCallView() {
        if (null != mCallView) {
            // insert the call view above the avatar
            val layout = findViewById<RelativeLayout>(R.id.call_layout)
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layout.removeView(mCallView)
            layout.visibility = View.VISIBLE

            // add the call view only is the call is a video one
            if (mCall!!.isVideo) {
                // reported by a rageshake
                if (null != mCallView!!.parent) {
                    (mCallView!!.parent as ViewGroup).removeView(mCallView)
                }
                layout.addView(mCallView, 1, params)
            }
            // init as GONE, will be displayed according to call states..
            mCall!!.visibility = View.GONE
        }
    }

    override fun getOtherThemes(): ActivityOtherThemes {
        return ActivityOtherThemes.Call
    }

    private lateinit var application: IApplication;
    override fun getLayoutRes(): Int {
        application = applicationContext as IApplication;
        setTheme(application.getCurrentTheme());
        return R.layout.activity_call_view
    }

    override fun initUiAndData() {
        val intent = intent
        if (intent ==
                null) {
            Log.e(LOG_TAG, "Need an intent to view.")
            finish()
            return
        }

        if (!intent.hasExtra(EXTRA_MATRIX_ID)) {
            Log.e(LOG_TAG, "No matrix ID extra.")
            finish()
            return
        }

        val callId = intent.getStringExtra(EXTRA_CALL_ID)
        mMatrixId = intent.getStringExtra(EXTRA_MATRIX_ID)

        mSession = Matrix.getInstance(applicationContext)!!.getSession(mMatrixId)
        if (null == mSession || !mSession!!.isAlive) {
            Log.e(LOG_TAG, "invalid session")
            finish()
            return
        }

        mCall = CallsManager.getSharedInstance().activeCall

        if (null == mCall || !TextUtils.equals(mCall!!.callId, callId)) {
            Log.e(LOG_TAG, "invalid call")
            finish()
            return
        }


        mCallsManager = CallsManager.getSharedInstance()

        // UI binding
        mHangUpImageView = findViewById(R.id.hang_up_button)
        mSpeakerSelectionView = findViewById(R.id.call_speaker_view)
        mAvatarView = findViewById(R.id.call_other_member)
        mMuteMicImageView = findViewById(R.id.mute_audio)
        mHeaderPendingCallView = findViewById(R.id.header_pending_callview)
        mSwitchRearFrontCameraImageView = findViewById(R.id.call_switch_camera_view)
        mMuteLocalCameraView = findViewById(R.id.mute_local_camera)
        mButtonsContainerView = findViewById(R.id.call_menu_buttons_layout_container)
        mIncomingCallTabbar = findViewById(R.id.incoming_call_menu_buttons_layout_container)
        mAcceptIncomingCallButton = findViewById(R.id.accept_incoming_call)
        val rejectIncomingCallButton = findViewById<View>(R.id.reject_incoming_call)

        val mainContainerLayoutView = findViewById<View>(R.id.call_layout)

        // when video is in full screen, touching the screen restore the edges (fade in)
        if (mCall!!.isVideo) {
            mainContainerLayoutView.setOnClickListener {
                //            fadeInVideoEdge()
//            startVideoFadingEdgesScreenTimer()
                if (mButtonsContainerView!!.visibility == View.GONE && mIncomingCallTabbar!!.visibility == View.GONE)
                    mButtonsContainerView!!.visibility = View.VISIBLE
                else
                    mButtonsContainerView!!.visibility = View.GONE
            }
        }

        mainContainerLayoutView.setOnTouchListener(mMainViewTouchListener)

        val roomLinkImageView = findViewById<ImageView>(R.id.room_chat_link)
        roomLinkImageView.setOnClickListener {
            // simulate a back button press
            finish()
            startRoomActivity()
        }

        mSwitchRearFrontCameraImageView!!.setOnClickListener {
            toggleRearFrontCamera()
            refreshSwitchRearFrontCameraButton()
//            startVideoFadingEdgesScreenTimer()
        }

        mMuteLocalCameraView!!.setOnClickListener {
            toggleVideoMute()
            refreshMuteVideoButton()
//            startVideoFadingEdgesScreenTimer()
        }

        mMuteMicImageView!!.setOnClickListener {
            toggleMicMute()
//            startVideoFadingEdgesScreenTimer()
        }

        mHangUpImageView!!.setOnClickListener { mCallsManager!!.onHangUp(null) }

        mSpeakerSelectionView!!.setOnClickListener {
            toggleSpeaker()
//            startVideoFadingEdgesScreenTimer()
        }

        if (!isFirstCreation()) {
            mLocalVideoLayoutConfig = getSavedInstanceState().getSerializable(EXTRA_LOCAL_FRAME_LAYOUT) as VideoLayoutConfiguration

            // check if the layout is not out of bounds
            if (null != mLocalVideoLayoutConfig) {
                val isPortrait = Configuration.ORIENTATION_LANDSCAPE != resources.configuration.orientation

                // do not keep the custom layout if the device orientation has been updated
                if (mLocalVideoLayoutConfig!!.mIsPortrait != isPortrait) {
                    mLocalVideoLayoutConfig = null
                }
            }

            mIsCustomLocalVideoLayoutConfig = null != mLocalVideoLayoutConfig
        }

        // init call UI setting buttons
        manageSubViews()

        // the webview has been saved after a screen rotation
        // getParent() != null : the static value have been reused whereas it should not
        if (null != mCallsManager!!.callView && null == mCallsManager!!.callView.parent) {
            mCallView = mCallsManager!!.callView
            insertCallView()

            if (null != mCallsManager!!.videoLayoutConfiguration) {
                val isPortrait = Configuration.ORIENTATION_LANDSCAPE != resources.configuration.orientation

                // do not keep the custom layout if the device orientation has been updated
                if (mCallsManager!!.videoLayoutConfiguration.mIsPortrait == isPortrait) {
                    mLocalVideoLayoutConfig = mCallsManager!!.videoLayoutConfiguration
                    mIsCustomLocalVideoLayoutConfig = true
                }
            }
        } else {
            // create the callview asap
            runOnUiThread {
                if (null != mCall!!.callView) {
                    mCallView = mCall!!.callView

                    insertCallView()
                    computeVideoUiLayout()
                    mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)

                    // if the view is ready, launch the incoming call
                    if (TextUtils.equals(mCall!!.callState, IMXCall.CALL_STATE_READY) && mCall!!.isIncoming) {
                        mCall!!.launchIncomingCall(mLocalVideoLayoutConfig)
                    }
                } else if (!mCall!!.isIncoming && TextUtils.equals(IMXCall.CALL_STATE_CREATED, mCall!!.callState)) {
                    mCall!!.createCallView()
                }
            }
        }

        // the avatar side must be the half of the min screen side
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val side = Math.min(size.x, size.y) / 2

        val avatarLayoutParams = mAvatarView!!.layoutParams as RelativeLayout.LayoutParams
        avatarLayoutParams.height = side
        avatarLayoutParams.width = side

        mAvatarView!!.layoutParams = avatarLayoutParams

        VectorUtils.loadCallAvatar(this, mSession, mAvatarView, mCall!!.room)

        if (CallsManager.getSharedInstance().isRinging && mCall!!.isIncoming) {
            mIncomingCallTabbar!!.visibility = View.VISIBLE
            mButtonsContainerView!!.visibility = View.GONE
        } else {
            mIncomingCallTabbar!!.visibility = View.GONE
            mButtonsContainerView!!.visibility = View.VISIBLE
        }

        val permissions = if (mCall!!.isVideo) PERMISSIONS_FOR_VIDEO_IP_CALL else PERMISSIONS_FOR_AUDIO_IP_CALL
        val requestCode = if (mCall!!.isVideo) PERMISSION_REQUEST_CODE_VIDEO_CALL else PERMISSION_REQUEST_CODE_AUDIO_CALL

        // the user can only accept if the dedicated permissions are granted
        mAcceptIncomingCallButton!!.visibility = if (checkPermissions(permissions, this, requestCode)) View.VISIBLE else View.GONE
        mAcceptIncomingCallButton!!.setOnClickListener {
            Log.d(LOG_TAG, "Accept the incoming call")
            mAcceptIncomingCallButton!!.visibility = View.GONE
            mCall!!.createCallView()
        }

        rejectIncomingCallButton.setOnClickListener {
            Log.d(LOG_TAG, "Reject the incoming call")
            mCallsManager!!.rejectCall()
        }

        runOnUiThread {
            intent.getSerializableExtra(EXTRA_UNKNOWN_DEVICES)?.let { serializable ->
                CommonActivityUtils.displayUnknownDevicesDialog(mSession,
                        this@CallViewActivity,
                        serializable as MXUsersDevicesMap<MXDeviceInfo>,
                        true, null)
            }
        }

        setupHeaderPendingCallView()
        Log.d(LOG_TAG, "## onCreate(): OUT")
    }

    /**
     * Customize the header pending call view to match the video/audio call UI.
     */
    private fun setupHeaderPendingCallView() {
        if (null != mHeaderPendingCallView) {
            // set the gradient effect in the background
            val mainContainerView = mHeaderPendingCallView!!.findViewById<View>(R.id.main_view)
            mainContainerView.setBackgroundResource(R.drawable.call_header_transparent_bg)

            // remove the call icon and display the back arrow icon
            mHeaderPendingCallView!!.findViewById<View>(R.id.call_icon_container).visibility = View.GONE
            val backButtonView = mHeaderPendingCallView!!.findViewById<View>(R.id.back_icon)
            backButtonView.visibility = View.VISIBLE
            backButtonView.setOnClickListener { onBackPressed() }

            // center the text horizontally and remove any padding
            val textInfoContainerView = mHeaderPendingCallView!!.findViewById<LinearLayout>(R.id.call_info_container)
            textInfoContainerView.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
            textInfoContainerView.setPadding(0, 0, 0, 0)

            // prevent the status call to be displayed
            mHeaderPendingCallView!!.enableCallStatusDisplay(false)
        }
    }

    /**
     * Perform the required initializations for the backlight management.
     *
     *
     * For video call the backlight must be ON. For voice call the backlight must be
     * OFF when the proximity sensor fires.
     */
    private fun initBackLightManagement() {
        if (null != mCall) {
            if (mCall!!.isVideo) {
                // video call: set the backlight on
                Log.d(LOG_TAG, "## initBackLightManagement(): backlight is ON")
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // same as android:keepScreenOn="true" in layout
            } else {
                if (null == mSensorMgr && null != mCall && TextUtils.equals(mCall!!.callState, IMXCall.CALL_STATE_CONNECTED)) {

                    // voice call: use the proximity sensor
                    mSensorMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                    mProximitySensor = mSensorMgr!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
                    // listener the proximity update
                    if (null == mProximitySensor) {
                        Log.w(LOG_TAG, "## initBackLightManagement(): Warning - proximity sensor not supported")
                    } else {
                        // define the
                        mSensorMgr!!.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
                    }
                }
            }
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        CommonActivityUtils.onLowMemory(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        CommonActivityUtils.onTrimMemory(this, level)
    }


    override fun finish() {
        super.finish()
        stopProximitySensor()
    }

    override fun onStop() {
        super.onStop()

        // called when the application is put in background
        if (!mIsScreenOff) {
            stopProximitySensor()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE_VIDEO_CALL) {
            // the user can only accept if the dedicated permissions are granted
            mAcceptIncomingCallButton!!.visibility = if (onPermissionResultVideoIpCall(this, grantResults)) View.VISIBLE else View.GONE
        } else if (requestCode == PERMISSION_REQUEST_CODE_AUDIO_CALL) {
            // the user can only accept if the dedicated permissions are granted
            mAcceptIncomingCallButton!!.visibility = if (onPermissionResultAudioIpCall(this, grantResults)) View.VISIBLE else View.GONE
        }
    }

    override fun onPause() {
        super.onPause()

        // on Samsung devices, the application is suspended when the screen is turned off
        // so the call must not be suspended
        if (!mIsScreenOff) {
            if (null != mCall) {
                mCall!!.onPause()
            }
        }

        if (null != mCall) {
            mCall!!.removeListener(mListener)
        }
        saveCallView()
        CallsManager.getSharedInstance().setCallActivity(null)
        CallSoundsManager.getSharedInstance(this).removeAudioConfigurationListener(mAudioConfigListener)
    }

    override fun onResume() {
        super.onResume()

        if (null == mCallsManager!!.activeCall) {
            Log.d(LOG_TAG, "## onResume() : the call does not exist anymore")
            finish()
            return
        }

        mHeaderPendingCallView!!.checkPendingCall()

        // compute video UI layout position after rotation & apply new position
        computeVideoUiLayout()
        if (null != mCall && mCall!!.isVideo && mCall!!.callState == IMXCall.CALL_STATE_CONNECTED) {
            mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)
        }

        if (null != mCall) {
            mCall!!.addListener(mListener)

            if (!mIsScreenOff) {
                mCall!!.onResume()
            }

            mIsScreenOff = false

            val fState = mCall!!.callState

            Log.d(LOG_TAG, "## onResume(): call state=$fState")

            // restore video layout after rotation
            mCallView = mCallsManager!!.callView
            insertCallView()

            // init the call button
            manageSubViews()

            // restore the backlight management
            initBackLightManagement()
            CallsManager.getSharedInstance().setCallActivity(this)
            CallSoundsManager.getSharedInstance(this).addAudioConfigurationListener(mAudioConfigListener)
        } else {
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        computeVideoUiLayout()
        if (null != mCall && mCall!!.isVideo && mCall!!.callState == IMXCall.CALL_STATE_CONNECTED) {
            mCall!!.updateLocalVideoRendererPosition(mLocalVideoLayoutConfig)
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)

        if (mIsCustomLocalVideoLayoutConfig) {
            savedInstanceState.putSerializable(EXTRA_LOCAL_FRAME_LAYOUT, mLocalVideoLayoutConfig)
        }
    }

    //==============================================================================================================
    // user interaction
    //==============================================================================================================

    /**
     * Toggle the mute feature of the mic.
     */
    private fun toggleMicMute() {
        val callSoundsManager = CallSoundsManager.getSharedInstance(this)
        callSoundsManager.isMicrophoneMute = !callSoundsManager.isMicrophoneMute
    }

    /**
     * Toggle the mute feature of the local camera.
     */
    private fun toggleVideoMute() {
        if (null != mCall) {
            if (mCall!!.isVideo) {
                val isMuted = mCall!!.isVideoRecordingMuted
                mCall!!.muteVideoRecording(!isMuted)
                Log.w(LOG_TAG, "## toggleVideoMute(): camera record turned to " + !isMuted)
            }
        } else {
            Log.w(LOG_TAG, "## toggleVideoMute(): Failed")
        }
    }

    /**
     * Toggle the mute feature of the mic.
     */
    private fun toggleSpeaker() {
        mCallsManager!!.toggleSpeaker()
    }

    /**
     * Toggle the cameras.
     */
    private fun toggleRearFrontCamera() {
        var wasCameraSwitched = false

        if (null != mCall && mCall!!.isVideo) {
            wasCameraSwitched = mCall!!.switchRearFrontCamera()
        } else {
            Log.w(LOG_TAG, "## toggleRearFrontCamera(): Skipped")
        }
        Log.w(LOG_TAG, "## toggleRearFrontCamera(): done? $wasCameraSwitched")
    }

    /**
     * Helper method to start the room activity.
     */
    private fun startRoomActivity() {
        if (null != mCall) {
            val roomId = mCall!!.room.roomId

            if (null != VectorApp.getCurrentActivity()) {
                val params = HashMap<String, Any>()
                params[RoomActivity.EXTRA_MATRIX_ID] = mMatrixId!!
                params[RoomActivity.EXTRA_ROOM_ID] = roomId
                CommonActivityUtils.goToRoomPage(this@CallViewActivity, mSession, params)
            } else {
                val intent = Intent(applicationContext, RoomActivity::class.java)
                intent.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId)
                intent.putExtra(RoomActivity.EXTRA_MATRIX_ID, mMatrixId)
                startActivity(intent)
            }
        }
    }

    //==============================================================================================================
    // fade management
    //==============================================================================================================

    /**
     * Stop the video fading timer.
     */
    private fun stopVideoFadingEdgesScreenTimer() {
        if (null != mVideoFadingEdgesTimer) {
            mVideoFadingEdgesTimer!!.cancel()
            mVideoFadingEdgesTimer = null
            mVideoFadingEdgesTimerTask = null
        }
    }

    /**
     * Start the video fading timer.
     */
    private fun startVideoFadingEdgesScreenTimer() {
        // do not hide the overlay during a voice call
        if (null == mCall || !mCall!!.isVideo) {
            return
        }

        // stop current timer in progress
        stopVideoFadingEdgesScreenTimer()

        try {
            mVideoFadingEdgesTimer = Timer()
            mVideoFadingEdgesTimerTask = object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        stopVideoFadingEdgesScreenTimer()
//                        fadeOutVideoEdge()
                    }
                }
            }

            mVideoFadingEdgesTimer!!.schedule(mVideoFadingEdgesTimerTask, VIDEO_FADING_TIMER.toLong())
        } catch (throwable: Throwable) {
            Log.e(LOG_TAG, "## startVideoFadingEdgesScreenTimer() " + throwable.message)

            stopVideoFadingEdgesScreenTimer()
//            fadeOutVideoEdge()
        }

    }

    /**
     * Set the fading effect on the view above the UI video.
     *
     * @param aOpacity      UTILS_OPACITY_NONE to fade out, UTILS_OPACITY_FULL to fade in
     * @param aAnimDuration animation duration in milliseconds
     */
    private fun fadeVideoEdge(aOpacity: Float, aAnimDuration: Int) {
        if (null != mHeaderPendingCallView) {
            if (aOpacity != mHeaderPendingCallView!!.alpha) {
                mHeaderPendingCallView!!.animate().alpha(aOpacity).setDuration(aAnimDuration.toLong()).interpolator = AccelerateInterpolator()
            }
        }

        if (null != mButtonsContainerView) {
            if (aOpacity != mButtonsContainerView!!.alpha) {
                mButtonsContainerView!!.animate()
                        .alpha(aOpacity)
                        .setDuration(aAnimDuration.toLong())
                        .setInterpolator(AccelerateInterpolator()).setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)

                                // set to GONE after the fade out, so buttons can not not be accessed by the user
                                if (UTILS_OPACITY_NONE == aOpacity) {
                                    mButtonsContainerView!!.visibility = View.GONE
                                } else {
                                    // restore visibility after fade in
                                    mButtonsContainerView!!.visibility = View.VISIBLE
                                }
                            }
                        })
            }
        }
    }

    /**
     * Remove the views (buttons settings + pending call view) above the video call with a fade out animation.
     */
    private fun fadeOutVideoEdge() {
        fadeVideoEdge(UTILS_OPACITY_NONE, FADE_OUT_DURATION)
    }

    /**
     * Restore the views (buttons settings + pending call view) above the video call with a fade in animation.
     */
    private fun fadeInVideoEdge() {
        fadeVideoEdge(UTILS_OPACITY_FULL, FADE_IN_DURATION)
    }

    //==============================================================================================================
    // UI items refresh
    //==============================================================================================================

    /**
     * Compute the top margin of the view that contains the video
     * of the local attendee of the call (the small video, where
     * the user sees himself).<br></br>
     * Ratios are taken from the UI specifications. The vertical space
     * between the video view and the container (call_menu_buttons_layout_container)
     * containing the buttons of the video menu, is specified as 4.3% of
     * the height screen.
     */
    private fun computeVideoUiLayout() {
        if (null == mLocalVideoLayoutConfig) {
            mLocalVideoLayoutConfig = VideoLayoutConfiguration()
        }

        // get the height of the screen
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        var screenHeight = metrics.heightPixels
        val screenWidth = metrics.widthPixels

        // compute action bar size: the video Y component starts below the action bar
        val actionBarHeight: Int
        val typedValue = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
            screenHeight -= actionBarHeight
        }

        val mMenuButtonsContainerView = findViewById<View>(R.id.hang_up_button)
        val layout = mMenuButtonsContainerView.layoutParams

        if (0 == mLocalVideoLayoutConfig!!.mWidth) {
            mLocalVideoLayoutConfig!!.mWidth = PERCENT_LOCAL_USER_VIDEO_SIZE
        }

        if (0 == mLocalVideoLayoutConfig!!.mHeight) {
            mLocalVideoLayoutConfig!!.mHeight = PERCENT_LOCAL_USER_VIDEO_SIZE
        }

        if (0 != mSourceVideoWidth && 0 != mSourceVideoHeight) {
            val previewWidth = screenWidth * mLocalVideoLayoutConfig!!.mWidth / 100
            val previewHeight = screenHeight * mLocalVideoLayoutConfig!!.mHeight / 100

            val sourceRatio = mSourceVideoWidth * 100 / mSourceVideoHeight
            val previewRatio = previewWidth * 100 / previewHeight

            // there is an aspect ratio update
            if (sourceRatio != previewRatio) {
                val maxPreviewWidth = screenWidth * PERCENT_LOCAL_USER_VIDEO_SIZE / 100
                val maxPreviewHeight = screenHeight * PERCENT_LOCAL_USER_VIDEO_SIZE / 100

                if (maxPreviewHeight * sourceRatio / 100 > maxPreviewWidth) {
                    mLocalVideoLayoutConfig!!.mHeight = maxPreviewWidth * 100 * 100 / sourceRatio / screenHeight
                    mLocalVideoLayoutConfig!!.mWidth = PERCENT_LOCAL_USER_VIDEO_SIZE
                } else {
                    mLocalVideoLayoutConfig!!.mWidth = maxPreviewHeight * sourceRatio / screenWidth
                    mLocalVideoLayoutConfig!!.mHeight = PERCENT_LOCAL_USER_VIDEO_SIZE
                }
            }
        } else {
            mLocalVideoLayoutConfig!!.mWidth = PERCENT_LOCAL_USER_VIDEO_SIZE
            mLocalVideoLayoutConfig!!.mHeight = PERCENT_LOCAL_USER_VIDEO_SIZE
        }

        if (!mIsCustomLocalVideoLayoutConfig) {
            val buttonsContainerHeight = if (mButtonsContainerView!!.visibility == View.VISIBLE) layout.height * 100 / screenHeight else 0
            val bottomLeftMargin = (VIDEO_TO_BUTTONS_VERTICAL_SPACE * screenHeight.toFloat() * 100f / screenHeight).toInt()

            mLocalVideoLayoutConfig!!.mX = bottomLeftMargin * screenHeight / screenWidth
            mLocalVideoLayoutConfig!!.mY = 100 - bottomLeftMargin - buttonsContainerHeight - mLocalVideoLayoutConfig!!.mHeight
        }

        mLocalVideoLayoutConfig!!.mIsPortrait = resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
        mLocalVideoLayoutConfig!!.mDisplayWidth = screenWidth
        mLocalVideoLayoutConfig!!.mDisplayHeight = screenHeight
        android.util.Log.d("CallView", mLocalVideoLayoutConfig!!.mX.toString() + " y " + mLocalVideoLayoutConfig!!.mY + "--" + mLocalVideoLayoutConfig!!.mWidth + " mHeight " + mLocalVideoLayoutConfig!!.mHeight)
        android.util.Log.d("CallView", mLocalVideoLayoutConfig!!.mX.toString() + " y " + mLocalVideoLayoutConfig!!.mY + "--" + mLocalVideoLayoutConfig!!.mWidth + " mHeight " + mLocalVideoLayoutConfig!!.mHeight)
        Log.d(LOG_TAG, "## computeVideoUiLayout() : x " + mLocalVideoLayoutConfig!!.mX + " y " + mLocalVideoLayoutConfig!!.mY)
        Log.d(LOG_TAG, "## computeVideoUiLayout() : mWidth " + mLocalVideoLayoutConfig!!.mWidth + " mHeight " + mLocalVideoLayoutConfig!!.mHeight)
    }


    /**
     * Update the mute mic icon according to mute mic status.
     */
    private fun refreshMuteMicButton() {
        // update icon
        var iconId = 0;
        if (CallSoundsManager.getSharedInstance(this).isMicrophoneMute) {
            iconId = R.mipmap.call_audio_mute_off_icon
        } else {
            iconId = R.mipmap.call_audio_mute_on_icon
        }
        mMuteMicImageView!!.setImageResource(iconId)
    }

    /**
     * Update the mute speaker icon according to speaker status.
     */
    fun refreshSpeakerButton() {
        // update icon
        var iconId: Int = 0;
        if (CallSoundsManager.getSharedInstance(this).isSpeakerphoneOn) {
            iconId = R.mipmap.call_speaker_off_icon
        } else {
            iconId = R.mipmap.call_speaker_on_icon
        }
        mSpeakerSelectionView!!.setImageResource(iconId)
    }

    /**
     * Update the mute video icon.
     */
    private fun refreshMuteVideoButton() {
        if (null != mCall && mCall!!.isVideo) {
            mMuteLocalCameraView!!.visibility = View.VISIBLE

            val isMuted = mCall!!.isVideoRecordingMuted
            Log.d(LOG_TAG, "## refreshMuteVideoButton(): isMuted=$isMuted")

            // update icon
            var iconId = 0
            if (isMuted) {
                iconId = R.mipmap.call_video_mute_off_icon
            } else {
                iconId = R.mipmap.call_video_mute_on_icon
            }
            mMuteLocalCameraView!!.setImageResource(iconId)
        } else {
            Log.d(LOG_TAG, "## refreshMuteVideoButton(): View.INVISIBLE")
            mMuteLocalCameraView!!.visibility = View.INVISIBLE
        }
    }

    /**
     * Update the switch camera icon.
     * Note that, this icon is only active if the device supports
     * camera switching (See [IMXCall.isSwitchCameraSupported])
     */
    private fun refreshSwitchRearFrontCameraButton() {
        if (null != mCall && mCall!!.isVideo && mCall!!.isSwitchCameraSupported) {
            mSwitchRearFrontCameraImageView!!.visibility = View.VISIBLE

            val isSwitched = mCall!!.isCameraSwitched
            Log.d(LOG_TAG, "## refreshSwitchRearFrontCameraButton(): isSwitched=$isSwitched")

            // update icon
            var iconId = 0
            var backgroundId = 0
            if (isSwitched) {
                iconId = R.mipmap.camera_switch
            } else {
                iconId = R.mipmap.camera_switch
            }
            mSwitchRearFrontCameraImageView!!.setImageResource(iconId)
            mSwitchRearFrontCameraImageView!!.setBackgroundResource(backgroundId)
        } else {
            Log.d(LOG_TAG, "## refreshSwitchRearFrontCameraButton(): View.INVISIBLE")
            mSwitchRearFrontCameraImageView!!.visibility = View.INVISIBLE
        }
    }

    /**
     * Manage the UI according to call state.
     */
    private fun manageSubViews() {
        // sanity check
        // the call could have been destroyed between call.
        if (null == mCall) {
            Log.d(LOG_TAG, "## manageSubViews(): call instance = null, just return")
            return
        }

        val callState = mCall!!.callState
        Log.d(LOG_TAG, "## manageSubViews() IN callState : $callState")

        // avatar visibility: video call => hide avatar, audio call => show avatar
        mAvatarView!!.visibility = if (callState == IMXCall.CALL_STATE_CONNECTED && mCall!!.isVideo) View.GONE else View.VISIBLE

        // update UI icon settings
        refreshSpeakerButton()
        refreshMuteMicButton()
        refreshMuteVideoButton()
        refreshSwitchRearFrontCameraButton()

        // display the hang up button according to the call state
        when (callState) {
            IMXCall.CALL_STATE_ENDED -> mHangUpImageView!!.visibility = View.INVISIBLE
            IMXCall.CALL_STATE_CONNECTED -> {
                initBackLightManagement()
                mHangUpImageView!!.visibility = View.VISIBLE
            }
            else -> mHangUpImageView!!.visibility = View.VISIBLE
        }

//        if (mCall!!.isVideo) {
//            when (callState) {
//                IMXCall.CALL_STATE_CONNECTED -> startVideoFadingEdgesScreenTimer()
//
//                else -> stopVideoFadingEdgesScreenTimer()
//            }
//        }

        // callview visibility management
        if (mCall!!.isVideo && callState != IMXCall.CALL_STATE_ENDED) {
            val visibility: Int

            when (callState) {
                IMXCall.CALL_STATE_WAIT_CREATE_OFFER, IMXCall.CALL_STATE_INVITE_SENT, IMXCall.CALL_STATE_RINGING, IMXCall.CALL_STATE_CREATE_ANSWER, IMXCall.CALL_STATE_CONNECTING, IMXCall.CALL_STATE_CONNECTED -> visibility = View.VISIBLE
                else -> visibility = View.GONE
            }

            if (null != mCall && visibility != mCall!!.visibility) {

//                mCall!!.visibility = visibility
                android.util.Log.d("CallView", mCall!!.callView.width.toString() + "--" + mCall!!.callView.height.toString())
            }
        }

        // other management
        when (callState) {
            IMXCall.CALL_STATE_RINGING ->
                // the call view is created when the user accepts the call.
                if (mCall!!.isIncoming) {
                    mCall!!.answer()
                    mIncomingCallTabbar!!.visibility = View.GONE
                    mButtonsContainerView!!.visibility = View.VISIBLE
                }
            else -> {
            }
        }// nothing to do..
        Log.d(LOG_TAG, "## manageSubViews(): OUT")
    }

    /**
     * Save the call view before leaving the activity.
     */
    private fun saveCallView() {
        if (null != mCall && mCall!!.callState != IMXCall.CALL_STATE_ENDED && null != mCallView && null != mCallView!!.parent) {

            // warn the call that the activity is going to be paused.
            // as the rendering is DSA, it saves time to close the activity while removing mCallView
            mCall!!.onPause()

            val parent = mCallView!!.parent as ViewGroup
            parent.removeView(mCallView)
            mCallsManager!!.callView = mCallView

            mCallsManager!!.videoLayoutConfiguration = mLocalVideoLayoutConfig

            // remove the call layout to avoid having a black screen
            val layout = findViewById<RelativeLayout>(R.id.call_layout)
            layout.visibility = View.GONE
            mCallView = null
        }
    }

    //==============================================================================================================
    // Proximity sensor management
    //==============================================================================================================

    private val PROXIMITY_THRESHOLD = 3.0f // centimeters
    private var mWakeLock: PowerManager.WakeLock? = null
    private var mField = 0x00000020

    /**
     * Init the screen management to be able to turn the screen on/off
     */
    private fun initScreenManagement() {
        try {
            try {
                mField = PowerManager::class.java.javaClass.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null)
            } catch (ignored: Throwable) {
                Log.e(LOG_TAG, "## initScreenManagement " + ignored.message, ignored)
            }

            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            mWakeLock = powerManager.newWakeLock(mField, localClassName)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## initScreenManagement() : failed " + e.message, e)
        }

    }

    /**
     * Turn the screen off
     */
    private fun turnScreenOff() {
        if (null == mWakeLock) {
            initScreenManagement()
        }

        try {
            if (null != mWakeLock && !mWakeLock!!.isHeld) {
                mWakeLock!!.acquire()
                mIsScreenOff = true
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## turnScreenOff() failed", e)
        }

        // set the back light level to the minimum
        // fallback if the previous trick does not work
        if (null != window && null != window.attributes) {
            val layoutParams = window.attributes
            layoutParams.screenBrightness = 0f
            window.attributes = layoutParams
        }
    }

    /**
     * Turn the screen on
     */
    private fun turnScreenOn() {
        try {
            if (null != mWakeLock) {
                mWakeLock!!.release()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## turnScreenOn() failed", e)
        }

        mIsScreenOff = false
        mWakeLock = null

        // restore previous brightness (whatever it was)
        if (null != window && null != window.attributes) {
            val layoutParams = window.attributes
            layoutParams.screenBrightness = -1f
            window.attributes = layoutParams
        }
    }

    /**
     * Stop the proximity sensor.
     */
    private fun stopProximitySensor() {
        // do not release the proximity sensor while pausing the activity
        // when the screen is turned off, the activity is paused.
        if (null != mProximitySensor && null != mSensorMgr) {
            mSensorMgr!!.unregisterListener(this)
            mProximitySensor = null
            mSensorMgr = null
        }

        turnScreenOn()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (null != event) {
            val distanceCentimeters = event.values[0]

            Log.d(LOG_TAG, "## onSensorChanged(): " + String.format(VectorLocale.applicationLocale, "distance=%.3f", distanceCentimeters))

            if (CallsManager.getSharedInstance().isSpeakerphoneOn) {
                Log.d(LOG_TAG, "## onSensorChanged(): Skipped due speaker ON")
            } else {
                if (distanceCentimeters <= PROXIMITY_THRESHOLD) {
                    turnScreenOff()
                    Log.d(LOG_TAG, "## onSensorChanged(): force screen OFF")
                } else {
                    turnScreenOn()
                    Log.d(LOG_TAG, "## onSensorChanged(): force screen ON")
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(LOG_TAG, "## onAccuracyChanged(): accuracy=$accuracy")
    }
}
