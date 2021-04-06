package com.ahmed.weatherapptask.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmed.weatherapptask.RoomDB.Dao.ImageDao
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity


@Database(entities = [ImageEntity::class], version = 1)
abstract class AppMainDB : RoomDatabase(){

    abstract fun imageDao(): ImageDao

    object DatabaseBuilder {

        private var INSTANCE: AppMainDB? = null

        fun getInstance(context: Context): AppMainDB {
            if (INSTANCE == null) {
                synchronized(AppMainDB::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppMainDB::class.java,
                "com-ahmed-weatherapptask"
            ).build()

    }

}