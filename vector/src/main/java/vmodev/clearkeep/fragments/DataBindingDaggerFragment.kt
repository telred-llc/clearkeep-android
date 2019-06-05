package vmodev.clearkeep.fragments

import dagger.android.support.DaggerFragment
import vmodev.clearkeep.binding.FragmentDataBindingComponent

abstract class DataBindingDaggerFragment : DaggerFragment() {
    val dataBindingComponent: FragmentDataBindingComponent
        get() = FragmentDataBindingComponent(this);
}