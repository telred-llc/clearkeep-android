package vmodev.clearkeep.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivitySettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity

class SettingsActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings, dataBinding.getDataBindingComponent())
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        Navigation.findNavController(this, R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = destination.label
            showToolBarByLabel(destination.label.toString())
        }
    }

    /**
     * show gone toolbar
     */
    private fun showToolBarByLabel(label: String) {
        when (label) {
            resources.getString(R.string.label_share_app) -> {
                binding.toolbar.visibility = View.GONE
            }
            else -> {

                binding.toolbar.visibility = View.VISIBLE
            }

        }

    }

    override fun getActivity(): FragmentActivity {
        return this
    }
}
