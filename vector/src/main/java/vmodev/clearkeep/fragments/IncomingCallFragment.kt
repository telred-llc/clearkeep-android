package vmodev.clearkeep.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import im.vector.R
import im.vector.databinding.FragmentIncomingCallBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment

/**
 * A simple [Fragment] subclass.
 */
class IncomingCallFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentIncomingCallBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incoming_call, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewAccept.setOnClickListener {
            findNavController().navigate(IncomingCallFragmentDirections.inProgressCall());
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
