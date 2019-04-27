package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener

interface ImageViewBindingAdapters {
    @BindingAdapter(value = ["imageUrl", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(imageView: ImageView, status: Byte?)
}