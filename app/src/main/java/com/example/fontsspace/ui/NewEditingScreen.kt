package com.example.fontsspace.ui


import android.content.ContentValues
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.fontsspace.R
import com.example.fontsspace.billing.GBilling
import com.example.fontsspace.databinding.ActivityNewEditingScreenBinding
import com.example.fontsspace.databinding.DilogSvgLoaderBinding
import com.example.fontsspace.other.Utils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import ru.santaev.outlinespan.OutlineSpan
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NewEditingScreen : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var mainBinding: ActivityNewEditingScreenBinding

    var deafultColor = -0x1000000

    var deafultText = "Font Applied Text"

    var deafualtSelect = true

    private var workerHandler = Handler(Looper.getMainLooper())
    private var workerThread: ExecutorService = Executors.newCachedThreadPool()
    private var dialogBinding: DilogSvgLoaderBinding? = null

    private var bgTextFormat: ArrayList<ConstraintLayout> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityNewEditingScreenBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        dialogBinding = DilogSvgLoaderBinding.bind(mainBinding.dialogLoader.root)

        Log.d("myFontName", "${Utils.folderFont} -- ${Utils.fontName}")

        updateBillingData()

        workerHandler.post {
            updateUi()
            updateClick()
        }

    }

    private fun updateBillingData() {

        if (GBilling.getConnectionStatus()) {
            Log.d("myBillingConnection", "Billing is connection")
        } else {
            Log.d("myBillingConnection", "billing Is  not connection")
        }

        GBilling.isSubscribedOrPurchased(Utils.subscriptionsKeyArray,
            Utils.inAppKeyArray,
            this,
            object : Observer<Boolean> {
                override fun onChanged(t: Boolean?) {

                    t?.let {

                        if (t) {
                            proUser()
                        } else {
                            Log.d("updateBillingData", "billing is not buy")
                        }

                    }
                }

            })

    }

    private fun proUser() {

    }

    private fun updateUi() {

        mainBinding.tvFontName.text = Utils.fontName

        bgTextFormat.add(mainBinding.defaultCard)
        bgTextFormat.add(mainBinding.upperCard)
        bgTextFormat.add(mainBinding.lowerCard)

        if (Utils.folderFont) {
            try {
                mainBinding.tvFontText.typeface =
                    Typeface.createFromAsset(this.assets, "font/" + "${Utils.fontName}")
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("myFontError", "font not Create")
            }
        } else {
            try {
                mainBinding.tvFontText.typeface =
                    Typeface.createFromAsset(this.assets, "mono/" + "${Utils.fontName}")
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("myFontError", "font not Create")
            }
        }

        mainBinding.tvFontText.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            (50).toFloat()
        )

        Log.d("myTextSize", mainBinding.tvFontText.textSize.toString())

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

    private fun updateClick() {

        mainBinding.btnBack2.setOnClickListener {
            finish()
        }


        mainBinding.defaultCard.setOnClickListener {

            alphaManager(bgTextFormat,it.id)
            mainBinding.tvDefault.setTextColor(ContextCompat.getColor(this, R.color.white))
            mainBinding.tvUper.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvLower.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvFontText.text = deafultText
        }

        mainBinding.upperCard.setOnClickListener {
            alphaManager(bgTextFormat,it.id)
            mainBinding.tvDefault.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvUper.setTextColor(ContextCompat.getColor(this, R.color.white))
            mainBinding.tvLower.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvFontText.let {
                it.text = it.text.toString().uppercase(Locale.ROOT)
            }

        }

        mainBinding.lowerCard.setOnClickListener {
            alphaManager(bgTextFormat,it.id)
            mainBinding.tvDefault.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvUper.setTextColor(ContextCompat.getColor(this, R.color.black))
            mainBinding.tvLower.setTextColor(ContextCompat.getColor(this, R.color.white))
            mainBinding.tvFontText.let {
                it.text = it.text.toString().lowercase(Locale.ROOT)
            }

        }

        dialogBinding?.root?.setOnClickListener {
            Log.d("myDialog", "Empty Click")
        }

        mainBinding.installFont.setOnClickListener {

            Log.d("myFontFolder","${Utils.getFontFolder(Utils.folderFont)}")

            if (EasyPermissions.hasPermissions(
                    this@NewEditingScreen, *Utils.readPermissionPass
                )
            ) {
                if (!this@NewEditingScreen.isFinishing) {
                    dialogBinding?.root?.visibility = View.VISIBLE
                }
                workerThread.execute {

                    val saveFiel = saveMediaToStorage()

                    workerHandler.postDelayed({

                        if (!this@NewEditingScreen.isFinishing) {
                            dialogBinding?.root?.visibility = View.GONE
                        }

                        Utils.showToast(this, "Fiel is save ${saveFiel}")
                    }, 500)

                }
            } else {
                EasyPermissions.requestPermissions(
                    this@NewEditingScreen, "Please allow permissions to proceed further",
                    Utils.GALLERY_IMAGE, *Utils.readPermissionPass
                )
            }

        }

        mainBinding.textView5.setOnClickListener {
            Utils.showToast(this, "Click to Install Font and Use it...!")
        }

        mainBinding.conCut.setOnClickListener {

            deafualtSelect = true

            mainBinding.conCut.setBackgroundResource(R.drawable.bg_selection)
            mainBinding.conDraw.setBackgroundResource(android.R.color.transparent)
            mainBinding.textView9.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )

            mainBinding.textView10.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )

            mainBinding.tvFontText.text = deafultText

        }

        mainBinding.conDraw.setOnClickListener {

            deafualtSelect = false

            mainBinding.conCut.setBackgroundResource(android.R.color.transparent)
            mainBinding.conDraw.setBackgroundResource(R.drawable.bg_selection)

            mainBinding.textView9.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )

            mainBinding.textView10.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )

            applyOutLine()

        }

        mainBinding.cardView10.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color1
            )

            applyOutLine()
        }
        mainBinding.cardView9.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color2
            )

            applyOutLine()
        }
        mainBinding.cardView11.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color3
            )

            applyOutLine()
        }
        mainBinding.cardView12.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color4
            )

            applyOutLine()
        }
        mainBinding.cardView13.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color5
            )

            applyOutLine()
        }
        mainBinding.cardView14.setOnClickListener {
            deafultColor = ContextCompat.getColor(
                this,
                R.color.color6
            )

            applyOutLine()
        }


        mainBinding.seekBar.max = 100
        mainBinding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {

                    mainBinding.tvSeekSize.text = "${progress}"

                    if (progress > 10) {
                        changeFontSize(progress, mainBinding.tvFontText)
                    }

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun changeFontSize(fontSize: Int, currentEditText: android.widget.TextView) {

        val oldW: Int = currentEditText.width
        val oldH: Int = currentEditText.height

        Log.e("changeFontSize", "OLD= $oldW, $oldH, ${currentEditText.x}, ${currentEditText.y}")

        currentEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize.toFloat())

        val newW: Float = currentEditText.width.toFloat()
        val factor = newW / oldW

        val cmDist: Float = (currentEditText.cameraDistance * factor)

        if (cmDist > 0) {
            try {
                currentEditText.cameraDistance = cmDist
            } catch (ex: IllegalArgumentException) {
            }
        }

    }

    fun applyOutLine() {

        if (deafualtSelect) {
            mainBinding.tvFontText.setTextColor(deafultColor)
        } else {

            mainBinding.tvFontText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textColor
                )
            )

            val outlineSpan = OutlineSpan(
                strokeColor = deafultColor,
                strokeWidth = 10F
            )

            val text = deafultText
            val spannable = SpannableString(text)
            spannable.setSpan(outlineSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            mainBinding.tvFontText.text = spannable
        }

    }

    fun fileChecker(path: String?): Boolean {
        return if (path != null) {
            val file = File(path)
            file.exists()
        } else {
            false
        }
    }

    fun saveMediaToStorage(): String? {

        var filePath: String? = null

        //Generating a file name
        val filename = Utils.fontName

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            applicationContext?.contentResolver?.also { resolver ->

                val dirDest = File(Utils.getRootPath(this, false),Utils.getFontFolder(Utils.folderFont))

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {
                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "$dirDest")
                }

                //MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                //Inserting the contentValues to contentResolver and getting the Uri
                // val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                val imageUri: Uri? = resolver.insert(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                    contentValues
                )

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }

                filePath = dirDest.toString()

            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val dirDest = File(Utils.getRootPath(this, false), "${Utils.getFontFolder(Utils.folderFont)}")
            if (!dirDest.exists()) {
                dirDest.mkdirs()
            }
            val image = File(dirDest, filename)

            Log.e("myFilePath", "$image")

            fos = FileOutputStream(image)

            filePath = image.toString()

        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            val isPut: InputStream = assets.open("${Utils.getFontFolder(Utils.folderFont)}/${Utils.fontName}")

            it.write(isPut.readBytes())

            it.flush()
            it.close()

            Log.e("myFileFos", "Saved to Photos Main")

        }

        return filePath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        if (perms.size == Utils.readPermissionPass.size) {

            if (!this@NewEditingScreen.isFinishing) {
                dialogBinding?.root?.visibility = View.VISIBLE
            }

            val saveFiel = saveMediaToStorage()

            workerHandler.postDelayed({

                if (!this@NewEditingScreen.isFinishing) {
                    dialogBinding?.root?.visibility = View.GONE
                }

                Utils.showToast(this, "Fiel is save ${saveFiel}")
            }, 500)


        } else {
            Utils.showToast(this, "Allow full Permsision")
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this@NewEditingScreen, perms)) {
            AppSettingsDialog.Builder(this@NewEditingScreen).build().show()
        }
    }

}