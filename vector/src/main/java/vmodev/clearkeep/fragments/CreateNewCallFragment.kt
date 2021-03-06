package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.ActivityCreateNewCallBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CreateNewCallFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewCallActivityViewModel>

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var binding: ActivityCreateNewCallBinding
    private val listSelected = HashMap<String, User>()
    private var currentRoomId: String = ""
    private var listUserSuggested: List<User>? = null
    var name = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_create_new_call, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, listSelected = listSelected
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id
            }
        }, dataBindingComponent = dataBinding.getDataBindingComponent()) { user, status ->
            if (listSelected.size > 0) {
                binding.btnCreate.isSelected = true
                binding.btnCreate.isEnabled = true
            } else {
                binding.btnCreate.isSelected = false
                binding.btnCreate.isEnabled = false
            }
        }

        binding.users = viewModelFactory.getViewModel().getUsers()
        binding.room = viewModelFactory.getViewModel().getCreateNewRoomResult()
        // cal create
        binding.btnCreate.setOnClickListener {
            name = "Call:"
            var topic = ""
            if (listSelected.size <= 1) {
                topic = "You are in 1:1 calling"
            } else {
                topic = "You are in conference calling"
            }
            listSelected.forEach {
                name += it.key + ","
            }
            viewModelFactory.getViewModel().setCreateNewRoom(name.dropLast(1), topic, "private")
        }

        binding.recyclerViewListUser.adapter = listUserAdapter
        binding.recyclerViewListUser.setHasFixedSize(true)
        binding.recyclerViewListUser.isNestedScrollingEnabled = false

        viewModelFactory.getViewModel().getListUserSuggested(1, application.getUserId()).observe(viewLifecycleOwner, Observer {
            listUserAdapter.submitList(it?.data)
            listUserSuggested = it?.data
        })
        viewModelFactory.getViewModel().getUsers().observe(viewLifecycleOwner, Observer {
            if (!TextUtils.isEmpty(binding.editTextQuery.text))
                listUserAdapter.submitList(it?.data)
        })
        viewModelFactory.getViewModel().getInviteUsersToRoomResult().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    it.data.let {
                        binding.room = viewModelFactory.getViewModel().joinRoomResult()
                        viewModelFactory.getViewModel().setJoinRoom(it?.id!!)
                    }
                }
            }
        })
        viewModelFactory.getViewModel().joinRoomResult().observe(viewLifecycleOwner, Observer {
            it.let {
                when (it.status) {
                    Status.ERROR -> {
                        Toast.makeText(this.activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        gotoRoom(currentRoomId)
                    }
                }
            }
        })
        viewModelFactory.getViewModel().getCreateNewRoomResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.btnCreate.setText(R.string.creating)
                        binding.btnCreate.isEnabled = false
                    }
                    Status.SUCCESS -> {
                        binding.btnCreate.setText(R.string.create)
                        binding.btnCreate.isEnabled = true
                        it.data?.let {
                            currentRoomId = it.id
                            val userIds: ArrayList<String> = ArrayList()
                            for ((key, value) in listSelected) {
                                userIds.add(key)
                            }
                            viewModelFactory.getViewModel().setJoinRoom(it.id)
                            viewModelFactory.getViewModel().setInviteUsersToRoom(it.id, userIds)
                        }
                    }
                    Status.ERROR -> {
                        binding.btnCreate.setText(R.string.create)
                        binding.btnCreate.isEnabled = true
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        binding.lifecycleOwner = this
        var disposable: Disposable? = null
        binding.editTextQuery.textChanges().subscribe {
            disposable?.let { disposable -> disposable.dispose() }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                    .mainThread()).subscribe { time: Long? ->
                if (it.length < 1 && TextUtils.isEmpty(it.toString())) {
                    listUserAdapter.submitList(listUserSuggested)
                } else {
                    viewModelFactory.getViewModel().setQuery(it.toString())
                }
            }
        }

        binding.editTextQuery.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun gotoRoom(roomId: String) {
        val intentRoom = Intent(this.activity, RoomActivity::class.java)
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, application.getUserId())
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId)
        startActivity(intentRoom)
        this.activity?.finish()
    }

    override fun getFragment(): Fragment {
        return this
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
