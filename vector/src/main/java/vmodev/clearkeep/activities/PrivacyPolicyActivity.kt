package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.databinding.ActivityPrivacyPolicyBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IPrivacyPolicyActivity
import vmodev.clearkeep.fragments.DataBindingDaggerFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class PrivacyPolicyActivity : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: ActivityPrivacyPolicyBinding;
    private val args : PrivacyPolicyActivityArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_privacy_policy, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webViewPrivacyPolicy.settings.javaScriptEnabled = true;
        args.url?.let { binding.webViewPrivacyPolicy.loadUrl(it); }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
