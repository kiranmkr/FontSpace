package com.example.fontsspace.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.example.fontsspace.R
import com.example.fontsspace.billing.GBilling
import com.example.fontsspace.databinding.ActivityProScreenBinding
import com.example.fontsspace.other.Utils
import java.util.ArrayList

class ProScreen : AppCompatActivity() {

    private var selectPrices: Int = 3

    private lateinit var mainBinding: ActivityProScreenBinding

    private var bgLayoutButtonBar: ArrayList<ConstraintLayout> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityProScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        updateUi()

        updateBillingData()
    }


    private fun updateUi() {

        bgLayoutButtonBar.add(mainBinding.consWeekly)
        bgLayoutButtonBar.add(mainBinding.consMonthly)
        bgLayoutButtonBar.add(mainBinding.consYearly)
        bgLayoutButtonBar.add(mainBinding.consLifeTime)

        mainBinding.consWeekly.setOnClickListener {
            alphaManager(bgLayoutButtonBar, it.id)
            selectPrices = 1
            updateSelection(selectPrices)
        }

        mainBinding.consMonthly.setOnClickListener {
            alphaManager(bgLayoutButtonBar, it.id)
            selectPrices = 2
            updateSelection(selectPrices)
        }

        mainBinding.consYearly.setOnClickListener {
            alphaManager(bgLayoutButtonBar, it.id)
            selectPrices = 3
            updateSelection(selectPrices)
        }

        mainBinding.consLifeTime.setOnClickListener {
            alphaManager(bgLayoutButtonBar, it.id)
            selectPrices = 4
            updateSelection(selectPrices)
        }

        mainBinding.btnBack.setOnClickListener {
            finish()
        }

        mainBinding.btnBuy.setOnClickListener {
            when (selectPrices) {
                1 -> {
                    if (GBilling.getConnectionStatus() && isNetworkAvailable()) {
                        GBilling.subscribe(this@ProScreen, Utils.inAppWeekly)
                    } else {
                        Utils.showToast(this, getString(R.string.internet_not_connected))
                    }
                }
                2 -> {
                    if (GBilling.getConnectionStatus() && isNetworkAvailable()) {
                        GBilling.subscribe(this@ProScreen, Utils.inAppMonthly)
                    } else {
                        Utils.showToast(this, getString(R.string.internet_not_connected))

                    }
                }
                3 -> {
                    if (GBilling.getConnectionStatus() && isNetworkAvailable()) {
                        GBilling.subscribe(this@ProScreen, Utils.inAppYearly)
                    } else {
                        Utils.showToast(this, getString(R.string.internet_not_connected))
                    }
                }
                4 -> {
                    if (GBilling.getConnectionStatus() && isNetworkAvailable()) {
                        GBilling.purchase(this@ProScreen, Utils.inAppPurchasedkey)
                    } else {
                        Utils.showToast(this, getString(R.string.internet_not_connected))
                    }
                }
                else -> {
                    Log.d("myBuy", "Select any Plan")
                }

            }
        }

        updateSelection(selectPrices)
        mainBinding.consYearly.setBackgroundResource(R.drawable.pro_selection_fil)
        mainBinding.tvGolden.setTextColor(ContextCompat.getColor(this, R.color.white))
        mainBinding.tvYearly.setTextColor(ContextCompat.getColor(this, R.color.white))
        mainBinding.tvPerYear.setTextColor(ContextCompat.getColor(this, R.color.white))

    }

    private fun updateSelection(position: Int) {
        showAnimation()
        when (position) {
            1 -> {
                mainBinding.imgWeekly.isSelected = true
                mainBinding.imgMonthly.isSelected = false
                mainBinding.imgYearly.isSelected = false
                mainBinding.imgLifeTime.isSelected = false

                mainBinding.tvLite.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvBasic.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvGolden.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvDiamond.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvWeekly.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvYearly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvPerWeekly.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvPerMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerYear.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvBuyText.text = getString(R.string.subscription_start)
                mainBinding.tvTrial.visibility = View.GONE

            }
            2 -> {
                mainBinding.imgWeekly.isSelected = false
                mainBinding.imgMonthly.isSelected = true
                mainBinding.imgYearly.isSelected = false
                mainBinding.imgLifeTime.isSelected = false

                mainBinding.tvLite.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvBasic.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvGolden.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvDiamond.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvWeekly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvMonthly.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvYearly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvPerWeekly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerMonthly.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvPerYear.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvBuyText.text = getString(R.string.subscription_start)
                mainBinding.tvTrial.visibility = View.GONE
            }
            3 -> {
                mainBinding.imgWeekly.isSelected = false
                mainBinding.imgMonthly.isSelected = false
                mainBinding.imgYearly.isSelected = true
                mainBinding.imgLifeTime.isSelected = false

                mainBinding.tvLite.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvBasic.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvGolden.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvDiamond.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvWeekly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvYearly.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvPerWeekly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerYear.setTextColor(ContextCompat.getColor(this, R.color.white))
                mainBinding.tvPerLifeTime.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )

                mainBinding.tvBuyText.text = getString(R.string.subscription_start)
                mainBinding.tvTrial.visibility = View.VISIBLE
            }
            4 -> {
                mainBinding.imgWeekly.isSelected = false
                mainBinding.imgMonthly.isSelected = false
                mainBinding.imgYearly.isSelected = false
                mainBinding.imgLifeTime.isSelected = true

                mainBinding.tvLite.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvBasic.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvGolden.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvDiamond.setTextColor(ContextCompat.getColor(this, R.color.white))

                mainBinding.tvWeekly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvYearly.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                mainBinding.tvLifeTime.setTextColor(ContextCompat.getColor(this, R.color.white))

                mainBinding.tvPerWeekly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerMonthly.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerYear.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorAccent
                    )
                )
                mainBinding.tvPerLifeTime.setTextColor(ContextCompat.getColor(this, R.color.white))

                mainBinding.tvBuyText.text = getString(R.string.purchase_start)
                mainBinding.tvTrial.visibility = View.GONE
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    private fun alphaManager(views: ArrayList<ConstraintLayout>, view_id: Int) {
        for (i in views.indices) {
            if (views[i].id == view_id) {
                views[i].setBackgroundResource(R.drawable.pro_selection_fil)
            } else {
                views[i].setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    private fun updateBillingData() {

        if (GBilling.getConnectionStatus()) {

            if (GBilling.isSubscribedOrPurchasedSaved) {

                mainBinding.tvBuyText.text = getString(R.string.already_subscribed)
                mainBinding.btnBuy.isClickable = false

                mainBinding.tvWeekly.text = "------"
                mainBinding.tvMonthly.text = "------"
                mainBinding.tvYearly.text = "------"
                mainBinding.tvLifeTime.text = "------"

            } else {

                if (!(isDestroyed || isFinishing)) {

                    val inAppString = ArrayList<String>()
                    inAppString.add(Utils.inAppPurchasedkey)

                    GBilling.getInAppSkuDetails(
                        inAppString,
                        false,
                        this,
                        object : Observer<List<SkuDetails>> {
                            override fun onChanged(t: List<SkuDetails>?) {
                                t?.let {
                                    if (it.isNotEmpty()) {
                                        Log.d("error", it[0].price)
                                        mainBinding.tvLifeTime.text = it[0].price

                                    } else {
                                        mainBinding.tvLifeTime.text = "------"
                                    }
                                }
                            }
                        })


                    val inAppWeekly = ArrayList<String>()
                    inAppWeekly.add(Utils.inAppWeekly)

                    GBilling.getSubscriptionsSkuDetails(
                        inAppWeekly,
                        false,
                        this,
                        object : Observer<List<SkuDetails>> {
                            override fun onChanged(t: List<SkuDetails>?) {
                                t?.let {
                                    if (it.isNotEmpty()) {
                                        Log.d("error", it[0].price)
                                        mainBinding.tvWeekly.text = it[0].price

                                    } else {
                                        mainBinding.tvWeekly.text = "------"
                                    }
                                }
                            }
                        })


                    val inAppMonthly = ArrayList<String>()
                    inAppMonthly.add(Utils.inAppMonthly)

                    GBilling.getSubscriptionsSkuDetails(
                        inAppMonthly,
                        false,
                        this,
                        object : Observer<List<SkuDetails>> {
                            override fun onChanged(t: List<SkuDetails>?) {
                                t?.let {
                                    if (it.isNotEmpty()) {
                                        Log.d("error", it[0].price)
                                        mainBinding.tvMonthly.text = it[0].price

                                    } else {
                                        mainBinding.tvMonthly.text = "------"
                                    }
                                }
                            }
                        })

                    val inAppYearly = ArrayList<String>()
                    inAppYearly.add(Utils.inAppYearly)

                    GBilling.getSubscriptionsSkuDetails(
                        inAppYearly,
                        false,
                        this,
                        object : Observer<List<SkuDetails>> {
                            override fun onChanged(t: List<SkuDetails>?) {
                                t?.let {
                                    if (it.isNotEmpty()) {
                                        Log.d("error", it[0].price)
                                        mainBinding.tvYearly.text = it[0].price
                                        mainBinding.tvTrial.text =
                                            "${getString(R.string._3_days_trial)} ${getString(R.string.then)} ${it[0].price}${
                                                getString(R.string.year_after)
                                            }"
                                        Log.d(
                                            "myPrices",
                                            "${getString(R.string.then)} ${it[0].price}${getString(R.string.year_after)}"
                                        )
                                    } else {
                                        mainBinding.tvYearly.text = "------"
                                    }
                                }
                            }
                        })
                }
            }
        }

        GBilling.setOnPurchasedObserver(this,
            object : Observer<Purchase> {
                override fun onChanged(t: Purchase?) {
                    if (t != null) {
                        if (GBilling.isSubscribedOrPurchasedSaved) {
                            finish()
                        }
                    }
                }
            })

        GBilling.setOnErrorObserver(this,
            object : Observer<Int> {
                override fun onChanged(t: Int?) {
                    if (t != null) {
                        Log.d("myBilling", "${t}")
                    }
                }
            })

    }


    private fun showAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TransitionManager.beginDelayedTransition(mainBinding.mainRoot)
        }
    }

}