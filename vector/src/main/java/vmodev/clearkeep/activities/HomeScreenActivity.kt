package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.databinding.ActivityHomeScreenBinding
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXOlmDevice
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXKey
import org.matrix.androidsdk.crypto.data.MXOlmSession
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.rest.model.Event
import org.matrix.olm.OlmAccount
import org.matrix.olm.OlmManager
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeScreenActivity : DataBindingDaggerActivity(), IActivity {
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractHomeScreenActivityViewModel>;

    @Inject
    lateinit var messageDao: AbstractMessageDao;

    private var doubleBackPressed: Boolean = false;

    lateinit var binding: ActivityHomeScreenBinding;
    lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.getWindow();
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_hint_text_color_light)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen, dataBinding.getDataBindingComponent());
        startIncomingCall();
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        binding.circleImageViewAvatar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
        }

        binding.frameLayoutSearch.setOnClickListener { v ->
            val intent = Intent(this, SearchActivity::class.java);
            intent.putExtra(SearchActivity.USER_ID, mxSession.myUserId);
            startActivity(intent);
        }
        binding.user = viewModelFactory.getViewModel().getUserById();

        binding.lifecycleOwner = this;
        binding.buttonCreateConvention.setOnClickListener {
            val intent = Intent(this, NewRoomActivity::class.java)
            startActivity(intent);
        }
        viewModelFactory.getViewModel().setValueForUserById(mxSession.myUserId);

        if (intent.hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
            val intentExtra: Intent = intent.getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS);
            mxSession.dataHandler.store?.let {
                if (it.isReady) {
                    runOnUiThread {
                        CommonActivityUtils.sendFilesTo(this@HomeScreenActivity, intentExtra)
                    }
                } else {
//                mSharedFilesIntent = sharedFilesIntent
                }
            }
        }

        val navController = Navigation.findNavController(this@HomeScreenActivity, R.id.frame_layout_fragment_container);
        binding.bottomNavigationViewHomeScreen.setupWithNavController(navController);


//        Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    mxSession.crypto?.let {
//                        val otks = it.getOlmDevice()?.privateOneTimeKeys;
//                        otks?.forEach {
//                            Log.d("OneTime", it.key);
//                            it.value.forEach {
//                                Log.d("OneTime", it.key + "--" + it.value);
//                            }
//                        }
//                    }
//                }

