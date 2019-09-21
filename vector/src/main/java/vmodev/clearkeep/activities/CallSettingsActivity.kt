package vmodev.clearkeep.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityCallSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.ICallSettingsActivity
import vmodev.clearkeep.di.AbstractCallSettingsActivityModule
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Inject

class CallSettingsActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCallSettingActivityViewModel>;

    private lateinit var binding: ActivityCallSettingsBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_settings, dataBinding.getDataBindingComponent());
        userId = if (intent.getStringExtra(USER_ID).isNullOrEmpty()) "" else intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.calls);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.switchCompatIntegratedCalling.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModelFactory.getViewModel().setChangeDeviceSettingValue(userId, if (isChecked) 1 else 0);
        }
        binding.deviceSettings = viewModelFactory.getViewModel().getDeviceSettingsResult();
        binding.deviceSettingsChanged = viewModelFactory.getViewModel().getChangeDeviceSettingsResult();
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setIdForDeviceSettingsResult(userId);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
