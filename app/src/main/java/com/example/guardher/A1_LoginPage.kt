package com.example.guardher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit

class A1_LoginPage : AppCompatActivity() {
    lateinit var edt_phone: EditText
    lateinit var btn_login: Button
    lateinit var otpLayout: TextInputLayout
    lateinit var edt_otp: EditText
    lateinit var verificationCode: String
    lateinit var resendingToken: ForceResendingToken

    var layoutIsVisible = false

    lateinit var uNum: String
    lateinit var uOtp: String

    lateinit var sharedPref : SharedPreferences
    val sharedPrefKey = "SharedPref_GuardHer"
    val mAuth: FirebaseAuth  = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a1_user_page)

        edt_phone = findViewById(R.id.lgn_edtPhone)
        btn_login = findViewById(R.id.lgn_btn)
        otpLayout = findViewById(R.id.lgn_otpLayout)
        edt_otp  = findViewById(R.id.lgn_edtOtp)



        sharedPref = getSharedPreferences(sharedPrefKey, MODE_PRIVATE)
        if(sharedPref.contains("uNum"))
        {

            val i = Intent(this@A1_LoginPage, A3_Userpage::class.java)
            uNum = sharedPref.getString("uNum", null).toString()
            startActivity(i)
        }

        btn_login.setOnClickListener {
            if(!layoutIsVisible)
            {
                if(edt_phone.text.toString().trim().isEmpty())
                {
                    Toast.makeText(this,"Please enter a number", Toast.LENGTH_SHORT).show()
                }
                else if(edt_phone.text.toString().trim().length != 10)
                {
                    Toast.makeText(this, "Invalid Number. Please recheck", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    otpLayout.visibility = View.VISIBLE
                    uNum = edt_phone.text.toString().trim()
                    layoutIsVisible = true
                    sendOtp(uNum, false)
                }
            }
            else
            {
                uOtp = edt_otp.text.toString().trim()
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, uOtp)
                signIn(credential)
            }
        }

    }
     fun sendOtp(phoneNum: String, isResend: Boolean)
    {
        val myBuilder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91 $phoneNum")
            .setTimeout(10L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Toast.makeText(this@A1_LoginPage,"User Successfully Logged In", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@A1_LoginPage, "Not Logged In; ${e.message.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("ver_err", e.message.toString())


                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationCode = p0
                    resendingToken = p1
                    Toast.makeText(this@A1_LoginPage, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                }
            })
        if(isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(myBuilder.setForceResendingToken(resendingToken).build())
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(myBuilder.build())
        }
    }


    fun signIn(phoneAuthCredential: PhoneAuthCredential)
    {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener {
            if(it.isSuccessful)
            {
                mySharedPreference()
                val i = Intent(this@A1_LoginPage, A3_Userpage::class.java)
                i.putExtra("uNum", uNum )
                startActivity(i)

            }
            else
            {
                Toast.makeText(this@A1_LoginPage, "OTP Verification Failed", Toast.LENGTH_SHORT).show()

            }
        }

    }

    fun mySharedPreference()
    {

        val editor = sharedPref.edit()
        editor.putString("uNum", uNum)
        editor.commit()

    }
}