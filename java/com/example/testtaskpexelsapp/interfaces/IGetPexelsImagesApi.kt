package com.example.testtaskpexelsapp.interfaces


import com.example.testtaskpexelsapp.constants.MyConstants
import com.example.testtaskpexelsapp.mvvm.model.FeaturedCollectionsResponseFromPexels
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import com.example.testtaskpexelsapp.mvvm.model.WholeResponseFromPexels
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface IGetPexelsImagesApi {


    @Headers("Authorization: " + MyConstants.MY_PEXELS_API_KEY)
    @GET("photos/{id}")
    suspend fun getPexelsImageById(@Path("id") id: Int): ImageResourceFromPexels


    @Headers("Authorization: " + MyConstants.MY_PEXELS_API_KEY)
    @GET("curated")
    suspend fun getPexelsCuratedImages(
        @Query("per_page") amountOfImagesPerPage: Int): WholeResponseFromPexels


    @Headers("Authorization: " + MyConstants.MY_PEXELS_API_KEY)
    @GET("collections/featured")
    suspend fun getPexelsFeaturedCollections(
        @Query("per_page") amountOfPopularTitles: Int): FeaturedCollectionsResponseFromPexels


    @Headers("Authorization: " + MyConstants.MY_PEXELS_API_KEY)
    @GET("search")
    suspend fun searchPexelsImagesByCategory(
        @Query("query") category: String,
        @Query("per_page") amountOfImagesPerPage: Int): WholeResponseFromPexels

}