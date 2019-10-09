package vmodev.clearkeep.workermanager.interfaces

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface IWorkerFactory {
    fun create(appContext : Context, params : WorkerParameters) : ListenableWorker;
}