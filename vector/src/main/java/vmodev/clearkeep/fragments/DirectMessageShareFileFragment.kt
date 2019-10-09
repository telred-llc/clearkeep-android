package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import androidx.lifecycle.Observer
import im.vector.activity.VectorHomeActivity
import im.vector.databinding.FragmentDirectMessageBinding
import im.vector.databinding.FragmentDirectMessageShareFileBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.data.RoomMediaMessage
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageShareFileFragment
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageShareFileFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomShareFileFragmentViewModel
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.log

class DirectMessageShareFileFragment : DataBindingDaggerFragment(), IDirectMessageShareFileFragment, IListRoomRecyclerViewAdapter.ICallbackToGetUsers {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DirectMessageShareFileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                DirectMessageShareFileFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    lateinit var binding: FragmentDirectMessageShareFileBinding;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractDirectMessageShareFileFragmentViewModel>;

    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.SHARE_FILE)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var applcation: IApplication;
    private val onClickItem: PublishSubject<String> = PublishSubject.create();

    private var listDataDirect: List<RoomListUser>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_message_share_file, container, false, dataBinding.getDataBindingComponent());
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        setEvent()
    }

    private fun initComponent() {
        binding.searchDirect.setIconifiedByDefault(false);
        binding.searchDirect.isIconified = false;
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvListDirectMessage.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.rvListDirectMessage.adapter = listRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer {
            listRoomAdapter!!.getAdapter().submitList(it?.data);
            listDataDirect = it?.data
        });
        viewModelFactory.getViewModel().getSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomAdapter!!.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
    }

    private fun setEvent() {
        listRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, applcation.getUserId());
        listRoomAdapter.setOnItemClick { roomListUser, i ->
            shareFile(applcation.getUserId(), roomListUser.room!!.id)
        }
        binding.searchDirect.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { filterDataDirect(it) }
                return false
            }
        })
    }

    override fun setQuery(query: String) {
        if (!::binding.isInitialized || !::viewModelFactory.isInitialized)
            return;
        if (query.isEmpty()) {
//            binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
            viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
        } else {
            binding.rooms = viewModelFactory.getViewModel().getSearchResult();
            viewModelFactory.getViewModel().setQueryForSearch("%" + query + "%");
        }
    }

    override fun onClickItemtRoom(): Observable<String> {
        return onClickItem
    }

    override fun getUsers(userIds: Array<String>): LiveData<Resource<List<User>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragment(): Fragment {
        return this
    }

    fun shareFile(userId: String, roomId: String) {
        val roomIntent = Intent(activity, RoomActivity::class.java);
        val cachedFiles = ArrayList(RoomMediaMessage.listRoomMediaMessages(activity!!.intent))
        putCachedFiles(roomIntent, cachedFiles)
        roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, userId);
        roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId)
        roomIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        if (activity!!.isTaskRoot) {
//            roomIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
        activity!!.startActivity(roomIntent);
    }


    private fun putCachedFiles(intent: Intent, cachedFiles: List<RoomMediaMessage>) {
        if (cachedFiles.isNotEmpty()) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND_MULTIPLE

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, cachedFiles as ArrayList<RoomMediaMessage>)
            shareIntent.setExtrasClassLoader(RoomMediaMessage::class.java.classLoader)
            shareIntent.type = "*/*"
            intent.putExtra(RoomActivity.EXTRA_ROOM_INTENT, shareIntent);
        }
    }

    private fun filterDataDirect(textSearch: String) {
        var newListData = ArrayList<RoomListUser>()
        if (listDataDirect!!.isNotEmpty() && !TextUtils.isEmpty(textSearch)) {
            for (item in listDataDirect!!) {
                if (item.room!!.name.indexOf(textSearch) > -1) {
                    newListData.add(item)
                }
            }
        } else {
            newListData = (listDataDirect as ArrayList<RoomListUser>?)!!
        }
        listRoomAdapter.getAdapter().submitList(newListData);

    }

    override fun onDestroy() {
        super.onDestroy()
        onClickItem.onComplete();
    }

}