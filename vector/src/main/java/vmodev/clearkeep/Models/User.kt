package vmodev.clearkeep.Models

import android.databinding.BaseObservable
import android.databinding.Bindable

class User : BaseObservable(){
    private val name : String = "";

    @Bindable
    public fun getName() : String{
        return name;
    }
}