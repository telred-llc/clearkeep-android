package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.widget.textChanges
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.FragmentRoomMemberListBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.matrix.androidsdk.core.Debug
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.IMXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.rest.model.bingrules.BingRule
import org.matrix.androidsdk.rest.model.sync.AccountDataElement
import vmodev.clearkeep.activities.ProfileActivity
import vmodev.clearkeep.activities.ViewUserProfileActivity
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapterCustom
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RoomMemberListFragment : DataBindingDaggerFragment(), IFragment, IMXEventListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var binding: FragmentRoomMemberListBinding
    private lateinit var roomViewModel: AbstractRoomViewModel
    private lateinit var listUserJoinAdapter: ListUserRecyclerViewAdapterCustom
    private lateinit var listUserInvitesAdapter: ListUserRecyclerViewAdapterCustom
    private val args: RoomMemberListFragmentArgs by navArgs()
    var mRoom: Room? = null
    private var listUserSuggested: List<RoomMember>? = null
    var callBackMembersAsync: ApiCallback<List<RoomMember>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_member_list, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextQuery.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
            }
            return@setOnEditorActionListener false
        }

        args.roomId?.let { roomID ->
            val mSession = Matrix.getInstance(activity).defaultSession
            mRoom = mSession.dataHandler.getRoom(roomID)
            mRoom?.addEventListener(this)
            roomViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractRoomViewModel::class.java)
            roomViewModel.setGetUserFromRoomId(roomID)
            binding.lifecycleOwner = viewLifecycleOwner
            callBackMembersAsync = object : ApiCallback<List<RoomMember>> {
                override fun onSuccess(mDataRoomMember: List<RoomMember>?) {
                    try {
                        listUserSuggested = mDataRoomMember
                        initJoins(roomID, listUserSuggested?.filterIndexed { index, roomMember -> RoomMember.MEMBERSHIP_JOIN.equals(roomMember.membership) })
                        initInvites(roomID, listUserSuggested?.filterIndexed { index, roomMember -> RoomMember.MEMBERSHIP_INVITE.equals(roomMember.membership) })
                    } catch (e: Exception) {

                    }
                }

                override fun onUnexpectedError(p0: Exception?) {
                    Log.e("Tag", "Error: ${p0?.message}")
                }

                override fun onMatrixError(p0: MatrixError?) {
                    Log.e("Tag", "Error: ${p0?.message}")
                }

                override fun onNetworkError(p0: Exception?) {
                    Log.e("Tag", "Error: ${p0?.message}")
                }

            }
            mRoom?.getActiveMembersAsync(callBackMembersAsync!!)
            binding.editTextQuery.textChanges().subscribe({
                val disposable = Observable.timer(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribe { time: Long? ->
                            if (TextUtils.isEmpty(it.toString())) {
                                listUserJoinAdapter.submitList(listUserSuggested?.filterIndexed { index, roomMember -> RoomMember.MEMBERSHIP_JOIN.equals(roomMember.membership) })
                                listUserInvitesAdapter.submitList(listUserSuggested?.filterIndexed { index, roomMember -> RoomMember.MEMBERSHIP_INVITE.equals(roomMember.membership) })
                            } else {
                                listUserJoinAdapter.submitList(listUserSuggested?.filterIndexed { index, roomMember -> (roomMember.name.contains(it.toString(), true) && RoomMember.MEMBERSHIP_JOIN.equals(roomMember.membership)) })
                                listUserInvitesAdapter.submitList(listUserSuggested?.filterIndexed { index, roomMember -> (roomMember.name.contains(it.toString(), true) && RoomMember.MEMBERSHIP_INVITE.equals(roomMember.membership)) })
                            }
                        }
                compositeDisposable.add(disposable)
            }, {
                Log.e("Tag", "Error: ${it.message}")
            })
        }
    }

    private fun initJoins(roomID: String?, mDataJoin: List<RoomMember>?) {
        listUserJoinAdapter = ListUserRecyclerViewAdapterCustom(activity!!, roomID!!, appExecutors, object : DiffUtil.ItemCallback<RoomMember>() {
            override fun areItemsTheSame(firstUser: RoomMember, secondUser: RoomMember): Boolean {
                return firstUser.userId == secondUser.userId
            }

            override fun areContentsTheSame(firstUser: RoomMember, secondUser: RoomMember): Boolean {
                return firstUser.avatarUrl == secondUser.avatarUrl && firstUser.name == secondUser.name
            }
        }, dataBinding) { user ->
            if (application.getUserId().compareTo(user.userId) == 0) {
                val userIntent = Intent(activity, ProfileActivity::class.java)
                userIntent.putExtra(ViewUserProfileActivity.ROOM_ID, roomID)
                userIntent.putExtra(ViewUserProfileActivity.JOINED, true)
                startActivity(userIntent)
            } else {
                val otherUserIntent = Intent(activity, ViewUserProfileActivity::class.java)
                otherUserIntent.putExtra(ViewUserProfileActivity.USER_ID, user.userId)
                otherUserIntent.putExtra(ViewUserProfileActivity.ROOM_ID, roomID)
                otherUserIntent.putExtra(ViewUserProfileActivity.JOINED, true)
                startActivity(otherUserIntent)
            }
        }
        listUserJoinAdapter.submitList(mDataJoin)
        binding.recyclerViewJoin.adapter = listUserJoinAdapter
        binding.recyclerViewJoin.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewJoin.isNestedScrollingEnabled = false
    }

    private fun initInvites(roomID: String, mDataInvite: List<RoomMember>?) {
        listUserInvitesAdapter = ListUserRecyclerViewAdapterCustom(activity!!, roomID, appExecutors, object : DiffUtil.ItemCallback<RoomMember>() {
            override fun areItemsTheSame(firstUser: RoomMember, secondUser: RoomMember): Boolean {
                return firstUser.userId == secondUser.userId
            }

            override fun areContentsTheSame(firstUser: RoomMember, secondUser: RoomMember): Boolean {
                return firstUser.avatarUrl == secondUser.avatarUrl && firstUser.name == secondUser.name
            }
        }, dataBinding) { user ->

            Debug.e("--- RoomID: ${roomID}")
            if (application.getUserId().compareTo(user.userId) == 0) {
                val userIntent = Intent(activity, ProfileActivity::class.java)
                userIntent.putExtra(ViewUserProfileActivity.ROOM_ID, roomID)
                startActivity(userIntent)
            } else {
                val otherUserIntent = Intent(activity, ViewUserProfileActivity::class.java)
                otherUserIntent.putExtra(ViewUserProfileActivity.USER_ID, user.userId)
                otherUserIntent.putExtra(ViewUserProfileActivity.ROOM_ID, roomID)
                startActivity(otherUserIntent)
            }
        }
        binding.recyclerViewInvites.adapter = listUserInvitesAdapter
        listUserInvitesAdapter.submitList(mDataInvite)
        binding.recyclerViewInvites.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewInvites.isNestedScrollingEnabled = false
    }

    override fun onGroupInvitedUsersListUpdate(p0: String?) {
        Log.e("Tag", "--- onGroupInvitedUsersListUpdate: $p0")
    }

    override fun onPresenceUpdate(p0: Event?, p1: User?) {
        when (p0?.type) {
            Event.EVENT_TYPE_PRESENCE -> {
                Debug.e("--- onPresenceUpdate: ${p1?.displayname} \naction: ${p0.type}")
                mRoom?.getActiveMembersAsync(callBackMembersAsync!!)
            }
        }
    }

    override fun onLiveEvent(p0: Event?, p1: RoomState?) {
        when (p0?.type) {
            Event.EVENT_TYPE_STATE_ROOM_MEMBER -> {
                try {
                    Debug.e("--- onLiveEvent: event: ${p0.type}")
                    mRoom?.getActiveMembersAsync(callBackMembersAsync!!)
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onDirectMessageChatRoomsListUpdate() {
        Log.e("Tag", "--- onDirectMessageChatRoomsListUpdate: ")
    }

    override fun onSyncError(p0: MatrixError?) {
        Log.e("Tag", "--- onSyncError: ")
    }

    override fun onEventSentStateUpdated(p0: Event?) {
        Log.e("Tag", "--- onEventSentStateUpdated: ")
    }

    override fun onBingRulesUpdate() {
        Log.e("Tag", "--- onBingRulesUpdate: ")
    }

    override fun onRoomTagEvent(p0: String?) {
        Debug.e("--- onRoomTagEvent: $p0")
    }

    override fun onLeaveRoom(p0: String?) {
        Debug.e("--- onLeaveRoom: $p0")
    }

    override fun onLiveEventsChunkProcessed(p0: String?, p1: String?) {
        Debug.e("--- onLiveEventsChunkProcessed: type: ${p0}, ${p1}")
        try {
            mRoom?.getActiveMembersAsync(callBackMembersAsync!!)
        } catch (e: Exception) {
            Debug.e("--- Error: ${e.message}")
        }
    }

    override fun onBingEvent(p0: Event?, p1: RoomState?, p2: BingRule?) {
        Log.e("Tag", "--- onBingEvent: ")
    }

    override fun onReadMarkerEvent(p0: String?) {
        Log.e("Tag", "--- onReadMarkerEvent: ")
    }

    override fun onStoreReady() {
        Log.e("Tag", "--- onStoreReady: ")
    }

    override fun onCryptoSyncComplete() {
        Log.e("Tag", "--- onCryptoSyncComplete: ")
    }

    override fun onJoinRoom(p0: String?) {
        Log.e("Tag", "--- onJoinRoom: có user join room: $p0")
    }

    override fun onReceiptEvent(p0: String?, p1: MutableList<String>?) {
        Log.e("Tag", "--- onReceiptEvent: có onReceiptEvent")
    }

    override fun onAccountInfoUpdate(p0: MyUser?) {
        Log.e("Tag", "--- onAccountInfoUpdate: ")
    }

    override fun onGroupProfileUpdate(p0: String?) {
        Log.e("Tag", "--- onGroupProfileUpdate: ")
    }

    override fun onIgnoredUsersListUpdate() {
        Log.e("Tag", "--- onIgnoredUsersListUpdate: ")
    }

    override fun onRoomKick(p0: String?) {
        Log.e("Tag", "--- onRoomKick: ")
    }

    override fun onLeaveGroup(p0: String?) {
        Log.e("Tag", "--- onLeaveGroup: có user onLeaveGroup room")
    }

    override fun onGroupUsersListUpdate(p0: String?) {
        Log.e("Tag", "--- onGroupUsersListUpdate: ")
    }

    override fun onEventSent(p0: Event?, p1: String?) {
        Log.e("Tag", "--- onEventSent: ")
    }

    override fun onAccountDataUpdated(p0: AccountDataElement?) {
        Log.e("Tag", "--- onAccountDataUpdated: ")
    }

    override fun onRoomFlush(p0: String?) {
        Log.e("Tag", "--- onRoomFlush: ")
    }

    override fun onNotificationCountUpdate(p0: String?) {
        Log.e("Tag", "--- onNotificationCountUpdate: ")
    }

    override fun onJoinGroup(p0: String?) {
        Log.e("Tag", "--- onJoinGroup: ")
    }

    override fun onRoomInternalUpdate(p0: String?) {
        Log.e("Tag", "--- onRoomInternalUpdate: ")
    }

    override fun onNewGroupInvitation(p0: String?) {
        Log.e("Tag", "--- onNewGroupInvitation: ")
    }

    override fun onGroupRoomsListUpdate(p0: String?) {
        Log.e("Tag", "--- onGroupRoomsListUpdate: ")
    }

    override fun onEventDecrypted(p0: String?, p1: String?) {
        Log.e("Tag", "--- onEventDecrypted: ")
    }

    override fun onNewRoom(p0: String?) {
        Log.e("Tag", "--- onNewRoom: ")
    }

    override fun onInitialSyncComplete(p0: String?) {
        Log.e("Tag", "--- onInitialSyncComplete: ")
    }

    override fun onToDeviceEvent(p0: Event?) {
        Log.e("Tag", "--- onToDeviceEvent: ")
    }

    override fun getFragment(): Fragment {
        return this
    }
}
