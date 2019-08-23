package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import im.vector.R
import im.vector.databinding.ActivityCreateNewRoomBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject

class CreateNewRoomActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewRoomActivityViewModel>;

    private lateinit var binding: ActivityCreateNewRoomBinding;

    @SuppressLint("CheckResult", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_room, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.new_room);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }

        binding.lifecycleOwner = this;
        binding.room = viewModelFactory.getViewModel().createNewRoomResult();
        viewModelFactory.getViewModel().createNewRoomResult().observe(this, Observer { t ->
            t?.data?.let { room ->
                val intent = Intent(this, InviteUsersToRoomActivity::class.java);
                intent.putExtra("ROOM_ID", room.id);
                startActivity(intent);
                finish();
            }
        })
        RxTextView.textChanges(binding.editTextRoomName).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: CharSequence? ->
                    if (!t.toString().isNullOrEmpty()) {
                        binding.textViewRightToolbar.setTextColor(Color.parseColor("#63CD9A"))
                        binding.textViewRightToolbar.isClickable = true;
                    } else {
                        binding.textViewRightToolbar.setTextColor(Color.parseColor("#B8BDC7"))
                        binding.textViewRightToolbar.isClickable = false;
                    }

                }
        binding.textViewRightToolbar.setOnClickListener { v ->
            if (binding.editTextRoomTopic.text.isNullOrEmpty()) binding.editTextRoomTopic.text = binding.editTextRoomName.text;
            viewModelFactory.getViewModel().setCreateNewRoom(binding.editTextRoomName.text.toString(), binding.editTextRoomTopic.text.toString(), if (binding.switchRoomVisibility.isChecked) "public" else "private")
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
