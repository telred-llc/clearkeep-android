package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.FragmentDeactivateUserAccountBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import vmodev.clearkeep.activities.LoginActivity
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.dialogfragments.EditTextDialogFragment
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDeactivateAccountActivityViewModel
import java.lang.Exception
import javax.inject.Inject

class DeactivateAccountFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var clearKeepDatabase: ClearKeepDatabase;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractDeactivateAccountActivityViewModel>
    @Inject
    lateinit var application: IApplication;

    private lateinit var binding: FragmentDeactivateUserAccountBinding;
    private lateinit var session: MXSession;
    private var deactivateAccountStatus = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deactivate_user_account, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardViewCancel.setOnClickListener { findNavController().navigate(DeactivateAccountFragmentDirections.cancel()) };
        session = Matrix.getInstance(application.getApplication()).defaultSession;
        binding.cardViewDeactivateAccount.setOnClickListener {
            if (deactivateAccountStatus)
                return@setOnClickListener;
            deactivateAccountStatus = true;
            binding.textViewDeactivateAccount.text = resources.getString(R.string.deativating);
            val dialogFragment = EditTextDialogFragment.newInstance(resources.getString(R.string.deactivate_account_title)
                    , resources.getString(R.string.deactivate_account_prompt_password)
                    , resources.getString(R.string.password), InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    , resources.getString(R.string.auth_submit), resources.getString(R.string.cancel));
            dialogFragment.show(childFragmentManager, "");
            dialogFragment.getOnClickOk().subscribe {
                binding.progressBar.visibility = View.VISIBLE;
                CommonActivityUtils.deactivateAccount(this.context, session, it, binding.checkboxAccept.isChecked, object : SimpleApiCallback<Void>() {
                    @SuppressLint("CheckResult")
                    override fun onSuccess(info: Void?) {
                        Observable.create<Int> { emmiter ->
                            clearKeepDatabase.clearAllTables();
                            emmiter.onNext(1);
                            emmiter.onComplete();
                        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                            binding.progressBar.visibility = View.INVISIBLE;
                            val loginActivityIntent = Intent(this@DeactivateAccountFragment.activity, LoginActivity::class.java);
                            startActivity(loginActivityIntent);
                            this@DeactivateAccountFragment.activity?.finish();
                        }
                    }

                    override fun onNetworkError(e: Exception?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountFragment.activity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onNetworkError(e)
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountFragment.activity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onMatrixError(e)
                    }

                    override fun onUnexpectedError(e: Exception?) {
                        binding.textViewDeactivateAccount.text = resources.getString(R.string.deactivate_account_title);
                        deactivateAccountStatus = false;
                        binding.progressBar.visibility = View.INVISIBLE;
                        e?.message?.let {
                            Toast.makeText(this@DeactivateAccountFragment.activity, it, Toast.LENGTH_LONG).show();
                        }
                        super.onUnexpectedError(e)
                    }
                })
            };
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
