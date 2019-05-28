package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityNotificationSettingsBinding
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory
import javax.inject.Inject

class NotificationSettingsActivity : DaggerAppCompatActivity(), INotificationSettingsActivity {

    @Inject
    lateinit var viewModelFactory: INotificationSettingsActivityViewModelFactory;
    lateinit var userId: String;
    private lateinit var binding: ActivityNotificationSettingsBinding;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_settings, dataBindingComponent);
        userId = if (intent.getStringExtra(USER_ID).isNullOrEmpty()) "" else intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.notifications);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.deviceSettings = viewModelFactory.getViewModel().getDeviceSettingsMergeResult();
        binding.switchCompatNotificationOnThisDevice.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangeNotificationOnThisDevice(userId, if (isChecked) 1 else 0) }
        binding.switchCompatShowDecryptedContent.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangeShowDecryptedContent(userId, if (isChecked) 1 else 0) }
        binding.switchCompatPinRoomsWithMissedNotifications.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangePinRoomsMissedNotifications(userId, if (isChecked) 1 else 0) }
        binding.switchCompatPinRoomsWithUnreadMessages.setOnCheckedChangeListener { buttonView, isChecked -> viewModelFactory.getViewModel().setChangePinRoomsUnreadNotifications(userId, if (isChecked) 1 else 0) }
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setFindDeviceSettings(userId)
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
