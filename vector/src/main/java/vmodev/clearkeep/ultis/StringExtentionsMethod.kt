package vmodev.clearkeep.ultis

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.net.URI

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