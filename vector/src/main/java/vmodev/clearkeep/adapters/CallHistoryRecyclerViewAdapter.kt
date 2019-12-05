package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemCallHistoryBinding
import vmodev.clearkeep.adapters.Interfaces.ICallHistoryRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.enums.TypeCallEnum
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class CallHistoryRecyclerViewAdapter constructor(appExecutors: AppExecutors,
                                                 private val dataBindingComponent: IDataBindingComponent,
                                                 diffCallback: DiffUtil.ItemCallback<MessageRoomUser>) :
        ListAdapter<MessageRoomUser, DataBoundViewHolder<ItemCallHistoryBinding>>(AsyncDifferConfig.Builder(diffCallback)
                .setBackgroundThreadExecutor(appExecutors.diskIO())
                .build()), ICallHistoryRecyclerViewAdapter {

    private lateinit var itemClick: (MessageRoomUser, Int) -> Unit?
    private var currentUserId: String? = null
    private var lifecycleOwner: LifecycleOwner? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemCallHistoryBinding> {
        val binding = DataBindingUtil.inflate<ItemCallHistoryBinding>(LayoutInflater.from(p0.context),
                R.layout.item_call_history, p0, false, dataBindingComponent.getDataBindingComponent());

        binding.imgCallVoice.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                binding.result?.let { it ->
                    itemClick.invoke(it, TypeCallEnum.CALL_VOICE.value)
                }
            }
        })

        binding.imgCallVideo.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                binding.result?.let { it ->
                    itemClick.invoke(it, TypeCallEnum.CALL_VIDEO.value)
                }
            }
        })
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        this.currentUserId = currentUserId
        this.lifecycleOwner = lifecycleOwner;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemCallHistoryBinding>, p1: Int) {
        p0.binding.result = getItem(p1);
        p0.binding.currentID = currentUserId

    }

    override fun setOnItemClick(itemClick: (MessageRoomUser, Int) -> Unit?) {
        this.itemClick = itemClick;
    }


}