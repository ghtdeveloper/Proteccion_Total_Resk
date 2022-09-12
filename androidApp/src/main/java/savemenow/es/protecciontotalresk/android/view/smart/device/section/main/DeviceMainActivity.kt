package savemenow.es.protecciontotalresk.android.view.smart.device.section.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.databinding.ActivityDeviceMainBinding

class DeviceMainActivity : AppCompatActivity()
{
    //Binding
    private lateinit var binding: ActivityDeviceMainBinding


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init
        init()
    }

    private fun init()
    {
        //Config Toolbar
        this.setSupportActionBar(binding.toolbarDevices)
        this.supportActionBar!!.title = getString(R.string.text_devices)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}