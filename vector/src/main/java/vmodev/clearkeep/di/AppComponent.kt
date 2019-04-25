package vmodev.clearkeep.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import vmodev.clearkeep.activities.PreviewInviteRoomActivity
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.DaggerVectorApp
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AndroidInjectionModule::class
    , AppModule::class, HomeScreenActivityModule::class, ProfileActivityModule::class
    , PreviewInviteRoomActivityModule::class, FindAndCreateNewConversationActivityModule::class])
interface AppComponent : AndroidInjector<ClearKeepApplication> {
    override fun inject(instance: ClearKeepApplication?)
//    fun inject(application: Application)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}