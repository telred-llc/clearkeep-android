package vmodev.clearkeep.bindingadapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestListener
import org.matrix.androidsdk.rest.model.message.ImageMessage
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.jsonmodels.FileContent
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

interface ImageViewBindingAdapters {
    @BindingAdapter(value = ["imageUrl", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["room", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, room: Room?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["publicRoom", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, room: PublicRoom?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["user", "requestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, user: User?, listener: RequestListener<Drawable?>?);

    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(imageView: ImageView, status: Byte?)

    @BindingAdapter(value = ["encrypted"], requireAll = false)
    fun bindEncrypted(imageView: ImageView, encrypted: Byte?)

    @BindingAdapter(value = ["validStatus"], requireAll = false)
    fun bindStatusValid(imageView: ImageView, validStatus: Byte?)

    @BindingAdapter(value = ["listUser", "currentUserId"], requireAll = false)
    fun bindStatusFromListUser(imageView: ImageView, users: List<User>?, currentUserId: String?)

    @BindingAdapter(value = ["imageFile"], requireAll = false)
    fun bindImageFile(imageView: ImageView, fileContent: ImageMessage)
}