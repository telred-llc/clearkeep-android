package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import im.vector.R
import im.vector.databinding.ActivityRolesPermissionBinding

class RolesPermissionActivity : DataBindingDaggerActivity() {

    private lateinit var binding: ActivityRolesPermissionBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roles_permission);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
    }
}
