package com.example.fontsspace.reAdapter


import android.content.Context
import android.graphics.Typeface
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.fontsspace.R
import com.example.fontsspace.billing.GBilling
import com.example.fontsspace.callBack.FontAdapterCallBack
import com.example.fontsspace.other.Utils
import java.lang.Exception

class MonoAdapter(
    private var mFontList: ArrayList<String>,
    private val mCallBack: FontAdapterCallBack
) : RecyclerView.Adapter<MonoAdapter.ViewHolder>() {

    lateinit var mcontext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.re_mono_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.textFont.typeface =
                Typeface.createFromAsset(mcontext.assets, "mono/" + mFontList[position])
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if (position > 2) {
            if (GBilling.isSubscribedOrPurchasedSaved) {
                Log.e("mybp", "buy pro")
                holder.thumbnailShow.setImageResource(R.drawable.next_icon)
            } else {
                Log.e("mybp", "not buy pro")
                holder.thumbnailShow.setImageResource(R.drawable.pro_icon)
            }
        } else {
            holder.thumbnailShow.setImageResource(R.drawable.next_icon)
        }

    }

    override fun getItemCount(): Int {
        return mFontList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textFont: TextView
        var thumbnailShow: ImageView

        init {
            thumbnailShow = itemView.findViewById(R.id.nextIcon)
            textFont = itemView.findViewById(R.id.monoText)
            itemView.setOnClickListener {
                mCallBack.setFont("${mFontList[adapterPosition].toString()}",adapterPosition)
            }
        }
    }

    // method for filtering our recyclerview items.
    fun filterList(filterList: ArrayList<String>) {
        // below line is to add our filtered
        // list in our course array list.
        mFontList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

}