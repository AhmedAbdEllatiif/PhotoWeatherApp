package com.ahmed.weatherapptask.RoomDB.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity

@Dao
interface ImageDao {
    
    @Query("SELECT * FROM ImageEntity ORDER BY id DESC")
    fun getAllImages(): LiveData<List<ImageEntity>>

    @Insert
    suspend fun insert(image: ImageEntity)

    @Query("SELECT * from ImageEntity WHERE id = :id")
    suspend fun getImageById(id: Int): ImageEntity

    @Delete
    suspend fun deleteImage(image: ImageEntity)

    @Query("DELETE FROM ImageEntity")
    suspend fun deleteAllImages()

}