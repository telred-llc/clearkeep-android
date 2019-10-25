package vmodev.clearkeep.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import im.vector.R
import im.vector.databinding.FragmentOutgoingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import org.webrtc.RendererCommon
import vmodev.clearkeep.fragments.Interfaces.IFragment

/**
 * A simple [Fragment] subclass.
 */
class OutgoingVideoCallCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentOutgoingCallBinding;

    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private var callSoundsManager: CallSoundsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            if (mxCall.isVideo) {
                if (this@OutgoingVideoCallCallFragment.callView == null) {
                    this@OutgoingVideoCallCallFragment.callView = callView;
                    insertCallView();
                }
            }
        }

        override fun onReady() {
            super.onReady()
            if (mxCall.isIncoming) {
                mxCall.launchIncomingCall(videoLayoutConfiguration);
                mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
            } else {
                mxCall.placeCall(videoLayoutConfiguration);
                mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
            }
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            Log.d("CallView", state.toString());
            when (state) {
                IMXCall.CALL_STATE_CONNECTED -> {
                    mxCall.visibility = View.VISIBLE;
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
                    mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
                }
                IMXCall.CALL_STATE_ENDED -> {
                    this@OutgoingVideoCallCallFragment.activity?.finish();
                }
            }
        }
    }

    private fun saveCallView() {
        if (mxCall.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                (it.parent as ViewGroup).removeView(it);
                callManager?.callView = it;
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration;
                binding.constraintLayoutRoot.visibility = View.GONE;
            }
        }
        callView = null;
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
                Toast.makeText(this.context, if (it.isMicrophoneMute) resources.getString(R.string.microphone_off) else resources.getString(R.string.microphone_on)
                        , Toast.LENGTH_SHORT).show();
            }
        }
        binding.imageViewSpeaker.setOnClickListener {
            callManager?.let {
                callManager?.toggleSpeaker();
                Toast.makeText(this.context, if (it.isSpeakerphoneOn) resources.getString(R.string.speaker_phone_on) else resources.getString(R.string.speaker_phone_off)
                        , Toast.LENGTH_SHORT).show();
            }
        }
        binding.imageViewGoToRoom.setOnClickListener {
            mxCall.removeListener(callListener);
            CallsManager.getSharedInstance().setCallActivity(null);
            saveCallView();
            this.activity?.finish();
        }
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outgoing_call, container, false, dataBinding.getDataBindingComponent());
        mxCall = CallsManager.getSharedInstance().activeCall;
        mxCall.createCallView();
        callManager = CallsManager.getSharedInstance();
        callSoundsManager = CallSoundsManager.getSharedInstance(this.activity);
        callView = callManager?.callView;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonControl();
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun onResume() {
        super.onResume()
        mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
        callManager?.let {
            if (it.activeCall != null) {
                mxCall.addListener(callListener);
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && mxCall.isVideo)
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
                callView = it.callView;
                CallsManager.getSharedInstance().setCallActivity(this.activity);
                callView?.let { insertCallView(); }

            }
            mxCall.visibility = View.VISIBLE;
            binding.constraintLayoutRoot.visibility = View.VISIBLE;
        } ?: run {
            this.activity?.finish();
        }
    }
}
