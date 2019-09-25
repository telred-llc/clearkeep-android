package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentCallSettingsBinding
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Inject

class CallSettingsFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCallSettingActivityViewModel>;
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: FragmentCallSettingsBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchCompatIntegratedCalling.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModelFactory.getViewModel().setChangeDeviceSettingValue(application.getUserId(), if (isChecked) 1 else 0);
        }
        binding.deviceSettings = viewModelFactory.getViewModel().getDeviceSettingsResult();
        binding.deviceSettingsChanged = viewModelFactory.getViewModel().getChangeDeviceSettingsResult();
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setIdForDeviceSettingsResult(application.getUserId());
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
