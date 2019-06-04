package vmodev.clearkeep.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import im.vector.R

class BottomDiaglogImageSendFile constructor(private val arrayTitles : Array<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_bottom_dialog_room_long_click, null);
        view.findViewById<ImageView>(R.id.image_view_icon).visibility = View.GONE;
        view.findViewById<TextView>(R.id.text_view_title).text = arrayTitles[position]
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