package vmodev.clearkeep.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityRoomFilesListBinding
import im.vector.fragments.VectorSearchRoomFilesListFragment
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.adapters.ListFileRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFileListActivityViewModel
import javax.inject.Inject

class RoomfilesListActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomFileListActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityRoomFilesListBinding;
    private lateinit var listFileAdapter: ListFileRecyclerViewAdapter;

    private lateinit var roomId: String;
    private lateinit var session: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_files_list);
        roomId = intent.getStringExtra(ROOM_ID);
        session = Matrix.getInstance(applicationContext).defaultSession;
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.file)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }

        binding.lifecycleOwner = this;
        val fileList = VectorSearchRoomFilesListFragment.newInstance(session.myUserId, roomId, R.layout.fragment_matrix_message_list_fragment);
        val trans = supportFragmentManager.beginTransaction();
        trans.replace(R.id.fragment_container, fileList);
        trans.commit();
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
