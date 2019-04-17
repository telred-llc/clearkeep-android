package vmodev.clearkeep.di

import android.app.Application
import dagger.Module
import dagger.Provides
import im.vector.Matrix
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.matrixsdk.MatrixServiceImplmenmt
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideMatrixSession(application : Application) : MXSession{
        return Matrix.getInstance(application).defaultSession;
    }

    @Singleton
    @Provides
    fun provideMatrixService(mxSession: MXSession) : MatrixService{
        return MatrixServiceImplmenmt(mxSession);
    }
}