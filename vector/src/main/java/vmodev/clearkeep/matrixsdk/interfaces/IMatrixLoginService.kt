package vmodev.clearkeep.matrixsdk.interfaces

import io.reactivex.Observable
import org.matrix.androidsdk.rest.model.pid.ThreePid

interface IMatrixLoginService {
    fun login(username: String, password: String): Observable<String>;
    fun register(username: String, email: String, password: String): Observable<String>;
    fun hasRegistrationResponse(): Observable<String>;
    fun waitingForVerifyEmail(): Observable<String>;
    fun forgetPassword(email: String): Observable<ThreePid>;
    fun resetPassword(password: String, threePid: ThreePid): Observable<String>;
}