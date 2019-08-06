package vmodev.clearkeep.fragments.Interfaces

import android.support.v4.app.Fragment

interface IFragment {
    fun getFragment(): Fragment;

    companion object {
        const val LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
        const val SIGNUP_FRAGMENT = "SIGNUP_FRAGMENT";
        const val PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT = "PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT";
        const val TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT = "TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT";
        const val HANDLER_VERIFY_EMAIL_FRAGMENT = "HANDLER_VERIFY_EMAIL_FRAGMENT";
        const val FORGOT_PASSWORD_FRAGMENT = "FORGOT_PASSWORD_FRAGMENT";
        const val FORGOT_PASSWORD_VERIFY_EMAIL_FRAGMENT = "FORGOT_PASSWORD_VERIFY_EMAIL_FRAGMENT";
        const val SEARCH_MESSAGE_FRAGMENT = "SEARCH_MESSAGE_FRAGMENT";
        const val SEARCH_ROOM_FRAGMENT = "SEARCH_ROOM_FRAGMENT";
    }
}