package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.databinding.FragmentOtherRoomSettingsAdvancedBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class OtherRoomSettingsAdvancedFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    private lateinit var binding: FragmentOtherRoomSettingsAdvancedBinding;
    private lateinit var roomViewModel: AbstractRoomViewModel;
    private val args : OtherRoomSettingsAdvancedFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_room_settings_advanced, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.room = roomViewModel.getRoom();
        binding.lifecycleOwner = this;
        args.roomId?.let { roomViewModel.setRoomId(it) }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
