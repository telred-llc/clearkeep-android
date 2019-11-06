package vmodev.clearkeep.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import im.vector.databinding.ItemRoomHeaderBinding
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomWithStickyHeaderRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.customviews.interfaces.IStickyHeaderItemDecorationListener
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.ultis.FormatString
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomWithStickyHeaderRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomListUser>, private val dataBindingComponent: IDataBindingComponent)

    : ListAdapter<RoomListUser, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomWithStickyHeaderRecyclerViewAdapter<ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData>, IStickyHeaderItemDecorationListener {

    private val layouts: Array<Int> = arrayOf(R.layout.room_invite_item, R.layout.room_item, R.layout.item_room_header);
    private lateinit var itemClick: (RoomListUser, Int) -> Unit?
    private lateinit var itemLongClick: (RoomListUser) -> Unit?
    private lateinit var itemHeaderClick: (Int) -> Unit?
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;
    private var listHeader: List<StickyHeaderData> = ArrayList();

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding;
        when (p1) {
            0 -> {
                binding = DataBindingUtil.inflate<RoomInviteItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent.getDataBindingComponent());
                binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 0) } }
                binding.buttonJoin.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 1) } }
                binding.buttonDecline.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 2) } }
            }
            1 -> {
                binding = DataBindingUtil.inflate<RoomItemBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent.getDataBindingComponent());
                binding.root.setOnClickListener { binding.roomListUser?.let { itemClick?.invoke(it, 3) } }
                binding.root.setOnLongClickListener { v: View? ->
                    binding.roomListUser?.let { itemLongClick?.invoke(it) }
                    return@setOnLongClickListener true
                }
            }
            else -> {
                binding = DataBindingUtil.inflate<ItemRoomHeaderBinding>(LayoutInflater.from(p0.context), layouts[p1], p0, false, dataBindingComponent.getDataBindingComponent());
            }
        }
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position == listHeader[0].headerSize || position == listHeader[0].headerSize + listHeader[1].headerSize) {
            return 2;
        } else if (getItem(position).room?.type != 65 && getItem(position).room?.type != 66)
            return 1 else return 0;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
        when (getItemViewType(p1)) {
            0 -> {
                (p0.binding as RoomInviteItemBinding).roomListUser = getItem(p1);
                p0.binding.name = getItem(p1).room?.name?.let { FormatString.formatName(it) }
                p0.binding.executePendingBindings();
            }
            1 -> {
                (p0.binding as RoomItemBinding).roomListUser = getItem(p1);
                if (getItem(p1).room?.notifyCount == 0) {
                    p0.binding.backgroundRoomItem.setBackgroundColor(ResourcesCompat.getColor(p0.itemView.resources, R.color.color_white, null))
                } else {
                    p0.binding.backgroundRoomItem.setBackgroundColor(ResourcesCompat.getColor(p0.itemView.resources, R.color.color_background_notification, null))

                }
                p0.binding.executePendingBindings();
                p0.binding.currentUserId = currentUserId;
            }
            else -> {
                for ((index, item) in listHeader.withIndex()){
                    if (item.headerPosition == p1){
                        (p0.binding as ItemRoomHeaderBinding).header = item;
                        p0.binding.root.setOnClickListener { itemHeaderClick?.invoke(index); }
                        break;
                    }
                }
            }
        }
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

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var position : Int = 0;
        for ((index, item) in listHeader.withIndex()){
            if (itemPosition < item.headerPosition + item.headerSize){
                position = index;
                break;
            }
        }
        return position;
    }

    override fun isHeader(itemPosition: Int): Boolean {
        var isHeader : Boolean = false;
        for ((index, item) in listHeader.withIndex()){
            if (itemPosition == item.headerPosition){
                isHeader = true;
                break;
            }
        }
        return isHeader;
    }

    override fun setListHeader(listHeader: List<StickyHeaderData>) {
        this.listHeader = listHeader;
    }

    override fun setOnItemStickyHeaderClick(itemStickyHeaderClick: (Int) -> Unit?) {
        this.itemHeaderClick = itemStickyHeaderClick;
    }

    override fun createView(headerPosition: Int, parent: RecyclerView): View {
        val binding = DataBindingUtil.inflate<ItemRoomHeaderBinding>(LayoutInflater.from(parent.context), R.layout.item_room_header, parent, false, dataBindingComponent.getDataBindingComponent());
        val text = listHeader[headerPosition].headerName + "(" + listHeader[headerPosition].headerSize + ")";
        binding.txtFavourites.text = text;
        return binding.root;
    }

    override fun onClickStickyHeader(headerPosition: Int) {
        itemHeaderClick?.invoke(headerPosition);
    }

    data class StickyHeaderData(
            val headerPosition : Int,
            val headerIcon : Int,
            val headerName : String,
            val headerSize : Int
    )
}