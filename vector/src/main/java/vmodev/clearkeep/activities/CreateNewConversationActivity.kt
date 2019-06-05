package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.core.widget.toast
import im.vector.R
import im.vector.activity.*
import im.vector.activity.VectorRoomInviteMembersActivity.EXTRA_HIDDEN_PARTICIPANT_ITEMS
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorRoomCreationAdapter
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.CreateRoomParams
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.util.Log
import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

class CreateNewConversationActivity : MXCActionBarActivity() {
    private val LOG_TAG = CreateNewConversationActivity::class.java.simpleName

    // participants list
    private val PARTICIPANTS_LIST = "PARTICIPANTS_LIST"

    //
    private val INVITE_USER_REQUEST_CODE = 456

    // UI items
    private lateinit var membersListView: ListView;
    private var mAdapter: VectorRoomCreationAdapter? = null

    // the search is displayed at first call
    private var mIsFirstResume = true

    private val mCreateDirectMessageCallBack: ApiCallback<String> = object : ApiCallback<String> {
        override fun onSuccess(p0: String?) {
            val params = HashMap<String, Any>()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = p0!!
            params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true

            Log.d(LOG_TAG, "## mCreateDirectMessageCallBack: onSuccess - start goToRoomPage")
            CommonActivityUtils.goToRoomPage(this@CreateNewConversationActivity, mSession, params)
        }

        override fun onUnexpectedError(p0: Exception?) {
            onError(p0?.message);
        }

        override fun onMatrixError(p0: MatrixError?) {
            onError(p0?.message);
        }

        override fun onNetworkError(p0: Exception?) {
            onError(p0?.message);
        }

        private fun onError(message: String?) {
            membersListView?.post(Runnable {
                if (null != message) {
                    Toast.makeText(this@CreateNewConversationActivity, message, Toast.LENGTH_LONG).show()
                }
                hideWaitingView()
            })
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_create_new_conversation;
    }

    // displayed participants
    private var mParticipants: MutableList<ParticipantAdapterItem> = ArrayList()

    override fun initUiAndData() {
        super.initUiAndData()
        configureToolbar()
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "onCreate : Restart the application.")
            CommonActivityUtils.restartApp(this)
            return
        }

        val intent = intent

        mSession = getSession(intent)

        if (mSession == null) {
            Log.e(LOG_TAG, "No MXSession.")
            finish()
            return
        }

        // get the UI items
        waitingView = findViewById(R.id.room_creation_spinner_views)
        membersListView = findViewById(R.id.room_creation_members_list_view)
        mAdapter = VectorRoomCreationAdapter(this,
                R.layout.adapter_item_vector_creation_add_member, R.layout.adapter_item_vector_add_participants, mSession)

        // init the content
        if (!isFirstCreation() && getSavedInstanceState().containsKey(PARTICIPANTS_LIST)) {
            mParticipants.clear()
            mParticipants = ArrayList(getSavedInstanceState().getSerializable(PARTICIPANTS_LIST) as List<ParticipantAdapterItem>)
        } else {
            mParticipants.add(ParticipantAdapterItem(mSession.myUser))
        }
        mAdapter!!.addAll(mParticipants)

        membersListView.adapter = mAdapter
        mAdapter!!.setRoomCreationAdapterListener(VectorRoomCreationAdapter.IRoomCreationAdapterListener { item ->
            mParticipants.remove(item)
            mAdapter!!.remove(item)
        })

        membersListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            // the first one is "add a member"
            if (0 == position) {
                launchSearchActivity()
            }
        })
    }

    /***
     * Launch the people search activity
     */
    private fun launchSearchActivity() {
        val intent = Intent(this@CreateNewConversationActivity, RoomInviteMembersActivity::class.java)
        intent.putExtra(EXTRA_MATRIX_ID, mSession.myUserId)
        intent.putExtra(EXTRA_HIDDEN_PARTICIPANT_ITEMS, mParticipants as ArrayList<*>)
        startActivityForResult(intent, INVITE_USER_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        if (mIsFirstResume) {
            mIsFirstResume = false
            launchSearchActivity()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PARTICIPANTS_LIST, mParticipants as ArrayList<*>)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (null != savedInstanceState) {
            if (savedInstanceState.containsKey(PARTICIPANTS_LIST)) {
                mParticipants = ArrayList(savedInstanceState.getSerializable(PARTICIPANTS_LIST) as List<ParticipantAdapterItem>)
            } else {
                mParticipants.clear()
                mParticipants.add(ParticipantAdapterItem(mSession.myUser))
            }
            mAdapter?.clear()
            mAdapter?.addAll(mParticipants)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INVITE_USER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val items = data?.getSerializableExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS) as List<ParticipantAdapterItem>
                mParticipants.addAll(items)
                mAdapter?.addAll(items)
                mAdapter?.sort(mAlphaComparator)
            } else if (1 == mParticipants.size) {
                // the user cancels the first user selection so assume he wants to cancel the room creation.
                finish()
            }
        }
    }

    // Comparator to order members alphabetically
    // the self item is always kept at top
    private val mAlphaComparator = Comparator<ParticipantAdapterItem> { part1, part2 ->
        // keep the self user id at top
        if (TextUtils.equals(part1.mUserId, mSession.myUserId)) {
            return@Comparator -1
        }

        if (TextUtils.equals(part2.mUserId, mSession.myUserId)) {
            return@Comparator +1
        }

        val lhs = part1.comparisonDisplayName
        val rhs = part2.comparisonDisplayName

        if (lhs == null) {
            return@Comparator -1
        } else if (rhs == null) {
            return@Comparator 1
        }

        String.CASE_INSENSITIVE_ORDER.compare(lhs, rhs)
    }

    override fun getMenuRes(): Int {
        return R.menu.vector_room_creation;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return if (CommonActivityUtils.shouldRestartApp(this) || null == mSession) {
            false
        } else true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_room -> {
                if (mParticipants.isEmpty()) {
                    createRoom(mParticipants)
                } else {
                    // the first entry is self so ignore
                    mParticipants.removeAt(0)

                    if (mParticipants.isEmpty()) {
                        // standalone case : should be accepted ?
                        createRoom(mParticipants)
                    } else if (mParticipants.size > 1) {
                        createRoom(mParticipants)
                    } else {
                        // 1 other participant
                        openOrCreateDirectChatRoom(mParticipants[0].mUserId)
                    }
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openOrCreateDirectChatRoom(otherUserId: String) {
        doesDirectChatRoomAlreadyExist(otherUserId, object : ApiCallback<String> {
            override fun onSuccess(existingRoomId: String?) {
                if (null != existingRoomId) {
                    val params = HashMap<String, Any>()
                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = otherUserId
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = existingRoomId
                    CommonActivityUtils.goToRoomPage(this@CreateNewConversationActivity, mSession, params)
                } else {
                    // direct message flow
                    showWaitingView()
                    mSession.createDirectMessageRoom(otherUserId, mCreateDirectMessageCallBack)
                }
            }

            override fun onNetworkError(e: Exception) {
                this@CreateNewConversationActivity.toast(e.localizedMessage, Toast.LENGTH_SHORT)
            }

            override fun onMatrixError(e: MatrixError) {
                this@CreateNewConversationActivity.toast(e.localizedMessage, Toast.LENGTH_SHORT)
            }

            override fun onUnexpectedError(e: Exception) {
                this@CreateNewConversationActivity.toast(e.localizedMessage, Toast.LENGTH_SHORT)
            }
        })
    }

    /**
     * Return the first direct chat room for a given user ID.
     *
     * @param aUserId  user ID to search for
     * @param callback callback to return a room ID if search succeed, null otherwise.
     */
    private fun doesDirectChatRoomAlreadyExist(aUserId: String, callback: ApiCallback<String>) {
        if (null != mSession) {
            val store = mSession.dataHandler.store

            val directChatRoomsDict: Map<String, List<String>>

            if (null != store.directChatRoomsDict) {
                directChatRoomsDict = HashMap(store.directChatRoomsDict)

                if (directChatRoomsDict.containsKey(aUserId)) {
                    val roomIdsList = ArrayList(directChatRoomsDict[aUserId]!!)

                    doesDirectChatRoomAlreadyExistRecursive(roomIdsList, 0, aUserId, callback)
                } else {
                    callback.onSuccess(null)
                }
            } else {
                callback.onSuccess(null)
            }
        } else {
            callback.onSuccess(null)
        }
    }

    private fun doesDirectChatRoomAlreadyExistRecursive(roomIdsList: List<String>,
                                                        index: Int,
                                                        aUserId: String,
                                                        callback: ApiCallback<String>) {
        if (index >= roomIdsList.size) {
            Log.d(LOG_TAG, "## doesDirectChatRoomAlreadyExist(): for user=$aUserId no found room")
            callback.onSuccess(null)
        } else {
            val room = mSession.dataHandler.getRoom(roomIdsList[index], false)

            // check if the room is already initialized
            if (room != null && room.isReady && !room.isInvited && !room.isLeaving) {
                room.getActiveMembersAsync(object : SimpleApiCallback<List<RoomMember>>(callback) {
                    override fun onSuccess(members: List<RoomMember>) {
                        // test if the member did not leave the room
                        for (member in members) {
                            if (TextUtils.equals(member.userId, aUserId)) {
                                Log.d("Log Tag: ", "## doesDirectChatRoomAlreadyExist(): for user=" + aUserId + " roomFound=" + roomIdsList[index])
                                callback.onSuccess(roomIdsList[index])
                                return
                            }
                        }

                        // Try next one
                        doesDirectChatRoomAlreadyExistRecursive(roomIdsList, index + 1, aUserId, callback)
                    }
                })
            } else {
                // Try next one
                doesDirectChatRoomAlreadyExistRecursive(roomIdsList, index + 1, aUserId, callback)
            }
        }
    }

    /**
     * Create a room with a list of participants.
     *
     * @param participants the list of participant
     */
    private fun createRoom(participants: List<ParticipantAdapterItem>) {
        showWaitingView()

        val params = CreateRoomParams()

        val ids = ArrayList<String>()
        for (item in participants) {
            if (null != item.mUserId) {
                ids.add(item.mUserId)
            }
        }

        params.addParticipantIds(mSession.homeServerConfig, ids)

        mSession.createRoom(params, object : ApiCallback<String> {
            override fun onSuccess(roomId: String) {
                runOnUiThread {
                    val params = HashMap<String, Any>()
                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession.myUserId
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId
                    CommonActivityUtils.goToRoomPage(this@CreateNewConversationActivity, mSession, params)
                }
            }

            private fun onError(message: String?) {
                membersListView?.post(Runnable {
                    if (null != message) {
                        Toast.makeText(this@CreateNewConversationActivity, message, Toast.LENGTH_LONG).show()
                    }
                    hideWaitingView()
                })
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                    hideWaitingView()

                    consentNotGivenHelper.displayDialog(e)
                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }
}
