package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityRoomFilesListBinding
import im.vector.databinding.ActivityRoomMemberListBinding
import im.vector.fragments.VectorSearchRoomFilesListFragment
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.adapters.ListFileRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.File
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Inject

class RoomfilesListActivity : DataBindingDaggerActivity(), IRoomFileListActivity {

    @Inject
    lateinit var viewModelFactory: IRoomFileListActivityViewModelFactory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityRoomFilesListBinding;
    private lateinit var listFileAdapter: ListFileRecyclerViewAdapter;

    private lateinit var roomId: String;
    private lateinit var session: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_files_list, dataBindingComponent);
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
