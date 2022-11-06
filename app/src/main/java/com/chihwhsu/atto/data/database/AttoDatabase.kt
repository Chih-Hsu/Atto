package com.chihwhsu.atto.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Event

@Database(entities = [App::class,Event::class], version = 4, exportSchema = false)
abstract class AttoDatabase : RoomDatabase() {

    abstract val attoDatabaseDao: AttoDatabaseDao


    companion object {

        @Volatile
        private var INSTANCE: AttoDatabase? = null


        fun getInstance(context: Context): AttoDatabase {

            synchronized(this) {


                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AttoDatabase::class.java,
                        "app_database"
                    )

                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}