package vmodev.clearkeep.fragments.Interfaces

import io.reactivex.Observable

interface ISearchRoomFragment : IFragment {
    fun setQuery(query: String)
    fun onClickItemtRoom(): Observable<String>;
}