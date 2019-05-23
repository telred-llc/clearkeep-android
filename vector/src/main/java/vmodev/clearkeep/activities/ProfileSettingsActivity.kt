package vmodev.clearkeep.activities

import android.content.Intent
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
    private lateinit var userId: String;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_settings, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.setting);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        userId = if (intent.getStringExtra(USER_ID).isNullOrEmpty()) "" else intent.getStringExtra(USER_ID);
        binding.lifecycleOwner = this;
        binding.editProfileGroup.setOnClickListener {
            val intentEditProfile = Intent(this, EditProfileActivity::class.java);
            intentEditProfile.putExtra(EditProfileActivity.USER_ID, userId);
            startActivity(intentEditProfile);
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
