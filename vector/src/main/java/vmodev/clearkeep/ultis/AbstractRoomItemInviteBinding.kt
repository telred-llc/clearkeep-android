package vmodev.clearkeep.ultis

import android.view.View
import android.widget.Button
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import im.vector.databinding.RoomInviteItemBinding
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractRoomItemInviteBinding(_bindingComponent: Any?, _root: View?, _localFieldCount: Int, buttonDecline: Button?, buttonJoin: Button?, circleImageViewAvatar: CircleImageView?, circleImageViewStatus: CircleImageView?, textViewName: TextView?, textViewNotify: TextView?, textViewTime: TextView?) : RoomInviteItemBinding(_bindingComponent, _root, _localFieldCount, buttonDecline, buttonJoin, circleImageViewAvatar, circleImageViewStatus, textViewName, textViewNotify, textViewTime), IRoomItemBinding {
    override fun customGetRoom(): Room? {
        return room;
    }

    override fun customSetRoom(room: Room) {
        setRoom(room);
    }
}