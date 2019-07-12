package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_push_backup_key, dataBindingComponent);
        binding.toolbar.setupSupportToolbar(this, R.string.backup)
        binding.backup = viewModelFactory.getViewModel().getBackupKeyResult();
        viewModelFactory.getViewModel().getBackupKeyResult().observe(this, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    Toast.makeText(this, "Backup Success with key: " + it.data, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (it.status == Status.ERROR) {
                    Toast.makeText(this, "Backup Error: " + it.message, Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.lifecycleOwner = this;
        binding.buttonBackup.setOnClickListener {
            viewModelFactory.getViewModel().setPassphrase(binding.editTextPassphrase.text.toString());
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
