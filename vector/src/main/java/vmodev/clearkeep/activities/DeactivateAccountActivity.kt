package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.InputType
import android.view.View
import android.widget.Toast
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ActivityDeactivateUserAccountBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import vmodev.clearkeep.activities.interfaces.IDeactivateAccountActivity
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.dialogfragments.EditTextDialogFragment
import java.lang.Exception
import javax.inject.Inject

class DeactivateAccountActivity : DataBindingDaggerActivity(), IDeactivateAccountActivity {

    @Inject
    lateinit var clearKeepDatabase: ClearKeepDatabase;

    private lateinit var binding: ActivityDeactivateUserAccountBinding;
    private lateinit var session: MXSession;
    private var deactivateAccountStatus = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_deactivate_user_account, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.deactivate_account_title);
        binding.textViewCancel.setOnClickListener { finish() };
        session = Matrix.getInstance(applicationContext).defaultSession;
        binding.cardViewDeactivateAccount.setOnClickListener {
            if (deactivateAccountStatus)
                return@setOnClickListener;
            deactivateAccountStatus = true;
            binding.textViewDeactivateAccount.text = resources.getString(R.string.deativating);
            val dialogFragment = EditTextDialogFragment.newInstance(resources.getString(R.string.deactivate_account_title)
                    , resources.getString(R.string.deactivate_account_prompt_password)
                    , resources.getString(R.string.password), InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    , resources.getString(R.string.auth_submit), resources.getString(R.string.cancel));
            dialogFragment.show(supportFragmentManager, "");
            dialogFragment.getOnClickOk().subscribe {
                binding.progressBar.visibility = View.VISIBLE;
                CommonActivityUtils.deactivateAccount(this, session, it, binding.checkboxAccept.isChecked, object : SimpleApiCallback<Void>() {
                    @SuppressLint("CheckResult")
                    override fun onSuccess(info: Void?) {
                        Observable.create<Int> { emmiter ->
                            clearKeepDatabase.clearAllTables();
                            emmiter.onNext(1);
                            emmiter.onComplete();
                        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                            binding.progressBar.visibility = View.INVISIBLE;
                            val loginActivityIntent = Intent(this@DeactivateAccountActivity, LoginActivity::class.java);
                            startActivity(loginActivityIntent);
                            finish();
                        }
                    }

                    override fun onNetworkError(e: Exception?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountActivity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onNetworkError(e)
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountActivity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onMatrixError(e)
                    }

                    override fun onUnexpectedError(e: Exception?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountActivity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onUnexpectedError(e)
                    }
                })
            };
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }
}
