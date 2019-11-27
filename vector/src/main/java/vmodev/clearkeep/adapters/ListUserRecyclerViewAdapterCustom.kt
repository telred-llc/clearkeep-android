package vmodev.clearkeep.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ItemUserMemberGroupsBinding
import im.vector.util.VectorUtils
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.rest.model.PowerLevels
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.User
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors

class ListUserRecyclerViewAdapterCustom(val mContext: Context, val mRoomId: String, appExecutors: AppExecutors, diffCallback: DiffUtil.ItemCallback<RoomMember>,
                                        private val dataBinding: IDataBindingComponent,
                                        private val itemClick: (RoomMember) -> Unit?)
    : ListAdapter<RoomMember, DataBoundViewHolder<ItemUserMemberGroupsBinding>>(AsyncDifferConfig.Builder<RoomMember>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {

    val mSession = Matrix.getInstance(mContext).defaultSession
    val mRoom = mSession.dataHandler.getRoom(mRoomId)
    val powerLevels: PowerLevels? = mRoom.state.powerLevels

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int): DataBoundViewHolder<ItemUserMemberGroupsBinding> {
        val binding = DataBindingUtil.inflate<ItemUserMemberGroupsBinding>(LayoutInflater.from(mContext), R.layout.item_user_member_groups, parent, false, dataBinding.getDataBindingComponent())
        binding.root.setOnClickListener { v ->
            binding.user?.let {
                itemClick.invoke(it)
            }
        }
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(dataBoundViewHolder: DataBoundViewHolder<ItemUserMemberGroupsBinding>, position: Int) {
        val item = getItem(position)
        dataBoundViewHolder.binding.user = item
        item?.let {
            val memberPowerLevel = powerLevels?.getUserPowerLevel(it.userId)!!.toFloat()
            if (memberPowerLevel == CommonActivityUtils.UTILS_POWER_LEVEL_ADMIN) {
                dataBoundViewHolder.binding.imageStatusAdmin.visibility = View.VISIBLE
                dataBoundViewHolder.binding.imageStatusAdmin.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow))
                dataBoundViewHolder.binding.textAdmin.visibility = View.VISIBLE
            } else {
                dataBoundViewHolder.binding.imageStatusAdmin.visibility = View.INVISIBLE
                dataBoundViewHolder.binding.textAdmin.visibility = View.GONE
            }
            VectorUtils.loadRoomMemberAvatar(mContext, mSession, dataBoundViewHolder.binding.circleImageViewAvatar, it)
            mSession.presenceApiClient.getPresence(it.userId, object : ApiCallback<User> {
                override fun onSuccess(it: User?) {
                    if (it?.isActive!!) {
                        dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.app_green))
                    } else {
                        dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.main_text_color_hint))
                    }
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    Log.e("Tag", "Lỗi: ${p0?.message}")
                    dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.main_text_color_hint))
                }

                override fun onMatrixError(p0: MatrixError?) {
                    Log.e("Tag", "Lỗi: ${p0?.message}")
                    dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.main_text_color_hint))
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    Log.e("Tag", "Lỗi: ${p0?.message}")
                    dataBoundViewHolder.binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.main_text_color_hint))
                }

            })
        }

    }

}