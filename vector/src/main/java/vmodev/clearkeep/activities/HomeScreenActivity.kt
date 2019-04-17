package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
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
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.viewmodelobjects.Status
import java.util.*
import java.util.function.Consumer
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeScreenActivity : DaggerAppCompatActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        DirectMessageFragment.OnFragmentInteractionListener, RoomFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        SearchRoomsFragment.OnFragmentInteractionListener, SearchMessagesFragment.OnFragmentInteractionListener, SearchPeopleFragment.OnFragmentInteractionListener,
        SearchFilesFragment.OnFragmentInteractionListener, PreviewFragment.OnFragmentInteractionListener {

    @Inject lateinit var matrixService : MatrixService;

    @Inject lateinit var mxSession: MXSession;
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
//        mxSession = Matrix.getInstance(this).defaultSession;

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
        VectorUtils.loadUserAvatar(this, mxSession, circle_image_view_avatar, mxSession.myUser);
        homeRoomViewModel = HomeRoomsViewModel(mxSession);

//        search_view.queryHint = getString(R.string.search);
        search_view.setIconifiedByDefault(false);


        switchFragment(HomeScreenFragment.newInstance());
        addMxEventListener();

//        search_view.setOnSearchClickListener { v -> kotlin.run { Log.d("Click: ", v.toString()) } }
        search_view.setOnQueryTextFocusChangeListener { v, hasFocus ->
            kotlin.run {
                if (hasFocus) {
                    if (checkCurrentSearchFragment())
                        return@run;
                    bottom_navigation_view_home_screen.visibility = View.GONE;
                    circle_image_view_avatar.visibility = View.GONE;
                    button_create_convention.visibility = View.GONE;
                    text_view_cancel.visibility = View.VISIBLE;
                    switchFragment(SearchFragment.newInstance("", ""));
                }
            }
        };
        text_view_cancel.setOnClickListener { v ->
            kotlin.run {
                search_view.clearFocus();
                bottom_navigation_view_home_screen.visibility = View.VISIBLE;
                circle_image_view_avatar.visibility = View.VISIBLE;
                button_create_convention.visibility = View.VISIBLE;
                text_view_cancel.visibility = View.GONE;
                onBackPressed();
            }
        };
        Log.d("Matrix Service: ", matrixService.toString());
    }

    override fun onBackPressed() {
//        if (supportFragmentManager.fragments.size == 1){
//            showAlertDiaglong("Quit application", "You are sure quit this application?");
//            return;
//        }
        super.onBackPressed()
//        val intent = Intent(this, VectorHomeActivity::class.java)
//        startActivity(intent);
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

    private fun addMxEventListener() {
        mxEventListener = object : MXEventListener() {
            override fun onInitialSyncComplete(toToken: String?) {
                super.onInitialSyncComplete(toToken)
                needUpdateData();
            }

            override fun onAccountInfoUpdate(myUser: MyUser?) {
                super.onAccountInfoUpdate(myUser)
                VectorUtils.loadUserAvatar(this@HomeScreenActivity, mxSession, circle_image_view_avatar, mxSession.myUser);
            }

            override fun onLiveEvent(event: Event?, roomState: RoomState?) {
                super.onLiveEvent(event, roomState);
                needUpdateData();
//                if (event!!.type == Event.EVENT_TYPE_STATE_ROOM_MEMBER) {
                needUpdateData();
//                }
            }

            override fun onLiveEventsChunkProcessed(fromToken: String?, toToken: String?) {
                super.onLiveEventsChunkProcessed(fromToken, toToken)
                needUpdateData();
            }

        };
        mxSession.dataHandler.addListener(mxEventListener);
    }

    @SuppressLint("CheckResult")
    private fun needUpdateData() {
        Observable.create<Status> { emitter ->
            kotlin.run {
                val result = homeRoomViewModel.update();
                if (result != null) {
                    directMessages = result.directChats;
                    rooms = result.otherRooms;
                    listFavourites = result.favourites;
                    listContacts = result.getDirectChatsWithFavorites();
                    emitter.onNext(Status.SUCCESS);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: Status? ->
            kotlin.run {
                getRoomInvitations();
                publishSubjectListRoomChanged.onNext(t!!);
                updateNotifyOnBottomNavigation();
            }
        };
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mxSession.dataHandler.removeListener(mxEventListener);
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

    override fun onClickItemJoin(room: Room) {
        joinRoom(room)
    }

    override fun onClickItemDecline(room: Room) {
        onRejectInvitation(room.roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
                needUpdateData();
            }
        })
    }

    override fun onClickItemPreview(room: Room) {
        toolbar.visibility = View.GONE;
        bottom_navigation_view_home_screen.visibility = View.GONE;
        switchFragment(PreviewFragment.newInstance(room.roomId));
    }

    override fun onJoinClick(room: Room) {
        onBackPressed();
        joinRoom(room)
        toolbar.visibility = View.VISIBLE;
        bottom_navigation_view_home_screen.visibility = View.VISIBLE;
    }

    override fun onDeclineClick(room: Room) {
        onBackPressed();
        onRejectInvitation(room.roomId, object : SimpleApiCallback<Void>(this) {
            override fun onSuccess(p0: Void?) {
                needUpdateData();
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

    private fun joinRoom(room: Room) {
        mxSession.joinRoom(room!!.getRoomId(), object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                val params = HashMap<String, Any>()

                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mxSession.getMyUserId()
                params[VectorRoomActivity.EXTRA_ROOM_ID] = room!!.getRoomId()

                CommonActivityUtils.goToRoomPage(this@HomeScreenActivity, mxSession, params)
                needUpdateData();
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
        needUpdateData();
    }

    override fun onResume() {
        super.onResume()
        needUpdateData();
    }
    //    val consentNotGivenHelper by lazy {
//        ConsentNotGivenHelper(this, savedInstanceState)
//                .apply { addToRestorables(this) }
//    }
}
