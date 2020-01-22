package vmodev.clearkeep.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
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
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.longTimeToString
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class OutgoingVideoCallCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentOutgoingCallBinding

    private lateinit var mCall: IMXCall
    private var mCallView: View? = null
    private var mCallsManager: CallsManager? = null
    private var callSoundsManager: CallSoundsManager? = null
    private var videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25)
    private var mListener = object : MXCallListener() {

        override fun onCallEnd(aReasonId: Int) {
            super.onCallEnd(aReasonId)
            activity?.finish()
        }

        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            activity?.runOnUiThread {
                if (null != mCall && mCall.isVideo && mCall.callState.equals(IMXCall.CALL_STATE_CONNECTED)) {
                    mCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
                }
            }
        }

        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            mCallView = callView
            insertCallView()
        }

        override fun onReady() {
            super.onReady()
            if (mCall.isIncoming) {
                mCall.launchIncomingCall(videoLayoutConfiguration)
            } else {
                mCall.placeCall(videoLayoutConfiguration)
            }
        }

        override fun onPreviewSizeChanged(width: Int, height: Int) {
            super.onPreviewSizeChanged(width, height)
            if (null != mCall && mCall.isVideo && mCall.callState.equals(IMXCall.CALL_STATE_CONNECTED)) {
                mCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
            }
        }
    }

    private fun saveCallView() {
        if (null != mCall && !mCall.callState.equals(IMXCall.CALL_STATE_ENDED) && null != mCallView && null != mCallView?.parent) {
            mCall.onPause()
            val parent = mCallView?.parent as ViewGroup
            parent.removeView(mCallView)
            mCallsManager?.callView = mCallView
            mCallsManager?.videoLayoutConfiguration = videoLayoutConfiguration
            val layout = view?.findViewById<RelativeLayout>(R.id.call_layout)
            layout?.visibility = View.GONE
            mCallView = null
        }
    }

    private fun setupButtonControl() {
        binding.imageViewHangUp.setOnClickListener {
            mCallsManager?.onHangUp(null)
        }
        binding.imageViewSwitchCamera.setOnClickListener {
            mCall.switchRearFrontCamera()
        }
        binding.imageViewMicrophone.setOnClickListener {
            callSoundsManager?.let {
                it.isMicrophoneMute = !it.isMicrophoneMute
                Toast.makeText(this.context, if (it.isMicrophoneMute) resources.getString(R.string.microphone_off) else resources.getString(R.string.microphone_on), Toast.LENGTH_SHORT).show()
                binding.callSoundsManager = it
            }
        }
        binding.imageViewSpeaker.setOnClickListener {
            mCallsManager?.let {
                mCallsManager?.toggleSpeaker()
                Toast.makeText(this.context, if (it.isSpeakerphoneOn) resources.getString(R.string.speaker_phone_on) else resources.getString(R.string.speaker_phone_off)
                        , Toast.LENGTH_SHORT).show()
                binding.callManager = it
            }
        }
        binding.imageViewGoToRoom.setOnClickListener {
            mCall.removeListener(mListener)
            CallsManager.getSharedInstance().setCallActivity(null)
            saveCallView()
            val intent = Intent(activity!!, RoomActivity::class.java)
                    .putExtra(RoomActivity.EXTRA_ROOM_ID, mCall.room.roomId)
                    .putExtra(RoomActivity.EXTRA_START_CALL_ID, mCall.callId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.activity?.finish()
        }
        binding.imageViewScreenShare.setOnClickListener {
            if (mCall.isScreenCast) {
                mCall.cameraVideo()
            } else {
                val mediaProjectionManager = activity?.application?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), SCREEN_SHARE_CODE)
            }
        }

        binding.imageViewMakeCamera.setOnClickListener {
            mCall.let {
                toggleVideo()
                binding.mxCall = mCall
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SCREEN_SHARE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    mCall.screenVideo(data)
                }
            }
        }
    }

    private fun insertCallView() {
        if (null != mCallView) {
            val layout = view?.findViewById<RelativeLayout>(R.id.call_layout)
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layout?.removeView(mCallView)
            layout?.visibility = View.VISIBLE
            if (mCall.isVideo) {
                if (null != mCallView?.parent) {
                    (mCallView?.parent as ViewGroup).removeView(mCallView)
                }
                layout?.addView(mCallView, 0, params)
            }
            mCall.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outgoing_call, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCall = CallsManager.getSharedInstance()?.activeCall!!
        mCall.let {
            mCallsManager = CallsManager.getSharedInstance()
            if (null != mCallsManager?.callView && null == mCallsManager?.callView?.parent) {
                mCallView = mCallsManager?.callView
                videoLayoutConfiguration = mCallsManager?.videoLayoutConfiguration!!
                mCall.addListener(mListener)
                insertCallView()
                callSoundsManager = CallSoundsManager.getSharedInstance(this.activity)
            } else {
                activity?.runOnUiThread {
                    if (null != mCall.callView) {
                        mCallView = mCall.callView
                        insertCallView()
                        mCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
                        if (mCall.callState.equals(IMXCall.CALL_STATE_READY) && mCall.isIncoming) {
                            mCall.launchIncomingCall(videoLayoutConfiguration)
                        }
                    } else if (!mCall.isIncoming && mCall.callState.equals(IMXCall.CALL_STATE_CREATED)) {
                        mCall.createCallView()
                    }
                }
            }
            setupButtonControl()
            initComponent()
            updateStatusControlCall()
        }
    }

    override fun onResume() {
        super.onResume()
        if (null != mCall && mCall.isVideo && mCall.callState.equals(IMXCall.CALL_STATE_CONNECTED)) {
            mCall.updateLocalVideoRendererPosition(videoLayoutConfiguration)
        }
        if (null != mCall) {
            mCall.addListener(mListener)
            mCallView = mCallsManager?.callView
            insertCallView()
            CallsManager.getSharedInstance().setCallActivity(activity)
        }
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCall.onPause()
        mCall.removeListener(mListener)
        saveCallView()
        CallsManager.getSharedInstance().setCallActivity(null)
    }

    private fun initComponent() {
        mCallsManager?.let {
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
        mCall.let {
            if (it.isVideoRecordingMuted) {
                toggleVideo()
            }
            binding.mxCall = mCall
        }
    }

    private fun updateStatusControlCall() {
        mCallsManager?.let {
            binding.callManager = it
        }
        callSoundsManager?.let {
            binding.callSoundsManager = it
        }
        mCall.let {
            binding.mxCall = mCall
        }

    }

    private fun toggleVideo() {
        mCall.let {
            if (mCall.isVideo) {
                val isMuted = mCall.isVideoRecordingMuted
                mCall.muteVideoRecording(!isMuted)
            }
        }
    }

    private fun upDateTimeCall() {
        val disposableCallElapsedTime = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mCall.callElapsedTime > -1) {
                        binding.tvTimeCall.text = mCall.callElapsedTime.longTimeToString()
                    }
                }
        compositeDisposable.add(disposableCallElapsedTime)
    }

    companion object {
        const val SCREEN_SHARE_CODE = 12321
    }
}
