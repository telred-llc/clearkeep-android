package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import im.vector.R
import im.vector.databinding.ActivityIncomingCallBinding
import vmodev.clearkeep.activities.interfaces.IActivity

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding;
    private lateinit var navController: NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent());
//        navController = findNavController(R.id.fragment);
//        intent.getStringExtra(ROOM_ID)?.let {
//            setupNavigationController(it)
//        }
    }

    private fun setupNavigationController(roomId: String) {
        val graph = navController.navInflater.inflate(R.navigation.navigation_incoming_call);
        val argOne = NavArgument.Builder().setDefaultValue(roomId).build();
        graph.addArgument("roomId", argOne);
        navController.graph = graph;
        navController.navigate(R.id.incomingCallFragment);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
