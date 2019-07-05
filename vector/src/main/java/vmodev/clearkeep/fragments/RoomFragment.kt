package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import dagger.android.DaggerFragment
import im.vector.R
import im.vector.databinding.FragmentDirectMessageBinding
import im.vector.databinding.FragmentRoomBinding
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.activities.CreateNewConversationActivity
import vmodev.clearkeep.activities.CreateNewRoomActivity
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.IListRoomOnFragmentInteractionListener
import vmodev.clearkeep.fragments.Interfaces.IRoomFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RoomFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RoomFragment : DataBindingDaggerFragment(), IRoomFragment {
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var viewModelFactory: IRoomFragmentViewModelFactory;
    lateinit var binding: FragmentRoomBinding;
    private val onClickItem: PublishSubject<String> = PublishSubject.create();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room, container, false, dataBindingComponent);
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner;
        listRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> onClickItem.onNext(room.id)
            }
        }
        listRoomAdapter.setOnItemLongClick { room ->
        }
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        binding.buttonStartDirectChat.setOnClickListener {
            val intentNewChat = Intent(context, CreateNewRoomActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.recyclerViewListConversation.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListConversation.adapter = listRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer {
            listRoomAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomAdapter.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().setListType(arrayOf(2, 130))
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onClickRoomSettings(id: String) {
        val intent = Intent(this.activity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, id);
        startActivity(intent);
    }

    private fun onClickAddToFavourite(roomId: String) {
        binding.roomObject = viewModelFactory.getViewModel().getAddToFavouriteResult();
        viewModelFactory.getViewModel().setAddToFavourite(roomId);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickItem.onComplete();
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun setQuery(query: String) {
        if (!::binding.isInitialized || !::viewModelFactory.isInitialized)
            return;
        if (query.isEmpty()) {
            binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
            viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
        } else {
            binding.rooms = viewModelFactory.getViewModel().getSearchResult();
            viewModelFactory.getViewModel().setQueryForSearch("%" + query + "%");
        }
    }

    override fun onClickItemtRoom(): Observable<String> {
        return onClickItem;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                RoomFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
