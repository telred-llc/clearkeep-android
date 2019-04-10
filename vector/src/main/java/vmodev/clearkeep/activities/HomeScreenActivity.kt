package vmodev.clearkeep.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.inputmethodservice.Keyboard
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.activity.VectorRoomActivity
import im.vector.ui.badge.BadgeProxy
import im.vector.util.HomeRoomsViewModel
import im.vector.util.VectorUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_screen.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.listeners.MXEventListener
import vmodev.clearkeep.fragments.*
import java.util.HashMap

class HomeScreenActivity : AppCompatActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        DirectMessageFragment.OnFragmentInteractionListener, RoomFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        SearchRoomsFragment.OnFragmentInteractionListener, SearchMessagesFragment.OnFragmentInteractionListener, SearchPeopleFragment.OnFragmentInteractionListener,
        SearchFilesFragment.OnFragmentInteractionListener {

    private lateinit var mxSession: MXSession;
    private lateinit var mxEventListener: MXEventListener;
    private lateinit var homeRoomViewModel: HomeRoomsViewModel;
    private lateinit var directMessages: List<Room>;
    private lateinit var rooms: List<Room>;
    private lateinit var listFavourites: List<Room>;
    private lateinit var listContacts: List<Room>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        mxSession = Matrix.getInstance(this).defaultSession;

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
        addMxEventListener();
        search_view.setOnSearchClickListener { v -> kotlin.run { Log.d("Click: ", v.toString()) } }
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
        search_view.queryHint = getString(R.string.search);
        search_view.setIconifiedByDefault(false);
        switchFragment(HomeScreenFragment.newInstance());
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
                val result = homeRoomViewModel.update();
                directMessages = result.directChats;
                rooms = result.otherRooms;
                listFavourites = result.favourites;
                listContacts = result.getDirectChatsWithFavorites();

                Observable.create<List<Room>> { emitter -> kotlin.run {
                    val homeRoomVM = HomeRoomsViewModel(mxSession);
                    homeRoomVM.update();
                    val result = homeRoomVM.result;
                    if (result != null){
                        emitter.onNext(result.directChats);
                        emitter.onComplete();
                    }
                    else{
                        emitter.onError(NullPointerException());
                        emitter.onComplete();
                    }
                } }.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe{t: List<Room>? -> kotlin.run { Log.d("Data: ", t!!.size.toString()) } };
            }
        };
        mxSession.dataHandler.addListener(mxEventListener);
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
}
