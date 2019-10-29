/*
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vmodev.clearkeep.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.binaryfork.spanny.Spanny

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.adapters.AbstractMessagesAdapter
import org.matrix.androidsdk.adapters.MessageRow
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.MXPatterns
import org.matrix.androidsdk.core.PermalinkUtils
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.db.MXMediaCache
import org.matrix.androidsdk.interfaces.HtmlToolbox
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import org.matrix.androidsdk.rest.model.message.Message
import org.matrix.androidsdk.view.HtmlTagHandler

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.Formatter
import java.util.HashMap
import java.util.HashSet
import java.util.Locale

import im.vector.R
import im.vector.VectorApp
import im.vector.adapters.AdapterUtils
import im.vector.extensions.*
import im.vector.listeners.IMessagesAdapterActionsListener
import im.vector.settings.VectorLocale
import im.vector.ui.VectorQuoteSpan
import im.vector.ui.themes.ThemeUtils
import im.vector.util.*
import im.vector.util.EventGroup
import im.vector.util.MatrixLinkMovementMethod
import im.vector.util.MatrixURLSpan
import im.vector.util.PreferencesManager
import im.vector.util.RiotEventDisplay
import im.vector.util.VectorImageGetter
import im.vector.widgets.WidgetsManager

/**
 * An adapter which can display room information.
 */
open class MessagesAdapter
/**
 * Expanded constructor.
 * each message type has its own layout.
 *
 * @param session               the dedicated layout.
 * @param context               the context
 * @param textResLayoutId       the text message layout.
 * @param imageResLayoutId      the image message layout.
 * @param noticeResLayoutId     the notice message layout.
 * @param roomMemberResLayoutId the room member message layout.
 * @param emoteRestLayoutId     the emote message layout
 * @param fileResLayoutId       the file message layout
 * @param mergeResLayoutId      the merge message layout
 * @param videoResLayoutId      the video message layout
 * @param emojiResLayoutId      the emoji message layout
 * @param codeResLayoutId       the code message layout
 * @param stickerResLayoutId    the sticker message layout
 * @param hiddenResLayoutId     the hidden message layout
 * @param mediasCache           the medias cache.
 */
