package vmodev.clearkeep.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivitySettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity

class SettingsActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivitySettingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.getWindow();
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_hint_text_color_light)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        Navigation.findNavController(this, R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = destination.label;
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
