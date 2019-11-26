package vmodev.clearkeep.bindingadapters

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.ui.themes.ThemeUtils
import im.vector.util.VectorUtils
import org.matrix.androidsdk.crypto.MXDecryptionException
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.enums.CallStatusEnum
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.jsonmodels.MessageContent
import vmodev.clearkeep.ultis.FormatString
import vmodev.clearkeep.ultis.toDateTime
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

class BindingAdaptersImplement : ImageViewBindingAdapters, TextViewBindingAdapters, ISwitchCompatViewBindingAdapters, CardViewBindingAdapters {

    override fun bindImage(imageView: ImageView, room: PublicRoom?, listener: RequestListener<Drawable?>?) {
        room?.let {
            if (room.avatarUrl.isNullOrEmpty()) {
                val bitmap = VectorUtils.getAvatar(imageView.context, VectorUtils.getAvatarColor(room.roomId), if (room.name.isNullOrEmpty()) room.roomId else room.name, true);
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(imageView.context).load(room.avatarUrl).centerCrop().error(R.drawable.ic_launcher_app).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        }
    }

    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(imageView.context).load(imageUrl).centerCrop().into(imageView);
        }
    }

    override fun bindTime(textView: TextView, timeStamp: Long?) {
        timeStamp?.let { l -> textView.text = l.toDateTime(textView.context) }
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
                if (it.notifyCount > 0)
                    count++;
            }
        }
        textView.text = count.toString();
        if (count == 0)
            textView.visibility = View.INVISIBLE;
        else
            textView.visibility = View.VISIBLE
    }

    override fun bindEncrypted(imageView: ImageView, encrypted: Byte?) {
        encrypted?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.drawable.ic_lock_outline_grey_24dp else R.drawable.ic_lock_outline_green_24dp); }
    }

    override fun bindStatus(switchCompat: SwitchCompat, status: Byte?) {
        status?.let {
            switchCompat.isChecked = status.compareTo(0) != 0
        }
    }

    override fun bindDecryptMessage(textView: TextView, message: Message?) {
        message?.let {
            val session = Matrix.getInstance(textView.context.applicationContext).defaultSession;
            val parser = JsonParser();
            val gson = Gson();
            val event = Event(message?.messageType, parser.parse(message.encryptedContent).asJsonObject, message.userId, message.roomId);
            Log.d("MessageType", message.encryptedContent + "--" + message.messageType)
            if (message.messageType.compareTo("m.room.encrypted") != 0) {
                textView.text = message.encryptedContent;
            } else {
                try {
                    val result = session.dataHandler.crypto?.decryptEvent(event, null);
                    result?.let {
                        val json = result.mClearEvent.asJsonObject;
                        val type = json.get("type").asString;
                        Log.d("LastMsg", type);
                        if (!type.isNullOrEmpty() && type.compareTo("m.room.message") == 0) {
                            val message = gson.fromJson(result.mClearEvent, MessageContent::class.java);
                            textView.text = message.getContent().getBody();
                        }
                    }
                } catch (e: MXDecryptionException) {
                    textView.text = message.encryptedContent;
                }
            }
        }
    }

    override fun bindStatusValid(imageView: ImageView, validStatus: Byte?) {
        validStatus?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.drawable.ic_lock_outline_grey_24dp else R.drawable.ic_lock_outline_green_24dp); }
    }

    override fun bindImage(imageView: ImageView, room: Room?, listener: RequestListener<Drawable?>?) {
        room?.let {
            if (room.avatarUrl.isNullOrEmpty()) {
                val bitmap = VectorUtils.getAvatar(imageView.context, VectorUtils.getAvatarColor(room.id), if (room.name.isNullOrEmpty()) room.id else room.name, true);
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(imageView.context).load(room.avatarUrl).centerCrop().error(R.drawable.ic_launcher_app).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        }
    }

    override fun bindImage(imageView: ImageView, user: User?, listener: RequestListener<Drawable?>?) {
        user?.let {
            if (user.avatarUrl.isEmpty()) {
                val bitmap = VectorUtils.getAvatar(imageView.context, VectorUtils.getAvatarColor(user.id), if (user.name.isEmpty()) user.id else user.name, true);
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(imageView.context).load(user.avatarUrl).centerCrop().into(imageView);
            }
        }
    }

    override fun bindStatusFromListUser(imageView: ImageView, users: List<User>?, currentUserId: String?) {
        users?.let { us ->
            currentUserId?.let { id ->
                for (u in us) {
                    if (u.id.compareTo(id) != 0 && u.status.compareTo(0) != 0) {
                        imageView.setImageResource(R.color.app_green);
                        break;
                    } else {
                        imageView.setImageResource(R.color.main_text_color_hint);
                    }
                }
            }
        }
    }

    override fun bindStatusFromListUser(cardView: MaterialCardView, users: List<User>?, currentUserId: String?) {
        users?.let { us ->
            currentUserId?.let { id ->
                for (u in us) {
                    if (u.id.compareTo(id) != 0 && u.status.compareTo(0) != 0) {
                        cardView.setCardBackgroundColor(ResourcesCompat.getColor(cardView.context.resources, R.color.app_green, null));
                        break;
                    } else {
                        cardView.setCardBackgroundColor(ResourcesCompat.getColor(cardView.context.resources, R.color.main_text_color_hint, null));
                    }
                }
            }
        }
    }

    override fun bindStatus(cardView: MaterialCardView, status: Byte?) {
        status?.let {
            cardView.setCardBackgroundColor(if (it.compareTo(0) == 0)
                ResourcesCompat.getColor(cardView.context.resources, R.color.main_text_color_hint, null)
            else
                ResourcesCompat.getColor(cardView.context.resources, R.color.app_green, null))
        }
    }

    override fun bindFormatName(textView: TextView, room: Room?) {
        room?.name?.let {
            when {
                it.contains("Invite from") -> textView.text = it.replace("Invite from", "").trim()
                it.contains("Call:") -> textView.text = it.replace("Call:", "").trim()
                else -> textView.text = it
            }
        }
    }

    override fun bindCheckMissCall(textView: TextView, message: Message?) {
        message?.encryptedContent?.let {
            if (message.encryptedContent == CallStatusEnum.MISS_CALL.value) {
                textView.setTextColor(ResourcesCompat.getColor(textView.resources, R.color.color_text_miss_Call, null))
            } else {
                textView.setTextColor(ThemeUtils.getColor(textView.context, R.attr.color_text_app_default))
            }
        }
    }


}