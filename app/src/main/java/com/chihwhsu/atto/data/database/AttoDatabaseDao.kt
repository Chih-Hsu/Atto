package com.chihwhsu.atto.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chihwhsu.atto.data.App

@Dao
interface AttoDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(app: App)

    @Update
    fun update(app: App)

    @Query("Update app_table set label = :label where package_name = :packageName")
    fun updateLabel(packageName: String , label : String?)

    @Query("DELETE from app_table WHERE package_name = :packageName")
    fun delete(packageName : String)

    @Query("DELETE FROM app_table")
    fun clear()

    @Query("SELECT * FROM app_table ORDER BY sort ASC")
    fun getAllApps(): LiveData<List<App>>

}

