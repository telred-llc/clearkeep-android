package vmodev.clearkeep.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import im.vector.Matrix
import im.vector.R
import im.vector.util.HomeRoomsViewModel
import im.vector.util.VectorUtils
import kotlinx.android.synthetic.main.activity_home_screen.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.listeners.MXEventListener
import vmodev.clearkeep.fragments.*

class HomeScreenActivity : AppCompatActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener,
        DirectMessageFragment.OnFragmentInteractionListener, RoomFragment.OnFragmentInteractionListener {

    private lateinit var mxSession: MXSession;
    private lateinit var mxEventListener : MXEventListener;
    private lateinit var homeRoomViewModel : HomeRoomsViewModel;
    private lateinit var directMessages : Array<Room>;
    private lateinit var rooms : Array<Room>;
    private lateinit var listFavourites : Array<Room>;
    private lateinit var listContacts : Array<Room>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        mxSession = Matrix.getInstance(this).defaultSession;

        bottom_navigation_view_home_screen.setOnNavigationItemSelectedListener({ menuItem ->
            kotlin.run {
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        switchFragment(HomeScreenFragment.newInstance(directMessages, rooms));
                    };
                    R.id.action_favorites -> {
                        switchFragment(FavouritesFragment.newInstance(listFavourites));
                    };
                    R.id.action_contacts -> {
                        switchFragment(ContactsFragment.newInstance(listContacts
                        ));
                    };
                }
                return@run true;
            }
        });
        VectorUtils.loadUserAvatar(this, mxSession, circle_image_view_avatar, mxSession.myUser);
        homeRoomViewModel = HomeRoomsViewModel(mxSession);
        addMxEventListener();
        search_view.setOnSearchClickListener { v -> kotlin.run { Log.d("Click: ", v.toString()) } }
        search_view.setOnQueryTextFocusChangeListener(View.OnFocusChangeListener { v, hasFocus -> kotlin.run { Log.d("Focus: ", hasFocus.toString()) } })
        search_view.queryHint = getString(R.string.search);
        search_view.setIconifiedByDefault(false);
        switchFragment(HomeScreenFragment.newInstance(directMessages, rooms));
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    override fun onGetMXSession() : MXSession {
        return mxSession;
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    private fun addMxEventListener(){
        mxEventListener = object:MXEventListener(){
            override fun onInitialSyncComplete(toToken: String?) {
                super.onInitialSyncComplete(toToken)
                val result = homeRoomViewModel.update();
                directMessages =result.directChats.toTypedArray();
                rooms = result.otherRooms.toTypedArray();
                listFavourites = result.favourites.toTypedArray();
                listContacts = result.getDirectChatsWithFavorites().toTypedArray();
            }
        };
        mxSession.dataHandler.addListener(mxEventListener);
    }
}
