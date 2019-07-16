package vmodev.clearkeep.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import im.vector.R
import im.vector.databinding.ActivityBackupKeyBinding
import vmodev.clearkeep.activities.interfaces.IBackupKeyActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.BackupKeyManageFragment
import vmodev.clearkeep.ultis.setupSupportToolbar
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyActivityViewModel
import javax.inject.Inject

class BackupKeyActivity : DataBindingDaggerActivity(), IBackupKeyActivity, BackupKeyManageFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractBackupKeyActivityViewModel>;

    private lateinit var binding: ActivityBackupKeyBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_backup_key, dataBindingComponent);
        userId = intent.getStringExtra(USER_ID);
        binding.toolbar.setupSupportToolbar(this, R.string.keys_backup);
        binding.keyBackup = viewModelFactory.getViewModel().getKeyBackupResult();
        viewModelFactory.getViewModel().getKeyBackupResult().observe(this, Observer {
            it?.let {
                it?.data?.let {
                    when (it.state) {
                        6, 7, 4 -> {
                            supportFragmentManager.beginTransaction().replace(R.id.container, BackupKeyManageFragment.newInstance(userId)).commitNow();
                        }
                    }
                }
                it?.message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show();
                }
            }
        })
        binding.buttonStartBackup.setOnClickListener {
            val intentStartBackup = Intent(this, PushBackupKeyActivity::class.java);
            startActivityForResult(intentStartBackup, HANDLE_PUSH_BACKUP_KEY);
        }
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setIdForGetKeyBackup(userId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HANDLE_PUSH_BACKUP_KEY && resultCode == Activity.RESULT_OK) {
            viewModelFactory.getViewModel().setIdForGetKeyBackup(userId);
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    companion object {
        const val USER_ID = "USER_ID";
        const val HANDLE_PUSH_BACKUP_KEY = 11546;
    }
}
