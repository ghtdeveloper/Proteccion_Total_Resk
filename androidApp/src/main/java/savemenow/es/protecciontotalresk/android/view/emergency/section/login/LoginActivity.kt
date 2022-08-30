package savemenow.es.protecciontotalresk.android.view.emergency.section.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.User
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl
import savemenow.es.protecciontotalresk.android.presenter.LoginPresenterCompl
import savemenow.es.protecciontotalresk.android.util.DateTimeStamp
import savemenow.es.protecciontotalresk.android.util.Encryptor
import savemenow.es.protecciontotalresk.android.view.emergency.section.main.MainActivity

class LoginActivity : AppCompatActivity(), Contract.ILoginView {

    //Views
    private lateinit var textInputEditTextEmail : TextInputEditText
    private lateinit var textInputEditTextPass : TextInputEditText
    private lateinit var btnLogin : Button
    private lateinit var cardViewGoogle : CardView
    private var dialog: AlertDialog? = null
    //Objects
    private lateinit var loginPresenterCompl: LoginPresenterCompl
    private lateinit var firebasePresenterCompl: FirebasePresenterCompl
    private lateinit var encryptor: Encryptor
    private lateinit var mAuth: FirebaseAuth
    //Google Sign
    private var googleSignInClient: GoogleSignInClient? = null
    private var mCallbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        FacebookSdk.sdkInitialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Views
        initViews()
        loginPresenterCompl = LoginPresenterCompl(this)
        firebasePresenterCompl = FirebasePresenterCompl()
        encryptor = Encryptor()
        mAuth = FirebaseAuth.getInstance()
        configGoogleAccess()
        //FB
        mCallbackManager = CallbackManager.Factory.create()
        loginButton?.setReadPermissions("email", "public_profile")
        loginButton!!.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("TAG_FACEBOOK", "Accceso Facebook exitoso$loginResult")
                //Manejar Acceso con Facebook Token
                firebaseAuthFB(loginResult.getAccessToken());//Verificar
            }

            override fun onCancel() {
                Log.d("TAG_FACEBOOK", "Solicitud de acceso Facebook cancelada")
                //Se actualiza la UI
            }

            override fun onError(error: FacebookException) {
                Log.e("TAG_FACEBOOK", "Error al acceder con facebook")
                //Se actualiza la UI
            }
        })
    }

    override fun initViews() {
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail)
        textInputEditTextPass = findViewById(R.id.textInputEditTextPass)
        btnLogin = findViewById(R.id.btn_login)
        loginButton = findViewById(R.id.btnAccFab)
        cardViewGoogle = findViewById(R.id.cardViewAccGoogle)
        //Listeners
        btnLogin.setOnClickListener {
            //Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show()
            if(loginPresenterCompl.validateFields(textInputEditTextEmail.text.toString(),
                textInputEditTextPass.text.toString()))
            {
                Toast.makeText(this,R.string.text_no_empty_fields,Toast.LENGTH_SHORT).show()
            }else
            {
                showDialogRunProcess()
            }
        }
        cardViewGoogle.setOnClickListener {
           launchAuthGoogle()
        }
    }

    override fun showAlert() {
        dialog!!.dismiss()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_login_fail_title)
        builder.setMessage(R.string.text_login_fail)
        builder.setNegativeButton(
            R.string.text_continuar
        ) { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showDialogRunProcess() {
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
                loginPresenterCompl.doLogin(textInputEditTextEmail.text.toString(),
                    encryptor.encodeData(textInputEditTextPass.text.toString()))
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

    override fun showMainMenu(id: String, fullName: String, email: String) {
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("fullName",fullName)
        intent.putExtra("email",email)
        startActivity(intent)
    }

    override fun configGoogleAccess()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.text_id_cliente_google_sing_in))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun firebaseAuthGoogle(idToken: String)
    {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    firebasePresenterCompl.addDataSignUpAuth(
                        User(
                            address = null,
                            company = null,
                            country = null,
                            email = user!!.email.toString(),
                            fullName = user.displayName.toString(),
                            id = user.uid,
                            pass = null,
                            phone =null,
                            providerType = "Google",
                            regDate = DateTimeStamp().getDateTime(),
                            uriPhoto = user.photoUrl.toString())
                    ,user.uid)
                    showMainMenu(user.displayName.toString(), user.displayName.toString(),user.email.
                    toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

    override fun firebaseAuthFB(idToken: AccessToken)
    {
        Log.d("TAG", "Acceso con Facebook$idToken")
        //Mostrando barra de progreso
        val credential = FacebookAuthProvider.getCredential(idToken.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = mAuth.currentUser
                firebasePresenterCompl.addDataSignUpAuth(
                    User(
                        address = null,
                        company = null,
                        country = null,
                        email = user!!.email.toString(),
                        fullName = user.displayName.toString(),
                        id = user.uid,
                        pass = null,
                        phone =null,
                        providerType = "Facebook",
                        regDate = DateTimeStamp().getDateTime(),
                        uriPhoto = user.photoUrl.toString())
                    ,user.uid)
                showMainMenu(user.displayName.toString(), user.displayName.toString(),user.email.toString())
            } else {
                Log.w("TAG", "signInWithCredential:failure", task.exception)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data) ///Se verifica lo del fcebook*
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val accountUser =
                    task.getResult(ApiException::class.java)!! //Error aqui
                firebaseAuthGoogle(accountUser.idToken!!)
            } catch (e: ApiException) {
                Log.d("TAG_GOOGLE", e.toString())
                e.printStackTrace()
            }
        } //Fin del if
    } //Fin del metodo onActivityResultEdit

    override fun launchAuthGoogle() {
        val intentAccesoGoogle = googleSignInClient!!.signInIntent
        startActivityForResult(intentAccesoGoogle, 1)
    }



}