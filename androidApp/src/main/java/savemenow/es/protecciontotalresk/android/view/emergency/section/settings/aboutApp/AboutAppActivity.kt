package savemenow.es.protecciontotalresk.android.view.emergency.section.settings.aboutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract

class AboutAppActivity : AppCompatActivity(), Contract.ISettingsAboutApp
{
    //Views
    private lateinit var toolbarAboutApp : Toolbar

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        //Init
        initViews()
    }

    override fun initViews() {
        toolbarAboutApp = findViewById(R.id.toolbarAboutApp)
        //Config Toolbar
        this.setSupportActionBar(toolbarAboutApp)
        this.supportActionBar!!.title = getString(R.string.text_version_app)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}