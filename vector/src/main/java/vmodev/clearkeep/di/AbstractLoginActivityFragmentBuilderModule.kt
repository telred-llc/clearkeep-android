package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.LoginFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SignUpFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.LoginFragment
import vmodev.clearkeep.fragments.SignUpFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSignUpFragmentViewModel
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
}