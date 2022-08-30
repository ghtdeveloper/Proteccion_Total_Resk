package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.adapter.adapter.CustomAdapterPhoneLong
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.contacts.Contacts

class ContactsCompActivity : AppCompatActivity(), Contract.IContactsDetal {

    //Views
    private lateinit var toolbListContactComp : Toolbar
    private lateinit var recyclerviewFullContacts : RecyclerView
    //Objects
    private lateinit var CustomAdapterPhoneLong: CustomAdapterPhoneLong
    private lateinit var listContactPhone: ArrayList<Contacts>
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_comp)
        listContactPhone = ArrayList()
        initViews()
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
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        //Iterate
        while(phones!!.moveToNext())
        {
            val name = phones.getString(phones.getColumnIndex(
                ContactsContract.CommonDataKinds.
            Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(
                ContactsContract.
            CommonDataKinds.Phone.NUMBER))
            val contacts = Contacts()
            contacts.setName(name)
            contacts.setPhone(phoneNumber)
            listContactPhone.add(contacts)
        }
        phones.close()
        CustomAdapterPhoneLong = CustomAdapterPhoneLong(listContactPhone)
        recyclerviewFullContacts.adapter = CustomAdapterPhoneLong
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}