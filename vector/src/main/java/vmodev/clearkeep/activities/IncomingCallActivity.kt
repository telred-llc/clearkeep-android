package vmodev.clearkeep.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import im.vector.R
import im.vector.databinding.ActivityIncomingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.IMXCall
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding;
    private lateinit var navController: NavController;
    private var mxCall: IMXCall? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent());
        navController = findNavController(R.id.fragment);
//        mxCall = CallsManager.getSharedInstance().activeCall;
//        mxCall?.let {
//            if (it.isVideo) {
//                requestMicrophoneAndCamera();
//            } else {
//                requestMicrophone();
//            }
//        } ?: run {
//            this.finish();
//        }
        navController.navigate(R.id.incomingCallFragment);
    }

    @AfterPermissionGranted(REQUEST_MICROPHONE_AND_CAMERA)
    private fun requestMicrophoneAndCamera() {
        val params = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
            navController.navigate(R.id.incomingCallFragment);
        } else {
            EasyPermissions.requestPermissions(this, "Application need permissions for video call", REQUEST_MICROPHONE_AND_CAMERA, *params);
        }
    }

    @AfterPermissionGranted(REQUEST_MICROPHONE)
    private fun requestMicrophone() {
        val params = arrayOf(Manifest.permission.RECORD_AUDIO);
        if (EasyPermissions.hasPermissions(this, *params)) {
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

    companion object {
        const val ROOM_ID = "ROOM_ID";
        const val REQUEST_MICROPHONE_AND_CAMERA = 12153;
        const val REQUEST_MICROPHONE = 12253;
    }
}
