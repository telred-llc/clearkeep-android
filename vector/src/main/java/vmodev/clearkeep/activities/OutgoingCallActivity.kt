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
import vmodev.clearkeep.activities.interfaces.IActivity

class OutgoingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityOutgoingCallBinding
    private lateinit var navController: NavController
    private var mxCall: IMXCall? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_outgoing_call, dataBinding.getDataBindingComponent())
        navController = findNavController(R.id.fragment)
        mxCall = CallsManager.getSharedInstance().activeCall
        if (mxCall != null) {
            if (mxCall!!.isVideo) {
                navController.navigate(R.id.outgoingVideoCallFragment)
            } else {
                navController.navigate(R.id.outgoingVoiceCallFragment)
            }
        }
    }

    override fun getActivity(): FragmentActivity {
        return this
    }

}
