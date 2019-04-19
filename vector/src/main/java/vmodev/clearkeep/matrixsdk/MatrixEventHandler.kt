package vmodev.clearkeep.matrixsdk

import android.app.Application
import android.util.Log
import im.vector.Matrix
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.MyUser
import org.matrix.androidsdk.listeners.MXEventListener
import vmodev.clearkeep.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatrixEventHandler @Inject constructor(private val application: Application,
                                             private val userRepository: UserRepository)
    : MXEventListener(), IMatrixEventHandler {
    private var mxSession: MXSession? = null;
    override fun onAccountDataUpdated() {
        super.onAccountDataUpdated()
        val user = mxSession!!.myUser;
        Log.d("UserId:", user.user_id);
        userRepository.updateUser(user.user_id, user.displayname, user.avatarUrl);
    }

    override fun onAccountInfoUpdate(myUser: MyUser?) {
        super.onAccountInfoUpdate(myUser)
        userRepository.updateUser(myUser!!.user_id, myUser.displayname, myUser.avatarUrl);
    }

    override fun getMXEventListener(mxSession: MXSession): MXEventListener {
        this.mxSession = mxSession;
        return this;
    }
}