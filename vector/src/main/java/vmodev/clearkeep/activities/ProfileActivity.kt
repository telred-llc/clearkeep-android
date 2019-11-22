package vmodev.clearkeep.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.orhanobut.dialogplus.DialogPlus
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ActivityProfileBinding
import im.vector.extensions.getColorFromAttr
import im.vector.extensions.hideKeyboard
import im.vector.extensions.showKeyboard
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.adapters.BottomDialogSelectImages
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class ProfileActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractProfileActivityViewModel>
    @Inject
    lateinit var clearKeepDatabase: ClearKeepDatabase
    @Inject
    lateinit var abstractUserDao: AbstractUserDao
    @Inject
    lateinit var abstractRoomDao: AbstractRoomDao
    @Inject
    lateinit var deviceSettingsDao: AbstractDeviceSettingsDao
    @Inject
    lateinit var backupKeyBackupDao: AbstractKeyBackupDao
    @Inject
    lateinit var roomUserJoinDao: AbstractRoomUserJoinDao
    @Inject
    lateinit var messageDao: AbstractMessageDao

    lateinit var binding: ActivityProfileBinding
    lateinit var mxSession: MXSession

    private var user: User? = null
    private var avatarImage: InputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile, dataBinding.getDataBindingComponent())
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession
        handelEditName()
        supportActionBar?.setTitle(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.user = viewModelFactory.getViewModel().getCurrentUserResult()
        viewModelFactory.getViewModel().getCurrentUserResult().observe(this, Observer {
            this.user = it?.data
        })
        binding.checkNeedBackup = viewModelFactory.getViewModel().getNeedBackupWhenLogout()
        viewModelFactory.getViewModel().setIdForGetCurrentUser(mxSession.myUserId)
        binding.buttonSignOut.setOnClickListener {
            if (mxSession.crypto?.keysBackup?.isEnabled == true) {
                viewModelFactory.getViewModel().setCheckNeedBackupWhenSignOut(Calendar.getInstance().timeInMillis)
            } else {
                AlertDialog.Builder(this).setTitle(R.string.action_sign_out).setMessage(R.string.sign_out_bottom_sheet_warning_backup_not_active)
                        .setNegativeButton(R.string.backup_key) { dialogInterface, i ->

                            val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java)
                            startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY)
                        }
                        .setPositiveButton(R.string.keep_sign_out) { dialogInterface, i -> signOut() }
                        .show()
            }
        }
        binding.imgSetting.setOnClickListener {
            val intentProfileSetting = Intent(this, SettingsActivity::class.java)
//            intentProfileSetting.putExtra(ProfileSettingsFragment.USER_ID, mxSession.myUserId)
            startActivity(intentProfileSetting)
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        viewModelFactory.getViewModel().getNeedBackupWhenLogout().observe(this, Observer {
            it?.data?.let {
                if (it != 1) {
                    AlertDialog.Builder(this).setTitle(R.string.action_sign_out).setMessage(R.string.sign_out_bottom_sheet_warning_backup_not_active)
                            .setNegativeButton(R.string.backup_key) { dialogInterface, i ->
                                if (it == 2) {
                                    val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java)
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY)
                                } else {
                                    val intentBackupKey = Intent(this, RestoreBackupKeyActivity::class.java)
                                    intentBackupKey.putExtra(RestoreBackupKeyActivity.USER_ID, mxSession.myUserId)
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY)
                                }
                            }
                            .setPositiveButton(R.string.keep_sign_out) { dialogInterface, i -> signOut() }
                            .show()
                } else {
                    signOut()
                }
            }
        })
        viewModelFactory.getViewModel().getUserUpdateResult().observe(this, Observer {
            if (it?.status == Status.SUCCESS) {
                Toast.makeText(this, R.string.update_profile_success, Toast.LENGTH_LONG).show()
                avatarImage?.close()
                avatarImage = null
            }
        })
        binding.circleImageViewAvatar.setOnClickListener {
            hideKeyboard()
            showOptionSelectImage()
        }
        binding.edtName.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                unFocusEdiText()
                saveProfile()
            }
            return@setOnEditorActionListener false
        }
        binding.colorTextDefault = getColorFromAttr(R.attr.color_text_app_default)
        binding.lifecycleOwner = this
    }

    private fun showOptionSelectImage() {
        val bottomDialog = DialogPlus.newDialog(this)
                .setAdapter(BottomDialogSelectImages())
                .setOnItemClickListener { dialog, item, view, position ->
                    when (position) {
                        0 -> {
                            requestCameraPermission()
                        }
                        1 -> {
                            requestReadExternalStorage()
                        }
                        2 -> {
                        }
                    }
                    dialog?.dismiss()
                }.create()
        bottomDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WAITING_FOR_BACK_UP_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                AlertDialog.Builder(this).setTitle(R.string.done).setMessage(R.string.keys_backup_info_keys_all_backup_up).setNegativeButton(R.string.close) { dialogInterface, i ->
                    signOut()
                }.show()
            } else {
                AlertDialog.Builder(this).setTitle(R.string.backup_error).setMessage(R.string.backup_error_message_sign_out)
                        .setPositiveButton(R.string.no) { dialogInterface, i -> signOut() }
                        .setNegativeButton(R.string.yes) { dialogInterface, i ->
                            val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java)
                            startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY)
                        }.show()
            }
        }

