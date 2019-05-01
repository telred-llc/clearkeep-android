package vmodev.clearkeep.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import im.vector.R
import vmodev.clearkeep.ultis.toDateTime

class ActivityBindingAdapters constructor(val activity: FragmentActivity) : ImageViewBindingAdapters, TextViewBindingAdapters {
    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(activity).load(imageUrl).listener(listener).placeholder(R.drawable.ic_launcher_app).into(imageView);
    }

    override fun bindTime(textView: TextView, timeStamp: Long?) {
        timeStamp?.let { l -> textView.text = l.toDateTime(activity) }
    }

    override fun bindStatus(imageView: ImageView, status: Byte?) {
        status?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.color.main_text_color_hint else R.color.app_green); }
    }
}