package vmodev.clearkeep.autokeybackups

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.text.InputType
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import im.vector.R
import im.vector.VectorApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
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

    @SuppressLint("CheckResult")
    override fun startAutoKeyBackup(userId: String, password: String?) {
        val passwordForGenerateKey = userId + "COLIAKIP";
        matrixService.getPassphrase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
            Toast.makeText(application, R.string.get_passphrase_successfully, Toast.LENGTH_SHORT).show();
            val decryptedData = crypto.decrypt(passwordForGenerateKey, passphrase.data.passphrase);
            matrixService.getKeyBackUpData(passphrase.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                when (it.state) {
                    4 -> {
                        matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                }, {
                                    showEnterPassphraseAlertDialog().subscribeOn(AndroidSchedulers.mainThread())
                                            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                                matrixService.restoreBackupFromPassphrase(it)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe({
                                                            Toast.makeText(application, it.toString(), Toast.LENGTH_SHORT).show();
                                                        },{
                                                            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                                        });
                                            },{
                                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                            });
                                });
                    }
                    0, 1 -> {
                        matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    if (it == 4) {
                                        matrixService.restoreBackupFromPassphrase(decryptedData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    Toast.makeText(application, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                                }, {
                                                    showEnterPassphraseAlertDialog().subscribeOn(AndroidSchedulers.mainThread())
                                                            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                                                matrixService.restoreBackupFromPassphrase(it)
                                                                        .subscribeOn(Schedulers.io())
                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                        .subscribe({
                                                                            Toast.makeText(application, it.toString(), Toast.LENGTH_SHORT).show();
                                                                        },{
                                                                            Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                                                        });
                                                            },{
                                                                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                                            });
                                                });
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
            val encryptedPassphrase = crypto.encrypt(passwordForGenerateKey, password + "COLIAKIP")
            matrixService.createPassphrase(encryptedPassphrase).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
                Toast.makeText(application, R.string.create_new_passphrase_successfully, Toast.LENGTH_SHORT).show();
                matrixService.getKeyBackUpData(passphrase.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    when (it.state) {
                        6, 7, 4 -> {
                            matrixService.deleteKeyBackup(it.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                val decryptedData = crypto.decrypt(passwordForGenerateKey, passphrase.data.passphrase);
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
                        3 -> {
                            val decryptedData = crypto.decrypt(passwordForGenerateKey, passphrase.data.passphrase);
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
                                        autoKeyBackupWhenCreatePassphrase(passwordForGenerateKey, passphrase);
                                    }, {
                                        Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }, {
                    Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
                });
            }, {
                Toast.makeText(application, it.message, Toast.LENGTH_SHORT).show();
            });
        })
    }

    private fun showEnterPassphraseAlertDialog() : Observable<String>{
        return Observable.create { emitter ->
            val editTextPassphrase = EditText(VectorApp.getCurrentActivity());
            editTextPassphrase.isSingleLine = true;
            editTextPassphrase.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
            val dialog = AlertDialog.Builder(VectorApp.getCurrentActivity())
                    .setMessage("Passphrase is wrong")
                    .setTitle("Error")
                    .setNegativeButton("OK") { dialogInterface, i ->
                        if(editTextPassphrase.text.toString().isNullOrEmpty()){
                            emitter.onError(Throwable("Passphrase is empty"))
                            emitter.onComplete();
                        }
                        else{
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
    private fun autoKeyBackupWhenCreatePassphrase(password: String, passphraseResponse: PassphraseResponse) {
        val decryptedData = crypto.decrypt(password, passphraseResponse.data.passphrase);
        matrixService.getKeyBackUpData(passphraseResponse.data.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it.state) {
                6, 7, 4 -> {
                    matrixService.deleteKeyBackup(it.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
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