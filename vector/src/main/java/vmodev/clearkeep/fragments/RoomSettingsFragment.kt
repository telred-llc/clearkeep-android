package vmodev.clearkeep.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import im.vector.R
import im.vector.databinding.FragmentRoomSettingsBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.observers.DisposableCompletableObserver
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.RoomfilesListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.RxEventBus
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsFragmentViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject

class RoomSettingsFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomSettingsFragmentViewModel>;

    private var alertDialog: AlertDialog? = null
    private lateinit var binding: FragmentRoomSettingsBinding;
    private val args: RoomSettingsFragmentArgs by navArgs();
    private var avatarImage: InputStream? = null;
    private var room: Room? = null
    private var user: User? = null
    private var isUpdateSuccess = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton();
        binding.room = viewModelFactory.getViewModel().getRoom();
        binding.user = viewModelFactory.getViewModel().getUserResult();
        viewModelFactory.getViewModel().getRoom().observe(viewLifecycleOwner, Observer {
            it?.data?.avatarUrl?.let {
                Log.d("RoomAvatar", it);
            }
            it?.data?.userCreated?.let {
                viewModelFactory.getViewModel().setUserId(it)
            }
            this.room = it.data
            // Send name group ro title activity
            RxEventBus.instanceOf<String>().putData(it.data?.name.toString())
        })
        binding.lifecycleOwner = this;
        args.roomId?.let { viewModelFactory.getViewModel().setRoomId(it) }

        setEventEditText()
    }

    private fun setupButton() {
        binding.leaveRoomGroup.setOnClickListener {
            if (alertDialog == null) {
                alertDialog = AlertDialog.Builder(this.context!!).setTitle(R.string.leave_room)
                        .setMessage(R.string.do_you_want_leave_room)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes) { dialog, v ->
                            binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoom();
                            viewModelFactory.getViewModel().getLeaveRoom().observe(activity!!, Observer { t ->
                                t?.let { resource ->
                                    if (resource.status == Status.SUCCESS) {
                                        this.activity?.setResult(-1);
                                        this.activity?.finish();
                                    }
                                }
                            })
                            args.roomId?.let { viewModelFactory.getViewModel().setLeaveRoom(it); }

                        }.show()
            }
            if (alertDialog!!.isShowing) {
                Log.d("alertDialog", "isShowing")
            } else {
                alertDialog!!.show()
            }

        }
        binding.settingsGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.otherSettings().setRoomId(args.roomId));
        }
        binding.membersGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.roomMemberList().setRoomId(args.roomId));
        }
        binding.addPeopleGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.inviteUsersToRoom().setRoomId(args.roomId));
        }
        binding.filesGroup.setOnClickListener { v ->
            args.roomId?.let {
                val filesIntent = Intent(this.activity, RoomfilesListActivity::class.java)
                filesIntent.putExtra(RoomfilesListActivity.ROOM_ID, it);
                startActivity(filesIntent)
            }
        }
        binding.nestedScrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            hideKeyboard()
        }
        binding.imgCamera.setOnClickListener {
            requestCameraPermission()
        }
        binding.viewSelectPicture.setOnClickListener {
            requestReadExternalStorage();
        }
        binding.btnSave.setOnClickListener {
            binding.isLoading = true
            isUpdateSuccess = false
            var name: String = binding.editTextRoomName.text.toString().toUpperCase().trim()
            var topic: String = binding.editTextRoomTopic.text.toString().trim()
            room?.let {
                if (!it.id.isNullOrBlank() && null != avatarImage) {
                    viewModelFactory.getViewModel().updateRoomAvatar(it.id, avatarImage!!).subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            binding.isLoading = false
                            isUpdateSuccess = true
                        }

                        override fun onError(e: Throwable) {
                            binding.isLoading = false
                            isUpdateSuccess = false
                        }
                    })
                }
                if (!it.id.isNullOrBlank() && !name.isNullOrBlank()) {
                    viewModelFactory.getViewModel().updateRoomName(it.id, name).subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            binding.isLoading = false
                            isUpdateSuccess = true
                        }

                        override fun onError(e: Throwable) {
                            binding.isLoading = false
                            isUpdateSuccess = false
                        }
                    })
                }
                if (!it.id.isNullOrBlank() && !topic.isNullOrBlank()) {
                    viewModelFactory.getViewModel().updateRoomTopic(it.id, topic).subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            isUpdateSuccess = true
                            binding.isLoading = false
                        }

                        override fun onError(e: Throwable) {
                            isUpdateSuccess = false
                            binding.isLoading = false
                        }
                    })
                }
                if (isUpdateSuccess) {
                    Toast.makeText(activity, "Update room success", Toast.LENGTH_LONG).show()
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_grey, null);
                    binding.btnSave.isEnabled = false;
                    binding.editTextRoomName.clearFocus()
                    binding.editTextRoomTopic.clearFocus()
                } else {
                    Toast.makeText(activity, "Update room error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setEventEditText() {
        binding.editTextRoomName.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            setColorFocusEditText(binding.editTextRoomName, hasFocus)
        }
        binding.editTextRoomTopic.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            setColorFocusEditText(binding.editTextRoomTopic, hasFocus)
        }

        binding.editTextRoomName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank() && room?.name != binding.editTextRoomName.text.toString()) {
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_blue, null)
                    binding.btnSave.isEnabled = true;
                } else {
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_grey, null);
                    binding.btnSave.isEnabled = false;
                }
            }
        })

        binding.editTextRoomTopic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!binding.editTextRoomName.text.toString().trim().isNullOrBlank() && (room?.topic != binding.editTextRoomTopic.text.toString() || room?.name != binding.editTextRoomName.text.toString())) {
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_blue, null)
                    binding.btnSave.isEnabled = true;
                } else {
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_grey, null);
                    binding.btnSave.isEnabled = false;
                }

            }
        })
    }

    @AfterPermissionGranted(RoomSettingsFragment.REQUEST_CAMERA_PERMISSION)
    private fun requestCameraPermission() {
        val params = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(activity!!, *params)) {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, RESULT_TAKE_IMAGE_FROM_CAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for take picture", REQUEST_CAMERA_PERMISSION, *params)
        }
    }

    @AfterPermissionGranted(REQUEST_READ_EXTERNAL_STORAGE)
    private fun requestReadExternalStorage() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (EasyPermissions.hasPermissions(activity!!, *params)) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission for get picture from gallery", REQUEST_READ_EXTERNAL_STORAGE, *params);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                try {
                    val imageUri = data?.data!!;
                    val inputStream = activity!!.contentResolver.openInputStream(imageUri);
                    var selectedImage = BitmapFactory.decodeStream(inputStream);
                    selectedImage = getResizedBitmap(selectedImage, 512, 512);
                    val bos = ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    val bitmapData = bos.toByteArray();
                    avatarImage = ByteArrayInputStream(bitmapData);
                    binding.imgAvatarRoom.setImageBitmap(selectedImage);
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_blue, null)
                    binding.btnSave.isEnabled = true;
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(activity!!, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            if (requestCode == RESULT_TAKE_IMAGE_FROM_CAMERA) {
                try {
                    var image: Bitmap = data?.extras?.get("data") as Bitmap
                    image = getResizedBitmap(image, 512, 512);
                    val bos = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100/*ignored for PNG*/, bos)
                    val bitmapData = bos.toByteArray()
                    avatarImage = ByteArrayInputStream(bitmapData)
                    image?.let { binding.imgAvatarRoom.setImageBitmap(image) }
                    binding.btnSave.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_gradient_blue, null)
                    binding.btnSave.isEnabled = true;
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.getWidth();
        val height = bm.getHeight();
        val scaleWidth: Float = (newWidth.toFloat()) / width;
        val scaleHeight: Float = (newHeight.toFloat()) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix: Matrix = Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap: Bitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun onDestroy() {
        super.onDestroy()
        avatarImage?.close();
        avatarImage = null;
    }

    private fun setColorFocusEditText(editText: TextInputEditText, isFocus: Boolean) {
        val drawable = ResourcesCompat.getDrawable(this.resources, R.drawable.ic_pen, null)
        val drawableFocus = ResourcesCompat.getDrawable(this.resources, R.drawable.ic_pen_blue, null)
        val drawableCompat = drawableFocus?.let { DrawableCompat.wrap(it) }
        drawableCompat?.let {
            DrawableCompat.setTint(it, ResourcesCompat.getColor(this.resources, R.color.text_color_blue, null))
            DrawableCompat.setTintMode(drawableCompat, PorterDuff.Mode.SRC_IN);
        }

        if (isFocus) {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableFocus, null);

        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
        const val RESULT_LOAD_IMG = 33768;
        const val RESULT_TAKE_IMAGE_FROM_CAMERA = 33769;
        const val REQUEST_CAMERA_PERMISSION = 34768;
        const val REQUEST_READ_EXTERNAL_STORAGE = 34778;
    }
}
