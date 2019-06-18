package vmodev.clearkeep.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import im.vector.R
import im.vector.databinding.ActivityOtherRoomSettingsBinding
import vmodev.clearkeep.binding.ActivityDataBindingComponent

class OtherRoomSettings : DataBindingDaggerActivity() {

    private lateinit var binding: ActivityOtherRoomSettingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_room_settings, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        val roomId = intent.getStringExtra(ROOM_ID);
        binding.advancedGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettings, OtherRoomSettingsAdvancedActivity::class.java);
            securityIntent.putExtra(OtherRoomSettingsAdvancedActivity.ROOM_ID, roomId);
            startActivity(securityIntent);
        }
        binding.rolesAndPermissionGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettings, RolesPermissionActivity::class.java);
            startActivity(securityIntent);
        }
        binding.securityAndPrivacyGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettings, SecurityActivity::class.java);
            startActivity(securityIntent);
        }
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
