package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentSettingDoNotDisturdBinding
import im.vector.databinding.FragmentSettingDoNotDisturdBindingImpl
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.OnSingleClickListener

class SettingDoNotDisturbFragment : DataBindingDaggerFragment(), IFragment {
    private lateinit var binding: FragmentSettingDoNotDisturdBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_do_not_disturd, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setEvent() {
        binding.rlTimeFrom.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        binding.rlTimeTo.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun getFragment(): Fragment {
        return this
    }

}