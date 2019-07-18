package vmodev.clearkeep.activities.interfaces

import android.support.v4.app.FragmentActivity

interface IActivity {
    fun getActivity(): FragmentActivity;

    companion object {
        const val LOGIN_ACTIVITY = "LOGIN_ACTIVITY";
        const val BACKUP_KEY = "BACKUP_KEY";
        const val RESTORE_BACKUP_KEY = "RESTORE_BACKUP_KEY";
        const val PUSH_BACKUP_KEY = "PUSH_BACKUP_KEY";
        const val WAITING_FOR_VERIFY_EMAIL_ACTIVITY = "WAITING_FOR_VERIFY_EMAIL_ACTIVITY";
        const val PROFILE_ACTIVITY = "PROFILE_ACTIVITY";
    }
}