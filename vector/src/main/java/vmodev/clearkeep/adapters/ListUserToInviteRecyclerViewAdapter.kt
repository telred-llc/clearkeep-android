package vmodev.clearkeep.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ItemUserWithRadioBinding
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User

class ListUserToInviteRecyclerViewAdapter constructor(appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<User>
                                                      , private val listSelected: HashMap<String, User>
                                                      , private val dataBindingComponent: DataBindingComponent
                                                      , private val itemClick: (User, Boolean) -> Unit?)
    : ListAdapter<User, DataBoundViewHolder<ItemUserWithRadioBinding>>(AsyncDifferConfig.Builder<User>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {
    private var listUserIDSelected: Array<String>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemUserWithRadioBinding> {
        val binding = DataBindingUtil.inflate<ItemUserWithRadioBinding>(LayoutInflater.from(p0.context), R.layout.item_user_with_radio, p0, false, dataBindingComponent)
        binding.root.setOnClickListener { v ->
            binding.checkbox.isChecked = !binding.checkbox.isChecked
            onClickItem(binding)
        }
        binding.checkbox.setOnClickListener { v ->
            onClickItem(binding)
        }
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(dataBoundViewHolder: DataBoundViewHolder<ItemUserWithRadioBinding>, p1: Int) {
        if (listSelected.containsKey(getItem(p1).id)) {
            dataBoundViewHolder.binding.checkbox.isChecked = true
        } else if (!listUserIDSelected.isNullOrEmpty() && listUserIDSelected!!.contains(getItem(p1).id)) {
            dataBoundViewHolder.binding.checkbox.isChecked = true
            listSelected.put(getItem(p1).id, getItem(p1))
            itemClick.invoke(getItem(p1), dataBoundViewHolder.binding.checkbox.isChecked)
        } else {
            dataBoundViewHolder.binding.checkbox.isChecked = false
        }
        dataBoundViewHolder.binding.user = getItem(p1)
        Matrix.getInstance(dataBoundViewHolder.binding.root.context).defaultSession.presenceApiClient.getPresence(getItem(p1).id, object : ApiCallback<org.matrix.androidsdk.rest.model.User> {
            override fun onSuccess(it: org.matrix.androidsdk.rest.model.User?) {
                if (it?.isActive!!) {
                    dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(dataBoundViewHolder.binding.root.context, R.color.app_green))
                } else {
                    dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(dataBoundViewHolder.binding.root.context, R.color.main_text_color_hint))
                }
            }

            override fun onUnexpectedError(p0: java.lang.Exception?) {
                Log.e("Tag", "Lỗi: ${p0?.message}")
                dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(dataBoundViewHolder.binding.root.context, R.color.main_text_color_hint))
            }

            override fun onMatrixError(p0: MatrixError?) {
                Log.e("Tag", "Lỗi: ${p0?.message}")
                dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(dataBoundViewHolder.binding.root.context, R.color.main_text_color_hint))
            }

            override fun onNetworkError(p0: java.lang.Exception?) {
                Log.e("Tag", "Lỗi: ${p0?.message}")
                dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(dataBoundViewHolder.binding.root.context, R.color.main_text_color_hint))
            }

        })
    }

    private fun onClickItem(binding: ItemUserWithRadioBinding) {
        binding.user?.let {
            if (binding.checkbox.isChecked) {
                listSelected.put(it.id, it)
            } else {
                listSelected.remove(it.id)
            }
            itemClick.invoke(it, binding.checkbox.isChecked)
        }
    }

    fun setKeySelected(listData: Array<String>) {
        this.listUserIDSelected = listData
    }
}