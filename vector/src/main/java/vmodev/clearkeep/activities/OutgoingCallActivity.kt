package vmodev.clearkeep.activities

import android.Manifest
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityOutgoingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.*
import org.webrtc.RendererCommon
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity

class OutgoingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityOutgoingCallBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call, dataBinding.getDataBindingComponent());
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
