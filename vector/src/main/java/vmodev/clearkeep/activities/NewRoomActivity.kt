package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityNewRoomBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class NewRoomActivity : DataBindingDaggerActivity(), IActivity, ListRoomFragment.OnFragmentInteractionListener
        , ContactsFragment.OnFragmentInteractionListener {

    private lateinit var binding : ActivityNewRoomBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_room, dataBinding.getDataBindingComponent());
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

}
