package vmodev.clearkeep.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import im.vector.R
import im.vector.util.VectorUtils
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import kotlin.collections.ArrayList

class DirectMessageRecyclerViewAdapter(rooms: List<Room>, invitation: List<Room>, mxSession: MXSession) : RecyclerView.Adapter<DirectMessageRecyclerViewAdapter.ViewHolder>() {

    public var rooms: List<Room> = rooms;
    public var invitations: List<Room> = invitation;
    private var mxSession: MXSession = mxSession;
    private lateinit var context: Context;
    public val publishSubject: PublishSubject<OnClickObject> = PublishSubject.create();
    private var arrayLayout: Array<Int> = arrayOf(R.layout.invite_message_item, R.layout.direct_message_item);

    private var listItem = ArrayList<Room>();

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context;
        val view = LayoutInflater.from(p0.context).inflate(arrayLayout[p1], p0, false);
        return createViewHoler(p1, view, publishSubject);
    }

    private fun createViewHoler(viewType: Int, view: View, publishSubject: PublishSubject<OnClickObject>): ViewHolder {
        if (viewType == 0)
            return ViewHolderInvitation(view, publishSubject);
        return ViewHolderRooms(view, publishSubject);
    }

    override fun getItemCount(): Int {
        return listItem.size;
    }

    override fun getItemViewType(position: Int): Int {
        if (position < invitations.size)
            return 0;
        return 1;
    }

    public fun updateData() {
        listItem.clear();
        listItem.addAll(invitations);
        listItem.addAll(rooms);
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        VectorUtils.loadRoomAvatar(context, mxSession, p0.imageViewAvatar, listItem[p1]);
        p0.name.text = listItem[p1].getRoomDisplayName(context);
        p0.room = listItem[p1];
    }

    open class ViewHolder(view: View, publishSubject: PublishSubject<OnClickObject>) : RecyclerView.ViewHolder(view) {
        var imageViewAvatar: CircleImageView = view.findViewById(R.id.circle_image_view_avatar);
        var name: TextView = view.findViewById(R.id.text_view_name);
        lateinit var room: Room;
        protected var publishSubject = publishSubject;
    }

    class ViewHolderRooms(view: View, publishSubject: PublishSubject<OnClickObject>) : ViewHolder(view, publishSubject) {
        init {
            view.setOnClickListener { v ->
                kotlin.run {
                    publishSubject.onNext(OnClickObject(0, room));
                }
            }
        }
    }

    class ViewHolderInvitation(itemView: View, publishSubject: PublishSubject<OnClickObject>) : ViewHolder(itemView, publishSubject) {
        var buttonDecline: Button = itemView.findViewById(R.id.button_decline);
        var buttonJoin: Button = itemView.findViewById(R.id.button_join)

        init {
            buttonDecline.setOnClickListener { v -> kotlin.run { publishSubject.onNext(OnClickObject(1, room)) } };
            buttonJoin.setOnClickListener { v -> kotlin.run { publishSubject.onNext(OnClickObject(2, room)) } };
            itemView.setOnClickListener { v -> kotlin.run { publishSubject.onNext(OnClickObject(3, room)) } };
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        publishSubject.onComplete();
    }

    class OnClickObject(clickType: Int, room: Room?) {
        var clickType = clickType;
        var room = room;
    }
}