package vmodev.clearkeep.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.ItemMessageBinding
import im.vector.databinding.ItemMessageReceivedWithAvatarBinding
import im.vector.databinding.ItemMessageReceivedWithNoAvatarBinding
import im.vector.databinding.ItemMessageSentBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Message

class ListMessageRecyclerViewAdapter constructor(private val ownerUserId: String, appExecutors: AppExecutors, private val dataBindingComponent: DataBindingComponent, diffCall: DiffUtil.ItemCallback<Message>)
    : ListAdapter<Message, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder<Message>(diffCall)
        .setBackgroundThreadExecutor(appExecutors.diskIO()).build()) {

    private val views: Array<Int> = arrayOf(R.layout.item_message_received_with_avatar, R.layout.item_message_received_with_no_avatar, R.layout.item_message_sent);

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(p0.context), views[p1], p0, false, dataBindingComponent);
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        when (getItemViewType(p1)) {
            0 -> (p0.binding as ItemMessageReceivedWithAvatarBinding).message = getItem(p1);
            1 -> (p0.binding as ItemMessageReceivedWithNoAvatarBinding).message = getItem(p1);
            2 -> (p0.binding as ItemMessageSentBinding).message = getItem(p1);
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).userId.compareTo(ownerUserId) == 0 -> 2
            position == 0 -> 0
            getItem(position).userId.compareTo(getItem(position - 1).userId) == 0 -> 1
            else -> 0
        }
    }
}