package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityChangeThemeBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractChangeThemeActivityViewModel
import javax.inject.Inject

class ChangeThemeActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractChangeThemeActivityViewModel>;

    private lateinit var binding: ActivityChangeThemeBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_theme, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.theme);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        userId = intent.getStringExtra(USER_ID);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.switchDarkTheme.setOnCheckedChangeListener { compoundButton, b ->
            binding.deviceSettings = viewModelFactory.getViewModel().getChangeThemeResult();
            if (b) {
                viewModelFactory.getViewModel().setChangeTheme(userId, R.style.DarkTheme);
            } else {
                viewModelFactory.getViewModel().setChangeTheme(userId, R.style.LightTheme);
            }
        }
        viewModelFactory.getViewModel().getDeviceSettingsResult().observe(this, Observer {
            it?.data?.let {
                binding.switchDarkTheme.isChecked = it.theme == R.style.DarkTheme;
            }
        })
        binding.deviceSettings = viewModelFactory.getViewModel().getDeviceSettingsResult();
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setDeviceSettingsId(userId);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }


    companion object {
        const val USER_ID = "USER_ID";
    }
}
