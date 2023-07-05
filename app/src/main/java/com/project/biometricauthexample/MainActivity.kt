package com.project.biometricauthexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var tvInfo: AppCompatTextView
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInfo = findViewById(R.id.info_tv)
        findViewById<AppCompatImageView>(R.id.finger_print_icon_iv).setOnClickListener {
            checkDeviceHasBiometrics()
        }


    }

    private fun createBiometricListener() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, errString, Toast.LENGTH_LONG).show()
                    tvInfo.text = errString
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Authentication Failed", Toast.LENGTH_LONG)
                        .show()
                    tvInfo.text = "Authentication Failed"
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@MainActivity, "Authentication Success", Toast.LENGTH_LONG)
                        .show()
                    tvInfo.text = "Authentication Success"
                }
            })
    }

    private fun createPromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for app")
            .setSubtitle("login using your biometric on credential")
            .setNegativeButtonText("CANCEL BIOMETRIC")
            .build()
    }

    private fun checkDeviceHasBiometrics() {
        val bioMetricManager = BiometricManager.from(this)
        when (bioMetricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                tvInfo.text = "App Can Authenticate Using biometric"
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                tvInfo.text = "No Biometric Feature Available on this device"
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                tvInfo.text = "Biometric Feature are currently Unavailable"
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                tvInfo.text = "Device not enabled for Biometric Feature"
            }

            else -> {
                tvInfo.text = "Something Went Wrong"
            }
        }
    }
}