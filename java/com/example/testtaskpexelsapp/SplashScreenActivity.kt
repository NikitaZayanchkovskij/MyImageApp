package com.example.testtaskpexelsapp


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.testtaskpexelsapp.constants.MyConstants
import com.example.testtaskpexelsapp.databinding.SplashScreenActivityBinding


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {
    private lateinit var binding: SplashScreenActivityBinding
    private lateinit var timer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startOpeningAppTimer()
    }


    override fun onStop() {
        super.onStop()
        timer.cancel()
    }


    private fun startOpeningAppTimer() {

        timer = object: CountDownTimer(MyConstants.SPLASH_SCREEN_TIMER, 1000) {

            override fun onTick(time: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }.start()
    }


}