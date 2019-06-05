package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.repositories.SearchRepository
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(searchRepository: SearchRepository) : AbstractSearchViewModel() {
    private val _searchNMessageByText = MutableLiveData<String>();
    private val _searchMediaByText = MutableLiveData<String>();
    private val searchMessageByTextResult = Transformations.switchMap(_searchNMessageByText) { input ->
        searchRepository.findMessageByText(input);
    }
    private val searchMediaByTextResult = Transformations.switchMap(_searchMediaByText) { input ->
        searchRepository.findMediaFiles(input);
    }

    override fun setKeywordSearchMessage(keyword: String) {
        if (_searchNMessageByText.value != keyword)
            _searchNMessageByText.value = keyword;
    }

    override fun getSearchMessageByTextResult(): LiveData<Resource<List<MessageSearchText>>> {
        return searchMessageByTextResult;
    }

    override fun setKeywordSearchMedia(keyword: String) {
        if (_searchMediaByText.value != keyword)
            _searchMediaByText.value = keyword;
    }

    override fun getSearchMediaByTextResult(): LiveData<Resource<List<String>>> {
        return searchMediaByTextResult;
    }
}