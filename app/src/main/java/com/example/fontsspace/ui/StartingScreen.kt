package com.example.fontsspace.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.fontsspace.R
import com.example.fontsspace.databinding.StartingScreenBinding
import com.example.fontsspace.other.Utils

class StartingScreen : AppCompatActivity() {

    private lateinit var mainBinding: StartingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        mainBinding = StartingScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({

            if (getSharedPreferences(Utils.walkThrough)){
                startActivity(Intent(this, WalkThroughScreen::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }, 1000)

    }

    private fun getSharedPreferences(keyShare: String): Boolean {
        val prefs: SharedPreferences = getSharedPreferences(
            "font",
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(keyShare, true)
    }

}