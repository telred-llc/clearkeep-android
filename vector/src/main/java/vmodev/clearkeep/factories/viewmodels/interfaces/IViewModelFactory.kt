package vmodev.clearkeep.factories.viewmodels.interfaces

import android.arch.lifecycle.ViewModel

interface IViewModelFactory<T : ViewModel> {
    fun getViewModel(): T;
}