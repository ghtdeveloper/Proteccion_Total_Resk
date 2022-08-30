package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl
import savemenow.es.protecciontotalresk.android.view.emergency.section.preview.PreviewActivity
import savemenow.es.protecciontotalresk.android.view.emergency.section.settings.aboutApp.AboutAppActivity
import savemenow.es.protecciontotalresk.android.view.emergency.section.settings.profile.ProfileMainActivity

class SettingsMain : AppCompatActivity(), Contract.ISettingsMainView
{
    //Views
    private lateinit var toolbarMainSettings : Toolbar
    private lateinit var card_view_profile_settings :CardView
    private lateinit var card_view_notif_settings : CardView
    private lateinit var card_view_aboutApp_settings : CardView
    private lateinit var card_view_signOut_settings : CardView
    private lateinit var tvEmailSettings : TextView
    //Objects
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main)
        firebasePresenter = FirebasePresenterCompl()
        //Init Views
        initViews()
        mAuth = FirebaseAuth.getInstance()
    }

    override  fun initViews() {
        toolbarMainSettings = findViewById(R.id.toolbarMainSettings)
        card_view_profile_settings = findViewById(R.id.card_view_profile_settings)
        card_view_notif_settings = findViewById(R.id.card_view_notif_settings)
        card_view_aboutApp_settings = findViewById(R.id.card_view_aboutApp_settings)
        card_view_signOut_settings = findViewById(R.id.card_view_signOut_settings)
        tvEmailSettings = findViewById(R.id.tvEmailSettings)
        //Config Toolbar
        this.setSupportActionBar(toolbarMainSettings)
        this.supportActionBar!!.title = getString(R.string.text_settings)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        //ShowData
        tvEmailSettings.text = intent.getStringExtra("email")
        //Listeners
        card_view_profile_settings.setOnClickListener {
            showProfileActivity()
        }
        card_view_notif_settings.setOnClickListener {

        }
        card_view_aboutApp_settings.setOnClickListener {
            showInfoApp()
        }
        card_view_signOut_settings.setOnClickListener {
            showAlertSignOut()
        }
    }


    override fun showProfileActivity() {
        val intent = Intent(this,ProfileMainActivity::class.java)
        intent.putExtra("id",intent.getStringExtra("id"))
        startActivity(intent)
    }

    override fun showNotifActivity() {
        TODO("Not yet implemented")
    }

    override fun showInfoApp() {
       val intent = Intent(this,AboutAppActivity::class.java)
        intent.putExtra("id",intent.getStringExtra("id"))
        startActivity(intent)
    }

    override fun showAlertSignOut() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_title_dialog_sign_out)
        builder.setMessage(R.string.text_message_sign_out)
        builder.setPositiveButton(
            R.string.text_yes
        ) { _, _ ->
            val currentUser = mAuth.currentUser
            if(currentUser != null)
            {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,PreviewActivity::class.java)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this,PreviewActivity::class.java)
                startActivity(intent)
            }
          }
        builder.setNegativeButton(
            R.string.text_no
        ) { _, _ ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    override  fun getEmailUser(uid: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO)
            {
                firebasePresenter.getQueryById(uid).get().addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        for(document in it.result)
                        {
                           // emailTemp = document["email"].toString()
                            tvEmailSettings.text = document["email"].toString()
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}