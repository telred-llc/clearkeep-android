package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityPrivacyPolicyBinding
import vmodev.clearkeep.activities.interfaces.IPrivacyPolicyActivity

class PrivacyPolicyActivity : DataBindingDaggerActivity(), IPrivacyPolicyActivity {

    private lateinit var binding: ActivityPrivacyPolicyBinding;
    private lateinit var title: String;
    private lateinit var url: String;
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy, dataBindingComponent);
        title = intent.getStringExtra(TITLE);
        url = intent.getStringExtra(URL);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.title = title;
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.webViewPrivacyPolicy.settings.javaScriptEnabled = true;
        binding.webViewPrivacyPolicy.loadUrl(url);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val TITLE = "TITLE";
        const val URL = "URL";
    }
}
