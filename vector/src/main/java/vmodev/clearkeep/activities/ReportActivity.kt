package vmodev.clearkeep.activities

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityReportBinding
import im.vector.util.BugReporter
import vmodev.clearkeep.activities.interfaces.IReportActivity
import vmodev.clearkeep.dialogfragments.ProgressDialogFragment
import vmodev.clearkeep.dialogfragments.ReportBugDialogFragment
import vmodev.clearkeep.factories.viewmodels.interfaces.IReportActivityViewModelFactory
import javax.inject.Inject

class ReportActivity : DataBindingDaggerActivity(), IReportActivity {

    @Inject
    lateinit var viewModelFactory: IReportActivityViewModelFactory;

    private lateinit var binding: ActivityReportBinding;
    private lateinit var userId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        userId = intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.report);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.deviceSettings = viewModelFactory.getViewModel().getMergeGetDeiveSettingsResult();
//        viewModelFactory.getViewModel().getSendAnonCrashAndUsageDataResult().observe(this, Observer {
//            it?.data?.let { binding.switchCompatSendCrash.isChecked = it.sendAnonCrashAndUsageData.compareTo(0) != 0 }
//        })
//        viewModelFactory.getViewModel().getRageShakeToReportBug().observe(this, Observer {
//            it?.data?.let {
//                binding.switchCompatRageShakeToReportBug.isChecked = it.rageShakeToReportBug.compareTo(0) != 0
//            }
//        })

        viewModelFactory.getViewModel().getMergeGetDeiveSettingsResult().observe(this, Observer {
            it?.data?.let {
                binding.switchCompatSendCrash.isChecked = it.sendAnonCrashAndUsageData.compareTo(0) != 0
                binding.switchCompatRageShakeToReportBug.isChecked = it.rageShakeToReportBug.compareTo(0) != 0
            }
        })

        binding.textViewReportBug.setOnClickListener {
            val reportBugDialogFragment = ReportBugDialogFragment.newInstance();
            reportBugDialogFragment.getOnSendClick().subscribe {
                val progressDialogFragment = ProgressDialogFragment.newInstance(resources.getString(R.string.report_bug), resources.getString(R.string.uploading_report));
                progressDialogFragment.show(supportFragmentManager, "");
                BugReporter.sendBugReport(this, it.isSendLogs, it.isSendLogs, false, it.bugContent, object : BugReporter.IMXBugReportListener {
                    override fun onUploadCancelled() {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportActivity, R.string.cancelled, Toast.LENGTH_LONG).show();
                    }

                    override fun onUploadFailed(reason: String?) {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportActivity, reason, Toast.LENGTH_LONG).show();
                    }

                    override fun onProgress(progress: Int) {
                        if (progress > 0) {
                            progressDialogFragment.setProgress(progress);
                        }
                    }

                    override fun onUploadSucceed() {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportActivity, R.string.successed, Toast.LENGTH_LONG).show();
                    }
                });
            }
            reportBugDialogFragment.show(supportFragmentManager, "");
        }
        binding.switchCompatRageShakeToReportBug.setOnCheckedChangeListener { compoundButton, b -> viewModelFactory.getViewModel().setRageShakeToReportBug(userId, if (b) 1 else 0) }
        binding.switchCompatSendCrash.setOnCheckedChangeListener { compoundButton, b -> viewModelFactory.getViewModel().setSendAnonCrashAndUsageData(userId, if (b) 1 else 0) }
        viewModelFactory.getViewModel().setIdForGetDeviceSettings(userId);
        binding.lifecycleOwner = this;
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
