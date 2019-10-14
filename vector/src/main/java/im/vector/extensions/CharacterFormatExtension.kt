package im.vector.extensions

import android.text.TextUtils



fun String.formatMessageEdit(message: String) =if(!TextUtils.isEmpty(message)) message.replace("*", "").trim() else message
