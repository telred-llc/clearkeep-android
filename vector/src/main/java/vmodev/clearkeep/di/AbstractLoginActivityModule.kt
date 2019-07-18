package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import vmodev.clearkeep.activities.LoginActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.ForgotPasswordFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.LoginActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.matrixsdk.MatrixLoginService
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixLoginService
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodels.*
import vmodev.clearkeep.viewmodels.interfaces.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@Suppress("unused")
abstract class AbstractLoginActivityModule {
    @ContributesAndroidInjector(modules = [AbstractLoginActivityFragmentBuilderModule::class])
    abstract fun contributeLoginActivity(): LoginActivity;

    @Binds
    @Named(IActivity.LOGIN_ACTIVITY)
    abstract fun bindLoginActivity(activity: LoginActivity): IActivity;

    @Binds
    abstract fun bindLoginActivityViewModelFactory(factory: LoginActivityViewModelFactory): IViewModelFactory<AbstractLoginActivityViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AbstractLoginActivityViewModel::class)
    abstract fun bindLoginActivityViewModel(viewModel: LoginActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractLoginFragmentViewModel::class)
    abstract fun bindLoginFragmentViewModel(viewModel: LoginFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSignUpFragmentViewModel::class)
    abstract fun bindSignUpFragmentViewModel(viewModel: SignUpFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractHandlerVerifyEmailFragmentViewModel::class)
    abstract fun bindHandlerVerifyEmailFragmentViewModel(viewModel: HandlerVerifyEmailFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractForgotPasswordFragmentViewModel::class)
    abstract fun bindForgotPasswordFragmentViewModel(viewModel: ForgotPasswordFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractForgotPasswordVerifyEmailFragmentViewModel::class)
    abstract fun bindForgotPasswordVerifyEmailFragmentViewModel(viewModel: ForgotPasswordVerifyEmailFragmentViewModel): ViewModel;

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Singleton
        fun provideMatrixLoginService(application: IApplication): IMatrixLoginService {
            return MatrixLoginService(application);
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideLoginRepository(matrixLoginService: IMatrixLoginService): LoginRepository {
            return LoginRepository(matrixLoginService);
        }
    }
}