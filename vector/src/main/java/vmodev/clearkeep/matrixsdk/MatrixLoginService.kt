package vmodev.clearkeep.matrixsdk

import android.annotation.SuppressLint
import android.net.Uri
import im.vector.BuildConfig
import im.vector.LoginHandler
import im.vector.RegistrationManager
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.matrix.androidsdk.HomeServerConnectionConfig
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.rest.client.ProfileRestClient
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse
import org.matrix.androidsdk.rest.model.login.ThreePidCredentials
import org.matrix.androidsdk.rest.model.pid.ThreePid
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixLoginService

class MatrixLoginService constructor(private val application: IApplication) : IMatrixLoginService {

    private var registrationManager: RegistrationManager = RegistrationManager(null);
    private var loginHandler: LoginHandler = LoginHandler();
    private var homeServerConnectionConfig: HomeServerConnectionConfig = HomeServerConnectionConfig.Builder().withHomeServerUri(Uri.parse(BuildConfig.HOME_SERVER)).withIdentityServerUri(Uri.parse(BuildConfig.IDENTIFY_SERVER)).build();

    override fun login(username: String, password: String): Observable<String> {
        return Observable.create { emitter ->
            loginHandler.login(application.getApplication(), homeServerConnectionConfig, username, null, null, password, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext(username);
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun hasRegistrationResponse(): Observable<String> {
        registrationManager = RegistrationManager(null);
        loginHandler = LoginHandler();
        homeServerConnectionConfig = HomeServerConnectionConfig.Builder().withHomeServerUri(Uri.parse(BuildConfig.HOME_SERVER)).withIdentityServerUri(Uri.parse(BuildConfig.IDENTIFY_SERVER)).build();
        return Observable.create { emitter ->
            if (!registrationManager.hasRegistrationResponse()) {
                loginHandler.getSupportedRegistrationFlows(application.getApplication(), homeServerConnectionConfig, object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        // Do something
                    }

                    override fun onNetworkError(e: Exception?) {
                        emitter.onError(Throwable(e?.message))
                        emitter.onComplete();
                    }

                    override fun onUnexpectedError(e: Exception?) {
                        emitter.onError(Throwable(e?.message))
                        emitter.onComplete();
                    }

                    override fun onMatrixError(p0: MatrixError?) {
                        var registrationFlowResponse: RegistrationFlowResponse? = null
                        p0?.let {
                            if (it.mStatus == 401) {
                                try {
                                    registrationFlowResponse = JsonUtils.toRegistrationFlowResponse(it.mErrorBodyAsString)
                                } catch (castExcept: Exception) {
                                    emitter.onError(castExcept);
                                }

                            } else if (it.mStatus == 403) {
                                emitter.onError(Throwable("Registration is not allowed"));
                                emitter.onComplete();
                            }

                            if (null != registrationFlowResponse) {
                                registrationManager.setSupportedRegistrationFlows(registrationFlowResponse)
                                emitter.onNext("Done");
                                emitter.onComplete();
                            }
                        } ?: let {
                            emitter.onError(Throwable("Registration Flow Response is null"));
                            emitter.onComplete();
                        }
                    }
                })
            } else {
                emitter.onNext("Has Registration Flows");
                emitter.onComplete();
            }
        }
    }

    override fun register(username: String, email: String, password: String): Observable<String> {
        return Observable.create { emitter ->
            registrationManager.clearThreePid();
            registrationManager.setHsConfig(homeServerConnectionConfig);
            registrationManager.addEmailThreePid(ThreePid(email, ThreePid.MEDIUM_EMAIL));
            registrationManager.setAccountData(username, password);
            if (email.isNullOrEmpty()) {
                registrationManager.clearThreePid();
            }
            registrationManager.checkUsernameAvailability(application.getApplication()) {
                if (!it) {
                    emitter.onError(Throwable("Username or Email is already in use"));
                    emitter.onComplete();
                } else {
                    registrationManager.attemptRegistration(application.getApplication(), object : RegistrationManager.RegistrationListener {
                        override fun onRegistrationSuccess(warningMessage: String?) {
                            warningMessage?.let {
                                emitter.onNext(it);
                                emitter.onComplete();
                            } ?: run {
                                emitter.onNext("");
                                emitter.onComplete()
                            }
                        }

                        override fun onRegistrationFailed(message: String?) {
                            emitter.onError(Throwable(message));
                            emitter.onComplete();
                        }

                        @SuppressLint("CheckResult")
                        override fun onWaitingEmailValidation() {
                            emitter.onNext("onWaitingEmailValidation");
                            emitter.onComplete();
                        }

                        override fun onWaitingCaptcha(publicKey: String?) {
                            emitter.onError(Throwable("Register with Captcha is not supported"))
                            emitter.onComplete();
                        }

                        override fun onWaitingTerms(localizedFlowDataLoginTerms: MutableList<LocalizedFlowDataLoginTerms>?) {
                            emitter.onError(Throwable(localizedFlowDataLoginTerms.toString()))
                            emitter.onComplete();
                        }

                        override fun onThreePidRequestFailed(message: String?) {
                            emitter.onError(Throwable(message));
                            emitter.onComplete();
                        }

                        override fun onResourceLimitExceeded(e: MatrixError?) {
                            emitter.onError(Throwable(e?.message));
                            emitter.onComplete();
                        }

                        override fun onIdentityServerMissing() {
                            emitter.onError(Throwable("Identity Server Missing"));
                            emitter.onComplete();
                        }
                    })
                }
            }
        }
    }

    override fun waitingForVerifyEmail(): Observable<String> {
        return Observable.create { emitter ->
            registrationManager.attemptRegistration(application.getApplication(), object : RegistrationManager.RegistrationListener {
                override fun onRegistrationSuccess(warningMessage: String?) {
                    warningMessage?.let {
                        emitter.onNext(it);
                        emitter.onComplete();
                    } ?: run {
                        emitter.onNext("");
                        emitter.onComplete()
                    }
                }

                override fun onRegistrationFailed(message: String?) {
                    emitter.onError(Throwable(message));
                    emitter.onComplete();
                }

                @SuppressLint("CheckResult")
                override fun onWaitingEmailValidation() {
                    emitter.onError(Throwable("onWaitingEmailValidation"))
                    emitter.onComplete();
                }

                override fun onWaitingCaptcha(publicKey: String?) {
                    emitter.onError(Throwable("Register with Captcha is not supported"))
                    emitter.onComplete();
                }

                override fun onWaitingTerms(localizedFlowDataLoginTerms: MutableList<LocalizedFlowDataLoginTerms>?) {
                    emitter.onError(Throwable(localizedFlowDataLoginTerms.toString()))
                    emitter.onComplete();
                }

                override fun onThreePidRequestFailed(message: String?) {
                    emitter.onError(Throwable(message));
                    emitter.onComplete();
                }

                override fun onResourceLimitExceeded(e: MatrixError?) {
                    emitter.onError(Throwable(e?.message));
                    emitter.onComplete();
                }

                override fun onIdentityServerMissing() {
                    emitter.onError(Throwable("Identity Server Missing"));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun forgetPassword(email: String): Observable<ThreePid> {
        return Observable.create { emitter ->
            val profileRestClient = ProfileRestClient(homeServerConnectionConfig);
            profileRestClient.forgetPassword(Uri.parse(BuildConfig.IDENTIFY_SERVER), email, object : ApiCallback<ThreePid> {
                override fun onSuccess(p0: ThreePid?) {
                    p0?.let {
                        emitter.onNext(it);
                        emitter.onComplete();
                    } ?: run {
                        emitter.onError(Throwable("ThreePid is null"));
                        emitter.onComplete();
                    }
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }

    override fun resetPassword(password: String, threePid: ThreePid): Observable<String> {
        return Observable.create { emitter ->
            val homeServer: HomeServerConnectionConfig = HomeServerConnectionConfig.Builder().withHomeServerUri(Uri.parse(BuildConfig.HOME_SERVER)).withIdentityServerUri(Uri.parse(BuildConfig.IDENTIFY_SERVER)).build();
            val profileRestClient = ProfileRestClient(homeServer);
            val threePidCredentials = ThreePidCredentials();
            threePidCredentials.clientSecret = threePid.clientSecret;
            threePidCredentials.sid = threePid.sid;
            threePidCredentials.idServer = homeServer.identityServerUri!!.host;
            profileRestClient.resetPassword(password, threePidCredentials, object : ApiCallback<Void> {
                override fun onSuccess(p0: Void?) {
                    emitter.onNext("Success");
                    emitter.onComplete();
                }

                override fun onUnexpectedError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onMatrixError(p0: MatrixError?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }

                override fun onNetworkError(p0: java.lang.Exception?) {
                    emitter.onError(Throwable(p0?.message));
                    emitter.onComplete();
                }
            })
        }
    }
}
