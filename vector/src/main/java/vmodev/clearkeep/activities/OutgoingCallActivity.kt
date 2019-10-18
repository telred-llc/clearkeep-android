package vmodev.clearkeep.activities

import android.Manifest
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityOutgoingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.*
import org.webrtc.RendererCommon
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity

class OutgoingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityOutgoingCallBinding;
    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private var callSoundsManager: CallSoundsManager? = null;
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            if (mxCall.isVideo) {
                callView?.let { this@OutgoingCallActivity.callView = it }
                insertCallView();
            }
        }

        override fun onReady() {
            super.onReady()
            if (mxCall.isIncoming) {
                mxCall.launchIncomingCall(videoLayoutConfiguration);
            } else {
                mxCall.placeCall(videoLayoutConfiguration);
                mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
            }
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_CONNECTED -> {
                    mxCall.visibility = View.VISIBLE;
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
                    mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
                }
                IMXCall.CALL_STATE_ENDED -> {
                    this@OutgoingCallActivity.finish();
                }
                else -> {
                }
            }
        }
    }
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call, dataBinding.getDataBindingComponent());
        mxCall = CallsManager.getSharedInstance().activeCall;
        mxCall.createCallView();
        callManager = CallsManager.getSharedInstance();
        callSoundsManager = CallSoundsManager.getSharedInstance(this);
        callView = callManager?.callView;
        setupButtonControl();
        requestCameraAndMicroPermissions();
    }

    private fun setupButtonControl() {
        binding.imageViewHangUp.setOnClickListener {
            callManager?.let { it.onHangUp(null) }
        }
        binding.imageViewSwitchCamera.setOnClickListener {
            mxCall.switchRearFrontCamera();
        }
        binding.imageViewMicrophone.setOnClickListener {
            callSoundsManager?.let {
                it.isMicrophoneMute = !it.isMicrophoneMute;
                Toast.makeText(this, if (it.isMicrophoneMute) resources.getString(R.string.microphone_off) else resources.getString(R.string.microphone_on)
                        , Toast.LENGTH_SHORT).show();
            }
        }
        binding.imageViewSpeaker.setOnClickListener {
            callManager?.let {
                callManager?.toggleSpeaker();
                Toast.makeText(this, if (it.isSpeakerphoneOn) resources.getString(R.string.speaker_phone_on) else resources.getString(R.string.speaker_phone_off)
                        , Toast.LENGTH_SHORT).show();
            }
        }
        binding.imageViewGoToRoom.setOnClickListener {
            mxCall.removeListener(callListener);
            CallsManager.getSharedInstance().setCallActivity(null);
            saveCallView();
            finish();
        }
    }

    private fun saveCallView() {
        if (mxCall.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                mxCall.onPause();
                (it.parent as ViewGroup).removeView(it);
                callManager?.callView = it;
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration;
                binding.constraintLayoutRoot.visibility = View.GONE;
            }
        }
        callView = null;
    }

    @AfterPermissionGranted(REQUEST_CAMERA_AND_MICRO)
    private fun requestCameraAndMicroPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA);
        if (EasyPermissions.hasPermissions(this, *permissions)) {
//            mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
//            mxCall.addListener(callListener);
        } else {
            EasyPermissions.requestPermissions(this, "", REQUEST_CAMERA_AND_MICRO, *permissions);
        }
    }

    override fun onResume() {
        super.onResume()
        mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
        callManager?.let {
            if (it.activeCall != null) {
//                requestCameraAndMicroPermissions();
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && mxCall.isVideo)
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
                mxCall.addListener(callListener);
                callView = it.callView;
                CallsManager.getSharedInstance().setCallActivity(this);
                mxCall.onResume();
                insertCallView();
            }
        } ?: run {
            finish();
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    private fun insertCallView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        binding.constraintLayoutRoot.removeView(callView)
        binding.constraintLayoutRoot.visibility = View.VISIBLE

        if (mxCall.isVideo) {
            callView?.let {
                if (it.parent != null)
                    (it.parent as ViewGroup).removeView(it);
                binding.constraintLayoutRoot.addView(it, 0, params)
            }
        }
        mxCall.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CAMERA_AND_MICRO = 15324;
    }
}
