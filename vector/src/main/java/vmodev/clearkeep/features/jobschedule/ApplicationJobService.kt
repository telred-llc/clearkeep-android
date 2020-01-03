package vmodev.clearkeep.features.jobschedule

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import vmodev.clearkeep.ultis.Debug
import java.text.SimpleDateFormat

@TargetApi(Build.VERSION_CODES.O)
class ApplicationJobService : JobService() {

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        Debug.e("--- onStartJob: ${dateFormat.format(System.currentTimeMillis())}")
        Util.scheduleJob(applicationContext)
        jobFinished(params, false)
        return false
    }

}