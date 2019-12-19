package vmodev.clearkeep.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import javax.inject.Inject

abstract class DataBindingDaggerFragment : Fragment(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var dataBinding: IDataBindingComponent

    @Inject
    lateinit var application: IApplication

    val compositeDisposable = CompositeDisposable()

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

}