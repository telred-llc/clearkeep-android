package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import im.vector.R
import im.vector.databinding.ActivitySecurityBinding
import javax.inject.Inject

class SecurityActivity : DataBindingDaggerActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    lateinit var binding: ActivitySecurityBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_security);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
    }
}
