package vmodev.clearkeep.matrixsdk

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import im.vector.Matrix
import im.vector.util.HomeRoomsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixServiceImplmenmt @Inject constructor(private val application: Application) : MatrixService {

    //    @Inject
    private var session: MXSession? = null;
    private var homeRoomsViewModel: HomeRoomsViewModel? = null;
    private val funcs: Array<Function<HomeRoomsViewModel.Result, List<Room>>> = Array(255, init = { Function { t: HomeRoomsViewModel.Result -> t.directChats } })
    private fun setMXSession() {
        if (session != null)
            return;
        session = Matrix.getInstance(application).defaultSession;
        homeRoomsViewModel = HomeRoomsViewModel(session!!);
        funcs[1] = Function { t: HomeRoomsViewModel.Result -> t.directChats };
        funcs[2] = Function { t: HomeRoomsViewModel.Result -> t.otherRooms };
        funcs[129] = Function { t: HomeRoomsViewModel.Result -> t.getDirectChatsWithFavorites() };
        funcs[130] = Function { t: HomeRoomsViewModel.Result -> t.getOtherRoomsWithFavorites() };
    }

    @SuppressLint("CheckResult")
    override fun getListDirectMessageConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.directChats);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            kotlin.run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListRoomConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.otherRooms);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            kotlin.run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListFavouriteConversation(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.favourites);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? ->
            run {
                liveData.value = t;
            }
        };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getListContact(): LiveData<List<Room>> {
        setMXSession();
        val liveData = MutableLiveData<List<Room>>();
        Observable.create<List<Room>> { emitter ->
            kotlin.run {
                homeRoomsViewModel!!.update();
                val result = homeRoomsViewModel!!.result;
                if (result != null) {
                    emitter.onNext(result.getDirectChatsWithFavorites());
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException())
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { t: List<Room>? -> kotlin.run { liveData.value = t; } };
        return liveData;
    }

    @SuppressLint("CheckResult")
    override fun getUser(): Observable<User> {
        setMXSession();
        return Observable.create<User> { emitter ->
            kotlin.run {
                val myUser = session!!.myUser;

                if (myUser != null) {
                    val avatarUrl = session!!.contentManager.getDownloadableUrl(myUser.avatarUrl);
                    val user = User(name = myUser.displayname, id = myUser.user_id, avatarUrl = avatarUrl);
                    emitter.onNext(user);
                    emitter.onComplete();
                } else {
                    emitter.onError(NullPointerException());
                    emitter.onComplete();
                }
            }
        }
    }

    override fun getListRoom(filter: Int): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>> {
        setMXSession();
        return Observable.create<List<vmodev.clearkeep.viewmodelobjects.Room>> { emitter ->
            kotlin.run {
                val listRoom = ArrayList<vmodev.clearkeep.viewmodelobjects.Room>();
                if (homeRoomsViewModel != null && homeRoomsViewModel!!.result != null) {
                    val rooms = funcs[filter].apply(homeRoomsViewModel!!.result);
                    rooms.forEach { t: Room? ->
                        listRoom.add(vmodev.clearkeep.viewmodelobjects.Room(id = t!!.roomId
                                , name = t!!.getRoomDisplayName(application)
                                , type = filter, avatarUrl = t!!.avatarUrl!!, updatedDate = 0))
                    }
                }
            }
        }
    }
}