package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivityNewRoomBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class NewRoomActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityNewRoomBinding;
    private lateinit var navController: NavController;
    private var startWith: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startWith = intent.getIntExtra(START_WITH, 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_room, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        navController = Navigation.findNavController(this, R.id.container);
        when (startWith) {
            0 -> {
                navController.navigate(R.id.findAndCreateMewConversationFragment);
            }
            1 -> {
                navController.navigate(R.id.navigationNewRoom);
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = destination.label;
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val START_WITH = "START_WITH";
    }
}
