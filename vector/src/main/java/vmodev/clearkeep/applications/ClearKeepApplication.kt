package vmodev.clearkeep.applications

import android.annotation.SuppressLint
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.widget.Toast
import dagger.android.AndroidInjector
import im.vector.Matrix
import im.vector.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.rests.ClearKeepApis
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import javax.inject.Inject


class ClearKeepApplication : DaggerVectorApp(), IApplication {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;
    @Inject
    lateinit var database: ClearKeepDatabase;
    @Inject
    lateinit var matrixService: MatrixService;

    private var currentTheme: Int = R.style.LightTheme;

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun applicationInjector(): AndroidInjector<out ClearKeepApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    fun setEventHandler() {
        val mxSession = Matrix.getInstance(this).defaultSession;
        mxSession!!.dataHandler!!.addListener(matrixEventHandler.getMXEventListener(mxSession))
    }

    override fun getCurrentTheme(): Int {
        return currentTheme;
    }

    override fun setCurrentTheme(theme: Int) {
        currentTheme = theme;
    }

    override fun getApplication(): Application {
        return this;
    }

    @SuppressLint("CheckResult")
    override fun startAutoKeyBackup() {
        matrixService.getPassphrase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
            Toast.makeText(this, passphrase.passphrase, Toast.LENGTH_SHORT).show();
            matrixService.getKeyBackUpData(passphrase.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                when (it.state) {
                    4 -> {
                        matrixService.restoreBackupFromPassphrase(passphrase.passphrase).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Toast.makeText(this, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                }, {
                                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                });
                    }
                    0, 1 -> {
                        matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    if (it == 4) {
                                        matrixService.restoreBackupFromPassphrase(passphrase.passphrase).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    Toast.makeText(this, "Restore success: " + it.successfullyNumberOfImportedKeys + "/" + it.totalNumberOfKeys + " keys", Toast.LENGTH_SHORT).show();
                                                }, {
                                                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }, {
                                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
            })

        }, {
            matrixService.createPassphrase("PBKDF2").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ passphrase ->
                Toast.makeText(this, passphrase.passphrase, Toast.LENGTH_SHORT).show();
                matrixService.getKeyBackUpData(passphrase.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    when (it.state) {
                        6, 7, 4 -> {
                            matrixService.deleteKeyBackup(it.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                matrixService.exportNewBackupKey(passphrase.passphrase).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show();
                                        }, {
                                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                        });
                            }, {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                            });
                        }
                        3 -> {
                            matrixService.exportNewBackupKey(passphrase.passphrase).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show();
                                    }, {
                                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                    });
                        }
                        0, 1 -> {
                            matrixService.checkBackupKeyStateWhenStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        autoKeyBackupWhenCreatePassphrase(passphrase);
                                    }, {
                                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                });
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
            });
        })
    }

    @SuppressLint("CheckResult")
    private fun autoKeyBackupWhenCreatePassphrase(passphraseResponse: PassphraseResponse) {
        matrixService.getKeyBackUpData(passphraseResponse.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it.state) {
                6, 7, 4 -> {
                    matrixService.deleteKeyBackup(it.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        matrixService.exportNewBackupKey(passphraseResponse.passphrase).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show();
                                }, {
                                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                                });
                    }, {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                    });
                }
                3 -> {
                    matrixService.exportNewBackupKey(passphraseResponse.passphrase).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                Toast.makeText(this, it, Toast.LENGTH_SHORT).show();
                            }, {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }
    }
}