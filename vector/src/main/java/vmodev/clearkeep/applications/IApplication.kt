package vmodev.clearkeep.applications

import android.app.Application

interface IApplication {
    fun getCurrentTheme(): Int;
    fun setCurrentTheme(theme: Int);
    fun getApplication(): Application;
    fun startAutoKeyBackup(password: String?);
    fun setEventHandler();
    fun removeEventHandler();
}