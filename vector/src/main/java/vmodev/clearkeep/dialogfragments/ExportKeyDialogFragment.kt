package vmodev.clearkeep.dialogfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import im.vector.R
import im.vector.databinding.DialogFragmentExportKeyBinding

public class ExportKeyDialogFragment : DialogFragment() {
    private var listener: OnFragmentInteractionListener? = null;
//    private val dataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentExportKeyBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_export_key, container, false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCancel.setOnClickListener {
            this.dismiss();
        }
        binding.buttonExport.setOnClickListener {
            if (binding.editTextCreatePassphrase.text.toString().compareTo(binding.editTextConfirmPassphrase.text.toString()) == 0) {
                onExportButtonClick(binding.editTextCreatePassphrase.text.toString());
                this.dismiss();
            } else {
                binding.editTextConfirmPassphrase.setText("");
                Toast.makeText(this.context, R.string.confirm_passphrase_mismatch, Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onStart() {
        super.onStart()
        this.dialog?.window?.setLayout(-1, -2);
    }

    private fun onExportButtonClick(passphrase: String) {
        listener?.let {
            it.onExportButtonClick(passphrase);
        }
    }

    private fun onCancalButtonClick() {
        listener?.let {
            it.onCancelButtonClick();
        }
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
                ExportKeyDialogFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onExportButtonClick(passphrase: String);

        fun onCancelButtonClick();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}