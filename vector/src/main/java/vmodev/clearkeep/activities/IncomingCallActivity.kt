package vmodev.clearkeep.activities

import android.Manifest
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import im.vector.R
import im.vector.databinding.ActivityIncomingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.IMXCall
import vmodev.clearkeep.activities.interfaces.IActivity

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding
    private var mxCall: IMXCall? = null
    private lateinit var navController: NavController

    companion object {
        const val ROOM_ID = "ROOM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent())
        navController = findNavController(R.id.fragment)
        mxCall = CallsManager.getSharedInstance().activeCall
        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        val navInflater = navController.navInflater
                        val graph = navInflater.inflate(R.navigation.navigation_incoming_call)
                        navController.graph = graph
                        navController.navigate(R.id.incomingCallFragment)
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        mxCall?.hangup(null)
                        finish()
                    }
                })
                .setDeniedMessage("Please go to your Setting and turn on Permission")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .check()
    }

    override fun getActivity(): FragmentActivity {
        return this
    }
}
