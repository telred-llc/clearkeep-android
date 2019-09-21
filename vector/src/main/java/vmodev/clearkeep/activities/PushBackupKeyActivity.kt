package vmodev.clearkeep.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityPushBackupKeyBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.ultis.setupSupportToolbar
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractPushBackupKeyActivityViewModel
import javax.inject.Inject

class PushBackupKeyActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractPushBackupKeyActivityViewModel>;

    private lateinit var binding: ActivityPushBackupKeyBinding;
    private var backupStatus: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_push_backup_key);
        binding.toolbar.setupSupportToolbar(this, R.string.backup)
        binding.backup = viewModelFactory.getViewModel().getBackupKeyResult();
        viewModelFactory.getViewModel().getBackupKeyResult().observe(this, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    binding.buttonBackup.setText(R.string.start_backup);
                    backupStatus = false;
                    Toast.makeText(this, "Backup Success with key: " + it.data, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK)
                    finish();
                } else if (it.status == Status.ERROR) {
                    binding.buttonBackup.setText(R.string.start_backup);
                    backupStatus = false;
                    Toast.makeText(this, "Backup Error: " + it.message, Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.lifecycleOwner = this;
        binding.buttonBackup.setOnClickListener {
            if (backupStatus)
                return@setOnClickListener;
            if (binding.editTextPassphrase.text.toString().compareTo(binding.editTextConfirmPassphrase.text.toString()) != 0) {
                Toast.makeText(this, R.string.passphrase_passphrase_does_not_match, Toast.LENGTH_LONG).show();
                binding.editTextConfirmPassphrase.setText("");
                return@setOnClickListener;
            }
            backupStatus = true;
            binding.buttonBackup.setText(R.string.backing_up)
            viewModelFactory.getViewModel().setPassphrase(binding.editTextPassphrase.text.toString());
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
