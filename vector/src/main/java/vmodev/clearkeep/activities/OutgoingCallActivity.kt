package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import im.vector.R
import im.vector.databinding.ActivityOutgoingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.core.Debug
import vmodev.clearkeep.activities.interfaces.IActivity

class OutgoingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityOutgoingCallBinding
    private lateinit var navController: NavController
    private var mxCall: IMXCall? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call, dataBinding.getDataBindingComponent())
        mxCall = CallsManager.getSharedInstance().activeCall
        navController = findNavController(R.id.fragment)
        mxCall?.let {
            if (it.isVideo) {
                navController.navigate(R.id.outgoingVideoCallFragment)
            } else {
                navController.navigate(R.id.outgoingVoiceCallFragment)
            }
        }
    }

    override fun getActivity(): FragmentActivity {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        Debug.e("--- onDestroy 2")
    }
}
