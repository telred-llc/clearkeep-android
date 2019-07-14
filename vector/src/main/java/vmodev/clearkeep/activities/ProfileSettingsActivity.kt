package vmodev.clearkeep.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.media.JetPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.KeysBackupManageActivity
import im.vector.activity.KeysBackupSetupActivity
import im.vector.databinding.ActivityProfileSettingsBinding
import vmodev.clearkeep.activities.interfaces.IProfileSettingsActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory
import javax.inject.Inject

class ProfileSettingsActivity : DataBindingDaggerActivity(), IProfileSettingsActivity {

    @Inject
    lateinit var profileSettingsActivityViewModelFactory: IProfileSettingsActivityViewModelFactory;

    private lateinit var binding: ActivityProfileSettingsBinding;
    private lateinit var userId: String;
//    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_settings, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.setting);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        userId = if (intent.getStringExtra(USER_ID).isNullOrEmpty()) "" else intent.getStringExtra(USER_ID);
        binding.lifecycleOwner = this;
        binding.editProfileGroup.setOnClickListener {
            val intentEditProfile = Intent(this, EditProfileActivity::class.java);
            intentEditProfile.putExtra(EditProfileActivity.USER_ID, userId);
            startActivity(intentEditProfile);
        }
        binding.callGroup.setOnClickListener {
            val intentCalls = Intent(this, CallSettingsActivity::class.java);
            intentCalls.putExtra(CallSettingsActivity.USER_ID, userId);
            startActivity(intentCalls)
        }
        binding.notificationGroup.setOnClickListener {
            val intentNotifications = Intent(this, NotificationSettingsActivity::class.java);
            intentNotifications.putExtra(NotificationSettingsActivity.USER_ID, userId);
            startActivity(intentNotifications);
        }
        binding.securityGroup.setOnClickListener {
            val intentSecurity = Intent(this, ExportKeyActivity::class.java);
            startActivity(intentSecurity);
        }
        binding.themeGroup.setOnClickListener {
            val intentTheme = Intent(this, ChangeThemeActivity::class.java);
            intentTheme.putExtra(ChangeThemeActivity.USER_ID, userId);
            startActivity(intentTheme);
        }
        binding.textViewDeactivateAccount.setOnClickListener {
            val deactivateAccountIntent = Intent(this, DeactivateAccountActivity::class.java);
            startActivity(deactivateAccountIntent);
        }
        binding.privacyPolicyGroup.setOnClickListener {
            val privacyPolicyActivityIntent = Intent(this, PrivacyPolicyActivity::class.java);
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.TITLE, resources.getString(R.string.privacy_and_policy));
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.URL, "https://riot.im/privacy");
            startActivity(privacyPolicyActivityIntent);
        }
        binding.termAndConditions.setOnClickListener {
            val privacyPolicyActivityIntent = Intent(this, PrivacyPolicyActivity::class.java);
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.TITLE, resources.getString(R.string.term_and_conditions));
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.URL, "https://matrix.org/legal/terms-and-conditions/");
            startActivity(privacyPolicyActivityIntent);
        }
        binding.copyrightGroup.setOnClickListener {
            val privacyPolicyActivityIntent = Intent(this, PrivacyPolicyActivity::class.java);
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.TITLE, resources.getString(R.string.copyright));
            privacyPolicyActivityIntent.putExtra(PrivacyPolicyActivity.URL, "https://riot.im/copyright");
            startActivity(privacyPolicyActivityIntent);
        }
        binding.reportGroup.setOnClickListener {
            val reportActivityIntent = Intent(this, ReportActivity::class.java);
            reportActivityIntent.putExtra(ReportActivity.USER_ID, userId);
            startActivity(reportActivityIntent);
        }
        binding.textViewClearCache.setOnClickListener {
            Matrix.getInstance(applicationContext).reloadSessions(applicationContext);
        }
        binding.backupKeyGroup.setOnClickListener {
            val intentBackup = Intent(this, BackupKeyActivity::class.java);
            intentBackup.putExtra(BackupKeyActivity.USER_ID, userId);
            startActivity(intentBackup);
        }
//        binding.backupKeyOldGroup.setOnClickListener {
//            val intentBackup = Intent(this, KeysBackupManageActivity::class.java);
//            startActivity(intentBackup);
//        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
