package com.example.testtaskpexelsapp.interfaces


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testtaskpexelsapp.constants.MyConstants
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import kotlinx.coroutines.flow.Flow


@Dao
interface IDaoForRoom {

    @Insert
    fun insertImageToRoom(image: ImageResourceFromPexels)

    @Query ("SELECT * FROM ${MyConstants.MY_ROOM_TABLE_NAME}")
    fun getAllImagesFromRoom(): Flow<List<ImageResourceFromPexels>>

}