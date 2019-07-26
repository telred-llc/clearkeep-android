package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.repositories.interfaces.IUserRepository
import javax.inject.Singleton

@Module
@Suppress("unused")
abstract class AbstractRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepository: UserRepository): IUserRepository;
}