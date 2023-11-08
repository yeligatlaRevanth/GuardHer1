package com.example.guardher

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class A4_UserBook : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var database: UserBookDatabase
    lateinit var btn_fab: FloatingActionButton

    val mAuth = FirebaseAuth.getInstance()
    lateinit var sharedPreferences: SharedPreferences

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a4_user_book)
        btn_fab = findViewById(R.id.btn_fab)
        listView = findViewById(R.id.lst1)
        database = UserBookDatabase.getDatabase(applicationContext)
        sharedPreferences = getSharedPreferences("SharedPref_GuardHer", MODE_PRIVATE)

        showData()
        btn_fab.setOnClickListener {
            val i = Intent(this@A4_UserBook, A5_AddContact::class.java)
            startActivity(i)

        }

    }

    fun showData()
    {
        var uNumber = ""
        if(sharedPreferences.contains("uNum"))
        {
            uNumber = sharedPreferences.getString("uNum", null)!!
        }
        else
        {
            uNumber = mAuth.currentUser!!.phoneNumber.toString()
        }
        database.UserBookDao().getUser(uNumber).observe(this){
            val adapter = MyAdapter(this, R.layout.my_list_item1, it)
            listView.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_userAcc -> {
                val i = Intent(this@A4_UserBook, A3_Userpage::class.java)
                startActivity(i)
            }
            R.id.menu_logout -> {
                logUserOut()
            }
            R.id.menu_aboutUs -> Toast.makeText(this, "Dummy Page", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
    fun logUserOut()
    {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val i = Intent(this@A4_UserBook, A1_LoginPage::class.java)
        startActivity(i)
    }
}