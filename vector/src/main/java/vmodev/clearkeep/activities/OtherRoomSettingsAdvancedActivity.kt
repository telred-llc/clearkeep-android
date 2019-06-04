package vmodev.clearkeep.activities

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.util.DataUtils
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityOtherRoomSettingsAdvancedBinding
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class OtherRoomSettingsAdvancedActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    private lateinit var binding: ActivityOtherRoomSettingsAdvancedBinding;
    private lateinit var roomViewModel: AbstractRoomViewModel;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_room_settings_advanced, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);

        binding.room = roomViewModel.getRoom();
        binding.lifecycleOwner = this;
        val roomId = intent.getStringExtra(ROOM_ID);
        roomViewModel.setRoomId(roomId);
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
