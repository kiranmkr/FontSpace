package com.example.fontsspace.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.transition.TransitionManager
import com.example.fontsspace.databinding.ActivityWalkThroughScreenBinding
import com.example.fontsspace.other.Utils

class WalkThroughScreen : AppCompatActivity() {

    private lateinit var mainBinding: ActivityWalkThroughScreenBinding
    private var screenIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityWalkThroughScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setSharedPreferences(Utils.walkThrough)

        mainBinding.tvSkip.setOnClickListener {
            gotoNextScreen()
        }

        mainBinding.cardNext.setOnClickListener {
            screenIndex++
            if (screenIndex == 4) {
                Log.d("myScreenVal", "${screenIndex}")
                gotoNextScreen()
            } else {
                gotoNextView()
            }
        }

        mainBinding.imgBack.setOnClickListener {
            if (screenIndex > 1) {
                screenIndex--
                gotoNextView()
            }
        }

    }

    private fun setSharedPreferences(keyShare: String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(
            "font",
            Context.MODE_PRIVATE
        ).edit()
        editor.putBoolean(keyShare, false)
        editor.apply()
    }

    private fun gotoNextView() {

        showAnimation()

        if (screenIndex > 1) {
            mainBinding.imgBack.visibility = View.VISIBLE
        } else {
            mainBinding.imgBack.visibility = View.GONE
        }

        when (screenIndex) {
            1 -> {
                mainBinding.firstLayout.visibility = View.VISIBLE
                mainBinding.secondLayout.visibility = View.GONE
                mainBinding.thirdLayout.visibility = View.GONE
            }
            2 -> {
                mainBinding.firstLayout.visibility = View.GONE
                mainBinding.secondLayout.visibility = View.VISIBLE
                mainBinding.thirdLayout.visibility = View.GONE
            }
            3 -> {
                mainBinding.firstLayout.visibility = View.GONE
                mainBinding.secondLayout.visibility = View.GONE
                mainBinding.thirdLayout.visibility = View.VISIBLE
            }
        }

    }

    private fun showAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TransitionManager.beginDelayedTransition(mainBinding.mainRoot)
        }
    }

    private fun gotoNextScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}