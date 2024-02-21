package com.example.testtaskpexelsapp.mvvm.model


data class WholeResponseFromPexels (

    val total_results: Int,
    val page: Int,
    val per_page: Int,
    val photos: List<ImageResourceFromPexels>,
    val next_page: String

)
