package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemFileBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.File

class ListFileRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<File>
                                              , private val itemClick: (File) -> Unit?)
    : ListAdapter<File, DataBoundViewHolder<ItemFileBinding>>(AsyncDifferConfig.Builder<File>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemFileBinding> {
        val binding = DataBindingUtil.inflate<ItemFileBinding>(LayoutInflater.from(p0.context), R.layout.item_file, p0, false);
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