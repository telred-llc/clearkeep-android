package vmodev.clearkeep.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import im.vector.Matrix
import im.vector.PublicRoomsManager
import im.vector.R
import im.vector.adapters.AdapterUtils
import im.vector.settings.VectorLocale
import im.vector.ui.themes.ThemeUtils
import im.vector.util.RiotEventDisplay
import im.vector.util.RoomUtils
import im.vector.util.VectorUtils
import im.vector.util.setRoundBackground
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.EventDisplay
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomSummary
import org.matrix.androidsdk.data.RoomTag
import org.matrix.androidsdk.rest.model.Event
import java.util.*
import kotlin.collections.ArrayList

class RoomSummaryAdapter
/**
 * Constructor
 *
 * @param aContext                       the context.
 * @param session                        the linked session.
 * @param isSearchMode                   true if the adapter is in search mode
 * @param displayDirectoryGroupWhenEmpty true to avoid empty history
 * @param aChildLayoutResourceId         the room child layout
 * @param aGroupHeaderLayoutResourceId   the room section header layout
 * @param listener                       the events listener
 */
(private val mContext: Context, private val mMxSession: MXSession?, // search mode set to true : display nothing if the search pattern is empty
        // search mode set to false : display all the known entries if the search pattern is empty
 private val mIsSearchMode: Boolean,
        // when set to true, avoid empty history by displaying the directory group
 private val mDisplayDirectoryGroupWhenEmpty: Boolean, private val mChildLayoutResourceId: Int,
 private val mHeaderLayoutResourceId: Int, private val mListener: RoomEventListener?, // the listener
 private val mMoreActionListener: RoomUtils.MoreActionListener) : BaseExpandableListAdapter() {
    private val mLayoutInflater: LayoutInflater
    private var mSummaryListByGroupPosition: ArrayList<ArrayList<RoomSummary>>? = null

    private var mRoomByAliasGroupPosition = -1 // the user wants to join  by room id or alias
    /**
     * @return the directory group position
     */
    var directoryGroupPosition = -1
        private set  // public rooms index
    private var mInvitedGroupPosition = -1  // "Invited" index
    private var mFavouritesGroupPosition = -1// "Favourites" index
    private var mNoTagGroupPosition = -1    // "Rooms" index
    private var mLowPriorGroupPosition = -1  // "Low Priority" index

    private val DBG_CLASS_NAME: String

    // search mode
    /**
     * @return the searched pattern
     */
    var searchedPattern: String? = null
        private set
    // force to display the directory group
    private var mForceDirectoryGroupDisplay: Boolean = false

    // public room search
    private var mPublicRoomsCount: Int = 0;
    private var mMatchedPublicRoomsCount: Int = 0;

    // drag and drop mode
    /**
     * @return true if the adapter is in drag and drop mode.
     */
    var isInDragAndDropMode = false
        private set

    /**
     * @return true if the directory group is displayed
     */
    val isDirectoryGroupDisplayed: Boolean
        get() = -1 != directoryGroupPosition

    /**
     * @return the matched public rooms count
     */
    var matchedPublicRoomsCount: Int = 0

    interface RoomEventListener {
        fun onPreviewRoom(session: MXSession?, roomId: String)

        fun onRejectInvitation(session: MXSession?, roomId: String)

        fun onGroupCollapsedNotif(aGroupPosition: Int)

        fun onGroupExpandedNotif(aGroupPosition: Int)
    }

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
        DBG_CLASS_NAME = javaClass.name
    }// init internal fields
    // get the complete summary list

    /**
     * Set to true to always display the directory group.
     *
     * @param forceDirectoryGroupDisplay true to always display the directory group.
     */
    fun setForceDirectoryGroupDisplay(forceDirectoryGroupDisplay: Boolean) {
        mForceDirectoryGroupDisplay = forceDirectoryGroupDisplay
    }

    /**
     * Provides the formatted timestamp to display.
     * null means that the timestamp text must be hidden.
     *
     * @param event the event.
     * @return the formatted timestamp to display.
     */
    private fun getFormattedTimestamp(event: Event): String {
        var text = AdapterUtils.tsToString(mContext, event.getOriginServerTs(), false)

        // don't display the today before the time
        val today = mContext.getString(R.string.today) + " "
        if (text.startsWith(today)) {
            text = text.substring(today.length)
        }

        return text
    }

    /**
     * Compute the name of the group according to its position.
     *
     * @param groupPosition index of the section
     * @return group title corresponding to the index
     */
    private fun getGroupTitle(groupPosition: Int): String {
        val retValue: String

        if (mRoomByAliasGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_join)
        } else if (directoryGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_directory)
        } else if (mFavouritesGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_favourites)
        } else if (mNoTagGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_conversations)
        } else if (mLowPriorGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_low_priority)
        } else if (mInvitedGroupPosition == groupPosition) {
            retValue = mContext.getString(R.string.room_recents_invites)
        } else {
            // unknown section
            retValue = "??"
        }

        return retValue
    }

    /**
     * Fullfill an array list with a pattern.
     *
     * @param list  the list to fill.
     * @param value the pattern.
     * @param count the number of occurences
     */
    private fun fillList(list: MutableList<RoomSummary>, value: RoomSummary, count: Int) {
        for (i in 0 until count) {
            list.add(value)
        }
    }

    /**
     * Check a room name contains the searched pattern.
     *
     * @param room the room.
     * @return true of the pattern is found.
     */
    private fun isMatchedPattern(room: Room): Boolean {
        var res = !mIsSearchMode

        if (!TextUtils.isEmpty(searchedPattern)) {
            val roomName = room.getRoomDisplayName(mContext)
            res = !TextUtils.isEmpty(roomName) && roomName.toLowerCase(VectorLocale.applicationLocale).contains(searchedPattern!!)
        }

        return res
    }

    /**
     * Tell if the group position is the join by
     *
     * @param groupPosition the group position to test.
     * @return true if it is room id group
     */
    fun isRoomByIdGroupPosition(groupPosition: Int): Boolean {
        return mRoomByAliasGroupPosition == groupPosition
    }

    /**
     * Test if the group position is the directory one.
     *
     * @param groupPosition the group position to test.
     * @return true if it is directory group.
     */
    fun isDirectoryGroupPosition(groupPosition: Int): Boolean {
        return directoryGroupPosition == groupPosition
    }

    override fun onGroupCollapsed(groupPosition: Int) {
        super.onGroupCollapsed(groupPosition)
        mListener?.onGroupCollapsedNotif(groupPosition)
    }

    override fun onGroupExpanded(groupPosition: Int) {
        super.onGroupExpanded(groupPosition)
        mListener?.onGroupExpandedNotif(groupPosition)
    }

    /**
     * Build an array of RoomSummary objects organized according to the room tags (sections).
     * So far we have 4 sections
     * - the invited rooms
     * - the rooms with tags ROOM_TAG_FAVOURITE
     * - the rooms with tags ROOM_TAG_LOW_PRIORITY
     * - the rooms with tags ROOM_TAG_NO_TAG (displayed as "ROOMS")
     * The section indexes: mFavouriteSectionIndex, mNoTagSectionIndex and mFavouriteSectionIndex are
     * also computed in this method.
     *
     * @param aRoomSummaryCollection the complete list of RoomSummary objects
     * @return an array of summary lists splitted by sections
     */
    private fun buildSummariesByGroups(aRoomSummaryCollection: Collection<RoomSummary>?): ArrayList<ArrayList<RoomSummary>> {
        val summaryListByGroupsRetValue = ArrayList<ArrayList<RoomSummary>>()
        var roomSummaryId: String

        // init index with default values
        mRoomByAliasGroupPosition = -1
        directoryGroupPosition = -1
        mInvitedGroupPosition = -1
        mFavouritesGroupPosition = -1
        mNoTagGroupPosition = -1
        mLowPriorGroupPosition = -1

        if (null != aRoomSummaryCollection) {

            val dummyRoomSummary = RoomSummary()

            // Retrieve lists of room IDs(strings) according to their tags
            val favouriteRoomIdList = mMxSession!!.roomIdsWithTag(RoomTag.ROOM_TAG_FAVOURITE)
            val lowPriorityRoomIdList = mMxSession.roomIdsWithTag(RoomTag.ROOM_TAG_LOW_PRIORITY)

            // ArrayLists allocations: will contain the RoomSummary objects deduced from roomIdsWithTag()
            val inviteRoomSummaryList = ArrayList<RoomSummary>()
            val favouriteRoomSummaryList = ArrayList<RoomSummary>(favouriteRoomIdList.size)
            val lowPriorityRoomSummaryList = ArrayList<RoomSummary>()
            val noTagRoomSummaryList = ArrayList<RoomSummary>(lowPriorityRoomIdList.size)

            fillList(favouriteRoomSummaryList, dummyRoomSummary, favouriteRoomIdList.size)
            fillList(lowPriorityRoomSummaryList, dummyRoomSummary, lowPriorityRoomIdList.size)

            // Search loop going through all the summaries:
            // here we translate the roomIds (Strings) to their corresponding RoomSummary objects
            for (roomSummary in aRoomSummaryCollection) {
                roomSummaryId = roomSummary.roomId
                val room = mMxSession.dataHandler.store.getRoom(roomSummaryId)

                // check if the room exists
                // the user conference rooms are not displayed.
                if (null != room && isMatchedPattern(room) && !room.isConferenceUserRoom) {
                    // list first the summary
                    if (room.isInvited) {
                        inviteRoomSummaryList.add(roomSummary)
                    } else {
                        var pos: Int

                        // search for each room Id in the room Id lists, retrieved from their corresponding tags
                        pos = favouriteRoomIdList.indexOf(roomSummaryId)
                        if (pos >= 0) {
                            // update the favourites list
                            // the favorites are ordered
                            favouriteRoomSummaryList[pos] = roomSummary
                        } else if ((lowPriorityRoomIdList.indexOf(roomSummaryId)) >= 0) {
                            pos = lowPriorityRoomIdList.indexOf(roomSummaryId)
                            // update the low priority list
                            // the low priority are ordered
                            lowPriorityRoomSummaryList[pos] = roomSummary
                        } else {
                            // default case: update the no tag list
                            noTagRoomSummaryList.add(roomSummary)
                        }
                    }
                } else if (null == room) {
                    Log.e(DBG_CLASS_NAME, "buildSummariesBySections $roomSummaryId has no known room")
                }
            }

            // Adding sections
            // Note the order here below: first the "invitations",  "favourite", then "no tag" and then "low priority"
            var groupIndex = 0

            // in search mode
            // the public rooms have a dedicated section
            if (mIsSearchMode || mDisplayDirectoryGroupWhenEmpty || mForceDirectoryGroupDisplay) {

                // detect if the pattern might a room ID or an alias
                if (!TextUtils.isEmpty(searchedPattern)) {
                    // a room id is !XXX:server.ext
                    // a room alias is #XXX:server.ext

                    var isRoomId = false
                    var isRoomAlias = false

                    if (searchedPattern!!.startsWith("!")) {
                        var sep = searchedPattern!!.indexOf(":")

                        if (sep > 0) {
                            sep = searchedPattern!!.indexOf(".", sep)
                        }

                        isRoomId = sep > 0
                    } else if (searchedPattern!!.startsWith("#")) {
                        var sep = searchedPattern!!.indexOf(":")

                        if (sep > 0) {
                            sep = searchedPattern!!.indexOf(".", sep)
                        }

                        isRoomAlias = sep > 0
                    }

                    if (isRoomId || isRoomAlias) {
                        mRoomByAliasGroupPosition = groupIndex++
                    }
                }

                directoryGroupPosition = groupIndex++
                // create a dummy entry to keep match between section index <-> summaries list
                summaryListByGroupsRetValue.add(ArrayList())
            }

            // first the invitations
            if (0 != inviteRoomSummaryList.size) {
                // the invitations are sorted from the older to the oldest to the more recent ones
                Collections.reverse(inviteRoomSummaryList)
                summaryListByGroupsRetValue.add(inviteRoomSummaryList)
                mInvitedGroupPosition = groupIndex
                groupIndex++
            }

            // favourite
            while (favouriteRoomSummaryList.remove(dummyRoomSummary));
            if (0 != favouriteRoomSummaryList.size) {
                summaryListByGroupsRetValue.add(favouriteRoomSummaryList)
                mFavouritesGroupPosition = groupIndex // save section index
                groupIndex++
            }

            // no tag
            if (0 != noTagRoomSummaryList.size) {
                summaryListByGroupsRetValue.add(noTagRoomSummaryList)
                mNoTagGroupPosition = groupIndex // save section index
                groupIndex++
            }

            // low priority
            while (lowPriorityRoomSummaryList.remove(dummyRoomSummary));
            if (0 != lowPriorityRoomSummaryList.size) {
                summaryListByGroupsRetValue.add(lowPriorityRoomSummaryList)
                mLowPriorGroupPosition = groupIndex // save section index
                groupIndex++
            }

            // in avoiding empty history mode
            // check if there is really nothing else
            if (mDisplayDirectoryGroupWhenEmpty && !mForceDirectoryGroupDisplay && groupIndex > 1) {
                summaryListByGroupsRetValue.removeAt(directoryGroupPosition)
                mRoomByAliasGroupPosition = -1
                directoryGroupPosition = -1
                mInvitedGroupPosition--
                mFavouritesGroupPosition--
                mNoTagGroupPosition--
                mLowPriorGroupPosition--
            }
        }

        return summaryListByGroupsRetValue
    }

    /**
     * Return the summary
     *
     * @param aGroupPosition group position
     * @param aChildPosition child position
     * @return the corresponding room summary
     */
    fun getRoomSummaryAt(aGroupPosition: Int, aChildPosition: Int): RoomSummary? {
        return mSummaryListByGroupPosition!![aGroupPosition][aChildPosition]
    }

    /**
     * Reset the count of the unread messages of the room set at this particular child position.
     *
     * @param aGroupPosition group position
     * @param aChildPosition child position
     * @return true if unread count reset was effective, false if unread count was yet reseted
     */
    fun resetUnreadCount(aGroupPosition: Int, aChildPosition: Int): Boolean {
        val retCode = false
        val roomSummary = getRoomSummaryAt(aGroupPosition, aChildPosition)

        if (null != roomSummary) {
            val room = roomFromRoomSummary(roomSummary)
            room?.sendReadReceipt()
        }

        return retCode
    }

    /**
     * Retrieve a Room from a room summary
     *
     * @param roomSummary the room roomId to retrieve.
     * @return the Room.
     */
    private fun roomFromRoomSummary(roomSummary: RoomSummary?): Room? {
        var roomRetValue: Room?
        var session: MXSession
        var userId: String = ""

        // sanity check
        if (null == roomSummary || null == (roomSummary.userId)) {
            roomRetValue = null
        } else {
            if (roomSummary.userId.isNullOrEmpty()) {
                roomRetValue = null;
            } else {
                userId = roomSummary.userId
                session = Matrix.getMXSession(mContext, userId)
                if (null == session || !session.isAlive) {
                    roomRetValue = null
                } else {
                    roomRetValue = session.dataHandler.store.getRoom(roomSummary.roomId)
                }// get session and check if the session is active
            }
        }
        return roomRetValue
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun refreshSummariesList() {
        if (null != mMxSession) {
            // sanity check
            val dataHandler = mMxSession.dataHandler
            if (null == dataHandler || null == dataHandler.store) {
                Log.w(DBG_CLASS_NAME, "## refreshSummariesList(): unexpected null values - return")
                return
            }

            // update/retrieve the complete summary list
            val roomSummariesCompleteList = ArrayList(dataHandler.store.summaries)

            // define comparator logic
            val summaryComparator = Comparator<RoomSummary> { aLeftObj, aRightObj ->
                val retValue: Int
                val deltaTimestamp: Long

                if (null == aLeftObj || null == aLeftObj.latestReceivedEvent) {
                    retValue = 1
                } else if (null == aRightObj || null == aRightObj.latestReceivedEvent) {
                    retValue = -1
                } else {
                    deltaTimestamp = aRightObj.latestReceivedEvent.getOriginServerTs() - aLeftObj.latestReceivedEvent.getOriginServerTs()

                    if (deltaTimestamp > 0) {
                        retValue = 1
                    } else if (deltaTimestamp < 0) {
                        retValue = -1
                    } else {
                        retValue = 0
                    }
                }

                retValue
            }

            Collections.sort(roomSummariesCompleteList, summaryComparator)

            // init data model used to be be displayed in the list view
            mSummaryListByGroupPosition = buildSummariesByGroups(roomSummariesCompleteList)
        }
    }

    override fun notifyDataSetChanged() {
        if (!isInDragAndDropMode) {
            refreshSummariesList()
        }
        super.notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return if (null != mSummaryListByGroupPosition) {
            mSummaryListByGroupPosition!!.size
        } else 0

    }

    override fun getGroup(groupPosition: Int): Any {
        return getGroupTitle(groupPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return getGroupTitle(groupPosition).hashCode().toLong()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        // the directory section has always only one entry
        // same for the join by room alias or ID
        return if (directoryGroupPosition == groupPosition || mRoomByAliasGroupPosition == groupPosition) {
            1
        } else mSummaryListByGroupPosition!![groupPosition].size

    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return null
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0L
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(mHeaderLayoutResourceId, null)
        }

        val sectionNameTxtView = convertView!!.findViewById<TextView>(R.id.heading)

        if (null != sectionNameTxtView) {
            sectionNameTxtView.text = getGroupTitle(groupPosition)
        }

        val imageView = convertView.findViewById<ImageView>(R.id.heading_image)

        if (mIsSearchMode) {
            imageView.visibility = View.GONE
        } else {
            val expandLogoRes = if (isExpanded) R.drawable.ic_material_expand_more_black else R.drawable.ic_material_expand_less_black
            imageView.setImageResource(expandLogoRes)
        }
        return convertView
    }

    /**
     * Compute the View that should be used to render the child,
     * given its position and its group’s position
     */
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // sanity check
        if (null == mSummaryListByGroupPosition) {
            return null
        }
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mChildLayoutResourceId, parent, false)
        }

        if (!mMxSession!!.isAlive) {
            return convertView
        }

        val roomNameBlack = ThemeUtils.getColor(mContext, android.R.attr.textColorTertiary)
        val fushiaColor = ContextCompat.getColor(mContext, R.color.vector_fuchsia_color)
        val vectorDefaultTimeStampColor = ThemeUtils.getColor(mContext, android.R.attr.textColorSecondary)
        val vectorAccentColor = ThemeUtils.getColor(mContext, R.attr.colorAccent)
        val vectorSilverColor = ContextCompat.getColor(mContext, R.color.vector_silver_color)

        // retrieve the UI items
        val avatarImageView = convertView!!.findViewById<ImageView>(R.id.adapter_item_recent_room_avatar)
        val roomNameTxtView = convertView.findViewById<TextView>(R.id.roomSummaryAdapter_roomName)
        val roomMsgTxtView = convertView.findViewById<TextView>(R.id.roomSummaryAdapter_roomMessage)
        val bingUnreadMsgView = convertView.findViewById<View>(R.id.bing_indicator_unread_message)
        val timestampTxtView = convertView.findViewById<TextView>(R.id.roomSummaryAdapter_ts)
        val separatorView = convertView.findViewById<View>(R.id.recents_separator)
        val separatorGroupView = convertView.findViewById<View>(R.id.recents_groups_separator_line)
        val actionView = convertView.findViewById<View>(R.id.roomSummaryAdapter_action)
        val actionImageView = convertView.findViewById<ImageView>(R.id.roomSummaryAdapter_action_image)
        val unreadCountTxtView = convertView.findViewById<TextView>(R.id.roomSummaryAdapter_unread_count)
        val directChatIcon = convertView.findViewById<View>(R.id.room_avatar_direct_chat_icon)
        val encryptedIcon = convertView.findViewById<View>(R.id.room_avatar_encrypted_icon)

        val invitationView = convertView.findViewById<View>(R.id.recents_groups_invitation_group)
        val preViewButton = convertView.findViewById<Button>(R.id.recents_invite_preview_button)
        val rejectButton = convertView.findViewById<Button>(R.id.recents_invite_reject_button)

        val showMoreView = convertView.findViewById<View>(R.id.roomSummaryAdapter_show_more_layout)
        val actionClickArea = convertView.findViewById<View>(R.id.roomSummaryAdapter_action_click_area)

        // directory management
        if (directoryGroupPosition == groupPosition || mRoomByAliasGroupPosition == groupPosition) {
            // some items are show
            bingUnreadMsgView.visibility = View.INVISIBLE
            timestampTxtView.visibility = View.GONE
            actionImageView.visibility = View.GONE
            invitationView.visibility = View.GONE
            separatorView.visibility = View.GONE
            separatorGroupView.visibility = View.VISIBLE
            showMoreView.visibility = View.VISIBLE
            actionClickArea.visibility = View.GONE
            unreadCountTxtView.visibility = View.GONE
            directChatIcon.visibility = View.GONE
            encryptedIcon.visibility = View.GONE

            if (directoryGroupPosition == groupPosition) {
                roomNameTxtView.text = mContext.getString(R.string.directory_search_results_title)

                if (!TextUtils.isEmpty(searchedPattern)) {
                    if (null == mMatchedPublicRoomsCount) {
                        roomMsgTxtView.text = mContext.getString(R.string.directory_searching_title)
                    } else {
                        var value = mMatchedPublicRoomsCount.toString()

                        if (mMatchedPublicRoomsCount >= PublicRoomsManager.PUBLIC_ROOMS_LIMIT) {
                            value = "> " + PublicRoomsManager.PUBLIC_ROOMS_LIMIT
                        }

                        roomMsgTxtView.text = mContext.resources
                                .getQuantityString(R.plurals.directory_search_rooms_for, mMatchedPublicRoomsCount, value, searchedPattern)
                    }
                } else {
                    if (null == mPublicRoomsCount) {
                        roomMsgTxtView.text = null
                    } else {
                        roomMsgTxtView.text = mContext.resources
                                .getQuantityString(R.plurals.directory_search_rooms, mPublicRoomsCount, mPublicRoomsCount)
                    }
                }

                avatarImageView.setImageBitmap(VectorUtils.getAvatar(avatarImageView.context, VectorUtils.getAvatarColor(null), null, true))
            } else {
                roomNameTxtView.text = searchedPattern
                roomMsgTxtView.text = ""
                avatarImageView.setImageBitmap(VectorUtils.getAvatar(avatarImageView.context, VectorUtils.getAvatarColor(null), "@", true))
            }
            return convertView
        }

        showMoreView.visibility = View.GONE

        val childRoomSummary = mSummaryListByGroupPosition!![groupPosition][childPosition]
        val childRoom = mMxSession.dataHandler.store.getRoom(childRoomSummary.roomId)
        val unreadMsgCount = childRoomSummary.unreadEventsCount
        var highlightCount = 0
        var notificationCount = 0

        var roomName: String? = null
        if (null != childRoom) {
            highlightCount = childRoom.highlightCount
            notificationCount = childRoom.notificationCount

            if (mMxSession.dataHandler.bingRulesManager.isRoomMentionOnly(childRoom.roomId)) {
                notificationCount = highlightCount
            }

            roomName = childRoom.getRoomDisplayName(mContext)
        }

        // get last message to be displayed
        val lastMsgToDisplay = getChildMessageToDisplay(childRoomSummary)

        // display the room avatar
        VectorUtils.loadRoomAvatar(mContext, mMxSession, avatarImageView, childRoom)

        // display the room name
        roomNameTxtView.text = roomName
