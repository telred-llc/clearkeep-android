package im.vector.activity.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class User : ViewModel() {
    lateinit var name : LiveData<String>
    init {

    }
}