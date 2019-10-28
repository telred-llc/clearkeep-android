package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import im.vector.R
import im.vector.databinding.FragmentOutgoingVoiceCallBinding
import im.vector.util.CallsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.longTimeToString
import vmodev.clearkeep.viewmodels.interfaces.AbstractOutgoingVoiceCallFragmentViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OutgoingVoiceCallFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractOutgoingVoiceCallFragmentViewModel>;

    private lateinit var binding: FragmentOutgoingVoiceCallBinding;

    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private var callSoundsManager: CallSoundsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private var disposableCallElapsedTime: Disposable? = null;
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            this@OutgoingVoiceCallFragment.activity?.finish();
        }

        override fun onReady() {
            super.onReady()
            if (mxCall.isIncoming) {
                mxCall.launchIncomingCall(videoLayoutConfiguration);
            } else {
                mxCall.placeCall(videoLayoutConfiguration);
            }
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_ENDED -> {
                    this@OutgoingVoiceCallFragment.activity?.finish();
                }
                IMXCall.CALL_STATE_CONNECTED -> {
                    disposableCallElapsedTime?.dispose();
                    disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                binding.textViewCalling.text = mxCall.callElapsedTime.longTimeToString();
                            }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outgoing_voice_call, container, false, dataBinding.getDataBindingComponent());
        mxCall = CallsManager.getSharedInstance().activeCall;
        binding.lifecycleOwner = viewLifecycleOwner;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mxCall.createCallView();
        callManager = CallsManager.getSharedInstance();
        callSoundsManager = CallSoundsManager.getSharedInstance(this.activity);
        callView = callManager?.callView;
        setupButtonControl();
        binding.room = viewModelFactory.getViewModel().getRoomResult();
        viewModelFactory.getViewModel().setRoomId(mxCall.room.roomId);
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun onResume() {
        super.onResume()
        mxCall.addListener(callListener);
        callManager?.let {
            if (it.activeCall != null) {
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && !mxCall.isVideo) {
                    disposableCallElapsedTime?.dispose();
                    disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                binding.textViewCalling.text = mxCall.callElapsedTime.longTimeToString();
                            }
                }
                callView = it.callView;
                CallsManager.getSharedInstance().setCallActivity(this.activity);
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
            callManager?.onHangUp(null)
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
            this.activity?.finish();
        }
    }
}
