package vmodev.clearkeep.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class User : ViewModel(){
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}