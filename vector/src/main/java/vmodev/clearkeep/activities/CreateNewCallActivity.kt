package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.ActivityCreateNewCallBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.adapters.ListUserToInviteRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CreateNewCallActivity : DataBindingDaggerActivity(), ICreateNewCallActivity {

    @Inject
    lateinit var viewModelFactory: ICreateNewCallActivityViewModelFactory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityCreateNewCallBinding;
    private val listSelected = HashMap<String, User>();
    private lateinit var userId: String;
    private var currentRoomId: String = ""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_call, dataBindingComponent);
        userId = intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.new_call);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        val listUserAdapter = ListUserToInviteRecyclerViewAdapter(appExecutors = appExecutors, dataBindingComponent = dataBindingComponent, listSelected = listSelected
                , diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }) { user, status ->
            if (listSelected.size > 0) {
                binding.textViewCreate.setTextColor(Color.parseColor("#63CD9A"))
                binding.textViewCreate.isClickable = true;
            } else {
                binding.textViewCreate.setTextColor(Color.parseColor("#B8BDC7"))
                binding.textViewCreate.isClickable = false;
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
                        Toast.makeText(this@CreateNewCallActivity, it.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        })

        binding.lifecycleOwner = this;
        var disposable: Disposable? = null;
        RxTextView.textChanges(binding.editTextQuery).subscribe { t: CharSequence? ->
            disposable?.let { disposable -> disposable.dispose(); }
            disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers
                    .mainThread()).subscribe { time: Long? ->
                run {
                    t?.let { charSequence ->
                        viewModelFactory.getViewModel().setQuery(charSequence.toString())
                    }
                }
            };
        }
    }

    private fun gotoRoom(roomId: String) {
        val intentRoom = Intent(this, RoomActivity::class.java);
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivity(intentRoom);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
