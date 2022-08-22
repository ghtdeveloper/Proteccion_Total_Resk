package savemenow.es.protecciontotalresk.android.contract

import com.facebook.AccessToken
import com.google.firebase.firestore.Query
import savemenow.es.protecciontotalresk.android.model.User


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
interface Contract
{

    /**
     *  Views Contracts
     */
    interface ISignUpView{
        fun initViews()
        fun doSignUp(user: User)
        fun onClearText()
        fun closeActivity()
        fun showAlert()
    }

    interface ILoginView{
        fun initViews()
        fun showAlert()
        fun showDialogRunProcess()
        fun showMainMenu(id: String, fullName: String)
        fun configGoogleAccess()
        fun firebaseAuthGoogle(idToken : String)
        fun firebaseAuthFB(idToken: AccessToken)
        fun launchAuthGoogle()
    }

    interface IMainMenuView
    {
        fun initViews()
    }

    /**
     *  Presenters Interfaces
     */
    ///Presenter
    interface ISignUpPresenter
    {
        fun onClearText()
        fun doSignUp(user: User)
        fun validatePassword(pass:String, confirmPass : String) : Boolean
        fun valideFields(email:String,fullname: String,pass: String,confirmPass: String) : Boolean
    }

    interface  IFirebasePresenter
    {
        fun addDataSignUp(user: User)
        fun addDataSignUpAuth(user: User,uid:String)
        fun getQueryByEmail(email : String) : Query
        fun getQueryById(id: String)
        fun getQueryAuth(email: String,pass: String) : Query
    }

    interface ILoginPresenter
    {
        fun validateFields(email: String,pass: String) : Boolean
        fun doLogin(email:String, pass: String)
    }

}