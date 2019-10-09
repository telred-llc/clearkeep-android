package vmodev.clearkeep.di.worker

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import im.vector.services.PushSimulatorWorker
import vmodev.clearkeep.workermanager.UpdateDatabaseFromMatrixEventWorker
import vmodev.clearkeep.workermanager.interfaces.IWorkerFactory

@Module
@Suppress("unused")
abstract class AbstractWorkerModule {
    @Binds
    @IntoMap
    @WokerKey(UpdateDatabaseFromMatrixEventWorker.UpdateRoomNameWorker::class)
    abstract fun bindUpdateRoomNameWorker(factory : UpdateDatabaseFromMatrixEventWorker.UpdateRoomNameWorker.Factory) : IWorkerFactory;

    @Binds
    @IntoMap
    @WokerKey(UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker::class)
    abstract fun bindInsertMessageWorker(factory : UpdateDatabaseFromMatrixEventWorker.InsertNewEventWorker.Factory) : IWorkerFactory;

    @Binds
    @IntoMap
    @WokerKey(PushSimulatorWorker::class)
    abstract fun bindPushSimulatorWorker(factory : PushSimulatorWorker.Factory) : IWorkerFactory;

    @Module(includes = [AssistedInject_AbstractWorkerModule_ClearKeepAssistedInjectModule::class])
    @AssistedModule
    interface ClearKeepAssistedInjectModule
}