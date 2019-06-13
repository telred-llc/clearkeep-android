package vmodev.clearkeep.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import im.vector.Matrix
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.matrixsdk.MatrixServiceImplmenmt
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, MatrixSDKModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDB(application: Application): ClearKeepDatabase {
        return Room.databaseBuilder(application, ClearKeepDatabase::class.java, "clearkeep.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    fun provideUserDao(clearKeepDatabase: ClearKeepDatabase): UserDao {
        return clearKeepDatabase.userDao();
    }

    @Singleton
    @Provides
    fun provideRoomDao(clearKeepDatabase: ClearKeepDatabase): RoomDao {
        return clearKeepDatabase.roomDao();
    }

    @Singleton
    @Provides
    fun provideRoomUserDao(clearKeepDatabase: ClearKeepDatabase): AbstractRoomUserJoinDao {
        return clearKeepDatabase.roomUserJoinDao();
    }

    @Singleton
    @Provides
    fun provideDeviceSettingsDao(clearKeepDatabase: ClearKeepDatabase): AbstractDeviceSettingsDao {
        return clearKeepDatabase.deviceSettingsDao();
    }

    @Singleton
    @Provides
    fun provideBackupKeyPath(clearKeepDatabase: ClearKeepDatabase): AbstractBackupKeyPathDao {
        return clearKeepDatabase.backupKeyPathDao();
    }
}