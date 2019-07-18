package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import org.matrix.androidsdk.rest.model.pid.ThreePid
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixLoginService
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

class LoginRepository constructor(private val matrixLoginService: IMatrixLoginService) {
    fun login(username: String, password: String): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixLoginService.login(username, password);
            }
        }.asLiveData();
    }

    fun register(username: String, email: String, password: String): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixLoginService.register(username, email, password);
            }
        }.asLiveData();
    }

    fun setRegistrationFlowsResponse(): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixLoginService.hasRegistrationResponse();
            }
        }.asLiveData();
    }

    fun handleVerifyEmail(): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixLoginService.waitingForVerifyEmail();
            }
        }.asLiveData();
    }

    fun forgetPassword(email: String): LiveData<Resource<ThreePid>> {
        return object : AbstractNetworkNonBoundSourceRx<ThreePid>() {
            override fun createCall(): Observable<ThreePid> {
                return matrixLoginService.forgetPassword(email);
            }
        }.asLiveData();
    }

    fun resetPassword(password: String, threePid: ThreePid): LiveData<Resource<String>> {
        return object : AbstractNetworkNonBoundSourceRx<String>() {
            override fun createCall(): Observable<String> {
                return matrixLoginService.resetPassword(password, threePid);
            }
        }.asLiveData();
    }
}