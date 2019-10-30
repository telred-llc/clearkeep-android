package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ExpandableListView
import butterknife.BindView
import im.vector.Matrix
import im.vector.R
import im.vector.activity.VectorBaseSearchActivity
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorParticipantsAdapter
import im.vector.contacts.Contact
import im.vector.contacts.ContactsManager
import im.vector.fragments.VectorBaseFragment
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.MXPatterns
import org.matrix.androidsdk.fragments.MatrixMessageListFragment
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.User
import vmodev.clearkeep.activities.UserInformationActivity

class SearchPeopleListFragment : VectorBaseFragment() {

    // the session
    private var mSession: MXSession? = null
    @BindView(R.id.search_people_list)
    lateinit var mPeopleListView: ExpandableListView
    private var mAdapter: VectorParticipantsAdapter? = null

    // contacts manager listener
    // detect if a contact is a matrix user
    private val mContactsListener = object : ContactsManager.ContactsManagerListener {
        override fun onRefresh() {
            if (null != activity) {
                activity!!.runOnUiThread {
                    if (activity is VectorBaseSearchActivity.IVectorSearchActivity) {
                        (activity as VectorBaseSearchActivity.IVectorSearchActivity).refreshSearch()
                    }
                }
            }
        }

        override fun onContactPresenceUpdate(contact: Contact, matrixId: String) {}

        override fun onPIDsUpdate() {
            if (null != activity) {
                activity!!.runOnUiThread { mAdapter!!.onPIdsUpdate() }
            }
        }

        override fun onIdentityServerTermsNotSigned(token: String) {
            Log.w("VectorSearchPeopleListFragment", "onIdentityServerTermsNotSigned()")
        }

        override fun onNoIdentityServerDefined() {

        }
    }

    // refresh the presence asap
    private val mEventsListener = object : MXEventListener() {
        override fun onPresenceUpdate(event: Event?, user: User?) {
            if (null != activity) {
                activity!!.runOnUiThread {
                    val visibleChildViews = VectorUtils.getVisibleChildViews(mPeopleListView!!, mAdapter)

                    for (groupPosition in visibleChildViews.keys) {
                        val childPositions = visibleChildViews[groupPosition]

                        for (childPosition in childPositions!!) {
                            val item = mAdapter!!.getChild(groupPosition!!, childPosition!!)

                            if (item is ParticipantAdapterItem) {

                                if (TextUtils.equals(user!!.user_id, item.mUserId)) {
                                    mAdapter!!.notifyDataSetChanged()
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return true if the local search is ready to start.
     */
    val isReady: Boolean
        get() =
            ContactsManager.getInstance().didPopulateLocalContacts() && mAdapter!!.isKnownMembersInitialized

    override fun getLayoutResId(): Int {
        val args = arguments

        return args!!.getInt(ARG_LAYOUT_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments

        val matrixId = args!!.getString(ARG_MATRIX_ID)
        mSession = Matrix.getInstance(activity)!!.getSession(matrixId)

        if (null == mSession || !mSession!!.isAlive) {
            throw RuntimeException("Must have valid default MXSession.")
        }

        // the chevron is managed in the header view
        mPeopleListView!!.setGroupIndicator(null)
        mAdapter = VectorParticipantsAdapter(activity,
                R.layout.adapter_item_add_participants,
                R.layout.adapter_item_vector_people_header,
                mSession, null, false)
        mPeopleListView!!.setAdapter(mAdapter)

        mPeopleListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val child = mAdapter!!.getChild(groupPosition, childPosition)

            if (child is ParticipantAdapterItem && child.mIsValid) {

//                val startRoomInfoIntent = Intent(activity, VectorMemberDetailsActivity::class.java)
//                startRoomInfoIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, child.mUserId)
//
//                if (!TextUtils.isEmpty(child.mAvatarUrl)) {
//                    startRoomInfoIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_AVATAR_URL, child.mAvatarUrl)
//                }
//
//                if (!TextUtils.isEmpty(child.mDisplayName)) {
//                    startRoomInfoIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_DISPLAY_NAME, child.mDisplayName)
//                }

//                startRoomInfoIntent.putExtra(VectorMemberDetailsActivity.EXTRA_MATRIX_ID, mSession!!.credentials.userId)
//                startActivity(startRoomInfoIntent)
                val intent = Intent(activity, UserInformationActivity::class.java);
                intent.putExtra(UserInformationActivity.USER_ID, child.mUserId);
                startActivity(intent);
            }

            true
        }
    }

    /**
     * Search a pattern in the room
     *
     * @param pattern                the pattern to search
     * @param onSearchResultListener the result listener
     */
    fun searchPattern(pattern: String, onSearchResultListener: MatrixMessageListFragment.OnSearchResultListener) {
        if (null == mPeopleListView) {
            return
        }

        // wait that the local contacts are populated
        if (!ContactsManager.getInstance().didPopulateLocalContacts()) {
            mAdapter!!.reset()
            return
        }

        var firstEntry: ParticipantAdapterItem? = null
        if (!TextUtils.isEmpty(pattern)) {
            // test if the pattern is a valid email or matrix id
            val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(pattern).matches() || MXPatterns.isUserId(pattern)
            firstEntry = ParticipantAdapterItem(pattern, null, pattern, isValid)
        }

        mAdapter!!.setSearchedPattern(pattern, firstEntry, VectorParticipantsAdapter.OnParticipantsSearchListener { count ->
            if (!isAdded) {
                return@OnParticipantsSearchListener
            }

            mPeopleListView!!.post {
                mPeopleListView!!.visibility = if (count == 0 && !TextUtils.isEmpty(pattern)) View.INVISIBLE else View.VISIBLE
                onSearchResultListener.onSearchSucceed(count)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mSession!!.dataHandler.removeListener(mEventsListener)
        ContactsManager.getInstance().removeListener(mContactsListener)
    }

    override fun onResume() {
        super.onResume()
        mSession!!.dataHandler.addListener(mEventsListener)
        ContactsManager.getInstance().addListener(mContactsListener)
    }

    companion object {

        private val ARG_MATRIX_ID = "VectorSearchPeopleListFragment.ARG_MATRIX_ID"
        private val ARG_LAYOUT_ID = "VectorSearchPeopleListFragment.ARG_LAYOUT_ID"


        /**
         * Static constructor
         *
         * @param matrixId the matrix id
         * @return a VectorSearchPeopleListFragment instance
         */
        fun newInstance(matrixId: String, layoutResId: Int): SearchPeopleListFragment {
            val f = SearchPeopleListFragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutResId)
            args.putString(ARG_MATRIX_ID, matrixId)
            f.arguments = args
            return f
        }
    }
}
