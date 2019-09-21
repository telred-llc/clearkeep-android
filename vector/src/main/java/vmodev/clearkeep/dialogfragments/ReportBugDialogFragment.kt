package vmodev.clearkeep.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import im.vector.R
import im.vector.databinding.DialogFragmentReportBugBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReportBugDialogFragment : DialogFragment() {

//    private val dataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentReportBugBinding;
    private val onSendClick: PublishSubject<SendBugObject> = PublishSubject.create();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_report_bug, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewCancel.setOnClickListener {
            onSendClick.onComplete();
            dismiss()
        }
        binding.textViewOk.setOnClickListener {
            onSendClick.onNext(SendBugObject(binding.checkboxSendLogs.isChecked, binding.editTextContent.text.toString()));
            onSendClick.onComplete();
            dismiss();
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public fun getOnSendClick(): Observable<SendBugObject> {
        return onSendClick;
    }

    data class SendBugObject(val isSendLogs: Boolean, val bugContent: String)

    companion object {
        @JvmStatic
        fun newInstance() =
                ReportBugDialogFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}