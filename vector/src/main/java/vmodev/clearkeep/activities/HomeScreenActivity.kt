package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.activity.VectorRoomActivity
import im.vector.activity.VectorRoomCreationActivity
import im.vector.databinding.ActivityHomeScreenBinding
import im.vector.services.EventStreamService
import im.vector.ui.badge.BadgeProxy
import im.vector.util.HomeRoomsViewModel
import im.vector.util.RoomUtils
import im.vector.util.VectorUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home_screen.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomPreviewData
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.RoomMember
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.UserViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeScreenActivity : DaggerAppCompatActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        DirectMessageFragment.OnFragmentInteractionListener, RoomFragment.OnFragmentInteractionListener
        , SearchFragment.OnFragmentInteractionListener
        , PreviewFragment.OnFragmentInteractionListener, LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    lateinit var mxSession: MXSession;
    private lateinit var mxEventListener: MXEventListener;
    private lateinit var homeRoomViewModel: HomeRoomsViewModel;
    private var directMessages: List<Room> = ArrayList();
    private var rooms: List<Room> = ArrayList();
    private var listFavourites: List<Room> = ArrayList();
    private var listContacts: List<Room> = ArrayList();
    private var roomInvites: ArrayList<Room> = ArrayList();
    private var directMessageInvite: ArrayList<Room> = ArrayList();

    private var roomsNotifyCount: Int = 0;
    private var directNotifyCount: Int = 0;

    private val publishSubjectListRoomChanged: PublishSubject<Status> = PublishSubject.create();

    var dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivityHomeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen, dataBindingComponent);
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        (application as ClearKeepApplication).setEventHandler();
        bottom_navigation_view_home_screen.setOnNavigationItemSelectedListener { menuItem ->
            kotlin.run {
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        switchFragment(HomeScreenFragment.newInstance());
                    };
                    R.id.action_favorites -> {
                        switchFragment(FavouritesFragment.newInstance());
                    };
                    R.id.action_contacts -> {
                        switchFragment(ContactsFragment.newInstance());
                    };
                }
                return@run true;
            }
        };
        dataBinding.circleImageViewAvatar.setOnClickListener { v ->
            kotlin.run {
                val intent = Intent(this, ProfileActivity::class.java);
                startActivity(intent);
            }
        }
        homeRoomViewModel = HomeRoomsViewModel(mxSession);

        switchFragment(HomeScreenFragment.newInstance());

        dataBinding.frameLayoutSearch.setOnClickListener { v ->
            val intent = Intent(this, SearchActivity::class.java);
            startActivity(intent);
        }
        val userViewModel: AbstractUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractUserViewModel::class.java);
        userViewModel.setUserId(mxSession.myUserId);
        dataBinding.user = userViewModel.getUserData();
        dataBinding.setLifecycleOwner(this);
        dataBinding.buttonCreateConvention.setOnClickListener { v ->
            kotlin.run {
                val intent = Intent(this, FindAndCreateNewConversationActivity::class.java)
                startActivity(intent);
            }
        }
    }

    private fun showAlertDiaglong(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("Yes") { dialog, which -> kotlin.run { finish() } }.setNegativeButton("No", null).show();
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private fun checkCurrentSearchFragment(): Boolean {
        val currentFragment = supportFragmentManager.fragments.get(supportFragmentManager.fragments.lastIndex);
        return currentFragment is SearchFragment;
    }

    override fun onGetMXSession(): MXSession {
        return mxSession;
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onGetListFavourites(): List<Room> {
        return listFavourites;
    }

    override fun onGetListContact(): List<Room> {
        return listContacts;
    }

    override fun onGetListDirectMessage(): List<Room> {
        return directMessages;
    }

    override fun onGetListRooms(): List<Room> {
        return rooms;
    }

    override fun onClickItem(room: Room) {
        openRoom(room);
    }

    override fun handleDataChange(): PublishSubject<Status> {
        return publishSubjectListRoomChanged;
    }

    override fun onGetListDirectMessageInvitation(): List<Room> {
        return directMessageInvite;
    }

    override fun onClickItemJoin(roomId: String) {
        joinRoom(roomId)
    }

    override fun onClickItemDecline(roomId: String) {
        onRejectInvitation(roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
            }
        })
    }

    override fun onClickItemPreview(roomId: String) {
        val intent: Intent = Intent(this, PreviewInviteRoomActivity::class.java);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    override fun onJoinClick(room: Room) {
        onBackPressed();
        joinRoom(room.roomId)
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
    }

    override fun onDeclineClick(room: Room) {
        onBackPressed();
        onRejectInvitation(room.roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
            }
        })
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
    }

    override fun onNavigationOnClick() {
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
        onBackPressed();
    }

    override fun onGetListRoomInvitation(): List<Room> {
        return roomInvites;
    }

    protected fun openRoom(room: Room?) {
        // sanity checks
        // reported by GA
        if (null == mxSession.getDataHandler() || null == mxSession.getDataHandler().getStore()) {
            return
        }

        val roomId: String?
        // cannot join a leaving room
        if (room == null || room.isLeaving) {
            roomId = null
        } else {
            roomId = room.roomId
        }

        if (roomId != null) {
            val roomSummary = mxSession.getDataHandler().getStore().getSummary(roomId)

            if (null != roomSummary) {
                room!!.sendReadReceipt()
            }

            // Update badge unread count in case device is offline
            BadgeProxy.specificUpdateBadgeUnreadCount(mxSession, this)

            // Launch corresponding room activity
            val params = HashMap<String, Any>()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.getMyUserId()
            params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId

            CommonActivityUtils.goToRoomPage(this, mxSession, params)
        }
    }

    fun getRoomInvitations() {
        val directChatInvitations = ArrayList<Room>()
        val roomInvitations = ArrayList<Room>()

        if (null == mxSession.getDataHandler().getStore()) {
//            return ArrayList()
            return;
        }

        val roomSummaries = mxSession.getDataHandler().getStore().getSummaries()
        for (roomSummary in roomSummaries) {
            // reported by rageshake
            // i don't see how it is possible to have a null roomSummary
            if (null != roomSummary) {
                val roomSummaryId = roomSummary!!.getRoomId()
                val room = mxSession.getDataHandler().getStore().getRoom(roomSummaryId)

                // check if the room exists
                // the user conference rooms are not displayed.
                if (room != null && !room!!.isConferenceUserRoom() && room!!.isInvited()) {
                    if (room!!.isDirectChatInvitation()) {
                        directChatInvitations.add(room)
                    } else {
                        roomInvitations.add(room)
                    }
                }
            }
        }

        // the invitations are sorted from the oldest to the more recent one
        val invitationComparator = RoomUtils.getRoomsDateComparator(mxSession, true)
        Collections.sort(directChatInvitations, invitationComparator)
        Collections.sort(roomInvitations, invitationComparator)

//        val roomInvites = ArrayList<Room>()
//        when (mCurrentMenuId) {
//            R.id.bottom_action_people -> roomInvites.addAll(directChatInvitations)
//            R.id.bottom_action_rooms -> roomInvites.addAll(roomInvitations)
//            else -> {
        directMessageInvite.clear();
        roomInvites.clear();
        directMessageInvite.addAll(directChatInvitations)
        roomInvites.addAll(roomInvitations)
//        Collections.sort(roomInvites, invitationComparator)
//            }
//        }

//        return roomInvites
    }

    fun onPreviewRoom(session: MXSession, roomId: String) {
        var roomAlias: String? = null
        var roomName: String? = null

        val room = session.dataHandler.getRoom(roomId)
        if (null != room && null != room.state) {
            roomAlias = room.state.canonicalAlias
            roomName = room.getRoomDisplayName(this)
        }

        val roomPreviewData = RoomPreviewData(mxSession, roomId, null, roomAlias, null)
        roomPreviewData.roomName = roomName
        CommonActivityUtils.previewRoom(this, roomPreviewData)
    }

    fun onRejectInvitation(roomId: String, onSuccessCallback: ApiCallback<Void>?) {
        val room = mxSession.getDataHandler().getRoom(roomId)

        if (null != room) {
            room!!.leave(createForgetLeaveCallback(roomId, onSuccessCallback))
        }
    }

    private fun createForgetLeaveCallback(roomId: String, onSuccessCallback: ApiCallback<Void>?): ApiCallback<Void> {
        return object : ApiCallback<Void> {
            override fun onSuccess(info: Void) {
                // clear any pending notification for this room
                EventStreamService.cancelNotificationsForRoomId(mxSession.getMyUserId(), roomId)

                onSuccessCallback?.onSuccess(null)
            }

            private fun onError(message: String) {
                Toast.makeText(this@HomeScreenActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
//                    consentNotGivenHelper.displayDialog(e)
                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        }
    }

    private fun joinRoom(roomId: String) {
        val room = mxSession.dataHandler.store.getRoom(roomId);
        mxSession.joinRoom(room!!.getRoomId(), object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()

                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.getMyUserId()
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.getRoomId()
                params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = room!!.isDirect;

                CommonActivityUtils.goToRoomPage(this@HomeScreenActivity, mxSession, params)
            }

            private fun onError(errorMessage: String) {
                Toast.makeText(this@HomeScreenActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {

                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }

    private fun updateNotifyOnBottomNavigation() {
        roomsNotifyCount = 0;
        directNotifyCount = 0;
        roomsNotifyCount = roomInvites.size;
        directNotifyCount = directMessageInvite.size;
        var notifyCount = roomInvites.size + directMessageInvite.size;
        for (room in rooms) {
            if (room.notificationCount > 0) {
                notifyCount++;
                roomsNotifyCount++;
            }
        }
        for (direct in directMessages) {
            if (direct.notificationCount > 0) {
                notifyCount++;
                directNotifyCount++;
            }
        }
        if (notifyCount > 0) {
            text_view_notify_home.text = notifyCount.toString();
            text_view_notify_home.visibility = View.VISIBLE;
        } else {
            text_view_notify_home.visibility = View.GONE;
        }
    }

    override fun getRoomNotifyCount(): Int {
        return roomsNotifyCount;
    }

    override fun getDirectNotifyCount(): Int {
        return directNotifyCount;
    }

    override fun updateData() {
    }

    override fun onClickGoRoom(roomId: String) {
        val room = mxSession.dataHandler.getRoom(roomId);
        openRoom(room);
    }
    override fun onClickItemJoin(room: Room) {
        joinRoom(room.roomId);
    }

    override fun onClickItemDecline(room: Room) {
        onRejectInvitation(room.roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
            }
        })
    }

    override fun onClickItemPreview(room: Room) {
        toolbar.visibility = View.GONE;
        bottom_navigation_view_home_screen.visibility = View.GONE;
        switchFragment(PreviewFragment.newInstance(room.roomId));
    }
}
