package vmodev.clearkeep.adapters

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import im.vector.PublicRoomsManager
import im.vector.R
import im.vector.util.PERMISSIONS_FOR_MEMBERS_SEARCH
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import vmodev.clearkeep.fragments.SearchMessagesListFragment
import vmodev.clearkeep.fragments.SearchPeopleListFragment
import vmodev.clearkeep.fragments.SearchRoomsFilesListFragment
import vmodev.clearkeep.fragments.SearchRoomsListFragment

class UnifiedSearchFragmentPagerAdapter
/**
 * Constructor
 *
 * @param fm      the fragment manager
 * @param context the context
 * @param session the session
 * @param roomId  the room id
 */
(fm: FragmentManager, private val mContext: Context, private val mSession: MXSession, private val mRoomId: String) : FragmentPagerAdapter(fm) {

    // position + (title res id , fragment)
    private val mFragmentsData: SparseArrayCompat<Pair<Int, Fragment>>?

    init {

        mFragmentsData = SparseArrayCompat()

        val searchInRoom = !TextUtils.isEmpty(mRoomId)

        var pos = 0
        if (!searchInRoom) {
//            mFragmentsData.put(pos, Pair(R.string.tab_title_search_rooms, null))
            pos++
        }

//        mFragmentsData.put(pos, Pair(R.string.tab_title_search_messages, null))
        pos++

        if (!searchInRoom) {
//            mFragmentsData.put(pos, Pair(R.string.tab_title_search_people, null))
            pos++
        }

//        mFragmentsData.put(pos, Pair(R.string.tab_title_search_files, null))
    }

    override fun getCount(): Int {
        return mFragmentsData!!.size()
    }

    override fun getItem(position: Int): Fragment {
        val pair = mFragmentsData!!.get(position)
        val titleId = if (pair == null) -1 else pair.first
        var fragment: Fragment? = pair?.second

        if (fragment == null) {
            when (titleId) {
                R.string.tab_title_search_rooms -> {
                    fragment = SearchRoomsListFragment.newInstance(mSession.myUserId,
                            R.layout.fragment_vector_recents_list)
                }
                R.string.tab_title_search_messages -> {
                    fragment = SearchMessagesListFragment.newInstance(mSession.myUserId,
                            mRoomId, org.matrix.androidsdk.R.layout.fragment_matrix_message_list_fragment)
                }
                R.string.tab_title_search_people -> {
                    fragment = SearchPeopleListFragment.newInstance(mSession.myUserId,
                            R.layout.fragment_vector_search_people_list)
                }
                R.string.tab_title_search_files -> {
                    fragment = SearchRoomsFilesListFragment.newInstance(mSession.myUserId,
                            mRoomId, org.matrix.androidsdk.R.layout.fragment_matrix_message_list_fragment)
                }
            }

            // should never fails
            if (null == fragment) {
                return SearchRoomsListFragment.newInstance(mSession.myUserId,
                        R.layout.fragment_vector_recents_list)
            }
        }

        return fragment
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        val pair = mFragmentsData!!.get(position)
        if (pair != null) {
            mFragmentsData.put(position, Pair(pair.first, createdFragment))
        }
        return createdFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (null != mFragmentsData && mFragmentsData.get(position) != null) {
            mContext.getString(mFragmentsData.get(position)!!.first!!)
        } else super.getPageTitle(position)

    }

    /**
     * Cancel any pending search
     */
    fun cancelSearch(position: Int) {
        val pair = mFragmentsData!!.get(position)
        val titleId = if (pair == null) -1 else pair.first
        val fragment = pair?.second ?: return

        if (titleId == R.string.tab_title_search_messages) {
            (fragment as SearchMessagesListFragment).cancelCatchingRequests()
        } else if (titleId == R.string.tab_title_search_files) {
            (fragment as SearchRoomsFilesListFragment).cancelCatchingRequests()
        }
    }

    /**
     * Triggers a search in the currently displayed fragments
     *
     * @param position the fragment position
     * @param pattern  the pattern to search
     * @param listener the search listener
     * @return true if a remote search is triggered
     */
    fun search(position: Int, pattern: String, listener: MatrixMessageListFragment.OnSearchResultListener): Boolean {
        // sanity checks
        if (null == mFragmentsData) {
            listener.onSearchSucceed(0)
            return false
        }

        val pair = mFragmentsData.get(position)
        val titleId = if (pair == null) -1 else pair.first
        val fragment = pair?.second

        // sanity checks
        if (null == fragment) {
            listener.onSearchSucceed(0)
            return false
        }

        var res = false

        when (titleId) {
            R.string.tab_title_search_rooms -> {
                res = PublicRoomsManager.getInstance().isRequestInProgress
                (fragment as SearchRoomsListFragment).searchPattern(pattern, listener)
            }
            R.string.tab_title_search_messages -> {
                res = !TextUtils.isEmpty(pattern)
                (fragment as SearchMessagesListFragment).searchPattern(pattern, listener)
            }
            R.string.tab_title_search_people -> {
                res = (fragment as SearchPeopleListFragment).isReady
                fragment.searchPattern(pattern, listener)
            }
            R.string.tab_title_search_files -> {
                res = !TextUtils.isEmpty(pattern)
                (fragment as SearchRoomsFilesListFragment).searchPattern(pattern, listener)
            }
        }

        return res
    }

    /**
     * Provide the permission request for a dedicated position
     *
     * @param position the position
     * @return the required permission or 0 if none are required
     */
    fun getPermissionsRequest(position: Int): Int {
        if (null != mFragmentsData) {
            val pair = mFragmentsData.get(position)
            val titleId = if (pair == null) -1 else pair.first

            if (titleId == R.string.tab_title_search_people) {
                return PERMISSIONS_FOR_MEMBERS_SEARCH
            }
        }

        return 0
    }

    /**
     * Tells if the current fragment at the provided position is the room search one.
     *
     * @param position the position
     * @return true if it is the expected one.
     */
    fun isSearchInRoomNameFragment(position: Int): Boolean {
        val pair = mFragmentsData?.get(position)
        return pair != null && R.string.tab_title_search_rooms === pair.first
    }

    /**
     * Tells if the current fragment at the provided position is the messages search one.
     *
     * @param position the position
     * @return true if it is the expected one.
     */
    fun isSearchInMessagesFragment(position: Int): Boolean {
        val pair = mFragmentsData?.get(position)
        return pair != null && R.string.tab_title_search_messages === pair.first
    }

    /**
     * Tells if the current fragment at the provided position is the files search one.
     *
     * @param position the position
     * @return true if it is the expected one.
     */
    fun isSearchInFilesFragment(position: Int): Boolean {
        val pair = mFragmentsData?.get(position)
        return pair != null && R.string.tab_title_search_files === pair.first
    }

    /**
     * Tells if the current fragment at the provided position is the people search one.
     *
     * @param position the position
     * @return true if it is the expected one.
     */
    fun isSearchInPeoplesFragment(position: Int): Boolean {
        val pair = mFragmentsData?.get(position)
        return pair != null && R.string.tab_title_search_people === pair.first
    }

}
