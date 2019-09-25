package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.databinding.FragmentOtherRoomSettingsBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment

class OtherRoomSettingsFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentOtherRoomSettingsBinding;
    private val args : OtherRoomSettingsFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_room_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.advancedGroup.setOnClickListener { v ->
            args.roomId?.let { findNavController().navigate(OtherRoomSettingsFragmentDirections.otherRoomSettingsAdvanced().setRoomId(it)) }
        }
        binding.rolesAndPermissionGroup.setOnClickListener { v ->
            findNavController().navigate(OtherRoomSettingsFragmentDirections.role());
        }
        binding.securityAndPrivacyGroup.setOnClickListener { v ->
            findNavController().navigate(OtherRoomSettingsFragmentDirections.security());
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
