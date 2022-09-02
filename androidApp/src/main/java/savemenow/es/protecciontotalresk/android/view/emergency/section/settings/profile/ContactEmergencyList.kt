package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.AdapterListContactEmergency
import savemenow.es.protecciontotalresk.android.databinding.ActivityContactEmergencyListBinding
import savemenow.es.protecciontotalresk.android.model.contacts.EmergencyContacts
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl

class ContactEmergencyList : AppCompatActivity()
{
    //Views
    private lateinit var binding: ActivityContactEmergencyListBinding
    //Objects
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private lateinit var adapterListContactEmergency: AdapterListContactEmergency

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityContactEmergencyListBinding.inflate(layoutInflater)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        //Init
        init()
    }

    override fun onStart() {
        super.onStart()
        showContacts()
    }

    fun init()
    {
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerListContacts.layoutManager = linearLayoutManager
        //Config Toolbar
        this.setSupportActionBar(binding.toolbarContactsFirebaseMain)
        this.supportActionBar!!.title = getString(R.string.text_contacto_emergencia)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showContacts()
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            val builderOptions = FirestoreRecyclerOptions.Builder<EmergencyContacts>()
                .setQuery(firebasePresenter.getQueryEmergencyContact(currentUser.uid),
                    EmergencyContacts::class.java).build()
            adapterListContactEmergency = AdapterListContactEmergency(builderOptions,object
                :AdapterListContactEmergency.ItemClickListener
            {
                override fun clickRow(post: Int) {
                   val intent = Intent(this@ContactEmergencyList,
                       ContactsEmergencttDetail::class.java)
                    intent.putExtra("idPhone",adapterListContactEmergency.getPhoneNumber())
                    startActivity(intent)
                }
            })
            binding.recyclerListContacts.adapter = adapterListContactEmergency
            adapterListContactEmergency.startListening()
        }
    }

}