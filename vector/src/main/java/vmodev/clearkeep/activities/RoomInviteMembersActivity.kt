package vmodev.clearkeep.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.activity.VectorBaseSearchActivity
import im.vector.activity.VectorRoomInviteMembersActivity
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorParticipantsAdapter
import im.vector.contacts.Contact
import im.vector.contacts.ContactsManager
import im.vector.util.PERMISSIONS_FOR_MEMBERS_SEARCH
import im.vector.util.PERMISSION_REQUEST_CODE
import im.vector.util.VectorUtils
import im.vector.util.checkPermissions
import im.vector.view.VectorAutoCompleteTextView
import org.matrix.androidsdk.MXPatterns
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.User
import org.matrix.androidsdk.util.Log
import java.util.*
import java.util.regex.Pattern

class RoomInviteMembersActivity : VectorBaseSearchActivity() {
    private val LOG_TAG = RoomInviteMembersActivity::class.java.simpleName

    // room identifier
    val EXTRA_ROOM_ID = "VectorInviteMembersActivity.EXTRA_ROOM_ID"

    // participants to hide in the list
    val EXTRA_HIDDEN_PARTICIPANT_ITEMS = "VectorInviteMembersActivity.EXTRA_HIDDEN_PARTICIPANT_ITEMS"

    // boolean : true displays a dialog to confirm the member selection
    val EXTRA_ADD_CONFIRMATION_DIALOG = "VectorInviteMembersActivity.EXTRA_ADD_CONFIRMATION_DIALOG"

    // the selected user ids list
    val EXTRA_OUT_SELECTED_USER_IDS = "VectorInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS"

    // the selected participants list
    val EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS = "VectorInviteMembersActivity.EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS"

    // account data
    private var mMatrixId: String? = null
    // main UI items
    @BindView(R.id.room_details_members_list)
    lateinit var mListView: ExpandableListView

    // participants list
    private var mHiddenParticipantItems: List<ParticipantAdapterItem> = ArrayList()

    // adapter
    private lateinit var mAdapter: VectorParticipantsAdapter;

    // tell if a confirmation dialog must be displayed to validate the user ids list
    private var mAddConfirmationDialog: Boolean = false

