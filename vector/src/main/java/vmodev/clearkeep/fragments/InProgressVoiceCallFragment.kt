package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
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
import im.vector.util.CallUtilities
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
import vmodev.clearkeep.ultis.FormatString
import vmodev.clearkeep.ultis.longTimeToString
import vmodev.clearkeep.viewmodels.interfaces.AbstractInProgressVoiceCallFragmentViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InProgressVoiceCallFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractInProgressVoiceCallFragmentViewModel>;

    private lateinit var binding: FragmentInProgressVoiceCallBinding;
    private lateinit var mxCall: IMXCall;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private var callSoundsManager: CallSoundsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private var disposableCallElapsedTime: Disposable? = null;
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            this@InProgressVoiceCallFragment.activity?.finish();
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_READY -> {
                    if (mxCall.callElapsedTime > -1)
                        upDateTimeCall()
                }
                IMXCall.CALL_STATE_CONNECTED -> {
                    upDateTimeCall()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress_voice_call, container, false, dataBinding.getDataBindingComponent());
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
        FormatString
        viewModelFactory.getViewModel().setRoomId(mxCall.room.roomId);
        binding.rippleBackground.startRippleAnimation()
        initComponent()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableCallElapsedTime?.dispose()
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

    @SuppressLint("CheckResult")
    private fun setupButtonControl() {
        binding.imageViewHangUp.setOnClickListener {
            callManager?.onHangUp(null)
            binding.rippleBackground.stopRippleAnimation()
        }
        binding.imageViewMicrophone.setOnClickListener {
            callSoundsManager?.let {
                it.isMicrophoneMute = !it.isMicrophoneMute;
                Toast.makeText(this.context, if (it.isMicrophoneMute) resources.getString(R.string.microphone_off) else resources.getString(R.string.microphone_on)
                        , Toast.LENGTH_SHORT).show();
                binding.callSoundsManager = it
            }
        }
        binding.imageViewSpeaker.setOnClickListener {
            callManager?.let {
                callManager?.toggleSpeaker();
                Toast.makeText(this.context, if (it.isSpeakerphoneOn) resources.getString(R.string.speaker_phone_on) else resources.getString(R.string.speaker_phone_off)
                        , Toast.LENGTH_SHORT).show();
                binding.callManager = it
            }
        }
        binding.imageViewGoToRoom.setOnClickListener {
            CallsManager.getSharedInstance().setCallActivity(null);
            this.activity?.finish();
        }
        disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            binding.textViewCalling.text = mxCall.callElapsedTime.longTimeToString();
        }
    }

    private fun initComponent() {
        callSoundsManager?.let {
            if (it.isMicrophoneMute) {
                it.isMicrophoneMute = !it.isMicrophoneMute
            }
            binding.callSoundsManager = it
        }
        callManager?.let {
            if (it.isSpeakerphoneOn) {
                it.toggleSpeaker()
            }
            binding.callManager = it
        }
    }


    private fun upDateTimeCall() {
        disposableCallElapsedTime?.dispose();
        disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.textViewCalling.text = mxCall.callElapsedTime.longTimeToString();
                }
    }
}
