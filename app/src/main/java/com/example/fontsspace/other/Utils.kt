package com.example.fontsspace.other

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.File
import java.util.ArrayList

object Utils {

    var folderFont = true
    var fontName = ""

   fun getFontFolder(status:Boolean):String{
       if (status){
           return "font"
       }else{
           return "mono"
       }
   }

    val readPermissionPass = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    const val GALLERY_IMAGE = 20202

    val inAppKeyArray: ArrayList<String> =
        arrayListOf("life_time")

    val subscriptionsKeyArray: ArrayList<String> = arrayListOf("weekly_plan","monthly_plan", "yearly_plan")

    const val inAppPurchasedkey: String = "life_time"

    const val inAppWeekly = "weekly_plan"

    const val inAppMonthly = "monthly_plan"

    const val inAppYearly = "yearly_plan"

    var feedBackDetails: String = "Report a Bug"

    var walkThrough: String = "ShowOrNot"

    @Suppress("DEPRECATION")
    @JvmField
    val BASE_LOCAL_PATH =
        "${Environment.getExternalStorageDirectory().absolutePath}/Download/FontSpace/"

    val Base_External_Save = "${Environment.DIRECTORY_DOWNLOADS}/FontSpace"

    @JvmStatic
    fun getRootPath(context: Context, internalDrir: Boolean): String {

        val root = if (internalDrir) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.getExternalFilesDir("FontSpace")?.absolutePath + "/"
            } else {
                BASE_LOCAL_PATH
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Base_External_Save
            } else {
                BASE_LOCAL_PATH
            }
        }

        val dirDest = File(root)

        if (!dirDest.exists()) {
            dirDest.mkdirs()
        }

        return root
    }

    //**************************This method hide keyboard*******************************************//
    fun hideKeyboardFromView(view: View) {
        val imm =
            (view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showToast(c: Context, message: String) {
        try {
            if (!(c as Activity).isFinishing) {
                c.runOnUiThread { //show your Toast here..
                    Toast.makeText(c.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

}