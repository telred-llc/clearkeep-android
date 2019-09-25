package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentRolesPermissionBinding
import vmodev.clearkeep.fragments.DataBindingDaggerFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class RolesPermissionFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentRolesPermissionBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_roles_permission, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
