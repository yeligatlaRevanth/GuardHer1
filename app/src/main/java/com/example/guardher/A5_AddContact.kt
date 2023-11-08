package com.example.guardher

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class A5_AddContact : AppCompatActivity() {
    lateinit var edt_friendName: EditText
    lateinit var edt_friendNum: EditText
    lateinit var btn_addFriend: Button


    var friendName: String = ""
    var friendNum: String = ""


//    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var sharedPref: SharedPreferences


    lateinit var database : UserBookDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a5_add_contact)

        edt_friendName = findViewById(R.id.edt_friendName)
        edt_friendNum = findViewById(R.id.edt_friendNum)
        btn_addFriend = findViewById(R.id.btn_addFriend)


//        var uNum = mAuth.currentUser!!.phoneNumber.toString()
        sharedPref = getSharedPreferences("SharedPref_GuardHer", MODE_PRIVATE)
        database = UserBookDatabase.getDatabase(applicationContext)

        btn_addFriend.setOnClickListener {
            friendName = edt_friendName.text.toString()
            friendNum = edt_friendNum.text.toString()
            if(friendNum.length == 10 && friendName.isNotEmpty())
            {
                GlobalScope.launch {
                    database.UserBookDao().insert(UserBook(0,
                        sharedPref.getString("uNum", null)!!,friendName,friendNum))

                }

                val i = Intent(this@A5_AddContact, A4_UserBook::class.java)
                startActivity(i)
            }
            else
            {
                Toast.makeText(this@A5_AddContact, "Invalid Details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}