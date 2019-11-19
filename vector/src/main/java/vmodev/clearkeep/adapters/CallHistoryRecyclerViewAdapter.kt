package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemCallHistoryBinding
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser

class CallHistoryRecyclerViewAdapter constructor(appExecutors: AppExecutors,
                                                 private val dataBindingComponent: IDataBindingComponent,
                                                 diffCallback: DiffUtil.ItemCallback<MessageRoomUser>,
                                                 private val itemClick: (MessageRoomUser) -> Unit?) :
        ListAdapter<MessageRoomUser, DataBoundViewHolder<ItemCallHistoryBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemCallHistoryBinding> {
        val binding = DataBindingUtil.inflate<ItemCallHistoryBinding>(LayoutInflater.from(p0.context),
                R.layout.item_call_history, p0, false, dataBindingComponent.getDataBindingComponent());
        binding.root.setOnClickListener { v ->
            binding.result?.let {
                itemClick?.invoke(it)
            }
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemCallHistoryBinding>, p1: Int) {
        p0.binding.result = getItem(p1);
    }

    }