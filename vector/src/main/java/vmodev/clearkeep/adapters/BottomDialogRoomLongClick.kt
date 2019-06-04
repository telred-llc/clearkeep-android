package vmodev.clearkeep.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import im.vector.R

class BottomDialogRoomLongClick : BaseAdapter() {

    private val arrayTitles: Array<Int> = arrayOf(R.string.turn_on_room_notification, R.string.add_to_favourite, R.string.setting, R.string.leave)
    private val arrayIcons: Array<Int> = arrayOf(R.drawable.ic_notifications_none_black_24dp, R.drawable.ic_star_black_24dp, R.drawable.ic_settings_black_24dp, R.drawable.ic_exit_to_app_black_24dp)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_bottom_dialog_room_long_click, null);
        view.findViewById<ImageView>(R.id.image_view_icon).setImageResource(arrayIcons[position])
        view.findViewById<TextView>(R.id.text_view_title).setText(arrayTitles[position])
        return view;
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