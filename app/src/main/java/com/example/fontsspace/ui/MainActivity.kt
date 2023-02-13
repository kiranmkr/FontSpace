package com.example.fontsspace.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.android.billingclient.api.Purchase
import com.example.fontsspace.R
import com.example.fontsspace.billing.GBilling
import com.example.fontsspace.callBack.FontAdapterCallBack
import com.example.fontsspace.databinding.ActivityMainBinding
import com.example.fontsspace.other.Utils
import com.example.fontsspace.reAdapter.FontAdapter
import com.example.fontsspace.reAdapter.MonoAdapter
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), FontAdapterCallBack {

    private lateinit var mainBinding: ActivityMainBinding

    //ArrayList var
    private var fontNames: ArrayList<String>? = null
    private var monoNames: ArrayList<String>? = null
    private var fontAdapter: FontAdapter? = null
    private var monoAdapter: MonoAdapter? = null

    private var workerHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        updateBillingData()

        updateUi()

        updateUiClicks()

    }

    private fun updateBillingData() {

        GBilling.setOnPurchasedObserver(this,
            object : Observer<Purchase> {
                override fun onChanged(t: Purchase?) {
                    if (t != null) {
                        if (GBilling.isSubscribedOrPurchasedSaved) {
                            proUser()
                        }
                    }
                }
            })

        GBilling.isSubscribedOrPurchased(
            Utils.subscriptionsKeyArray,
            Utils.inAppKeyArray,
            this,
            object : Observer<Boolean> {
                override fun onChanged(t: Boolean?) {
                    if (t != null) {
                        if (t) {
                            Log.d("myBilling", "Billing is buy")
                            proUser()
                        } else {
                            Log.d("myBilling", "Billing  is not  buy")
                            startActivity(Intent(this@MainActivity, ProScreen::class.java))
                        }
                    }
                }

            }
        )


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun proUser() {
        fontAdapter?.notifyDataSetChanged()
        monoAdapter?.notifyDataSetChanged()
        mainBinding.goPro.visibility = View.GONE
    }

    private fun updateUi() {

        mainBinding.fontCard.setCardBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.selectionColor
            )
        )

        mainBinding.imgFont.isSelected = true
        mainBinding.tvImageFont.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.textSelection
            )
        )

        try {

            fontNames = assets.list("font")?.toCollection(ArrayList())

            if (fontNames != null) {

                Log.d("myFont", "${fontNames?.size}")

                mainBinding.reFont.setHasFixedSize(true)
                fontAdapter = FontAdapter(fontNames!!, this)
                mainBinding.reFont.adapter = fontAdapter
            } else {
                Log.d("myFont", "list is null")
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {

            monoNames = assets.list("mono")?.toCollection(ArrayList())

            if (monoNames != null) {

                Log.d("myMono", "${monoNames?.size}")

                mainBinding.reMono.setHasFixedSize(true)
                mainBinding.reMono.adapter = MonoAdapter(monoNames!!, this)

            } else {
                Log.d("myMono", "list is null")
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun updateUiClicks() {

        mainBinding.btnMenu.setOnClickListener {
            val intenet = Intent(this, SettingScreen::class.java)
            startActivity(intenet)
        }

        mainBinding.closeSearch.setOnClickListener {

            if (fontNames != null) {
                fontAdapter?.filterList(fontNames!!)
            }

            if (monoNames != null) {
                monoAdapter?.filterList(monoNames!!)
            }

            mainBinding.edtSearchText.clearFocus()
            mainBinding.edtSearchText.text.clear()

            hideKeyboardFromView(it)
        }

        mainBinding.fontCard.setOnClickListener {

            showAnimation()

            mainBinding.fontCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.selectionColor
                )
            )

            mainBinding.imgFont.isSelected = true
            mainBinding.tvImageFont.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textSelection
                )
            )

            mainBinding.monoCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.shapeColor
                )
            )

            mainBinding.imgMono.isSelected = false
            mainBinding.tvMono.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textColor
                )
            )

            mainBinding.reMono.visibility = View.GONE
            mainBinding.reFont.visibility = View.VISIBLE

            if (fontNames != null) {
                fontAdapter?.filterList(fontNames!!)
            }
        }

        mainBinding.monoCard.setOnClickListener {

            showAnimation()

            mainBinding.fontCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.shapeColor
                )
            )

            mainBinding.imgFont.isSelected = false
            mainBinding.tvImageFont.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textColor
                )
            )

            mainBinding.monoCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.selectionColor
                )
            )

            mainBinding.imgMono.isSelected = true
            mainBinding.tvMono.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textSelection
                )
            )

            mainBinding.reMono.visibility = View.VISIBLE
            mainBinding.reFont.visibility = View.GONE

            if (monoNames != null) {
                monoAdapter?.filterList(monoNames!!)
            }
        }

        mainBinding.edtSearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Log.e("calling", "beforeTextChanged");
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Log.e("calling", "onTextChanged");
            }

            override fun afterTextChanged(s: Editable) {
                Log.e("calling", "afterTextChanged $s")
                fontFilter(s.toString())
            }
        })

        mainBinding.btnHome.setOnClickListener {
            Utils.showToast(this, "Already Home Screen")
        }

        mainBinding.btnSetting.setOnClickListener {
            val intenet = Intent(this, SettingScreen::class.java)
            startActivity(intenet)
        }

        mainBinding.btnLike.setOnClickListener {
            Utils.showToast(this, "calling like btn")
        }

        mainBinding.goPro.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProScreen::class.java))
        }

    }

    private fun showAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TransitionManager.beginDelayedTransition(mainBinding.mainRoot)
        }
    }

    override fun setFont(fontPath: String, position: Int) {

        if (mainBinding.reFont.visibility == View.VISIBLE) {
            Log.d("myFontName", "font ${fontPath}")
            Utils.folderFont = true
        } else {
            Log.d("myFontName", "mono ${fontPath}")
            Utils.folderFont = false
        }

        Log.d("myFontName", "${position}")

        Utils.fontName = fontPath

        if (position > 2) {
            if (GBilling.isSubscribedOrPurchasedSaved) {
                Log.e("mybp", "buy pro")
                startActivity(Intent(this, NewEditingScreen::class.java))
            } else {
                Log.e("mybp", "not buy pro")
                startActivity(Intent(this, ProScreen::class.java))
            }
        } else {

            startActivity(Intent(this, NewEditingScreen::class.java))
        }

    }

    private fun fontFilter(text: String) {

        if (mainBinding.reFont.visibility == View.VISIBLE) {
            Log.d("mySearch", "Font Search")
            if (fontNames != null) {

                // creating a new array list to filter our data.
                val filterers: ArrayList<String> = ArrayList()

                // running a for loop to compare elements.
                for (item in fontNames!!) {
                    // checking if the entered string matched with any item of our recycler view.
                    if (item.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        filterers.add(item)
                    }
                }

                if (filterers.isEmpty()) {
                    // if no item is added in filtered list we are
                    // displaying a toast message as no data found.
                    fontAdapter?.filterList(fontNames!!)
                } else {
                    // at last we are passing that filtered
                    // list to our adapter class.
                    fontAdapter?.filterList(filterers)
                }
            }
        } else {
            Log.d("mySearch", "Mono Search")

            if (monoNames != null) {

                // creating a new array list to filter our data.
                val filterers: ArrayList<String> = ArrayList()

                // running a for loop to compare elements.
                for (item in monoNames!!) {
                    // checking if the entered string matched with any item of our recycler view.
                    if (item.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        filterers.add(item)
                    }
                }

                if (filterers.isEmpty()) {
                    // if no item is added in filtered list we are
                    // displaying a toast message as no data found.
                    monoAdapter?.filterList(monoNames!!)
                } else {
                    // at last we are passing that filtered
                    // list to our adapter class.
                    monoAdapter?.filterList(filterers)
                }
            }
        }

    }

    //**************************This method hide keyboard*******************************************//
    private fun hideKeyboardFromView(view: View) {
        val imm =
            (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}