package vmodev.clearkeep.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import im.vector.R
import im.vector.databinding.DialogFragmentProgressBinding

private const val TITLE = "TITLE";
private const val CONTENT = "CONTENT";

class ProgressDialogFragment : DialogFragment() {

//    private val dataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentProgressBinding;
    private var title: String = "";
    private var content: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE, "");
            content = it.getString(CONTENT, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_progress, container, false);
        binding.textViewTitle.text = title;
        binding.textViewContent.text = content;
        return binding.root;
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.progress = 0;
        binding.textViewCancel.setOnClickListener { dismiss() }
    }

    fun setProgress(value: Int) {
        binding.progressBar.progress = value;
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, content: String) =
                ProgressDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title);
                        putString(CONTENT, content);
                    }
                }
    }
}