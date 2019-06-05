package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityCallSettingsBinding
import vmodev.clearkeep.activities.interfaces.ICallSettingsActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallSettingsActivityViewModelFactory
import javax.inject.Inject

class CallSettingsActivity : DaggerAppCompatActivity(), ICallSettingsActivity {

    @Inject
    lateinit var viewModelFactory: ICallSettingsActivityViewModelFactory;

    private lateinit var binding: ActivityCallSettingsBinding;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_settings, dataBindingComponent);
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