//        mxSession.crypto?.getOlmDevice()?.insertOneTimeKeyToLast("GPifSqNX5o/gxYSEJ4ZgkWCK7it8c3XYfrSUXbBFaCw", "Qqurq6urq6urq6urq6urq6urq6urq6urq6urq6urq6s");
        mxSession.crypto?.getOlmDevice()?.let {
            val olmAccount = OlmAccount();
            val mxDevice = MXOlmDevice(mxSession.crypto?.cryptoStore, olmAccount);
            mxDevice.changeIdentityKeys("FrcqCNaal67PMQylsEHVx0jB1VxbEVoumFw/hZ4f7AE", "5miVvYU7XUeqjVuBV8I2ydlgTb8WFIjrmP+DdEdxj9g"
                    , "B6EKRr3Us7iuhPvzlzPwj8IyWSG8Xez/A4VJRUQwlw0", "0EQ+9U641XsPKau0filAzRc07P9ZugicF3z8q5BV8VNW1zRo/YXgrp+zae7W1Ust/xiYlsMFfxM3q1WMLyLDig");
            mxDevice.generateOneTimeKeys(50);
            mxDevice.insertOneTimeKeyToLast("AAAAAQ","fDQGGAGCtQY1B2TXoB+RCciQNVcEjZuIK6Lh7YBOC3g", "aFiaJL7Lp7ROAT8/CjYXn1zDD/wPv6ikxuXFyzUYLDk");
            val senderKey = "zulhuJs5WaszJzCR0ZI6qj7EqzK3BjWgYrMA3aZ1pwk";
            val ciphertext = "AwogfDQGGAGCtQY1B2TXoB+RCciQNVcEjZuIK6Lh7YBOC3gSIFiFAYMyk7pfvYkxn3MiWtphSuQc2w/YCGJKNanO6GI6GiDO6WG4mzlZqzMnMJHRkjqqPsSrMrcGNaBiswDdpnWnCSKwBgMKIBmJD1OKiyT4UjQsBJwM7AZdpzEgBdUUblviIlaPehg6EAAigAYUIfzn+Ts6BvZAKJrTtSqC3egb85Y/TxIXQUM9k7NqlLznIded1dOtXfcLrusZZ0W2hFmVP5eLEW9iD9sKEhIKx6wUlwQAc4Rvdwf4qH75weXDhXja01iOeX0X41Zlc5MNYiqHUu+7TGIHCw/3CfZO4H/ZCSz0qcc8gYci0jTxKZxIoHbeJnZ/DA+5ZGJufzwTL4tChBIbetCkdT0dU3kp6ODIKnjDeIb3OSzmWDyaW8WzF8TCEqt6ZxRvWvf3LyS8MldrNZ6wRUqkElEQ1iWKbYFJ1yVEp3OoJj2CkoKqga0SjgDi8HFrfIzyTZba9x5+QBH5Dt+0Ux71lxencuSPFeB3bLXGrVBkV0oNWfg6Xrg5m5ACvPSnhy8coR6DBq8Q6g1lIiQjCGGvmLKFg1qRUsImWXeob9Eat8WbKjD/wsXzIm8RJFsdpL+vhfwNPwCDTCoVk+XFhdhSaSpdh8syj+chSdK1X1jbAkQ2MOBFvxRE8b8WBLaBCfZ+KXV/gIBRLqmA5zTSLXvX9Pbh5IxxtOXSBRHbb6bYxbl00wAeWfnQZOKn362tE+Am9ct9aLXhWr+R+RV35dpZhxHcDf9JV7dWLT3JIiUrBesPqcydUa3X233/IadSGq58flxIK2p2kjlnytM6XAjJxTGfnQWwgkJnKL8FgujWbqGEcA4vQYmTN0rhBD5KS9djL6Lf8rQLdJTXJlCCdS7bOwo+I9zWRPHwvpYDzlTUUCGErUym7YRYGka/+wenaRBUBS/+nB8UJ8Xr3Xc+E6m5VLCoKUFkybBjaV5tIduCwdkjhBdxIduSfDTLh04AgOwAtnRiOEFzArwbv53nUTahjq66JjQI7nlCwMSB9TPlWawij8CDAtCJHAXHrc37WWRS754lFSCffAN1Cmuk7LAKqgYTXQ4sU3jqSPgq/VodfFEzQXvfLzLoI86kR9BjrECZVj1E5ckL2Zzd1Ix3Kk1xoh8FAPspUz17K9Pu6uZDYcz4HmNScdDUfbR+1VfbLotIze9LF1FxqRlftjrsHA";
//            val signature = "obud+E4kKR2UBMD4zHguayL+dqQHEodb0K8CqRgUtH/6eMTmbJe8NHVuVQt685EZV26jscW74jYbluSPQfN+Dg";
//            val key = "nGRsjqvPOSFk6Y9JlqZqcsRWf8v7V3slUh8s9Uqh4Uw";
//
//            val algorithms  = arrayListOf<String>("m.olm.v1.curve25519-aes-sha2", "m.megolm.v1.aes-sha2");
//            val deviceId = "FYXNDMYCNI";
//            val userId = "@boyoung:local.vmodev.com";
//            val linkedTreeMap = linkedMapOf<String, String>("curve25519:FYXNDMYCNI" to "FnjGJ+WBatXxWWTyP8xcpxHYtJC5JXdAhqlKvJ054Gw", "ed25519:FYXNDMYCNI" to "nGRsjqvPOSFk6Y9JlqZqcsRWf8v7V3slUh8s9Uqh4Uw");
//
//            val hashData = mapOf<String, Any>("algorithms" to algorithms, "device_id" to deviceId, "user_id" to userId, "keys" to linkedTreeMap);
//
//
//            val signature2 = "DyWpx2567M7O5pEcUXlJ3YXWgiE52V+36HAiktplmcFF3c8qtu83MW486iGBODcFvL6hjtrIfVZBdJUwkIDWDA";
//            val key2 = "aUrqIArddvyTip20rrckF5xFJUYZBUO5SYFmsPEO9jc";
//
//            val algorithms2  = arrayListOf<String>("m.olm.v1.curve25519-aes-sha2", "m.megolm.v1.aes-sha2");
//            val deviceId2 = "WKACBUCWDZ";
//            val userId2 = "@boyoung:local.vmodev.com";
//            val linkedTreeMap2 = linkedMapOf<String, String>("curve25519:WKACBUCWDZ" to "XS7YGL28oTxzozKeA9BKd1QjcynfdPJDjXLLRGAnt0Y", "ed25519:WKACBUCWDZ" to "aUrqIArddvyTip20rrckF5xFJUYZBUO5SYFmsPEO9jc");
//
//            val hashData2 = mapOf<String, Any>("algorithms" to algorithms2, "device_id" to deviceId2, "user_id" to userId2, "keys" to linkedTreeMap2);
//
//            val signature3 = "r4RqV3mSr7e9roTVm1kUrSY5MlJ9yGUGxip1vvRJDxB8Nq0zFVrWap4A4DUBiLd0f40GjymQf0UY6M9U57DDDw";
//            val key3 = "aUbDMJ8PQjAPKAYypUBwVlo2U7E7ylkXT7yKLzSrGxc";
//
//            val algorithms3  = arrayListOf<String>("m.olm.v1.curve25519-aes-sha2", "m.megolm.v1.aes-sha2");
//            val deviceId3 = "CFGCYRKIUN";
//            val userId3 = "@seolhyun:local.vmodev.com";
//            val linkedTreeMap3 = linkedMapOf<String, String>("curve25519:CFGCYRKIUN" to "2HjsPEtgkrx6Ix9WRzCb1jiZpGoLBOSt+QalA5qAAlU", "ed25519:CFGCYRKIUN" to "aUbDMJ8PQjAPKAYypUBwVlo2U7E7ylkXT7yKLzSrGxc");
//
//            val hashData3 = mapOf<String, Any>("algorithms" to algorithms3, "device_id" to deviceId3, "user_id" to userId3, "keys" to linkedTreeMap3);
//
//            val signature4 = "VAl+up5eJFOfSsSUZk1ZiecvncZb1pZkjA01k+D3QcByMgIEE+WBRzXSo3dAbOl0UvewjtwqJQdPcZ6qua/ACw";
//            val key4 = "4nlyoJQOY88lHJv/bB6AzkmLCQmJcfpANjDJhwlAD/o";
//
//            val algorithms4  = arrayListOf<String>("m.olm.v1.curve25519-aes-sha2", "m.megolm.v1.aes-sha2");
//            val deviceId4 = "RMFWSAVLCK";
//            val userId4 = "@seolhyun:local.vmodev.com";
//            val linkedTreeMap4 = linkedMapOf<String, String>("curve25519:RMFWSAVLCK" to "2HjsPEtgkrx6Ix9WRzCb1jiZpGoLBOSt+QalA5qAAlU", "ed25519:RMFWSAVLCK" to "4nlyoJQOY88lHJv/bB6AzkmLCQmJcfpANjDJhwlAD/o");
//
//            val hashData4 = mapOf<String, Any>("algorithms" to algorithms4, "device_id" to deviceId4, "user_id" to userId4, "keys" to linkedTreeMap4);
//
//            it.verifySignature(key, hashData, signature);
//            it.verifySignature(key2, hashData2, signature2);
//            it.verifySignature(key3, hashData3, signature3);
////            mxDevice.verifySignature(key3, hashData3, signature3);
////            mxDevice.verifySignature(key4, hashData4, signature4);
//
            mxDevice.createInboundSession(senderKey, 0, ciphertext);
        }
