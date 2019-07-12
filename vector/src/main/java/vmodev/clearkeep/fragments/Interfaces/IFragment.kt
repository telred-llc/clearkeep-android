package vmodev.clearkeep.fragments.Interfaces

import android.support.v4.app.Fragment

interface IFragment {
    fun getFragment(): Fragment;

    companion object {
        const val PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT = "PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT";
        const val TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT = "TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT";
    }
}