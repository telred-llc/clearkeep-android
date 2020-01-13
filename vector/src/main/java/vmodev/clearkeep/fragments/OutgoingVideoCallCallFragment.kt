package vmodev.clearkeep.fragments


import android.app.Activity
import android.content.Intent
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
import im.vector.databinding.FragmentOutgoingCallBinding
import im.vector.util.CallsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.call.CallSoundsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import org.matrix.androidsdk.core.Debug
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.longTimeToString
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class OutgoingVideoCallCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentOutgoingCallBinding

    private lateinit var mxCall: IMXCall
    private var callView: View? = null
    private var callManager: CallsManager? = null
    private var callSoundsManager: CallSoundsManager? = null
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25)
    private var isConnecting: Boolean = false
    private var callListener: MXCallListener = object : MXCallListener() {

        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            Log.e("Tag", "--- Demo")
            activity?.finish()
        }

        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            if (mxCall.isVideo) {
                if (this@OutgoingVideoCallCallFragment.callView == null) {
                    this@OutgoingVideoCallCallFragment.callView = callView
                    insertCallView()
                }
            }
        }

        override fun onReady() {
            super.onReady()
            if (mxCall.isIncoming) {
                mxCall.launchIncomingCall(videoLayoutConfiguration)
//                mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
            } else {
                mxCall.placeCall(videoLayoutConfiguration)
//                mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
            }
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            Log.d("CallView", state.toString())
            when (state) {
                IMXCall.CALL_STATE_INVITE_SENT -> {
                    initComponent()
                }
                IMXCall.CALL_STATE_CONNECTED -> {
                    mxCall.visibility = View.VISIBLE
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
//                    mxCall.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)

                }
                IMXCall.CALL_STATE_ENDED -> {
                    Log.e("Tag", "--- Tag endcall 2")
                    this@OutgoingVideoCallCallFragment.activity?.finish()
                }
                IMXCall.CALL_STATE_CONNECTING -> {
                    upDateTimeCall()
                }
                IMXCall.CALL_STATE_READY -> {
                    upDateTimeCall()
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
                binding.constraintLayoutRoot.visibility = View.GONE
            }
        }
        callView = null
    }

    private fun setupButtonControl() {
        binding.imageViewHangUp.setOnClickListener {
            callManager?.onHangUp(null)
            activity?.finish()
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
            mxCall.removeListener(callListener)
            CallsManager.getSharedInstance().setCallActivity(null)
            saveCallView()
            val intent = Intent(activity!!, RoomActivity::class.java)
                    .putExtra(RoomActivity.EXTRA_ROOM_ID, mxCall.room.roomId)
                    .putExtra(RoomActivity.EXTRA_START_CALL_ID, mxCall.callId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.activity?.finish()
        }
        binding.imageViewScreenShare.setOnClickListener {
            //            if (mxCall.isScreenCast) {
//                mxCall.cameraVideo()
//            } else {
//                val mediaProjectionManager = activity?.application?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
//                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), SCREEN_SHARE_CODE)
//            }
        }

        binding.imageViewMakeCamera.setOnClickListener {
            mxCall.let {
                toggleVideo()
                binding.mxCall = mxCall
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SCREEN_SHARE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
//                    mxCall.screenVideo(data)
                }
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outgoing_call, container, false, dataBinding.getDataBindingComponent())
        mxCall = CallsManager.getSharedInstance().activeCall
        mxCall.createCallView()
        callManager = CallsManager.getSharedInstance()
        callSoundsManager = CallSoundsManager.getSharedInstance(this.activity)
        callView = callManager?.callView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonControl()
        updateStatusControlCall()
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun onResume() {
        super.onResume()
        mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
        callManager?.let {
            if (it.activeCall != null) {
                mxCall.addListener(callListener)
                if (mxCall.callState == IMXCall.CALL_STATE_CONNECTED && mxCall.isVideo)
                    mxCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
                callView = it.callView
                CallsManager.getSharedInstance().setCallActivity(this.activity)
                callView?.let { insertCallView() }
            }
            mxCall.visibility = View.VISIBLE
            binding.constraintLayoutRoot.visibility = View.VISIBLE
        } ?: run {
            this.activity?.finish()
        }
    }

    private fun initComponent() {
        callManager?.let {
            if (!it.isSpeakerphoneOn) {
                it.toggleSpeaker()
            }
            binding.callManager = it
        }
        callSoundsManager?.let {
            if (it.isMicrophoneMute) {
                it.isMicrophoneMute = !it.isMicrophoneMute
            }
            binding.callSoundsManager = it
        }
        mxCall.let {
            if (it.isVideoRecordingMuted) {
                toggleVideo()
            }
            binding.mxCall = mxCall
        }
    }

    private fun updateStatusControlCall() {
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

    private fun toggleVideo() {
        mxCall.let {
            if (mxCall.isVideo) {
                val isMuted = mxCall.isVideoRecordingMuted
                mxCall.muteVideoRecording(!isMuted)
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        Debug.e("--- onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.e("--- onDestroy")
    }


    companion object {
        const val SCREEN_SHARE_CODE = 12321
    }
}
