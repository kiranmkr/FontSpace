package com.example.fontsspace.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.android.billingclient.api.Purchase
import com.example.fontsspace.R
import com.example.fontsspace.billing.GBilling
import com.example.fontsspace.callBack.PopularClickListener
import com.example.fontsspace.dataModel.RecyclerItemsModel
import com.example.fontsspace.databinding.ActivitySettingScreenBinding
import com.example.fontsspace.other.FeedbackUtils
import com.example.fontsspace.other.Utils
import com.example.fontsspace.reAdapter.BottomMenuAdapter
import java.lang.Exception

class SettingScreen : AppCompatActivity(), PopularClickListener {

    private lateinit var mainBinding: ActivitySettingScreenBinding
    private var listItems: ArrayList<RecyclerItemsModel> = ArrayList()
    private var newAdapter: BottomMenuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivitySettingScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        updateBillingData()

        updateUi()
    }


    private fun updateBillingData() {

        if (GBilling.getConnectionStatus()) {
            Log.d("myBillingConnection", "Billing is connection")
        } else {
            Log.d("myBillingConnection", "billing Is  not connection")
        }

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
            Utils.inAppKeyArray, this
        ) {
            if (it) {
                Log.d("myBilling", "Billing is buy")
                proUser()
            } else {
                Log.d("myBilling", "Billing  is not  buy")
            }
        }


    }

    private fun proUser() {

        if (GBilling.isSubscribedOrPurchasedSaved) {
            Log.d("myBilling", "billing is buy")
        } else {
            Log.d("myBilling", "billing is not  buy")
            listItems.add(RecyclerItemsModel(R.drawable.pro_screen_icon, "Go Premium", "premium"))
        }
//        listItems.add(RecyclerItemsModel(R.drawable.restore_icon, "Restore Purchase", "purchase"))
        listItems.add(RecyclerItemsModel(R.drawable.bug_icon, "Report a Bug", "bug"))
        listItems.add(RecyclerItemsModel(R.drawable.feature_icon, "Request a Feature", "feature"))
        listItems.add(RecyclerItemsModel(R.drawable.policy_icon, "Privacy policy", "policy"))
        listItems.add(RecyclerItemsModel(R.drawable.terms_icon, "Terms of Service", "service"))
        listItems.add(RecyclerItemsModel(R.drawable.rate_us_icon, "Rate this App", "rate"))
        listItems.add(RecyclerItemsModel(R.drawable.share_icon, "Share", "share"))
        listItems.add(RecyclerItemsModel(R.drawable.other_app_icon, "Other Apps", "other_apps"))

        Log.d("myListSize", "${listItems.size}")

        newAdapter?.upDateCallBack(this)
        newAdapter?.upIconList(listItems)
    }

    private fun updateUi() {

        if (GBilling.isSubscribedOrPurchasedSaved) {
            Log.d("myBilling", "billing is buy")
        } else {
            Log.d("myBilling", "billing is not  buy")
            listItems.add(RecyclerItemsModel(R.drawable.pro_screen_icon, "Go Premium", "premium"))
        }
        listItems.add(RecyclerItemsModel(R.drawable.bug_icon, "Report a Bug", "bug"))
        listItems.add(RecyclerItemsModel(R.drawable.feature_icon, "Request a Feature", "feature"))
        listItems.add(RecyclerItemsModel(R.drawable.policy_icon, "Privacy policy", "policy"))
        listItems.add(RecyclerItemsModel(R.drawable.terms_icon, "Terms of Service", "service"))
        listItems.add(RecyclerItemsModel(R.drawable.rate_us_icon, "Rate this App", "rate"))
        listItems.add(RecyclerItemsModel(R.drawable.share_icon, "Share", "share"))
        listItems.add(RecyclerItemsModel(R.drawable.other_app_icon, "Other Apps", "other_apps"))

        Log.d("myListSize", "${listItems.size}")

        newAdapter = BottomMenuAdapter(listItems)
        newAdapter?.upDateCallBack(this)

        mainBinding.reMain.setHasFixedSize(true)
        mainBinding.reMain.adapter = newAdapter

        mainBinding.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onPopularClick(position: String) {
        Log.d("myCallBack", position)

        when (position) {
            "premium" -> {
                if (GBilling.isSubscribedOrPurchasedSaved) {
                    Utils.showToast(this, getString(R.string.already_subscribed))
                    Log.d("myBilling", "billing is buy")
                } else {
                    val intenet = Intent(this, ProScreen::class.java)
                    startActivity(intenet)
                }
            }
            "purchase" -> {
                Log.d("restore", "This is log to restore purchase")
            }
            "bug" -> {
                Utils.feedBackDetails = "Report a Bug"
                FeedbackUtils.startFeedbackEmail(this@SettingScreen)
            }
            "feature" -> {
                Utils.feedBackDetails = "Request a Feature"
                FeedbackUtils.startFeedbackEmail(this@SettingScreen)
            }
            "policy" -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://pdfmergeandsplit.blogspot.com/p/privacy-policy.html")
                        )
                    )
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            }
            "service" -> {

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://pdfmergeandsplit.blogspot.com/p/privacy-policy.html")
                        )
                    )
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }

            }
            "rate" -> {
                try {

                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri
                                .parse("market://details?id=$packageName")
                        )
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            "share" -> {
                try {

                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    var sAux = "\nLet me recommend you this application\n\n"
                    sAux = """
                    ${sAux}https://play.google.com/store/apps/details?id=$packageName
                    """.trimIndent()
                    i.putExtra(Intent.EXTRA_TEXT, sAux)

                    startActivity(
                        Intent.createChooser(
                            i,
                            resources.getString(R.string.choose_one)
                        )
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


            }
            "other_apps" -> {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri
                                .parse("https://play.google.com/store/apps/developer?id=EE+Applications")
                        )
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
            else -> {
                Log.d("myPopularClick", "not Match above of this")
            }
        }

    }
}