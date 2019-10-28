package vmodev.clearkeep.adapters

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.google.gson.JsonNull
import im.vector.R
import im.vector.listeners.IMessagesAdapterActionsListener
import im.vector.settings.VectorLocale
import im.vector.ui.themes.ThemeUtils
import im.vector.util.*
import im.vector.view.PillView
import im.vector.view.UrlPreviewView
import im.vector.widgets.WidgetsManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.adapters.MessageRow
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.URLPreview
import org.matrix.androidsdk.rest.model.group.Group
import org.matrix.androidsdk.rest.model.group.GroupProfile
import org.matrix.androidsdk.rest.model.message.Message
import org.matrix.androidsdk.rest.model.message.StickerMessage
import org.matrix.androidsdk.view.HtmlTagHandler
import java.lang.ref.WeakReference
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class MessagesAdapterHelper constructor(val mContext: Context, val mSession: MXSession, val mAdapter: MessagesAdapter) {


    private var mEventsListener: IMessagesAdapterActionsListener? = null

    private var mRoom: Room? = null

    private var mLinkMovementMethod: MatrixLinkMovementMethod? = null

    private var mImageGetter: VectorImageGetter? = null

    /**
     * Define the events listener
     *
     * @param listener the events listener
     */
    fun setVectorMessagesAdapterActionsListener(listener: IMessagesAdapterActionsListener?) {
        mEventsListener = listener
    }

    /**
     * Define the links movement method
     *
     * @param method the links movement method
     */
    fun setLinkMovementMethod(method: MatrixLinkMovementMethod?) {
        mLinkMovementMethod = method
    }

    /**
     * Set the image getter.
     *
     * @param imageGetter the image getter
     */
    fun setImageGetter(imageGetter: VectorImageGetter) {
        mImageGetter = imageGetter
    }

    /**
     * init the sender value
     *
     * @param convertView  the base view
     * @param row          the message row
     * @param isMergedView true if the cell is merged
     */
    fun setSenderValue(convertView: View, row: MessageRow, isMergedView: Boolean) {
        // manage sender text
        val senderTextView = convertView.findViewById<TextView>(R.id.messagesAdapter_sender)
        val groupFlairView = convertView.findViewById<View>(R.id.messagesAdapter_flair_groups_list)

        if (null != senderTextView) {
            val event = row.event

            // Hide the group flair by default
            groupFlairView.visibility = View.GONE
            groupFlairView.tag = null

            if (isMergedView) {
                senderTextView.visibility = View.GONE
            } else {
                val eventType = event.getType()

                // theses events are managed like notice ones
                // but they are dedicated behaviour i.e the sender must not be displayed
                if (event.isCallEvent
                        || Event.EVENT_TYPE_STATE_ROOM_TOPIC == eventType
                        || Event.EVENT_TYPE_STATE_ROOM_MEMBER == eventType
                        || Event.EVENT_TYPE_STATE_ROOM_NAME == eventType
                        || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE == eventType
                        || Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY == eventType
                        || Event.EVENT_TYPE_MESSAGE_ENCRYPTION == eventType) {
                    senderTextView.visibility = View.GONE
                } else {
                    senderTextView.visibility = View.VISIBLE
                    senderTextView.text = row.senderDisplayName

                    val fSenderId = event.getSender()
                    val fDisplayName = if (null == senderTextView.text) "" else senderTextView.text.toString()

                    val context = senderTextView.context
                    val textColor = colorIndexForSender(fSenderId)
                    senderTextView.setTextColor(context.resources.getColor(textColor))

                    senderTextView.setOnClickListener {
                        if (null != mEventsListener) {
                            mEventsListener!!.onSenderNameClick(fSenderId, fDisplayName)
                        }
                    }

                    refreshGroupFlairView(groupFlairView, event)
                }
            }
        }
    }

    /**
     * Refresh the flairs group view
     *
     * @param groupFlairView the flairs view
     * @param event          the event
     * @param groupIdsSet    the groupids
     * @param tag            the tag
     */
    private fun refreshGroupFlairView(groupFlairView: View, event: Event, groupIdsSet: MutableSet<String>, tag: String) {
        Log.d(LOG_TAG, "## refreshGroupFlairView () : " + event.sender + " allows flair to " + groupIdsSet)
        Log.d(LOG_TAG, "## refreshGroupFlairView () : room related groups " + mRoom!!.state.relatedGroups)

        if (!groupIdsSet.isEmpty()) {
            // keeps only the intersections
            groupIdsSet.retainAll(mRoom!!.state.relatedGroups)
        }

        Log.d(LOG_TAG, "## refreshGroupFlairView () : group ids to display $groupIdsSet")

        if (groupIdsSet.isEmpty()) {
            groupFlairView.visibility = View.GONE
        } else {

            if (!mSession.isAlive) {
                return
            }

            groupFlairView.visibility = View.VISIBLE

            val imageViews = ArrayList<ImageView>()

            imageViews.add(groupFlairView.findViewById(R.id.message_avatar_group_1))
            imageViews.add(groupFlairView.findViewById(R.id.message_avatar_group_2))
            imageViews.add(groupFlairView.findViewById(R.id.message_avatar_group_3))

            val moreText = groupFlairView.findViewById<TextView>(R.id.message_more_than_expected)

            val groupIds = ArrayList(groupIdsSet)
            var index = 0
            val bound = Math.min(groupIds.size, imageViews.size)

            while (index < bound) {
                val groupId = groupIds[index]
                val imageView = imageViews[index]

                imageView.visibility = View.VISIBLE

                var group: Group? = mSession.groupsManager.getGroup(groupId)

                if (null == group) {
                    group = Group(groupId)
                }

                val cachedGroupProfile = mSession.groupsManager.getGroupProfile(groupId)

                if (null != cachedGroupProfile) {
                    Log.d(LOG_TAG, "## refreshGroupFlairView () : profile of $groupId is cached")
                    group.groupProfile = cachedGroupProfile
                    VectorUtils.loadGroupAvatar(mContext, mSession, imageView, group)
                } else {
                    VectorUtils.loadGroupAvatar(mContext, mSession, imageView, group)

                    Log.d(LOG_TAG, "## refreshGroupFlairView () : get profile of $groupId")

                    mSession.groupsManager.getGroupProfile(groupId, object : ApiCallback<GroupProfile> {
                        private fun refresh(profile: GroupProfile?) {
                            if (TextUtils.equals(groupFlairView.tag as String, tag)) {
                                val group = Group(groupId)
                                group.groupProfile = profile
                                Log.d(LOG_TAG, "## refreshGroupFlairView () : refresh group avatar $groupId")
                                VectorUtils.loadGroupAvatar(mContext, mSession, imageView, group)
                            }
                        }

                        override fun onSuccess(groupProfile: GroupProfile) {
                            Log.d(LOG_TAG, "## refreshGroupFlairView () : get profile of $groupId succeeded")
                            refresh(groupProfile)
                        }

                        override fun onNetworkError(e: Exception) {
                            Log.e(LOG_TAG, "## refreshGroupFlairView () : get profile of " + groupId + " failed " + e.message, e)
                            refresh(null)
                        }

                        override fun onMatrixError(e: MatrixError) {
                            Log.e(LOG_TAG, "## refreshGroupFlairView () : get profile of " + groupId + " failed " + e.message)
                            refresh(null)
                        }

                        override fun onUnexpectedError(e: Exception) {
                            Log.e(LOG_TAG, "## refreshGroupFlairView () : get profile of " + groupId + " failed " + e.message, e)
                            refresh(null)
                        }
                    })
                }
                index++
            }

            while (index < imageViews.size) {
                imageViews[index].visibility = View.GONE
                index++
            }

            moreText.visibility = if (groupIdsSet.size <= imageViews.size) View.GONE else View.VISIBLE
            moreText.text = mContext.getString(R.string.plus_x, groupIdsSet.size - imageViews.size)

            if (groupIdsSet.size > 0) {
                groupFlairView.setOnClickListener {
                    if (null != mEventsListener) {
                        mEventsListener!!.onGroupFlairClick(event.getSender(), groupIds)
                    }
                }
            } else {
                groupFlairView.setOnClickListener(null)
            }
        }
    }

    /**
     * Refresh the group flair view
     *
     * @param groupFlairView the flairs view
     * @param event          the event
     */
    private fun refreshGroupFlairView(groupFlairView: View, event: Event) {
        val tag = event.getSender() + "__" + event.eventId

        if (null == mRoom) {
            // The flair handling required the room state. So we retrieve the current room (if any).
            // Do not create it if it is not available. For example the room is not available during a room preview.
            // Indeed the room is then stored in memory, and we could not reach it from here for the moment.
            // TODO render the flair in the room preview history.
            mRoom = mSession.dataHandler.getRoom(event.roomId, false)

            if (null == mRoom) {
                Log.d(LOG_TAG, "## refreshGroupFlairView () : the room is not available")
                groupFlairView.visibility = View.GONE
                return
            }
        }

        // Check whether there are some related groups to this room
        if (mRoom!!.state.relatedGroups.isEmpty()) {
            Log.d(LOG_TAG, "## refreshGroupFlairView () : no related group")
            groupFlairView.visibility = View.GONE
            return
        }

        groupFlairView.tag = tag

        Log.d(LOG_TAG, "## refreshGroupFlairView () : eventId " + event.eventId + " from " + event.sender)

        // cached value first
        val userPublicisedGroups = mSession.groupsManager.getUserPublicisedGroups(event.getSender())

        if (null != userPublicisedGroups) {
            refreshGroupFlairView(groupFlairView, event, userPublicisedGroups, tag)
        } else {
            groupFlairView.visibility = View.GONE
            mSession.groupsManager.getUserPublicisedGroups(event.getSender(), false, object : ApiCallback<Set<String>> {
                override fun onSuccess(groupIdsSet: Set<String>) {
                    refreshGroupFlairView(groupFlairView, event, groupIdsSet.toMutableSet(), tag)
                }

                override fun onNetworkError(e: Exception) {
                    Log.e(LOG_TAG, "## refreshGroupFlairView failed " + e.message, e)
                }

                override fun onMatrixError(e: MatrixError) {
                    Log.e(LOG_TAG, "## refreshGroupFlairView failed " + e.message)
                }

                override fun onUnexpectedError(e: Exception) {
                    Log.e(LOG_TAG, "## refreshGroupFlairView failed " + e.message, e)
                }
            })
        }
    }

    // JSON keys
    private val AVATAR_URL_KEY = "avatar_url"
    private val MEMBERSHIP_KEY = "membership"
    private val DISPLAYNAME_KEY = "displayname"

    /**
     * Load the avatar image in the avatar view
     *
     * @param avatarView the avatar view
     * @param row        the message row
     */
    fun loadMemberAvatar(avatarView: ImageView, row: MessageRow) {
        val event = row.event

        val roomMember = row.sender

        var url: String? = null
        var displayName: String? = null

        // Check whether this avatar url is updated by the current event (This happens in case of new joined member)
        val msgContent = event.contentAsJsonObject

        if (msgContent!!.has(AVATAR_URL_KEY)) {
            url = if (msgContent.get(AVATAR_URL_KEY) === JsonNull.INSTANCE) null else msgContent.get(AVATAR_URL_KEY).asString
        }

        if (msgContent.has(MEMBERSHIP_KEY)) {
            val memberShip = if (msgContent.get(MEMBERSHIP_KEY) === JsonNull.INSTANCE) null else msgContent.get(MEMBERSHIP_KEY).asString

            // the avatar url is the invited one not the inviter one.
            if (TextUtils.equals(memberShip, RoomMember.MEMBERSHIP_INVITE)) {
                url = null

                if (null != roomMember) {
                    url = roomMember.getAvatarUrl()
                }
            }

            if (TextUtils.equals(memberShip, RoomMember.MEMBERSHIP_JOIN)) {
                // in some cases, the displayname cannot be retrieved because the user member joined the room with this event
                // without being invited (a public room for example)
                if (msgContent.has(DISPLAYNAME_KEY)) {
                    displayName = if (msgContent.get(DISPLAYNAME_KEY) === JsonNull.INSTANCE) null else msgContent.get(DISPLAYNAME_KEY).asString
                }
            }
        }

        val userId = event.getSender()

        if (!mSession.isAlive) {
            return
        }

        // if there is no preferred display name, use the member one
        if (TextUtils.isEmpty(displayName) && null != roomMember) {
            displayName = roomMember.displayname
        }

        if (roomMember != null && null == url) {
            url = roomMember.getAvatarUrl()
        }

        if (null != roomMember) {
            VectorUtils.loadUserAvatar(mContext, mSession, avatarView, url, roomMember.userId, displayName)
        } else {
            VectorUtils.loadUserAvatar(mContext, mSession, avatarView, url, userId, displayName)
        }
    }

    /**
     * init the sender avatar
     *
     * @param convertView  the base view
     * @param row          the message row
     * @param isMergedView true if the cell is merged
     * @return the avatar layout
     */
    fun setSenderAvatar(convertView: View, row: MessageRow, isMergedView: Boolean): View? {
        val event = row.event
        val avatarView = convertView.findViewById<ImageView>(R.id.messagesAdapter_avatar)

        if (null != avatarView) {
            val userId = event.getSender()

            avatarView.setOnLongClickListener { null != mEventsListener && mEventsListener!!.onAvatarLongClick(userId) }

            // click on the avatar opens the details page
            avatarView.setOnClickListener {
                if (null != mEventsListener) {
                    mEventsListener!!.onAvatarClick(userId)
                }
            }
        }

        if (null != avatarView) {
            if (TextUtils.equals(mSession.myUserId, event.sender)) {
                avatarView.visibility = View.GONE
            } else if (isMergedView) {
                avatarView.visibility = View.INVISIBLE
            } else {
                avatarView.visibility = View.VISIBLE

                avatarView.tag = null

                loadMemberAvatar(avatarView, row)
            }
        }

        return avatarView
    }


    /**
     * Hide the sticker description view
     *
     * @param convertView base view
     */
    fun hideStickerDescription(convertView: View) {
        val stickerDescription = convertView.findViewById<View>(R.id.message_adapter_sticker_layout)

        if (null != stickerDescription) {
            stickerDescription.visibility = View.GONE
        }
    }

    /**
     * Show the sticker description view
     *
     * @param view           base view
     * @param stickerMessage the sticker message
     */
    fun showStickerDescription(view: View, stickerMessage: StickerMessage) {
        val stickerDescriptionLayout = view.findViewById<View>(R.id.message_adapter_sticker_layout)
        val stickerTriangle = view.findViewById<ImageView>(R.id.message_adapter_sticker_triangle)
        val stickerDescription = view.findViewById<TextView>(R.id.message_adapter_sticker_description)

        if (null != stickerDescriptionLayout && null != stickerTriangle && null != stickerDescription) {
            stickerDescriptionLayout.visibility = View.VISIBLE
            stickerTriangle.visibility = View.VISIBLE
            stickerDescription.visibility = View.VISIBLE
            stickerDescription.text = stickerMessage.body
        }
    }

    /**
     * Hide the read receipts view
     *
     * @param convertView base view
     */
    fun hideReadReceipts(convertView: View) {
        val avatarsListView = convertView.findViewById<View>(R.id.messagesAdapter_avatars_list)

        if (null != avatarsListView) {
            avatarsListView.visibility = View.GONE
        }
    }

    /**
     * Display the read receipts within the dedicated vector layout.
     * Console application displays them on the message side.
     * Vector application displays them in a dedicated line under the message
     *
     * @param convertView   base view
     * @param row           the message row
     * @param isPreviewMode true if preview mode
     */
    fun displayReadReceipts(convertView: View,
                            row: MessageRow,
                            isPreviewMode: Boolean,
                            liveRoomMembers: Map<String, RoomMember>?) {
        val avatarsListView = convertView.findViewById<View>(R.id.messagesAdapter_avatars_list)
                ?: return

        if (!mSession.isAlive) {
            return
        }

        val eventId = row.event.eventId

        val store = mSession.dataHandler.store

        // hide the read receipts until there is a way to retrieve them
        // without triggering a request per message
        if (isPreviewMode) {
            avatarsListView.visibility = View.GONE
            return
        }

        val receipts = store!!.getEventReceipts(row.event.roomId, eventId, true, true)

        // if there is no receipt to display
        // hide the dedicated layout
        if (null == receipts || 0 == receipts.size) {
            avatarsListView.visibility = View.GONE
            return
        }

        if (null == mRoom) {
            // The read receipt handling required the room state. So we retrieve the current room (if any).
            // Do not create it if it is not available. For example the room is not available during a room preview.
            mRoom = mSession.dataHandler.getRoom(row.event.roomId, false)

            if (null == mRoom) {
                Log.d(LOG_TAG, "## displayReadReceipts () : the room is not available")
                avatarsListView.visibility = View.GONE
                return
            }
        }

        avatarsListView.visibility = View.VISIBLE

        val imageViews = ArrayList<View>()

        imageViews.add(avatarsListView.findViewById(R.id.message_avatar_receipt_1))
        imageViews.add(avatarsListView.findViewById(R.id.message_avatar_receipt_2))
        imageViews.add(avatarsListView.findViewById(R.id.message_avatar_receipt_3))
        imageViews.add(avatarsListView.findViewById(R.id.message_avatar_receipt_4))
        imageViews.add(avatarsListView.findViewById(R.id.message_avatar_receipt_5))

        val moreText = avatarsListView.findViewById<TextView>(R.id.message_more_than_expected)

        var index = 0
        val bound = Math.min(receipts.size, imageViews.size)

        while (index < bound) {
            val r = receipts[index]
            // For read receipt, we use the last room member data, so get it from the room state
            var member = mRoom!!.state.getMember(r.userId)

            if (member == null && liveRoomMembers != null) {
                // Get the member form the live room members
                member = liveRoomMembers[r.userId]
            }

            val imageView = imageViews[index] as ImageView

            imageView.visibility = View.VISIBLE
            imageView.tag = null

            if (null != member) {
                VectorUtils.loadRoomMemberAvatar(mContext, mSession, imageView, member)
            } else {
                // should never happen
                VectorUtils.loadUserAvatar(mContext, mSession, imageView, null, r.userId, r.userId)
            }
            index++
        }

        moreText.visibility = if (receipts.size <= imageViews.size) View.GONE else View.VISIBLE
        moreText.text = mContext.getString(R.string.x_plus, receipts.size - imageViews.size)

        while (index < imageViews.size) {
            imageViews[index].visibility = View.INVISIBLE
            index++
        }

        // Read receipt clickable zone
        var clickable: View? = avatarsListView.findViewById(R.id.read_receipt_avatars_list)
        if (clickable == null) {
            // Fallback to the parent
            clickable = avatarsListView
        }

        if (receipts.size > 0) {
            clickable.setOnClickListener {
                if (null != mEventsListener) {
                    mEventsListener!!.onMoreReadReceiptClick(eventId)
                }
            }
        } else {
            clickable.setOnClickListener(null)
        }
    }

    // cache the pills to avoid compute them again
    private val mPillsDrawableCache = HashMap<String, Drawable>()

    /**
     * Trap the clicked URL.
     *
     * @param strBuilder    the input string
     * @param span          the URL
     * @param isHighlighted true if the message is highlighted
     */
    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, isHighlighted: Boolean) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)

        if (start >= 0 && end >= 0) {
            val flags = strBuilder.getSpanFlags(span)

            if (PillView.isPillable(span.url)) {
                // This URL link can be replaced by a Pill:
                // Build the Drawable spannable thanks to a PillView
                // And replace the URLSpan by a clickable ImageSpan

                // the key is built with the link, the highlight status and the text of the link
                val key = span.url + " " + isHighlighted + " " + strBuilder.subSequence(start, end).toString()
                var drawable = mPillsDrawableCache[key]

                if (null == drawable) {
                    val pillView = PillView(mContext)
                    pillView.setBackgroundResource(android.R.color.transparent)
                    // Define a weak reference of the view because of the cross reference in the OnUpdateListener.
                    val weakView = WeakReference(pillView)

                    pillView.initData(strBuilder.subSequence(start, end), span.url, mSession) {
                        if (null != weakView && null != weakView.get()) {
                            val pillView = weakView.get()
                            // get a drawable from the view (force to compose)
                            pillView?.let {
                                val updatedDrawable = pillView.getDrawable(true)
                                mPillsDrawableCache[key] = updatedDrawable!!
                                // should update only the current cell
                                // but it might have been recycled
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                    pillView.setHighlighted(isHighlighted)
                    drawable = pillView.getDrawable(false)
                }

                if (null != drawable) {
                    mPillsDrawableCache[key] = drawable
                    val imageSpan = ImageSpan(drawable)
                    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    strBuilder.setSpan(imageSpan, start, end, flags)
                }
            }

            val clickable = object : ClickableSpan() {
                override fun onClick(view: View) {
                    if (null != mEventsListener) {
                        mEventsListener!!.onURLClick(Uri.parse(span.url))
                    }
                }
            }

            strBuilder.setSpan(clickable, start, end, flags)
            strBuilder.removeSpan(span)
        }
    }

    /**
     * Determine if the message body contains any code blocks.
     *
     * @param message the message
     * @return true if it contains code blocks
     */
    fun containsFencedCodeBlocks(message: Message): Boolean {
        return (null != message.formatted_body
                && message.formatted_body.contains(START_FENCED_BLOCK)
                && message.formatted_body.contains(END_FENCED_BLOCK))
    }

    private val mCodeBlocksMap = HashMap<String, Array<String>>()

    /**
     * Split the message body with code blocks delimiters.
     *
     * @param message the message
     * @return the split message body
     */
    fun getFencedCodeBlocks(message: Message): Array<String> {
        if (TextUtils.isEmpty(message.formatted_body)) {
            return arrayOf()
        }

        var codeBlocks = mCodeBlocksMap[message.formatted_body]

        if (null == codeBlocks) {
            codeBlocks = FENCED_CODE_BLOCK_PATTERN.split(message.formatted_body)
            mCodeBlocksMap[message.formatted_body] = codeBlocks!!
        }

        return codeBlocks
    }

    /**
     * Highlight fenced code
     *
     * @param textView the text view
     */
    fun highlightFencedCode(textView: TextView?) {
        // sanity check
        if (null == textView) {
            return
        }

        textView.setBackgroundColor(ThemeUtils.getColor(mContext, R.attr.vctr_markdown_block_background_color))
    }

    /**
     * Apply link movement method to the TextView if not null
     *
     * @param textView
     */
    fun applyLinkMovementMethod(textView: TextView?) {
        if (textView != null && mLinkMovementMethod != null) {
            textView.movementMethod = mLinkMovementMethod
        }
    }

    /**
     * Highlight the pattern in the text.
     *
     * @param text               the text to display
     * @param pattern            the  pattern
     * @param highLightTextStyle the highlight text style
     * @param isHighlighted      true when the message is highlighted
     * @return CharSequence of the text with highlighted pattern
     */
    fun highlightPattern(text: Spannable, pattern: String?, highLightTextStyle: CharacterStyle, isHighlighted: Boolean): CharSequence {
        pattern?.let {
            if (!TextUtils.isEmpty(pattern) && !TextUtils.isEmpty(text) && text.length >= pattern.length) {

                val lowerText = text.toString().toLowerCase(VectorLocale.applicationLocale)
                val lowerPattern = pattern?.toLowerCase(VectorLocale.applicationLocale)

                var start = 0
                var pos = lowerText.indexOf(lowerPattern, start)

                while (pos >= 0) {
                    start = pos + lowerPattern.length
                    text.setSpan(highLightTextStyle, pos, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    text.setSpan(StyleSpan(Typeface.BOLD), pos, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    pos = lowerText.indexOf(lowerPattern, start)
                }
            }
        }

        val strBuilder = SpannableStringBuilder(text)
        val urls = strBuilder.getSpans(0, text.length, URLSpan::class.java)

        if (null != urls && urls.size > 0) {
            for (span in urls) {
                makeLinkClickable(strBuilder, span, isHighlighted)
            }
        }

        MatrixURLSpan.refreshMatrixSpans(strBuilder, mEventsListener)

        return strBuilder
    }


    fun convertToHtml(htmlFormattedText: String?): CharSequence {
        val htmlTagHandler = HtmlTagHandler()
        htmlTagHandler.mContext = mContext
        htmlTagHandler.setCodeBlockBackgroundColor(ThemeUtils.getColor(mContext, R.attr.vctr_markdown_block_background_color))

        var sequence: CharSequence

        // an html format has been released
        if (null != htmlFormattedText) {
            val isCustomizable = !htmlFormattedText.contains("<table>")

            // the markdown tables are not properly supported
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sequence = Html.fromHtml(htmlFormattedText,
                        Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM,
                        mImageGetter,
                        if (isCustomizable) htmlTagHandler else null)
            } else {
                sequence = Html.fromHtml(htmlFormattedText, mImageGetter, if (isCustomizable) htmlTagHandler else null)
            }

            // sanity check
            if (!TextUtils.isEmpty(sequence)) {
                // remove trailing \n to avoid having empty lines..
                var markStart = 0
                var markEnd = sequence.length - 1

                // search first non \n character
                while (markStart < sequence.length - 1 && '\n' == sequence[markStart]) {
                    markStart++
                }

                // search latest non \n character
                while (markEnd >= 0 && '\n' == sequence[markEnd]) {
                    markEnd--
                }

                // empty string ?
                if (markEnd < markStart) {
                    sequence = sequence.subSequence(0, 0)
                } else {
                    sequence = sequence.subSequence(markStart, markEnd + 1)
                }
            }
        } else {
            sequence = ""
        }

        return sequence
    }

    /**
     * Check if an event is displayable
     *
     * @param context the context
     * @param row     the row
     * @return true if the event is managed.
     */
    fun isDisplayableEvent(context: Context, row: MessageRow?): Boolean {
        if (null == row) {
            return false
        }

        val event = row.event ?: return false

        val eventType = event.getType()

        if (Event.EVENT_TYPE_MESSAGE == eventType) {
            // Redacted messages are not displayed (for the moment)
            if (event.isRedacted) {
                return false
            }

            // A message is displayable as long as it has a body, emote can have empty body, formatted message can also have empty body
            val message = JsonUtils.toMessage(event.content)
            return (!TextUtils.isEmpty(message.body)
                    || TextUtils.equals(message.msgtype, Message.MSGTYPE_EMOTE)
                    || TextUtils.equals(message.format, Message.FORMAT_MATRIX_HTML) && !TextUtils.isEmpty(message.formatted_body))
        } else if (Event.EVENT_TYPE_STICKER == eventType) {
            // A sticker is displayable as long as it has a body
            // Redacted stickers should not be displayed
            val stickerMessage = JsonUtils.toStickerMessage(event.content)
            return !TextUtils.isEmpty(stickerMessage.body) && !event.isRedacted
        } else if (Event.EVENT_TYPE_STATE_ROOM_TOPIC == eventType || Event.EVENT_TYPE_STATE_ROOM_NAME == eventType) {
            val display = RiotEventDisplay(context)
            return row.getText(null, display) != null
        } else if (event.isCallEvent) {
            return (Event.EVENT_TYPE_CALL_INVITE == eventType
                    || Event.EVENT_TYPE_CALL_ANSWER == eventType
                    || Event.EVENT_TYPE_CALL_HANGUP == eventType)
        } else if (Event.EVENT_TYPE_STATE_ROOM_MEMBER == eventType || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE == eventType) {
            // if we can display text for it, it's valid.
            val display = RiotEventDisplay(context)
            return row.getText(null, display) != null
        } else if (Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY == eventType) {
            return true
        } else if (Event.EVENT_TYPE_MESSAGE_ENCRYPTED == eventType || Event.EVENT_TYPE_MESSAGE_ENCRYPTION == eventType) {
            // if we can display text for it, it's valid.
            val display = RiotEventDisplay(context)
            return event.hasContentFields() && row.getText(null, display) != null
        } else if (TextUtils.equals(WidgetsManager.WIDGET_EVENT_TYPE, event.getType())) {
            // Matrix apps are enabled
            return true
        } else if (Event.EVENT_TYPE_STATE_ROOM_CREATE == eventType) {
            val roomCreateContent = JsonUtils.toRoomCreateContent(event.content)
            return roomCreateContent != null && roomCreateContent.predecessor != null
        }
        return false
    }

    //================================================================================
    // HTML management
    //================================================================================

    private val mHtmlMap = HashMap<String, String>()

    /**
     * Retrieves the sanitised html.
     * !!!!!! WARNING !!!!!!
     * IT IS NOT REMOTELY A COMPREHENSIVE SANITIZER AND SHOULD NOT BE TRUSTED FOR SECURITY PURPOSES.
     * WE ARE EFFECTIVELY RELYING ON THE LIMITED CAPABILITIES OF THE HTML RENDERER UI TO AVOID SECURITY ISSUES LEAKING UP.
     *
     * @param html the html to sanitize
     * @return the sanitised HTML
     */
    fun getSanitisedHtml(html: String): String? {
        // sanity checks
        if (TextUtils.isEmpty(html)) {
            return null
        }

        var res = mHtmlMap[html]

        if (null == res) {
            res = sanitiseHTML(html)
            mHtmlMap[html] = res
        }

        return res
    }

    private val mAllowedHTMLTags = HashSet(Arrays.asList(
            "font", // custom to matrix for IRC-style font coloring
            "del", // for markdown
            "h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "p", "a", "ul", "ol", "sup", "sub",
            "nl", "li", "b", "i", "u", "strong", "em", "strike", "code", "hr", "br", "div",
            "table", "thead", "caption", "tbody", "tr", "th", "td", "pre", "span", "img"))

    private val mHtmlPatter = Pattern.compile("<(\\w+)[^>]*>", Pattern.CASE_INSENSITIVE)

    /**
     * Sanitise the HTML.
     * The matrix format does not allow the use some HTML tags.
     *
     * @param htmlString the html string
     * @return the sanitised string.
     */
    private fun sanitiseHTML(htmlString: String): String {
        var html = htmlString
        val matcher = mHtmlPatter.matcher(htmlString)

        val tagsToRemove = HashSet<String>()

        while (matcher.find()) {

            try {
                val tag = htmlString.substring(matcher.start(1), matcher.end(1))

                // test if the tag is not allowed
                if (!mAllowedHTMLTags.contains(tag)) {
                    tagsToRemove.add(tag)
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "sanitiseHTML failed " + e.localizedMessage, e)
            }

        }

        // some tags to remove ?
        if (!tagsToRemove.isEmpty()) {
            // append the tags to remove
            var tagsToRemoveString = ""

            for (tag in tagsToRemove) {
                if (!tagsToRemoveString.isEmpty()) {
                    tagsToRemoveString += "|"
                }

                tagsToRemoveString += tag
            }

            html = html.replace("<\\/?($tagsToRemoveString)[^>]*>".toRegex(), "")
        }

        return html
    }

    /*
     * *********************************************************************************************
     *  Url preview managements
     * *********************************************************************************************
     */
    private val mExtractedUrls = HashMap<String, List<String>>()
    private val mUrlsPreviews = HashMap<String, URLPreview>()
    private val mPendingUrls = HashSet<String>()

    /**
     * Retrieves the webUrl extracted from a text
     *
     * @param text the text
     * @return the web urls list
     */
    private fun extractWebUrl(text: String): List<String> {
        var list: MutableList<String>? = mExtractedUrls[text]?.toMutableList()

        if (null == list) {
            list = ArrayList()

            val matcher = android.util.Patterns.WEB_URL.matcher(text)
            while (matcher.find()) {
                try {
                    val value = text.substring(matcher.start(0), matcher.end(0))

                    if (!list.contains(value)) {
                        list.add(value)
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "## extractWebUrl() " + e.message, e)
                }

            }

            mExtractedUrls[text] = list
        }

        return list
    }

    fun manageURLPreviews(message: Message, convertView: View, id: String) {
        val urlsPreviewLayout = convertView.findViewById<LinearLayout>(R.id.messagesAdapter_urls_preview_list)
                ?: return

        // sanity checks

        //
        if (TextUtils.isEmpty(message.body)) {
            urlsPreviewLayout.visibility = View.GONE
            return
        }

        val urls = extractWebUrl(message.body)

        if (urls.isEmpty()) {
            urlsPreviewLayout.visibility = View.GONE
            return
        }

        // avoid removing items if they are displayed
        if (TextUtils.equals(urlsPreviewLayout.tag as String, id)) {
            // all the urls have been displayed
            if (urlsPreviewLayout.childCount == urls.size) {
                return
            }
        }

        urlsPreviewLayout.tag = id

        // remove url previews
        while (urlsPreviewLayout.childCount > 0) {
            urlsPreviewLayout.removeViewAt(0)
        }

        urlsPreviewLayout.visibility = View.VISIBLE

        for (url in urls) {
            val downloadKey = url.hashCode().toString() + "---"
            val displayKey = "$url<----->$id"

            if (!mSession.isURLPreviewEnabled) {
                if (!mUrlsPreviews.containsKey(downloadKey)) {
                    mUrlsPreviews[downloadKey] = null!!
                    mAdapter.notifyDataSetChanged()
                }
            } else if (UrlPreviewView.didUrlPreviewDismiss(displayKey)) {
                Log.d(LOG_TAG, "## manageURLPreviews() : $displayKey has been dismissed")
            } else if (mPendingUrls.contains(url)) {
                // please wait
            } else if (!mUrlsPreviews.containsKey(downloadKey)) {
                mPendingUrls.add(url)
                mSession.eventsApiClient.getURLPreview(url, System.currentTimeMillis(), object : ApiCallback<URLPreview> {
                    override fun onSuccess(urlPreview: URLPreview?) {
                        mPendingUrls.remove(url)

                        if (!mUrlsPreviews.containsKey(downloadKey)) {
                            mUrlsPreviews[downloadKey] = urlPreview!!
                            mAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onNetworkError(e: Exception) {
                        onSuccess(null)
                    }

                    override fun onMatrixError(e: MatrixError) {
                        onSuccess(null)
                    }

                    override fun onUnexpectedError(e: Exception) {
                        onSuccess(null)
                    }
                })
            } else {
                val previewView = UrlPreviewView(mContext)
                previewView.setUrlPreview(mContext, mSession, mUrlsPreviews[downloadKey], displayKey)
                urlsPreviewLayout.addView(previewView)
            }
        }
    }

    //Based on riot-web implementation
    @ColorRes
    private fun colorIndexForSender(sender: String): Int {
        var hash = 0
        var i: Int
        var chr: Char
        if (sender.length == 0) {
            return R.color.username_1
        }
        i = 0
        while (i < sender.length) {
            chr = sender[i]
            hash = (hash shl 5) - hash + chr.toInt()
            hash = hash or 0
            i++
        }
        val cI = Math.abs(hash) % 8 + 1
        when (cI) {
            1 -> return R.color.username_1
            2 -> return R.color.username_2
            3 -> return R.color.username_3
            4 -> return R.color.username_4
            5 -> return R.color.username_5
            6 -> return R.color.username_6
            7 -> return R.color.username_7
            else -> return R.color.username_8
        }
    }

    companion object {
        val LOG_TAG = MessagesAdapterHelper::class.java.simpleName
        /**
         * Enable multiline mode, split on <pre>`...`</pre> and retain those delimiters in
         * the returned fenced block.
         */

        val START_FENCED_BLOCK = "<pre><code>"
        val END_FENCED_BLOCK = "</code></pre>"
        private val FENCED_CODE_BLOCK_PATTERN = Pattern.compile("(?m)(?=<pre><code>)|(?<=</code></pre>)")
        /**
         * init the timeStamp value
         *
         * @param convertView the base view
         * @param value       the new value
         * @return the dedicated textView
         */
        fun setTimestampValue(convertView: View, value: String): TextView? {
            val tsTextView = convertView.findViewById<TextView>(R.id.messagesAdapter_timestamp)

            if (null != tsTextView) {
                if (TextUtils.isEmpty(value)) {
                    tsTextView.visibility = View.GONE
                } else {
                    tsTextView.visibility = View.VISIBLE
                    tsTextView.text = value
                }
            }

            return tsTextView
        }

        /**
         * Align the avatar and the message body according to the mergeView flag
         *
         * @param subView          the message body
         * @param bodyLayoutView   the body layout
         * @param avatarLayoutView the avatar layout
         * @param isMergedView     true if the view is merged
         */
        fun alignSubviewToAvatarView(subView: View, bodyLayoutView: View, avatarLayoutView: View, isMergedView: Boolean) {
            val bodyLayout = bodyLayoutView.layoutParams as ViewGroup.MarginLayoutParams
            val subViewLinearLayout = subView.layoutParams as FrameLayout.LayoutParams

            val avatarLayout = avatarLayoutView.layoutParams
            subViewLinearLayout.gravity = Gravity.START or Gravity.CENTER_VERTICAL

            if (isMergedView) {
                bodyLayout.setMargins(avatarLayout.width, bodyLayout.topMargin, bodyLayout.rightMargin, bodyLayout.bottomMargin)
            } else {
                bodyLayout.setMargins(0, bodyLayout.topMargin, bodyLayout.rightMargin, bodyLayout.bottomMargin)
            }
            subView.layoutParams = bodyLayout

            bodyLayoutView.layoutParams = bodyLayout
            subView.layoutParams = subViewLinearLayout
        }

        /**
         * Update the header text.
         *
         * @param convertView the convert view
         * @param newValue    the new value
         * @param position    the item position
         */
        fun setHeader(convertView: View, newValue: String?, position: Int) {
            // display the day separator
            val headerLayout = convertView.findViewById<View>(R.id.messagesAdapter_message_header)

            if (null != headerLayout) {
                if (null != newValue) {
                    val headerText = convertView.findViewById<TextView>(R.id.messagesAdapter_message_header_text)
                    headerText.text = newValue
                    headerLayout.visibility = View.VISIBLE

                    val topHeaderMargin = headerLayout.findViewById<View>(R.id.messagesAdapter_message_header_top_margin)
                    topHeaderMargin.visibility = if (0 == position) View.GONE else View.VISIBLE
                } else {
                    headerLayout.visibility = View.GONE
                }
            }
        }

        /**
         * Refresh the media progress layouts
         *
         * @param convertView    the convert view
         * @param bodyLayoutView the body layout
         */
        fun setMediaProgressLayout(convertView: View, bodyLayoutView: View) {
            val bodyLayoutParams = bodyLayoutView.layoutParams as ViewGroup.MarginLayoutParams
            val marginLeft = bodyLayoutParams.leftMargin

            val downloadProgressLayout = convertView.findViewById<View>(R.id.content_download_progress_layout)

            if (null != downloadProgressLayout) {
                val downloadProgressLayoutParams = downloadProgressLayout.layoutParams as ViewGroup.MarginLayoutParams
                downloadProgressLayoutParams.setMargins(marginLeft, downloadProgressLayoutParams.topMargin,
                        downloadProgressLayoutParams.rightMargin, downloadProgressLayoutParams.bottomMargin)
                downloadProgressLayout.layoutParams = downloadProgressLayoutParams
            }

            val uploadProgressLayout = convertView.findViewById<View>(R.id.content_upload_progress_layout)

            if (null != uploadProgressLayout) {
                val uploadProgressLayoutParams = uploadProgressLayout.layoutParams as ViewGroup.MarginLayoutParams
                uploadProgressLayoutParams.setMargins(marginLeft, uploadProgressLayoutParams.topMargin,
                        uploadProgressLayoutParams.rightMargin, uploadProgressLayoutParams.bottomMargin)
                uploadProgressLayout.layoutParams = uploadProgressLayoutParams
            }
        }

        /**
         * Check if an event is displayable
         *
         * @param context the context
         * @param row     the row
         * @return true if the event is managed.
         */
        fun isDisplayableEvent(context: Context, row: MessageRow?): Boolean {
            if (null == row) {
                return false
            }

            val event = row.event ?: return false

            val eventType = event.getType()

            if (Event.EVENT_TYPE_MESSAGE == eventType) {
                // Redacted messages are not displayed (for the moment)
                if (event.isRedacted) {
                    return false
                }

                // A message is displayable as long as it has a body, emote can have empty body, formatted message can also have empty body
                val message = JsonUtils.toMessage(event.content)
                return (!TextUtils.isEmpty(message.body)
                        || TextUtils.equals(message.msgtype, Message.MSGTYPE_EMOTE)
                        || TextUtils.equals(message.format, Message.FORMAT_MATRIX_HTML) && !TextUtils.isEmpty(message.formatted_body))
            } else if (Event.EVENT_TYPE_STICKER == eventType) {
                // A sticker is displayable as long as it has a body
                // Redacted stickers should not be displayed
                val stickerMessage = JsonUtils.toStickerMessage(event.content)
                return !TextUtils.isEmpty(stickerMessage.body) && !event.isRedacted
            } else if (Event.EVENT_TYPE_STATE_ROOM_TOPIC == eventType || Event.EVENT_TYPE_STATE_ROOM_NAME == eventType) {
                val display = RiotEventDisplay(context)
                return row.getText(null, display) != null
            } else if (event.isCallEvent) {
                return (Event.EVENT_TYPE_CALL_INVITE == eventType
                        || Event.EVENT_TYPE_CALL_ANSWER == eventType
                        || Event.EVENT_TYPE_CALL_HANGUP == eventType)
            } else if (Event.EVENT_TYPE_STATE_ROOM_MEMBER == eventType || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE == eventType) {
                // if we can display text for it, it's valid.
                val display = RiotEventDisplay(context)
                return row.getText(null, display) != null
            } else if (Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY == eventType) {
                return true
            } else if (Event.EVENT_TYPE_MESSAGE_ENCRYPTED == eventType || Event.EVENT_TYPE_MESSAGE_ENCRYPTION == eventType) {
                // if we can display text for it, it's valid.
                val display = RiotEventDisplay(context)
                return event.hasContentFields() && row.getText(null, display) != null
            } else if (TextUtils.equals(WidgetsManager.WIDGET_EVENT_TYPE, event.getType())) {
                // Matrix apps are enabled
                return true
            } else if (Event.EVENT_TYPE_STATE_ROOM_CREATE == eventType) {
                val roomCreateContent = JsonUtils.toRoomCreateContent(event.content)
                return roomCreateContent != null && roomCreateContent.predecessor != null
            }
            return false
        }
    }
}