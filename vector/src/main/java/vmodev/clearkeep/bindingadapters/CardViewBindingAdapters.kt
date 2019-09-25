package vmodev.clearkeep.bindingadapters

import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import vmodev.clearkeep.viewmodelobjects.User

interface CardViewBindingAdapters {
    @BindingAdapter(value = ["listUser", "currentUserId"], requireAll = false)
    fun bindStatusFromListUser(cardView: MaterialCardView, users: List<User>?, currentUserId: String?)

    @BindingAdapter(value = ["status"], requireAll = false)
    fun bindStatus(cardView: MaterialCardView, status : Byte?);
}