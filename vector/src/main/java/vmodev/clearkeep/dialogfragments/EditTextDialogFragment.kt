package vmodev.clearkeep.dialogfragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import im.vector.R
import im.vector.databinding.DialogFragmentEditTextBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

private const val TITLE = "TITLE"
private const val CONTENT = "CONTENT"
private const val EDIT_TEXT_HINT = "EDIT_TEXT_HINT"
private const val EDIT_TEXT_INPUT_TYPE = "EDIT_TEXT_INPUT_TYPE";
private const val OK_TITLE = "OK_TITLE";
private const val CANCEL_TITLE = "CANCEL_TITLE";

class EditTextDialogFragment : DialogFragment() {

//    private val dataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentEditTextBinding;
    private var title: String = "";
    private var content: String = "";
    private var editTextHint: String = "";
    private var editTextInputType: Int = InputType.TYPE_CLASS_TEXT;
    private var okTitle: String = "";
    private var cancelTitle: String = "";
    private val okOnClick: PublishSubject<String> = PublishSubject.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE, "")
            content = it.getString(CONTENT, "")
            editTextHint = it.getString(EDIT_TEXT_HINT, "");
            editTextInputType = it.getInt(EDIT_TEXT_INPUT_TYPE);
            okTitle = it.getString(OK_TITLE, "");
            cancelTitle = it.getString(CANCEL_TITLE, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_edit_text, container, false);
        binding.textViewTitle.text = title;
        binding.textViewContent.text = content;
        binding.editTextContent.hint = editTextHint;
        binding.editTextContent.inputType = editTextInputType;
        binding.textViewOk.text = okTitle;
        binding.textViewCancel.text = cancelTitle;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewCancel.setOnClickListener {
            okOnClick.onComplete();
            this.dismiss();
        }
        binding.textViewOk.setOnClickListener {
            okOnClick.onNext(binding.editTextContent.text.toString())
            okOnClick.onComplete();
            this.dismiss();
        }
    }

    public fun setTitle(title: String) {
        binding.textViewTitle.text = title;
    }

    public fun setContent(content: String) {
        binding.textViewContent.text = content;
    }

    public fun setEditTextHint(hint: String) {
        binding.editTextContent.hint = hint;
    }

    public fun getOnClickOk(): Observable<String> {
        return okOnClick;
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String = "", content: String = "", editTextHint: String = "", editTextInputType: Int = InputType.TYPE_CLASS_TEXT
                        , okTitle: String = "", cancelTitle: String = "") =
                EditTextDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title);
                        putString(CONTENT, content);
                        putString(EDIT_TEXT_HINT, editTextHint);
                        putInt(EDIT_TEXT_INPUT_TYPE, editTextInputType);
                        putString(OK_TITLE, okTitle);
                        putString(CANCEL_TITLE, cancelTitle);
                    }
                }
    }
}