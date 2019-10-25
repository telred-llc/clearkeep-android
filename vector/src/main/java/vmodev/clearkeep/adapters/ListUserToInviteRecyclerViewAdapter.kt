package vmodev.clearkeep.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemUserWithRadioBinding
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User

class ListUserToInviteRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<User>
                                                      , private val listSelected: HashMap<String, User>
                                                      , private val dataBindingComponent: DataBindingComponent
                                                      , private val itemClick: (User, Boolean) -> Unit?)
    : ListAdapter<User, DataBoundViewHolder<ItemUserWithRadioBinding>>(AsyncDifferConfig.Builder<User>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    private var listUserIDSelected: Array<String> ?= null

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
        if (listSelected.containsKey(getItem(p1).id)) {
            p0.binding.checkbox.isChecked = true
        } else if (!listUserIDSelected.isNullOrEmpty() && listUserIDSelected!!.contains(getItem(p1).id)) {
            p0.binding.checkbox.isChecked = true
            listSelected.put(getItem(p1).id,getItem(p1))
            itemClick?.invoke(getItem(p1), p0.binding.checkbox.isChecked)
        } else {
            p0.binding.checkbox.isChecked = false
        }
        p0.binding.user = getItem(p1);
    }

    private fun onClickItem(binding: ItemUserWithRadioBinding) {
        binding.user?.let {

            if (binding.checkbox.isChecked) {
                listSelected.put(it.id, it);
            } else {
                listSelected.remove(it.id);
            }
            itemClick?.invoke(it, binding.checkbox.isChecked)
        }
    }

    fun setKeySelected(listData: Array<String>) {
        this.listUserIDSelected = listData
    }
}