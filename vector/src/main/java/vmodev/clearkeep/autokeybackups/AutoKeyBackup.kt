package vmodev.clearkeep.autokeybackups

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.CommonActivityUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import javax.inject.Inject


class AutoKeyBackup @Inject constructor() : IAutoKeyBackup {
    @Inject
    lateinit var application: ClearKeepApplication;
    @Inject
    lateinit var crypto: ICrypto;
    @Inject
    lateinit var matrixService: MatrixService;
    @Inject
    lateinit var roomDao: AbstractRoomDao;
    @Inject
    lateinit var userDao: AbstractUserDao;
    @Inject
    lateinit var messageDao: AbstractUserDao;
    @Inject
    lateinit var roomUserJoinDao: AbstractRoomUserJoinDao;
    @Inject
    lateinit var deviceSettingsDao: AbstractDeviceSettingsDao;
    @Inject
    lateinit var keyBackupDao: AbstractKeyBackupDao;

    @SuppressLint("CheckResult")
    override fun startAutoKeyBackup(userId: String, password: String?) {
        val passwordForGenerateKey = userId + "COLIAKIP";
        matrixService.getPassphrase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
            Toast.makeText(application, R.string.get_passphrase_successfully, Toast.LENGTH_SHORT).show();
            val decryptedData = crypto.decrypt(passwordForGenerateKey, passphrase.data.passphrase);
            matrixService.getKeyBackUpData(passphrase.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                when (it.state) {
                    4, 6 -> {
                        Log.d("AutoBackup", "Start4");
                        matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                }, {
                                    if (password.isNullOrEmpty()) {
                                        logout();
                                    } else {
                                        handleRestoreDeleteAndExportNewKey(userId, decryptedData);
                                    }
                                });
                    }
                    else -> {
                        matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    if (it == 4 || it == 6) {
                                        Log.d("AutoBackup", "Start");
                                        matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                                }, {
                                                    if (password.isNullOrEmpty()) {
                                                        logout();
                                                    } else {
                                                        handleRestoreDeleteAndExportNewKey(userId, decryptedData);
                                                    }
                                                });
                                    } else if (it == 3) {
                                        if (password.isNullOrEmpty()) {
                                            logout();
                                            return@subscribe;
                                        }

                                        exportNewKey(passphrase, password);
                                    }
                                }, {
                                    Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }, {
                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
            })

        }, {
            matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it == 4 || it == 6 || it == 3) {
                            if (password.isNullOrEmpty()) {
                                logout();
                                return@subscribe;
                            }

                            createNewKey(password, userId);
                        }
                    }
        })
    }

    @SuppressLint("CheckResult")
    private fun createNewKey(password: String, userId: String) {
        val passwordForGenerateKey = userId + "COLIAKIP";
        val encryptedPassphrase = crypto.encrypt(passwordForGenerateKey, password + "COLIAKIP")
        matrixService.createPassphrase(encryptedPassphrase).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
            Toast.makeText(application, R.string.create_new_passphrase_successfully, Toast.LENGTH_SHORT).show();
            exportNewKey(passphrase, password);
        }, {
            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("CheckResult")
    private fun exportNewKey(passphrase: PassphraseResponse, password: String?) {
        val passwordForGenerateKey = passphrase.data.id + "COLIAKIP";
        val decryptedData = crypto.decrypt(passwordForGenerateKey, passphrase.data.passphrase);
        matrixService.getKeyBackUpData(passphrase.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            when (it.state) {
                6, 7, 4 -> {
                    matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                            }, {
                                if (password.isNullOrEmpty()) {
                                    logout();
                                } else {
                                    handleRestoreDeleteAndExportNewKey(passphrase.data.id, decryptedData);
                                }
                            });
                }
                3 -> {
                    matrixService.exportNewBackupKey(decryptedData).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                Toast.makeText(application, R.string.export_key_successfully, Toast.LENGTH_SHORT).show();
                            }, {
                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
                0, 1 -> {
                    matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                autoKeyBackupWhenCreatePassphrase(passphrase.data.id, password, decryptedData, passphrase);
                            }, {
                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }, {
            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("CheckResult")
    private fun handleRestoreDeleteAndExportNewKey(userId: String, decryptedData: String) {
        showEnterPassphraseAlertDialog().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    matrixService.restoreBackupFromPassphrase(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            if (it == 6) {
                                                matrixService.deleteKeyBackup(userId)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe({
                                                            matrixService.exportNewBackupKey(decryptedData).subscribeOn(Schedulers.io())
                                                                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                                                        Toast.makeText(application, R.string.export_key_successfully, Toast.LENGTH_SHORT).show();
                                                                    }, {
                                                                        Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }, {
                                                            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        }, {});
                            }, {
                                it.message?.let {
                                    if (it.contains("Invalid")) {
                                        showAlertForRetypeOrNewKey(userId, decryptedData);
                                    } else {
                                        Toast.makeText(application, it, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }, {
                    Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("CheckResult")
    private fun logout() {
        AlertDialog.Builder(application).setMessage(application.resources.getString(R.string.auto_backup_key_logout_content))
                .setNegativeButton(application.getString(R.string.logout)) { dialogInterface, i ->
                    Completable.fromAction {
                        roomDao.delete();
                        userDao.delete();
                        roomUserJoinDao.delete();
                        messageDao.delete();
                        deviceSettingsDao.delete();
                        keyBackupDao.delete();
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                CommonActivityUtils.logout(null, true);
                            }, {
                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
                .setPositiveButton(application.getString(R.string.close)) { dialogInterface, i ->
                    Toast.makeText(application, "Auto key backup is not enabled", Toast.LENGTH_SHORT).show();
                }.show();

    }

    private fun showAlertForRetypeOrNewKey(userId: String, decryptedData: String) {
        AlertDialog.Builder(VectorApp.getCurrentActivity()).setMessage("Try again or using new key (Old data will be lost)")
                .setTitle("Error")
                .setNegativeButton("Try again") { dialogInterface, i ->
                    handleRestoreDeleteAndExportNewKey(userId, decryptedData);
                }
                .setPositiveButton("New key") { dialogInterface, i ->
                    matrixService.deleteKeyBackup(userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                matrixService.exportNewBackupKey(decryptedData).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            Toast.makeText(application, R.string.export_key_successfully, Toast.LENGTH_SHORT).show();
                                        }, {
                                            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                        });
                            }, {
                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
                .show();
    }

    private fun showEnterPassphraseAlertDialog(): Observable<String> {
        return Observable.create { emitter ->
            val editTextPassphrase = EditText(VectorApp.getCurrentActivity());
            editTextPassphrase.isSingleLine = true;
            editTextPassphrase.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD;
            val dialog = AlertDialog.Builder(VectorApp.getCurrentActivity())
                    .setMessage("Passphrase is wrong")
                    .setTitle("Error")
                    .setNegativeButton("OK") { dialogInterface, i ->
                        if (editTextPassphrase.text.toString().isNullOrEmpty()) {
                            emitter.onError(Throwable("Passphrase is empty"))
                            emitter.onComplete();
                        } else {
                            emitter.onNext(editTextPassphrase.text.toString());
                        }
                    }
                    .setPositiveButton("Close") { dialogInterface, i ->
                        emitter.onError(Throwable("Auto key backup is stopped"))
                        emitter.onComplete();
                    }
                    .setView(editTextPassphrase)
                    .create();
            dialog.show();
        }
    }

    @SuppressLint("CheckResult")
    private fun autoKeyBackupWhenCreatePassphrase(userId: String, password: String?, decryptedData: String, passphraseResponse: PassphraseResponse) {
        matrixService.getKeyBackUpData(passphraseResponse.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it.state) {
                6, 7, 4 -> {
                    matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                            }, {
                                if (password.isNullOrEmpty()) {
                                    logout();
                                } else {
                                    handleRestoreDeleteAndExportNewKey(userId, decryptedData);
                                }
                            });
                }
                3 -> {
                    matrixService.exportNewBackupKey(decryptedData).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                Toast.makeText(application, R.string.export_key_successfully, Toast.LENGTH_SHORT).show();
                            }, {
                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }
    }
}