package vmodev.clearkeep.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.ActivityCreateNewCallBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
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
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewCallActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: ActivityCreateNewCallBinding;
    private val listSelected = HashMap<String, User>();
    private lateinit var userId: String;
    private var currentRoomId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_create_new_call, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, listSelected = listSelected
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }, dataBindingComponent = dataBinding.getDataBindingComponent()) { user, status ->
            if (listSelected.size > 0) {
                binding.cardViewCreate.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.app_green, null));
                binding.cardViewCreate.isClickable = true;
            } else {
                binding.cardViewCreate.setCardBackgroundColor(ResourcesCompat.getColor(this.resources, R.color.button_disabled_text_color, null));
                binding.cardViewCreate.isClickable = false;
            }
        }

        binding.users = viewModelFactory.getViewModel().getUsersByQueryResult();
        binding.room = viewModelFactory.getViewModel().getCreateNewRoomResult();

        binding.textViewCreate.setOnClickListener {
            var name = "Call:"
            var topic = "";
            if (listSelected.size <= 1) {
                topic = "You are in 1:1 calling";
            } else {
                topic = "You are in conference calling";
            }
            listSelected.forEach {
                name += it.value.name + ",";
            }
            viewModelFactory.getViewModel().setCreateNewRoom(name, topic, "private");
        }

        binding.recyclerViewListUser.adapter = listUserAdapter;

        viewModelFactory.getViewModel().getUsersByQueryResult().observe(this, Observer {
            listUserAdapter.submitList(it?.data);
        });
        viewModelFactory.getViewModel().getCreateNewRoomResult().observe(this, Observer {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.textViewCreate.setText(R.string.creating);
                        binding.textViewCreate.isClickable = false;
                    }
                    Status.SUCCESS -> {
                        binding.textViewCreate.setText(R.string.create);
                        binding.textViewCreate.isClickable = true;
                        it.data?.let {
                            currentRoomId = it.id
                            gotoRoom(currentRoomId)
                        }
                    }
                    Status.ERROR -> {
                        binding.textViewCreate.setText(R.string.create);
                        binding.textViewCreate.isClickable = true;
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        })

        binding.lifecycleOwner = this;
        var disposable: Disposable? = null;
        binding.editTextQuery.textChanges().subscribe {
            disposable?.let { disposable -> disposable.dispose(); }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                    .mainThread()).subscribe { time: Long? ->
                viewModelFactory.getViewModel().setQuery(it.toString())
            };
        }
    }

    private fun gotoRoom(roomId: String) {
        val intentRoom = Intent(this.activity, RoomActivity::class.java);
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, application.getUserId());
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivity(intentRoom);
        findNavController().popBackStack(R.id.action_home,false)
    }

    override fun getFragment(): Fragment {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
