package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.ForgotPasswordVerifyEmailFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.*
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractLoginActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentLoginBindModule::class])
    abstract fun contributeLoginFragment(): LoginFragment;

    @ContributesAndroidInjector(modules = [FragmentSignUpBindModule::class])
    abstract fun contributeSignUpFragment(): SignUpFragment;

    @ContributesAndroidInjector(modules = [FragmentHandlerVerifyEmailBindModule::class])
    abstract fun contributeHandlerVerifyEmailFragment(): HandlerVerifyEmailFragment;

    @ContributesAndroidInjector(modules = [FragmentForgotPasswordBindModule::class])
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment;

    @ContributesAndroidInjector(modules = [FragmentForgotPasswordVerifyEmailBindModule::class])
    abstract fun contributeForgotPasswordVerifyEmailFragment(): ForgotPasswordVerifyEmailFragment;

    @Module
    abstract class FragmentLoginBindModule {
        @Binds
        @Named(IFragment.LOGIN_FRAGMENT)
        abstract fun bindLoginFragment(fragment: LoginFragment): IFragment;

        @Binds
        abstract fun bindLoginFragmentViewModelFactory(factory: LoginFragmentViewModelFactory): IViewModelFactory<AbstractLoginFragmentViewModel>;
    }

    @Module
    abstract class FragmentSignUpBindModule {
        @Binds
        @Named(IFragment.SIGNUP_FRAGMENT)
        abstract fun bindSignUpFragment(fragment: SignUpFragment): IFragment;

        @Binds
        abstract fun bindSignUpFragmentViewModelFactory(factory: SignUpFragmentViewModelFactory): IViewModelFactory<AbstractSignUpFragmentViewModel>;
    }

    @Module
    abstract class FragmentHandlerVerifyEmailBindModule {
        @Binds
        @Named(IFragment.HANDLER_VERIFY_EMAIL_FRAGMENT)
        abstract fun bindHandlerVerifyEmailFragment(fragment: HandlerVerifyEmailFragment): IFragment;

        @Binds
        abstract fun bindHandlerVerifyEmailFragmentViewModeFactory(factory: HandlerVerifyEmailFragmentViewModelFactory): IViewModelFactory<AbstractHandlerVerifyEmailFragmentViewModel>;
    }

    @Module
    abstract class FragmentForgotPasswordBindModule {
        @Binds
        @Named(IFragment.FORGOT_PASSWORD_FRAGMENT)
        abstract fun bindForgotPasswordFragment(fragment: ForgotPasswordFragment): IFragment;

        @Binds
        abstract fun bindForgotPasswordFragmentViewModelFactory(factory: ForgotPasswordFragmentViewModelFactory): IViewModelFactory<AbstractForgotPasswordFragmentViewModel>;
    }

    @Module
    abstract class FragmentForgotPasswordVerifyEmailBindModule {
        @Binds
        @Named(IFragment.FORGOT_PASSWORD_VERIFY_EMAIL_FRAGMENT)
        abstract fun bindForgotPasswordVerifyEmailFragment(fragment: ForgotPasswordVerifyEmailFragment): IFragment;

        @Binds
        abstract fun bindForgotPasswordVerifyEmailFragmentViewModelFactory(factory: ForgotPasswordVerifyEmailFragmentViewModelFactory): IViewModelFactory<AbstractForgotPasswordVerifyEmailFragmentViewModel>
    }
}