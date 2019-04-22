package vmodev.clearkeep.binding

import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener

class ActivityBindingAdapters constructor(val activity: FragmentActivity) : ImageViewBindingAdapters {
    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(activity).load(imageUrl).listener(listener).into(imageView);
    }
}