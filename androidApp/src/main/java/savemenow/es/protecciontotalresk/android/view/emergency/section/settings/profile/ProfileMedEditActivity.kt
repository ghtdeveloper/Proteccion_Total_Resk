package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.MedicalInfo
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl

class ProfileMedEditActivity : AppCompatActivity(), Contract.IProfileMedEdit,AdapterView.
OnItemSelectedListener
{

    //Views
    private lateinit var toolbarMedEdit : Toolbar
    private lateinit var spin_blood_type : Spinner
   // private lateinit var textInputEditTextTypeBlood : TextInputEditText
    private lateinit var textInputEditTextMedCond : TextInputEditText
    private lateinit var textInputEditTextMedication : TextInputEditText
    private lateinit var textInputEditTextAllergies : TextInputEditText
    private lateinit var btn_save_edit : Button
    //Objects
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private var onItemSelect = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_med_edit)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        //Views
        initViews()
    }


    override fun initViews() {
        toolbarMedEdit = findViewById(R.id.toolbarMedEdit)
        spin_blood_type = findViewById(R.id.spin_blood_type)
        //Load Spiner
        ArrayAdapter.createFromResource(this,R.array.array_type_blood,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spin_blood_type.adapter = adapter
            }
        //textInputEditTextTypeBlood = findViewById(R.id.textInputEditTextTypeBlood)
        textInputEditTextMedCond = findViewById(R.id.textInputEditTextMedCond)
        textInputEditTextMedication = findViewById(R.id.textInputEditTextMedication)
        textInputEditTextAllergies = findViewById(R.id.textInputEditTextAllergies)
        btn_save_edit = findViewById(R.id.btn_save_edit)
        //Config Toolbar
        this.setSupportActionBar(toolbarMedEdit)
        this.supportActionBar!!.title = getString(R.string.text_inf_medical)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //Show Info
        showInfo()
        //Listener
        btn_save_edit.setOnClickListener {saveData()}
        spin_blood_type.onItemSelectedListener = this
    }

    override fun showInfo()
    {
        val currentUser = mAuth.currentUser
        var tempDataSelect = ""
        if(currentUser != null)
        {
            lifecycleScope.launch {
                firebasePresenter.getMedicalInfo(mAuth.currentUser!!.uid).get()
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            for(document in it.result)
                            {
                                //showDataSpiner(document["typBlood"].toString())
                                tempDataSelect =document["typBlood"].toString()
                                textInputEditTextMedCond.setText(document["medContidition"].toString())
                                textInputEditTextMedication.setText(document["medicate"].toString())
                                textInputEditTextAllergies.setText(document["allergies"].toString())

                            }
                            //spin_blood_type.setSelection(6)
                            showDataSpiner(tempDataSelect)
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
                                tempDataSelect =document["typBlood"].toString()
                                textInputEditTextMedCond.setText(document["medContidition"].toString())
                                textInputEditTextMedication.setText(document["medicate"].toString())
                                textInputEditTextAllergies.setText(document["allergies"].toString())
                            }
                            showDataSpiner(tempDataSelect)
                        }
                    }
            }
        }
    }

    fun showDataSpiner(value:String)
    {
        for (position in 0 until spin_blood_type.adapter.count ) {
            if(spin_blood_type.getItemAtPosition(position).equals(value))
            {
               Log.d("TAG","POSITION FIND $position")
                spin_blood_type.setSelection(position,true)
            }
        }
    }

    override fun saveData()
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            firebasePresenter.addDataMedInfo(mAuth.currentUser!!.uid, MedicalInfo(
                id = mAuth.currentUser!!.uid,
                typBlood = onItemSelect,
                medContidition = textInputEditTextMedCond.text.toString(),
                medicate = textInputEditTextMedication.text.toString(),
                allergies = textInputEditTextAllergies.text.toString()))
            Toast.makeText(this,R.string.text_info_update, Toast.LENGTH_SHORT).show()
            showBackActivity()//Back
        }else
        {
            firebasePresenter.addDataMedInfo(intent.getStringExtra("id").toString(),
                MedicalInfo(
                id = intent.getStringExtra("id"),
                typBlood = onItemSelect,
                medContidition = textInputEditTextMedCond.text.toString(),
                medicate = textInputEditTextMedication.text.toString(),
                allergies = textInputEditTextAllergies.text.toString()))
            Toast.makeText(this,R.string.text_info_update, Toast.LENGTH_SHORT).show()
            showBackActivity()//Back

        }
    }

    override fun showBackActivity() {
        val intent = Intent(this,ProfileMednfoActivity::class.java)
        //intent.putExtra("id",intent.getStringExtra("id"))
        startActivity(intent)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        if (p0 != null) {
            onItemSelect = p0.getItemAtPosition(pos) as String
        }
        Log.d("TAG","DATA SELECTED $onItemSelect")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}