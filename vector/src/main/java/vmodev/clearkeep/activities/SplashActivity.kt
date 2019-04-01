package vmodev.clearkeep.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import im.vector.R
import im.vector.databinding.ActivitySplashBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import pl.droidsonroids.gif.GifDrawable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        val drawable = GifDrawable(assets, "splash_animation.gif");
        gif_image_view_splash.background = drawable;
        Observable.timer(2500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t ->
            run {
                val intent = Intent(this, LoginActivity::class.java);
                startActivity(intent);
                finish();
            }
        });
    }
}
