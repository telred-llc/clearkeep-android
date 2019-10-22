package vmodev.clearkeep.fragments.Interfaces

import androidx.fragment.app.Fragment

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
        const val SEARCH_PEOPLE_FRAGMENT = "SEARCH_PEOPLE_FRAGMENT";
        const val SEARCH_FILES_FRAGMENT = "SEARCH_FILES_FRAGMENT";
        const val HOME_SCREEN_FRAGMENT = "HOME_SCREEN_FRAGMENT";
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT";
        const val CONTACTS_FRAGMENT = "CONTACTS_FRAGMENT";
        const val LIST_ROOM_FRAGMENT = "LIST_ROOM_FRAGMENT";
        const val BACKUP_KEY_MANAGE_FRAGMENT = "BACKUP_KEY_MANAGE_FRAGMENT";
        const val FIND_AND_CREATE_NEW_CONVERSATION_FRAGMENT = "FIND_AND_CREATE_NEW_CONVERSATION_FRAGMENT";
        const val CREATE_NEW_ROOM_FRAGMENT = "CREATE_NEW_ROOM_FRAGMENT";
        const val CREATE_NEW_CALL_FRAGMENT = "CREATE_NEW_CALL_FRAGMENT";
        const val INVITE_USERS_TO_ROOM_FRAGMENT = "INVITE_USERS_TO_ROOM_FRAGMENT"
        const val PROFILE_SETTINGS_FRAGMENT = "PROFILE_SETTINGS_FRAGMENT";
        const val NOTIFICATION_SETTINGS_FRAGMENT = "NOTIFICATION_SETTINGS_FRAGMENT";
        const val CALL_SETTINGS_FRAGMENT = "CALL_SETTINGS_FRAGMENT";
        const val REPORT_FRAGMENT = "REPORT_FRAGMENT";
        const val PRIVACY_POLICY_FRAGMENT = "PRIVACY_POLICY_FRAGMENT";
        const val DEACTIVATE_ACCOUNT_FRAGMENT = "DEACTIVATE_ACCOUNT_FRAGMENT";
        const val ROOM_SETTINGS_FRAGMENT = "ROOM_SETTINGS_FRAGMENT";
        const val OTHER_ROOM_SETTINGS_FRAGMENT = "OTHER_ROOM_SETTINGS_FRAGMENT";
        const val ROOM_MEMBER_LIST_FRAGMENT = "ROOM_MEMBER_LIST_FRAGMENT";
        const val OTHER_ROOM_SETTINGS_ADVANCE_FRAGMENT = "OTHER_ROOM_SETTINGS_ADVANCE_FRAGMENT";
        const val ROLES_PERMISSION_FRAGMENT = "ROLES_PERMISSION_FRAGMENT";
        const val SECURITY_FRAGMENT = "SECURITY_FRAGMENT";
        const val ROOM_SHARE_FILE_FRAGMENT = "ROOM_SHARE_FILE_FRAGMENT";
        const val DIRECT_MESSAGE_SHARE_FILE_FRAGMENT ="DIRECT_MESSAGE_SHARE_FILE_FRAGMENT";
        const val OUTGOING_CALL_FRAGMENT = "OUTGOING_CALL_FRAGMENT";
        const val INCOMING_CALL_FRAGMENT = "INCOMING_CALL_FRAGMENT";
        const val IN_PROGRESS_CALL_FRAGMENT = "IN_PROGRESS_CALL_FRAGMENT";
    }
}