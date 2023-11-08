package com.example.guardher

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserBook::class], version = 1)
abstract class UserBookDatabase : RoomDatabase() {

    abstract fun UserBookDao() : UserBookDao

    companion object {
        @Volatile
        private var INSTANCE: UserBookDatabase? = null

        fun getDatabase(context: Context) : UserBookDatabase{
            if( INSTANCE == null)
            {
                synchronized(this){
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): UserBookDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                UserBookDatabase::class.java,
                "finalTable"
            ).build()
        }
    }
}