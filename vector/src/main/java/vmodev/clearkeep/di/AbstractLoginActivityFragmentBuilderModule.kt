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
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment;

    @Binds
    @Named(IFragment.LOGIN_FRAGMENT)
    abstract fun bindLoginFragment(fragment: LoginFragment): IFragment;

    @Binds
    abstract fun bindLoginFragmentViewModelFactory(factory: LoginFragmentViewModelFactory): IViewModelFactory<AbstractLoginFragmentViewModel>;

    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment;

    @Binds
    @Named(IFragment.SIGNUP_FRAGMENT)
    abstract fun bindSignUpFragment(fragment: SignUpFragment): IFragment;

    @Binds
    abstract fun bindSignUpFragmentViewModelFactory(factory: SignUpFragmentViewModelFactory): IViewModelFactory<AbstractSignUpFragmentViewModel>;

    @ContributesAndroidInjector
    abstract fun contributeHandlerVerifyEmailFragment(): HandlerVerifyEmailFragment;

    @Binds
    @Named(IFragment.HANDLER_VERIFY_EMAIL_FRAGMENT)
    abstract fun bindHandlerVerifyEmailFragment(fragment: HandlerVerifyEmailFragment): IFragment;

    @Binds
    abstract fun bindHandlerVerifyEmailFragmentViewModeFactory(factory: HandlerVerifyEmailFragmentViewModelFactory): IViewModelFactory<AbstractHandlerVerifyEmailFragmentViewModel>;

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment;

    @Binds
    @Named(IFragment.FORGOT_PASSWORD_FRAGMENT)
    abstract fun bindForgotPasswordFragment(fragment: ForgotPasswordFragment): IFragment;

    @Binds
    abstract fun bindForgotPasswordFragmentViewModelFactory(factory: ForgotPasswordFragmentViewModelFactory): IViewModelFactory<AbstractForgotPasswordFragmentViewModel>;

    @ContributesAndroidInjector
    abstract fun contributeForgotPasswordVerifyEmailFragment(): ForgotPasswordVerifyEmailFragment;

    @Binds
    @Named(IFragment.FORGOT_PASSWORD_VERIFY_EMAIL_FRAGMENT)
    abstract fun bindForgotPasswordVerifyEmailFragment(fragment: ForgotPasswordVerifyEmailFragment): IFragment;

    @Binds
    abstract fun bindForgotPasswordVerifyEmailFragmentViewModelFactory(factory: ForgotPasswordVerifyEmailFragmentViewModelFactory): IViewModelFactory<AbstractForgotPasswordVerifyEmailFragmentViewModel>
}