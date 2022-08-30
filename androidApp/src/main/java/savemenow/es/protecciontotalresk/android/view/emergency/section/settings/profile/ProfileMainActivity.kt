package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl


class ProfileMainActivity : AppCompatActivity() , Contract.IProfileMain
{
    //Views
    private lateinit var toolbarProfileMain : Toolbar
    private lateinit var  bottomNavMainMenu: BottomNavigationView
    private lateinit var tvFullNameProfile : TextView
    private lateinit var tvEmailProfile : TextView
    private lateinit var imgAccesType : ImageView
    private lateinit var imgProfile : CircleImageView
    private lateinit var imgQR : ImageView
    //Objects
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private lateinit var bitmapPath : String
    private lateinit var bitmpaUri : Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_main)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()

        //Init
        initViews()
    }

    override fun initViews()
    {
        toolbarProfileMain = findViewById(R.id.toolbarProfileMain)
        bottomNavMainMenu = findViewById(R.id.bottomNavMainMenu)
        tvFullNameProfile = findViewById(R.id.tvFullNameProfile)
        tvEmailProfile = findViewById(R.id.tvEmailProfile)
        imgAccesType = findViewById(R.id.imgAccesTypee)
        imgProfile = findViewById(R.id.imgProfile)
        imgQR = findViewById(R.id.imgQR)
        //Config Toolbar
        this.setSupportActionBar(toolbarProfileMain)
        this.supportActionBar!!.title = getString(R.string.text_my_profile)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //Get Data
        showInfo()
        onClickBottomNav()
    }

    fun generateQR(content: String?, size: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(
                content,
                BarcodeFormat.QR_CODE, size, size
            )
            bitmapPath = MediaStore.Images.Media.insertImage(contentResolver,bitmap,"scan_qr",null)
            bitmpaUri = Uri.parse(bitmapPath)
            Log.d("TAG","PATH URI QR CODE $bitmpaUri")

        } catch (e: WriterException) {
            Log.e("generateQR()",e.toString())
        }

        return bitmap
    }

    private fun createVcard(fullName: String, phoneNumber:String, email:String, address:String,
                            company:String): String {
        return """
            BEGIN:VCARD
            VERSION:4.0
            FN:$fullName
            ORG:$company
            TEL;TYPE=home,voice;VALUE=uri:tel:$phoneNumber
            EMAIL:$email
            END:VCARD
            """.trimIndent()
    }


    override fun onClickBottomNav()
    {
        bottomNavMainMenu.setOnItemReselectedListener {
            when(it.itemId)
            {
                R.id.menu_profile_edit ->
                {
                    val intent = Intent(this,ProfileEditActivity::class.java)
                    intent.putExtra("id",intent.getStringExtra("id"))
                    startActivity(intent)
                }

                R.id.menu_medical_info ->
                {
                    val intent = Intent(this,ProfileMednfoActivity::class.java)
                    intent.putExtra("id",intent.getStringExtra("id"))
                    startActivity(intent)
                }

                R.id.menu_share ->
                {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_STREAM, bitmpaUri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.type = "image/png"
                    startActivity(intent)
                }
            }
        }
    }

    override fun showInfo() {
        val currentUser = mAuth.currentUser
        val screenWidth = resources.displayMetrics.widthPixels - 600

        if (currentUser != null) {
            lifecycleScope.launch {
                tvFullNameProfile.text = mAuth.currentUser!!.displayName
                tvEmailProfile.text = mAuth.currentUser!!.email
                Glide.with(this@ProfileMainActivity).load(mAuth.currentUser!!.photoUrl).into(imgProfile)

                firebasePresenter.getQueryById(
                  currentUser.uid).get().addOnCompleteListener {
                    for (document in it.result)
                    {
                        imgQR.setImageBitmap(generateQR(
                           createVcard(fullName = document.getString("fullName").toString()
                               , phoneNumber =document.getString("phone").toString(),
                                email =document.getString("email").toString(),
                                address = document.getString("address").toString(),
                               company = document.getString("company").
                               toString()), size = screenWidth))
                    }

                }

            }

        }//If ednd
        else
        {
            imgProfile.setBackgroundResource(R.drawable.img_perfil_unknow)
            imgAccesType.visibility = View.INVISIBLE
            firebasePresenter.getQueryById(intent.getStringExtra("id").toString()).get()
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        for(document in it.result)
                        {
                            tvFullNameProfile.text = document["fullName"].toString()
                            tvEmailProfile.text = document["email"].toString()

                            imgQR.setImageBitmap(generateQR(
                                createVcard(fullName = document.getString("fullName").toString()
                                    , phoneNumber =document.getString("phone").toString(),
                                    email =document.getString("email").toString(),
                                    address = document.getString("address").toString(),
                                    company = document.getString("company").
                                    toString()), size = screenWidth))
                        }
                    }
                }
        }
        //Show QR
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}