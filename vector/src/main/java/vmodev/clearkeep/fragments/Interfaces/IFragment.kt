package vmodev.clearkeep.fragments.Interfaces

import android.support.v4.app.Fragment

interface IFragment {
    fun getFragment(): Fragment;

    companion object {
        const val LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
        const val SIGNUP_FRAGMENT = "SIGNUP_FRAGMENT";
    }
}