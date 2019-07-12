package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
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
        supportFragmentManager.beginTransaction().replace(R.id.container, BackupKeyManageFragment.newInstance(userId)).commitNow();
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
