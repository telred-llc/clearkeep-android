package vmodev.clearkeep.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.*
import javax.inject.Singleton

@Module
@Suppress("unused")
abstract class AbstractDatabaseModule {
    @Module
    companion object {
        @Singleton
        @Provides
        @JvmStatic
        fun provideDB(application: IApplication): ClearKeepDatabase {
            return Room.databaseBuilder(application.getApplication(), ClearKeepDatabase::class.java, "clearkeep.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideUserDao(clearKeepDatabase: ClearKeepDatabase): AbstractUserDao {
            return clearKeepDatabase.userDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideRoomDao(clearKeepDatabase: ClearKeepDatabase): AbstractRoomDao {
            return clearKeepDatabase.roomDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideRoomUserDao(clearKeepDatabase: ClearKeepDatabase): AbstractRoomUserJoinDao {
            return clearKeepDatabase.roomUserJoinDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideDeviceSettingsDao(clearKeepDatabase: ClearKeepDatabase): AbstractDeviceSettingsDao {
            return clearKeepDatabase.deviceSettingsDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideBackupKeyPath(clearKeepDatabase: ClearKeepDatabase): AbstractBackupKeyPathDao {
            return clearKeepDatabase.backupKeyPathDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideSignature(clearKeepDatabase: ClearKeepDatabase): AbstractSignatureDao {
            return clearKeepDatabase.signatureDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideKeyBackup(clearKeepDatabase: ClearKeepDatabase): AbstractKeyBackupDao {
            return clearKeepDatabase.keyBackupDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideLocalSettings(clearKeepDatabase: ClearKeepDatabase): AbstractLocalSettingsDao {
            return clearKeepDatabase.localSettingsDao();
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideMessageDao(clearKeepDatabase: ClearKeepDatabase): AbstractMessageDao {
            return clearKeepDatabase.messageDao();
        }
    }
}