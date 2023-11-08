package com.example.guardher

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.auth.FirebaseAuth

@Dao
interface UserBookDao {

    @Insert
    suspend fun insert(userbook: UserBook)

    @Update
    suspend fun update(userbook: UserBook)

    @Delete
    suspend fun delete(userbook: UserBook)

    @Query(" SELECT * FROM userbook WHERE userNum = :userNum ")
    fun getUser(userNum: String): LiveData<List<UserBook>>


    @Query(" SELECT phone FROM userbook WHERE userNum = :userNum ")
    fun getPhoneNumbers(userNum: String): LiveData<List<String>>
}