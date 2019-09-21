package vmodev.clearkeep.activities

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityExportKeyBinding
import org.matrix.androidsdk.MXSession
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IExportKeyActivity
import vmodev.clearkeep.dialogfragments.ExportBackupKeyResultDialogFragment
import vmodev.clearkeep.dialogfragments.ExportKeyDialogFragment
import vmodev.clearkeep.factories.viewmodels.interfaces.IExportKeyActivityViewModeFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.ultis.writeToInternal
import vmodev.clearkeep.ultis.writeToInternalCache
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractExportKeyActivityViewModel
import java.util.*
import javax.inject.Inject


class ExportKeyActivity : DataBindingDaggerActivity(), IActivity, ExportKeyDialogFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractExportKeyActivityViewModel>;

    private lateinit var binding: ActivityExportKeyBinding;
    private var exportStatus = false;
    private var backupKeyContent: String = "";
    private lateinit var mxSession: MXSession;
    private lateinit var userId: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_export_key, dataBinding.getDataBindingComponent());
        userId = intent.getStringExtra(USER_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.security);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        mxSession = Matrix.getInstance(applicationContext).defaultSession;
        binding.frameLayoutExportKeysGroup.setOnClickListener {
            //            if (exportStatus)
//                return@setOnClickListener;
//            val fragment = ExportKeyDialogFragment.newInstance();
//            fragment.show(supportFragmentManager, "");
            val intentBackupKey = Intent(this, BackupKeyActivity::class.java);
            intentBackupKey.putExtra(BackupKeyActivity.USER_ID, userId);
            startActivity(intentBackupKey);
        }
        binding.backupKey = viewModelFactory.getViewModel().getExportBackupKeyResult();
        viewModelFactory.getViewModel().getExportBackupKeyResult().observe(this, Observer {
            it?.let {
                if (it.status == Status.LOADING) {
                    exportStatus = true;
                    binding.textViewExportKeys.setText(R.string.exporting);
                } else if (it.status == Status.SUCCESS) {
                    it.data?.let {
                        backupKeyContent = it;
                        val shareOrSaveDialog = ExportBackupKeyResultDialogFragment.newInstance();
                        shareOrSaveDialog.buttonSaveToFile().subscribe() {
                            requestReadWriteExternalStorageForSaveToFile();
                            shareOrSaveDialog.dismiss();
                        }
                        shareOrSaveDialog.buttonCopy().subscribe {
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.text = backupKeyContent;
                            shareOrSaveDialog.dismiss();
                            Toast.makeText(this, R.string.key_copy_to_clipboard, Toast.LENGTH_LONG).show();
                        }
                        shareOrSaveDialog.buttonShare().subscribe {
                            requestReadWriteExternalStorageForShare();
//                            shareOrSaveDialog.dismiss();
                        }
                        shareOrSaveDialog.show(supportFragmentManager, "");
                        binding.textViewExportKeys.setText(R.string.export_keys);
                        exportStatus = false;
                    }
                } else {
                    binding.textViewExportKeys.setText(R.string.export_keys);
                    exportStatus = false;
                }
            }
        });
        binding.lifecycleOwner = this;
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onExportButtonClick(passphrase: String) {
        viewModelFactory.getViewModel().setExportBackupKey(passphrase);
    }

    @AfterPermissionGranted(REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SHARE)
    private fun requestReadWriteExternalStorageForShare() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (EasyPermissions.hasPermissions(this, * params)) {
            shareFile();
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission to write file to cache", REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SHARE, *params);
        }
    }

    @AfterPermissionGranted(REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SAVE_TO_FILE)
    private fun requestReadWriteExternalStorageForSaveToFile() {
        val params = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (EasyPermissions.hasPermissions(this, * params)) {
            openSelectFolderPath();
        } else {
            EasyPermissions.requestPermissions(this, "Application need permission to write file", REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SAVE_TO_FILE, *params);
        }
    }

    private fun shareFile() {
        val currentTime = Calendar.getInstance().time;
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = backupKeyContent.writeToInternalCache("ClearKeep-keys-$currentTime.txt");
        val intentShareFile = Intent(Intent.ACTION_SEND)
        file?.let {
            intentShareFile.setType("application/txt");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + it.path));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "ClearKeep Export Key");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "ClearKeep Export Key");

            startActivityForResult(Intent.createChooser(intentShareFile, "Share Backup Key"), RECEIVED_SEND_FILE);
        }
    }

    private fun openSelectFolderPath() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        startActivityForResult(Intent.createChooser(i, "Choose directory"), CHOOSE_FOLDER_FOR_SAVE_FILE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_FOLDER_FOR_SAVE_FILE) {
            data?.let {
                val uri = it.data;
                val currentTime = Calendar.getInstance().time;
                backupKeyContent.writeToInternal(uri!!.path!!, "ClearKeep-keys-$currentTime.txt")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onCancelButtonClick() {
    }

    companion object {
        const val REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SHARE = 1;
        const val REQUEST_READ_WRITE_EXTERNAL_STORAGE_FOR_SAVE_TO_FILE = 2;
        const val CHOOSE_FOLDER_FOR_SAVE_FILE = 13425;
        const val USER_ID = "USER_ID";
    }
}
