package vmodev.clearkeep.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import im.vector.R
import im.vector.activity.JitsiCallActivity
import im.vector.activity.VectorMediaPickerActivity
import im.vector.databinding.FragmentCallHistoryBinding
import im.vector.util.*
import im.vector.widgets.Widget
import im.vector.widgets.WidgetManagerProvider
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.activities.OutgoingCallActivity
import vmodev.clearkeep.adapters.CallHistoryRecyclerViewAdapter
import vmodev.clearkeep.enums.TypeCallEnum
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallHistoryViewModel
import java.util.*
import javax.inject.Inject

class CallHistoryFragment : DataBindingDaggerFragment(), IFragment {
    lateinit var binding: FragmentCallHistoryBinding;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCallHistoryViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    private lateinit var listSearchAdapter: CallHistoryRecyclerViewAdapter;

    /** Handel Call*/
    private val LOG_TAG = CallHistoryFragment::class.java.simpleName
    private val TAKE_IMAGE_REQUEST_CODE = 1
    private val CAMERA_VALUE_TITLE = "attachment"
    private var mLatestTakePictureCameraUri: String? = null
    private var mxSession: MXSession? = null
    private var currentRoom: Room? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_history, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mxSession = im.vector.Matrix.getInstance(activity)!!.defaultSession
        setupRecycleView()
        initData()
    }

    override fun getFragment(): Fragment {
        return this
    }

    private fun initData() {
        viewModelFactory.getViewModel().getListMessageRoomUser().observe(viewLifecycleOwner, Observer {
            it?.data?.let { it1 ->
                viewModelFactory.getViewModel().getListCallHistory(it1).observe(viewLifecycleOwner, Observer { dataResult ->
                    dataResult?.data?.let {
                        val sortCallHistory = it.sortedWith(compareBy { it.message?.createdAt })?.reversed()
                        listSearchAdapter.submitList(sortCallHistory)
                    }

                })
            }
        })
        viewModelFactory.getViewModel().setTimeForRefreshLoadMessage(Calendar.getInstance().timeInMillis);
    }


    private fun setupRecycleView() {
        listSearchAdapter = CallHistoryRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<MessageRoomUser>() {
            override fun areItemsTheSame(oldItem: MessageRoomUser, newItem: MessageRoomUser): Boolean {
                return oldItem.message?.id == newItem.message?.id
            }

            override fun areContentsTheSame(oldItem: MessageRoomUser, newItem: MessageRoomUser): Boolean {
                return oldItem.room?.get(0)?.avatarUrl == newItem.room?.get(0)?.avatarUrl && oldItem.message?.encryptedContent == newItem.message?.encryptedContent
                        && oldItem.user?.get(0)?.name == newItem.user?.get(0)?.name;
            }
        }, dataBindingComponent = dataBinding)
        binding.rvCallHistory.adapter = listSearchAdapter;

        listSearchAdapter.setOnItemClick { messageRoomUser, i ->
            onCallItemClicked(i, messageRoomUser)

        }

    }

