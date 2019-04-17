package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.ViewModel

/**
 * ViewModelProvider need ViewModel type to get a ViewModel instance
 * So, I cannot Interface in here to declare abstract for ViewModel
 * So, I using a Abstract Class to declare abstract for ViewModel
 */
abstract class AbstractUserViewModel : ViewModel() {
}