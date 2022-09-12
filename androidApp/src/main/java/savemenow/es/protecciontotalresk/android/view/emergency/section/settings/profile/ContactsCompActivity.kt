package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.CustomAdapterPhoneLong
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.contacts.Contacts
import savemenow.es.protecciontotalresk.android.model.contacts.EmergencyContacts
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl


class ContactsCompActivity : AppCompatActivity(), Contract.IContactsDetal {

    //Views
    private lateinit var toolbListContactComp : Toolbar
    private lateinit var recyclerviewFullContacts : RecyclerView
    //Objects
    private lateinit var customAdapterPhoneLong: CustomAdapterPhoneLong
    private lateinit var listContactPhone: ArrayList<Contacts>
    private lateinit var listContactPhoneTemp : ArrayList<Contacts>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebasePresenter = FirebasePresenterCompl()
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_contacts_comp)
        listContactPhone = ArrayList()
        listContactPhoneTemp = ArrayList()
        initViews()
        Log.d("ID USER",intent.getStringExtra("id").toString())
    }

    override fun onStart() {
        super.onStart()
        //Load RecyclerVew
        showRecyclerView()
    }

    override fun initViews()
    {
        toolbListContactComp = findViewById(R.id.toolbListContactComp)
        recyclerviewFullContacts = findViewById(R.id.recyclerviewFullContacts)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerviewFullContacts.layoutManager = linearLayoutManager
        //Config Toolbar
        this.setSupportActionBar(toolbListContactComp)
        this.supportActionBar!!.title = getString(R.string.text_contacts)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
    }

    @SuppressLint("Recycle", "Range")
    override fun showRecyclerView() {
        val projection = arrayOf(
            CommonDataKinds.Phone.CONTACT_ID,
            CommonDataKinds.Phone.DISPLAY_NAME,
            CommonDataKinds.Phone.NUMBER
        )
        val cursor : Cursor? = contentResolver.query(CommonDataKinds.Phone
            .CONTENT_URI, projection, null, null, null)
        while (cursor!!.moveToNext())
        {
            val name = cursor.getString(1)
            val phone = cursor.getString(2)
            val contacts = Contacts()
            contacts.setName(name)
            contacts.setPhone(phone)
            listContactPhone.add(contacts)
        }
        cursor.close()
        customAdapterPhoneLong = CustomAdapterPhoneLong(listContactPhone, object :
            CustomAdapterPhoneLong.ItemClickListenerContacts{
            override fun clickRow(post: Int) {
                showDialogSaveContact(customAdapterPhoneLong.getContactName(),
                    customAdapterPhoneLong.getPhoneNumber())
            }
        })
        recyclerviewFullContacts.adapter = customAdapterPhoneLong
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu)
    }//Fin del metodo onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.menuShowsContacts ->
            {
               val intent = Intent(this,ContactEmergencyList::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }//Fin del metodo onOptionsItemSelected

    private fun showDialogSaveContact(name:String,phone:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_contact_title)
        builder.setMessage(R.string.text_contact_message)
        builder.setNegativeButton(
            R.string.text_cancel_dialog
        ) { _, _ -> }
        builder.setPositiveButton(
            R.string.text_continuar
        ) { dialog, which ->
            saveData(name, phone)
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun saveData(name:String,phone:String)
    {
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            firebasePresenter.addEmergencyContact(currentUser.uid,phone, EmergencyContacts(
                name = name,
                phone = phone.trim()))
        }
        Toast.makeText(this,"Contacto agregado!",Toast.LENGTH_SHORT)
            .show()
        dialog.dismiss()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}