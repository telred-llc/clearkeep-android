package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.ActivityHomeScreenBinding
import im.vector.services.EventStreamService
import im.vector.ui.badge.BadgeProxy
import im.vector.util.HomeRoomsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_screen.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.activities.interfaces.IHomeScreenActivity
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenViewModelFactory
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.IListRoomOnFragmentInteractionListener
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class HomeScreenActivity : DataBindingDaggerActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        IListRoomOnFragmentInteractionListener
        , SearchFragment.OnFragmentInteractionListener
        , PreviewFragment.OnFragmentInteractionListener, ListRoomFragment.OnFragmentInteractionListener, IHomeScreenActivity {

    @Inject
    @field:Named(value = IFragmentFactory.HOME_SCREEN_FRAGMENT)
    lateinit var homeScreenFragmentFactory: IFragmentFactory;
//    @Inject
//    @field:Named(value = IFragmentFactory.FAVOURITES_FRAGMENT)
//    lateinit var favouritesFragmentFactory: IFragmentFactory;
    @Inject
    @field:Named(IFragmentFactory.CONTACTS_FRAGMENT)
    lateinit var contactsFragmentFactory: IFragmentFactory;
    @Inject
    @field:Named(IFragmentFactory.LIST_ROOM_FRAGMENT)
    lateinit var listRoomFragmentFactory: IFragmentFactory;
    @Inject
    lateinit var viewModelFactory: IHomeScreenViewModelFactory;

    lateinit var binding: ActivityHomeScreenBinding;
    lateinit var mxSession: MXSession;
    private lateinit var homeRoomViewModel: HomeRoomsViewModel;

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen, dataBindingComponent);
        val startFromLogin = intent.getIntExtra(START_FROM_LOGIN, 0);
        startIncomingCall();
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        (application as ClearKeepApplication).setEventHandler();
        binding.bottomNavigationViewHomeScreen.setOnNavigationItemSelectedListener { menuItem ->
            kotlin.run {
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        switchFragment(listRoomFragmentFactory.createNewInstance().getFragment());
                    };
//                    R.id.action_favorites -> {
//                        switchFragment(favouritesFragmentFactory.createNewInstance().getFragment());
//                    };
                    R.id.action_contacts -> {
                        switchFragment(contactsFragmentFactory.createNewInstance().getFragment());
                    };
                }
                return@run true;
            }
        };
        binding.circleImageViewAvatar.setOnClickListener { v ->
            kotlin.run {
                val intent = Intent(this, ProfileActivity::class.java);
                startActivity(intent);
            }
        }
        homeRoomViewModel = HomeRoomsViewModel(mxSession);

        switchFragment(listRoomFragmentFactory.createNewInstance().getFragment());

        binding.frameLayoutSearch.setOnClickListener { v ->
            //            val intent = Intent(this, SearchActivity::class.java);
            val intent = Intent(this, UnifiedSearchActivity::class.java)
            startActivity(intent);
        }
        binding.user = viewModelFactory.getViewModel().getUserById();
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        binding.roomsFavourite = viewModelFactory.getViewModel().getListRoomTypeFavouriteResult();
        binding.lifecycleOwner = this;
        binding.buttonCreateConvention.setOnClickListener { v ->
            kotlin.run {
                val intent = Intent(this, FindAndCreateNewConversationActivity::class.java)
                startActivity(intent);
            }
        }
        viewModelFactory.getViewModel().setValueForUserById(mxSession.myUserId);
        viewModelFactory.getViewModel().setValueForListRoomType(arrayOf(1, 2, 65, 66))
        viewModelFactory.getViewModel().setValueForListRoomTypeFavourite(arrayOf(129, 130))
        if (intent.hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
            val intentExtra: Intent = intent.getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS);
            if (mxSession.dataHandler.store.isReady) {
                runOnUiThread {
                    CommonActivityUtils.sendFilesTo(this@HomeScreenActivity, intentExtra)
                }
            } else {
//                mSharedFilesIntent = sharedFilesIntent
            }
        }

        if (startFromLogin != 0) {
            viewModelFactory.getViewModel().getBackupKeyStatusResult().observe(this, android.arch.lifecycle.Observer {
                it?.data?.let {
                    binding.progressBar.visibility = View.GONE;
                    if (it == 2) {
                        AlertDialog.Builder(this).setTitle(R.string.backup).setMessage(R.string.have_not_create_backup_key_content)
                                .setPositiveButton(R.string.close, null)
                                .setNegativeButton(R.string.start_using_backup_key) { dialogInterface, i ->
                                    val intentBackupKey = Intent(this, PushBackupKeyActivity::class.java);
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                                }
                                .show();
                    } else if (it == 4) {
                        AlertDialog.Builder(this).setTitle(R.string.backup).setMessage(R.string.have_had_backup_key_content)
                                .setPositiveButton(R.string.close, null)
                                .setNegativeButton(R.string.start_using_backup_key) { dialogInterface, i ->
                                    val intentBackupKey = Intent(this, RestoreBackupKeyActivity::class.java);
                                    intentBackupKey.putExtra(RestoreBackupKeyActivity.USER_ID, mxSession.myUserId);
                                    startActivityForResult(intentBackupKey, WAITING_FOR_BACK_UP_KEY);
                                }
                                .show();
                    } else {

                    }
                }
            });
            binding.progressBar.visibility = View.VISIBLE;
            viewModelFactory.getViewModel().setValueForGetBackupStatus(Calendar.getInstance().timeInMillis);
        }

    }

    private fun startIncomingCall() {
        if (intent.getStringExtra(EXTRA_CALL_SESSION_ID).isNullOrEmpty() || intent.getStringExtra(EXTRA_CALL_ID).isNullOrEmpty())
            return;
        val intentCall = Intent(this, CallViewActivity::class.java);
        intentCall.putExtra(CallViewActivity.EXTRA_MATRIX_ID, intent.getStringExtra(EXTRA_CALL_SESSION_ID));
        intentCall.putExtra(CallViewActivity.EXTRA_CALL_ID, intent.getStringExtra(EXTRA_CALL_ID));
        startActivity(intentCall);
    }

    private fun switchFragment(fragment: Fragment) {
      Handler().post(Runnable {
          kotlin.run {
              val transaction = supportFragmentManager.beginTransaction();
              transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
              transaction.replace(R.id.frame_layout_fragment_container, fragment);
              transaction.addToBackStack(null);
              transaction.commit();
          }
      })
    }


    override fun onGetMXSession(): MXSession {
        return mxSession;
    }

    override fun onFragmentInteraction(uri: Uri) {
    }


    override fun onClickItemJoin(roomId: String) {
        joinRoom(roomId)
    }

    override fun onClickItemDecline(roomId: String) {
        onRejectInvitation(roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
            }
        })
    }

    override fun onClickItemPreview(roomId: String) {
        val intent: Intent = Intent(this, PreviewInviteRoomActivity::class.java);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    override fun onJoinClick(room: Room) {
        onBackPressed();
        joinRoom(room.roomId)
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
    }

    override fun onDeclineClick(room: Room) {
        onBackPressed();
        onRejectInvitation(room.roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
            }
        })
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
    }

    override fun onNavigationOnClick() {
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
        onBackPressed();
    }

    protected fun openRoom(room: Room?) {
        // sanity checks
        // reported by GA
        if (null == mxSession.dataHandler || null == mxSession.dataHandler.store) {
            return
        }

        val roomId: String?
        // cannot join a leaving room
        if (room == null || room.isLeaving) {
            roomId = null
        } else {
            roomId = room.roomId
        }

        if (roomId != null) {
            val roomSummary = mxSession.dataHandler.store.getSummary(roomId)

            if (null != roomSummary) {
                room!!.sendReadReceipt()
            }

            // Update badge unread count in case device is offline
            BadgeProxy.specificUpdateBadgeUnreadCount(mxSession, this)

            // Launch corresponding room activity
            val params = HashMap<String, Any>()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId

            CommonActivityUtils.goToRoomPage(this, mxSession, params)
        }
    }

    fun onRejectInvitation(roomId: String, onSuccessCallback: ApiCallback<Void>?) {
        val room = mxSession.dataHandler.getRoom(roomId)

        if (null != room) {
            room!!.leave(createForgetLeaveCallback(roomId, onSuccessCallback))
        }
    }

    private fun createForgetLeaveCallback(roomId: String, onSuccessCallback: ApiCallback<Void>?): ApiCallback<Void> {
        return object : ApiCallback<Void> {
            override fun onSuccess(info: Void) {
                // clear any pending notification for this room
                EventStreamService.cancelNotificationsForRoomId(mxSession.myUserId, roomId)

                onSuccessCallback?.onSuccess(null)
            }

            private fun onError(message: String) {
                Toast.makeText(this@HomeScreenActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
//                    consentNotGivenHelper.displayDialog(e)
                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        }
    }

    private fun joinRoom(roomId: String) {
        binding.progressBar.visibility = View.VISIBLE;
        val room = mxSession.dataHandler.store.getRoom(roomId);
        mxSession.joinRoom(room!!.roomId, object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()

                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.myUserId
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.roomId
                params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = room!!.isDirect;

                CommonActivityUtils.goToRoomPage(this@HomeScreenActivity, mxSession, params)
                binding.progressBar.visibility = View.GONE;
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@HomeScreenActivity, errorMessage, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE;
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
                binding.progressBar.visibility = View.GONE;
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                    binding.progressBar.visibility = View.GONE;
                } else {
                    onError(e.localizedMessage)
                    binding.progressBar.visibility = View.GONE;
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
                binding.progressBar.visibility = View.GONE;
            }
        })
    }

    override fun onClickGoRoom(roomId: String) {
        val room = mxSession.dataHandler.getRoom(roomId);
        openRoom(room);
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

    companion object {
        const val START_FROM_LOGIN = "START_FROM_LOGIN";
        const val WAITING_FOR_BACK_UP_KEY = 11352;
        const val EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID"
        const val EXTRA_CALL_ID = "EXTRA_CALL_ID"
        const val EXTRA_CALL_SESSION_ID = "EXTRA_CALL_SESSION_ID";
    }
}
