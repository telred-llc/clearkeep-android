package vmodev.clearkeep.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentInProgressCallBinding
import im.vector.util.CallsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.longTimeToString
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class InProgressCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentInProgressCallBinding
    private lateinit var mxCall: IMXCall
    private var callView: View? = null
    private var callManager: CallsManager? = null
    private var callSoundsManager: CallSoundsManager? = null
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25)
    private var callListener: MXCallListener = object : MXCallListener() {
        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            this@InProgressCallFragment.activity?.finish()
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_CONNECTED -> {
//                    disposableCallElapsedTime?.dispose();
//                    disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                            .subscribe {
//                                binding.tvTimeCall.text = mxCall.callElapsedTime.longTimeToString();
//                            }
                    upDateTimeCall()
                }
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress_call, container, false, dataBinding.getDataBindingComponent())
        mxCall = CallsManager.getSharedInstance().activeCall
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mxCall.createCallView()
        callManager = CallsManager.getSharedInstance()
        callSoundsManager = CallSoundsManager.getSharedInstance(this.activity)
        callView = callManager?.callView
        setupButtonControl()
        initComponent()
    }

    override fun getFragment(): Fragment {
        return this
    }

    private fun upDateTimeCall() {
        val disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mxCall.callElapsedTime > -1) {
                        binding.tvTimeCall.text = mxCall.callElapsedTime.longTimeToString()
                    }
                }
        compositeDisposable.add(disposableCallElapsedTime)
    }

    override fun onResume() {
        super.onResume()
        mxCall.addListener(callListener)
        callManager?.let {
            if (it.activeCall != null) {
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && mxCall.isVideo)
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
                Log.d("CallView", it.callView.toString() + "--")
                callView = it.callView
                CallsManager.getSharedInstance().setCallActivity(this.activity)
                callView?.let { insertCallView(); }
//                disposableCallElapsedTime?.dispose();
//                disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            binding.tvTimeCall.text = mxCall.callElapsedTime.longTimeToString();
//                        }
                upDateTimeCall()
            }
            mxCall.visibility = View.VISIBLE
        } ?: run {
            this.activity?.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        mxCall.removeListener(callListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OutgoingVideoCallCallFragment.SCREEN_SHARE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
//                    imageViewScreenShareimageViewScreenShare(data);
                }
            }
        }
    }

    private fun saveCallView() {
        if (mxCall.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                (it.parent as ViewGroup).removeView(it)
                callManager?.callView = it
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration
            }
        }
        callView = null
    }

    private fun setupButtonControl() {
        binding.imageViewHangUp.setOnClickListener {
            callManager?.let { it.onHangUp(null) }
        }
        binding.imageViewSwitchCamera.setOnClickListener {
            mxCall.switchRearFrontCamera()
        }
        binding.imageViewMicrophone.setOnClickListener {
            callSoundsManager?.let {
                it.isMicrophoneMute = !it.isMicrophoneMute
                Toast.makeText(this.context, if (it.isMicrophoneMute) resources.getString(R.string.microphone_off) else resources.getString(R.string.microphone_on)
                        , Toast.LENGTH_SHORT).show()
                binding.callSoundsManager = it
            }
        }
        binding.imageViewSpeaker.setOnClickListener {
            callManager?.let {
                callManager?.toggleSpeaker()
                Toast.makeText(this.context, if (it.isSpeakerphoneOn) resources.getString(R.string.speaker_phone_on) else resources.getString(R.string.speaker_phone_off)
                        , Toast.LENGTH_SHORT).show()

                binding.callManager = it
            }
        }
        binding.imageViewGoToRoom.setOnClickListener {
            CallsManager.getSharedInstance().setCallActivity(null)
            saveCallView()
            this.activity?.finish()
        }
        binding.imageViewMakeCamera.setOnClickListener {
            mxCall.let {
                if (mxCall.isVideo) {
                    val isMuted = mxCall.isVideoRecordingMuted
                    mxCall.muteVideoRecording(!isMuted)
                    binding.mxCall = mxCall
                }
            }
        }
        binding.imageViewScreenShare.setOnClickListener {
            if (mxCall.isScreenCast) {
                mxCall.cameraVideo()
            } else {
                val mediaProjectionManager = activity?.application?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), OutgoingVideoCallCallFragment.SCREEN_SHARE_CODE)
            }
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
                    (it.parent as ViewGroup).removeView(it)
                binding.constraintLayoutRoot.addView(it, 0, params)
            }
        }
        mxCall.visibility = View.GONE
    }

    private fun initComponent() {
        callManager?.let {
            binding.callManager = it
        }
        callSoundsManager?.let {
            binding.callSoundsManager = it
        }
        mxCall.let {
            binding.mxCall = mxCall
        }
    }

    companion object {
        const val SCREEN_SHARE_CODE = 12321
    }
}
