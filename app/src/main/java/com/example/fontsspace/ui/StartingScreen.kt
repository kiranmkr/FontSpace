package com.example.fontsspace.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.fontsspace.R
import com.example.fontsspace.databinding.StartingScreenBinding

class StartingScreen : AppCompatActivity() {

    private lateinit var mainBinding: StartingScreenBinding

    private var workerHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        mainBinding = StartingScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        Log.d("myApplication","onCreate SplashScreen")

        workerHandler.postDelayed({
            startActivity(Intent(this, WalkThroughScreen::class.java))
            finish()
        },1000)

    }

}