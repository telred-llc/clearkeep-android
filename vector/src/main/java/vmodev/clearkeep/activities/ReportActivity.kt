package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import im.vector.R
import im.vector.databinding.ActivityReportBinding
import im.vector.util.BugReporter
import vmodev.clearkeep.activities.interfaces.IReportActivity
import vmodev.clearkeep.dialogfragments.ProgressDialogFragment
import vmodev.clearkeep.dialogfragments.ReportBugDialogFragment

class ReportActivity : DataBindingDaggerActivity(), IReportActivity {

    private lateinit var binding: ActivityReportBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.report);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }

        binding.textViewReportBug.setOnClickListener {
            val reportBugDialogFragment = ReportBugDialogFragment.newInstance();
            reportBugDialogFragment.getOnSendClick().subscribe {
                val progressDialogFragment = ProgressDialogFragment.newInstance();
                progressDialogFragment.show(supportFragmentManager, "");
                BugReporter.sendBugReport(this, it.isSendLogs, it.isSendLogs, false, it.bugContent, object : BugReporter.IMXBugReportListener {
                    override fun onUploadCancelled() {
                        progressDialogFragment.dismiss();
                    }

                    override fun onUploadFailed(reason: String?) {
                        progressDialogFragment.dismiss();
                    }

                    override fun onProgress(progress: Int) {
                        if (progress > 0) {
                            progressDialogFragment.setProgress(progress);
                        }
                    }

                    override fun onUploadSucceed() {
                        progressDialogFragment.dismiss();
                    }
                });
            }
            reportBugDialogFragment.show(supportFragmentManager, "");
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
