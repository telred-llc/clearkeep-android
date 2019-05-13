package vmodev.clearkeep.activities

import dagger.android.support.DaggerAppCompatActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent

@Suppress("LeakingThis")
abstract class DataBindingDaggerActivity : DaggerAppCompatActivity() {

    val dataBindingComponent: ActivityDataBindingComponent
        get() = ActivityDataBindingComponent(this)
}
