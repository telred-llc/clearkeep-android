package vmodev.clearkeep.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.SearchRoomItemBinding
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomSearchRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomListUser>, private val dataBindingComponent: IDataBindingComponent)

    : ListAdapter<RoomListUser, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomRecyclerViewAdapter {
    override fun getflag(flag: Int?) {
        Log.d("", "")
    }

    private lateinit var itemClick: (RoomListUser, Int) -> Unit?
    private lateinit var itemLongClick: (RoomListUser) -> Unit?
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<SearchRoomItemBinding>(LayoutInflater.from(p0.context), R.layout.search_room_item, p0, false, dataBindingComponent.getDataBindingComponent());
        binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 3) } }
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).room?.type != 65 && getItem(position).room?.type != 66) 1 else 0;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        (p0.binding as SearchRoomItemBinding).roomListUser = getItem(p1);
        p0.binding.executePendingBindings();
    }

    override fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?) {
        this.itemClick = itemClick;
    }

    override fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?) {
        this.itemLongClick = itemLongClick;
    }

    override fun getAdapter(): ListAdapter<RoomListUser, *> {
        return this;
    }
}