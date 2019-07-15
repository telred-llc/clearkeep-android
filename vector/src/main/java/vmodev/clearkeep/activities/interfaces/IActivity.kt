package vmodev.clearkeep.activities.interfaces

import android.support.v4.app.FragmentActivity

interface IActivity {
    fun getActivity(): FragmentActivity;

    companion object {
        const val LOGIN_ACTIVITY = "LOGIN_ACTIVITY";
    }
}