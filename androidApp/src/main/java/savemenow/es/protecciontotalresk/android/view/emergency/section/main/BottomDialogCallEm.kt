package savemenow.es.protecciontotalresk.android.view.emergency.section.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.databinding.BottomDialogCallEmergencyBinding
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl


/**
 ****Created by: Edison Martinez,
 ****Date:29,Monday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class BottomDialogCallEm(context:Context) : BottomSheetDialogFragment()
{
    //Binding
    private lateinit var binding :BottomDialogCallEmergencyBinding
    //Objects
    private lateinit var dialog : AlertDialog
    private lateinit var dialogSilentEmergency: BottomDialogSilentEmergency
    private lateinit var firebasePresenter: FirebasePresenterCompl
    private lateinit var  list: ArrayList<String>
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        firebasePresenter = FirebasePresenterCompl()
        list =  ArrayList()
        mAuth = FirebaseAuth.getInstance()
        dialogSilentEmergency = BottomDialogSilentEmergency(context!!)
        binding = BottomDialogCallEmergencyBinding.inflate(layoutInflater)
        init()

        return binding.root
    }

    fun init()
    {
        getPhoneNumbers()

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
        binding.imgCall.setOnClickListener {
            //Toast.makeText(context,"DO CALL",Toast.LENGTH_SHORT).show()
            makeCall("888")
            sendNotification()
        }
        binding.imgChat.setOnClickListener {
            //Example +8293403254
            showDialogMessage("888")

        }
        binding.imgSilenceCall.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",arguments!!.getString("id"))
            bundle.putString("addressPlace",arguments!!.getString("addressPlace"))
            bundle.putDouble("latitudeGet",arguments!!.getDouble("latitudeGet"))
            bundle.putDouble("longitudeGet",arguments!!.getDouble("longitudeGet"))
            dialogSilentEmergency.arguments = bundle
          dialogSilentEmergency.show(childFragmentManager,"show")
        }
    }

    fun makeCall(phoneNumber :String)
    {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }

    fun showDialogMessage(phoneNumber: String)
    {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val viewDialogo = inflater.inflate(R.layout.dialog_send_sms, null)
        val textInputEditTextPhone = viewDialogo.
        findViewById<TextInputEditText>(R.id.textInputEditTextPhone)
        textInputEditTextPhone.setText(phoneNumber)
        val textInputEditTextMessage = viewDialogo.
        findViewById<TextInputEditText>(R.id.textInputEditTextMessage)
        builder.setView(viewDialogo)
        //Cast a las vistas
        builder.setNegativeButton(
            R.string.text_cancel_dialog
        ) { _, _ ->
        }
        builder.setPositiveButton(
            R.string.text_send
        ) { dialog, which ->
            if(textInputEditTextMessage.text.toString().isEmpty())
            {
                Toast.makeText(context,"Favor ingresar el texto del mesaje",Toast.LENGTH_SHORT)
                    .show()
            }else
            {
                val smsManager : SmsManager =  SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber,null,
                    textInputEditTextMessage.text.toString(),null,null)
                Toast.makeText(context,R.string.text_message_send,Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                sendNotification()
            }
        }
        dialog = builder.create()
        dialog.layoutInflater
        dialog.show()
    }

    private fun getPhoneNumbers()
    {
        val currentUser = mAuth.currentUser

        if(currentUser != null)
        {
            firebasePresenter.getQueryEmergencyContact(mAuth.uid.toString()).get()
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        for(document in it.result)
                        {
                            list.add(document.id)
                        }
                    }
                }
        }

    }

    private fun sendNotification()
    {
        for(value in list)
        {
            val smsManager : SmsManager =  SmsManager.getDefault()
            smsManager.sendTextMessage(value,null,
               "SOS"+ "\n " +
                       "\n"+mAuth.currentUser!!.displayName+"\n"+
                   "\n"+"Ubicacion reportada: "+ arguments!!.getString("addressPlace")+"\n"+"\n"+
                   "Latitud y longitud :"+arguments!!.getDouble("latitudeGet")+ ","+
                       arguments!!.getDouble("longitudeGet"),
                null,null)
        }
    }



}