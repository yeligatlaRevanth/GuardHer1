package com.example.guardher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class A2_SignupPage : AppCompatActivity() {
    lateinit var edt_uName: EditText
    lateinit var edt_uNum: EditText
    lateinit var btn_signup: Button

    var uName: String = ""
    var uNum: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a2_signup_page)

        edt_uName = findViewById(R.id.edt_uname)
        edt_uNum = findViewById(R.id.edt_uNum)
        btn_signup = findViewById(R.id.btn1_signup)

        btn_signup.setOnClickListener {
            uName = edt_uName.text.toString()
            uNum = edt_uNum.text.toString()
        }
    }
}