package vmodev.clearkeep.applications

import android.app.Application
import com.google.errorprone.annotations.ForOverride
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.internal.Beta
import im.vector.VectorApp
import javax.inject.Inject


@Beta
abstract class DaggerVectorApp : VectorApp()
        , HasAndroidInjector {

    @Inject
    @Volatile
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        injectIfNecessary()
    }

    /**
     * Implementations should return an [AndroidInjector] for the concrete [ ]. Typically, that injector is a [dagger.Component].
     */
    @ForOverride
    protected abstract fun applicationInjector(): AndroidInjector<out DaggerApplication>

    /**
     * Lazily injects the [DaggerApplication]'s members. Injection cannot be performed in [ ][Application.onCreate] since [android.content.ContentProvider]s' [ ][android.content.ContentProvider.onCreate] method will be called first and might
     * need injected members on the application. Injection is not performed in the constructor, as
     * that may result in members-injection methods being called before the constructor has completed,
     * allowing for a partially-constructed instance to escape.
     */
    private fun injectIfNecessary() {
//        if (androidInjector == null) {
            synchronized(this) {
//                if (androidInjector == null) {
                    val applicationInjector = applicationInjector() as AndroidInjector<DaggerVectorApp>
                    applicationInjector.inject(this)
                    checkNotNull(androidInjector) { "The AndroidInjector returned from applicationInjector() did not inject the " + "DaggerApplication" }
//                }
//            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any>? {
        // injectIfNecessary should already be called unless we are about to inject a ContentProvider,
        // which can happen before Application.onCreate()
        injectIfNecessary()

        return androidInjector
    }
}