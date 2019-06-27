package vmodev.clearkeep.dialogfragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.DialogFragmentProgressBinding
import vmodev.clearkeep.binding.FragmentDataBindingComponent

class ProgressDialogFragment : DialogFragment() {

    private val dataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: DialogFragmentProgressBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_progress, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.progress = 0;
    }

    public fun setProgress(value: Int) {
        binding.progressBar.progress = value;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ProgressDialogFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}