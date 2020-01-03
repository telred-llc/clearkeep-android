package vmodev.clearkeep.features.jobschedule

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class Util {
    companion object {
        var jobSechudler: JobScheduler? = null

        @RequiresApi(Build.VERSION_CODES.M)
        fun scheduleJob(mContext: Context) {
            val serviceComponent = ComponentName(mContext, ApplicationJobService::class.java)
            val builder = JobInfo.Builder(1, serviceComponent)
            builder.setMinimumLatency(1000)
            builder.setOverrideDeadline(6000)
            builder.setPersisted(true)
            jobSechudler = mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?
            jobSechudler?.let { it.schedule(builder.build()) }

        }

    }


}