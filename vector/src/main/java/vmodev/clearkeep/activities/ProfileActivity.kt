package vmodev.clearkeep.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ActivityProfileBinding
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import org.matrix.androidsdk.MXSession
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*
import javax.inject.Inject

class ProfileActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractProfileActivityViewModel>
    @Inject
    lateinit var clearKeepDatabase: ClearKeepDatabase;
    @Inject
    lateinit var abstractUserDao: AbstractUserDao;
    @Inject
    lateinit var abstractRoomDao: AbstractRoomDao;
    @Inject
    lateinit var deviceSettingsDao: AbstractDeviceSettingsDao;
    @Inject
    lateinit var backupKeyBackupDao: AbstractKeyBackupDao;
    @Inject
    lateinit var roomUserJoinDao: AbstractRoomUserJoinDao;
    @Inject
    lateinit var messageDao: AbstractMessageDao;

    lateinit var binding: ActivityProfileBinding;
    lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.getWindow();
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_hint_text_color_light)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile, dataBinding.getDataBindingComponent());
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        handelEditName()
//        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.profile);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
//        binding.toolbar.setNavigationOnClickListener {
//            onBackPressed();
//        }
        binding.user = viewModelFactory.getViewModel().getCurrentUserResult();
        binding.checkNeedBackup = viewModelFactory.getViewModel().getNeedBackupWhenLogout();
        viewModelFactory.getViewModel().setIdForGetCurrentUser(mxSession.myUserId);
        binding.buttonSignOut.setOnClickListener {
            if (mxSession.crypto?.keysBackup?.isEnabled == true) {
                viewModelFactory.getViewModel().setCheckNeedBackupWhenSignOut(Calendar.getInstance().timeInMillis)
            } else {
                AlertDialog.Builder(this).setTitle(R.string.action_sign_out).setMessage(R.string.sign_out_bottom_sheet_warning_backup_not_active)
                        .setNegativeButton(R.string.backup_key) { dialogInterface, i ->

                            val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java);
                            startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                        }
                        .setPositiveButton(R.string.keep_sign_out) { dialogInterface, i -> signOut() }
                        .show();
            }
        }
        binding.imgSetting.setOnClickListener {
            val intentProfileSetting = Intent(this, SettingsActivity::class.java);
//            intentProfileSetting.putExtra(ProfileSettingsFragment.USER_ID, mxSession.myUserId);
            startActivity(intentProfileSetting);
        }
//        binding.rlEdit.setOnClickListener {
//            val intentEditProfile = Intent(this, EditProfileActivity::class.java);
//            intentEditProfile.putExtra(EditProfileActivity.USER_ID, mxSession.myUserId)
//            startActivity(intentEditProfile)
//        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        viewModelFactory.getViewModel().getNeedBackupWhenLogout().observe(this, Observer {
            it?.data?.let {
                if (it != 1) {
                    AlertDialog.Builder(this).setTitle(R.string.action_sign_out).setMessage(R.string.sign_out_bottom_sheet_warning_backup_not_active)
                            .setNegativeButton(R.string.backup_key) { dialogInterface, i ->
                                if (it == 2) {
                                    val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java);
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                                } else {
                                    val intentBackupKey = Intent(this, RestoreBackupKeyActivity::class.java);
                                    intentBackupKey.putExtra(RestoreBackupKeyActivity.USER_ID, mxSession.myUserId);
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                                }
                            }
                            .setPositiveButton(R.string.keep_sign_out) { dialogInterface, i -> signOut() }
                            .show();
                } else {
                    signOut();
                }
            }
        })
        binding.lifecycleOwner = this;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WAITING_FOR_BACK_UP_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                AlertDialog.Builder(this).setTitle(R.string.done).setMessage(R.string.keys_backup_info_keys_all_backup_up).setNegativeButton(R.string.close) { dialogInterface, i ->
                    signOut();
                }.show();
            } else {
                AlertDialog.Builder(this).setTitle(R.string.backup_error).setMessage(R.string.backup_error_message_sign_out)
                        .setPositiveButton(R.string.no) { dialogInterface, i -> signOut() }
                        .setNegativeButton(R.string.yes) { dialogInterface, i ->
                            val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java);
                            startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                        }.show();
            }
        }

//        handel picture **********************
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode == EditProfileActivity.RESULT_LOAD_IMG) {
                try {
                    val imageUri = data?.data!!;
                    val inputStream = contentResolver.openInputStream(imageUri);
                    var selectedImage = BitmapFactory.decodeStream(inputStream);
                    selectedImage = getResizedBitmap(selectedImage, 512, 512);
                    val bos = ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    val bitmapData = bos.toByteArray();
//                    avatarImage = ByteArrayInputStream(bitmapData);
//                    binding.imageViewTakePhoto.setImageBitmap(selectedImage);
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            if (requestCode == EditProfileActivity.RESULT_TAKE_IMAGE_FROM_CAMERA) {
                try {
                    var image: Bitmap = data?.extras?.get("data") as Bitmap
                    image = getResizedBitmap(image, 512, 512);
                    val bos = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100/*ignored for PNG*/, bos)
                    val bitmapData = bos.toByteArray()
//                    avatarImage = ByteArrayInputStream(bitmapData)
//                    image?.let { binding.imageViewTakePhoto.setImageBitmap(image) }
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
                    application.removeEventHandler();
                    Completable.fromAction {
                        messageDao.delete();
                        roomUserJoinDao.delete();
                        abstractUserDao.delete();
                        abstractRoomDao.delete();
                        deviceSettingsDao.delete();
                        backupKeyBackupDao.delete();
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        CommonActivityUtils.logout(null, true);
                    };
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    private fun handelEditName() {
        binding.imgEdit.setOnClickListener {
            binding.isEditTextNameFocus = true
            binding.edtName.requestFocus()
        }
        binding.imgDone.setOnClickListener {
            binding.isEditTextNameFocus = false
            binding.edtName.clearFocus()
        }
        binding.edtName.setOnFocusChangeListener { view, hasFocus ->
            binding.isEditTextNameFocus = hasFocus
        }
    }

    // Handel Picture


    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.getWidth();
        val height = bm.getHeight();
        val scaleWidth: Float = (newWidth.toFloat()) / width;
        val scaleHeight: Float = (newHeight.toFloat()) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix: android.graphics.Matrix = android.graphics.Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap: Bitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @AfterPermissionGranted(EditProfileActivity.REQUEST_CAMERA_PERMISSION)
    private fun requestCameraPermission() {
        val params = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *params)) {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, EditProfileActivity.RESULT_TAKE_IMAGE_FROM_CAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for take picture", EditProfileActivity.REQUEST_CAMERA_PERMISSION, *params)
        }
    }

    @AfterPermissionGranted(EditProfileActivity.REQUEST_READ_EXTERNAL_STORAGE)
    private fun requestReadExternalStorage() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (EasyPermissions.hasPermissions(this, *params)) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, EditProfileActivity.RESULT_LOAD_IMG)
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for get picture from gallery", EditProfileActivity.REQUEST_READ_EXTERNAL_STORAGE, *params);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private fun saveProfile() {
        if (binding.edtName.text.toString().isNullOrEmpty()) {
            AlertDialog.Builder(this).setTitle(R.string.display_name_cannot_empty)
                    .setMessage(R.string.you_need_enter_you_name)
                    .setNegativeButton(R.string.close, null)
                    .show();
        } else {
//            viewModelFactory.getViewModel().setUpdateUser(      userId, binding.editTextDisplayName.text.toString().trim(), avatarImage);
        }
    }


    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 10343;
    }
}
