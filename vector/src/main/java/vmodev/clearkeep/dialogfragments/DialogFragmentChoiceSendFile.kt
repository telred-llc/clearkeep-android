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
import im.vector.databinding.DialogFragmentChoiceSendFileBinding
import kotlinx.android.synthetic.main.dialog_fragment_choice_call.btn_close
import kotlinx.android.synthetic.main.dialog_fragment_choice_send_file.*

class DialogFragmentChoiceSendFile : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogFragmentChoiceSendFileBinding
    var iAction: IAction? = null

    companion object {
        fun newInstance(): DialogFragmentChoiceSendFile {
            val fragment = DialogFragmentChoiceSendFile()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choice_send_file, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnClose.setOnClickListener(this)
        binding.btnSendFile.setOnClickListener(this)
        binding.btnSendMedia.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_close -> {
                dismiss()
            }
            btn_send_file -> {
                onActionSend(false)
            }
            btn_send_media -> {
                onActionSend(true)
            }
        }
    }

    fun onActionSend(isMedia: Boolean) {
        TedPermission.with(activity)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        //Todo
                        if (isMedia) {
                            iAction?.sendMedia()
                        } else {
                            iAction?.sendFile()
                        }
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

        fun sendFile()

        fun sendMedia()

        fun disableDialog()
    }

}