package vmodev.clearkeep.activities

import android.Manifest
import android.os.Bundle
import android.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.facebook.react.bridge.UiThreadUtil
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorCallViewActivity
import im.vector.activity.VectorCallViewActivity.EXTRA_MATRIX_ID
import im.vector.activity.VectorHomeActivity
import im.vector.databinding.ActivityIncomingCallBinding
import im.vector.util.CallsManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import vmodev.clearkeep.activities.interfaces.IActivity
import java.util.ArrayList

class IncomingCallActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityIncomingCallBinding
    private var mxCall: IMXCall? = null
    private lateinit var navController: NavController
    private var mxSession: MXSession? = null
    private var mUnknownDevicesMap: MXUsersDevicesMap<MXDeviceInfo>? = null

    companion object {
        const val ROOM_ID = "ROOM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call, dataBinding.getDataBindingComponent())
        navController = findNavController(R.id.fragment)
        mxSession = Matrix.getInstance(applicationContext)!!.getSession(intent.getStringExtra(CallViewActivity.EXTRA_MATRIX_ID))
        mUnknownDevicesMap = intent.getSerializableExtra(VectorHomeActivity.EXTRA_CALL_UNKNOWN_DEVICES)?.let {
            it as MXUsersDevicesMap<MXDeviceInfo>
        }
        mxCall = CallsManager.getSharedInstance().activeCall

        runOnUiThread(Runnable {
            val lisDevice = getDevicesList()
            setDevicesKnown(lisDevice)
        })

        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        val navInflater = navController.navInflater
                        val graph = navInflater.inflate(R.navigation.navigation_incoming_call)
                        navController.graph = graph
                        navController.navigate(R.id.incomingCallFragment)
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        mxCall?.hangup(null)
                        finish()
                    }
                })
                .setDeniedMessage("Please go to your Setting and turn on Permission")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .check()
    }

    override fun getActivity(): FragmentActivity {
        return this
    }

    private fun setDevicesKnown(devicesList: List<Pair<String, List<MXDeviceInfo>>>) {
        if (null != mUnknownDevicesMap) {
            // release the static members list
            mUnknownDevicesMap = null

            val dis = ArrayList<MXDeviceInfo>()

            for (item in devicesList) {
                dis.addAll(item.second)
            }
            if (!dis.isNullOrEmpty()) {
                mxSession?.crypto?.setDevicesKnown(dis, object : ApiCallback<Void> {
                    // common method
                    private fun onDone() {
                    }

                    override fun onSuccess(info: Void) {
                        onDone()
                    }

                    override fun onNetworkError(e: Exception) {
                        onDone()
                    }

                    override fun onMatrixError(e: MatrixError) {
                        onDone()
                    }

                    override fun onUnexpectedError(e: Exception) {
                        onDone()
                    }
                })
            }
        }

    }

    fun getDevicesList(): List<Pair<String, List<MXDeviceInfo>>> {
        val res = ArrayList<Pair<String, List<MXDeviceInfo>>>()

        // sanity check
        if (null != mUnknownDevicesMap) {
            val userIds = mUnknownDevicesMap!!.userIds

            for (userId in userIds) {
                val deviceInfos = ArrayList<MXDeviceInfo>()
                val deviceIds = mUnknownDevicesMap!!.getUserDeviceIds(userId)

                for (deviceId in deviceIds) {
                    deviceInfos.add(mUnknownDevicesMap!!.getObject(deviceId, userId))
                }
                res.add(Pair(userId, deviceInfos))
            }
        }

        return res
    }


//        val callDevice = intent.getSerializableExtra(VectorHomeActivity.EXTRA_CALL_UNKNOWN_DEVICES)?.let {
//            it as MXUsersDevicesMap<MXDeviceInfo>
//        }
//        UiThreadUtil.runOnUiThread(Runnable {
//            CommonActivityUtils.displayUnknownDevicesDialog(mxSession,
//                    this,
//                    callDevice,
//                    true,
//                    null)
//        })


//        mxSession?.let { s ->
//            s.crypto?.let { crypto ->
//                crypto.getUserDevices(application.getUserId()).forEach {
//                    crypto.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, it.deviceId,it.userId, object : ApiCallback<Void> {
//                        override fun onSuccess(p0: Void?) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                        override fun onUnexpectedError(p0: Exception?) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                        override fun onMatrixError(p0: MatrixError?) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                        override fun onNetworkError(p0: Exception?) {
//                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                        }
//
//                    })
//                }
//
//            }
//
//        }

    // }

}
