package savemenow.es.protecciontotalresk.android.presenter

import android.util.Log
import savemenow.es.protecciontotalresk.android.contract.Contract


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class LoginPresenterCompl(private val iLoginView: Contract.ILoginView) : Contract.ILoginPresenter
{
    private var firebasePresenterCompl: FirebasePresenterCompl = FirebasePresenterCompl()
    //f

    override fun validateFields(email: String, pass: String): Boolean {
      return (email.isEmpty() || pass.isEmpty())
    }

    override fun doLogin(email: String, pass: String) {
       firebasePresenterCompl.getQueryAuth(email, pass).get().addOnSuccessListener { it ->
           if (it.isEmpty)
           {
               Log.e("Inicio sesion", "Error en la autenticacion")
               iLoginView.showAlert()
           }else
           {
               Log.d("Inicio sesion", "Autenticacion exitosa")
               it.query.get().addOnCompleteListener {
                    var fullName = ""
                    var id = ""
                   var email =""
                   if(it.isSuccessful)
                   {
                       for(document in it.result)
                       {
                           fullName = document["fullName"].toString()
                           id = document["id"].toString()
                           email = document["email"].toString()
                       }
                   }
                   iLoginView.showMainMenu(id, fullName,email)//Pass Params
               }//OnComplete

           }
       }
    }
}