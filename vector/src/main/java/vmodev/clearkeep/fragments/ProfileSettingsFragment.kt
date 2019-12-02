package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
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
import vmodev.clearkeep.activities.ExportKeyActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.ultis.SharedPreferencesUtils
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import java.util.*
import javax.inject.Inject

class ProfileSettingsFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractProfileSettingsActivityViewModel>

    private lateinit var binding: FragmentProfileSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_settings, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.editProfileGroup.setOnClickListener {
            activity!!.onBackPressed()
        }
        binding.callGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.calls())
        }
        binding.notificationGroup.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.notifications())
        }
        binding.securityGroup.setOnClickListener {
            val intentSecurity = Intent(this.activity, ExportKeyActivity::class.java)
            intentSecurity.putExtra(ExportKeyActivity.USER_ID, application.getUserId())
            startActivity(intentSecurity)
        }
        binding.textViewDeactivateAccount.setOnClickListener {
            findNavController().navigate(ProfileSettingsFragmentDirections.deactivateAccount())
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
            findNavController().navigate(ProfileSettingsFragmentDirections.report())
        }
        binding.feedBackGroup.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                findNavController().navigate(ProfileSettingsFragmentDirections.feedBack())
            }
        })
        binding.textViewClearCache.setOnClickListener {
            Matrix.getInstance(application.getApplication()).reloadSessions(application.getApplication(), false)
        }
        binding.switchCompatChangeTheme.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                viewModelFactory.getViewModel().setChangeTheme(R.style.DarkTheme)
            } else {
                viewModelFactory.getViewModel().setChangeTheme(R.style.LightTheme)
            }
        }
        viewModelFactory.getViewModel().getThemeResult().observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                when(it.theme) {
                    R.style.DarkTheme -> {
                        binding.switchCompatChangeTheme.isChecked = true
                        SharedPreferencesUtils.putBoolean(activity, "THEME_DARK", true)
                    }
                    else -> {
                        binding.switchCompatChangeTheme.isChecked = false
                        SharedPreferencesUtils.putBoolean(activity, "THEME_DARK", false)
                    }
                }
            }
        })
        binding.lifecycleOwner = this
        viewModelFactory.getViewModel().setTimeToGetTheme(Calendar.getInstance().timeInMillis)
    }


    override fun getFragment(): Fragment {
        return this
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
