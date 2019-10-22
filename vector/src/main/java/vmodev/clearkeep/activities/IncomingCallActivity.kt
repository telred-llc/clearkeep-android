package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityIncomingCallBinding
import vmodev.clearkeep.activities.interfaces.IActivity

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent());
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
