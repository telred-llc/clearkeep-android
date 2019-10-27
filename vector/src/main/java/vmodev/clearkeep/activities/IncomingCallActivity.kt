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
    private var mxCall: IMXCall? = null;
    private lateinit var navController: NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent());
        navController = findNavController(R.id.fragment);
        mxCall = CallsManager.getSharedInstance().activeCall;
        mxCall?.let {
            if (it.isVideo) {
                requestMicrophoneAndCamera();
            } else {
                requestMicrophone();
            }
        }
    }

    private fun inflateNavGraph(){
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.navigation_incoming_call);
        navController.graph = graph;
        navController.navigate(R.id.incomingCallFragment);
    }

    @AfterPermissionGranted(REQUEST_MICROPHONE_AND_CAMERA)
    private fun requestMicrophoneAndCamera() {
        val params = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
            inflateNavGraph();
        } else {
            EasyPermissions.requestPermissions(this, "Application need permissions for video call", REQUEST_MICROPHONE_AND_CAMERA, *params);
        }
    }

    @AfterPermissionGranted(REQUEST_MICROPHONE)
    private fun requestMicrophone() {
        val params = arrayOf(Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
            inflateNavGraph();
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

    companion object {
        const val ROOM_ID = "ROOM_ID";
        const val REQUEST_MICROPHONE_AND_CAMERA = 12153;
        const val REQUEST_MICROPHONE = 12253;
    }
}
