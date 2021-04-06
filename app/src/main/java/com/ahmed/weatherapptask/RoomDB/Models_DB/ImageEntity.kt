package com.ahmed.weatherapptask.RoomDB.Models_DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity()
data class ImageEntity(
    @ColumnInfo(name = "image_name")
    val image_name: String?,

    @ColumnInfo(name = "image_path")
    val image_path: String?,

    @ColumnInfo(name = "image_uri")
    val image_uri: String?,

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
