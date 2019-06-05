package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import com.google.errorprone.annotations.ForOverride
import dagger.android.*
import dagger.internal.Beta
import im.vector.VectorApp
import javax.inject.Inject

@Beta
abstract class DaggerVectorApp : VectorApp()
        , HasActivityInjector
        , HasFragmentInjector
        , HasServiceInjector
        , HasBroadcastReceiverInjector
        , HasContentProviderInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>;
    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>;
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>;
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>;
    @Inject
    lateinit var contentProviderInjector: DispatchingAndroidInjector<ContentProvider>;
    @Volatile
    private var needToInject = true

    override fun onCreate() {
        super.onCreate()
        injectIfNecessary()
    }

    /**
     * Implementations should return an [AndroidInjector] for the concrete [ ]. Typically, that injector is a [dagger.Component].
     */
    @ForOverride
    protected abstract fun applicationInjector(): AndroidInjector<out DaggerVectorApp>

    /**
     * Lazily injects the [DaggerApplication]'s members. Injection cannot be performed in [ ][Application.onCreate] since [android.content.ContentProvider]s' [ ][android.content.ContentProvider.onCreate] method will be called first and might
     * need injected members on the application. Injection is not performed in the constructor, as
     * that may result in members-injection methods being called before the constructor has completed,
     * allowing for a partially-constructed instance to escape.
     */
    private fun injectIfNecessary() {
        if (needToInject) {
            synchronized(this) {
                if (needToInject) {
                    val applicationInjector = applicationInjector() as AndroidInjector<DaggerVectorApp>
                    applicationInjector.inject(this)
                    if (needToInject) {
                        throw IllegalStateException(
                                "The AndroidInjector returned from applicationInjector() did not inject the " + "DaggerApplication")
                    }
                }
            }
        }
    }

    @Inject
    internal fun setInjected() {
        needToInject = false
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityInjector
    }

    override fun fragmentInjector(): DispatchingAndroidInjector<Fragment>? {
        return fragmentInjector
    }

    override fun broadcastReceiverInjector(): DispatchingAndroidInjector<BroadcastReceiver>? {
        return broadcastReceiverInjector
    }

    override fun serviceInjector(): DispatchingAndroidInjector<Service>? {
        return serviceInjector
    }

    // injectIfNecessary is called here but not on the other *Injector() methods because it is the
    // only one that should be called (in AndroidInjection.inject(ContentProvider)) before
    // Application.onCreate()
    override fun contentProviderInjector(): AndroidInjector<ContentProvider>? {
        injectIfNecessary()
        return contentProviderInjector
    }
}