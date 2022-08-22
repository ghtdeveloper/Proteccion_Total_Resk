package savemenow.es.protecciontotalresk.android.view.emergency.section.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.view.emergency.section.preview.PreviewActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
    override fun onStart() {
        super.onStart()
        showSignUp()
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
}