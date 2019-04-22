package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener

class FragmentBindingAdapters constructor(val fragment: Fragment) : ImageViewBindingAdapters {
    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment).load(imageUrl).listener(listener).into(imageView);
    }
}