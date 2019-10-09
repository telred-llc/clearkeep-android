package vmodev.clearkeep.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityCreateNewRoomBinding
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject

class CreateNewRoomFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewRoomActivityViewModel>;

    private lateinit var binding: ActivityCreateNewRoomBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_create_new_room, container, false, dataBinding.getDataBindingComponent());
        binding.lifecycleOwner = this;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.room = viewModelFactory.getViewModel().createNewRoomResult();
        viewModelFactory.getViewModel().createNewRoomResult().observe(this, Observer{
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.textViewCreate.setText(R.string.creating);
                        binding.cardViewCreate.isClickable = false;
                    }
                    Status.SUCCESS -> {
                        binding.textViewCreate.setText(R.string.create);
                        binding.cardViewCreate.isClickable = true;
                        it.data?.let {
                            findNavController().navigate(CreateNewRoomFragmentDirections.inviteUsersToRoom().setRoomId(it.id));

                        }
                    }
                    Status.ERROR -> {
                        binding.textViewCreate.setText(R.string.create);
                        binding.cardViewCreate.isClickable = true;
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        })
        binding.editTextRoomName.textChanges().subscribe {
            if (!it.toString().isNullOrEmpty()) {
                binding.cardViewCreate.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.app_green, null));
                binding.cardViewCreate.isClickable = true;
            } else {
                binding.cardViewCreate.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.button_disabled_text_color, null));
                binding.cardViewCreate.isClickable = false;
            }
        }
        binding.cardViewCreate.setOnClickListener { v ->
            if (binding.editTextRoomTopic.text.isNullOrEmpty()) binding.editTextRoomTopic.text = binding.editTextRoomName.text;
            viewModelFactory.getViewModel().setCreateNewRoom(binding.editTextRoomName.text.toString(), binding.editTextRoomTopic.text.toString(), if (binding.switchRoomVisibility.isChecked) "public" else "private")
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
