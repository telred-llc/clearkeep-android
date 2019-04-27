package vmodev.clearkeep.adapters

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.ItemUserBinding
import im.vector.databinding.ItemUserWithRadioBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User

class ListUserToInviteRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<User>
                                                      , private val dataBindingComponent: DataBindingComponent
                                                      , private val listSelected : HashMap<String, String>
                                                      , private val itemClick: (User, Boolean) -> Unit?)
    : ListAdapter<User, DataBoundViewHolder<ItemUserWithRadioBinding>>(AsyncDifferConfig.Builder<User>(diffCallback)
    .setBackgroundThreadExecutor(appExecutors.diskIO())
    .build()) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemUserWithRadioBinding> {
        val binding = DataBindingUtil.inflate<ItemUserWithRadioBinding>(LayoutInflater.from(p0.context), R.layout.item_user_with_radio, p0, false, dataBindingComponent);
        binding.root.setOnClickListener { v ->
            binding.checkbox.isChecked = !binding.checkbox.isChecked;
            onClickItem(binding);
        }
        binding.checkbox.setOnClickListener { v ->
            onClickItem(binding);
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemUserWithRadioBinding>, p1: Int) {
        p0.binding.checkbox.isChecked = listSelected.containsKey(getItem(p1).id);
        p0.binding.user = getItem(p1);
    }

    private fun onClickItem(binding: ItemUserWithRadioBinding) {
        binding.user?.let {

            if (binding.checkbox.isChecked) {
                listSelected.put(it.id, it.id);
            } else {
                listSelected.remove(it.id);
            }
            itemClick?.invoke(it, binding.checkbox.isChecked)
        }
    }
}