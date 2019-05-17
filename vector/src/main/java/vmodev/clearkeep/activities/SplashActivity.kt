package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.activity.VectorLauncherActivity
import im.vector.databinding.ActivitySplashBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.matrix.androidsdk.util.Log
import pl.droidsonroids.gif.GifDrawable
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    private lateinit var binding: ActivitySplashBinding;
    //    private lateinit var userViewModel: AbstractUserViewModel;
    private lateinit var roomViewMOdel: AbstractRoomViewModel;

    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash, dataBindingComponent);
        val drawable = GifDrawable(assets, "splash_animation.gif");
        binding.gifImageViewSplash.background = drawable;

        roomViewMOdel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.rooms = roomViewMOdel.getRoomsData();
        val timer = Observable.timer(2500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
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
                };
//        val loadDatas =
//        Observable.create<Long> { emitter ->

//        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe { t: Long? ->
//
//        };
//        roomViewMOdel.getRoomsData().observe(this, Observer { t ->
//            if (t?.status == Status.SUCCESS) {
//                if (!hasCredentials()) {
//                    val intent = Intent(this, LoginActivity::class.java);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    val intent = Intent(this, im.vector.activity.SplashActivity::class.java);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        })
//        roomViewMOdel.setFilter(arrayOf(1, 2, 65, 66, 129, 130));
//        Observable.zip(timer, loadDatas, BiFunction { t1, t2 ->  }).
        binding.lifecycleOwner = this;
    }

    private fun hasCredentials(): Boolean {
        try {
            val session = Matrix.getInstance(applicationContext).defaultSession;
            return null != session && session.isAlive

        } catch (e: Exception) {
            Log.d("Error Credentials: ", e?.message)
        }

        runOnUiThread {
            try {
                // getDefaultSession could trigger an exception if the login data are corrupted
                CommonActivityUtils.logout(this@SplashActivity)
            } catch (e: Exception) {
                Log.d("Error Credentials: ", e?.message)
            }
        }

        return false
    }
}
