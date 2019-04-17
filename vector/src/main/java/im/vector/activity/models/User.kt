package im.vector.activity.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class User : ViewModel() {
    lateinit var name : LiveData<String>
    init {

    }
}