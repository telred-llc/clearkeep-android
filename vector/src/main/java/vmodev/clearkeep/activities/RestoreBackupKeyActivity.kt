package vmodev.clearkeep.activities

import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityRestoreBackupKeyBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.PassphraseRestoreBackupKeyFragment
import vmodev.clearkeep.fragments.TextFileRestoreBackupKeyFragment
import vmodev.clearkeep.ultis.setupSupportToolbar
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRestoreBackupKeyActivityViewModel
import javax.inject.Inject

class RestoreBackupKeyActivity : DataBindingDaggerActivity(), IActivity, PassphraseRestoreBackupKeyFragment.OnFragmentInteractionListener, TextFileRestoreBackupKeyFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRestoreBackupKeyActivityViewModel>;

    private lateinit var binding: ActivityRestoreBackupKeyBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restore_backup_key);
        userId = intent.getStringExtra(USER_ID);
        binding.toolbar.setupSupportToolbar(this, R.string.restore)
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().getAuthDataAsMegolmBackupAuthDataResult().observe(this, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    supportFragmentManager.beginTransaction().replace(R.id.container, PassphraseRestoreBackupKeyFragment.newInstance(userId)).commitNow();
                } else if (it.status == Status.ERROR) {
                    supportFragmentManager.beginTransaction().replace(R.id.container, TextFileRestoreBackupKeyFragment.newInstance(userId)).commitNow();
                }
            }
        })
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onSwitchToUseFileBackup() {
        supportFragmentManager.beginTransaction().replace(R.id.container, TextFileRestoreBackupKeyFragment.newInstance(userId)).commitNow();
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
