package savemenow.es.protecciontotalresk.android.presenter

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.User


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class FirebasePresenterCompl : Contract.IFirebasePresenter
{
    //Objetc Firebase
    var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    init {

    }

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

    override fun getQueryByEmail(email: String): Query {

        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").whereEqualTo("email",email)
    }

    override fun getQueryById(id: String) {
        TODO("Not yet implemented")
    }

    override fun getQueryAuth(email: String, pass: String): Query {
        return  db!!.collection("collect_dev_enviroment").document("User")
            .collection("collect_user").whereEqualTo("email",email)
            .whereEqualTo("pass",pass)
    }

}