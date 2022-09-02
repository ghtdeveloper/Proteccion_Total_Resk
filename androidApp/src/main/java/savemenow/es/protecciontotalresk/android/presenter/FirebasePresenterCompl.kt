package savemenow.es.protecciontotalresk.android.presenter

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.User.MedicalInfo
import savemenow.es.protecciontotalresk.android.model.User.User
import savemenow.es.protecciontotalresk.android.model.contacts.EmergencyContacts
import savemenow.es.protecciontotalresk.android.model.reportemergency.EmergencyInfo


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class FirebasePresenterCompl : Contract.IFirebasePresenter
{
    //Objetc Firebase
    var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    override fun addDataSignUp(user: User)
    {
     db!!.collection("collect_dev_enviroment").document("User").
             collection("collect_user").add(user).addOnSuccessListener {
                 val id = it.id
                 it.update("id",id).addOnSuccessListener {
                     Log.d("TAG","Id Update")
                 }
            }
    }
    override fun addDataSignUpAuth(user: User, uid: String) {
        db!!.collection("collect_dev_enviroment").document("User").
        collection("collect_user").document(uid).set(user)
    }

    override fun addDataMedInfo(uid: String, medicalInfo: MedicalInfo) {
        db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").document(uid).
            collection("collect_med").document(uid).set(medicalInfo)
    }

    override fun addEmergencyContact(uid: String, phone: String, emergencyContacts:
    EmergencyContacts)
    {
        db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").document(uid).
            collection("collect_emergency_contacts").document(phone)
            .set(emergencyContacts)
    }

    override fun addReportEmergency(uid: String, emergencyInfo: EmergencyInfo)
    {
        db!!.collection("collect_dev_enviroment").document("Reports").
        collection("collect_report_emergency").document("Emergencies")
            .collection("collect_user_reports").add(emergencyInfo).addOnSuccessListener {
            val id = it.id
            it.update("id",id).addOnSuccessListener {
                Log.d("TAG","Id Update")
            }
        }
    }

    override fun getQueryByEmail(email: String): Query {
        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").whereEqualTo("email",email)
    }



    override fun getQueryById(id: String): Query {
        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").whereEqualTo("id",id)
    }

    override fun getQueryEmergencyContact(id: String) : Query
    {
        return db!!.collection("collect_dev_enviroment").document("User").
        collection("collect_user").document(id).
        collection("collect_emergency_contacts")
    }

    override fun getReportEmergency(): Query
    {
        return db!!.collection("collect_dev_enviroment").document("Reports").
        collection("collect_report_emergency").document("Emergencies").
                collection("collect_user_reports")
    }

    override fun getQueryAuth(email: String, pass: String): Query {
        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").whereEqualTo("email",email)
            .whereEqualTo("pass",pass)
    }

    override fun updateUserDocument(uid: String): DocumentReference {
       return db!!.collection("collect_dev_enviroment").document("User").
        collection("collect_user").document(uid)
    }

    override fun updateContactInfo(uid: String, phone: String): DocumentReference {
        return db!!.collection("collect_dev_enviroment").document("User").
        collection("collect_user").document(uid).
        collection("collect_emergency_contacts").document(phone)
    }

    override fun getMedicalInfo(id: String): Query {
        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").document(id).
            collection("collect_med").whereEqualTo("id",id)
    }

}