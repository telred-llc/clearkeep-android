package vmodev.clearkeep.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.leanback.widget.DiffCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import im.vector.R
import im.vector.databinding.FragmentCallHistoryBinding
import im.vector.databinding.FragmentContactsBinding
import io.reactivex.Observable
import vmodev.clearkeep.adapters.CallHistoryRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListSearchMessageRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallHistoryViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import java.util.*
import javax.inject.Inject

class CallHistoryFragment : DataBindingDaggerFragment(), IFragment {
    lateinit var binding: FragmentCallHistoryBinding;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCallHistoryViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    private lateinit var listSearchAdapter: CallHistoryRecyclerViewAdapter;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_history, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listSearchAdapter = CallHistoryRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<MessageRoomUser>() {
            override fun areItemsTheSame(oldItem: MessageRoomUser, newItem: MessageRoomUser): Boolean {
                return oldItem.message?.id == newItem.message?.id
            }

            override fun areContentsTheSame(oldItem: MessageRoomUser, newItem: MessageRoomUser): Boolean {
                return oldItem.room?.get(0)?.avatarUrl == newItem.room?.get(0)?.avatarUrl && oldItem.message?.encryptedContent == newItem.message?.encryptedContent
                        && oldItem.user?.get(0)?.name == newItem.user?.get(0)?.name;
            }
        }, dataBindingComponent = dataBinding) {

        }
        binding.rvCallHistory.adapter = listSearchAdapter;
        viewModelFactory.getViewModel().getListMessageRoomUser().observe(viewLifecycleOwner, Observer {
            it?.data?.let { it1 ->
                viewModelFactory.getViewModel().getListCallHistory(it1).observe(viewLifecycleOwner, Observer { dataResult ->
                    val sortCallHistory = dataResult.data?.sortedWith(compareBy { it.message?.createdAt })?.reversed()
                    listSearchAdapter.submitList(sortCallHistory)
                })
            }
        })
        viewModelFactory.getViewModel().setTimeForRefreshLoadMessage(Calendar.getInstance().timeInMillis);
    }

    override fun getFragment(): Fragment {
        return this
    }
}