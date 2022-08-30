package savemenow.es.protecciontotalresk.android.view.emergency.section.preview

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.view.emergency.section.login.LoginActivity
import savemenow.es.protecciontotalresk.android.view.emergency.section.main.MainActivity
import savemenow.es.protecciontotalresk.android.view.emergency.section.signup.SignUpActivity

class PreviewActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var dialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        //Load Views
        init()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser != null)
        {
            Log.d("TAG","There is a user connected")
            showDialogRunProcess(currentUser.uid,currentUser.displayName.toString(),currentUser.
            email.toString())
        }else
        {
            Log.d("TAG","There isn't a user connected")
        }

    }

     fun showDialogRunProcess(id:String,fullName:String,email:String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val viewDialogo = inflater.inflate(R.layout.layout_dialogo_progress_bar, null)
        val textViewTituloProgress = viewDialogo.findViewById<TextView>(R.id.textVieewProcesando)
        textViewTituloProgress.text = getText(R.string.text_do_loginProcess)
        builder.setView(viewDialogo)
        //Cast a las vistas
        Thread {
            try {
                Thread.sleep(1700)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("fullName",fullName)
                intent.putExtra("email",email)
                startActivity(intent)
                dialog!!.dismiss()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } //Fin del metodo run
            .start()
        dialog = builder.create()
        dialog!!.layoutInflater
        dialog!!.show()
    }

    private fun init()
    {
        val btnSignUp : Button = findViewById(R.id.btn_create_account)
        val btnLogin : Button = findViewById(R.id.btn_login)
        btnSignUp.setOnClickListener {
            val mIntent = Intent(this, SignUpActivity::class.java)
            startActivity(mIntent)
        }
        btnLogin.setOnClickListener {
            val mIntent = Intent(this, LoginActivity::class.java)
            startActivity(mIntent)
        }

    }
}