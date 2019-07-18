package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.matrixsdk.MatrixEventHandler
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.matrixsdk.MatrixServiceImplement

@Suppress("unused")
@Module
abstract class MatrixSDKModule {
    @Binds
    abstract fun bindMatrixEventHandler(matrixEventHandler: MatrixEventHandler): IMatrixEventHandler;

    @Binds
    abstract fun bindMatrixService(matrixService: MatrixServiceImplement): MatrixService;
}