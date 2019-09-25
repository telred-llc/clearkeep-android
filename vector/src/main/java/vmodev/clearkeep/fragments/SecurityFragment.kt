package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import im.vector.R
import im.vector.databinding.FragmentSecurityBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment
import javax.inject.Inject

class SecurityFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    private lateinit var binding: FragmentSecurityBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_security, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
