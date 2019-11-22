package vmodev.clearkeep.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import im.vector.R
import im.vector.databinding.FragmentIncomingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import org.webrtc.RendererCommon
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractIncomingCallFragmentViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class IncomingCallFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractIncomingCallFragmentViewModel>;

    private lateinit var binding: FragmentIncomingCallBinding;
    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private val callListener = object : MXCallListener() {
        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_ENDED -> {
                    this@IncomingCallFragment.activity?.finish();
                }
                IMXCall.CALL_STATE_CONNECTED -> {
                    mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
                    saveCallView();
                    if (mxCall.isVideo) {
                        findNavController().navigate(IncomingCallFragmentDirections.inProgressCall());
                    } else {
                        findNavController().navigate(IncomingCallFragmentDirections.inProgressVoiceCall());
                    }
                }
                IMXCall.CALL_STATE_CONNECTING -> {
                    binding.textViewCalling.text = resources.getText(R.string.call_connecting);
                }
                IMXCall.CALL_STATE_RINGING -> {
                    binding.textViewCalling.text = resources.getText(R.string.calling);
                }
            }
        }

        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            callView?.let {
                this@IncomingCallFragment.callView = it;
                mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration);
            }
        }

        override fun onReady() {
            super.onReady()
            mxCall.launchIncomingCall(videoLayoutConfiguration);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mxCall = CallsManager.getSharedInstance().activeCall;
        mxCall.createCallView();
        callManager = CallsManager.getSharedInstance();
    }

    override fun onResume() {
        super.onResume()
        mxCall.addListener(callListener);
    }

    override fun onPause() {
        super.onPause()
        mxCall.removeListener(callListener);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incoming_call, container, false, dataBinding.getDataBindingComponent());
        binding.lifecycleOwner = viewLifecycleOwner;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.room = viewModelFactory.getViewModel().getRoomResult();
        binding.imageViewAccept.setOnClickListener {
            mxCall.answer();
            binding.imageViewAccept.visibility = View.GONE;
        }
        binding.imageViewDecline.setOnClickListener {
            mxCall.hangup(null);
            binding.rippleBackground.stopRippleAnimation()
        }
        binding.rippleBackground.startRippleAnimation()
        viewModelFactory.getViewModel().setRoomId(mxCall.room.roomId);
    }

    override fun getFragment(): Fragment {
        return this;
    }

    private fun saveCallView() {
        if (mxCall.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                callManager?.callView = it;
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration;
            }
        }
        callView = null;
    }
}
