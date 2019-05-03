package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractSearchViewModel : ViewModel() {
    abstract fun setKeywordSearchMessage(keyword: String);
    abstract fun getSearchMessageByTextResult(): LiveData<Resource<List<MessageSearchText>>>;
}