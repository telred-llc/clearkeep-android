package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.databinding.FragmentShareAppBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import im.vector.BuildConfig
import im.vector.R
import vmodev.clearkeep.ultis.OnSingleClickListener
import javax.inject.Inject


class ShareAppFragment : DataBindingDaggerFragment(), IFragment {

    private lateinit var binding: FragmentShareAppBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share_app, container, false, dataBinding.getDataBindingComponent());

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
    }

    private fun setEvent() {
        binding.btnShare.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                shareApp()
            }
        })
        binding.tvBack.setOnClickListener {
            //            findNavController().navigate(R.id.navigation_settings,null,NavOptions.Builder().setPopUpTo(R.id.next_action,true).build())
            findNavController()
                    .navigate(R.id.next_action)

        }
    }

    private fun shareApp() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, resources.getString(R.string.riot_app_name))
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, BuildConfig.CLEAR_KEEP_SERVER + BuildConfig.LINK_SHARE_APP)
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.riot_app_name)))

    }

    override fun getFragment(): Fragment {
        return this
    }


}