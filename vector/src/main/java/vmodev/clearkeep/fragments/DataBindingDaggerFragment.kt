package vmodev.clearkeep.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import javax.inject.Inject

abstract class DataBindingDaggerFragment : Fragment(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var dataBinding: IDataBindingComponent;

    @Inject
    lateinit var application: IApplication

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector;
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}