package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.PreferenceManager
import dagger.android.support.DaggerAppCompatActivity
import im.vector.ErrorListener
import im.vector.Matrix
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.CommonActivityUtils
import im.vector.analytics.TrackingEvent
import im.vector.databinding.ActivitySplashBinding
import im.vector.services.EventStreamService
import im.vector.util.PreferencesManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.listeners.IMXEventListener
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.util.Log
import vmodev.clearkeep.activities.interfaces.ISplashActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.ISplashActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class SplashActivity : DataBindingDaggerActivity(), ISplashActivity {

    @Inject
    lateinit var viewModelFactory: ISplashActivityViewModelFactory;

    private lateinit var binding: ActivitySplashBinding;

    private val mListeners = HashMap<MXSession, IMXEventListener>()
    private val mDoneListeners = HashMap<MXSession, IMXEventListener>()

    private val mLaunchTime = System.currentTimeMillis()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash, dataBindingComponent);

        binding.rooms = viewModelFactory.getViewModel().getAllRoomResult();
        if (!hasCredentials()) {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
            finish();
        } else {
            startApp();
        }
        binding.lifecycleOwner = this;
    }

    private fun startApp() {
        val sessions = Matrix.getInstance(applicationContext)!!.sessions

        if (sessions == null) {
            finish()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
                && PreferenceManager.getDefaultSharedPreferences(this).getInt(PreferencesManager.VERSION_BUILD, 0) < 81200
                && PreferenceManager.getDefaultSharedPreferences(this).getBoolean(NEED_TO_CLEAR_CACHE_BEFORE_81200, true)) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(NEED_TO_CLEAR_CACHE_BEFORE_81200, false)
                    .apply()

            // Force a clear cache
//            Matrix.getInstance(this)!!.reloadSessions(this)
            reloadSession(applicationContext);
            return
        }
        checkLazyLoadingStatus(sessions);
    }

    private fun reloadSession(context: Context) {
        CommonActivityUtils.logout(context, Matrix.getInstance(applicationContext).sessions, false, object : SimpleApiCallback<Void>() {
            override fun onSuccess(p0: Void?) {
                synchronized("") {
                    // build a new sessions list
                    val configs = Matrix.getInstance(applicationContext).loginStorage.credentialsList

                    for (config in configs) {
                        val session = Matrix.getInstance(applicationContext).createSession(config)
                        Matrix.getInstance(applicationContext).sessions.add(session)
                    }
                }

                // clear FCM token before launching the splash screen
                Matrix.getInstance(context)!!.pushManager.clearFcmData(object : SimpleApiCallback<Void>() {
                    override fun onSuccess(anything: Void?) {
//                        startApp();
//                        val intent = Intent(context.applicationContext, SplashActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        context.applicationContext.startActivity(intent)

                        val intent = Intent(this@SplashActivity, SplashActivity::class.java);
                        startActivity(intent);
//                        finish();

//                        if (null != VectorApp.getCurrentActivity()) {
//                            VectorApp.getCurrentActivity().finish()
//
//                            if (context is SplashActivity) {
//                                // Avoid bad visual effect, due to check of lazy loading status
//                                context.overridePendingTransition(0, 0)
//                            }
//                        }
                    }
                })
            }
        })
    }

    private fun hasCredentials(): Boolean {
        try {
            val session = Matrix.getInstance(applicationContext).defaultSession;
            return null != session && session.isAlive
        } catch (e: Exception) {
            Log.d("Error Credentials: ", e?.message)
        }

        runOnUiThread {
            try {
                // getDefaultSession could trigger an exception if the login data are corrupted
                CommonActivityUtils.logout(this@SplashActivity)
            } catch (e: Exception) {
                Log.d("Error Credentials: ", e?.message)
            }
        }

        return false
    }

    private fun hasCorruptedStore(): Boolean {
        var hasCorruptedStore = false
        val sessions = Matrix.getMXSessions(this)

        for (session in sessions) {
            if (session.isAlive) {
                hasCorruptedStore = hasCorruptedStore or session.dataHandler.store.isCorrupted
            }
        }
        return hasCorruptedStore
    }

    private fun onFinish() {
        val finishTime = System.currentTimeMillis()
        val duration = finishTime - mLaunchTime
        val event = TrackingEvent.LaunchScreen(duration)
        VectorApp.getInstance().analytics.trackEvent(event)

        if (!hasCorruptedStore()) {
            val intent = Intent(this, HomeScreenActivity::class.java)

            viewModelFactory.getViewModel().getAllRoomResult().observe(this, Observer { t ->
                if (t?.status == Status.SUCCESS) {
                    startActivity(intent)
                    finish()
                }
            })
            viewModelFactory.getViewModel().setFiltersForGetAllRoom(arrayOf(1, 2, 65, 66, 129, 130));
        } else {
            CommonActivityUtils.logout(this)
        }
    }

    private fun checkLazyLoadingStatus(sessions: List<MXSession>) {
        // Note: currently Riot uses a simple boolean to enable or disable LL, and does not support multi sessions
        // If it was the case, every session may not support LL. So for the moment, only consider 1 session
        if (sessions.size != 1) {
            // Go to next step
            startEventStreamService(sessions)
        }
        // If LL is already ON, nothing to do
        if (PreferencesManager.useLazyLoading(this)) {
            // Go to next step
            startEventStreamService(sessions)
        } else {
            // Check that user has not explicitly disabled the lazy loading
            if (PreferencesManager.hasUserRefusedLazyLoading(this)) {
                // Go to next step
                startEventStreamService(sessions)
            } else {
                // Try to enable LL
                val session = sessions[0]

                session.canEnableLazyLoading(object : ApiCallback<Boolean> {
                    override fun onNetworkError(e: Exception) {
                        // Ignore, maybe next time
                        startEventStreamService(sessions)
                    }

                    override fun onMatrixError(e: MatrixError) {
                        // Ignore, maybe next time
                        startEventStreamService(sessions)
                    }

                    override fun onUnexpectedError(e: Exception) {
                        // Ignore, maybe next time
                        startEventStreamService(sessions)
                    }

                    override fun onSuccess(info: Boolean?) {
                        if (info!!) {
                            // We can enable lazy loading
                            PreferencesManager.setUseLazyLoading(this@SplashActivity, true)

                            // Reload the sessions
                            reloadSession(this@SplashActivity);
                        } else {
                            // Maybe in the future this home server will support it
                            startEventStreamService(sessions)
                        }
                    }
                })
            }
        }
    }

    private fun startEventStreamService(sessions: Collection<MXSession>) {
        var matrixIds: MutableList<String> = ArrayList()

        for (session in sessions) {

            val eventListener = object : MXEventListener() {
                private fun onReady() {
                    val isAlreadyDone: Boolean

                    synchronized("") {
                        isAlreadyDone = mDoneListeners.containsKey(session)
                    }

                    if (!isAlreadyDone) {
                        synchronized("") {
                            val noMoreListener: Boolean

                            mDoneListeners[session] = mListeners[session]!!
                            // do not remove the listeners here
                            // it crashes the application because of the upper loop
                            //fSession.getDataHandler().removeListener(mListeners.get(fSession));
                            // remove from the pending list

                            mListeners.remove(session)
                            noMoreListener = mListeners.size == 0

                            if (noMoreListener) {
                                VectorApp.addSyncingSession(session)
                                onFinish()
                            }
                        }
                    }
                }

                // should be called if the application was already initialized
                override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
                    onReady()
                }

                // first application launched
                override fun onInitialSyncComplete(toToken: String?) {
                    onReady()
                }
            }

            if (!session.dataHandler.isInitialSyncComplete) {
                session.dataHandler.store.open()

                mListeners[session] = eventListener
                session.dataHandler.addListener(eventListener)

                // Set the main error listener
                session.setFailureCallback(ErrorListener(session, this))

                // session to activate
                matrixIds.add(session.credentials.userId)
            }
        }

        // when the events stream has been disconnected by the user
        // they must be awoken even if they are initialized
        if (Matrix.getInstance(this)!!.mHasBeenDisconnected) {
            matrixIds = ArrayList()

            for (session in sessions) {
                matrixIds.add(session.credentials.userId)
            }

            Matrix.getInstance(this)!!.mHasBeenDisconnected = false
        }

        if (EventStreamService.getInstance() == null) {
            // Start the event stream service
            val intent = Intent(this, EventStreamService::class.java)
            intent.putExtra(EventStreamService.EXTRA_MATRIX_IDS, matrixIds.toTypedArray())
            intent.putExtra(EventStreamService.EXTRA_STREAM_ACTION, EventStreamService.StreamAction.START.ordinal)
            startService(intent)
        } else {
            EventStreamService.getInstance()!!.startAccounts(matrixIds)
        }

        // trigger the push registration if required
        val pushManager = Matrix.getInstance(applicationContext)!!.pushManager
        pushManager.deepCheckRegistration(this)

        val noUpdate: Boolean

        synchronized("") {
            noUpdate = mListeners.size == 0
        }

        // nothing to do ?
        // just dismiss the activity
        if (noUpdate) {
            // do not launch an activity if there was nothing new.
            onFinish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sessions = mDoneListeners.keys

        for (session in sessions) {
            if (session.isAlive) {
                session.dataHandler.removeListener(mDoneListeners[session])
                session.setFailureCallback(null)
            }
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val NEED_TO_CLEAR_CACHE_BEFORE_81200 = "NEED_TO_CLEAR_CACHE_BEFORE_81200";
    }
}
