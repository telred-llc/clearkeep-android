package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemUserBinding
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User

class ListUserRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<User>,
                                              private val dataBinding: IDataBindingComponent
                                              , private val itemClick: (User) -> Unit?)
    : ListAdapter<User, DataBoundViewHolder<ItemUserBinding>>(AsyncDifferConfig.Builder<User>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemUserBinding> {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(LayoutInflater.from(p0.context), R.layout.item_user, p0, false, dataBinding.getDataBindingComponent())
        binding.root.setOnClickListener { v ->
            binding.user?.let {
                itemClick.invoke(it)
            }
        }
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemUserBinding>, p1: Int) {
        p0.binding.user = getItem(p1)

    }
}