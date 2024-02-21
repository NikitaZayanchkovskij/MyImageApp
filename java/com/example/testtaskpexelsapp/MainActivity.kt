package com.example.testtaskpexelsapp


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import com.example.testtaskpexelsapp.adapters.MyViewPager2Adapter
import com.example.testtaskpexelsapp.databinding.ActivityMainBinding
import com.example.testtaskpexelsapp.fragments.BookmarksFragment
import com.example.testtaskpexelsapp.fragments.Fragment0ForTabLayoutCuratedImages
import com.example.testtaskpexelsapp.fragments.Fragment1ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment2ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment3ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment4ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment5ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment6ForTabLayout
import com.example.testtaskpexelsapp.fragments.Fragment7ForTabLayout
import com.example.testtaskpexelsapp.fragments.FragmentImagesFromSearchViewRequest
import com.example.testtaskpexelsapp.mvvm.viewmodel.InternetConnectionManager
import com.example.testtaskpexelsapp.mvvm.viewmodel.MyViewModel
import com.example.testtaskpexelsapp.utils.closeFragmentFromActivity
import com.example.testtaskpexelsapp.utils.openFragmentFromActivity
import com.example.testtaskpexelsapp.utils.showToastForTheUser
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    /* Здесь специально не сделал переменную private т.к. нужен доступ к элементам View этого
     * активити (MainActivity), чтобы скрыть их при открытии некоторых фрагментов,
     * например ImageDetailsFragment, а при закрытии фрагмента - снова показать.
     */
    lateinit var binding: ActivityMainBinding

    private val myViewModel: MyViewModel by viewModels()

    private val fragmentsListForTabLayout: ArrayList<Fragment> = arrayListOf (
        Fragment0ForTabLayoutCuratedImages(this), Fragment1ForTabLayout(this),
        Fragment2ForTabLayout(this), Fragment3ForTabLayout(this),
        Fragment4ForTabLayout(this), Fragment5ForTabLayout(this),
        Fragment6ForTabLayout(this), Fragment7ForTabLayout(this)
    )

    private lateinit var networkManager: InternetConnectionManager
    private var applicationsFirstStart: Boolean = true
    var homeScreenIsOpened: Boolean = true
    private var bookmarksScreenIsOpened: Boolean = false
    private var jobOfReceivingDataFromPexels: Job? = null
    private var jobOfReceivingSearchViewDataFromPexels: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.max = 8

        checkInternetConnection()
        setUpSearchView()
        bottomMenuButtons()
    }


    /** Если пользователь закрывает или сворачивает приложение не дождавшись загрузки изображений -
     * то отменяем работу по получению этих изображений с сервера (pexels.com)
     */
    override fun onStop() {
        super.onStop()
        jobOfReceivingDataFromPexels?.cancel()
        jobOfReceivingSearchViewDataFromPexels?.cancel()
    }


    /** Функция проверяет, есть ли подключение к интернету.
     * Если есть - можно получать данные с сервера.
     * Если нет - показываем заглушку и прячем всё остлальное.
     *
     * Если приложение открыто у пользователя и пропадает интернет или он его выключает, то когда
     * снова появится соединение с интернетом - данные не будут повторно запрашиваться с сервера,
     * а просто возьмутся из LiveData используя MVVM ViewModel.
     * Это сэкономит количество запросов к базе данных.
     * Для этого и делаю проверку if (applicationsFirstStart == true).
     */
    private fun checkInternetConnection() = with(binding){
        networkManager = InternetConnectionManager(this@MainActivity)

        networkManager.observe(this@MainActivity) { internet ->

            if (internet == true) {

                if (applicationsFirstStart == true) {
                    tabLayout.visibility = View.VISIBLE
                    viewPager2.visibility = View.VISIBLE
                    imNoWiFi.visibility = View.GONE
                    tvTryAgain.visibility = View.GONE

                    jobOfReceivingDataFromPexels = CoroutineScope(Dispatchers.Main).launch {

                        myViewModel.liveProgressBarData.observe(this@MainActivity) { progress ->
                            binding.progressBar.progress = progress
                        }

                        myViewModel.receiveDataFromPexelsApiUsingRetrofit()

                        /* После того, как получили все изображения - прячем ProgressBar. */
                        progressBar.visibility = View.GONE

                        /* После того как получили все изображения Curated, изображения для каждой
                         * из 7 категорий, и получили название каждой из 7 категорий - устанавливаем
                         * названия категорий во вкладки TabLayout.
                         */
                        setUpTabLayout()
                    }

                    applicationsFirstStart = false

                } else {
                    tabLayout.visibility = View.VISIBLE
                    viewPager2.visibility = View.VISIBLE
                    imNoWiFi.visibility = View.GONE
                    tvTryAgain.visibility = View.GONE
                }

            } else {
                tabLayout.visibility = View.GONE
                viewPager2.visibility = View.GONE
                imNoWiFi.visibility = View.VISIBLE
                tvTryAgain.visibility = View.VISIBLE

                tvTryAgain.setOnClickListener {
                    showToastForTheUser(getString(R.string.try_again_no_internet))
                }
            }
        }
    }


    /** Функция настраивает SearchView и отвечает за то, что происходит непосредственно при наборе
     * текста и за то, что происходит при нажатии на кнопку поиска.
     */
    private fun setUpSearchView() = with(binding) {

        searchView.queryHint = resources.getString(R.string.search_bar_hint)

        searchView.setOnQueryTextListener(object: OnQueryTextListener {

            /* Запускается при нажатии на иконку лупы/кнопку поиска на моб. клавиатуре.
             * Если поле не пустое - проверяем соединение с интернетом.
             * Если соединение есть - то делаем запрос.
             */
            override fun onQueryTextSubmit(text: String?): Boolean {

                if (text != null) {

                    networkManager.observe(this@MainActivity) {

                        if (it == true) {

                            jobOfReceivingSearchViewDataFromPexels = CoroutineScope(Dispatchers.Main).launch {

                                myViewModel.searchImagesByCategoryFromSearchView(text)

                                mainLinearLayWithViewElements.visibility = View.GONE
                                bottomNavView.visibility = View.GONE

                                openFragmentFromActivity(
                                    FragmentImagesFromSearchViewRequest(text, this@MainActivity)
                                )
                            }

                        } else {
                           showToastForTheUser(getString(R.string.try_again_no_internet))
                        }
                    }
                }
                return true
            }

            /* Эта функция запускается каждый раз при вводе новой буквы в SearchView - не использую
             * это т.к. тогда будет очень много запросов к базе данных.
             */
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }


    /** Функция подключает ViewPager2Adapter к TabLayout и присваивает слушатель нажатий на вкладки
     * TabLayout.
     */
    private fun setUpTabLayout() = with(binding) {
        val viewPager2Adapter = MyViewPager2Adapter(this@MainActivity, fragmentsListForTabLayout)
        viewPager2.adapter = viewPager2Adapter

        TabLayoutMediator(tabLayout, viewPager2) { tabItem, position ->

            /* Устанавливаем название популярного заголовка Featured Collections для
             * соответствующего фрагмента.
             */
            tabItem.text = myViewModel.fragmentTitlesListForTabLayout[position]

        }.attach()
    }


    /** Слушатели нажатий на кнопки нижнего меню (BottomNavigationMenu).
     */
    private fun bottomMenuButtons() = with(binding) {

        bottomNavView.setOnItemSelectedListener { menuButton ->

            when (menuButton.itemId) {
                R.id.home_screen -> {
                    if (bookmarksScreenIsOpened == true) {
                        mainLinearLayWithViewElements.visibility = View.VISIBLE
                        closeFragmentFromActivity()
                        homeScreenIsOpened = true
                    }
                }
                R.id.bookmarks_screen -> {
                    mainLinearLayWithViewElements.visibility = View.GONE
                    openFragmentFromActivity(BookmarksFragment(this@MainActivity))
                    homeScreenIsOpened = false
                    bookmarksScreenIsOpened = true
                }
            }

            true
        }
    }


}