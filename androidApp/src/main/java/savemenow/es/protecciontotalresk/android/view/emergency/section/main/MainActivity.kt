package savemenow.es.protecciontotalresk.android.view.emergency.section.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import savemenow.es.protecciontotalresk.Greeting
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract


class MainActivity : AppCompatActivity(), Contract.IMainMenuView {

    //Views
    private lateinit var toolbarMainMenu : Toolbar
    //Objects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Init
        initViews()


    }

    override fun initViews() {
        toolbarMainMenu = findViewById(R.id.toolbarMainMenu)
        //Config Toolbar
        this.setSupportActionBar(toolbarMainMenu)
        this.supportActionBar!!.title = "Bienvenido ${intent.getStringExtra("fullName")}"
        this.supportActionBar!!.subtitle = getString(R.string.text_info_welcome)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
