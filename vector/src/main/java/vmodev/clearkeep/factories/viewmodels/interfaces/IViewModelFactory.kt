package vmodev.clearkeep.factories.viewmodels.interfaces

import android.arch.lifecycle.ViewModel

interface IViewModelFactory<T : ViewModel> {
    fun getViewModel(): T;

    companion object {
        const val KEYS_BACK_UP_MANAGE_FRAGMENT = "KEYS_BACK_UP_MANAGE_FRAGMENT";
        const val RESTORE_BACK_UP_KEY_ACTIVITY = "RESTORE_BACK_UP_KEY_ACTIVITY";
        const val PASSPHRASE_RESTORE_BACKUP_KEY_FRAGMENT = "PASSPHRASE_RESTORE_BACKUP_KEY_FRAGMENT";
        const val TEXT_FILE_RESTORE_BACKUP_KEY_FRAGMENT = "TEXT_FILE_RESTORE_BACKUP_KEY_FRAGMENT";
    }
}