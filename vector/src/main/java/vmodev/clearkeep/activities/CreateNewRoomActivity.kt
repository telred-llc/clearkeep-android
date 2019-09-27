package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityCreatNewRoomBinding
import im.vector.databinding.ActivityCreateNewRoomBinding
import vmodev.clearkeep.activities.DataBindingDaggerActivity
import vmodev.clearkeep.activities.interfaces.IActivity

class CreateNewRoomActivity : DataBindingDaggerActivity(),IActivity {
    private lateinit var binding : ActivityCreatNewRoomBinding
    override fun getActivity(): FragmentActivity {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_creat_new_room, dataBinding.getDataBindingComponent());
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