//        handel picture **********************
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode == EditProfileActivity.RESULT_LOAD_IMG) {
                try {
                    val imageUri = data?.data!!
                    val inputStream = contentResolver.openInputStream(imageUri)
                    var selectedImage = BitmapFactory.decodeStream(inputStream)
                    selectedImage = getResizedBitmap(selectedImage, 512, 512)
                    val bos = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                    val bitmapData = bos.toByteArray()
                    avatarImage = ByteArrayInputStream(bitmapData)
                    binding.circleImageViewAvatar.setImageBitmap(selectedImage)
                    saveProfile()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            if (requestCode == EditProfileActivity.RESULT_TAKE_IMAGE_FROM_CAMERA) {
                try {
                    var image: Bitmap = data?.extras?.get("data") as Bitmap
                    image = getResizedBitmap(image, 512, 512)
                    val bos = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100/*ignored for PNG*/, bos)
                    val bitmapData = bos.toByteArray()
                    avatarImage = ByteArrayInputStream(bitmapData)
                    image.let { binding.circleImageViewAvatar.setImageBitmap(image) }
                    saveProfile()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun signOut() {
        AlertDialog.Builder(this)
                .setTitle(R.string.action_sign_out)
                .setMessage(R.string.action_sign_out_confirmation_simple)
                .setPositiveButton(R.string.action_sign_out) { dialog, which ->
                    application.removeEventHandler()
                    val disposable = Completable.fromAction {
                        messageDao.delete()
                        roomUserJoinDao.delete()
                        abstractUserDao.delete()
                        abstractRoomDao.delete()
                        deviceSettingsDao.delete()
                        backupKeyBackupDao.delete()
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        CommonActivityUtils.logout(null, true)
                    }
                    compositeDisposable.add(disposable)
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    private fun handelEditName() {
        binding.isEditTextNameFocus = false
        binding.edtName.setOnClickListener {
            if (binding.isEditTextNameFocus == false) {
                focusEdiText()
            }
        }
        binding.relativeLayout.setOnClickListener {
            showKeyboard()
        }
        binding.imgDone.setOnClickListener {
            unFocusEdiText()
            saveProfile()
        }
        binding.edtName.setOnFocusChangeListener { view, hasFocus ->
            binding.isEditTextNameFocus = hasFocus

        }
    }

    // Handel Picture
    private fun focusEdiText() {
        binding.isEditTextNameFocus = true
        binding.edtName.isFocusable = true
        binding.edtName.isFocusableInTouchMode = true
        binding.edtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.edtName.focus()
        showKeyboard()
    }

    private fun unFocusEdiText() {
        hideKeyboard()
        binding.isEditTextNameFocus = false
        binding.edtName.isFocusable = false
        binding.edtName.isFocusableInTouchMode = false
        binding.edtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_grey, 0)
        binding.edtName.clearFocus()
    }


    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height


        val scaleWidth: Float = (newWidth.toFloat()) / width
        val scaleHeight: Float = (newHeight.toFloat()) / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix: android.graphics.Matrix = android.graphics.Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap: Bitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }

    @AfterPermissionGranted(EditProfileActivity.REQUEST_CAMERA_PERMISSION)
    private fun requestCameraPermission() {
        val params = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *params)) {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, EditProfileActivity.RESULT_TAKE_IMAGE_FROM_CAMERA)
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for take picture", EditProfileActivity.REQUEST_CAMERA_PERMISSION, *params)
        }
    }

    @AfterPermissionGranted(EditProfileActivity.REQUEST_READ_EXTERNAL_STORAGE)
    private fun requestReadExternalStorage() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *params)) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, EditProfileActivity.RESULT_LOAD_IMG)
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for get picture from gallery", EditProfileActivity.REQUEST_READ_EXTERNAL_STORAGE, *params)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun saveProfile() {
        if (binding.edtName.text.toString().isNullOrEmpty()) {
            binding.edtName.setText(user?.name.toString().trim())
        } else {
            this.user?.id?.let {
                viewModelFactory.getViewModel().setUpdateUser(it, binding.edtName.text.toString().trim(), avatarImage)
            }
        }
    }


    override fun getActivity(): FragmentActivity {
        return this
    }

    private fun EditText.focus() {
        requestFocus()
        setSelection(length())
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 10343
    }
}
