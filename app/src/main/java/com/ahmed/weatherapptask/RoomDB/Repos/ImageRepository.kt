package com.ahmed.weatherapptask.RoomDB.Repos

import android.app.Application
import androidx.lifecycle.LiveData
import com.ahmed.weatherapptask.RoomDB.AppMainDB
import com.ahmed.weatherapptask.RoomDB.Dao.ImageDao
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity

class ImageRepository(application: Application) {

    private val appMainDB: AppMainDB
    private val imageDao: ImageDao

    init {
        appMainDB = AppMainDB.DatabaseBuilder.getInstance(application)
        imageDao = appMainDB.imageDao()
    }

    suspend fun insertImage(image: ImageEntity) {
        imageDao.insert(image)
    }

    suspend fun getImageById(id: Int): ImageEntity {
        return imageDao.getImageById(id)
    }

    suspend fun deleteImage(image: ImageEntity){
        imageDao.deleteImage(image)
    }

    suspend fun deleteAllImages(){
        imageDao.deleteAllImages()
    }

    fun getAllImages(): LiveData<List<ImageEntity>> {
        return imageDao.getAllImages()
    }

}