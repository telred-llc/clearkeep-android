package vmodev.clearkeep.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import vmodev.clearkeep.databases.*
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, MatrixSDKModule::class, AbstractMatrixSDKModule::class])
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
    fun provideMessageDao(clearKeepDatabase: ClearKeepDatabase): AbstractMessageDao {
        return clearKeepDatabase.messageDao();
    }
}