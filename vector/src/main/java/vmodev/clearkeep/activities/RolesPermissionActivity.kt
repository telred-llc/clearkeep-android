package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityRolesPermissionBinding
import vmodev.clearkeep.binding.ActivityDataBindingComponent

class RolesPermissionActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityRolesPermissionBinding;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roles_permission, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
    }
}
