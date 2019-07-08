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
import im.vector.databinding.ItemFileBinding
import im.vector.databinding.ItemUserBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.File
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

class ListFileRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<File>
                                              , private val dataBindingComponent: DataBindingComponent
                                              , private val itemClick: (File) -> Unit?)
    : ListAdapter<File, DataBoundViewHolder<ItemFileBinding>>(AsyncDifferConfig.Builder<File>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemFileBinding> {
        val binding = DataBindingUtil.inflate<ItemFileBinding>(LayoutInflater.from(p0.context), R.layout.item_file, p0, false, dataBindingComponent);
        binding.root.setOnClickListener { v ->
            binding.file?.let {
                itemClick?.invoke(it)
            }
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemFileBinding>, p1: Int) {
        p0.binding.file = getItem(p1);
    }
}