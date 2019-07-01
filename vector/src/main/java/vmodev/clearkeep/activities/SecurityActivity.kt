package vmodev.clearkeep.activities

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivitySearchBinding
import im.vector.databinding.ActivitySecurityBinding
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import javax.inject.Inject

class SecurityActivity : DataBindingDaggerActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    lateinit var binding: ActivitySecurityBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_security, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
    }
}
