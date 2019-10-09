package vmodev.clearkeep.workermanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import vmodev.clearkeep.workermanager.interfaces.IWorkerFactory
import javax.inject.Inject
import javax.inject.Provider

class ClearKeepWorkerFactory @Inject constructor(private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<IWorkerFactory>>) : WorkerFactory() {
    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        val foundEntry =
                workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
                ?: throw IllegalArgumentException("Unknown worker class name: $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}