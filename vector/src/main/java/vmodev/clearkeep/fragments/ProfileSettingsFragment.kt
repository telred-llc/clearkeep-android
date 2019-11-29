package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.FragmentProfileSettingsBinding
import vmodev.clearkeep.activities.EditProfileActivity
import vmodev.clearkeep.activities.ExportKeyActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import java.util.*
import javax.inject.Inject

class ProfileSettingsFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractProfileSettingsActivityViewModel>
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: FragmentProfileSettingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CreateFragment", "Created")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this;
        binding.editProfileGroup.setOnClickListener {
            //            val intentEditProfile = Intent(this.activity, EditProfileActivity::class.java);
//            intentEditProfile.putExtra(EditProfileActivity.USER_ID, application.getUserId());
//            startActivity(intentEditProfile);
            activity!!.onBackPressed()
        }
        binding.callGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.calls())
        }
        binding.notificationGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.notifications())
        }
        binding.securityGroup.setOnClickListener {
            val intentSecurity = Intent(this.activity, ExportKeyActivity::class.java);
            intentSecurity.putExtra(ExportKeyActivity.USER_ID, application.getUserId());
            startActivity(intentSecurity);
        }
        binding.textViewDeactivateAccount.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.deactivateAccount());
        }
        binding.privacyPolicyGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.privacyPolicy().setUrl("https://riot.im/privacy"))
        }
        binding.termAndConditions.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.privacyPolicy().setUrl("https://matrix.org/legal/terms-and-conditions/"))
        }
        binding.copyrightGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.privacyPolicy().setUrl("https://riot.im/copyright"))
        }
        binding.reportGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.report());
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
