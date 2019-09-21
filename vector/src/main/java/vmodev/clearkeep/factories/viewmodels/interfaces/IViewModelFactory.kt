package vmodev.clearkeep.factories.viewmodels.interfaces

import androidx.lifecycle.ViewModel


interface IViewModelFactory<T : ViewModel> {
    fun getViewModel(): T;
}