package vmodev.clearkeep.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import vmodev.clearkeep.activities.WaitingForVerifyEmailActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.WaitingForVerifyEmailActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.WaitingForVerifyEmailActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractWaitingForVerifyEmailActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractWaitingForVerifyEmailActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeWaitingForVerifyEmailActivity(): WaitingForVerifyEmailActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.WAITING_FOR_VERIFY_EMAIL_ACTIVITY)
        abstract fun bindWaitingForVerifyEmailActivity(activity: WaitingForVerifyEmailActivity): IActivity;

        @Binds
        abstract fun bindWaitingForVerifyEmailActivityViewModelFactory(factory: WaitingForVerifyEmailActivityViewModelFactory): IViewModelFactory<AbstractWaitingForVerifyEmailActivityViewModel>;
    }

    @Binds
    @IntoMap
    @ViewModelKey(AbstractWaitingForVerifyEmailActivityViewModel::class)
    abstract fun bindWaitingForVerifyEmailActivityViewModel(viewModel: WaitingForVerifyEmailActivityViewModel): ViewModel;
}