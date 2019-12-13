package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration

import im.vector.R
import im.vector.databinding.FragmentSearchMessagesBinding
import im.vector.databinding.FragmentSearchMessagesInRoomBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListSearchMessageInRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListSearchMessageRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageInroomFragmentViewModel
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"
private const val ROOM_ID = "ROOM_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchMessagesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchMessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchMessagesInRoomFragment : DataBindingDaggerFragment(), ISearchFragment {
    // TODO: Rename and change types of parameters
    private var userId: String? = null
    private var roomId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var queryText : String? = null

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchMessageInroomFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;

    lateinit var binding: FragmentSearchMessagesInRoomBinding;
    private var disposableEditText: Disposable? = null;
    private val listMessage = ArrayList<MessageRoomUser>();
    private lateinit var listSearchAdapter: ListSearchMessageInRoomRecyclerViewAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
            roomId = it.getString(ROOM_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_messages_in_room, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        viewModelFactory.getViewModel().getRoomId(roomId!!)
        listSearchAdapter = ListSearchMessageInRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<MessageRoomUser>() {
            override fun areItemsTheSame(p0: MessageRoomUser, p1: MessageRoomUser): Boolean {
                return p0.message?.id == p1.message?.id
            }

            override fun areContentsTheSame(p0: MessageRoomUser, p1: MessageRoomUser): Boolean {
                return p0.room?.get(0)?.avatarUrl == p1.room?.get(0)?.avatarUrl && p0.message?.encryptedContent == p1.message?.encryptedContent
                        && p0.user?.get(0)?.name == p1.user?.get(0)?.name;
            }
        }, dataBindingComponent = dataBinding) { messageSearchText ->
            val intentRoom = Intent(this.activity, RoomActivity::class.java);
            messageSearchText.room?.let {
                intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it[0].id);
            }

            intentRoom.putExtra(RoomActivity.EXTRA_MATRIX_ID, userId);
            messageSearchText.message?.let {
                intentRoom.putExtra(RoomActivity.EXTRA_EVENT_ID, it.id);
            }

            startActivity(intentRoom);
        };
        binding.recyclerView.adapter = listSearchAdapter;
        viewModelFactory.getViewModel().getListMessageRoomUserInRoom().observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                viewModelFactory.getViewModel().decryptListMessage(it).observe(viewLifecycleOwner, Observer {
                    it?.data?.let {
                        listMessage.clear()
                        listMessage.addAll(it)
                    }
                })
            }
        })
        binding.listMessage = 0;
        viewModelFactory.getViewModel().setTimeForRefreshLoadMessage(Calendar.getInstance().timeInMillis);
        getSearchViewTextChange()?.subscribe { s ->
            listSearchAdapter.getSearchText(s)
            val listFilter = listMessage.filter { messageRoomUser ->
                messageRoomUser.message?.let {
                    if (s.isNullOrEmpty())
                        false;
                    else
                        it.encryptedContent.toLowerCase(Locale.US).contains(s.toLowerCase(Locale.US))
                } ?: run {
                    false
                }
            }
            listSearchAdapter.submitList(listFilter)
            binding.listMessage =  listFilter.size
            listSearchAdapter.notifyDataSetChanged()
        }
        binding.nestScroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            hideKeyboard()
        }
        binding.lifecycleOwner = viewLifecycleOwner;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun getSearchViewTextChange(): Observable<String>? {
        return listener?.getSearchViewTextChange();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun getSearchViewTextChange(): Observable<String>;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param2 Parameter 1.
         * @return A new instance of fragment SearchMessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String,roomId : String) =
                SearchMessagesInRoomFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                        putString(ROOM_ID, roomId)
                    }
                }
    }

    override fun selectedFragment(query: String): ISearchFragment {
        queryText = query
        val listMessageFilter = listMessage.filter { messageRoomUser ->
            messageRoomUser.message?.let {
                if (query.isNullOrEmpty())
                    false;
                else
                    listSearchAdapter.getSearchText(query)
                it.encryptedContent.toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))
            } ?: run {
                false
            }
        }
        if (query.isNotEmpty()){
            listSearchAdapter.submitList(listMessageFilter)
            binding.listMessage = listMessageFilter.size
        }
        return this;
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun unSelectedFragment() {
        disposableEditText?.dispose();
    }
}
