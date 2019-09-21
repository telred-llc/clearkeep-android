package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import im.vector.R
import im.vector.databinding.ActivityOtherRoomSettingsAdvancedBinding
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class OtherRoomSettingsAdvancedActivity : DataBindingDaggerActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    private lateinit var binding: ActivityOtherRoomSettingsAdvancedBinding;
    private lateinit var roomViewModel: AbstractRoomViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_room_settings_advanced);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java);

        binding.room = roomViewModel.getRoom();
        binding.lifecycleOwner = this;
        val roomId = intent.getStringExtra(ROOM_ID);
        roomViewModel.setRoomId(roomId);
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
