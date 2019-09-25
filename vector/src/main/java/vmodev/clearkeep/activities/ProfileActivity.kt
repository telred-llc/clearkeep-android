package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile, dataBinding.getDataBindingComponent());
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.profile);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.user = viewModelFactory.getViewModel().getCurrentUserResult();
        binding.checkNeedBackup = viewModelFactory.getViewModel().getNeedBackupWhenLogout();
        viewModelFactory.getViewModel().setIdForGetCurrentUser(mxSession.myUserId);
        binding.buttonSignOut.setOnClickListener {
            viewModelFactory.getViewModel().setCheckNeedBackupWhenSignOut(Calendar.getInstance().timeInMillis)
        }
        binding.buttonSetting.setOnClickListener {
            val intentProfileSetting = Intent(this, SettingsActivity::class.java);
//            intentProfileSetting.putExtra(ProfileSettingsActivity.USER_ID, mxSession.myUserId);
            startActivity(intentProfileSetting);
        }
        binding.buttonEditProfile.setOnClickListener {
            val intentEditProfile = Intent(this, EditProfileActivity::class.java);
            intentEditProfile.putExtra(EditProfileActivity.USER_ID, mxSession.myUserId)
            startActivity(intentEditProfile)
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

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 10343;
    }
}
