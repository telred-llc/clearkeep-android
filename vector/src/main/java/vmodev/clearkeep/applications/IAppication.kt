package vmodev.clearkeep.applications

import android.app.Application

interface IAppication {
    fun getCurrentTheme(): Int;
    fun setCurrentTheme(theme: Int);
    fun getApplication(): Application;
}