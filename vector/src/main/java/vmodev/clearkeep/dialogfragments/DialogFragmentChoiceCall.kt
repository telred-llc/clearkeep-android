package vmodev.clearkeep.dialogfragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import im.vector.R
import im.vector.databinding.DialogFragmentChoiceCallBinding
import kotlinx.android.synthetic.main.dialog_fragment_choice_call.*
import vmodev.clearkeep.viewmodelobjects.User

class DialogFragmentChoiceCall : BottomSheetDialogFragment(), View.OnClickListener {

    var user: User? = null
    private lateinit var binding: DialogFragmentChoiceCallBinding
    var iAction: IAction? = null

    companion object {
        fun newInstance(user: User): DialogFragmentChoiceCall {
            val bundle = Bundle()
            bundle.putSerializable(User::class.java.simpleName, user)
            val fragment = DialogFragmentChoiceCall()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choice_call, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        user = arguments?.getSerializable(User::class.java.simpleName) as User
        binding.user = user
        binding.btnClose.setOnClickListener(this)
        binding.btnVideoCall.setOnClickListener(this)
        binding.btnVoiceCall.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_close -> {
                dismiss()
            }
            btn_video_call -> {
                onActionCall(true)
            }
            btn_voice_call -> {
                onActionCall(false)
            }
        }
    }

    fun onActionCall(isVideoCall: Boolean) {
        TedPermission.with(activity)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        //Todo
                        iAction?.call(isVideoCall)
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .check()
    }

    override fun onDestroy() {
        super.onDestroy()
        iAction?.disableDialog()
    }

    interface IAction {

        fun call(isVideoCall: Boolean)


        fun disableDialog()
    }

}