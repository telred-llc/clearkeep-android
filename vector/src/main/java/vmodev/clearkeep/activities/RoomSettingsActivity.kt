package vmodev.clearkeep.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavArgs
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivityRoomSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.RoomSettingsFragment
import vmodev.clearkeep.ultis.RxEventBus

class RoomSettingsActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding : ActivityRoomSettingsBinding;
    private var roomId : String? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra(ROOM_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_settings, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        val navController = Navigation.findNavController(this, R.id.fragment);
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.title = destination.label;
        }
        val graph = navController.navInflater.inflate(R.navigation.navigation_room_settings);
        val navArgs = NavArgument.Builder().setDefaultValue(roomId).build();
        graph.addArgument("roomId", navArgs);
        navController.graph = graph;
        binding.toolbar.setTitle(R.string.settings);
        RxEventBus.instanceOf<String>().getData()?.subscribe {
         Log.d("XXXXX",it)
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object{
        const val ROOM_ID = "ROOM_ID";
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if(currentFragment is RoomSettingsFragment){
            currentFragment.onActivityResult(requestCode,resultCode,data)
        }

    }
}