internal constructor(// session
        internal val mSession: MXSession,
        internal val mContext: Context,
        textResLayoutId: Int,
        imageResLayoutId: Int,
        noticeResLayoutId: Int,
        roomMemberResLayoutId: Int,
        emoteRestLayoutId: Int,
        fileResLayoutId: Int,
        mergeResLayoutId: Int,
        videoResLayoutId: Int,
        emojiResLayoutId: Int,
        codeResLayoutId: Int,
        stickerResLayoutId: Int,
        hiddenResLayoutId: Int,
        roomVersionedResLayoutId: Int,
        textEditedResLayoutId: Int,
        textResLayoutOwnerId: Int,
        // media cache
        private val mMediasCache: MXMediaCache) : AbstractMessagesAdapter(mContext, 0) {

    // an event is selected when the user taps on it
    /**
     * Get the current selected event or null if no event is selected
     *
     * @return the current selected event or null if no event is selected
     */
    var currentSelectedEvent: Event? = null
        private set

    // events listeners
    internal var mVectorMessagesAdapterEventsListener: IMessagesAdapterActionsListener? = null

    // current date : used to compute the day header
    private var mReferenceDate = Date()

    // day date of each message
    // the hours, minutes and seconds are removed
    private var mMessagesDateList: List<Date> = ArrayList()

    // when the adapter is used in search mode
    // the searched message should be highlighted
    private var mSearchedEventId: String? = null
    private var mHighlightedEventId: String? = null

    // formatted time by event id
    // it avoids computing them several times
    private val mEventFormattedTsMap = HashMap<String, String>()

    // define the e2e icon to use for a dedicated eventId
    // can be a drawable or
    private var mE2eIconByEventId: Map<String, Any> = HashMap()

    // device info by device id
    private var mE2eDeviceByEventId: Map<String, MXDeviceInfo> = HashMap()

    // true when the room is encrypted
    var mIsRoomEncrypted: Boolean = false

    // Current sessionId set waiting for an encryption key, after a reRequest from user
    private val mSessionIdsWaitingForE2eReRequest = HashSet<String>()
    private val mRowTypeToLayoutId = HashMap<Int, Int>()
    internal val mLayoutInflater: LayoutInflater

    // To keep track of events and avoid duplicates. For instance, we add a message event
    // when the current user sends one but it will also come down the event stream
    private val mEventRowMap = HashMap<String, MessageRow>()

    private val mEventType = HashMap<String, Int>()

    // the message text colors
    private val mDefaultMessageTextColor: Int
    private val mNotSentMessageTextColor: Int
    private val mSendingMessageTextColor: Int
    private val mEncryptingMessageTextColor: Int
    private val mHighlightMessageTextColor: Int
    protected var mBackgroundColorSpan: BackgroundColorSpan

    private val mMaxImageWidth: Int
    private val mMaxImageHeight: Int

    private var mIsSearchMode = false
    private var mIsPreviewMode = false
    private var mIsUnreadViewMode = false
    private var mPattern: String? = null
    private var mLiveMessagesRowList: MutableList<MessageRow>? = null

    // id of the read markers event
    private var mReadReceiptEventId: String? = null

    private var mLinkMovementMethod: MatrixLinkMovementMethod? = null

    private lateinit var mMediasHelper: MessagesAdapterMediasHelper
    protected lateinit var mHelper: MessagesAdapterHelper

    private val mHiddenEventIds = HashSet<String>()

    private val mLocale: Locale

    // custom settings
    private val mAlwaysShowTimeStamps: Boolean
    private val mShowReadReceipts: Boolean

    // Key is member id.
    private val mLiveRoomMembers = HashMap<String, RoomMember>()

    // the color depends in the theme
    private val mPadlockDrawable: Drawable

    private var mImageGetter: VectorImageGetter? = null

    private val mHtmlToolbox = object : HtmlToolbox {
        internal var mHtmlTagHandler: HtmlTagHandler? = null

        override fun convert(html: String): String? {
            val sanitised = mHelper.getSanitisedHtml(html)

            return sanitised ?: html

        }

        override fun getImageGetter(): Html.ImageGetter? {
            return mImageGetter
        }

        override fun getTagHandler(html: String): Html.TagHandler? {
            // the markdown tables are not properly supported
            val isCustomizable = !html.contains("<table>")

            if (isCustomizable) {
                if (mHtmlTagHandler == null) {
                    mHtmlTagHandler = HtmlTagHandler()
                    mHtmlTagHandler!!.mContext = mContext
                    mHtmlTagHandler!!.setCodeBlockBackgroundColor(ThemeUtils.getColor(mContext, R.attr.vctr_markdown_block_background_color))
                }
                return mHtmlTagHandler
            }

            return null
        }
    }

    // customization methods
    private val defaultMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_message_text_color)

    private val noticeTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_notice_text_color)

    private val encryptingMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_encrypting_message_text_color)

    private val sendingMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_sending_message_text_color)

    private val highlightMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_highlighted_message_text_color)

    private val searchHighlightMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_highlighted_searched_message_text_color)

    private val notSentMessageTextColor: Int
        get() = ThemeUtils.getColor(mContext, R.attr.vctr_unsent_message_text_color)

    /**
     * @return true if there is a selected item.
     */
    val isInSelectionMode: Boolean
        get() = null != currentSelectedEvent

    /**
     * Text message management
     *
     * @param viewType    the view type
     * @param position    the message position
     * @param convertView the text message view
     * @param parent      the parent view
     * @return the updated text view.
     */


    private val editedMessageMap = HashMap<String, Event>()

    /*
     * *********************************************************************************************
     * Read markers
     * *********************************************************************************************
     */

    private var mReadMarkerEventId: String? = null
    private var mCanShowReadMarker = true
    private var mReadMarkerListener: ReadMarkerListener? = null

    /*
     * *********************************************************************************************
     *  EventGroups events
     * *********************************************************************************************
     */

    private val mEventGroups = ArrayList<EventGroup>()

    /**
     * Creates a messages adapter with the default layouts.
     */
    constructor(session: MXSession, context: Context, mediasCache: MXMediaCache) : this(session, context,
            R.layout.clearkeep_adapter_item_vector_message_text_emote_notice,
            R.layout.adapter_item_vector_message_image_video,
            R.layout.clearkeep_adapter_item_vector_message_text_emote_notice,
            R.layout.adapter_item_vector_message_room_member,
            R.layout.clearkeep_adapter_item_vector_message_text_emote_notice,
            R.layout.adapter_item_vector_message_file,
            R.layout.adapter_item_vector_message_merge,
            R.layout.adapter_item_vector_message_image_video,
            R.layout.adapter_item_vector_message_emoji,
            R.layout.adapter_item_vector_message_code,
            R.layout.adapter_item_vector_message_image_video,
            R.layout.adapter_item_vector_message_redact,
            R.layout.adapter_item_vector_message_room_versioned,
            R.layout.adapter_item_vector_message_text_edited_emote_notice,
            R.layout.adapter_item_vector_message_text_emote_notice_own,
            mediasCache) {
    }

    init {
        mRowTypeToLayoutId[ROW_TYPE_TEXT] = textResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_IMAGE] = imageResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_NOTICE] = noticeResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_ROOM_MEMBER] = roomMemberResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_EMOTE] = emoteRestLayoutId
        mRowTypeToLayoutId[ROW_TYPE_FILE] = fileResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_MERGE] = mergeResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_VIDEO] = videoResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_EMOJI] = emojiResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_CODE] = codeResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_STICKER] = stickerResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_HIDDEN] = hiddenResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_VERSIONED_ROOM] = roomVersionedResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_TEXT_EDITED] = textEditedResLayoutId
        mRowTypeToLayoutId[ROW_TYPE_TEXT_OWNER] = textResLayoutOwnerId
        mLayoutInflater = LayoutInflater.from(mContext)
        // the refresh will be triggered only when it is required
        // for example, retrieve the historical messages triggers a refresh for each message
        setNotifyOnChange(false)

        mDefaultMessageTextColor = defaultMessageTextColor
        mNotSentMessageTextColor = notSentMessageTextColor
        mSendingMessageTextColor = sendingMessageTextColor
        mEncryptingMessageTextColor = encryptingMessageTextColor
        mHighlightMessageTextColor = highlightMessageTextColor
        mBackgroundColorSpan = BackgroundColorSpan(searchHighlightMessageTextColor)

        val size = Point(0, 0)
        getScreenSize(size)

        val screenWidth = size.x
        val screenHeight = size.y

        // landscape / portrait
        if (screenWidth < screenHeight) {
            mMaxImageWidth = Math.round(screenWidth * 0.6f)
            mMaxImageHeight = Math.round(screenHeight * 0.4f)
        } else {
            mMaxImageWidth = Math.round(screenWidth * 0.4f)
            mMaxImageHeight = Math.round(screenHeight * 0.6f)
        }

        // helpers
        mMediasHelper = MessagesAdapterMediasHelper(mContext,
                mSession, mMaxImageWidth, mMaxImageHeight, mNotSentMessageTextColor, mDefaultMessageTextColor)
        mHelper = MessagesAdapterHelper(mContext, mSession, this)

        mLocale = VectorLocale.applicationLocale

        mAlwaysShowTimeStamps = PreferencesManager.alwaysShowTimeStamps(VectorApp.getInstance())
        mShowReadReceipts = PreferencesManager.showReadReceipts(VectorApp.getInstance())

        mPadlockDrawable = ThemeUtils.tintDrawable(mContext,
                ContextCompat.getDrawable(mContext, R.drawable.ic_lock_open_black_24dp)!!, R.attr.vctr_settings_icon_tint_color)
    }

    /*
     * *********************************************************************************************
     * Graphical items
     * *********************************************************************************************
     */

    /**
     * Return the screen size.
     *
     * @param size the size to set
     */
    @SuppressLint("NewApi")
    private fun getScreenSize(size: Point) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getSize(size)
    }

    /**
     * @return the max thumbnail width
     */
    override fun getMaxThumbnailWidth(): Int {
        return mMaxImageWidth
    }

    /**
     * @return the max thumbnail height
     */
    override fun getMaxThumbnailHeight(): Int {
        return mMaxImageHeight
    }

    /*
     * *********************************************************************************************
     * Items getter / setter
     * *********************************************************************************************
     */

    /**
     * Tests if the row can be inserted in a merge row.
     *
     * @param row the message row to test
     * @return true if the row can be merged
     */
    internal open fun supportMessageRowMerge(row: MessageRow): Boolean {
        return EventGroup.isSupported(row)
    }

    override fun addToFront(row: MessageRow) {
        if (isSupportedRow(row)) {
            android.util.Log.d("AddItem", row.event.contentJson.toString())
            // ensure that notifyDataSetChanged is not called
            // it seems that setNotifyOnChange is reinitialized to true;
            setNotifyOnChange(false)

            if (mIsSearchMode) {
                mLiveMessagesRowList!!.add(0, row)
            } else {
                insert(row, if (!addToEventGroupToFront(row)) 0 else 1)
            }

            if (row.event.eventId != null) {
                mEventRowMap[row.event.eventId] = row
            }
        }
    }

    override fun remove(row: MessageRow?) {
        if (null != row) {
            if (mIsSearchMode) {
                mLiveMessagesRowList!!.remove(row)
            } else {
                removeFromEventGroup(row)

                // get the position before removing the item
                val position = getPosition(row)

                // remove it
                super.remove(row)

                // check merge
                checkEventGroupsMerge(row, position)
            }
        }
    }

    override fun add(row: MessageRow?) {
        add(row, true)
    }

    override fun add(row: MessageRow?, refresh: Boolean) {
        if (isSupportedRow(row!!)) {
            // ensure that notifyDataSetChanged is not called
            // it seems that setNotifyOnChange is reinitialized to true;
            setNotifyOnChange(false)

            if (mIsSearchMode) {
                mLiveMessagesRowList!!.add(row)
            } else {
                addToEventGroup(row)
                super.add(row)
            }

            if (row.event.eventId != null) {
                mEventRowMap[row.event.eventId] = row
            }

            if (!mIsSearchMode && refresh) {
                notifyDataSetChanged()
            } else {
                setNotifyOnChange(true)
            }
        }
    }

    override fun getMessageRow(eventId: String?): MessageRow? {
        return if (null != eventId) {
            mEventRowMap[eventId]
        } else {
            null
        }
    }

    override fun getClosestRow(event: Event?): MessageRow? {
        return if (event == null) {
            null
        } else {
            getClosestRowFromTs(event.eventId, event.getOriginServerTs())
        }
    }

    override fun getClosestRowFromTs(eventId: String, eventTs: Long): MessageRow? {
        var messageRow = getMessageRow(eventId)

        if (messageRow == null) {
            val rows = ArrayList(mEventRowMap.values)

            // loop because the list is not sorted
            for (row in rows) {
                if (row.event !is EventGroup) {
                    val rowTs = row.event.getOriginServerTs()

                    // check if the row event has been received after eventTs (from)
                    if (rowTs > eventTs) {
                        // not yet initialised
                        if (messageRow == null) {
                            messageRow = row
                        } else if (rowTs < messageRow.event.getOriginServerTs()) {
                            messageRow = row
                            Log.d(LOG_TAG, "## getClosestRowFromTs() " + row.event.eventId)
                        }// keep the closest row
                    }
                }
            }
        }

        return messageRow
    }

    override fun getClosestRowBeforeTs(eventId: String, eventTs: Long): MessageRow? {
        var messageRow = getMessageRow(eventId)

        if (messageRow == null) {
            val rows = ArrayList(mEventRowMap.values)

            // loop because the list is not sorted
            for (row in rows) {
                if (row.event !is EventGroup) {
                    val rowTs = row.event.getOriginServerTs()

                    // check if the row event has been received before eventTs (from)
                    if (rowTs < eventTs) {
                        // not yet initialised
                        if (messageRow == null) {
                            messageRow = row
                        } else if (rowTs > messageRow.event.getOriginServerTs()) {
                            messageRow = row
                            Log.d(LOG_TAG, "## getClosestRowBeforeTs() " + row.event.eventId)
                        }// keep the closest row
                    }
                }
            }
        }

        return messageRow
    }

    override fun updateEventById(event: Event, oldEventId: String) {
        val row = mEventRowMap[event.eventId]

        // the event is not yet defined
        if (null == row) {
            val oldRow = mEventRowMap[oldEventId]

            if (null != oldRow) {
                mEventRowMap.remove(oldEventId)
                mEventRowMap[event.eventId] = oldRow
            }
        } else {
            // the eventId already exists
            // remove the old display
            removeEventById(oldEventId)
        }

        notifyDataSetChanged()
    }

    override fun removeEventById(eventId: String) {
        // ensure that notifyDataSetChanged is not called
        // it seems that setNotifyOnChange is reinitialized to true;
        setNotifyOnChange(false)

        val row = mEventRowMap[eventId]

        if (row != null) {
            remove(row)
            mEventRowMap.remove(eventId)
        }
    }

    /*
     * *********************************************************************************************
     * Display modes
     * *********************************************************************************************
     */

    override fun setIsPreviewMode(isPreviewMode: Boolean) {
        mIsPreviewMode = isPreviewMode
    }

    override fun setIsUnreadViewMode(isUnreadViewMode: Boolean) {
        mIsUnreadViewMode = isUnreadViewMode
    }

    override fun isUnreadViewMode(): Boolean {
        return mIsUnreadViewMode
    }

    /*
     * *********************************************************************************************
     * Preview mode
     * *********************************************************************************************
     */
    override fun setSearchPattern(pattern: String) {
        if (!TextUtils.equals(pattern, mPattern)) {
            mPattern = pattern
            mIsSearchMode = !TextUtils.isEmpty(mPattern)

            // in search mode, the live row are cached.
            if (mIsSearchMode) {
                // save once
                if (null == mLiveMessagesRowList) {
                    // backup live events
                    mLiveMessagesRowList = ArrayList()
                    for (pos in 0 until count) {
                        mLiveMessagesRowList!!.add(getItem(pos)!!)
                    }
                }
            } else if (null != mLiveMessagesRowList) {
                // clear and restore the cached live events.
                clear()
                addAll(mLiveMessagesRowList!!)
                mLiveMessagesRowList = null
            }
        }
    }

    /*
     * *********************************************************************************************
     * ArrayAdapter methods
     * *********************************************************************************************
     */

    override fun getViewTypeCount(): Int {
        return NUM_ROW_TYPES
    }

    override fun clear() {
        super.clear()
        if (!mIsSearchMode) {
            mEventRowMap.clear()
        }
    }

    override fun getItemViewType(position: Int): Int {
        // GA Crash
        if (position >= count) {
            return ROW_TYPE_TEXT
        }

        val row = getItem(position)
        row?.event?.let {
            return getItemViewType(it)
        }?:run {
            return  ROW_TYPE_TEXT;
        }

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // GA Crash : it seems that some invalid indexes are required
        if (position >= count) {
            Log.e(LOG_TAG, "## getView() : invalid index $position >= $count")

            // create dummy one is required
            if (null == convertView) {
                convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_TEXT]!!, parent, false)
            }

            if (null != mVectorMessagesAdapterEventsListener) {
                mVectorMessagesAdapterEventsListener!!.onInvalidIndexes()
            }

            return convertView!!
        }

        val inflatedView: View?
        val viewType = getItemViewType(position)

        // when the user scrolls quickly
        // it seems that the recycled view does not have the right layout.
        // check it
        if (null != convertView) {
            if (viewType != convertView.tag as Int) {
                Log.e(LOG_TAG, "## getView() : invalid view type : got " + convertView.tag + " instead of " + viewType)
                convertView = null
            }
        }

        when (viewType) {
            ROW_TYPE_EMOJI, ROW_TYPE_CODE, ROW_TYPE_TEXT -> inflatedView = getTextView(viewType, position, convertView, parent)
            ROW_TYPE_IMAGE, ROW_TYPE_VIDEO, ROW_TYPE_STICKER -> inflatedView = getImageVideoView(viewType, position, convertView, parent)
            ROW_TYPE_NOTICE, ROW_TYPE_ROOM_MEMBER -> inflatedView = getNoticeRoomMemberView(viewType, position, convertView, parent)
            ROW_TYPE_EMOTE -> inflatedView = getEmoteView(position, convertView, parent)
            ROW_TYPE_FILE -> inflatedView = getFileView(position, convertView, parent)
            ROW_TYPE_HIDDEN -> inflatedView = getHiddenView(position, convertView, parent)
            ROW_TYPE_MERGE -> inflatedView = getMergeView(position, convertView, parent)
            ROW_TYPE_VERSIONED_ROOM -> inflatedView = getVersionedRoomView(position, convertView, parent)
            ROW_TYPE_TEXT_EDITED -> inflatedView = getTextEditedView(viewType, position, convertView, parent)
            ROW_TYPE_TEXT_OWNER -> inflatedView = getTextView(viewType, position, convertView, parent)
            else -> throw RuntimeException("Unknown item view type for position $position")
        }

        if (mReadMarkerListener != null) {
            handleReadMarker(inflatedView!!, position)
        }

        if (null != inflatedView) {
            inflatedView.setBackgroundColor(Color.TRANSPARENT)
            inflatedView.tag = viewType
        }

        displayE2eIcon(inflatedView!!, position)

        displayE2eReRequest(inflatedView, position)

        return inflatedView
    }

    override fun notifyDataSetChanged() {
        // undelivered events must be pushed at the end of the history
        setNotifyOnChange(false)
        val undeliverableEvents = ArrayList<MessageRow>()

        var i = 0
        while (i < count) {
            val row = getItem(i)
            val event = row!!.event

            if (null != event && (event.isUndelivered || event.isUnknownDevice)) {
                undeliverableEvents.add(row)
                remove(row)
                i--
            }
            i++
        }

        if (undeliverableEvents.size > 0) {
            try {
                Collections.sort(undeliverableEvents) { m1, m2 ->
                    val diff = m1.event.getOriginServerTs() - m2.event.getOriginServerTs()
                    if (diff > 0) +1 else if (diff < 0) -1 else 0
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "## notifyDataSetChanged () : failed to sort undeliverableEvents " + e.message, e)
            }

            addAll(undeliverableEvents)
        }

        setNotifyOnChange(true)

        // build event -> date list
        refreshRefreshDateList()

        manageCryptoEvents()

        //  do not refresh the room when the application is in background
        // on large rooms, it drains a lot of battery
        if (!VectorApp.isAppInBackground()) {
            super.notifyDataSetChanged()
        }
    }

    /*
     * *********************************************************************************************
     * Public methods
     * *********************************************************************************************
     */

    override fun setLiveRoomMembers(roomMembers: List<RoomMember>) {
        mLiveRoomMembers.clear()

        for (roomMember in roomMembers) {
            mLiveRoomMembers[roomMember.userId] = roomMember
        }

        // Update the Ui (ex: read receipt avatar)
        notifyDataSetChanged()
    }

    /**
     * Notify the fragment that some bing rules could have been updated.
     */
    override fun onBingRulesUpdate() {
        notifyDataSetChanged()
    }

    /**
     * the parent fragment is paused.
     */
    fun onPause() {
        mEventFormattedTsMap.clear()
    }

    /**
     * Toggle the selection mode.
     *
     * @param event the tapped event.
     */
    fun onEventTap(event: Event?) {
        // the tap to select is only enabled when the adapter is not in search mode.
        if (!mIsSearchMode) {
            if (null == currentSelectedEvent) {
                currentSelectedEvent = event
            } else {
                currentSelectedEvent = null
            }
            notifyDataSetChanged()

            if (mVectorMessagesAdapterEventsListener != null) {
                mVectorMessagesAdapterEventsListener!!.onSelectedEventChange(currentSelectedEvent)
            }
        }
    }

    /**
     * Display a bar to the left of the message
     *
     * @param eventId the event id
     */
    fun setSearchedEventId(eventId: String) {
        mSearchedEventId = eventId
        updateHighlightedEventId()
    }

    /**
     * Cancel the message selection mode
     */
    fun cancelSelectionMode() {
        if (null != currentSelectedEvent) {
            currentSelectedEvent = null
            notifyDataSetChanged()

            if (mVectorMessagesAdapterEventsListener != null) {
                mVectorMessagesAdapterEventsListener!!.onSelectedEventChange(currentSelectedEvent)
            }
        }
    }

    /**
     * Define the events listener
     *
     * @param listener teh events listener
     */
    fun setVectorMessagesAdapterActionsListener(listener: IMessagesAdapterActionsListener?) {
        mVectorMessagesAdapterEventsListener = listener
        mMediasHelper.setVectorMessagesAdapterActionsListener(listener)
        mHelper.setVectorMessagesAdapterActionsListener(listener)

        if (null != mLinkMovementMethod) {
            mLinkMovementMethod!!.updateListener(listener)
        } else if (null != listener) {
            mLinkMovementMethod = MatrixLinkMovementMethod(listener)
        }
        mHelper.setLinkMovementMethod(mLinkMovementMethod)
    }

    /**
     * Retrieves the MXDevice info from an event id
     *
     * @param eventId the event id
     * @return the linked device info, null it it does not exist.
     */
    fun getDeviceInfo(eventId: String?): MXDeviceInfo? {
        var deviceInfo: MXDeviceInfo? = null

        if (null != eventId) {
            deviceInfo = mE2eDeviceByEventId[eventId]
        }

        return deviceInfo
    }

    /*
     * *********************************************************************************************
     * Item view methods
     * *********************************************************************************************
     */

    /**
     * Convert Event to view type.
     *
     * @param event the event to convert
     * @return the view type.
     */
    private fun getItemViewType(event: Event): Int {
        val eventId = event.eventId
        var eventType : String? = event.getType()
        if (null != eventId && mHiddenEventIds.contains(eventId)) {
            return ROW_TYPE_HIDDEN
        }
        // never cache the view type of the encrypted messages
        if (Event.EVENT_TYPE_MESSAGE_ENCRYPTED == eventType) {
            //Hide message when this message is encrypted
            return ROW_TYPE_TEXT
            //Hide message when this message is encrypted
            //            return ROW_TYPE_HIDDEN;
        }

        if (event is EventGroup) {
            return ROW_TYPE_MERGE
        }

        if (event.contentJson != null) {
            val content = event.contentJson.asJsonObject
            val relatesTo = content.getAsJsonObject("m.relates_to")
            if (relatesTo != null && relatesTo.has("rel_type")) {
                val eventRelatesToId = relatesTo.get("event_id").asString
                if (editedMessageMap.containsKey(eventRelatesToId)) {
                    if (event.originServerTs > editedMessageMap[eventRelatesToId]!!.originServerTs)
                        editedMessageMap[eventRelatesToId] = event
                } else {
                    editedMessageMap[eventRelatesToId] = event
                }
                return ROW_TYPE_HIDDEN
            }
        }

        if (editedMessageMap.containsKey(eventId)) {
            return ROW_TYPE_TEXT_EDITED
        }

        // never cache the view type of encrypted events
        if (null != eventId) {
            val type = mEventType[eventId]

            if (null != type) {
                return type
            }
        }

        val viewType: Int

        if (Event.EVENT_TYPE_MESSAGE == eventType) {
            val message = JsonUtils.toMessage(event.content)
            val msgType = message.msgtype

            if (Message.MSGTYPE_TEXT == msgType) {
                if (containsOnlyEmojis(message.body)) {
                    viewType = ROW_TYPE_EMOJI
                } else if (!TextUtils.isEmpty(message.formatted_body) && mHelper.containsFencedCodeBlocks(message)) {
                    viewType = ROW_TYPE_CODE
                } else {
                    if (TextUtils.equals(event.sender, mSession.myUserId)){
                        viewType = ROW_TYPE_TEXT_OWNER
                    }
                    else{
                        viewType = ROW_TYPE_TEXT
                    }
                }
            } else if (Message.MSGTYPE_IMAGE == msgType) {
                viewType = ROW_TYPE_IMAGE
            } else if (Message.MSGTYPE_EMOTE == msgType) {
                viewType = ROW_TYPE_EMOTE
            } else if (Message.MSGTYPE_NOTICE == msgType) {
                viewType = ROW_TYPE_NOTICE
            } else if (Message.MSGTYPE_FILE == msgType || Message.MSGTYPE_AUDIO == msgType) {
                viewType = ROW_TYPE_FILE
            } else if (Message.MSGTYPE_VIDEO == msgType) {
                viewType = ROW_TYPE_VIDEO
            } else {
                // Default is to display the body as text
                if (TextUtils.equals(event.sender, mSession.myUserId)){
                    viewType = ROW_TYPE_TEXT_OWNER
                }
                else{
                    viewType = ROW_TYPE_TEXT
                }
            }
        } else if (Event.EVENT_TYPE_STICKER == eventType) {
            viewType = ROW_TYPE_STICKER
        } else if (event.isCallEvent
                || Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY == eventType
                || Event.EVENT_TYPE_STATE_ROOM_TOPIC == eventType
                || Event.EVENT_TYPE_STATE_ROOM_MEMBER == eventType
                || Event.EVENT_TYPE_STATE_ROOM_NAME == eventType
                || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE == eventType
                || Event.EVENT_TYPE_MESSAGE_ENCRYPTION == eventType) {
            viewType = ROW_TYPE_ROOM_MEMBER

        } else if (WidgetsManager.WIDGET_EVENT_TYPE == eventType) {
            return ROW_TYPE_ROOM_MEMBER
        } else if (Event.EVENT_TYPE_STATE_ROOM_CREATE == eventType) {
            viewType = ROW_TYPE_VERSIONED_ROOM
        } else {
            throw RuntimeException("Unknown event type: $eventType")
        }

        if (null != eventId) {
            mEventType[eventId] = viewType
        }
        return viewType
    }

    /**
     * Common view management.
     *
     * @param position    the item position.
     * @param convertView the row view
     * @param subView     the message content view
     * @param msgType     the message type
     */
    private fun manageSubView(position: Int, convertView: View, subView: View, msgType: Int) {
        val row = getItem(position)

        convertView.isClickable = true

        // click on the message row select it
        convertView.setOnClickListener {
            if (null != mVectorMessagesAdapterEventsListener) {
                mVectorMessagesAdapterEventsListener!!.onRowClick(position)
            }
        }

        // long click on the message row display the message options menu
        convertView.setOnLongClickListener { null != mVectorMessagesAdapterEventsListener && mVectorMessagesAdapterEventsListener!!.onRowLongClick(position) }

        val event = row!!.event

        // isMergedView -> the message is going to be merged with the previous one
        // willBeMerged ->tell if a message separator must be displayed
        var isMergedView = false
        var willBeMerged = false

        // the notices are never merged
        if (!mIsSearchMode && isMergeableEvent(msgType)) {
            if (position > 0) {
                val prevEvent = getItem(position - 1)!!.event
                isMergedView = isMergeableEvent(getItemViewType(prevEvent)) && TextUtils.equals(prevEvent.getSender(), event.getSender())
            }

            // not the last message
            if (position + 1 < count) {
                val nextEvent = getItem(position + 1)!!.event
                willBeMerged = isMergeableEvent(getItemViewType(nextEvent)) && TextUtils.equals(nextEvent.getSender(), event.getSender())
            }
        }

        // inherited class custom behaviour
        isMergedView = mergeView(event, position, isMergedView)

        // init senders
        mHelper.setSenderValue(convertView, row, isMergedView)

        // message timestamp
        val tsTextView = MessagesAdapterHelper.setTimestampValue(convertView, getFormattedTimestamp(event))

        if (null != tsTextView) {
            if (row.event.isUndelivered || row.event.isUnknownDevice) {
                tsTextView.setTextColor(mNotSentMessageTextColor)
            } else {
                tsTextView.setTextColor(ThemeUtils.getColor(mContext, android.R.attr.textColorSecondary))
            }

            tsTextView.visibility = if (position + 1 == count || mIsSearchMode || mAlwaysShowTimeStamps) View.VISIBLE else View.GONE
        }

        // Sender avatar
        val avatarView = mHelper.setSenderAvatar(convertView, row, isMergedView)

        // if the messages are merged
        // the thumbnail is hidden
        // and the subview must be moved to be aligned with the previous body
        val bodyLayoutView = convertView.findViewById<View>(R.id.messagesAdapter_body_layout)
        MessagesAdapterHelper.alignSubviewToAvatarView(subView, bodyLayoutView, avatarView!!, isMergedView)

        // messages separator
        val messageSeparatorView = convertView.findViewById<View>(R.id.messagesAdapter_message_separator)

        if (null != messageSeparatorView) {
            messageSeparatorView.visibility = if (willBeMerged || position + 1 == count) View.GONE else View.VISIBLE
        }

        // display the day separator
        MessagesAdapterHelper.setHeader(convertView, headerMessage(position), position)

        // read receipts
        if (!mShowReadReceipts) {
            mHelper.hideReadReceipts(convertView)
        } else {
            mHelper.displayReadReceipts(convertView, row, mIsPreviewMode, mLiveRoomMembers)
        }

        // selection mode
        manageSelectionMode(convertView, event, msgType)

        // read marker
        setReadMarker(convertView, row, isMergedView, avatarView, bodyLayoutView)

        // download / upload progress layout
        if (ROW_TYPE_IMAGE == msgType || ROW_TYPE_FILE == msgType || ROW_TYPE_VIDEO == msgType || ROW_TYPE_STICKER == msgType) {
            MessagesAdapterHelper.setMediaProgressLayout(convertView, bodyLayoutView)
        }
    }


    private fun getTextView(viewType: Int, position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[viewType]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val event = row!!.event
            //            if (editedMessageMap.containsKey(event.eventId)) {
            //                event = editedMessageMap.get(event.eventId);
            //                row = new MessageRow(event, mSession.getDataHandler().getRoom(event.roomId).getState());
            //            }

            val message = JsonUtils.toMessage(event.content)

            val shouldHighlighted = null != mVectorMessagesAdapterEventsListener && mVectorMessagesAdapterEventsListener!!.shouldHighlightEvent(event)

            val textViews: MutableList<TextView>

            if (ROW_TYPE_CODE == viewType) {
                textViews = populateRowTypeCode(message, convertView!!, shouldHighlighted)
            } else {
                val bodyTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_body)

                // cannot refresh it
                if (null == bodyTextView) {
                    Log.e(LOG_TAG, "getTextView : invalid layout")
                    return convertView
                }

                val display = RiotEventDisplay(mContext, mHtmlToolbox)

                val body = row.getText(VectorQuoteSpan(mContext), display)

                val result = mHelper.highlightPattern(body,
                        mPattern,
                        mBackgroundColorSpan,
                        shouldHighlighted)

                bodyTextView.text = result

                mHelper.applyLinkMovementMethod(bodyTextView)
                bodyTextView.vectorCustomLinkify(true)
                textViews = ArrayList()
                textViews.add(bodyTextView)
            }

            val textColor: Int

            if (row.event.isEncrypting) {
                textColor = mEncryptingMessageTextColor
            } else if (row.event.isSending || row.event.isUnsent) {
                textColor = mSendingMessageTextColor
            } else if (row.event.isUndelivered || row.event.isUnknownDevice) {
                textColor = mNotSentMessageTextColor
            } else {
                textColor = if (shouldHighlighted) mHighlightMessageTextColor else mDefaultMessageTextColor
            }

            for (tv in textViews) {
                tv.setTextColor(textColor)
            }

            val textLayout = convertView.findViewById<View>(R.id.messagesAdapter_text_layout)
            manageSubView(position, convertView, textLayout, viewType)

            for (tv in textViews) {
                addContentViewListeners(convertView, tv, position, viewType)
            }

            mHelper.manageURLPreviews(message, convertView, event.eventId)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getTextView() failed : " + e.message, e)
        }

        return convertView
    }

    private fun getTextEditedView(viewType: Int, position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[viewType]!!, parent, false)
        }

        try {
            var row = getItem(position)
            var event: Event? = row!!.event
            val message: Message
            if (editedMessageMap.containsKey(event!!.eventId)) {
                event = editedMessageMap[event.eventId]
                row = MessageRow(event!!, mSession.dataHandler.getRoom(event.roomId).state)
                message = JsonUtils.toMessage(event.content)
                message.body = event.content.getAsJsonObject().get("m.new_content").getAsJsonObject().get("body").getAsString() + "<i>(edited)</i>"
            } else {
                message = JsonUtils.toMessage(event.content)
            }


            val shouldHighlighted = null != mVectorMessagesAdapterEventsListener && mVectorMessagesAdapterEventsListener!!.shouldHighlightEvent(event)

            val textViews: MutableList<TextView>

            if (ROW_TYPE_CODE == viewType) {
                textViews = populateRowTypeCode(message, convertView!!, shouldHighlighted)
            } else {
                val bodyTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_body)

                // cannot refresh it
                if (null == bodyTextView) {
                    Log.e(LOG_TAG, "getTextView : invalid layout")
                    return convertView
                }

                val display = RiotEventDisplay(mContext, mHtmlToolbox)

                val body = row.getText(VectorQuoteSpan(mContext), display)
                var result = mHelper.highlightPattern(body,
                        Message.FORMAT_MATRIX_HTML,
                        mBackgroundColorSpan,
                        shouldHighlighted)
                result = "$result ${context.resources.getString(R.string.edited_suffix)}";
                result = result.subSequence(2, result.length)
                bodyTextView.text = result;

                mHelper.applyLinkMovementMethod(bodyTextView)
                bodyTextView.vectorCustomLinkify(true)
                textViews = ArrayList()
                textViews.add(bodyTextView)
            }

            val textColor: Int

            if (row.event.isEncrypting) {
                textColor = mEncryptingMessageTextColor
            } else if (row.event.isSending || row.event.isUnsent) {
                textColor = mSendingMessageTextColor
            } else if (row.event.isUndelivered || row.event.isUnknownDevice) {
                textColor = mNotSentMessageTextColor
            } else {
                textColor = if (shouldHighlighted) mHighlightMessageTextColor else mDefaultMessageTextColor
            }

            for (tv in textViews) {
                tv.setTextColor(textColor)
            }

            val textLayout = convertView.findViewById<View>(R.id.messagesAdapter_text_layout)
            manageSubView(position, convertView, textLayout, viewType)

            for (tv in textViews) {
                addContentViewListeners(convertView, tv, position, viewType)
            }

            mHelper.manageURLPreviews(message, convertView, event.eventId)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getTextView() failed : " + e.message, e)
        }

        return convertView
    }

    /**
     * For ROW_TYPE_CODE message which may contain mixture of
     * fenced and inline code blocks and non-code (issue 145)
     */
    private fun populateRowTypeCode(message: Message,
                                    convertView: View,
                                    shouldHighlighted: Boolean): MutableList<TextView> {
        val textViews = ArrayList<TextView>()
        val container = convertView.findViewById<LinearLayout>(R.id.messages_container)

        // remove older blocks
        container.removeAllViews()

        val blocks = mHelper.getFencedCodeBlocks(message)

        for (block in blocks) {
            // Skip empty block
            if (TextUtils.isEmpty(block)) {
                continue
            }

            if (block.startsWith(MessagesAdapterHelper.START_FENCED_BLOCK) && block.endsWith(MessagesAdapterHelper.END_FENCED_BLOCK)) {
                // Fenced block
                val minusTags = block
                        .substring(MessagesAdapterHelper.START_FENCED_BLOCK.length,
                                block.length - MessagesAdapterHelper.END_FENCED_BLOCK.length)
                        .replace("\n", "<br/>")
                        .replace(" ", "&nbsp;")
                        .trim { it <= ' ' }

                val htmlReady = mHelper.convertToHtml(minusTags)
                val blockView = mLayoutInflater.inflate(R.layout.adapter_item_vector_message_code_block, null)
                val tv = blockView.findViewById<TextView>(R.id.messagesAdapter_body)
                tv.text = htmlReady

                mHelper.highlightFencedCode(tv)
                mHelper.applyLinkMovementMethod(tv)

                container.addView(blockView)
                textViews.add(tv)

                (tv.parent as View).setBackgroundColor(ThemeUtils.getColor(mContext, R.attr.vctr_markdown_block_background_color))
            } else {
                // Not a fenced block
                val tv = mLayoutInflater.inflate(R.layout.adapter_item_vector_message_code_text, null) as TextView
                var block2 = block.trim { it <= ' ' }
                if (TextUtils.equals(Message.FORMAT_MATRIX_HTML, message.format)) {
                    val sanitized = mHelper.getSanitisedHtml(block2)
                    if (sanitized != null) {
                        block2 = sanitized
                    }
                }
                val sequence = mHelper.convertToHtml(block2)
                val strBuilder = mHelper.highlightPattern(SpannableString(sequence),
                        mPattern,
                        mBackgroundColorSpan,
                        shouldHighlighted)
                tv.text = strBuilder
                mHelper.applyLinkMovementMethod(tv)
                container.addView(tv)
                textViews.add(tv)
            }
        }

        return textViews
    }

    /**
     * Image / Video  message management
     *
     * @param type        ROW_TYPE_IMAGE or ROW_TYPE_VIDEO
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getImageVideoView(type: Int, position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[type]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val event = row!!.event
            var message: Message? = null

            var videoContent = false
            if (type == ROW_TYPE_IMAGE) {
                val imageMessage = JsonUtils.toImageMessage(event.content)
                if ("image/gif" == imageMessage.mimeType) {
                    videoContent = true
                }
                message = imageMessage
            } else if (type == ROW_TYPE_VIDEO) {
                videoContent = true
                message = JsonUtils.toVideoMessage(event.content)
            } else if (type == ROW_TYPE_STICKER) {
                val stickerMessage = JsonUtils.toStickerMessage(event.content)
                message = stickerMessage
            }

            // display a play icon for video content
            val playCircleView = convertView!!.findViewById<ImageView>(R.id.messagesAdapter_play_circle)
            if (null == playCircleView) {
                Log.e(LOG_TAG, "getImageVideoView : invalid layout")
                return convertView
            }
            playCircleView.visibility = View.GONE
            if (videoContent) {
                playCircleView.visibility = View.VISIBLE
            }

            if (null != message) {
                mHelper.hideStickerDescription(convertView)

                // download management
                mMediasHelper.managePendingImageVideoDownload(convertView, event, message, position)

                // upload management
                mMediasHelper.managePendingImageVideoUpload(convertView, event, message)
            }

            // dimmed when the message is not sent
            val imageLayout = convertView.findViewById<View>(R.id.messagesAdapter_image_layout)
            imageLayout.alpha = if (event.isSent) 1.0f else 0.5f

            manageSubView(position, convertView, imageLayout, type)

            val imageView = convertView.findViewById<ImageView>(R.id.messagesAdapter_image)
            addContentViewListeners(convertView, imageView, position, type)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getImageVideoView() failed : " + e.message, e)
        }

        return convertView
    }

    /**
     * Notice and RoomMember message management
     *
     * @param viewType    the viewType
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getNoticeRoomMemberView(viewType: Int, position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[viewType]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val msg = row!!.event

            val notice: CharSequence

            val display = RiotEventDisplay(mContext)
            notice = row.getText(null, display)

            val noticeTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_body)

            if (null == noticeTextView) {
                Log.e(LOG_TAG, "getNoticeRoomMemberView : invalid layout")
                return convertView
            }

            if (TextUtils.isEmpty(notice)) {
                noticeTextView.text = ""
            } else {
                val strBuilder = SpannableStringBuilder(notice)
                MatrixURLSpan.refreshMatrixSpans(strBuilder, mVectorMessagesAdapterEventsListener)
                mHelper.applyLinkMovementMethod(noticeTextView)
                noticeTextView.text = strBuilder
                //In room member we don't want autolink, but do it for m.notice
                if (viewType == ROW_TYPE_NOTICE) {
                    noticeTextView.vectorCustomLinkify(true)
                }
            }

            val textLayout = convertView.findViewById<View>(R.id.messagesAdapter_text_layout)
            manageSubView(position, convertView, textLayout, viewType)

            addContentViewListeners(convertView, noticeTextView, position, viewType)

            // android seems having a big issue when the text is too long and an alpha !=1 is applied:
            // ---> the text is not displayed.
            // It is sometimes partially displayed and/or flickers while scrolling.
            // Apply an alpha != 1, trigger the same issue.
            // It is related to the number of characters not to the number of lines.
            // I don't understand why the render graph fails to do it.
            // the patch apply the alpha to the text color but it does not work for the hyperlinks.
            noticeTextView.alpha = 1.0f
            noticeTextView.setTextColor(noticeTextColor)

            val message = JsonUtils.toMessage(msg.content)
            mHelper.manageURLPreviews(message, convertView, msg.eventId)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getNoticeRoomMemberView() failed : " + e.message, e)
        }

        return convertView
    }

    /**
     * Emote message management
     *
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getEmoteView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_EMOTE]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val event = row!!.event

            val emoteTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_body)

            if (null == emoteTextView) {
                Log.e(LOG_TAG, "getEmoteView : invalid layout")
                return convertView
            }

            val message = JsonUtils.toMessage(event.content)

            var body: CharSequence = "* " + row.senderDisplayName + " " + message.body

            val isCustomHtml = TextUtils.equals(Message.FORMAT_MATRIX_HTML, message.format)
            if (isCustomHtml) {
                val htmlString = mHelper.getSanitisedHtml(message.formatted_body)

                if (null != htmlString) {
                    val sequence = mHelper.convertToHtml(htmlString)

                    body = TextUtils.concat("* ", row.senderDisplayName, " ", sequence)
                }
            }

            val strBuilder = mHelper.highlightPattern(SpannableString(body), null, mBackgroundColorSpan, false)

            emoteTextView.text = strBuilder
            mHelper.applyLinkMovementMethod(emoteTextView)
            emoteTextView.vectorCustomLinkify(isCustomHtml)

            val textColor: Int

            if (row.event.isEncrypting) {
                textColor = mEncryptingMessageTextColor
            } else if (row.event.isSending || row.event.isUnsent) {
                textColor = mSendingMessageTextColor
            } else if (row.event.isUndelivered || row.event.isUnknownDevice) {
                textColor = mNotSentMessageTextColor
            } else {
                textColor = mDefaultMessageTextColor
            }

            emoteTextView.setTextColor(textColor)

            val textLayout = convertView.findViewById<View>(R.id.messagesAdapter_text_layout)
            manageSubView(position, convertView, textLayout, ROW_TYPE_EMOTE)

            addContentViewListeners(convertView, emoteTextView, position, ROW_TYPE_EMOTE)

            mHelper.manageURLPreviews(message, convertView, event.eventId)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getEmoteView() failed : " + e.message, e)
        }

        return convertView
    }

    /**
     * File message management
     *
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getFileView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_FILE]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val event = row!!.event

            val fileMessage = JsonUtils.toFileMessage(event.content)
            val fileTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_filename)

            if (null == fileTextView) {
                Log.e(LOG_TAG, "getFileView : invalid layout")
                return convertView
            }

            fileTextView.paintFlags = fileTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            fileTextView.text = fileMessage.body

            // display the right message type icon.
            // Audio and File messages are managed by the same method
            val imageTypeView = convertView.findViewById<ImageView>(R.id.messagesAdapter_image_type)

            imageTypeView?.setImageResource(if (Message.MSGTYPE_AUDIO == fileMessage.msgtype) R.drawable.filetype_audio else R.drawable.filetype_attachment)
            imageTypeView!!.setBackgroundColor(Color.TRANSPARENT)

            mMediasHelper.managePendingFileDownload(convertView, event, fileMessage, position)
            mMediasHelper.managePendingUpload(convertView, event, ROW_TYPE_FILE, fileMessage.url)

            val fileLayout = convertView.findViewById<View>(R.id.messagesAdapter_file_layout)
            manageSubView(position, convertView, fileLayout, ROW_TYPE_FILE)

            addContentViewListeners(convertView, fileTextView, position, ROW_TYPE_FILE)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getFileView() failed " + e.message, e)
        }

        return convertView
    }

    /**
     * Hidden message management.
     *
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getHiddenView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_HIDDEN]!!, parent, false)
        }

        // display the day separator
        MessagesAdapterHelper.setHeader(convertView!!, headerMessage(position), position)

        return convertView
    }

    /**
     * Get a merge view for a position.
     *
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated text view.
     */
    private fun getMergeView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_MERGE]!!, parent, false)
        }

        try {
            val row = getItem(position)
            val event = row!!.event as EventGroup

            val headerLayout = convertView!!.findViewById<View>(R.id.messagesAdapter_merge_header_layout)
            val headerTextView = convertView.findViewById<TextView>(R.id.messagesAdapter_merge_header_text_view)
            val summaryTextView = convertView.findViewById<TextView>(R.id.messagesAdapter_merge_summary)
            val separatorLayout = convertView.findViewById<View>(R.id.messagesAdapter_merge_separator)
            val avatarsLayout = convertView.findViewById<View>(R.id.messagesAdapter_merge_avatar_list)

            // test if the layout is still valid
            // reported by a rageshake
            if (null == headerLayout || null == headerTextView || null == summaryTextView
                    || null == separatorLayout || null == avatarsLayout) {
                Log.e(LOG_TAG, "getMergeView : invalid layout")
                return convertView
            }

            separatorLayout.visibility = if (event.isExpanded) View.VISIBLE else View.GONE
            summaryTextView.visibility = if (event.isExpanded) View.GONE else View.VISIBLE
            avatarsLayout.visibility = if (event.isExpanded) View.GONE else View.VISIBLE

            headerTextView.setText(if (event.isExpanded) R.string.merged_events_collapse else R.string.merged_events_expand)

            if (!event.isExpanded) {
                avatarsLayout.visibility = View.VISIBLE
                val avatarView = ArrayList<ImageView>()

                avatarView.add(convertView.findViewById<View>(R.id.mels_list_avatar_1) as ImageView)
                avatarView.add(convertView.findViewById<View>(R.id.mels_list_avatar_2) as ImageView)
                avatarView.add(convertView.findViewById<View>(R.id.mels_list_avatar_3) as ImageView)
                avatarView.add(convertView.findViewById<View>(R.id.mels_list_avatar_4) as ImageView)
                avatarView.add(convertView.findViewById<View>(R.id.mels_list_avatar_5) as ImageView)

                val messageRows = event.getAvatarRows(avatarView.size)

                for (i in avatarView.indices) {
                    val imageView = avatarView[i]

                    if (i < messageRows.size) {
                        mHelper.loadMemberAvatar(imageView, messageRows[i])
                        imageView.visibility = View.VISIBLE
                    } else {
                        imageView.visibility = View.GONE
                    }
                }


                summaryTextView.text = event.toString(mContext)
            }

            headerLayout.setOnClickListener {
                event.setIsExpanded(!event.isExpanded)
                updateHighlightedEventId()

                if (currentSelectedEvent != null && event.contains(currentSelectedEvent!!.eventId)) {
                    cancelSelectionMode()
                } else {
                    notifyDataSetChanged()
                }
            }

            // set the message marker
            convertView.findViewById<View>(R.id.messagesAdapter_highlight_message_marker)
                    .setBackgroundColor(if (TextUtils.equals(mHighlightedEventId, event.eventId))
                        ThemeUtils.getColor(mContext, R.attr.colorAccent)
                    else
                        ContextCompat.getColor(mContext, android.R.color.transparent))

            // display the day separator
            MessagesAdapterHelper.setHeader(convertView, headerMessage(position), position)

            val isInSelectionMode = null != currentSelectedEvent
            val isSelected = isInSelectionMode && TextUtils.equals(event.eventId, currentSelectedEvent!!.eventId)

            val alpha = if (!isInSelectionMode || isSelected) 1.0f else 0.2f

            // the message body is dimmed when not selected
            convertView.findViewById<View>(R.id.messagesAdapter_body_view).alpha = alpha
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## getMergeView() failed " + e.message, e)
        }

        return convertView
    }

    /**
     * Versioned Room management
     *
     * @param position    the message position
     * @param convertView the message view
     * @param parent      the parent view
     * @return the updated View
     */
    private fun getVersionedRoomView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mRowTypeToLayoutId[ROW_TYPE_VERSIONED_ROOM]!!, parent, false)
        }
        val row = getItem(position)
        val roomCreateContent = JsonUtils.toRoomCreateContent(row!!.event.content)
        val roomLink = PermalinkUtils.createPermalink(roomCreateContent.predecessor.roomId)
        val urlSpan = MatrixURLSpan(roomLink, MXPatterns.PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ID, mVectorMessagesAdapterEventsListener)
        val textColorInt = ContextCompat.getColor(mContext, R.color.riot_primary_text_color_light)
        val text = Spanny(mContext.getString(R.string.room_tombstone_continuation_description),
                StyleSpan(Typeface.BOLD),
                ForegroundColorSpan(textColorInt))
                .append("\n")
                .append(mContext.getString(R.string.room_tombstone_predecessor_link), urlSpan, ForegroundColorSpan(textColorInt))

        val versionedTextView = convertView!!.findViewById<TextView>(R.id.messagesAdapter_room_versioned_text)
        versionedTextView.movementMethod = LinkMovementMethod.getInstance()
        versionedTextView.text = text
        return convertView
    }

    /**
     * Check if the row must be added to the list.
     *
     * @param row the row to check.
     * @return true if should be added
     */
    private fun isSupportedRow(row: MessageRow): Boolean {
        val event = row.event

        // sanity checks
        if (null == event || null == event.eventId) {
            Log.e(LOG_TAG, "## isSupportedRow() : invalid row")
            return false
        }

        val eventId = event.eventId
        val currentRow = mEventRowMap[eventId]

        if (null != currentRow) {
            // waiting for echo
            // the message is displayed as sent event if the echo has not been received
            // it avoids displaying a pending message whereas the message has been sent
            if (event.getAge() == Event.DUMMY_EVENT_AGE) {
                currentRow.updateEvent(event)
                Log.d(LOG_TAG, "## isSupportedRow() : update the timestamp of $eventId")
            } else {
                Log.e(LOG_TAG, "## isSupportedRow() : the event $eventId has already been received")
            }
            return false
        }

        var isSupported = MessagesAdapterHelper.isDisplayableEvent(mContext, row)

        if (!isSupported) {
            Log.w(LOG_TAG, "Unsupported row. Event type: " + event.getType())
        }

        if (isSupported && TextUtils.equals(event.getType(), Event.EVENT_TYPE_STATE_ROOM_MEMBER)) {
            val roomMember = JsonUtils.toRoomMember(event.content)
            val membership = roomMember.membership

            if (!PreferencesManager.showJoinLeaveMessages(mContext)) {
                isSupported = !TextUtils.equals(membership, RoomMember.MEMBERSHIP_LEAVE) && !TextUtils.equals(membership, RoomMember.MEMBERSHIP_JOIN)
            }

            if (isSupported && !PreferencesManager.showAvatarDisplayNameChangeMessages(mContext) && TextUtils.equals(membership, RoomMember.MEMBERSHIP_JOIN)) {
                val eventContent = JsonUtils.toEventContent(event.contentAsJsonObject)
                val prevEventContent = event.prevContent

                val senderDisplayName = eventContent.displayname
                var prevUserDisplayName: String? = null
                val avatar = eventContent.avatar_url
                var prevAvatar: String? = null

                if (null != prevEventContent) {
                    prevUserDisplayName = prevEventContent.displayname
                    prevAvatar = prevEventContent.avatar_url
                }

                // !Updated display name && same avatar
                isSupported = TextUtils.equals(prevUserDisplayName, senderDisplayName) && TextUtils.equals(avatar, prevAvatar)
            }
        }

        return isSupported
    }

    /*
     * *********************************************************************************************
     * Private methods
     * *********************************************************************************************
     */

    /**
     * Provides the formatted timestamp to display.
     * null means that the timestamp text must be hidden.
     *
     * @param event the event.
     * @return the formatted timestamp to display.
     */
    private fun getFormattedTimestamp(event: Event): String {
        var res = mEventFormattedTsMap[event.eventId]

        if (null != res) {
            return res
        }

        if (event.isValidOriginServerTs) {
            res = AdapterUtils.tsToString(mContext, event.getOriginServerTs(), true)
        } else {
            res = " "
        }

        res?.let {
            mEventFormattedTsMap[event.eventId] = it;
            return res;
        } ?: run {
            return "";
        }
    }

    /**
     * Refresh the messages date list
     */
    private fun refreshRefreshDateList() {
        // build messages timestamps
        val dates = ArrayList<Date>()

        var latestDate = AdapterUtils.zeroTimeDate(Date())

        for (index in 0 until count) {
            val row = getItem(index)
            val event = row!!.event

            if (event.isValidOriginServerTs) {
                latestDate = AdapterUtils.zeroTimeDate(Date(event.getOriginServerTs()))
            }

            dates.add(latestDate)
        }

        synchronized(this) {
            mMessagesDateList = dates
            mReferenceDate = Date()
        }
    }

    /**
     * Converts a difference of days to a string.
     *
     * @param date    the date to display
     * @param nbrDays the number of days between the reference days
     * @return the date text
     */
    private fun dateDiff(date: Date, nbrDays: Long): String {
        if (nbrDays == 0L) {
            return mContext.getString(R.string.today)
        } else if (nbrDays == 1L) {
            return mContext.getString(R.string.yesterday)
        } else if (nbrDays < 7) {
            return SimpleDateFormat("EEEE", mLocale).format(date)
        } else {
            val flags = DateUtils.FORMAT_SHOW_DATE or
                    DateUtils.FORMAT_SHOW_YEAR or
                    DateUtils.FORMAT_ABBREV_ALL or
                    DateUtils.FORMAT_SHOW_WEEKDAY

            val f = Formatter(StringBuilder(50), mLocale)
            return DateUtils.formatDateRange(mContext, f, date.time, date.time, flags).toString()
        }
    }

    /**
     * Compute the message header for the item at position.
     * It might be null.
     *
     * @param position the event position
     * @return the header
     */
    internal fun headerMessage(position: Int): String? {
        var prevMessageDate: Date? = null
        var messageDate: Date? = null

        synchronized(this) {
            if (position > 0 && position < mMessagesDateList.size) {
                prevMessageDate = mMessagesDateList[position - 1]
            }
            if (position < mMessagesDateList.size) {
                messageDate = mMessagesDateList[position]
            }
        }


        messageDate?.let {
            // same day or get the oldest message
            return if (null != prevMessageDate && 0L == prevMessageDate!!.time - messageDate!!.time) {
                null
            } else dateDiff(it, (mReferenceDate.time - messageDate!!.time) / AdapterUtils.MS_IN_DAY)
        } ?: run {
            return null;
        }
    }

    /**
     * Manage the select mode i.e highlight an item when the user tap on it
     *
     * @param contentView the cell view.
     * @param event       the linked event
     */
    private fun manageSelectionMode(contentView: View, event: Event, msgType: Int) {
        val eventId = event.eventId

        val isInSelectionMode = null != currentSelectedEvent
        val isSelected = isInSelectionMode && TextUtils.equals(eventId, currentSelectedEvent!!.eventId)

        // display the action icon when selected
        contentView.findViewById<View>(R.id.messagesAdapter_action_image).visibility = if (isSelected) View.VISIBLE else View.GONE

        val alpha = if (!isInSelectionMode || isSelected) 1.0f else 0.2f

        // the message body is dimmed when not selected
        contentView.findViewById<View>(R.id.messagesAdapter_body_view).alpha = alpha
        contentView.findViewById<View>(R.id.messagesAdapter_avatars_list).alpha = alpha

        val urlsPreviewView = contentView.findViewById<View>(R.id.messagesAdapter_urls_preview_list)
        if (null != urlsPreviewView) {
            urlsPreviewView.alpha = alpha
        }

        val tsTextView = contentView.findViewById<TextView>(R.id.messagesAdapter_timestamp)
        if (isInSelectionMode && isSelected) {
            tsTextView.visibility = View.VISIBLE
        }

        // Show the description of the sticker only on message row click
        if (Event.EVENT_TYPE_STICKER == event.getType()) {
            val stickerMessage = JsonUtils.toStickerMessage(event.content)
            if (null != stickerMessage && isInSelectionMode && isSelected) {
                mHelper.showStickerDescription(contentView, stickerMessage)
            }
        }

        if (event !is EventGroup) {
            contentView.findViewById<View>(R.id.message_timestamp_layout).setOnClickListener {
                if (currentSelectedEvent != null && TextUtils.equals(eventId, currentSelectedEvent!!.eventId)) {
                    onMessageClick(event, getEventText(contentView, event, msgType), contentView.findViewById(R.id.messagesAdapter_action_anchor))
                } else {
                    onEventTap(event)
                }
            }

            contentView.setOnLongClickListener(View.OnLongClickListener {
                if (!mIsSearchMode) {
                    onMessageClick(event, getEventText(contentView, event, msgType), contentView.findViewById(R.id.messagesAdapter_action_anchor))

                    onEventTap(event)
                    return@OnLongClickListener true
                }

                false
            })
        }
    }

    /**
     * Check an event can be merged with the previous one
     *
     * @param event          the event to merge
     * @param position       the event position in the list
     * @param shouldBeMerged true if the event should be merged
     * @return true to merge the event
     */
    internal open fun mergeView(event: Event, position: Int, shouldBeMerged: Boolean): Boolean {
        var shouldBeMerged = shouldBeMerged
        if (shouldBeMerged) {
            shouldBeMerged = null == headerMessage(position)
        }

        return shouldBeMerged && !event.isCallEvent
    }

    /**
     * Return the text displayed in a convertView in the chat history.
     *
     * @param contentView the cell view
     * @return the displayed text.
     */
    private fun getEventText(contentView: View?, event: Event, msgType: Int): String? {
        var text: String? = null

        if (null != contentView) {
            if (ROW_TYPE_CODE == msgType || ROW_TYPE_TEXT == msgType) {
                val message = JsonUtils.toMessage(event.content)
                text = message.body
            } else {
                val bodyTextView = contentView.findViewById<TextView>(R.id.messagesAdapter_body)

                if (null != bodyTextView) {
                    text = bodyTextView.text.toString()
                }
            }
        }

        return text
    }

    /**
     * Add click listeners on content view
     *
     * @param convertView the cell view
     * @param contentView the main message view
     * @param position    the item position
     */
    private fun addContentViewListeners(convertView: View, contentView: View, position: Int, msgType: Int) {
        contentView.setOnClickListener {
            if (null != mVectorMessagesAdapterEventsListener) {
                // GA issue
                if (position < count) {
                    mVectorMessagesAdapterEventsListener!!.onContentClick(position)
                }
            }
        }

        contentView.setOnLongClickListener(View.OnLongClickListener {
            // GA issue
            if (position < count) {
                val row = getItem(position)
                val event = row!!.event

                if (!mIsSearchMode) {
                    onMessageClick(event, getEventText(contentView, event, msgType), convertView.findViewById(R.id.messagesAdapter_action_anchor))

                    onEventTap(event)
                    return@OnLongClickListener true
                }
            }

            true
        })
    }

    /*
     * *********************************************************************************************
     * E2e management
     * *********************************************************************************************
     */

    /**
     * Display the e2e icon
     *
     * @param inflatedView the base view
     * @param position     the item position
     */
    private fun displayE2eIcon(inflatedView: View, position: Int) {
        val e2eIconView = inflatedView.findViewById<ImageView>(R.id.message_adapter_e2e_icon)

        if (null != e2eIconView) {
            val senderMargin = inflatedView.findViewById<View>(R.id.e2e_sender_margin)
            val senderNameView = inflatedView.findViewById<View>(R.id.messagesAdapter_sender)

            val row = getItem(position)
            val event = row!!.event

            if (mE2eIconByEventId.containsKey(event.eventId)) {
                if (null != senderMargin) {
                    senderMargin.visibility = senderNameView.visibility
                }
                e2eIconView.visibility = View.VISIBLE

                val icon = mE2eIconByEventId[event.eventId]

                if (icon is Drawable) {
                    e2eIconView.setImageDrawable(icon as Drawable?)
                } else {
                    e2eIconView.setImageResource(icon as Int)
                }

                val type = getItemViewType(position)

                if (type == ROW_TYPE_IMAGE || type == ROW_TYPE_VIDEO || type == ROW_TYPE_STICKER) {
                    val bodyLayoutView = inflatedView.findViewById<View>(R.id.messagesAdapter_body_layout)
                    val bodyLayout = bodyLayoutView.layoutParams as ViewGroup.MarginLayoutParams
                    val e2eIconViewLayout = e2eIconView.layoutParams as ViewGroup.MarginLayoutParams

                    e2eIconViewLayout.setMargins(bodyLayout.leftMargin, e2eIconViewLayout.topMargin,
                            e2eIconViewLayout.rightMargin, e2eIconViewLayout.bottomMargin)
                    bodyLayout.setMargins(4, bodyLayout.topMargin, bodyLayout.rightMargin, bodyLayout.bottomMargin)
                    e2eIconView.layoutParams = e2eIconViewLayout
                    bodyLayoutView.layoutParams = bodyLayout
                }

                e2eIconView.setOnClickListener {
                    if (null != mVectorMessagesAdapterEventsListener) {
                        mVectorMessagesAdapterEventsListener!!.onE2eIconClick(event, mE2eDeviceByEventId[event.eventId])
                    }
                }
            } else {
                e2eIconView.visibility = View.GONE
                if (null != senderMargin) {
                    senderMargin.visibility = View.GONE
                }
            }
        }
    }

    private fun displayE2eReRequest(inflatedView: View, position: Int) {
        val reRequestE2EKeyTextView = inflatedView.findViewById<TextView>(R.id.messagesAdapter_re_request_e2e_key)

        if (reRequestE2EKeyTextView != null) {
            val row = getItem(position)
            val event = row!!.event

            val sessionId = event.getSessionId()

            if (sessionId != null
                    && event.cryptoError != null
                    && MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE == event.cryptoError.errcode) {

                reRequestE2EKeyTextView.visibility = View.VISIBLE

                if (mSessionIdsWaitingForE2eReRequest.contains(sessionId)) {
                    // Request for this session Id has already been sent
                    reRequestE2EKeyTextView.setText(R.string.e2e_re_request_encryption_key_sent)
                    reRequestE2EKeyTextView.setOnClickListener(null)
                    reRequestE2EKeyTextView.isClickable = false
                } else {
                    // Show the link to re-request the key
                    reRequestE2EKeyTextView.setText(R.string.e2e_re_request_encryption_key)
                    mSession.crypto!!.reRequestRoomKeyForEvent(event)
                    reRequestE2EKeyTextView.setOnClickListener {
                        mSessionIdsWaitingForE2eReRequest.add(sessionId)

                        if (mVectorMessagesAdapterEventsListener != null) {
                            mVectorMessagesAdapterEventsListener!!.onEventAction(event, null, R.id.ic_action_re_request_e2e_key)
                        }

                        // Update the link message (for other events with same sessionId too)
                        notifyDataSetChanged()
                    }
                }
            } else {
                reRequestE2EKeyTextView.visibility = View.GONE
                reRequestE2EKeyTextView.setOnClickListener(null)

                if (sessionId != null && mSessionIdsWaitingForE2eReRequest.contains(sessionId)) {
                    // We have decrypted one (or more) event!
                    if (mVectorMessagesAdapterEventsListener != null) {
                        mVectorMessagesAdapterEventsListener!!.onEventDecrypted()
                    }

                    mSessionIdsWaitingForE2eReRequest.remove(sessionId)
                }
            }
        }
    }

    /**
     * Found the dedicated icon to display for each event id
     */
    private fun manageCryptoEvents() {
        val e2eIconByEventId = HashMap<String, Any>()
        val e2eDeviceInfoByEventId = HashMap<String, MXDeviceInfo>()

        if (mIsRoomEncrypted && mSession.isCryptoEnabled) {
            // the key is "userid_deviceid"
            for (index in 0 until count) {
                val row = getItem(index)
                val event = row!!.event

                // oneself event
                if (event.mSentState != Event.SentState.SENT) {
                    //                    e2eIconByEventId.put(event.eventId, R.drawable.e2e_verified);
                } else if (!event.isEncrypted) {
                    e2eIconByEventId[event.eventId] = mPadlockDrawable
                } else if (null != event.cryptoError) {
                    //                    e2eIconByEventId.put(event.eventId, R.drawable.e2e_blocked);
                } else {
                    val encryptedEventContent = JsonUtils.toEncryptedEventContent(event.wireContent.getAsJsonObject())

                    if (TextUtils.equals(mSession.credentials.deviceId, encryptedEventContent.device_id) && TextUtils.equals(mSession.myUserId, event.getSender())) {
                        //                        e2eIconByEventId.put(event.eventId, R.drawable.e2e_verified);
                        val deviceInfo = mSession.crypto!!
                                .deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm)

                        if (null != deviceInfo) {
                            e2eDeviceInfoByEventId[event.eventId] = deviceInfo
                        }

                    } else {
                        val deviceInfo = mSession.crypto!!
                                .deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm)

                        if (null != deviceInfo) {
                            e2eDeviceInfoByEventId[event.eventId] = deviceInfo
                            if (deviceInfo.isVerified) {
                                //                                e2eIconByEventId.put(event.eventId, R.drawable.e2e_verified);
                            } else if (deviceInfo.isBlocked) {
                                //                                e2eIconByEventId.put(event.eventId, R.drawable.e2e_blocked);
                            } else {
                                //                                e2eIconByEventId.put(event.eventId, R.drawable.e2e_warning);
                                mSession.crypto?.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, deviceInfo.deviceId, event.sender, object : SimpleApiCallback<Void>() {
                                    override fun onSuccess(aVoid: Void?) {
                                        //                                        e2eIconByEventId.put(event.eventId, R.drawable.e2e_verified);
                                    }
                                })
                            }
                        } else {
                            //                            e2eIconByEventId.put(event.eventId, R.drawable.e2e_warning);
                        }
                    }
                }// in error cases, do not display
                // not encrypted event
            }
        }

        mE2eDeviceByEventId = e2eDeviceInfoByEventId
        mE2eIconByEventId = e2eIconByEventId
    }

    override fun resetReadMarker() {
        Log.d(LOG_TAG, "resetReadMarker")
        mReadMarkerEventId = null
    }

    override fun updateReadMarker(readMarkerEventId: String?, readReceiptEventId: String) {
        mReadMarkerEventId = readMarkerEventId
        mReadReceiptEventId = readReceiptEventId
        if (readMarkerEventId != null && readMarkerEventId != mReadMarkerEventId) {
            Log.d(LOG_TAG, "updateReadMarker read marker id has changed: $readMarkerEventId")
            mCanShowReadMarker = true
            notifyDataSetChanged()
        }
    }

    interface ReadMarkerListener {
        fun onReadMarkerDisplayed(event: Event, view: View?)
    }

    /**
     * Specify a listener for read marker
     *
     * @param listener the read marker listener
     */
    fun setReadMarkerListener(listener: ReadMarkerListener) {
        mReadMarkerListener = listener
    }

    /**
     * Set a image getter
     *
     * @param imageGetter the image getter
     */
    fun setImageGetter(imageGetter: VectorImageGetter) {
        mImageGetter = imageGetter
        mHelper.setImageGetter(imageGetter)
    }

    /**
     * Animate a read marker view
     */
    private fun animateReadMarkerView(event: Event, readMarkerView: View?) {
        if (readMarkerView != null && mCanShowReadMarker) {
            mCanShowReadMarker = false
            if (readMarkerView.animation == null) {
                val animation = AnimationUtils.loadAnimation(context, R.anim.unread_marker_anim)
                animation.startOffset = 500
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        readMarkerView.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                readMarkerView.animation = animation
            }

            val uiHandler = Handler(Looper.getMainLooper())

            uiHandler.post {
                if (readMarkerView != null && readMarkerView.animation != null) {
                    readMarkerView.visibility = View.VISIBLE
                    readMarkerView.animation.start()

                    // onAnimationEnd does not seem being called when
                    // NotifyDataSetChanged is called during the animation.
                    // This issue is easily reproducable on an Android 7.1 device.
                    // So, we ensure that the listener is always called.
                    uiHandler.postDelayed({
                        if (mReadMarkerListener != null) {
                            mReadMarkerListener!!.onReadMarkerDisplayed(event, readMarkerView)
                        }
                    }, readMarkerView.animation.duration + readMarkerView.animation.startOffset)

                } else {
                    // The animation has been cancelled by a notifyDataSetChanged
                    // With the membership events merge, it will happen more often than before
                    // because many new back paginate will be required to fill the screen.
                    if (mReadMarkerListener != null) {
                        mReadMarkerListener!!.onReadMarkerDisplayed(event, readMarkerView)
                    }
                }
            }
        }
    }

    /**
     * Tells if the event is the mReadMarkerEventId one.
     *
     * @param event the event to test
     * @return true if the event is the mReadMarkerEventId one.
     */
    private fun isReadMarkedEvent(event: Event): Boolean {
        mReadMarkerEventId?.let {
            // if the read marked event is hidden and the event is a merged one
            return if (mHiddenEventIds.contains(it) && event is EventGroup) {
                // check it is contains in it
                event.contains(mReadMarkerEventId)
            } else event.eventId == mReadMarkerEventId
        } ?: run {
            return false;
        }
    }

    /**
     * Check whether the read marker view should be displayed for the given row
     *
     * @param inflatedView row view
     * @param position     position in adapter
     */
    private fun handleReadMarker(inflatedView: View, position: Int) {
        val row = getItem(position)
        val event = row?.event
        val readMarkerView = inflatedView.findViewById<View>(R.id.message_read_marker)
        if (readMarkerView != null) {
            if (event != null && !event.isDummyEvent && mReadMarkerEventId != null && mCanShowReadMarker
                    && isReadMarkedEvent(event) && !mIsPreviewMode && !mIsSearchMode
                    && (mReadMarkerEventId != mReadReceiptEventId || position < count - 1)) {
                Log.d(LOG_TAG, " Display read marker " + event.eventId + " mReadMarkerEventId" + mReadMarkerEventId)
                // Show the read marker
                animateReadMarkerView(event, readMarkerView)
            } else if (View.GONE != readMarkerView.visibility) {
                Log.v(LOG_TAG, "hide read marker")
                readMarkerView.visibility = View.GONE
            }
        }
    }

    /**
     * Init the read marker
     *
     * @param convertView      the main view
     * @param row              the message row
     * @param isMergedView     true if the message is merged
     * @param avatarLayoutView the avatar layout
     * @param bodyLayoutView   the body layout
     */
    private fun setReadMarker(convertView: View, row: MessageRow, isMergedView: Boolean, avatarLayoutView: View, bodyLayoutView: View) {
        val event = row.event

        // search message mode
        val highlightMakerView = convertView.findViewById<View>(R.id.messagesAdapter_highlight_message_marker)
        val readMarkerView = convertView.findViewById<View>(R.id.message_read_marker)

        if (null != highlightMakerView) {
            // align marker view with the message
            val highlightMakerLayout = highlightMakerView.layoutParams as ViewGroup.MarginLayoutParams
            highlightMakerLayout.setMargins(5, highlightMakerLayout.topMargin, 5, highlightMakerLayout.bottomMargin)

            if (TextUtils.equals(mHighlightedEventId, event.eventId)) {
                if (mIsUnreadViewMode) {
                    highlightMakerView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent))
                    if (readMarkerView != null) {
                        // Show the read marker
                        animateReadMarkerView(event, readMarkerView)
                    }
                } else {
                    val avatarLayout = avatarLayoutView.layoutParams
                    val bodyLayout = bodyLayoutView.layoutParams as ViewGroup.MarginLayoutParams

                    if (isMergedView) {
                        highlightMakerLayout.setMargins(avatarLayout.width + 5, highlightMakerLayout.topMargin, 5, highlightMakerLayout.bottomMargin)
                    } else {
                        highlightMakerLayout.setMargins(5, highlightMakerLayout.topMargin, 5, highlightMakerLayout.bottomMargin)
                    }

                    // move left the body
                    bodyLayout.setMargins(4, bodyLayout.topMargin, 4, bodyLayout.bottomMargin)
                    highlightMakerView.setBackgroundColor(ThemeUtils.getColor(mContext, R.attr.colorAccent))
                }
            } else {
                highlightMakerView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent))
            }

            highlightMakerView.layoutParams = highlightMakerLayout
        }
    }

    /*
     * *********************************************************************************************
     * Handle message click events
     * *********************************************************************************************
     */

    /**
     * The user taps on the action icon.
     *
     * @param event      the selected event.
     * @param textMsg    the event text
     * @param anchorView the popup anchor.
     */
    @SuppressLint("NewApi")
    private fun onMessageClick(event: Event, textMsg: String?, anchorView: View) {
        val popup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            PopupMenu(mContext, anchorView, Gravity.END)
        else
            PopupMenu(mContext, anchorView)

        //        popup.getMenuInflater().inflate(R.menu.vector_room_message_settings, popup.getMenu());
        popup.menuInflater.inflate(R.menu.room_message_setting_popup, popup.menu)
        // force to display the icons
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper!!.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onMessageClick : force to display the icons failed " + e.localizedMessage!!, e)
        }

        val menu = popup.menu
        ThemeUtils.tintMenuIcons(menu, ThemeUtils.getColor(mContext, R.attr.vctr_settings_icon_tint_color))

        // hide entries
        for (i in 0 until menu.size()) {
            menu.getItem(i).isVisible = false
        }

        //        menu.findItem(R.id.ic_action_view_source).setVisible(true);
        //        menu.findItem(R.id.ic_action_view_decrypted_source).setVisible(event.isEncrypted() && (null != event.getClearEvent()));

        if (!TextUtils.isEmpty(textMsg)) {
            menu.findItem(R.id.ic_action_vector_copy).isVisible = true
            menu.findItem(R.id.ic_action_vector_quote).isVisible = true
        }

        if (event.isUploadingMedia(mMediasCache)) {
            menu.findItem(R.id.ic_action_vector_cancel_upload).isVisible = true
        }

        if (event.isDownloadingMedia(mMediasCache)) {
            menu.findItem(R.id.ic_action_vector_cancel_download).isVisible = true
        }

        if (event.canBeResent()) {
            menu.findItem(R.id.ic_action_vector_resend_message).isVisible = true

            //            if (event.isUndelivered() || event.isUnknownDevice()) {
            //                menu.findItem(R.id.ic_action_vector_redact_message).setVisible(true);
            //            }
        } else if (event.mSentState == Event.SentState.SENT) {

            // test if the event can be redacted
            var canBeRedacted = !mIsPreviewMode && !TextUtils.equals(event.getType(), Event.EVENT_TYPE_MESSAGE_ENCRYPTION)

            if (canBeRedacted) {
                // oneself message -> can redact it
                if (TextUtils.equals(event.sender, mSession.myUserId)) {
                    canBeRedacted = true
                } else {
                    // need the minimum power level to redact an event
                    val room = mSession.dataHandler.getRoom(event.roomId)

                    if (null != room && null != room.state.getPowerLevels()) {
                        val powerLevels = room.state.getPowerLevels()
                        canBeRedacted = powerLevels!!.getUserPowerLevel(mSession.myUserId) >= powerLevels!!.redact
                    }
                }
            }

            //            menu.findItem(R.id.ic_action_vector_redact_message).setVisible(canBeRedacted);

            if (Event.EVENT_TYPE_MESSAGE == event.getType()) {
                val message = JsonUtils.toMessage(event.contentAsJsonObject)

                // share / forward the message
                menu.findItem(R.id.ic_action_vector_share).isVisible = !mIsRoomEncrypted
                //                menu.findItem(R.id.ic_action_vector_forward).setVisible(true);

                // save the media in the downloads directory
                if (Message.MSGTYPE_IMAGE == message.msgtype
                        || Message.MSGTYPE_VIDEO == message.msgtype
                        || Message.MSGTYPE_FILE == message.msgtype) {
                    menu.findItem(R.id.ic_action_vector_save).isVisible = true
                }

                // offer to report a message content
                //                menu.findItem(R.id.ic_action_vector_report).setVisible(!mIsPreviewMode && !TextUtils.equals(event.sender, mSession.getMyUserId()));

                if (TextUtils.equals(event.sender, mSession.myUserId) && TextUtils.equals(message.msgtype, Message.MSGTYPE_TEXT)) {
                    menu.findItem(R.id.ic_action_edit).isVisible = true
                }
            }
        }
        menu.findItem(R.id.ic_action_vector_share).isVisible = true
        // e2e
        menu.findItem(R.id.ic_action_device_verification).isVisible = mE2eIconByEventId.containsKey(event.eventId)

        // display the menu
        popup.setOnMenuItemClickListener { item ->
            // warn the listener
            if (null != mVectorMessagesAdapterEventsListener) {
                mVectorMessagesAdapterEventsListener!!.onEventAction(event, textMsg, item.itemId)
            }

            // disable the selection
            cancelSelectionMode()

            true
        }

        // fix an issue reported by GA
        try {
            popup.show()
        } catch (e: Exception) {
            Log.e(LOG_TAG, " popup.show failed " + e.message, e)
        }

    }

    /**
     * Insert the MessageRow in an EventGroup to the front.
     *
     * @param row the messageRow
     * @return true if the MessageRow has been inserted
     */
    private fun addToEventGroupToFront(row: MessageRow): Boolean {
        var eventGroupRow: MessageRow? = null

        if (supportMessageRowMerge(row)) {
            if (count > 0 && getItem(0)!!.event is EventGroup && (getItem(0)!!.event as EventGroup).canAddRow(row)) {
                eventGroupRow = getItem(0)
            }

            if (null == eventGroupRow) {
                eventGroupRow = MessageRow(EventGroup(mHiddenEventIds), null)
                mEventGroups.add(eventGroupRow.event as EventGroup)
                super.insert(eventGroupRow, 0)
                mEventRowMap[eventGroupRow.event.eventId] = row
            }

            (eventGroupRow.event as EventGroup).addToFront(row)
            updateHighlightedEventId()
        }

        return null != eventGroupRow
    }

    /**
     * Add a MessageRow into an EventGroup (if it is possible)
     *
     * @param row the row to added
     */
    private fun addToEventGroup(row: MessageRow) {
        if (supportMessageRowMerge(row)) {
            var eventGroupRow: MessageRow? = null

            // search backward the EventGroup event
            for (i in count - 1 downTo 0) {
                val curRow = getItem(i)

                if (curRow!!.event is EventGroup) {
                    // the event can be added (same day ?)
                    if ((curRow.event as EventGroup).canAddRow(row)) {
                        eventGroupRow = curRow
                    }
                    break
                } else
                // there is no more room member events
                    if (!TextUtils.equals(curRow.event.getType(), Event.EVENT_TYPE_STATE_ROOM_MEMBER)) {
                        break
                    }
            }

            if (null == eventGroupRow) {
                eventGroupRow = MessageRow(EventGroup(mHiddenEventIds), null)
                super.add(eventGroupRow)
                mEventGroups.add(eventGroupRow.event as EventGroup)
                mEventRowMap[eventGroupRow.event.eventId] = eventGroupRow
            }

            (eventGroupRow.event as EventGroup).add(row)
            updateHighlightedEventId()
        }
    }

    /**
     * Remove a message row from the known event groups
     *
     * @param row the message row
     * @return true if the message has been removed
     */
    private fun removeFromEventGroup(row: MessageRow) {
        if (supportMessageRowMerge(row)) {
            val eventId = row.event.eventId
            for (eventGroup in mEventGroups) {
                if (eventGroup.contains(eventId)) {
                    eventGroup.removeByEventId(eventId)

                    if (eventGroup.isEmpty) {
                        mEventGroups.remove(eventGroup)
                        super.remove(row)
                        updateHighlightedEventId()
                        return
                    }
                }
            }
        }
    }

    /**
     * Update the highlighted eventId
     */
    private fun updateHighlightedEventId() {
        mSearchedEventId?.let {
            if (!mEventGroups.isEmpty() && mHiddenEventIds.contains(it)) {
                for (eventGroup in mEventGroups) {
                    if (eventGroup.contains(mSearchedEventId)) {
                        mHighlightedEventId = eventGroup.eventId
                        return
                    }
                }
            }
        }
        mHighlightedEventId = mSearchedEventId
    }

    /**
     * This method is called after a message deletion at position 'position'.
     * It checks and merges if required two EventGroup around the deleted item.
     *
     * @param deletedRow the deleted row
     * @param position   the deleted item position
     */
    private fun checkEventGroupsMerge(deletedRow: MessageRow, position: Int) {
        if (position > 0 && position < count - 1 && !EventGroup.isSupported(deletedRow)) {
            val eventBef = getItem(position - 1)!!.event
            val eventAfter = getItem(position)!!.event

            if (TextUtils.equals(eventBef.getType(), Event.EVENT_TYPE_STATE_ROOM_MEMBER) && eventAfter is EventGroup) {
                var eventGroupBefore: EventGroup? = null

                for (i in position - 1 downTo 0) {
                    if (getItem(i)!!.event is EventGroup) {
                        eventGroupBefore = getItem(i)!!.event as EventGroup
                        break
                    }
                }

                if (null != eventGroupBefore) {
                    val nextRows = ArrayList(eventAfter.rows)
                    // check if the next EventGroup can be added in the previous Event group.
                    // it might be impossible if the messages were not sent the same days
                    if (eventGroupBefore.canAddRow(nextRows[0])) {
                        for (rowToAdd in nextRows) {
                            eventGroupBefore.add(rowToAdd)
                        }
                    }

                    val row = mEventRowMap[eventAfter.eventId]
                    mEventGroups.remove(eventAfter)
                    super.remove(row)

                    updateHighlightedEventId()
                }
            }
        }
    }

    companion object {
        private val LOG_TAG = MessagesAdapter::class.java.simpleName

        internal val ROW_TYPE_TEXT = 0
        internal val ROW_TYPE_IMAGE = 1
        internal val ROW_TYPE_NOTICE = 2
        internal val ROW_TYPE_EMOTE = 3
        internal val ROW_TYPE_FILE = 4
        internal val ROW_TYPE_VIDEO = 5
        internal val ROW_TYPE_MERGE = 6
        internal val ROW_TYPE_HIDDEN = 7
        internal val ROW_TYPE_ROOM_MEMBER = 8
        internal val ROW_TYPE_EMOJI = 9
        internal val ROW_TYPE_CODE = 10
        internal val ROW_TYPE_STICKER = 11
        internal val ROW_TYPE_VERSIONED_ROOM = 12
        internal val ROW_TYPE_TEXT_EDITED = 13
        internal val ROW_TYPE_TEXT_OWNER = 14
        internal val NUM_ROW_TYPES = 15

        /**
         * Tells if the event of type 'eventType' can be merged.
         *
         * @param eventType the event type to test
         * @return true if the event can be merged
         */
        private fun isMergeableEvent(eventType: Int): Boolean {
            return ROW_TYPE_NOTICE != eventType && ROW_TYPE_ROOM_MEMBER != eventType && ROW_TYPE_HIDDEN != eventType
        }
    }
}
