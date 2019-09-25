package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.FragmentReportBinding
import im.vector.util.BugReporter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.dialogfragments.ProgressDialogFragment
import vmodev.clearkeep.dialogfragments.ReportBugDialogFragment
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractReportActivityViewModel
import javax.inject.Inject

class ReportFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractReportActivityViewModel>;
    @Inject
    lateinit var application : IApplication;

    private lateinit var binding: FragmentReportBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deviceSettings = viewModelFactory.getViewModel().getMergeGetDeiveSettingsResult();

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
                progressDialogFragment.show(childFragmentManager, "");
                BugReporter.sendBugReport(this.context, it.isSendLogs, it.isSendLogs, false, it.bugContent, object : BugReporter.IMXBugReportListener {
                    override fun onUploadCancelled() {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportFragment.context, R.string.cancelled, Toast.LENGTH_LONG).show();
                    }

                    override fun onUploadFailed(reason: String?) {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportFragment.context, reason, Toast.LENGTH_LONG).show();
                    }

                    override fun onProgress(progress: Int) {
                        if (progress > 0) {
                            progressDialogFragment.setProgress(progress);
                        }
                    }

                    override fun onUploadSucceed() {
                        progressDialogFragment.dismiss();
                        Toast.makeText(this@ReportFragment.context, R.string.successed, Toast.LENGTH_LONG).show();
                    }
                });
            }
            reportBugDialogFragment.show(childFragmentManager, "");
        }
        binding.switchCompatRageShakeToReportBug.setOnCheckedChangeListener { compoundButton, b -> viewModelFactory.getViewModel().setRageShakeToReportBug(application.getUserId(), if (b) 1 else 0) }
        binding.switchCompatSendCrash.setOnCheckedChangeListener { compoundButton, b -> viewModelFactory.getViewModel().setSendAnonCrashAndUsageData(application.getUserId(), if (b) 1 else 0) }
        viewModelFactory.getViewModel().setIdForGetDeviceSettings(application.getUserId());
        binding.lifecycleOwner = this;
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
