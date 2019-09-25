package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemMessageReceivedWithAvatarBinding
import im.vector.databinding.ItemMessageReceivedWithNoAvatarBinding
import im.vector.databinding.ItemMessageSentWithAvatarBinding
import im.vector.databinding.ItemMessageSentWithNoAvatarBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.User

class ListMessageRecyclerViewAdapter constructor(private val ownerUserId: String, private val members: Map<String, User>, appExecutors: AppExecutors, diffCall: DiffUtil.ItemCallback<Message>)
    : ListAdapter<Message, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder<Message>(diffCall)
        .setBackgroundThreadExecutor(appExecutors.diskIO()).build()) {

    private val views: Array<Int> = arrayOf(R.layout.item_message_received_with_avatar, R.layout.item_message_received_with_no_avatar, R.layout.item_message_sent_with_no_avatar, R.layout.item_message_sent_with_avatar);

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(p0.context), views[p1], p0, false);
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        when (getItemViewType(p1)) {
            0 -> {
                (p0.binding as ItemMessageReceivedWithAvatarBinding).message = getItem(p1)
                val userId = getItem(p1).userId;
                members[getItem(p1).userId]?.let { p0.binding.user = it }
            };
            1 -> (p0.binding as ItemMessageReceivedWithNoAvatarBinding).message = getItem(p1);
            2 -> (p0.binding as ItemMessageSentWithNoAvatarBinding).message = getItem(p1);
            3 -> {
                (p0.binding as ItemMessageSentWithAvatarBinding).message = getItem(p1)
                members[getItem(p1).userId]?.let { p0.binding.user = it }
            };
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> {
                return if (getItem(position).userId.compareTo(ownerUserId) == 0)
                    3;
                else
                    0;
            }
            getItem(position).userId.compareTo(getItem(position - 1).userId) == 0 -> {
                return if (getItem(position).userId.compareTo(ownerUserId) == 0)
                    2;
                else
                    1;
            }
            else -> {
                return if (getItem(position).userId.compareTo(ownerUserId) == 0)
                    3;
                else
                    0;
            }
        }
    }
}