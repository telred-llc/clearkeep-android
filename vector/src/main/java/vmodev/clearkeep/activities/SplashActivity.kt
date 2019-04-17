package vmodev.clearkeep.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.activity.VectorLauncherActivity
import im.vector.databinding.ActivitySplashBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.matrix.androidsdk.util.Log
import pl.droidsonroids.gif.GifDrawable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private val LOG_TAG: String = SplashActivity::javaClass.name;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        val drawable = GifDrawable(assets, "splash_animation.gif");
        gif_image_view_splash.background = drawable;
        Observable.timer(2500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t ->
            run {
                if (!hasCredentials()) {
                    val intent = Intent(this, LoginActivity::class.java);
                    startActivity(intent);
                    finish();
                } else {
                    val intent = Intent(this, im.vector.activity.SplashActivity::class.java);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private fun hasCredentials(): Boolean {
        try {
            val session = Matrix.getInstance(this)!!.defaultSession
            return null != session && session.isAlive

        } catch (e: Exception) {
            Log.e(LOG_TAG, "## Exception: " + e.message, e)
        }

        Log.e(LOG_TAG, "## hasCredentials() : invalid credentials")

        runOnUiThread {
            try {
                // getDefaultSession could trigger an exception if the login data are corrupted
                CommonActivityUtils.logout(this@SplashActivity)
            } catch (e: Exception) {
                Log.w(LOG_TAG, "## Exception: " + e.message, e)
            }
        }

        return false
    }
}
