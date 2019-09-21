package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.R
import im.vector.databinding.ActivityCreateNewRoomBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject

class CreateNewRoomActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractCreateNewRoomActivityViewModel>;

    private lateinit var binding: ActivityCreateNewRoomBinding;

    @SuppressLint("CheckResult", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_room, dataBinding.getDataBindingComponent());
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
            t?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.textViewRightToolbar.setText(R.string.creating);
                        binding.textViewRightToolbar.isClickable = false;
                    }
                    Status.SUCCESS -> {
                        binding.textViewRightToolbar.setText(R.string.create);
                        binding.textViewRightToolbar.isClickable = true;
                        it.data?.let {
                            val intent = Intent(this, InviteUsersToRoomActivity::class.java);
                            intent.putExtra("ROOM_ID", it.id);
                            startActivity(intent);
                            finish();
                        }
                    }
                    Status.ERROR -> {
                        binding.textViewRightToolbar.setText(R.string.create);
                        binding.textViewRightToolbar.isClickable = true;
                        Toast.makeText(this@CreateNewRoomActivity, it.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        })
        binding.editTextRoomName.textChanges().subscribe {
            if (!it.toString().isNullOrEmpty()) {
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
