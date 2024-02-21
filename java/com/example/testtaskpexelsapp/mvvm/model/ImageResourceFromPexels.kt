package com.example.testtaskpexelsapp.mvvm.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testtaskpexelsapp.constants.MyConstants


/** Аннотация Entity нужна для того, чтобы мы могли сохранять изображение в закладки используя ROOM.
 */
@Entity(tableName = MyConstants.MY_ROOM_TABLE_NAME)
data class ImageResourceFromPexels (

    /* Указываю, чтобы ROOM сама НЕ создавала id для изображений т.к. они у них уже есть, буду
     * сохранять под теми, какие присвоены им на PexelsApi.
     */
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "width")
    val width: Int,

    @ColumnInfo(name = "height")
    val height: Int,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "photographer")
    val photographer: String,

    @ColumnInfo(name = "photographer_url")
    val photographer_url: String,

    @ColumnInfo(name = "photographer_id")
    val photographer_id: Int,

    @ColumnInfo(name = "avg_color")
    val avg_color: String,

    @Embedded
    val src: ImageSrcDetailsFromPexels,

    @ColumnInfo(name = "liked")
    val liked: Boolean,

    @ColumnInfo(name = "alt")
    val alt: String

)