package com.example.guardher
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userbook")
data class UserBook(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val userNum: String,
    val name: String,
    val phone: String
)
