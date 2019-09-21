package vmodev.clearkeep.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import im.vector.R
import im.vector.databinding.DialogFragmentExportKeyResultBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ExportBackupKeyResultDialogFragment : DialogFragment() {

//    private val dataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentExportKeyResultBinding;

    private val buttonSaveToFile: PublishSubject<Int> = PublishSubject.create();
    private val buttonShare: PublishSubject<Int> = PublishSubject.create();
    private val buttonCopy: PublishSubject<Int> = PublishSubject.create();

    fun buttonShare(): Observable<Int> {
        return buttonShare;
    }

    fun buttonSaveToFile(): Observable<Int> {
        return buttonSaveToFile;
    }

    fun buttonCopy(): Observable<Int> {
        return buttonCopy;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_export_key_result, container, false);
        return binding.root;
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCancel.setOnClickListener { this.dismiss() }
        binding.buttonSaveToFile.setOnClickListener { buttonSaveToFile.onNext(1) }
        binding.buttonCopy.setOnClickListener { buttonCopy.onNext(1) }
        binding.buttonShare.setOnClickListener { buttonShare.onNext(1) }
    }

    override fun onDetach() {
        super.onDetach()
        buttonSaveToFile.onComplete();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DirectMessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ExportBackupKeyResultDialogFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}