//    Handel Call

    private fun onCallItemClicked(typeCallEnum: Int, messageRoomUser: MessageRoomUser) {
        val isVideoCall: Boolean
        val permissions: Int
        val requestCode: Int
        currentRoom = mxSession!!.dataHandler.getRoom(messageRoomUser.room?.get(0)?.id, false)

        if (typeCallEnum == TypeCallEnum.CALL_VOICE.value) {
            isVideoCall = false
            permissions = PERMISSIONS_FOR_AUDIO_IP_CALL
            requestCode = PERMISSION_REQUEST_CODE_AUDIO_CALL
        } else {
            isVideoCall = true
            permissions = PERMISSIONS_FOR_VIDEO_IP_CALL
            requestCode = PERMISSION_REQUEST_CODE_VIDEO_CALL
        }

        if (checkPermissions(permissions, activity!!, requestCode)) {
            startIpCall(PreferencesManager.useJitsiConfCall(activity!!), isVideoCall)
        }
    }

    /**
     * Launch the camera
     */
    private fun launchNativeVideoRecorder() {
//        enableActionBarHeader(RoomActivity.HIDE_ACTION_BAR_HEADER)

        openVideoRecorder(activity!!, TAKE_IMAGE_REQUEST_CODE)
    }

    /**
     * Launch the camera
     */
    private fun launchNativeCamera() {
//        enableActionBarHeader(RoomActivity.HIDE_ACTION_BAR_HEADER)

        mLatestTakePictureCameraUri = openCamera(activity!!, CAMERA_VALUE_TITLE, TAKE_IMAGE_REQUEST_CODE)
    }

    /**
     * Launch the camera
     */
    private fun launchCamera() {

        val intent = Intent(activity, VectorMediaPickerActivity::class.java)
        intent.putExtra(VectorMediaPickerActivity.EXTRA_VIDEO_RECORDING_MODE, true)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (permissions.isNullOrEmpty()) {
            org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): cancelled $requestCode")
        } else if ((requestCode == PERMISSION_REQUEST_CODE_LAUNCH_CAMERA
                        || requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA
                        || requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA)) {
            var isCameraPermissionGranted = false
            var isWritePermissionGranted = false

            for (i in permissions.indices) {
                org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): " + permissions[i] + "=" + grantResults[i])

                if (Manifest.permission.CAMERA == permissions[i]) {
                    if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                        org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission granted")
                        isCameraPermissionGranted = true
                    } else {
                        org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission not granted")
                    }
                }

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE == permissions[i]) {
                    if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                        org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission granted")
                        isWritePermissionGranted = true
                    } else {
                        org.matrix.androidsdk.core.Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission not granted")
                    }
                }
            }

            // Because external storage permission is not mandatory to launch the camera,
            // external storage permission is not tested.
            if (isCameraPermissionGranted) {
                if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_CAMERA) {
                    launchCamera()
                } else if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA) {
                    if (isWritePermissionGranted) {
                        launchNativeCamera()
                    } else {
                        Toast.makeText(activity, getString(R.string.missing_permissions_error), Toast.LENGTH_SHORT).show()
                    }
                } else if (requestCode == PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA) {
                    if (isWritePermissionGranted) {
                        launchNativeVideoRecorder()
                    } else {
                        Toast.makeText(activity, getString(R.string.missing_permissions_error), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, getString(R.string.missing_permissions_warning), Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_AUDIO_CALL) {
            if (onPermissionResultAudioIpCall(activity!!, grantResults)) {
                startIpCall(PreferencesManager.useJitsiConfCall(activity), false)
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_VIDEO_CALL) {
            if (onPermissionResultVideoIpCall(activity!!, grantResults)) {
                startIpCall(PreferencesManager.useJitsiConfCall(activity), true)
            }
        } else {
            // Transmit to Fragment
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    /**
     * Start an IP call: audio call if aIsVideoCall is false or video call if aIsVideoCall
     * is true.
     *
     * @param useJitsiCall true to use jitsi calls
     * @param aIsVideoCall true to video call, false to audio call
     */
    private fun startIpCall(useJitsiCall: Boolean, aIsVideoCall: Boolean) {
        if (currentRoom == null) {
            return
        }

        if ((currentRoom!!.numberOfMembers > 2) && useJitsiCall) {
            startJitsiCall(aIsVideoCall)
            return
        }
//        showWaitingView()

        // create the call object
        mxSession!!.mCallsManager.createCallInRoom(currentRoom!!.roomId, aIsVideoCall, object : ApiCallback<IMXCall> {
            override fun onSuccess(call: IMXCall) {
                org.matrix.androidsdk.core.Log.d(LOG_TAG, "## startIpCall(): onSuccess")
                activity!!.runOnUiThread {
                    //                    hideWaitingView()
                    val intent = Intent(activity!!, OutgoingCallActivity::class.java);
                    startActivity(intent);
                }
            }

            private fun onError(errorMessage: String) {
                activity!!.runOnUiThread {
                    //                    hideWaitingView()
                    Toast.makeText(activity,
                            getString(R.string.cannot_start_call) + " (" + errorMessage + ")", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNetworkError(e: Exception) {
                org.matrix.androidsdk.core.Log.e(LOG_TAG, "## startIpCall(): onNetworkError Msg=" + e.message, e)
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                org.matrix.androidsdk.core.Log.e(LOG_TAG, "## startIpCall(): onMatrixError Msg=" + e.localizedMessage)

                if (e is MXCryptoError) {
                    val cryptoError = e as MXCryptoError
                    if (MXCryptoError.UNKNOWN_DEVICES_CODE == cryptoError.errcode) {
//                        hideWaitingView()

                        val devicesInfo = cryptoError.mExceptionData as MXUsersDevicesMap<MXDeviceInfo>;
                        devicesInfo?.let {
                            val deviceList = getDevicesList(it);
                            deviceList.forEach { t: Pair<String, List<MXDeviceInfo>>? ->
                                t?.second?.forEach { d: MXDeviceInfo? ->
                                    d?.let { mxDeviceInfo ->
                                        if (mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNVERIFIED || mxDeviceInfo.mVerified == MXDeviceInfo.DEVICE_VERIFICATION_UNKNOWN) {
                                            mxSession!!.crypto?.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, mxDeviceInfo.deviceId, mxDeviceInfo.userId, object : SimpleApiCallback<Void>() {
                                                override fun onSuccess(p0: Void?) {
                                                    android.util.Log.d("Verify device success", mxDeviceInfo.deviceId.toString())
                                                }
                                            })
                                        }
                                    }
                                }
                            }
                        }

                        return
                    }
                }

                onError(e.localizedMessage)
            }

            override fun onUnexpectedError(e: Exception) {
                org.matrix.androidsdk.core.Log.e(LOG_TAG, "## startIpCall(): onUnexpectedError Msg=" + e.localizedMessage, e)
                onError(e.localizedMessage)
            }
        })
    }

    /**
     * Convert a MXUsersDevicesMap to a list of List
     *
     * @return the list of list
     */
    private fun getDevicesList(devicesInfo: MXUsersDevicesMap<MXDeviceInfo>): List<Pair<String, List<MXDeviceInfo>>> {
        val res = ArrayList<Pair<String, List<MXDeviceInfo>>>()

        // sanity check
        if (null != devicesInfo) {
            val userIds = devicesInfo.userIds

            for (userId in userIds) {
                val deviceInfos = ArrayList<MXDeviceInfo>()
                val deviceIds = devicesInfo.getUserDeviceIds(userId)

                for (deviceId in deviceIds) {
                    deviceInfos.add(devicesInfo.getObject(deviceId, userId))
                }
                res.add(Pair(userId, deviceInfos))
            }
        }

        return res
    }

    /**
     * Start a jisti call
     *
     * @param aIsVideoCall true if the call is a video one
     */
    private fun startJitsiCall(aIsVideoCall: Boolean) {
        WidgetManagerProvider.getWidgetManager(activity!!)?.createJitsiWidget(mxSession, currentRoom, aIsVideoCall, object : ApiCallback<Widget> {
            override fun onSuccess(widget: Widget) {

                launchJitsiActivity(widget, aIsVideoCall)
            }

            private fun onError(errorMessage: String?) {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                onError(e.localizedMessage)
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }

    /**
     * Manage widget
     *
     * @param widget       the widget
     * @param aIsVideoCall true if it is a video call
     */
    private fun launchJitsiActivity(widget: Widget?, aIsVideoCall: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Display a error dialog for old API
            AlertDialog.Builder(activity!!)
                    .setTitle(R.string.dialog_title_error)
                    .setMessage(R.string.error_jitsi_not_supported_on_old_device)
                    .setPositiveButton(R.string.ok, null)
                    .show()
        } else {
            val intent = Intent(activity, JitsiCallActivity::class.java)
            intent.putExtra(JitsiCallActivity.EXTRA_WIDGET_ID, widget)
            intent.putExtra(JitsiCallActivity.EXTRA_ENABLE_VIDEO, aIsVideoCall)
            startActivity(intent)
        }
    }


}