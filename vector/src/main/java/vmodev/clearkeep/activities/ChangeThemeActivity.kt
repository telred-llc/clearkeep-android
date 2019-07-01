package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityChangeThemeBinding
import vmodev.clearkeep.activities.interfaces.IChangeThemeActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IChangeThemeActivityViewModelFactory
import javax.inject.Inject

class ChangeThemeActivity : DataBindingDaggerActivity(), IChangeThemeActivity {

    @Inject
    lateinit var viewModelFactory: IChangeThemeActivityViewModelFactory;

    private val bindingDataComponent = ActivityDataBindingComponent(this);
    private lateinit var binding: ActivityChangeThemeBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_theme, bindingDataComponent);
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
