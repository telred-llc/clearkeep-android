package vmodev.clearkeep.autokeybackups.interfaces

import vmodev.clearkeep.applications.IApplication

interface IAutoKeyBackup {
    fun startAutoKeyBackup(userId: String, password: String?, callback: IApplication.IAction)
}