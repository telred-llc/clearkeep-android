package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.SignatureItemBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Signature

class ListSignatureRecyclerViewAdapter constructor(appExecutors: AppExecutors, private val dataBindingComponent : DataBindingComponent, diffCall: DiffUtil.ItemCallback<Signature>)
    : ListAdapter<Signature, DataBoundViewHolder<SignatureItemBinding>>(AsyncDifferConfig.Builder(diffCall).setBackgroundThreadExecutor(appExecutors.diskIO()).build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<SignatureItemBinding> {
        val binding = DataBindingUtil.inflate<SignatureItemBinding>(LayoutInflater.from(p0.context), R.layout.signature_item, p0, false, dataBindingComponent);
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<SignatureItemBinding>, p1: Int) {
        p0.binding.signature = getItem(p1);
    }
}