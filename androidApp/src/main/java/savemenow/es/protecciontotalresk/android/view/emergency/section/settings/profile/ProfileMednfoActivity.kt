package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.CustomAdapterPhoneShort
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.contacts.Contacts
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl

class ProfileMednfoActivity : AppCompatActivity(), Contract.IProfileMedInfoMain {

    //Views
    private lateinit var toolbarMedInfo : Toolbar
    private  lateinit var cardViewInfoMed : CardView
    private  lateinit var cardViewListContactShort : CardView
    private lateinit var tvValueTypeBlood : TextView
    private lateinit var tvValueMedCondition : TextView
    private lateinit var recyclerviewShotContacts : RecyclerView
    //Objects
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private lateinit var customAdapterPhone: CustomAdapterPhoneShort
    private lateinit var listContactPhone: ArrayList<Contacts>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_mednfo)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        listContactPhone = ArrayList()
        //Init
        initViews()
    }

    override fun onStart() {
        super.onStart()
        //Load RecyclerVew
        showRecyclerView()
    }

    override fun initViews() {
        toolbarMedInfo = findViewById(R.id.toolbarMedInfo)
        cardViewInfoMed = findViewById(R.id.cardViewInfoMed)
        cardViewListContactShort = findViewById(R.id.cardViewListContactShort)
        tvValueTypeBlood = findViewById(R.id.tvValueTypeBlood)
        tvValueMedCondition = findViewById(R.id.tvValueMedCondition)
        recyclerviewShotContacts = findViewById(R.id.recyclerviewShotContacts)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewShotContacts.layoutManager = linearLayoutManager
        //Config Toolbar
        this.setSupportActionBar(toolbarMedInfo)
        this.supportActionBar!!.title = getString(R.string.text_inf_medical)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //Show Info (DATA)
        showInfo()
        //Listener
        cardViewInfoMed.setOnClickListener {
            val intent = Intent(this,ProfileMedEditActivity::class.java)
            intent.putExtra("id",intent.getStringExtra("id"))
            startActivity(intent)
        }
        cardViewListContactShort.setOnClickListener {
            val intent = Intent(this,ContactsCompActivity::class.java)
            intent.putExtra("id",intent.getStringExtra("id"))
            startActivity(intent)
        }
    }

    override fun showInfo() {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
           lifecycleScope.launch {
               firebasePresenter.getMedicalInfo(mAuth.currentUser!!.uid).get()
                   .addOnCompleteListener {
                       if(it.isSuccessful)
                       {
                          for(document in it.result)
                          {
                              tvValueTypeBlood.text = document["typBlood"].toString()
                              tvValueMedCondition.text = document["medContidition"].toString()
                          }
                       }
                   }
           }
        }else{
            lifecycleScope.launch {
                firebasePresenter.getMedicalInfo(intent.getStringExtra("id").toString()).get()
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            for(document in it.result)
                            {
                                tvValueTypeBlood.text = document["typBlood"].toString()
                                tvValueMedCondition.text = document["medContidition"].toString()
                            }
                        }
                    }
            }
        }
    }

    @SuppressLint("Recycle", "Range")
    override fun showRecyclerView() {
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        //Iterate
        while(phones!!.moveToNext())
        {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.
            Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.
            CommonDataKinds.Phone.NUMBER))
            val contacts = Contacts()
            contacts.setName(name)
            contacts.setPhone(phoneNumber)
            listContactPhone.add(contacts)
        }
        phones.close()
        customAdapterPhone = CustomAdapterPhoneShort(listContactPhone)
        recyclerviewShotContacts.adapter = customAdapterPhone
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}