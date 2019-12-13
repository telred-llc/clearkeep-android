package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.FragmentSearchFilesBinding
import im.vector.databinding.FragmentSearchFilesInRoomBinding
import im.vector.extensions.hideKeyboard
import im.vector.util.SlidableMediaInfo
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.message.ImageMessage
import org.matrix.androidsdk.rest.model.message.Message
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListSearchFileRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListSearchMessageRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.jsonmodels.FileContent
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesInRoomFragmentViewModel
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ROOM_ID = "ROOM_ID"
private const val USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchFilesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchFilesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchFilesInRoomFragment : DataBindingDaggerFragment(), ISearchFragment {
    // TODO: Rename and change types of parameters
    private var userId: String? = null
    private var roomId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchFilesInRoomFragmentViewModel>
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: FragmentSearchFilesInRoomBinding;
    private var disposable: Disposable? = null;
    private val listMessage = ArrayList<MessageRoomUser>();
    private var listFilter: List<MessageRoomUser>? = null

    private lateinit var listSearchAdapter: ListSearchFileRecyclerViewAdapter;
    private lateinit var gson: Gson
    private var imageMessage: ImageMessage? = null
    private var currentSearch: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "");
            roomId = it.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_files_in_room, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun getSearchViewTextChange(): Observable<String>? {
        return listener?.getSearchViewTextChange();
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory.getViewModel().getRoomId(roomId!!)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        gson = Gson()
        listSearchAdapter = ListSearchFileRecyclerViewAdapter(appExecutors = appExecutors, gson = gson, diffCallback = object : DiffUtil.ItemCallback<MessageRoomUser>() {
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
                    listMessage.clear()
                    it?.data?.let {
                        listMessage.addAll(it);
                        currentSearch?.let { it1 -> filterFile(it1) }
                    }
                })
            }
        })
        binding.listMessage = 0;
        viewModelFactory.getViewModel().setTimeForRefreshLoadMessage(Calendar.getInstance().timeInMillis);
        getSearchViewTextChange()?.subscribe { s ->
            filterFile(s)
        }
        binding.lifecycleOwner = viewLifecycleOwner;
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFilesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String,roomId : String) =
                SearchFilesInRoomFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                        putString(ROOM_ID, roomId)
                    }
                }
    }

    override fun selectedFragment(query: String): ISearchFragment {
        currentSearch = query
        filterFile(query)
        return this;
    }

    private fun filterFile(query: String) = if (!query.isBlank() && listMessage.isNotEmpty()) {
        val listFilter = listMessage.filter { messageRoomUser ->
            messageRoomUser.message?.let {
                imageMessage = JsonUtils.toImageMessage(gson.fromJson(it.encryptedContent, JsonElement::class.java))
                if (imageMessage?.body.isNullOrEmpty())
                    false;
                else
                    imageMessage?.body?.toLowerCase(Locale.US)?.contains(query.toLowerCase(Locale.US))
            } ?: run {
                false
            }
        }
        listSearchAdapter.submitList(listFilter)
        binding.listMessage =  listFilter.size
        listSearchAdapter.notifyDataSetChanged()

    } else {
        listSearchAdapter.submitList(null)
        binding.listMessage = 0
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun unSelectedFragment() {
        disposable?.dispose();
    }
}
