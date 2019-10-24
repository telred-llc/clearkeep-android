package vmodev.clearkeep.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import im.vector.R
import im.vector.databinding.FragmentInProgressVoiceCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import vmodev.clearkeep.fragments.Interfaces.IFragment

class InProgressVoiceCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentInProgressVoiceCallBinding;

    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private var callSoundsManager: CallSoundsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            this@InProgressVoiceCallFragment.activity?.finish();
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress_voice_call, container, false, dataBinding.getDataBindingComponent());
        mxCall = CallsManager.getSharedInstance().activeCall;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mxCall.createCallView();
        callManager = CallsManager.getSharedInstance();
        callSoundsManager = CallSoundsManager.getSharedInstance(this.activity);
        callView = callManager?.callView;
        setupButtonControl();
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun onResume() {
        super.onResume()
        mxCall.addListener(callListener);
        callManager?.let {
            if (it.activeCall != null) {
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && mxCall.isVideo)
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
                callView = it.callView;
                CallsManager.getSharedInstance().setCallActivity(this.activity);
//                callView?.let { insertCallView(); }

            }
            mxCall.visibility = View.VISIBLE;
        } ?: run {
            this.activity?.finish();
        }
    }

    override fun onPause() {
        super.onPause()
        mxCall.removeListener(callListener);
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
            CallsManager.getSharedInstance().setCallActivity(null);
            saveCallView();
            this.activity?.finish();
        }
    }

    private fun insertCallView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        if (mxCall.isVideo) {
            callView?.let {
                binding.constraintLayoutRoot.addView(it, 0, params)
            }
        }
        mxCall.visibility = View.GONE
    }

    private fun saveCallView() {
        if (mxCall.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                (it.parent as ViewGroup).removeView(it);
                callManager?.callView = it;
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration;
            }
        }
        callView = null;
    }
}
