package vmodev.clearkeep.fragments.Interfaces

import io.reactivex.Observable

interface IRoomShareFileFragment:IFragment {
    fun setQuery(query: String)
    fun onClickItemtRoom(): Observable<String>;

    companion object{
        const val ROOM_SHARE_FILE_FRAGMENT = "ROOM_SHARE_FILE_FRAGMENT";
    }
}