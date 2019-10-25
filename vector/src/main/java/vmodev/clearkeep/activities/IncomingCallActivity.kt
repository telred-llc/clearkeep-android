package vmodev.clearkeep.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import im.vector.R
import im.vector.databinding.ActivityIncomingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.call.MXCallListener
import org.matrix.androidsdk.call.VideoLayoutConfiguration
import org.webrtc.RendererCommon
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.IncomingCallFragmentDirections

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding;
    private lateinit var navController: NavController;
    private var mxCall: IMXCall? = null;
    private var callView: View? = null;
    private var callManager: CallsManager? = null;
    private val videoLayoutConfiguration = VideoLayoutConfiguration(5, 66, 25, 25);
    private val callListener = object : MXCallListener() {
        override fun onStateDidChange(state: String?) {
            super.onStateDidChange(state)
            when (state) {
                IMXCall.CALL_STATE_ENDED -> {
                    this@IncomingCallActivity.finish();
                }
            }
        }

        override fun onCallViewCreated(callView: View?) {
            super.onCallViewCreated(callView)
            callView?.let {
                if (this@IncomingCallActivity.callView == null) {
                    this@IncomingCallActivity.callView = it;
//                    mxCall?.let {
//                        if (it.isVideo) {
//                            requestMicrophoneAndCamera();
//                        } else {
//                            requestMicrophone();
//                        }
//                    } ?: run {
//                        this@IncomingCallActivity.finish();
//                    }
                }
            }
        }

        override fun onReady() {
            super.onReady()
            mxCall?.launchIncomingCall(videoLayoutConfiguration);
        }
    }

    private fun insertCallView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        binding.frameLayoutRoot.removeView(callView)
        binding.frameLayoutRoot.visibility = View.VISIBLE

        mxCall?.let {
            if (it?.isVideo) {
                callView?.let {
                    if (it.parent != null)
                        (it.parent as ViewGroup).removeView(it);
                    binding.frameLayoutRoot.addView(it, 1, params)
                }
            }
        }
//        mxCall.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent());
        navController = findNavController(R.id.fragment);
        mxCall = CallsManager.getSharedInstance().activeCall;
        callManager = CallsManager.getSharedInstance();
        mxCall?.createCallView();
        mxCall?.addListener(callListener);
    }

    override fun onStart() {
        super.onStart()

    }

    @AfterPermissionGranted(REQUEST_MICROPHONE_AND_CAMERA)
    private fun requestMicrophoneAndCamera() {
        val params = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
            mxCall?.removeListener(callListener);
            saveCallView();
            navController.navigate(R.id.incomingCallFragment);
        } else {
            EasyPermissions.requestPermissions(this, "Application need permissions for video call", REQUEST_MICROPHONE_AND_CAMERA, *params);
        }
    }

    @AfterPermissionGranted(REQUEST_MICROPHONE)
    private fun requestMicrophone() {
        val params = arrayOf(Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
            mxCall?.removeListener(callListener);
            saveCallView();
            navController.navigate(R.id.incomingCallFragment);
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for voice call", REQUEST_MICROPHONE, *params);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    private fun saveCallView() {
        if (mxCall?.callState != IMXCall.CALL_STATE_ENDED) {
            callView?.let {
                callManager?.callView = it;
                callManager?.videoLayoutConfiguration = videoLayoutConfiguration;
            }
        }
        callView = null;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
        const val REQUEST_MICROPHONE_AND_CAMERA = 12153;
        const val REQUEST_MICROPHONE = 12253;
    }
}
