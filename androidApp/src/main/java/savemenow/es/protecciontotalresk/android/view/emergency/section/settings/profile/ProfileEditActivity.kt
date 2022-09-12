package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl

class ProfileEditActivity : AppCompatActivity(), Contract.IProfileEdit
{
    //Views
    private lateinit var toolbarProfileEdit : Toolbar
    private lateinit var textInputEditTextFullName : TextInputEditText
    private lateinit var textInputEditTextPhone : TextInputEditText
    private lateinit var textInputEditTextAddress : TextInputEditText
    private lateinit var cardViewEditCountry : CardView
    //private lateinit var textInputEditTextCountry : TextInputEditText
    private lateinit var textInputEditTextOrg : TextInputEditText
    private lateinit var btn_save_edit : Button
    private lateinit var tvCountryNameEdit : TextView
    private lateinit var imgFlagCountryEdit : ImageView
    //Objects
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        //Init
        initViews()
    }

    override fun initViews()
    {
        toolbarProfileEdit = findViewById(R.id.toolbarProfileEdit)
        textInputEditTextFullName = findViewById(R.id.textInputEditTextFullName)
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone)
        textInputEditTextAddress = findViewById(R.id.textInputEditTextAddress)
        cardViewEditCountry = findViewById(R.id.cardViewEditCountry)
        tvCountryNameEdit = findViewById(R.id.tvCountryNameEdit)
        imgFlagCountryEdit = findViewById(R.id.imgFlagCountryEdit)
        textInputEditTextOrg = findViewById(R.id.textInputEditTextOrg)
        btn_save_edit = findViewById(R.id.btn_save_edit)
        //Config Toolbar
        this.setSupportActionBar(toolbarProfileEdit)
        this.supportActionBar!!.title = getString(R.string.text_edit)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //listeners
        btn_save_edit.setOnClickListener {updateProfile() }
        cardViewEditCountry.setOnClickListener { showDialogCountry() }

    }

    override fun onStart() {
        super.onStart()
        //Dowload Data
        showInfo()
    }

    override fun showInfo()
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            textInputEditTextFullName.setText(mAuth.currentUser!!.displayName.toString())

            firebasePresenter.getQueryById(mAuth.currentUser!!.uid).get()
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        for(document in it.result)
                        {
                            if(document["fullName"].toString().equals(null))
                            {
                                textInputEditTextFullName.setText("")
                            }else
                            {
                                textInputEditTextFullName.setText(document["fullName"].toString())
                            }
                            if(document["phone"].toString() == "null")
                            {
                                textInputEditTextPhone.setText("")
                            }else
                            {
                                textInputEditTextPhone.setText(document["phone"].toString())
                            }
                            if(document["address"].toString() == "null")
                            {
                                textInputEditTextAddress.setText("")
                            }else
                            {
                                textInputEditTextAddress.setText(document["address"].toString())
                            }
                            if(document["company"].toString() == "null")
                            {
                                textInputEditTextOrg.setText("")
                            }else
                            {
                                textInputEditTextOrg.setText(document["company"].toString())
                            }

                            if(intent.getBooleanExtra("edit_country",false))
                            {
                                if(intent.getStringExtra("selected_country_name") != null ||
                                    intent.getStringExtra("selected_flag_country") != null)
                                {
                                    tvCountryNameEdit.text =intent.getStringExtra("selected_country_name")
                                    Glide.with(this)
                                        .load(Uri.parse(intent.getStringExtra("selected_flag_country")))
                                        .into(imgFlagCountryEdit)
                                }
                            }else{

                                val countryGet : String =document["country"].toString()
                                val uriFlag : String = document["flagCountry"].toString()
                                if(countryGet == "null")
                                {
                                    tvCountryNameEdit.text = "No Seleccionado"
                                }else{
                                    //Log.d("TAG","Country DB $countryGet")
                                    tvCountryNameEdit.text = countryGet
                                }
                                if(uriFlag == "null")
                                {
                                    imgFlagCountryEdit.visibility = View.INVISIBLE

                                }else{
                                    imgFlagCountryEdit.visibility = View.VISIBLE
                                    Glide.with(this)
                                        .load(Uri.parse(uriFlag))
                                        .into(imgFlagCountryEdit)
                                }

                            }
                        }
                    }
                }
        }else
        {
            firebasePresenter.getQueryById(intent.getStringExtra(intent.getStringExtra("id"))
                .toString()).get()
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        for(document in it.result)
                        {
                            if(document["fullName"].toString().equals(null))
                            {
                                textInputEditTextFullName.setText("")
                            }else
                            {
                                textInputEditTextFullName.setText(document["fullName"].toString())
                            }
                            if(document["phone"].toString() == "null")
                            {
                                textInputEditTextPhone.setText("")
                            }else
                            {
                                textInputEditTextPhone.setText(document["phone"].toString())
                            }
                            if(document["address"].toString() == "null")
                            {
                                textInputEditTextAddress.setText("")
                            }else
                            {
                                textInputEditTextAddress.setText(document["address"].toString())
                            }
                            if(document["company"].toString() == "null")
                            {
                                textInputEditTextOrg.setText("")
                            }else
                            {
                                textInputEditTextOrg.setText(document["company"].toString())
                            }

                            if(intent.getBooleanExtra("edit_country",false))
                            {
                                if(intent.getStringExtra("selected_country_name") != null ||
                                    intent.getStringExtra("selected_flag_country") != null)
                                {
                                    tvCountryNameEdit.text =intent.getStringExtra("selected_country_name")
                                    Glide.with(this)
                                        .load(Uri.parse(intent.getStringExtra("selected_flag_country")))
                                        .into(imgFlagCountryEdit)
                                }
                            }else{

                                val countryGet : String =document["country"].toString()
                                val uriFlag : String = document["flagCountry"].toString()
                                if(countryGet == "null")
                                {
                                    tvCountryNameEdit.text = "No Seleccionado"
                                }else{
                                    //Log.d("TAG","Country DB $countryGet")
                                    tvCountryNameEdit.text = countryGet
                                }
                                if(uriFlag == "null")
                                {
                                    imgFlagCountryEdit.visibility = View.INVISIBLE

                                }else{
                                    imgFlagCountryEdit.visibility = View.VISIBLE
                                    Glide.with(this)
                                        .load(Uri.parse(uriFlag))
                                        .into(imgFlagCountryEdit)
                                }

                            }
                        }
                    }
                }
        }
    }

    override fun updateProfile()
    {
        if(textInputEditTextPhone.text!!.isEmpty()||textInputEditTextAddress.text!!.isEmpty()
            ||textInputEditTextOrg.text!!.isEmpty())
        {
            Toast.makeText(this,R.string.text_no_empty_fields, Toast.LENGTH_SHORT).show()

        }else
        {
            val currentUser = mAuth.currentUser
            if(currentUser != null)
            {
                firebasePresenter.updateUserDocument(mAuth.currentUser!!.uid).update(
                    "address",textInputEditTextAddress.text.toString(),
                    "company",textInputEditTextOrg.text.toString(),
                    "country",tvCountryNameEdit.text.toString(),
                    "flagCountry",intent.getStringExtra("selected_flag_country"),
                    "phone",textInputEditTextPhone.text.toString()
                ).addOnSuccessListener {
                    Toast.makeText(this,R.string.text_info_update,Toast.LENGTH_SHORT).show()
                   val intent = Intent(this,ProfileMainActivity::class.java)
                    startActivity(intent)//Back
                }
            }else
            {
                firebasePresenter.updateUserDocument(intent.getStringExtra("id").toString())
                    .update(
                    "address",textInputEditTextAddress.text.toString(),
                    "company",textInputEditTextOrg.text.toString(),
                        "country",tvCountryNameEdit.text.toString(),
                        "flagCountry",intent.getStringExtra("selected_flag_country"),
                    "phone",textInputEditTextPhone.text.toString()
                ).addOnSuccessListener {
                    Toast.makeText(this,R.string.text_info_update,Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,ProfileMainActivity::class.java)
                        startActivity(intent)//Back
                }
            }
        }
    }

    override fun showDialogCountry()
    {
        val intent = Intent(this,CountriesActivity::class.java)
        startActivity(intent)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}