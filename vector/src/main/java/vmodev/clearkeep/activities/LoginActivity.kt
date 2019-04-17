package vmodev.clearkeep.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import im.vector.LoginHandler
import im.vector.Matrix
import im.vector.R
import im.vector.RegistrationManager
import im.vector.activity.CommonActivityUtils
import im.vector.activity.SplashActivity
import im.vector.repositories.ServerUrlsRepository
import kotlinx.android.synthetic.main.activity_login.*
import org.matrix.androidsdk.HomeServerConnectionConfig
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.HttpException
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse
import org.matrix.androidsdk.rest.model.pid.ThreePid
import org.matrix.androidsdk.util.JsonUtils
import org.matrix.androidsdk.util.Log
import vmodev.clearkeep.fragments.HandlerVerifyEmailFragment
import vmodev.clearkeep.fragments.LoginFragment
import vmodev.clearkeep.fragments.SignUpFragment
import javax.net.ssl.HttpsURLConnection

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener,
        RegistrationManager.UsernameValidityListener, RegistrationManager.RegistrationListener, HandlerVerifyEmailFragment.OnFragmentInteractionListener {
    private val LOG_TAG: String = LoginActivity::javaClass.name;

    private lateinit var mRegistrationManager: RegistrationManager;
    private lateinit var hsConfig: HomeServerConnectionConfig;
    private lateinit var mLoginHandler: LoginHandler;
    private var handleVerifyEmail: Runnable? = null;
    private var mHandler: Handler? = null;
    private var HANDLING_VERIFY_EMAIL: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        val loginFragment = LoginFragment.newInstance("", "");
        changeFragment(loginFragment);
    }

    override fun onLoginSuccess() {
        progress_bar.visibility = View.INVISIBLE;
        finish();
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE;
    }

    override fun hideLoading() {
        progress_bar.visibility = View.INVISIBLE;
    }

    override fun onPressedSignUp() {
        showLoading();
        initRegisterScreen();
    }

    override fun onPressedHaveAnAccount() {
        val loginFragment = LoginFragment.newInstance("", "");
        changeFragment(loginFragment);
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private fun initRegisterScreen() {
        mLoginHandler = LoginHandler();
        mRegistrationManager = RegistrationManager(null);
        hsConfig = HomeServerConnectionConfig.Builder().withHomeServerUri(Uri.parse("https://study.sinbadflyce.com")).withIdentityServerUri(Uri.parse("https://matrix.org")).build();
        mHandler = Handler(mainLooper);
        checkRegistrationFlows();
    }

    private fun checkRegistrationFlows() {
        // should only check registration flows
//        if (mMode != MODE_ACCOUNT_CREATION) {
//            return
//        }

        if (!mRegistrationManager.hasRegistrationResponse()) {
            try {
//                val hsConfig = getHsConfig()

                // invalid URL
                if (null == hsConfig) {
                    return;
                } else {
//                    enableLoadingScreen(true)

                    mLoginHandler.getSupportedRegistrationFlows(this, hsConfig, object : SimpleApiCallback<Void>() {
                        override fun onSuccess(avoid: Void) {
                            // should never be called
                        }

                        private fun onError(errorMessage: String) {
                            // should not check login flows
                        }

                        override fun onNetworkError(e: Exception) {
                            Log.e(LOG_TAG, "Network Error: " + e.message, e)
                            onError(getString(R.string.login_error_registration_network_error) + " : " + e.localizedMessage)
                        }

                        override fun onUnexpectedError(e: Exception) {
                            if (e is HttpException && e.httpError.httpCode == HttpsURLConnection.HTTP_BAD_METHOD /* 405 */) {
                                // Registration is not allowed
//                                    onRegistrationNotAllowed()
                            } else {
                                onError(getString(R.string.login_error_unable_register) + " : " + e.localizedMessage)
                            }
                        }

                        override fun onMatrixError(e: MatrixError) {

                            Log.d(LOG_TAG, "## checkRegistrationFlows(): onMatrixError - Resp=" + e.localizedMessage)
                            var registrationFlowResponse: RegistrationFlowResponse? = null

                            // when a response is not completed the server returns an error message
                            if (null != e.mStatus) {
                                if (e.mStatus == 401) {
                                    try {
                                        registrationFlowResponse = JsonUtils.toRegistrationFlowResponse(e.mErrorBodyAsString)
                                    } catch (castExcept: Exception) {
                                        Log.e(LOG_TAG, "JsonUtils.toRegistrationFlowResponse " + castExcept.localizedMessage, castExcept)
                                    }

                                } else if (e.mStatus == 403) {
//                                        onRegistrationNotAllowed()
                                }

                                if (null != registrationFlowResponse) {
                                    mRegistrationManager.setSupportedRegistrationFlows(registrationFlowResponse)
                                    hideLoading();
//                                    onCheckRegistrationFlowsSuccess();
//                                    onRegistrationFlow()
                                    val signUpFragment = SignUpFragment.newInstance("", "");
                                    changeFragment(signUpFragment)
                                } else {
//                                    onFailureDuringAuthRequest(e)
                                }

                                // start Login due to a pending email validation
//                                checkIfMailValidationPending()
                            }
                        }
                    })
                }
            } catch (e: Exception) {
//                Toast.makeText(getApplicationContext(), getString(R.string.login_error_invalid_home_server), Toast.LENGTH_SHORT).show()
//                enableLoadingScreen(false)
            }

        } else {
//            setActionButtonsEnabled(true)
        }
    }

    private fun saveServerUrlsIfCustomValuesHasBeenEntered() {
        // Save urls if not using default
        ServerUrlsRepository.saveServerUrls(this, "https://study.sinbadflyce.com", "https://matrix.org");
    }

    private fun showAlertDiaglong(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setNegativeButton("Close", null).show();
    }

    override fun onRegistrationSuccess(warningMessage: String?) {
        if (mHandler != null && handleVerifyEmail != null)
            mHandler!!.removeCallbacks(handleVerifyEmail);
        saveServerUrlsIfCustomValuesHasBeenEntered();
        val intent = Intent(this, SplashActivity::class.java);
        startActivity(intent);
        finish();
    }

    override fun onPressedRegister(username: String, email: String, password: String) {
        showLoading();
        mRegistrationManager.setHsConfig(hsConfig);
        mRegistrationManager.addEmailThreePid(ThreePid(email, ThreePid.MEDIUM_EMAIL));
        mRegistrationManager.setAccountData(username, password);
        if(email.isNullOrEmpty()){
            mRegistrationManager.clearThreePid();
        }
        mRegistrationManager.checkUsernameAvailability(this, this);
    }

    override fun onUsernameAvailabilityChecked(isAvailable: Boolean) {
        hideLoading();
        if (!isAvailable) {
            showAlertDiaglong("Sign up error", "Username or Email is already in use");
        } else {
            mRegistrationManager.attemptRegistration(this, this);
        }
    }

    override fun onRegistrationFailed(message: String?) {
        showAlertDiaglong("Sign up error", message!!);
    }

    override fun onWaitingEmailValidation() {
        if (!HANDLING_VERIFY_EMAIL) {
            HANDLING_VERIFY_EMAIL = true;
            val handlerVerifyEmailFragment = HandlerVerifyEmailFragment.newInstance("", "");
            changeFragment(handlerVerifyEmailFragment);
            image_view_logo.visibility = View.GONE;
            showLoading();
        }

        handleVerifyEmail = object : Runnable {
            override fun run() {
                mRegistrationManager.attemptRegistration(this@LoginActivity, this@LoginActivity)
                mHandler!!.postDelayed(handleVerifyEmail, 10000);
            }
        }
        mHandler!!.postDelayed(handleVerifyEmail, 10000);
    }

    override fun onWaitingCaptcha(publicKey: String?) {
        //This feature is not implemented in version
    }

    override fun onWaitingTerms(localizedFlowDataLoginTerms: MutableList<LocalizedFlowDataLoginTerms>?) {
        //This feature is not implemented in version
    }

    override fun onThreePidRequestFailed(message: String?) {
        showAlertDiaglong("Sign up error", message!!);
    }

    override fun onResourceLimitExceeded(e: MatrixError?) {
        showAlertDiaglong("Sign up error", e!!.message);
    }

    override fun onPressedCancel() {
        image_view_logo.visibility = View.VISIBLE;
        HANDLING_VERIFY_EMAIL = false;
        hideLoading();
        if (mHandler != null && handleVerifyEmail != null)
            mHandler!!.removeCallbacks(handleVerifyEmail);
        val signUpFragment = SignUpFragment.newInstance("", "");
        changeFragment(signUpFragment);
    }
}
