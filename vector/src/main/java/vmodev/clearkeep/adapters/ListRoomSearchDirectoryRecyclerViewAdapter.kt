package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.RoomInviteItemBinding
import im.vector.databinding.RoomItemBinding
import im.vector.databinding.SearchRoomDirectoryItemBinding
import im.vector.databinding.SearchRoomItemBinding
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.adapters.Interfaces.IListRoomDirectoryRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.DataBindingComponentImplement
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.ultis.FormatString
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomSearchDirectoryRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<PublicRoom>, private val dataBindingComponent : IDataBindingComponent)

    : ListAdapter<PublicRoom, DataBoundViewHolder<ViewDataBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()), IListRoomDirectoryRecyclerViewAdapter {

    private lateinit var itemClick: (PublicRoom, Int) -> Unit?
    private lateinit var itemLongClick: (PublicRoom) -> Unit?
    private var lifecycleOwner: LifecycleOwner? = null;
    private var currentUserId: String? = null;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<SearchRoomDirectoryItemBinding>(LayoutInflater.from(p0.context), R.layout.search_room_directory_item, p0, false, dataBindingComponent.getDataBindingComponent());
            binding.root.setOnClickListener { binding.roomDirectory?.let { itemClick?.invoke(it, 3) } }
        lifecycleOwner?.let { binding.lifecycleOwner = lifecycleOwner }
        return DataBoundViewHolder(binding);
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?) {
        this.lifecycleOwner = lifecycleOwner;
        this.currentUserId = currentUserId;
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ViewDataBinding>, p1: Int) {
            (p0.binding as SearchRoomDirectoryItemBinding).roomDirectory = getItem(p1);
        if (getItem(p1).numJoinedMembers == 1){
            p0.binding.txtmember.text = (getItem(p1).numJoinedMembers.toString() + " user")
        }else{
            p0.binding.txtmember.text = (getItem(p1).numJoinedMembers.toString() + " users")
        }
            p0.binding.executePendingBindings();
//            p0.binding.currentUserId = currentUserId;
//            p0.binding.members = getItem(p1).members?.size.toString();
    }

    override fun setOnItemClick(itemClick: (PublicRoom, Int) -> Unit?) {
        this.itemClick = itemClick;
    }

    override fun setOnItemLongClick(itemLongClick: (PublicRoom) -> Unit?) {
        this.itemLongClick = itemLongClick;
    }

    override fun getAdapter(): ListAdapter<PublicRoom, *> {
        return this;
    }
}