//        roomNameTxtView.setTextColor(roomNameBlack)
        roomNameTxtView.setTypeface(null, if (0 != unreadMsgCount) Typeface.BOLD else Typeface.NORMAL)

        // display the last message
        roomMsgTxtView.text = lastMsgToDisplay

        // set the timestamp
        timestampTxtView.text = getFormattedTimestamp(childRoomSummary.latestReceivedEvent)
        timestampTxtView.setTextColor(vectorDefaultTimeStampColor)
        timestampTxtView.setTypeface(null, Typeface.NORMAL)

        // set bing view background colour
        val bingUnreadColor: Int
        if (0 != highlightCount) {
            bingUnreadColor = fushiaColor
        } else if (0 != notificationCount) {
            bingUnreadColor = vectorAccentColor
        } else if (0 != unreadMsgCount) {
            bingUnreadColor = vectorSilverColor
        } else {
            bingUnreadColor = Color.TRANSPARENT
        }
        bingUnreadMsgView.setBackgroundColor(bingUnreadColor)

        // display the unread badge counter
        if (0 != notificationCount) {
            unreadCountTxtView.visibility = View.VISIBLE
            unreadCountTxtView.text = notificationCount.toString()
            unreadCountTxtView.setTypeface(null, Typeface.BOLD)
            unreadCountTxtView.setRoundBackground(bingUnreadColor)
        } else {
            unreadCountTxtView.visibility = View.GONE
        }

        // some items are shown
        var isInvited = false

        if (null != childRoom) {
            isInvited = childRoom.isInvited
        }

        if (null != childRoom) {
            //            directChatIcon.setVisibility(RoomUtils.isDirectChat(mMxSession, childRoom.getRoomId()) ? View.VISIBLE : View.GONE);
            //            encryptedIcon.setVisibility(childRoom.isEncrypted() ? View.VISIBLE : View.GONE);
        } else {
            directChatIcon.visibility = View.GONE
            encryptedIcon.visibility = View.GONE
        }

        bingUnreadMsgView.visibility = if (isInvited) View.INVISIBLE else View.VISIBLE
        invitationView.visibility = if (isInvited) View.VISIBLE else View.GONE

        val fRoomId = childRoomSummary.roomId

        if (isInvited) {
            actionClickArea.visibility = View.GONE

            preViewButton.setOnClickListener {
                mListener?.onPreviewRoom(mMxSession, fRoomId)
            }

            rejectButton.setOnClickListener {
                mListener?.onRejectInvitation(mMxSession, fRoomId)
            }

            // display an exclamation mark like the webclient
            unreadCountTxtView.visibility = View.VISIBLE
            unreadCountTxtView.text = "!"
            unreadCountTxtView.setTypeface(null, Typeface.BOLD)
            unreadCountTxtView.setRoundBackground(fushiaColor)
            timestampTxtView.visibility = View.GONE
            actionImageView.visibility = View.GONE
        } else {

            val isFavorite = groupPosition == mFavouritesGroupPosition
            val isLowPrior = groupPosition == mLowPriorGroupPosition

            actionClickArea.visibility = View.VISIBLE
            actionClickArea.setOnClickListener { RoomUtils.displayPopupMenu(mContext, mMxSession, childRoom, actionView, isFavorite, isLowPrior, mMoreActionListener) }

            timestampTxtView.visibility = if (mIsSearchMode) View.INVISIBLE else View.VISIBLE
            actionImageView.visibility = if (mIsSearchMode) View.INVISIBLE else View.VISIBLE
        }

        separatorView.visibility = if (isLastChild) View.GONE else View.VISIBLE
        separatorGroupView.visibility = if (isLastChild && groupPosition + 1 < groupCount) View.VISIBLE else View.GONE

        return convertView
    }

    /**
     * Get the displayable name of the user whose ID is passed in aUserId.
     *
     * @param aMatrixId matrix ID
     * @param aUserId   user ID
     * @return the user display name
     */
    private fun getMemberDisplayNameFromUserId(aMatrixId: String?, aUserId: String?): String? {
        val displayNameRetValue: String?
        val session: MXSession = Matrix.getMXSession(mContext, aMatrixId);
        if (null == aMatrixId || null == aUserId) {
            displayNameRetValue = null
        } else if (null == session || !session.isAlive) {
            displayNameRetValue = null
        } else {
            val user = session.dataHandler.store.getUser(aUserId)

            if (null != user && !TextUtils.isEmpty(user.displayname)) {
                displayNameRetValue = user.displayname
            } else {
                displayNameRetValue = aUserId
            }
        }

        return displayNameRetValue
    }

    /**
     * Retrieves the text to display for a RoomSummary.
     *
     * @param aChildRoomSummary the roomSummary.
     * @return the text to display.
     */
    private fun getChildMessageToDisplay(aChildRoomSummary: RoomSummary?): CharSequence? {
        var messageToDisplayRetValue: CharSequence? = null
        val eventDisplay: EventDisplay

        if (null != aChildRoomSummary) {
            if (aChildRoomSummary.latestReceivedEvent != null) {
                eventDisplay = RiotEventDisplay(mContext)
                eventDisplay.setPrependMessagesWithAuthor(true)
                messageToDisplayRetValue = eventDisplay.getTextualDisplay(ThemeUtils.getColor(mContext, android.R.attr.textColorTertiary),
                        aChildRoomSummary.latestReceivedEvent,
                        aChildRoomSummary.latestRoomState)
            }

            // check if this is an invite
            if (aChildRoomSummary.isInvited && null != aChildRoomSummary.inviterUserId) {
                // TODO Re-write this algorithm, it's so complicated to understand for nothing...
                val latestRoomState = aChildRoomSummary.latestRoomState
                var inviterUserId: String? = aChildRoomSummary.inviterUserId
                var myName: String? = aChildRoomSummary.userId

                if (null != latestRoomState) {
                    inviterUserId = latestRoomState.getMemberName(inviterUserId)
                    myName = latestRoomState.getMemberName(myName)
                } else {
                    inviterUserId = getMemberDisplayNameFromUserId(aChildRoomSummary.userId, inviterUserId)
                    myName = getMemberDisplayNameFromUserId(aChildRoomSummary.userId, myName)
                }

                if (TextUtils.equals(mMxSession!!.myUserId, aChildRoomSummary.userId)) {
                    messageToDisplayRetValue = mContext.getString(org.matrix.androidsdk.R.string.notice_room_invite_you, inviterUserId)
                } else {
                    messageToDisplayRetValue = mContext.getString(org.matrix.androidsdk.R.string.notice_room_invite, inviterUserId, myName)
                }
            }
        }

        return messageToDisplayRetValue
    }

    /**
     * Defines the new searched pattern
     *
     * @param pattern the new searched pattern
     */
    fun setSearchPattern(pattern: String?) {
        var trimmedPattern = pattern

        if (null != pattern) {
            trimmedPattern = pattern.trim { it <= ' ' }.toLowerCase(VectorLocale.applicationLocale)
            trimmedPattern = if (TextUtils.getTrimmedLength(trimmedPattern) == 0) null else trimmedPattern
        }

        if (!TextUtils.equals(trimmedPattern, searchedPattern)) {

            searchedPattern = trimmedPattern
            mMatchedPublicRoomsCount = 0

            // refresh the layout
            notifyDataSetChanged()
        }
    }

    /**
     * Update the public rooms list count and refresh the display.
     *
     * @param roomsListCount the new public rooms count
     */
    fun setPublicRoomsCount(roomsListCount: Int) {
        if (roomsListCount !== mPublicRoomsCount) {
            mPublicRoomsCount = roomsListCount
            super.notifyDataSetChanged()
        }
    }

    /**
     * Update the matched public rooms list count and refresh the display.
     *
     * @param roomsListCount the new public rooms count
     */
    fun setMatchedPublicRoomsCount(roomsListCount: Int?) {
        if (roomsListCount !== mMatchedPublicRoomsCount) {
            mMatchedPublicRoomsCount = roomsListCount!!
            super.notifyDataSetChanged()
        }
    }

    /**
     * Set the drag and drop mode i.e. there is no automatic room summaries lists refresh.
     *
     * @param isDragAndDropMode the drag and drop mode
     */
    fun setIsDragAndDropMode(isDragAndDropMode: Boolean) {
        isInDragAndDropMode = isDragAndDropMode
    }

    /**
     * Move a childview in the roomSummary dir tree
     *
     * @param fromGroupPosition the group position origin
     * @param fromChildPosition the child position origin
     * @param toGroupPosition   the group position destination
     * @param toChildPosition   the child position destination
     */
    fun moveChildView(fromGroupPosition: Int, fromChildPosition: Int, toGroupPosition: Int, toChildPosition: Int) {
        val fromList : MutableList<RoomSummary> = mSummaryListByGroupPosition!![fromGroupPosition]
        val toList : MutableList<RoomSummary> = mSummaryListByGroupPosition!![toGroupPosition]

        val summary : RoomSummary = fromList[fromChildPosition]
        fromList.removeAt(fromChildPosition)

        if (toChildPosition >= toList.size) {
            toList.add(summary)
        } else {
            toList.add(toChildPosition, summary)
        }
    }

    /**
     * Tell if a group position is the invited one.
     *
     * @param groupPos the proup position.
     * @return true if the  group position is the invited one.
     */
    fun isInvitedRoomPosition(groupPos: Int): Boolean {
        return mInvitedGroupPosition == groupPos
    }

    /**
     * Tell if a group position is the favourite one.
     *
     * @param groupPos the proup position.
     * @return true if the  group position is the favourite one.
     */
    fun isFavouriteRoomPosition(groupPos: Int): Boolean {
        return mFavouritesGroupPosition == groupPos
    }

    /**
     * Tell if a group position is the no tag one.
     *
     * @param groupPos the proup position.
     * @return true if the  group position is the no tag one.
     */
    fun isNoTagRoomPosition(groupPos: Int): Boolean {
        return mNoTagGroupPosition == groupPos
    }

    /**
     * Tell if a group position is the low priority one.
     *
     * @param groupPos the proup position.
     * @return true if the  group position is the low priority one.
     */
    fun isLowPriorityRoomPosition(groupPos: Int): Boolean {
        return mLowPriorGroupPosition == groupPos
    }
}