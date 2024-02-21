package com.example.testtaskpexelsapp.mvvm.viewmodel


import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testtaskpexelsapp.constants.MyConstants
import com.example.testtaskpexelsapp.interfaces.IGetPexelsImagesApi
import com.example.testtaskpexelsapp.mvvm.model.CollectionsItemFromPexels
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyViewModel: ViewModel() {

    val liveSearchViewImagesData = MutableLiveData<List<ImageResourceFromPexels>>()

    val liveCuratedImagesData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment1ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment2ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment3ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment4ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment5ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment6ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()
    val liveFragment7ImageCategoryData = MutableLiveData<List<ImageResourceFromPexels>>()

    val liveProgressBarData = MutableLiveData<Int>()
    val fragmentTitlesListForTabLayout: ArrayList<String> = arrayListOf("Curated")

    private lateinit var pexelsApi: IGetPexelsImagesApi


    /** Функция отвечает за получение изображений с PexelsApi соответствующих введённой в SearchView
     * категории, например: nature, ice, watches, drawing и т.д.
     */
    suspend fun searchImagesByCategoryFromSearchView(text: String) = withContext(Dispatchers.IO) {
        val searchedCategoryResponse =
            pexelsApi.searchPexelsImagesByCategory(text, 30)

        val searchViewImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()
        searchViewImagesList.clear()
        searchViewImagesList.addAll(searchedCategoryResponse.photos)

        CoroutineScope(Dispatchers.Main).launch {
            liveSearchViewImagesData.value = searchViewImagesList
        }
    }


    /** Функция отвечает за получение данных с сервера PexelsApi используя Retrofit.
     */
    suspend fun receiveDataFromPexelsApiUsingRetrofit() = withContext(Dispatchers.IO) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(MyConstants.PEXELS_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pexelsApi = retrofit.create(IGetPexelsImagesApi::class.java)

        get30CuratedImages()
        getAll7FeaturedCollectionsImagesAccordingToTitle()
    }


    /** Функция отвечает за получение 30 популярных запросов Curated.
     */
    private suspend fun get30CuratedImages() = withContext(Dispatchers.IO) {

        val curatedImagesResponse = pexelsApi.getPexelsCuratedImages(30)

        val curatedImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

        curatedImagesList.clear()
        curatedImagesList.addAll(curatedImagesResponse.photos)

        CoroutineScope(Dispatchers.Main).launch {
            liveCuratedImagesData.value = curatedImagesList
            liveProgressBarData.value = 1
        }
    }


    /** Функция отвечает за получение всех изображений для каждого из 7 популярных заголовков во
     * второстепенном потоке.
     */
    private suspend fun getAll7FeaturedCollectionsImagesAccordingToTitle() = withContext(Dispatchers.IO) {

        /* Получаю 7 FeaturedCollections. */
        val featuredCollectionsResponse = pexelsApi.getPexelsFeaturedCollections(7)
        val featuredCollectionsList = featuredCollectionsResponse.collections

        /* Теперь на каждой итерации запрашиваю данные по категории/заголовку для каждого фрагмента:
         * на первой для 1, на второй для 2 и т.д.
         */
        var counter = 1

        for (item in featuredCollectionsList) {

            when (counter) {

                1 -> {
                    val fragment1ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment1ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment1ImagesList.clear()
                    fragment1ImagesList.addAll(fragment1ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment1ImageCategoryData.value = fragment1ImagesList
                        liveProgressBarData.value = 2
                    }

                    counter++
                }

                2 -> {
                    val fragment2ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment2ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment2ImagesList.clear()
                    fragment2ImagesList.addAll(fragment2ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment2ImageCategoryData.value = fragment2ImagesList
                        liveProgressBarData.value = 3
                    }

                    counter++
                }

                3 -> {
                    val fragment3ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment3ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment3ImagesList.clear()
                    fragment3ImagesList.addAll(fragment3ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment3ImageCategoryData.value = fragment3ImagesList
                        liveProgressBarData.value = 4
                    }

                    counter++
                }

                4 -> {
                    val fragment4ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment4ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment4ImagesList.clear()
                    fragment4ImagesList.addAll(fragment4ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment4ImageCategoryData.value = fragment4ImagesList
                        liveProgressBarData.value = 5
                    }

                    counter++
                }

                5 -> {
                    val fragment5ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment5ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment5ImagesList.clear()
                    fragment5ImagesList.addAll(fragment5ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment5ImageCategoryData.value = fragment5ImagesList
                        liveProgressBarData.value = 6
                    }

                    counter++
                }

                6 -> {
                    val fragment6ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment6ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment6ImagesList.clear()
                    fragment6ImagesList.addAll(fragment6ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment6ImageCategoryData.value = fragment6ImagesList
                        liveProgressBarData.value = 7
                    }

                    counter++
                }

                7 -> {
                    val fragment7ImagesCategoryList =
                        pexelsApi.searchPexelsImagesByCategory(item.title, 30)

                    val fragment7ImagesList: ArrayList<ImageResourceFromPexels> = arrayListOf()

                    fragment7ImagesList.clear()
                    fragment7ImagesList.addAll(fragment7ImagesCategoryList.photos)

                    fragmentTitlesListForTabLayout.add(item.title)

                    CoroutineScope(Dispatchers.Main).launch {
                        liveFragment7ImageCategoryData.value = fragment7ImagesList
                        liveProgressBarData.value = 8
                    }

                    counter++
                }
            }
        }
    }

}