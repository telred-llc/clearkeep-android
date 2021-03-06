package vmodev.clearkeep.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import im.vector.R

class BottomDialogRoomLongClick constructor(state: Byte = 0x02) : BaseAdapter() {

    private var arrayTitles: Array<Int> = arrayOf(R.string.turn_on_room_notification, R.string.add_to_favourite, R.string.setting, R.string.leave/*, R.string.preview_message*/)
    private var arrayIcons: Array<Int> = arrayOf(R.drawable.ic_notification_setting, R.drawable.ic_room_favourite, R.drawable.ic_room_settings, R.drawable.ic_leave_room, R.drawable.ic_forward_black_24dp)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_bottom_dialog_room_long_click, null);
        view.findViewById<ImageView>(R.id.image_view_icon).setImageResource(arrayIcons[position])
        view.findViewById<TextView>(R.id.text_view_title).setText(arrayTitles[position])
        return view;
    }

    init {
        when (state) {
            0x01.toByte(), 0x02.toByte() -> {
                arrayTitles[0] = R.string.turn_off_room_notification
                arrayIcons[0] = R.drawable.ic_notifications_off_black_24dp
            }
            0x04.toByte() -> {
                arrayTitles[0] = R.string.turn_on_room_notification
                arrayIcons[0] = R.drawable.ic_notifications_black_24dp
            }
        }
    }

    override fun getItem(position: Int): Any {
        return arrayTitles[position];
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getCount(): Int {
        return arrayTitles.size;
    }
}