package vmodev.clearkeep.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import im.vector.R
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room

class DirectMessageRecyclerViewAdapter (rooms : Array<Room>, mxSession: MXSession) : RecyclerView.Adapter<DirectMessageRecyclerViewAdapter.ViewHolder>() {

    private var rooms : Array<Room> = rooms;
    private var mxSession : MXSession = mxSession;
    private lateinit var context : Context;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context;
        val view = LayoutInflater.from(p0.context).inflate(R.layout.direct_message_item, p0, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return this.rooms.size;
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        VectorUtils.loadRoomAvatar(context, mxSession, p0.imageViewAvatar, rooms[p1]);
        p0.name.text = rooms[p1].getRoomDisplayName(context);
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imageViewAvatar : CircleImageView = view.findViewById(R.id.circle_image_view_avatar);
        var name : TextView = view.findViewById(R.id.text_view_name);

    }
}