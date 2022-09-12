package savemenow.es.protecciontotalresk.android.view.emergency.section.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.view.emergency.section.preview.PreviewActivity
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
    override fun onStart() {
        super.onStart()
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = Objects.requireNonNull(connectivityManager).activeNetworkInfo
        val conexionActiva = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (conexionActiva) {
            Log.d("Internet", "Tenemos internet")
            showSignUp()
        } else {
            Log.d("Internet", " NO Tenemos internet")
            showAlert()
        }
        //showSignUp()
    }
    private fun showSignUp()
    {
        Handler().postDelayed(
            {
                val mIntent = Intent(this, PreviewActivity::class.java)
                startActivity(mIntent)
                finish()
            }, 1100)
    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_error)
        builder.setMessage(R.string.text_not_network)
        builder.setNegativeButton(
            R.string.text_continuar
        ) { _, _ ->
           finishAffinity()
        }
        val dialog = builder.create()
        dialog.show()
    }
}