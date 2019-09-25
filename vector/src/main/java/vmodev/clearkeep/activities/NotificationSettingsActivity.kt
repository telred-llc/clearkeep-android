package vmodev.clearkeep.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityNotificationSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.DataBindingDaggerFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractNotificationSettingsActivityViewModel
import javax.inject.Inject

class NotificationSettingsActivity : DataBindingDaggerFragment(), IFragment {
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractNotificationSettingsActivityViewModel>;
    @Inject
    lateinit var application : IApplication;
    private lateinit var binding: ActivityNotificationSettingsBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_notification_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deviceSettings = viewModelFactory.getViewModel().getDeviceSettingsMergeResult();
        setOnChangeSwitch();
        binding.lifecycleOwner = viewLifecycleOwner;
        viewModelFactory.getViewModel().getDeviceSettingsMergeResult().observe(this, Observer {
            it?.data?.let {
                binding.switchCompatNotificationOnThisDevice.isChecked = it.notificationOnThisDevice.compareTo(0) != 0;
                binding.switchCompatPinRoomsWithMissedNotifications.isChecked = it.pinRoomWithMissedNotifications.compareTo(0) != 0;
                binding.switchCompatPinRoomsWithUnreadMessages.isChecked = it.pinRoomWithUnreadMessages.compareTo(0) != 0;
                binding.switchCompatShowDecryptedContent.isChecked = it.showDecryptedContent.compareTo(0) != 0;
            }
        })
        viewModelFactory.getViewModel().setFindDeviceSettings(application.getUserId());
    }

    private fun setOnChangeSwitch() {
        binding.switchCompatNotificationOnThisDevice.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangeNotificationOnThisDevice(application.getUserId(), if (isChecked) 1 else 0) }
        binding.switchCompatShowDecryptedContent.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangeShowDecryptedContent(application.getUserId(), if (isChecked) 1 else 0) }
        binding.switchCompatPinRoomsWithMissedNotifications.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangePinRoomsMissedNotifications(application.getUserId(), if (isChecked) 1 else 0) }
        binding.switchCompatPinRoomsWithUnreadMessages.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangePinRoomsUnreadNotifications(application.getUserId(), if (isChecked) 1 else 0) }
    }

    override fun getFragment(): Fragment {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
