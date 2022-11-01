package com.chihwhsu.atto.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chihwhsu.atto.data.App

@Dao
interface AttoDatabaseDao {

    @Insert
    fun insert(app: App)


    @Update
    fun update(app: App)

    @Query("DELETE from app_table WHERE package_name = :packageName")
    fun delete(packageName : String)

    @Query("DELETE FROM app_table")
    fun clear()

    @Query("SELECT * FROM app_table ORDER BY sort ASC")
    fun getAllApps(): LiveData<List<App>>

}

