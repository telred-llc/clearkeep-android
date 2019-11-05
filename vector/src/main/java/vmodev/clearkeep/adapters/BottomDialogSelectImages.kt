package vmodev.clearkeep.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import im.vector.R

class BottomDialogSelectImages() : BaseAdapter() {
    private val arrayTitles: Array<Int> = arrayOf(R.string.option_take_photo, R.string.option_choose_from_library, R.string.cancel)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.item_slect_image, null);
        view.findViewById<TextView>(R.id.tvTitle).setText(arrayTitles[p0])
        return view;
    }

    override fun getItem(p0: Int): Any {
        return arrayTitles[p0];
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arrayTitles.size;
    }

}