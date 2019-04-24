package vmodev.clearkeep.ultis

import android.view.View
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import im.vector.databinding.RoomItemBinding
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractRoomItemBinding(_bindingComponent: Any?, _root: View?, _localFieldCount: Int, circleImageViewAvatar: CircleImageView?, circleImageViewStatus: CircleImageView?, textViewName: TextView?, textViewTime: TextView?) : RoomItemBinding(_bindingComponent, _root, _localFieldCount, circleImageViewAvatar, circleImageViewStatus, textViewName, textViewTime), IRoomItemBinding {
    override fun customGetRoom(): Room? {
        return room;
    }

    override fun customSetRoom(room: Room) {
        setRoom(room);
    }
}