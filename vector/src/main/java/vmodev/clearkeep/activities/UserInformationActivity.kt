package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import com.orhanobut.dialogplus.DialogPlus
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.ActivityUserInformationBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IUserInformationActivity
import vmodev.clearkeep.adapters.BottomDialogFavouriteRoomLongClick
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IUserInformationActivityViewModelFactory
import javax.inject.Inject
import javax.inject.Named

class UserInformationActivity : DataBindingDaggerActivity(), IUserInformationActivity {

    @Inject
    lateinit var viewModelFactory: IUserInformationActivityViewModelFactory;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listDirectChatRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomChatRoomAdapter: IListRoomRecyclerViewAdapter;

    private lateinit var binding: ActivityUserInformationBinding;
    private lateinit var userId: String;
    private lateinit var session: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_information, dataBindingComponent);
        userId = intent.getStringExtra(USER_ID);
        session = Matrix.getInstance(applicationContext).defaultSession;
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayShowTitleEnabled(false);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.user = viewModelFactory.getViewModel().getUserByIdResult();
        binding.directChatRooms = viewModelFactory.getViewModel().getDirectChatByUserIdResult();
        binding.roomChatRooms = viewModelFactory.getViewModel().getRoomChatByUserIdResult();
        setUpRecyclerView();
        viewModelFactory.getViewModel().getUserByIdResult().observe(this, Observer {
            it?.data?.let {
                if (it.status.compareTo(0) == 0) {
                    binding.textViewStatus.setText(R.string.offline);
                    binding.textViewStatus.setTextColor(resources.getColor(R.color.lb_grey))
                } else {
                    binding.textViewStatus.setText(R.string.online);
                    binding.textViewStatus.setTextColor(resources.getColor(R.color.app_green))
                }
            }
        })
        viewModelFactory.getViewModel().getCreateNewConversationResult().observe(this, Observer {
            it?.data?.let {
                val intentRoom = Intent(this, RoomActivity::class.java);
                intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, session.myUserId);
                intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                startActivity(intentRoom);
            }
        })
        binding.frameLayoutStartNewChat.setOnClickListener {
            binding.room = viewModelFactory.getViewModel().getCreateNewConversationResult();
            viewModelFactory.getViewModel().setUserIdForCreateNewConversation(userId);
        }
        binding.lifecycleOwner = this;

        viewModelFactory.getViewModel().setUserId(userId);
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewListDirectChat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerViewListRoomChat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listDirectChatRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listRoomChatRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listDirectChatRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> {
                    val intentRoom = Intent(this, RoomActivity::class.java);
                    intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, session.myUserId);
                    intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, room.id);
                    startActivity(intentRoom);
                }
            }
        }
        listDirectChatRoomAdapter.setOnItemLongClick {
            if (it.type == 129) {
                val bottomDialog = DialogPlus.newDialog(this)
                        .setAdapter(BottomDialogFavouriteRoomLongClick())
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                1 -> {
                                    binding.room = viewModelFactory.getViewModel().gerRemoveRoomFromFavouriteResult();
                                    viewModelFactory.getViewModel().setRoomIdForRemoveFromFavourite(it.id);
                                }
                                2 -> {
                                    val intent = Intent(this, RoomSettingsActivity::class.java);
                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
                                    startActivity(intent);
                                }
                                3 -> {
                                    binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
                                    viewModelFactory.getViewModel().setLeaveRoomId(it.id);
                                }
                            }
                            dialog?.dismiss();
                        }.create();
                bottomDialog.show();
            } else {
                val bottomDialog = DialogPlus.newDialog(this)
                        .setAdapter(BottomDialogRoomLongClick())
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                1 -> {
                                    binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
                                    viewModelFactory.getViewModel().setAddToFavouriteRoomId(it.id);
                                }
                                2 -> {
                                    val intent = Intent(this, RoomSettingsActivity::class.java);
                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
                                    startActivity(intent);
                                }
                                3 -> {
                                    binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
                                    viewModelFactory.getViewModel().setLeaveRoomId(it.id);
                                }
                            }

                            dialog?.dismiss();
                        }.create();
                bottomDialog.show();
            }
        }
        listRoomChatRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> {
                    val intentRoom = Intent(this, RoomActivity::class.java);
                    intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, session.myUserId);
                    intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, room.id);
                    startActivity(intentRoom);
                }
            }
        }
        listRoomChatRoomAdapter.setOnItemLongClick {
            if (it.type == 130) {
                val bottomDialog = DialogPlus.newDialog(this)
                        .setAdapter(BottomDialogFavouriteRoomLongClick())
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                1 -> {
                                    binding.room = viewModelFactory.getViewModel().gerRemoveRoomFromFavouriteResult();
                                    viewModelFactory.getViewModel().setRoomIdForRemoveFromFavourite(it.id);
                                }
                                2 -> {
                                    val intent = Intent(this, RoomSettingsActivity::class.java);
                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
                                    startActivity(intent);
                                }
                                3 -> {
                                    binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
                                    viewModelFactory.getViewModel().setLeaveRoomId(it.id);
                                }
                            }
                            dialog?.dismiss();
                        }.create();
                bottomDialog.show();
            } else {
                val bottomDialog = DialogPlus.newDialog(this)
                        .setAdapter(BottomDialogRoomLongClick())
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                1 -> {
                                    binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
                                    viewModelFactory.getViewModel().setAddToFavouriteRoomId(it.id);
                                }
                                2 -> {
                                    val intent = Intent(this, RoomSettingsActivity::class.java);
                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
                                    startActivity(intent);
                                }
                                3 -> {
                                    binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
                                    viewModelFactory.getViewModel().setLeaveRoomId(it.id);
                                }
                            }

                            dialog?.dismiss();
                        }.create();
                bottomDialog.show();
            }
        }
        binding.recyclerViewListDirectChat.adapter = listDirectChatRoomAdapter.getAdapter();
        binding.recyclerViewListRoomChat.adapter = listRoomChatRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getDirectChatByUserIdResult().observe(this, Observer {
            listDirectChatRoomAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getRoomChatByUserIdResult().observe(this, Observer {
            listRoomChatRoomAdapter.getAdapter().submitList(it?.data);
        });
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val USER_ID = "USER_ID";
    }
}
