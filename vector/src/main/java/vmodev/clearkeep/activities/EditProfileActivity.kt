package vmodev.clearkeep.activities

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import im.vector.databinding.ActivityEditProfileBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IEditProfileActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IEditProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject
import android.graphics.Bitmap.CompressFormat
import android.support.v7.app.AlertDialog
import im.vector.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class EditProfileActivity : DaggerAppCompatActivity(), IEditProfileActivity {

    @Inject
    lateinit var viewModelFactory: IEditProfileActivityViewModelFactory;

    private lateinit var binding: ActivityEditProfileBinding;
    private lateinit var userId: String;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);
    private var avatarImage: InputStream? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.edit_profile);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        userId = if (intent.getStringExtra(USER_ID).isNullOrEmpty()) "" else intent.getStringExtra(USER_ID);
        binding.user = viewModelFactory.getViewModel().getUserResult();
        binding.userUpdate = viewModelFactory.getViewModel().getUserUpdateResult();
        viewModelFactory.getViewModel().getUserUpdateResult().observe(this, Observer {
            if (it?.status == Status.SUCCESS) {
                Toast.makeText(this, R.string.update_profile_success, Toast.LENGTH_LONG).show();
                avatarImage?.close();
                avatarImage = null;
            }
        })
        viewModelFactory.getViewModel().setUserId(userId)
        binding.lifecycleOwner = this;

        binding.imageViewTakePhoto.setOnClickListener {
            requestReadExternalStorge();
        }
        binding.imageViewTakeCamera.setOnClickListener {
            requestCameraPermission();
        }

        binding.textViewSave.setOnClickListener {
            if (binding.editTextDisplayName.text.toString().isNullOrEmpty()) {
                AlertDialog.Builder(this).setTitle(R.string.display_name_cannot_empty)
                        .setMessage(R.string.you_need_enter_you_name)
                        .setNegativeButton(R.string.close, null)
                        .show();
                return@setOnClickListener;
            }
            viewModelFactory.getViewModel().setUpdateUser(userId, binding.editTextDisplayName.text.toString(), avatarImage);
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                try {
                    val imageUri = data?.data;
                    val inputStream = contentResolver.openInputStream(imageUri);
                    val selectedImage = BitmapFactory.decodeStream(inputStream);
                    val bos = ByteArrayOutputStream();
                    selectedImage.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    val bitmapData = bos.toByteArray();
                    avatarImage = ByteArrayInputStream(bitmapData);
                    binding.imageViewTakePhoto.setImageBitmap(selectedImage);
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            if (requestCode == RESULT_TAKE_IMAGE_FROM_CAMERA) {
                try {
                    val image: Bitmap = data?.extras?.get("data") as Bitmap
                    val bos = ByteArrayOutputStream()
                    image.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                    val bitmapData = bos.toByteArray()
                    avatarImage = ByteArrayInputStream(bitmapData)
                    image?.let { binding.imageViewTakePhoto.setImageBitmap(image) }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        avatarImage?.close();
        avatarImage = null;
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERMISSION)
    private fun requestCameraPermission() {
        val params = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *params)) {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, RESULT_TAKE_IMAGE_FROM_CAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "", REQUEST_CAMERA_PERMISSION, *params)
        }
    }

    @AfterPermissionGranted(REQUEST_READ_EXTERNAL_STORAGE)
    private fun requestReadExternalStorge() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (EasyPermissions.hasPermissions(this, *params)) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        } else {
            EasyPermissions.requestPermissions(this, "", REQUEST_READ_EXTERNAL_STORAGE, *params);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    companion object {
        const val USER_ID = "USER_ID";
        const val RESULT_LOAD_IMG = 32768;
        const val RESULT_TAKE_IMAGE_FROM_CAMERA = 32769;
        const val REQUEST_CAMERA_PERMISSION = 33768;
        const val REQUEST_READ_EXTERNAL_STORAGE = 33778;
    }
}
