package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivityNewRoomBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class NewRoomActivity : DataBindingDaggerActivity(), IActivity{

    private lateinit var binding : ActivityNewRoomBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_room, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        Navigation.findNavController(this, R.id.container).addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = destination.label;
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

}
