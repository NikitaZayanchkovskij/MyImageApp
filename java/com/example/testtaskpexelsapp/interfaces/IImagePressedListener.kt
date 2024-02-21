package com.example.testtaskpexelsapp.interfaces


import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels


/** Функция из этого интерфейса будет запускаться при нажатии на какое-либо изображение из списка.
 * При нажатии на изображение открывается Image Details Screen.
 */
interface IImagePressedListener {

    fun imagePressed(image: ImageResourceFromPexels)

}