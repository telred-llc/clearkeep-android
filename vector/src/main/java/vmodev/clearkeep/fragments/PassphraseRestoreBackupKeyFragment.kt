package vmodev.clearkeep.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import im.vector.R
import im.vector.databinding.FragmentPassphraseRestoreBackupKeyBinding
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractPassphraseRestoreBackupKeyFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PassphraseRestoreBackupKeyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PassphraseRestoreBackupKeyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PassphraseRestoreBackupKeyFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractPassphraseRestoreBackupKeyFragmentViewModel>

    // TODO: Rename and change types of parameters
    private lateinit var userId: String
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentPassphraseRestoreBackupKeyBinding
    private var restoreStatus = false
    private val USER_ID = "USER_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            userId = it.getString(USER_ID);
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_passphrase_restore_backup_key, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.result = viewModelFactory.getViewModel().getPassphraseRestoreResult()
//        binding.buttonUseBackupFile.setOnClickListener {
//            onSwitchToUseFileBackup();
//        }
        binding.buttonRestore.setOnClickListener {
            if (restoreStatus)
                return@setOnClickListener
            restoreStatus = true
            binding.buttonRestore.setText(R.string.resotring)
            viewModelFactory.getViewModel().setPassphraseForRestore(binding.editTextPassphrase.text.toString())
        }
        viewModelFactory.getViewModel().getPassphraseRestoreResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    it.data?.let {
                        restoreStatus = false
                        binding.buttonRestore.setText(R.string.restore)
                        Toast.makeText(this.context, "Success restore " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " total number keys", Toast.LENGTH_LONG).show()
                        activity?.setResult(-1)
                        activity?.finish()
                    }
                }
                if (it.status == Status.ERROR) {
                    it.message?.let {
                        Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
                        restoreStatus = false
                        binding.buttonRestore.setText(R.string.restore)
                        binding.editTextPassphrase.setText("")
                    }
                }
            }
        })
        binding.lifecycleOwner = viewLifecycleOwner
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onSwitchToUseFileBackup() {
        listener?.onSwitchToUseFileBackup()
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

    override fun getFragment(): Fragment {
        return this
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
        fun onSwitchToUseFileBackup()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
         * @return A new instance of fragment PassphraseRestoreBackupKeyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                PassphraseRestoreBackupKeyFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }
}
