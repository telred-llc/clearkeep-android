package im.vector.extensions

import android.text.TextUtils


fun String.formatMessageEdit(message: String) = if (!TextUtils.isEmpty(message)) message.replaceFirst("*","")else message
