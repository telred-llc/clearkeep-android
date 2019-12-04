package vmodev.clearkeep.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityViewUserProfileBinding
import kotlinx.android.synthetic.main.activity_view_user_profile.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.rest.model.User
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.dialogfragments.DialogFragmentChoiceCall
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Inject

class ViewUserProfileActivity : DataBindingDaggerActivity(), IViewUserProfileActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractViewUserProfileActivityViewModel>

    private lateinit var binding: ActivityViewUserProfileBinding
    private lateinit var session: MXSession

    companion object {
        const val USER_ID = "USER_ID"
        const val ROOM_ID = "ROOM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_user_profile, dataBinding.getDataBindingComponent())
        val userId = intent.getStringExtra(USER_ID)
        val roomID = intent.getStringExtra(ROOM_ID)
        binding.user = viewModelFactory.getViewModel().getUserResult()
        binding.room = viewModelFactory.getViewModel().createNewDirectChatResult()
        binding.imageStatusAdmin.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
        binding.buttonMakeAdmin.isSelected = true
        session = Matrix.getInstance(applicationContext).defaultSession
        val state = session.dataHandler.getRoom(roomID).state.powerLevels
        val powerLever = state.getUserPowerLevel(userId)
        binding.powerLevel = powerLever
        if (powerLever.toFloat() == CommonActivityUtils.UTILS_POWER_LEVEL_ADMIN) {
            binding.buttonMakeAdmin.visibility = View.INVISIBLE
        } else {
            val powerLeverDevice = state.getUserPowerLevel(session.myUserId)
            if (powerLeverDevice.toFloat() == CommonActivityUtils.UTILS_POWER_LEVEL_ADMIN) {
                binding.buttonMakeAdmin.visibility = View.VISIBLE
            } else {
                binding.buttonMakeAdmin.visibility = View.INVISIBLE
            }
        }
        session.presenceApiClient.getPresence(userId, object : ApiCallback<User> {
            override fun onSuccess(it: User?) {
                if (it?.isActive!!) {
                    binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(this@ViewUserProfileActivity, R.color.app_green))
                } else {
                    binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(this@ViewUserProfileActivity, R.color.main_text_color_hint))
                }
            }

            override fun onUnexpectedError(p0: java.lang.Exception?) {
                Log.e("Tag", "Errror: ${p0?.message}")
                binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(this@ViewUserProfileActivity, R.color.main_text_color_hint))
            }

            override fun onMatrixError(p0: MatrixError?) {
                Log.e("Tag", "Errror: ${p0?.message}")
                binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(this@ViewUserProfileActivity, R.color.main_text_color_hint))
            }

            override fun onNetworkError(p0: java.lang.Exception?) {
                Log.e("Tag", "Errror: ${p0?.message}")
                binding.circleImageViewStatus.setColorFilter(ContextCompat.getColor(this@ViewUserProfileActivity, R.color.main_text_color_hint))
            }

        })
        binding.imgChat.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                viewModelFactory.getViewModel().setUserIdForCreateNewChat(userId)
            }

        })
        binding.imgCall.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                DialogFragmentChoiceCall.newInstance(binding.user?.value?.data!!).show(supportFragmentManager, DialogFragmentChoiceCall::class.java.simpleName)
//                viewModelFactory.getViewModel().setUserIdForCreateNewChat(userId)
            }

        })
        viewModelFactory.getViewModel().createNewDirectChatResult().observe(this, Observer {
            it?.data?.let {
                joinRoom(it.id)
            }
        })
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.buttonMakeAdmin.setOnClickListener {
            val state = layout_state.visibility != View.VISIBLE
            setAdmin(state)
        }

        binding.lifecycleOwner = this
        viewModelFactory.getViewModel().setGetUser(userId)
    }

    private fun setAdmin(isAdmin: Boolean) {
        val room = session.dataHandler.store!!.getRoom(intent.getStringExtra(ROOM_ID))
        val powerLevel = if (isAdmin) CommonActivityUtils.UTILS_POWER_LEVEL_ADMIN.toInt() else CommonActivityUtils.UTILS_POWER_LEVEL_MODERATOR.toInt()
        room.updateUserPowerLevels(intent.getStringExtra(USER_ID), powerLevel, object : ApiCallback<Void> {
            override fun onSuccess(p0: Void?) {
                layout_state.visibility = if (isAdmin) View.VISIBLE else View.GONE
                if (isAdmin) {
                    button_make_admin.visibility = View.INVISIBLE
                } else {
                    button_make_admin.visibility = View.VISIBLE
                }
            }

            override fun onUnexpectedError(p0: java.lang.Exception?) {
            }

            override fun onMatrixError(p0: MatrixError?) {
            }

            override fun onNetworkError(p0: java.lang.Exception?) {
            }

        })
    }

    private fun joinRoom(roomId: String) {
        val room = session.dataHandler.store!!.getRoom(roomId)
        session.joinRoom(room!!.roomId, object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()
                params[VectorRoomActivity.EXTRA_MATRIX_ID] = session.myUserId
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room.roomId
                CommonActivityUtils.goToRoomPage(this@ViewUserProfileActivity, session, params)
                finish()
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@ViewUserProfileActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {

                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }

    override fun getActivity(): FragmentActivity {
        return this
    }
}
