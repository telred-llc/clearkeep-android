package vmodev.clearkeep.ultis

import android.content.Context
import android.os.Environment
import org.matrix.androidsdk.MXSession
import java.io.File
import java.io.FileWriter
import java.net.URI
import java.text.DecimalFormat
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.pow

fun String.writeToInternal(folderPath: String, fileName: String): String {
    val file = File(folderPath);
    val paths = file.path.split(":");
    try {
        val path = File(Environment.getExternalStoragePublicDirectory(paths[1]), "")
        val gpxFile = File(path, fileName)
        val writer = FileWriter(gpxFile)
        writer.append(this)
        writer.flush()
        writer.close()

    } catch (e: Exception) {
        e.printStackTrace()

    }
    return this;
}

fun String.writeToInternalCache(fileName: String): File? {
    try {
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)
        val writer = FileWriter(path)
        writer.append(this)
        writer.flush()
        writer.close()
        return path;

    } catch (e: Exception) {
        e.printStackTrace()
        return null;
    }
}

fun String.isEmailValid(): Boolean {
    val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
    val matcher = pattern.matcher(this);

    return matcher.matches();
}

fun String.matrixUrlToRealUrl(session: MXSession?): String? {
    if (session == null)
        return null;
    return session.contentManager.getDownloadableUrl(this, false);

}
fun String.formatSizeData(size: Long):String?{
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    var result: String? = null
    result = DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    return result
}