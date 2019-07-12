package vmodev.clearkeep.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.SignatureItemBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.Signature

class ListSignatureRecyclerViewAdapter constructor(appExecutors: AppExecutors, private val dataBindingComponent: DataBindingComponent, diffCall: DiffUtil.ItemCallback<Signature>)
    : ListAdapter<Signature, DataBoundViewHolder<SignatureItemBinding>>(AsyncDifferConfig.Builder(diffCall).setBackgroundThreadExecutor(appExecutors.diskIO()).build()) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<SignatureItemBinding> {
        val binding = DataBindingUtil.inflate<SignatureItemBinding>(LayoutInflater.from(p0.context), R.layout.signature_item, p0, false, dataBindingComponent);
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<SignatureItemBinding>, p1: Int) {
        p0.binding.signature = getItem(p1);
    }
}