    private val mContactsListener = object : ContactsManager.ContactsManagerListener {
        override fun onRefresh() {
            runOnUiThread { onPatternUpdate(false) }
        }

        override fun onContactPresenceUpdate(contact: Contact, matrixId: String) {}

        override fun onPIDsUpdate() {
            runOnUiThread { mAdapter.onPIdsUpdate() }
        }
    }
    // refresh the presence asap
    private val mEventsListener = object : MXEventListener() {
        override fun onPresenceUpdate(event: Event?, user: User?) {
            runOnUiThread {
                val visibleChildViews = VectorUtils.getVisibleChildViews(mListView, mAdapter)

                for (groupPosition in visibleChildViews.keys) {
                    val childPositions = visibleChildViews[groupPosition]

                    for (childPosition in childPositions!!) {
                        val item = mAdapter.getChild(groupPosition!!, childPosition!!)

                        if (item is ParticipantAdapterItem) {

                            if (TextUtils.equals(user!!.user_id, item.mUserId)) {
                                mAdapter.notifyDataSetChanged()
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_room_invite_members;
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

        val intent = intent

        if (intent.hasExtra(MXCActionBarActivity.EXTRA_MATRIX_ID)) {
            mMatrixId = intent.getStringExtra(MXCActionBarActivity.EXTRA_MATRIX_ID)
        }

        // get current session
        mSession = Matrix.getInstance(applicationContext)!!.getSession(mMatrixId)

        if (null == mSession || !mSession.isAlive) {
            finish()
            return
        }

        if (intent.hasExtra(EXTRA_HIDDEN_PARTICIPANT_ITEMS)) {
            mHiddenParticipantItems = intent.getSerializableExtra(EXTRA_HIDDEN_PARTICIPANT_ITEMS) as List<ParticipantAdapterItem>
        }

        val roomId = intent.getStringExtra(EXTRA_ROOM_ID)

        if (null != roomId) {
            mRoom = mSession.dataHandler.store.getRoom(roomId)
        }

        // tell if a confirmation dialog must be displayed.
        mAddConfirmationDialog = intent.getBooleanExtra(EXTRA_ADD_CONFIRMATION_DIALOG, false)

        // the user defines a
        if (null != mPatternToSearchEditText) {
            mPatternToSearchEditText.setHint(R.string.room_participants_invite_search_another_user)
        }

        waitingView = findViewById(R.id.search_in_progress_view)

        // the chevron is managed in the header view
        mListView?.setGroupIndicator(null)

        mAdapter = VectorParticipantsAdapter(this,
                R.layout.adapter_item_vector_add_participants,
                R.layout.adapter_item_vector_people_header,
                mSession, roomId, true)
        mAdapter.setHiddenParticipantItems(mHiddenParticipantItems)
        mListView?.setAdapter(mAdapter)

        mListView?.setOnChildClickListener(ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val item = mAdapter.getChild(groupPosition, childPosition)

            if (item is ParticipantAdapterItem && item.mIsValid) {
                finish(ArrayList(Arrays.asList(item)))
                return@OnChildClickListener true
            }
            false
        })

        // Check permission to access contacts
        checkPermissions(PERMISSIONS_FOR_MEMBERS_SEARCH, this, PERMISSION_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        mSession.dataHandler.addListener(mEventsListener)
        ContactsManager.getInstance().addListener(mContactsListener)
    }

    override fun onPause() {
        super.onPause()
        mSession.dataHandler.removeListener(mEventsListener)
        ContactsManager.getInstance().removeListener(mContactsListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (0 == permissions.size) {
            Log.d(LOG_TAG, "## onRequestPermissionsResult(): cancelled $requestCode")
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission granted")
                ContactsManager.getInstance().refreshLocalContactsSnapshot()
                onPatternUpdate(false)
            } else {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission not granted")
                Toast.makeText(this, R.string.missing_permissions_warning, Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * The search pattern has been updated
     */
    protected override fun onPatternUpdate(isTypingUpdate: Boolean) {
        val pattern = mPatternToSearchEditText.text.toString()

        // display a spinner while the other room members are listed
        if (!mAdapter.isKnownMembersInitialized) {
            showWaitingView()
        }

        // wait that the local contacts are populated
        if (!ContactsManager.getInstance().didPopulateLocalContacts()) {
            Log.d(LOG_TAG, "## onPatternUpdate() : The local contacts are not yet populated")
            mAdapter.reset()
            showWaitingView()
            return
        }

        mAdapter.setSearchedPattern(pattern, null, VectorParticipantsAdapter.OnParticipantsSearchListener {
            if (mListView == null) {
                // Activity is dead
                return@OnParticipantsSearchListener
            }

            mListView!!.post(Runnable { hideWaitingView() })
        })
    }
    /**
     * Display a selection confirmation dialog.
     *
     * @param participantAdapterItems the selected participants
     */
    private fun finish(participantAdapterItems: List<ParticipantAdapterItem>) {
        val hiddenUserIds = ArrayList<String>()

        // list the hidden user Ids
        for (item in mHiddenParticipantItems) {
            hiddenUserIds.add(item.mUserId)
        }

        // if a room is defined
        if (null != mRoom) {
            // the room members must not be added again
            mRoom.getDisplayableMembersAsync(object : SimpleApiCallback<List<RoomMember>>() {
                override fun onSuccess(members: List<RoomMember>) {
                    for (member in members) {
                        if (TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_JOIN) || TextUtils.equals(member.membership, RoomMember.MEMBERSHIP_INVITE)) {
                            hiddenUserIds.add(member.userId)
                        }
                    }

                    finishStep2(participantAdapterItems, hiddenUserIds)
                }
            })
        } else {
            finishStep2(participantAdapterItems, hiddenUserIds)
        }
    }
    private fun finishStep2(participantAdapterItems: List<ParticipantAdapterItem>, hiddenUserIds: List<String>) {
        val userIds = ArrayList<String>()
        val displayNames = ArrayList<String>()

        // build the output lists
        for (item in participantAdapterItems) {
            // check if the user id can be added
            if (!hiddenUserIds.contains(item.mUserId)) {
                userIds.add(item.mUserId)
                // display name
                if (MXPatterns.isUserId(item.mUserId)) {
                    val user = mSession.dataHandler.store.getUser(item.mUserId)
                    if (null != user && !TextUtils.isEmpty(user.displayname)) {
                        displayNames.add(user.displayname)
                    } else {
                        displayNames.add(item.mUserId)
                    }
                } else {
                    displayNames.add(item.mUserId)
                }
            }
        }

        // a confirmation dialog has been requested
        if (mAddConfirmationDialog && displayNames.size > 0) {
            val builder = AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_confirmation)

            var message = ""
            var msgPartA = ""
            var msgPartB = ""

            if (displayNames.size == 1) {
                message = displayNames[0]
            } else {
                for (i in 0 until displayNames.size - 2) {
                    msgPartA += getString(R.string.room_participants_invite_join_names, displayNames[i])
                }

                msgPartB = getString(R.string.room_participants_invite_join_names_and,
                        displayNames[displayNames.size - 2],
                        displayNames[displayNames.size - 1])
                message = getString(R.string.room_participants_invite_join_names_combined,
                        msgPartA, msgPartB)
            }

            builder.setMessage(getString(R.string.room_participants_invite_prompt_msg, message))
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        // returns the selected users
                        val intent = Intent()
                        intent.putExtra(EXTRA_OUT_SELECTED_USER_IDS, userIds as ArrayList<*>)
                        intent.putExtra(EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS, participantAdapterItems as ArrayList<*>)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        } else {
            // returns the selected users
            val intent = Intent()
            intent.putExtra(EXTRA_OUT_SELECTED_USER_IDS, userIds as ArrayList<*>)
            intent.putExtra(EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS, participantAdapterItems as ArrayList<*>)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    /**
     * Display the invitation dialog.
     */
    @OnClick(R.id.search_invite_by_id)
    internal fun displayInviteByUserId() {
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_invite_by_id, null)

        val builder = AlertDialog.Builder(this)
                .setTitle(R.string.people_search_invite_by_id_dialog_title)
                .setView(dialogLayout)

        val inviteTextView = dialogLayout.findViewById<VectorAutoCompleteTextView>(R.id.invite_by_id_edit_text)
        inviteTextView.initAutoCompletion(mSession)
        inviteTextView.setProvideMatrixIdOnly(true)

        val inviteDialog = builder
                .setPositiveButton(R.string.invite) { dialog, which ->
                    // will be overridden to avoid dismissing the dialog while displaying the progress
                }
                .setNegativeButton(R.string.cancel, null)
                .show()

        val inviteButton = inviteDialog.getButton(AlertDialog.BUTTON_POSITIVE)

        if (null != inviteButton) {
            inviteButton.isEnabled = false

            inviteButton.setOnClickListener {
                val text = inviteTextView.text.toString()
                val items = ArrayList<ParticipantAdapterItem>()
                val patterns = Arrays.asList(MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER, android.util.Patterns.EMAIL_ADDRESS)

                for (pattern in patterns) {
                    val matcher = pattern.matcher(text)
                    while (matcher.find()) {
                        try {
                            val userId = text.substring(matcher.start(0), matcher.end(0))
                            items.add(ParticipantAdapterItem(userId, null, userId, true))
                        } catch (e: Exception) {
                            Log.e(LOG_TAG, "## displayInviteByUserId() " + e.message, e)
                        }

                    }
                }

                finish(items)

                inviteDialog.dismiss()
            }
        }

        inviteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (null != inviteButton) {
                    val text = inviteTextView.text.toString()

                    val containMXID = MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(text).find()
                    val containEmailAddress = android.util.Patterns.EMAIL_ADDRESS.matcher(text).find()

                    inviteButton.isEnabled = containMXID || containEmailAddress
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}

