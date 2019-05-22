package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityProfileSettingsBinding
import vmodev.clearkeep.activities.interfaces.IProfileSettingsActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory
import javax.inject.Inject

class ProfileSettingsActivity : DaggerAppCompatActivity(), IProfileSettingsActivity {

    @Inject
    lateinit var profileSettingsActivityViewModelFactory: IProfileSettingsActivityViewModelFactory;

    private lateinit var binding: ActivityProfileSettingsBinding;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_settings, dataBindingComponent);
        binding.lifecycleOwner = this;
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
