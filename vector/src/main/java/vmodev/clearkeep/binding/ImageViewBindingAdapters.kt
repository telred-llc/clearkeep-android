package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestListener
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

interface ImageViewBindingAdapters {
    @BindingAdapter(value = ["imageUrl", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["room", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, room: Room?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["user", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, user: User?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(imageView: ImageView, status: Byte?)

    @BindingAdapter(value = ["encrypted"], requireAll = false)
    fun bindEncrypted(imageView: ImageView, encrypted: Byte?)

    @BindingAdapter(value = ["validStatus"], requireAll = false)
    fun bindStatusValid(imageView: ImageView, validStatus: Byte?)
}