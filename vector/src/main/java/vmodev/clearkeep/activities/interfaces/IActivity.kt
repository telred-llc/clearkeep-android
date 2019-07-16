package vmodev.clearkeep.activities.interfaces

import android.support.v4.app.FragmentActivity

interface IActivity {
    fun getActivity(): FragmentActivity;

    companion object {
        const val BACKUP_KEY = "BACKUP_KEY";
        const val RESTORE_BACKUP_KEY = "RESTORE_BACKUP_KEY";
        const val PUSH_BACKUP_KEY = "PUSH_BACKUP_KEY";
    }
}