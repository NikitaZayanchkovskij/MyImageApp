package com.example.testtaskpexelsapp.fragments


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.testtaskpexelsapp.MainActivity
import com.example.testtaskpexelsapp.R
import com.example.testtaskpexelsapp.databinding.ImageDetailsScreenBinding
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import com.example.testtaskpexelsapp.room.MyRoomDataBase
import com.example.testtaskpexelsapp.utils.closeFragmentFromFragment
import com.example.testtaskpexelsapp.utils.openFragmentFromFragment
import com.example.testtaskpexelsapp.utils.showToastForTheUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class ImageDetailsFragment(
    private val wasOpenedFromBookmarksFragment: Boolean,
    private val wasOpenedFromSearchViewFragment: Boolean,
    private val context: MainActivity, private val image: ImageResourceFromPexels): Fragment() {

    private lateinit var binding: ImageDetailsScreenBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ImageDetailsScreenBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolBar()
        fillImageInfoToUI()
        buttonDownloadTheImage()
        buttonAddImageToBookmarks()
    }


    /** Функция присваивает слушатель нажатий стрелочке назад на ToolBar-е и проверяет:
     * 1) Если фрагмент был открыт из SearchViewFragment - то просто закрываем фрагмент ImageDetails.
     * 2) Если открыли из экрана Bookmarks - то при закрытии снова показываем BottomNavView.
     * 3) Если открыли из MainActivity из 1 из 8 фрагментов TabLayout - то при закрытии снова
     * показываю все элементы view HomeScreen на MainActivity.
     */
    private fun setUpToolBar() {
        binding.tbImageDetailsScreen.setNavigationOnClickListener {

            if (wasOpenedFromBookmarksFragment == true) {
                closeFragmentFromFragment()
                context.binding.bottomNavView.visibility = View.VISIBLE

            } else if (wasOpenedFromSearchViewFragment == true) {
                closeFragmentFromFragment()

            } else {
                closeFragmentFromFragment()
                context.binding.mainLinearLayWithViewElements.visibility = View.VISIBLE
                context.binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }


    private fun fillImageInfoToUI() = with(binding) {
        tvToolBarNameSurname.text = image.photographer

        Glide
            .with(context)
            .load(image.src.original)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(imImageDetailsScreen)
    }


    /** Функция отвечает за загрузку изображения на устройство при нажатии на кнопку.
     */
    private fun buttonDownloadTheImage() {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(image.src.original)
        val downloadRequest = DownloadManager.Request(uri)
        val imageTitleForDeviceStorage = "${image.photographer}_${image.alt}"
        val imageNameForSubPath = imageTitleForDeviceStorage + ".jpg"

        downloadRequest
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(imageTitleForDeviceStorage)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, imageNameForSubPath)

        binding.imBtDownload.setOnClickListener {
            downloadManager.enqueue(downloadRequest)
            showToastForTheUser(context.getString(R.string.image_download_completed))
        }
    }


    /** Функция сохраняет изображение в закладки использую библиотеку ROOM.
     */
    private fun buttonAddImageToBookmarks() {
        val myRoomDataBase = MyRoomDataBase.getDataBase(context)

        binding.imBtBackToBookmarks.setOnClickListener {

            val imageItemForRoom = ImageResourceFromPexels (
                image.id, image.width, image.height, image.url, image.photographer,
                image.photographer_url, image.photographer_id, image.avg_color,
                image.src, image.liked, image.alt)

            CoroutineScope(Dispatchers.IO).launch {
                myRoomDataBase.receiveDaoInterface().insertImageToRoom(imageItemForRoom)
            }
        }
    }


}