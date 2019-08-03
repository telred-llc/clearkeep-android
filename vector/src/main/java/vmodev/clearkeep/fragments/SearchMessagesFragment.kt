package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import im.vector.R
import im.vector.databinding.FragmentSearchMessagesBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListSearchMessageRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchMessagesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchMessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchMessagesFragment : DataBindingDaggerFragment(), ISearchFragment {
    // TODO: Rename and change types of parameters
    private var userId: String? = null
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchMessageFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;

    lateinit var binding: FragmentSearchMessagesBinding;

    private var disposableEditext: Disposable? = null;

    private val listMessage = ArrayList<MessageRoomUser>();
    private lateinit var listSearchAdapter: ListSearchMessageRecyclerViewAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_messages, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.results = searchViewModel.getSearchMessageByTextResult();

        listSearchAdapter = ListSearchMessageRecyclerViewAdapter(appExecutors = appExecutors
                , dataBindingComponent = dataBindingComponent, diffCallback = object : DiffUtil.ItemCallback<MessageRoomUser>() {
            override fun areItemsTheSame(p0: MessageRoomUser, p1: MessageRoomUser): Boolean {
                return p0.message?.id == p1.message?.id
            }

            override fun areContentsTheSame(p0: MessageRoomUser, p1: MessageRoomUser): Boolean {
                return p0.room?.get(0)?.avatarUrl == p1.room?.get(0)?.avatarUrl && p0.message?.encryptedContent == p1.message?.encryptedContent
                        && p0.user?.get(0)?.name == p1.user?.get(0)?.name;
            }
        }) { messageSearchText ->
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
        viewModelFactory.getViewModel().getLoadMessagesResult().observe(this, Observer {
            it?.data?.let {
//                viewModelFactory.getViewModel().decryptListMessage(it).observe(this, Observer {
//                    it?.data?.let {
//
//                    }
//                })
            }
        })
        viewModelFactory.getViewModel().getListMessageRoomUser().observe(this, Observer {
            it?.data?.let {
                viewModelFactory.getViewModel().decryptListMessage(it).observe(this, Observer {
                    it?.data?.let {
                        listMessage.addAll(it);
                    }
                })
            }
        })
        viewModelFactory.getViewModel().setTimeForRefreshLoadMessage(Calendar.getInstance().timeInMillis);
        getSearchViewTextChange()?.subscribe { s ->
            listSearchAdapter.submitList(listMessage.filter { messageRoomUser ->
                messageRoomUser.message?.let {
                    it.encryptedContent.contains(s)
                } ?: run {
                    false
                }
            })
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
        fun newInstance(userId: String) =
                SearchMessagesFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }

    override fun selectedFragment(query: String): ISearchFragment {
        listSearchAdapter.submitList(listMessage.filter { messageRoomUser ->
            messageRoomUser.message?.let {
                it.encryptedContent.contains(query)
            } ?: run {
                false
            }
        })
        return this;
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun unSelectedFragment() {
        disposableEditext?.dispose();
    }
}
