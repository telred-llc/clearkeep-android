package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import im.vector.R
import vmodev.clearkeep.ultis.toDateTime
import vmodev.clearkeep.viewmodelobjects.Room

class FragmentBindingAdapters constructor(val fragment: Fragment) : ImageViewBindingAdapters, TextViewBindingAdapters {
    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment).load(imageUrl).listener(listener).placeholder(R.drawable.ic_launcher_app).into(imageView);
    }

    override fun bindTime(textView: TextView, timeStamp: Long?) {
        timeStamp?.let { l -> fragment.context?.let { context -> textView.text = l.toDateTime(context) } }
    }

    override fun bindStatus(imageView: ImageView, status: Byte?) {
        status?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.color.main_text_color_hint else R.color.app_green); }
    }

    override fun bindRoomsHighlightCount(textView: TextView, rooms: List<Room>?) {
        var count: Int = 0;
        rooms?.forEach { t: Room? ->
            t?.let {
                count += it.highlightCount
                if (it.type == 1 or 64 || it.type == 2 or 64)
                    count++;
            }
        }
        textView.text = count.toString();
        if (count == 0)
            textView.visibility = View.INVISIBLE;
        else
            textView.visibility = View.VISIBLE
    }
}