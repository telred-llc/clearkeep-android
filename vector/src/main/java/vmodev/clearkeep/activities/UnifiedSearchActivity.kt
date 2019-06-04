package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorBaseSearchActivity
import im.vector.adapters.VectorUnifiedSearchFragmentPagerAdapter
import im.vector.contacts.ContactsManager
import im.vector.util.PERMISSION_REQUEST_CODE
import im.vector.util.checkPermissions
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.util.Log
import vmodev.clearkeep.adapters.UnifiedSearchFragmentPagerAdapter

class UnifiedSearchActivity : BaseSearchActivity(), VectorBaseSearchActivity.IVectorSearchActivity {

    // UI items
    private var mBackgroundImageView: ImageView? = null
    private var mNoResultsTxtView: TextView? = null
    private var mLoadOldestContentView: View? = null

    private var mRoomId: String? = null

    private var mPagerAdapter: UnifiedSearchFragmentPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    private var mPosition: Int = 0

    override fun getLayoutRes(): Int {
        return R.layout.activity_unified_search
    }

    override fun initUiAndData() {
        super.initUiAndData()

        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application.")
            CommonActivityUtils.restartApp(this)
            return
        }

        if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen")
            return
        }

        // the session should be passed in parameter
        // but the current design does not describe how the multi accounts will be managed.
        val session = Matrix.getInstance(this)!!.defaultSession
        if (session == null) {
            Log.e(LOG_TAG, "No MXSession.")
            finish()
            return
        }

        // UI widgets binding & init fields
        mBackgroundImageView = findViewById(R.id.search_background_imageview)
        mNoResultsTxtView = findViewById(R.id.search_no_result_textview)
        waitingView = findViewById(R.id.search_in_progress_view)
        mLoadOldestContentView = findViewById(R.id.search_load_oldest_progress)

        if (null != intent) {
            mRoomId = intent.getStringExtra(EXTRA_ROOM_ID)
        }
        val roomId = if (mRoomId.isNullOrEmpty()) "" else mRoomId;
        mPagerAdapter = UnifiedSearchFragmentPagerAdapter(supportFragmentManager, this, session, roomId!!)

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = findViewById(R.id.search_view_pager)
        mViewPager!!.adapter = mPagerAdapter

        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val permissions = mPagerAdapter!!.getPermissionsRequest(position)

                if (0 != permissions) {
                    // Check permission to access contacts
                    checkPermissions(permissions, this@UnifiedSearchActivity, PERMISSION_REQUEST_CODE)
                }
                searchAccordingToSelectedTab()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        // Give the TabLayout the ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.search_filter_tabs)
        tabLayout.setupWithViewPager(mViewPager)

        // the tab i
        if (null != intent && intent.hasExtra(EXTRA_TAB_INDEX)) {
            mPosition = intent.getIntExtra(EXTRA_TAB_INDEX, 0)
        } else {
            mPosition = if (isFirstCreation()) 0 else getSavedInstanceState().getInt(KEY_STATE_CURRENT_TAB_INDEX, 0)
        }
        mViewPager!!.currentItem = mPosition

        // restore the searched pattern
        mPatternToSearchEditText.setQuery(if (isFirstCreation()) null else getSavedInstanceState().getString(KEY_STATE_SEARCH_PATTERN, null), false)
        mPatternToSearchEditText.setIconifiedByDefault(false);
        mPatternToSearchEditText.isIconified = false;
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Trigger a new search to the selected fragment.
     */
    private fun searchAccordingToSelectedTab() {
        val pattern = mPatternToSearchEditText.query.toString().trim { it <= ' ' }
        val position = mViewPager!!.currentItem

        if (mPosition != position) {
            mPagerAdapter!!.cancelSearch(mPosition)
        }

        mPosition = position
        mPagerAdapter?.let {
            // the background image view should only be displayed when there is no pattern,
            // the rooms searches has a result : the public rooms list.
            resetUi(TextUtils.isEmpty(pattern) && !mPagerAdapter!!.isSearchInRoomNameFragment(position)
                    && !mPagerAdapter!!.isSearchInPeoplesFragment(position))

            val isRemoteSearching = mPagerAdapter!!.search(position, pattern, object : MatrixMessageListFragment.OnSearchResultListener {
                override fun onSearchSucceed(nbrMessages: Int) {
                    onSearchEnd(position, nbrMessages)
                }

                override fun onSearchFailed() {
                    onSearchEnd(position, 0)
                }
            })

            if (isRemoteSearching) {
                showWaitingView()
            }
        }
    }

    override fun onPatternUpdate(isTypingUpdate: Boolean) {
        val position = mViewPager!!.currentItem

        // the messages searches are not done locally.
        // so, such searches can only be done if the user taps on the search button.
        mPagerAdapter?.let {
            if (isTypingUpdate && (mPagerAdapter!!.isSearchInMessagesFragment(position) || mPagerAdapter!!.isSearchInFilesFragment(position))) {
                return
            }
            searchAccordingToSelectedTab()
        }
    }

    /**
     * Reset the UI to its init state:
     * - "waiting while searching" screen disabled
     * - background image visible
     * - no results message disabled
     *
     * @param showBackgroundImage true to display it
     */
    private fun resetUi(showBackgroundImage: Boolean) {
        // stop "wait while searching" screen
        hideWaitingView()

        // display the background
        if (null != mBackgroundImageView) {
            mBackgroundImageView!!.visibility = if (showBackgroundImage) View.VISIBLE else View.GONE
        }

        if (null != mNoResultsTxtView) {
            mNoResultsTxtView!!.visibility = View.GONE
        }

        if (null != mLoadOldestContentView) {
            mLoadOldestContentView!!.visibility = View.GONE
        }
    }

    /**
     * The search is done.
     *
     * @param tabIndex    the tab index
     * @param nbrMessages the number of found messages.
     */
    private fun onSearchEnd(tabIndex: Int, nbrMessages: Int) {
        if (mViewPager!!.currentItem == tabIndex) {
            Log.d(LOG_TAG, "## onSearchEnd() nbrMsg=$nbrMessages")
            // stop "wait while searching" screen
            hideWaitingView()

            // display the background view if there is no pending such
            mBackgroundImageView!!.visibility = if (!mPagerAdapter!!.isSearchInPeoplesFragment(tabIndex)
                    && 0 == nbrMessages && TextUtils.isEmpty(mPatternToSearchEditText.query.toString()))
                View.VISIBLE
            else
                View.GONE

            // display the "no result" text only if the researched text is not empty
            mNoResultsTxtView!!.visibility = if (0 == nbrMessages && !TextUtils.isEmpty(mPatternToSearchEditText.query.toString()))
                View.VISIBLE
            else
                View.GONE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (0 == permissions.size) {
            Log.d(LOG_TAG, "## onRequestPermissionsResult(): cancelled $requestCode")
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission granted")
                // trigger a contacts book refresh
                ContactsManager.getInstance().refreshLocalContactsSnapshot()

                searchAccordingToSelectedTab()
            } else {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission not granted")
                Toast.makeText(this, R.string.missing_permissions_warning, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //==============================================================================================================
    // Life cycle Activity methods
    //==============================================================================================================

    @SuppressLint("LongLogTag")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(LOG_TAG, "## onSaveInstanceState(): ")

        // save current tab
        val currentIndex = mViewPager!!.currentItem
        outState.putInt(KEY_STATE_CURRENT_TAB_INDEX, currentIndex)

        val searchPattern = mPatternToSearchEditText.query.toString()

        if (!TextUtils.isEmpty(searchPattern)) {
            outState.putString(KEY_STATE_SEARCH_PATTERN, searchPattern)
        }
    }

    //==============================================================================================================
    // VectorBaseSearchActivity.IVectorSearchActivity
    //==============================================================================================================

    override fun refreshSearch() {
        searchAccordingToSelectedTab()
    }

    companion object {
        private val LOG_TAG = UnifiedSearchActivity::class.java.simpleName

        val EXTRA_ROOM_ID = "VectorUnifiedSearchActivity.EXTRA_ROOM_ID"
        val EXTRA_TAB_INDEX = "VectorUnifiedSearchActivity.EXTRA_TAB_INDEX"

        // activity life cycle management:
        // - Bundle keys
        private val KEY_STATE_CURRENT_TAB_INDEX = "CURRENT_SELECTED_TAB"
        private val KEY_STATE_SEARCH_PATTERN = "SEARCH_PATTERN"

        // item position when it is a search in no room
        val SEARCH_ROOMS_TAB_POSITION = 0
        val SEARCH_MESSAGES_TAB_POSITION = 1
        val SEARCH_PEOPLE_TAB_POSITION = 2
        val SEARCH_FILES_TAB_POSITION = 3
    }
}