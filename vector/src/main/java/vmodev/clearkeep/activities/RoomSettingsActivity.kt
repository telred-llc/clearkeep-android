package vmodev.clearkeep.activities

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivityRoomSettingsBinding
import im.vector.extensions.getColorFromAttr
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.RoomSettingsFragment
import vmodev.clearkeep.ultis.RxEventBus

class RoomSettingsActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityRoomSettingsBinding
    private var roomId: String? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra(ROOM_ID)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_settings, dataBinding.getDataBindingComponent())
        binding.toolbar.setTitleTextColor(getColorFromAttr(R.attr.color_text_tool_bar))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val upArrow = resources.getDrawable(R.drawable.ic_back, null)
        upArrow.setColorFilter(getColorFromAttr(R.attr.color_tint_img_back), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        val navController = Navigation.findNavController(this, R.id.fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.title = destination.label
        }
        disposable = RxEventBus.instanceOf<String>().getData()?.subscribe {
            binding.toolbar.title = it
        }
        val graph = navController.navInflater.inflate(R.navigation.navigation_room_settings)
        val navArgs = NavArgument.Builder().setDefaultValue(roomId).build()
        graph.addArgument("roomId", navArgs)
        navController.graph = graph
        binding.toolbar.setTitle(R.string.settings)
    }

    override fun getActivity(): FragmentActivity {
        return this
    }

    companion object {
        const val ROOM_ID = "ROOM_ID"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (currentFragment is RoomSettingsFragment) {
            currentFragment.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            it.dispose()
        }
    }
}
