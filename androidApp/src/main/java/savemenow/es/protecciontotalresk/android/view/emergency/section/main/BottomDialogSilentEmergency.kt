package savemenow.es.protecciontotalresk.android.view.emergency.section.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import savemenow.es.protecciontotalresk.android.R
import savemenow.es.protecciontotalresk.android.databinding.BottomDialogSilentEmergencyBinding
import savemenow.es.protecciontotalresk.android.model.reportemergency.EmergencyInfo
import savemenow.es.protecciontotalresk.android.presenter.FirebasePresenterCompl
import savemenow.es.protecciontotalresk.android.util.DateTimeStamp


/**
 ****Created by: Edison Martinez,
 ****Date:30,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class BottomDialogSilentEmergency( context: Context) :  BottomSheetDialogFragment()
{
    //Binding
    private lateinit var binding: BottomDialogSilentEmergencyBinding
    //Objetcts
    private lateinit var dialog : AlertDialog
    private lateinit var presenterFirebase : FirebasePresenterCompl
    private var displayUser =""
    private lateinit var  list: ArrayList<String>
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenterFirebase = FirebasePresenterCompl()
        list =  ArrayList()
        mAuth = FirebaseAuth.getInstance()
        binding = BottomDialogSilentEmergencyBinding.inflate(layoutInflater)
        //Log.d("TAG","TAG GET DATA ID ${arguments!!.getString("id")}")
        getUserConnect()
        //Log.d("TAG","TAG GET DATA PLACE ${arguments!!.getString("addressPlace")}")
        init()
        return binding.root
    }

    fun init()
    {
        getPhoneNumbers()

        binding.imgViolenceGen.setOnClickListener {
            showDialogConfirm("Violencia género")
            //sendNotification("test")
        }
        binding.imgAgresion.setOnClickListener {
            showDialogConfirm("Agresión")
        }
        binding.imgRobo.setOnClickListener {
            showDialogConfirm("Robo")
        }
        binding.imgViolenceSecuestro.setOnClickListener {
            showDialogConfirm("Secuestro")
        }
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    fun showDialogConfirm(type:String)
    {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.text_title_confirm_emergency)
        builder.setMessage(R.string.text_message_confirma_emergeny)
        builder.setPositiveButton(
            R.string.text_continuar
        ) { _, _ ->
            if(arguments!!.getString("id") != null ||
                arguments!!.getString("addressPlace") != null)
            {
                presenterFirebase.addReportEmergency(
                    arguments!!.getString("id").toString(),
                EmergencyInfo(
                    id = null,
                    type = type,
                    reportingUser = displayUser,
                    addressReported = arguments!!.getString("addressPlace"),
                    reportedDate = DateTimeStamp().getDateTime(),
                    geoPoint = GeoPoint(arguments!!.getDouble("latitudeGet"),
                        arguments!!.getDouble("longitudeGet")),
                    status = "Nueva"
                ))
                //sendNotification(type)//Send Msg
            }
            sendNotification()
            showDialogSuccess()//Success

        }
        builder.setNegativeButton(
            R.string.text_cancel_dialog
        ) { dialog, which ->

        }
        dialog = builder.create()
        dialog.show()

    }

    fun getUserConnect() : String
    {

        if(arguments!!.getString("id") != null)
        {
            val idGet = arguments!!.getString("id")
            presenterFirebase.getQueryById(idGet.toString()).get().addOnCompleteListener {
                if(it.isSuccessful)
                {
                    for(document in it.result)
                    {
                        Log.d("TAG","USER CONNECTED ${document.getString("fullName")}")
                        displayUser =document.getString("fullName").toString()
                    }
                }
            }
        }
        else
        {
            Toast.makeText(context,"No Disponible",Toast.LENGTH_SHORT).show()
        }
        return displayUser
    }

    fun showDialogSuccess()
    {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.text_tittle_emergency_success)
        builder.setMessage(R.string.text_emergency_success)
        builder.setPositiveButton(
            R.string.text_continuar
        ) { dialog, which ->
            dialog.dismiss()
            this.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }


    private fun getPhoneNumbers()
    {
        val currentUser = mAuth.currentUser

        if(currentUser != null)
        {
            presenterFirebase.getQueryEmergencyContact(mAuth.uid.toString()).get()
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
            Log.d("NOTIF",value)
        }
    }

}