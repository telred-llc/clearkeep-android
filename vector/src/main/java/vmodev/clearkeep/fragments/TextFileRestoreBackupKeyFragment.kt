package vmodev.clearkeep.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import im.vector.R
import im.vector.databinding.FragmentTextFileRestoreBackupKeyBinding
import im.vector.fragments.keysbackup.restore.KeysBackupRestoreFromKeyFragment
import im.vector.util.startImportTextFromFileIntent
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractTextFileRestoreBackupKeyFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TextFileRestoreBackupKeyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TextFileRestoreBackupKeyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TextFileRestoreBackupKeyFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractTextFileRestoreBackupKeyFragmentViewModel>

    // TODO: Rename and change types of parameters
    private lateinit var userId: String;
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentTextFileRestoreBackupKeyBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text_file_restore_backup_key, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.result = viewModelFactory.getViewModel().getRestoreResult();
        binding.buttonCancel.setOnClickListener {
            activity?.finish();
        }
        binding.buttonRestore.setOnClickListener {
            viewModelFactory.getViewModel().setRecoveryKey(binding.editTextContent.text.toString());
        }
        binding.imageViewSelectFromFile.setOnClickListener {
            startImportTextFromFileIntent(this, REQUEST_TEXT_FILE_GET)
        }
        viewModelFactory.getViewModel().getRestoreResult().observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                Toast.makeText(this.context, "Success restore " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " total number keys", Toast.LENGTH_LONG).show();
                activity?.finish();
            }
        });
        binding.lifecycleOwner = viewLifecycleOwner;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TEXT_FILE_GET && resultCode == Activity.RESULT_OK) {
            val dataURI = data?.data
            if (dataURI != null) {
                try {
                    activity
                            ?.contentResolver
                            ?.openInputStream(dataURI)
                            ?.bufferedReader()
                            ?.use { it.readText() }
                            ?.let {
                                binding.editTextContent.setText(it)
                                binding.editTextContent.setSelection(it.length)
                            }
                } catch (e: Exception) {
                    Log.e(KeysBackupRestoreFromKeyFragment::javaClass.name, "Failed to read recovery kay from text", e)
                }
            }
            return
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
         * @return A new instance of fragment TextFileRestoreBackupKeyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                TextFileRestoreBackupKeyFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }

        const val REQUEST_TEXT_FILE_GET = 12536;
    }
}
