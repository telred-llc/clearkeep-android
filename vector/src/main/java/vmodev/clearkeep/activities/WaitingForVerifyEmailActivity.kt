package vmodev.clearkeep.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.BuildConfig
import im.vector.R
import im.vector.databinding.ActivityWaitingForVerifyEmailBinding
import im.vector.repositories.ServerUrlsRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractWaitingForVerifyEmailActivityViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WaitingForVerifyEmailActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractWaitingForVerifyEmailActivityViewModel>;

    private lateinit var binding: ActivityWaitingForVerifyEmailBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waiting_for_verify_email);
        binding.verifyEmail = viewModelFactory.getViewModel().getWaitingForVerifyEmailResult();
        binding.lifecycleOwner = this;
        val disposable = Observable.interval(0, 10, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    viewModelFactory.getViewModel().getWaitingForVerifyEmailResult().observe(this, Observer {
                        it?.let {
                            if (it.status == Status.SUCCESS) {
                                it?.data?.let {
                                    ServerUrlsRepository.saveServerUrls(this, BuildConfig.HOME_SERVER, BuildConfig.IDENTIFY_SERVER);
                                    val intent = Intent(this, SplashActivity::class.java);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            if (it.status == Status.ERROR) {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
        binding.buttonCancel.setOnClickListener {
            disposable.dispose();
            finish();
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
