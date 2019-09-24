package vmodev.clearkeep.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityProfileSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.DataBindingDaggerFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import java.util.*
import javax.inject.Inject

class ProfileSettingsActivity : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractProfileSettingsActivityViewModel>
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: ActivityProfileSettingsBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_profile_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this;
        binding.editProfileGroup.setOnClickListener {
            val intentEditProfile = Intent(this.activity, EditProfileActivity::class.java);
            intentEditProfile.putExtra(EditProfileActivity.USER_ID, application.getUserId());
            startActivity(intentEditProfile);
        }
        binding.callGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.calls())
        }
        binding.notificationGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.notifications())
        }
        binding.securityGroup.setOnClickListener {
            val intentSecurity = Intent(this.activity, ExportKeyActivity::class.java);
            intentSecurity.putExtra(ExportKeyActivity.USER_ID, application.getUserId());
            startActivity(intentSecurity);
        }
        binding.textViewDeactivateAccount.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.deactivateAccount());
        }
        binding.privacyPolicyGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.privacyPolicy().setUrl("https://riot.im/privacy"))
        }
        binding.termAndConditions.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.privacyPolicy().setUrl("https://matrix.org/legal/terms-and-conditions/"))
        }
        binding.copyrightGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.privacyPolicy().setUrl("https://riot.im/copyright"))
        }
        binding.reportGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsActivityDirections.report());
        }
        binding.textViewClearCache.setOnClickListener {
            Matrix.getInstance(application.getApplication()).reloadSessions(application.getApplication(), false);
        }
        binding.switchCompatChangeTheme.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                viewModelFactory.getViewModel().setChangeTheme(R.style.DarkTheme);
            } else {
                viewModelFactory.getViewModel().setChangeTheme(R.style.LightTheme)
            }
        }
        viewModelFactory.getViewModel().getThemeResult().observe(this, Observer {
            it?.data?.let {
                binding.switchCompatChangeTheme.isChecked = it.theme == R.style.DarkTheme;
            }
        })
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setTimeToGetTheme(Calendar.getInstance().timeInMillis);
    }

    override fun getFragment(): Fragment {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
