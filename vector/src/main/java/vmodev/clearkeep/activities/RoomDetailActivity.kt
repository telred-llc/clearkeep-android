package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import im.vector.R
import im.vector.databinding.ActivityRoomDetailBinding
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.fragments.ClearKeepMessageListFragment
import javax.inject.Inject

class RoomDetailActivity : DataBindingDaggerActivity(), IActivity, ClearKeepMessageListFragment.VectorMessageListFragmentListener {

    private lateinit var binding: ActivityRoomDetailBinding;
    private var roomId: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra(ROOM_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_detail, dataBinding.getDataBindingComponent());
        roomId?.let {
            val messageListFragment =
                    ClearKeepMessageListFragment.newInstance(application.getUserId(), it, null, null, org.matrix.androidsdk.R.layout.fragment_matrix_message_list_fragment);
            supportFragmentManager.beginTransaction().add(R.id.frame_layout_fragment_container,messageListFragment, "").commitNow();
        } ?: run {
            finish();
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }

    override fun showPreviousEventsLoadingWheel() {

    }

    override fun hidePreviousEventsLoadingWheel() {

    }

    override fun showNextEventsLoadingWheel() {

    }

    override fun hideNextEventsLoadingWheel() {

    }

    override fun showMainLoadingWheel() {

    }

    override fun hideMainLoadingWheel() {

    }

    override fun onSelectedEventChange(currentSelectedEvent: Event?) {

    }
}
