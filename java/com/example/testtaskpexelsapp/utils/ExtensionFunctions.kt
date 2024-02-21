package com.example.testtaskpexelsapp.utils


import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testtaskpexelsapp.R


/** Функция открывает фрагменты из фрагментов.
 */
fun Fragment.openFragmentFromFragment(fragmentToOpen: Fragment) {
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            android.R.anim.fade_in, android.R.anim.fade_out,
            android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.fragmentHolderForHomeAndBookmarks, fragmentToOpen, "test_frag")
        .addToBackStack("test_frag")
        .commit()
}


fun Fragment.closeFragmentFromFragment() {
    (activity as AppCompatActivity).supportFragmentManager
        .popBackStack()
}


fun AppCompatActivity.closeFragmentFromActivity() {
    supportFragmentManager.popBackStack()
}


/** Функция открывает фрагменты из активити.
 */
fun AppCompatActivity.openFragmentFromActivity(fragmentToOpen: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            android.R.anim.fade_in, android.R.anim.fade_out,
            android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.fragmentHolderForHomeAndBookmarks, fragmentToOpen, "searched_images_frag")
        .addToBackStack("searched_images_frag")
        .commit()
}


/** Функция показывает сообщение Toast для пользователя в каком-либо активити.
 */
fun AppCompatActivity.showToastForTheUser(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


/** Функция показывает сообщение Toast для пользователя в каком-либо фрагменте.
 */
fun Fragment.showToastForTheUser(message: String) {
    Toast.makeText(activity as AppCompatActivity, message, Toast.LENGTH_LONG).show()
}