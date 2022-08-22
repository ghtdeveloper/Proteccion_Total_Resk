package savemenow.es.protecciontotalresk.android.view.emergency.section.signup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.User
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl
import savemenow.es.protecciontotalresk.android.presenter.SignUpPresenterCompl
import savemenow.es.protecciontotalresk.android.util.DateTimeStamp
import savemenow.es.protecciontotalresk.android.util.Encryptor
import savemenow.es.protecciontotalresk.android.view.emergency.section.preview.PreviewActivity

class SignUpActivity : AppCompatActivity(), Contract.ISignUpView {

    //Views
    private lateinit var toolbarSignUp : Toolbar
    private lateinit var textInputEditTextEmail : TextInputEditText
    private lateinit var textInputEditTextFullName : TextInputEditText
    private lateinit var textInputEditTextPass : TextInputEditText
    private lateinit var textInputEditTextConfimPass : TextInputEditText
    private lateinit var btnSignUp : Button
    //Objects
    private lateinit var presenter : SignUpPresenterCompl
    private lateinit var presenterFirebase : FirebasePresenterCompl
    private lateinit var encryptor: Encryptor
    private lateinit var dateTimeStamp: DateTimeStamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //Find Views
        initViews()
        //init
        presenter = SignUpPresenterCompl(this)
        presenterFirebase = FirebasePresenterCompl()
        encryptor = Encryptor()
        dateTimeStamp = DateTimeStamp()
    }

    override fun initViews() {
        //Cast Views
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail)
        textInputEditTextFullName = findViewById(R.id.textInputEditTextFullName)
        textInputEditTextPass = findViewById(R.id.textInputEditTextPass)
        textInputEditTextConfimPass = findViewById(R.id.textInputEditTextConfimPass)
        btnSignUp = findViewById(R.id.btn_sign_up)
        toolbarSignUp = findViewById(R.id.toolbarSignUp)
        //Config Toolbar
        this.setSupportActionBar(toolbarSignUp)
        this.supportActionBar!!.title = resources.getString(R.string.text_toolbar_sign_up)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_close_32)
        btnSignUp.setOnClickListener {
            if(presenter.valideFields(textInputEditTextEmail.text.toString(),
                    textInputEditTextFullName.text.toString(),textInputEditTextPass.text.toString(),
                    textInputEditTextConfimPass.text.toString()))
            {
                Toast.makeText(this,R.string.text_no_empty_fields,Toast.LENGTH_SHORT).show()
            }else
            {
                if (presenter.validatePassword(textInputEditTextPass.text.toString(),
                        textInputEditTextConfimPass.text.toString()))
                {
                    Toast.makeText(this,R.string.text_pass_not_match,Toast.LENGTH_SHORT)
                        .show()
                }else
                {
                    presenterFirebase.getQueryByEmail(textInputEditTextEmail.text.toString())
                        .get().addOnSuccessListener {
                            if(it.isEmpty)
                            {
                                presenterFirebase.addDataSignUp(
                                    User(
                                        address = null,
                                        company = null,
                                        country = null,
                                        email = textInputEditTextEmail.text.toString(),
                                        fullName = textInputEditTextFullName.text.toString(),
                                        id = null,
                                        pass = encryptor.encodeData(textInputEditTextPass.text.
                                        toString()),
                                        phone =null,
                                        providerType = "N/A",
                                        regDate = dateTimeStamp.getDateTime(),
                                        uriPhoto = null)
                                )
                                Toast.makeText(this,R.string.text_title_user_reg,Toast.LENGTH_SHORT).
                                show()
                                closeActivity()
                            }else { showAlert() }
                        }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun doSignUp(user: User)
    {
        Toast.makeText(this,"Sign Up",Toast.LENGTH_SHORT).show()
    }

    override fun onClearText()
    {
        textInputEditTextEmail.setText("")
        textInputEditTextFullName.setText("")
        textInputEditTextPass.setText("")
        textInputEditTextConfimPass.setText("")
    }

    override fun closeActivity()
    {
       val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }

    override fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.text_email_registered_title)
        builder.setMessage(R.string.text_email_registered_message)
        builder.setNegativeButton(
            R.string.text_continuar
        ) { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }
}