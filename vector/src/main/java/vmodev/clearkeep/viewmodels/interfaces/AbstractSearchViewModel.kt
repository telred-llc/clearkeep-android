package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractSearchViewModel : ViewModel() {
    abstract fun setKeywordSearchMessage(keyword: String);
    abstract fun getSearchMessageByTextResult(): LiveData<Resource<List<MessageSearchText>>>;
    abstract fun setKeywordSearchMedia(keyword: String);
    abstract fun getSearchMediaByTextResult() : LiveData<Resource<List<String>>>;
}