package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemMessageSearchBinding
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser

class ListSearchMessageRecyclerViewAdapter constructor(appExecutors: AppExecutors,
                                                       private val dataBindingComponent: IDataBindingComponent,
                                                       diffCallback: DiffUtil.ItemCallback<MessageRoomUser>,
                                                       private val itemClick: (MessageRoomUser) -> Unit?)
    : ListAdapter<MessageRoomUser, DataBoundViewHolder<ItemMessageSearchBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemMessageSearchBinding> {
        val binding = DataBindingUtil.inflate<ItemMessageSearchBinding>(LayoutInflater.from(p0.context),
                R.layout.item_message_search, p0, false, dataBindingComponent.getDataBindingComponent());
        binding.root.setOnClickListener { v ->
            binding.result?.let {
                itemClick?.invoke(it)
            }
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemMessageSearchBinding>, p1: Int) {
        p0.binding.result = getItem(p1);
    }
}