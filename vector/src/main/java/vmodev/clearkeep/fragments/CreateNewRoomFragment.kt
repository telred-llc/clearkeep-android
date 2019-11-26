package vmodev.clearkeep.fragments

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityCreateNewRoomBinding
import im.vector.extensions.hideKeyboard
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreateNewRoomFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewRoomActivityViewModel>
    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var binding: ActivityCreateNewRoomBinding
    private val listSelected = HashMap<String, User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_create_new_room, container, false, dataBinding.getDataBindingComponent())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.room = viewModelFactory.getViewModel().createNewRoomResult()
        binding.rvListUserSuggested.setHasFixedSize(true)
        binding.rvListUserSuggested.isNestedScrollingEnabled = false
        viewModelFactory.getViewModel().createNewRoomResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.btnCreate.setText(R.string.creating)
                        binding.btnCreate.isEnabled = false
                        binding.btnCreate.isSelected = false
                    }
                    Status.SUCCESS -> {
                        binding.btnCreate.setText(R.string.create)
                        binding.btnCreate.isEnabled = true
                        binding.btnCreate.isSelected = true
                        it.data?.let {
                            val listKeySelected: List<String> = ArrayList(listSelected.keys)
                            findNavController().navigate(CreateNewRoomFragmentDirections.inviteUsersToRoom().setListUser(listKeySelected.toTypedArray()).setRoomId(it.id))
                        }
                    }
                    Status.ERROR -> {
                        binding.btnCreate.setText(R.string.create)
                        binding.btnCreate.isEnabled = true
                        binding.btnCreate.isSelected = true
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, listSelected = listSelected
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }
        }, dataBindingComponent = dataBinding.getDataBindingComponent()) { user, status ->
        }
        viewModelFactory.getViewModel().getListUserSuggested(1, application.getUserId()).observe(viewLifecycleOwner, Observer {
            it?.let {
                listUserAdapter.submitList(it.data)
            }
        })
        binding.rvListUserSuggested.adapter = listUserAdapter
        binding.editTextRoomName.textChanges().subscribe {
            if (!TextUtils.isEmpty(it.toString().trim())) {
                binding.btnCreate.isEnabled = true
                binding.btnCreate.isSelected = true
            } else {
                binding.btnCreate.isEnabled = false
                binding.btnCreate.isSelected = false
            }
        }
        binding.btnCreate.setOnClickListener { v ->
            this.onCreate()
        }
        binding.editTextRoomTopic.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            return@setOnEditorActionListener false

        }
    }

    private fun onCreate() {
        var titleTopic = binding.editTextRoomTopic.text.toString()
        if (TextUtils.isEmpty(titleTopic)) {
            binding.editTextRoomTopic.text = binding.editTextRoomName.text
            titleTopic = binding.editTextRoomName.text.toString()
        }
        viewModelFactory.getViewModel().setCreateNewRoom(binding.editTextRoomName.text.toString().toUpperCase().trim(), titleTopic, if (binding.checkboxRoomVisibility.isChecked) "public" else "private")
    }

    override fun getFragment(): Fragment {
        return this
    }
}
