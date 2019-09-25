package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.databinding.FragmentPrivacyPolicyBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment

class PrivacyPolicyFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentPrivacyPolicyBinding;
    private val args : PrivacyPolicyFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false, dataBinding.getDataBindingComponent());
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