//        mxSession.crypto?.let {
//            val otks = it.getOlmDevice()?.oneTimeKeys;
//            otks?.forEach {
//                Log.d("OneTime", it.key);
//                it.value.forEach {
//                    Log.d("OneTime", it.key + "--" + it.value);
//                }
//            }
//        }


//        mxSession.crypto?.getCryptoRestClient()?.let {
//            val body = MXUsersDevicesMap<String>();
//            body.setObject("signed_curve25519", "@seung:study.sinbadflyce.com", "CGTHKDYNJT");
//            body.setObject("signed_curve25519", "@hyun:study.sinbadflyce.com", "IAOTPHAZNO");
//            it.claimOneTimeKeysForUsersDevices(body, object : ApiCallback<MXUsersDevicesMap<MXKey>>{
//                override fun onSuccess(info: MXUsersDevicesMap<MXKey>?) {
//                    Log.d("Success", info?.userIds?.size.toString());
//                    val olmAccount = OlmAccount().apply {  };
//                    olmAccount.identityKeys()?.let {
//                        mxSession.crypto?.getOlmDevice()?.let { mx ->
//                            it["ed25519"] = "82cwp8cxcSO/p9YaQ7HXQk9tUjBNAleZMqyxNLu7AMw";
//                            it["curve25519"] = "OUFlohQW8wvsoN/u74KICf8exbbSoMRIazn/Nu9AOV4";
//                        }
//                    }
//                    val mxOlmDevice = MXOlmDevice(mxSession.crypto?.cryptoStore, olmAccount);
//                    mxOlmDevice.generateOneTimeKeys(1);
//                    val otk = info?.getObject("CGTHKDYNJT", "@seung:study.sinbadflyce.com");
////                    val otk2 = info?.getObject("IAOTPHAZNO", "@hyun:study.sinbadflyce.com");
//                    mxOlmDevice.verifySignature("82cwp8cxcSO/p9YaQ7HXQk9tUjBNAleZMqyxNLu7AMw", otk?.signalableJSONDictionary(), otk?.signatureForUserId("@seung:study.sinbadflyce.com", "ed25519:CGTHKDYNJT"));
////                    mxOlmDevice.verifySignature("vrPbPUyzZOfGx7EI6zblNQsqNA+JBXp/I9b4UaYfL50", otk2?.signalableJSONDictionary(), otk2?.signatureForUserId("@hyun:study.sinbadflyce.com", "ed25519:IAOTPHAZNO"));
//                    otk?.let {
//                        Log.d("OneTime3", it.signatureForUserId("@seung:study.sinbadflyce.com", "ed25519:CGTHKDYNJT"))
//                    }
//                    mxOlmDevice.oneTimeKeys.forEach {
//                        Log.d("OneTime2", it.key);
//                        it.value.forEach {
//                            Log.d("OneTime2", it.key + "--" + it.value);
//                        }
//                    }
//                    val senderKey = "MmwRHAf5QR7KS9ts7VihJ9oE3fg75N20ltjhQ7XEumk";
//                    val ciphertext = "Awogs1N/VJpErnm7hbyYY63xiQO51QEbeKUo9/Vx2ZXeVz0SIKAHVi1mtyPCXZwFjknKHHSQ2WcJiZQ4ecpA/sJW0ZJLGiAybBEcB/lBHspL22ztWKEn2gTd+Dvk3bSW2OFDtcS6aSLABgMKIMnLc1WmS7oQMONUlDBEgusW4rR1oY2/nRBw9q5V2vgREAAikAYeBntbo2dbAwYB9yYYlN2Ji/WuujeR2JomtYSPnvod93mrbwoyr/yhchdjoMbZmO8wU9XXmpc4U4zABLFccKkFctwJ4LLe62ZgLhfgTcbcHt/zjpKUN7bsp5PhqrMOVZaQrZOVLixM1Z3q7f4TZUONLzH5SAp8b7tqTMpeAPEXoXj+xAaZs3xvwE/BXgesXXtC1GMtXiyZKLCYaqr2kl8ceUCpMZ2cF5HPkpnNkWtrd5+A/JeEQhdX0o0CADkgRJ6YUoRlRn25IRtkMHqGAVSxQdmP6lfWwiZBCzo1ieLnppg/871FE6JJSCrTktjeS8a4ZHYJ1XNIEtf62XdKem9uNNG3Et0V3cw+9RbwHS0qfT298ZtrPhaZT1nVmIxGrVWFgaYvtdQtS5+w1zEtVZuZnlqTrH59/kuv3HJ9HpLOtDTNeHzv4ifgV7JLqAjVZ7neDFhi0d2GgmR/8hKq/Hn9TbrS1oL7ompbgJBjrT6UYQ7mY9pA7T6WLS3wgwULBnGvjDrfH1qff057hhslyluXsbNq8K+1eJS1FAsndiujYpOysWO7JWaWjEWLGXceaGNG8W1GE2GbKnLRkU9rOzco4rAJVziuGYRBZu4PVgWCd+FlYuWQ82C68RYK+4250i8+Qh2w93W21VzKBCTajnXONFWFt0cqJo840T/aqdA/AikJUdUQ/XWsizUDbNsvEw7c50DG2ygn1Ud4aErBnM37ZYOKsCu6ZX3Za4AgraCv6ScZ+/ZPAWlO4InSDDldfIVgC2qALfAsXrVet+feIAO3CNSCpmEH0+ihZiMvIdqMkJqDMZS6S6N4ES1dpzEBuCR3rJc+6s+aviNSdggd1r+1CJn9R3wxjgBu+p5yEBqefetMDHfW+gVE05554qW6GYJucJWEQAQVbqNwfccKGNFJpOdRJTdf2oxKCyEb2+A/YdPZIgZkSoqB+QcnFx1veYlBTUfWXKIfiNsmr4kMp9iUVP/Baa3HKdpR0t4nJUDPUfuYkEHQ2YLKaJFPeyxvN5Li/aAo2Vn+50bUzyZiSsBOTrTtSsVURV4";
//                    mxOlmDevice.createInboundSession(senderKey, 0, ciphertext);
//                }
//
//                override fun onUnexpectedError(e: Exception?) {
//                    Log.d("Error", e.toString());
//                }
//
//                override fun onNetworkError(e: Exception?) {
//                    Log.d("Error", e.toString());
//                }
//
//                override fun onMatrixError(e: MatrixError?) {
//                    Log.d("Error", e.toString());
//                }
//            })
//        }
//        val olmManager = OlmManager();
    }

    private fun startIncomingCall() {
        if (intent.getStringExtra(EXTRA_CALL_SESSION_ID).isNullOrEmpty() || intent.getStringExtra(EXTRA_CALL_ID).isNullOrEmpty())
            return;
        val intentCall = Intent(this, CallViewActivity::class.java);
        intentCall.putExtra(CallViewActivity.EXTRA_MATRIX_ID, intent.getStringExtra(EXTRA_CALL_SESSION_ID));
        intentCall.putExtra(CallViewActivity.EXTRA_CALL_ID, intent.getStringExtra(EXTRA_CALL_ID));
        startActivity(intentCall);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WAITING_FOR_BACK_UP_KEY && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Successfully", Toast.LENGTH_LONG).show();
        }
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            finish()
        }
        this.doubleBackPressed = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable {
            doubleBackPressed = false
        }, 3000)
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 11352;
        const val EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID"
        const val EXTRA_CALL_ID = "EXTRA_CALL_ID"
        const val EXTRA_CALL_SESSION_ID = "EXTRA_CALL_SESSION_ID";
    }

    override fun onResume() {
        super.onResume()
        doubleBackPressed = false
    }
}
