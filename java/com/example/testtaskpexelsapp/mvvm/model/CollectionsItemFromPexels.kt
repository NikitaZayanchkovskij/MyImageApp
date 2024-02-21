package com.example.testtaskpexelsapp.mvvm.model


data class CollectionsItemFromPexels (

    val id: String,
    val title: String,
    val description: String,
    val private: Boolean,
    val media_count: Int,
    val photos_count: Int,
    val videos_count: Int

)
