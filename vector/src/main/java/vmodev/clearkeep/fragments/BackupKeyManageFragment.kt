package vmodev.clearkeep.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil

import im.vector.R
import im.vector.databinding.FragmentBackupKeyManageBinding
import vmodev.clearkeep.activities.RestoreBackupKeyActivity
import vmodev.clearkeep.adapters.ListSignatureRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IBackupKeyManageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Signature
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BackupKeyManageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BackupKeyManageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BackupKeyManageFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractBackupKeyManageFragmentViewModel>
    @Inject
    lateinit var appExecutors: AppExecutors;

    // TODO: Rename and change types of parameters
    private lateinit var userId: String;
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentBackupKeyManageBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_backup_key_manage, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterListSignature = ListSignatureRecyclerViewAdapter(appExecutors, dataBinding.getDataBindingComponent(),object : DiffUtil.ItemCallback<Signature>() {
            override fun areItemsTheSame(p0: Signature, p1: Signature): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: Signature, p1: Signature): Boolean {
                return p0.description == p1.description && p0.status == p1.status;
            }
        });
        binding.recyclerViewListSignature.adapter = adapterListSignature;
        binding.keyBackup = viewModelFactory.getViewModel().getKeyBackupResult();
        viewModelFactory.getViewModel().getSignatureListResult().observe(this, Observer {
            adapterListSignature.submitList(it?.data)
        });
        viewModelFactory.getViewModel().getDeleteKeyBackupResult().observe(this, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    Toast.makeText(this.context, R.string.delete_success, Toast.LENGTH_LONG).show();
                } else if (it.status == Status.ERROR) {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show();
                }
            }
        })

        binding.buttonRestore.setOnClickListener {
            val intentRestore = Intent(this.activity, RestoreBackupKeyActivity::class.java);
            intentRestore.putExtra(RestoreBackupKeyActivity.USER_ID, userId);
            startActivity(intentRestore);
        }
        binding.buttonDelete.setOnClickListener {
            activity?.let {
                AlertDialog.Builder(it)
                        .setTitle(R.string.keys_backup_settings_delete_confirm_title)
                        .setMessage(R.string.keys_backup_settings_delete_confirm_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.keys_backup_settings_delete_confirm_title) { _, _ ->
                            binding.keyBackup = viewModelFactory.getViewModel().getDeleteKeyBackupResult();
                            viewModelFactory.getViewModel().setIdForDeleteKeyBackup(userId);
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .setCancelable(true)
                        .show()
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner;

        viewModelFactory.getViewModel().setIdForGetKeyBackup(userId);
        viewModelFactory.getViewModel().setIdForGetSignature(userId);
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BackupKeyManageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                BackupKeyManageFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }
}
