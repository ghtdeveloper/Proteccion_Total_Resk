package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.databinding.ActivityContactsEmergencttDetailBinding
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl

class ContactsEmergencttDetail : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //Views
    private lateinit var binding:ActivityContactsEmergencttDetailBinding
    //Objects
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private var onItemSelectBlood = ""
    private var onItemSelectRelationship = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        binding = ActivityContactsEmergencttDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init()
    {
        linearLayoutManager = LinearLayoutManager(this)
        //Config Toolbar
            this.setSupportActionBar(binding.toolbarContactEmergencyDetail)
        this.supportActionBar!!.title = getString(R.string.text_contacto_emergencia)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //Load Data Spinner
        loadDataSpinTypeBlood()
        loadDataSpinRelationship()
        //Get Info DB
        showData()
        //Listener
        binding.btnSaveEdit.setOnClickListener { updateData() }
        binding.spinBloodType.onItemSelectedListener = this
        binding.spinRelationShip.onItemSelectedListener = this
    }

    private fun loadDataSpinTypeBlood()
    {
        //Load Spiner
        ArrayAdapter.createFromResource(this,R.array.array_type_blood,android.R.layout
            .simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinBloodType.adapter = adapter
            }
    }

    private fun loadDataSpinRelationship()
    {
        //Load Spiner
        ArrayAdapter.createFromResource(this,R.array.array_relationship,android.R.layout
            .simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinRelationShip.adapter = adapter
            }
    }

    private fun showData()
    {
        var tempDataSelectBlood = ""
        var tempDataSelectRelationShip = ""
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            firebasePresenter.getQueryEmergencyContact(currentUser.uid).whereEqualTo("phone",
            intent.getStringExtra("idPhone")).get().addOnCompleteListener {
                if(it.isSuccessful)
                {
                    for(document in it.result)
                    {
                        binding.textInputEditTextFullName.setText(document.getString("name")
                            .toString())
                        binding.textInputEditTextPhone.setText(document.getString("phone"))
                        binding.textInputEditTextAddress.setText(document.getString("address"))
                        //Spiner
                        //showSpinerRelationsship(document.getString("relationship").toString())
                        tempDataSelectBlood = document.getString("typBlood").toString()
                        tempDataSelectRelationShip = document.getString("relationship")
                            .toString()

                    }
                    showDataSpinerTypeBlood(tempDataSelectBlood)
                    showSpinerRelationsship(tempDataSelectRelationShip)
                }
            }
        }
    }

    private fun showDataSpinerTypeBlood(value : String)
    {
        for(position in 0 until binding.spinBloodType.adapter.count)
        {
            if(binding.spinBloodType.getItemAtPosition(position).equals(value))
            {
                binding.spinBloodType.setSelection(position,true)
            }
        }
    }

    private fun showSpinerRelationsship(value : String)
    {
        for(position in 0 until binding.spinRelationShip.adapter.count)
        {
            if(binding.spinRelationShip.getItemAtPosition(position).equals(value))
            {
                binding.spinRelationShip.setSelection(position,true)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long)
    {
       if(parent!!.id == binding.spinBloodType.id)
       {
           onItemSelectBlood = parent.getItemAtPosition(pos) as String

       }else if(parent.id == binding.spinRelationShip.id)
       {
           onItemSelectRelationship = parent.getItemAtPosition(pos) as String
       }

    }

    override fun onNothingSelected(p0: AdapterView<*>?)
    {}

    private fun updateData()
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            firebasePresenter.updateContactInfo(currentUser.uid,intent.
            getStringExtra("idPhone").toString()).update(
                "address",binding.textInputEditTextAddress.text.toString(),
                     "name",binding.textInputEditTextFullName.text.toString(),
                "typBlood",onItemSelectBlood,
                "relationship",onItemSelectRelationship
            ).addOnSuccessListener {
                val intent = Intent(this,ContactEmergencyList::class.java)
                startActivity(intent)
                Toast.makeText(this,"Datos actualizados correctamente",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }
}