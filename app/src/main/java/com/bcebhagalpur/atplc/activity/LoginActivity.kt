package com.bcebhagalpur.atplc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bcebhagalpur.atplc.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mobileNumber: String = ""
    private var verificationID: String = ""
    private var token: String = ""
    lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var etNumber: EditText

    private lateinit var adView1: AdView
    private lateinit var adView2: AdView
    private var adRequest: AdRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etNumber = findViewById(R.id.etNumber)
        mobileNumber = etNumber.text.toString()
        mAuth = FirebaseAuth.getInstance()

        adView1=findViewById(R.id.adView1)
        adView2=findViewById(R.id.adView2)
        adRequest=AdRequest.Builder().build()
        adView1.loadAd(adRequest)
        adView2.loadAd(adRequest)

        btnSignIn.setOnClickListener {

            mobileNumber = etNumber.text.toString()

            if (mobileNumber.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                loginTask()
            } else {
                etNumber.error = "Enter valid phone number"
            }
        }

    }

    private fun loginTask() {

        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                if (p0 != null) {
                    signInWithPhoneAuthCredential(p0)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@LoginActivity, "Invalid phone number or verification failed.",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                progressBar.visibility = View.GONE
                verificationID = p0
                token = p1.toString()

                etNumber.setText("")

                etNumber.hint = "Enter OTP "
                btnSignIn.text = getString(R.string.v1)

                btnSignIn.setOnClickListener {
                    try {
                        progressBar.visibility = View.VISIBLE
                        try {
                            verifyAuthentication(verificationID, etNumber.text.toString())
                        }catch (e:IllegalArgumentException){
                           Toast.makeText(this@LoginActivity,"please enter otp",Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }

                Log.e("Login : verificationId ", verificationID)
                Log.e("Login : token ", token)

                btnResentOtp.setOnClickListener {
                    if (mobileNumber != null) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            mobileNumber, 1, TimeUnit.MINUTES, this@LoginActivity, mCallBacks
                        )
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "please enter mobile number",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                progressBar.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Time out", Toast.LENGTH_LONG).show()
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallBacks
        )


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@LoginActivity
            ) { task ->
                if (task.isSuccessful) {
                    task.result!!.user
                    progressBar.visibility = View.GONE
                    updateUi()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "invalid otp", Toast.LENGTH_LONG).show()
                    }
                }
            }


    }

    private fun verifyAuthentication(verificationID: String, otpText: String) {
        try {
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationID, otpText))
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun updateUi() {
        mobileNumber = etNumber.text.toString()
        val intent1 = Intent(this@LoginActivity, StudentActivity::class.java)
        val intent2 = intent
        intent2.putExtra("mobileNumber", mobileNumber)
        startActivity(intent1)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                finish()
            })

        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}