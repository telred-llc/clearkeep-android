package vmodev.clearkeep.autokeybackups.interfaces

interface IAutoKeyBackup {
    fun startAutoKeyBackup(userId: String, password: String?);
}