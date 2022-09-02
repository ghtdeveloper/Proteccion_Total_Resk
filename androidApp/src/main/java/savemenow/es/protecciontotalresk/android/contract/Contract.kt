package savemenow.es.protecciontotalresk.android.contract

import com.facebook.AccessToken
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import savemenow.es.protecciontotalresk.android.model.User.MedicalInfo
import savemenow.es.protecciontotalresk.android.model.User.User
import savemenow.es.protecciontotalresk.android.model.contacts.EmergencyContacts
import savemenow.es.protecciontotalresk.android.model.reportemergency.EmergencyInfo


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
        fun showMainMenu(id: String, fullName: String,email:String)
        fun configGoogleAccess()
        fun firebaseAuthGoogle(idToken : String)
        fun firebaseAuthFB(idToken: AccessToken)
        fun launchAuthGoogle()
    }

    interface IMainMenuView
    {
        fun initViews()
        fun onClickBottomNav()
        fun showSettingsActivity(uid: String,email:String)
    }


    interface ISettingsMainView {
        fun initViews()
        fun showProfileActivity()
        fun showNotifActivity()
        fun showInfoApp()
        fun showAlertSignOut()
        fun getEmailUser(uid: String)
    }

    interface ISettingsAboutApp
    {
        fun initViews()
    }

    interface IProfileMain
    {
        fun initViews()
        fun onClickBottomNav()
        fun showInfo()
    }

    interface IProfileEdit
    {
        fun initViews()
        fun showInfo()
        fun updateProfile()
        fun showDialogCountry()
    }

    interface IProfileMedInfoMain {
        fun initViews()
        fun showInfo()
        fun showRecyclerView()
    }

    interface IProfileMedEdit {
        fun initViews()
        fun showInfo()
        fun saveData()
        fun showBackActivity()
    }

    interface IContactsDetal
    {
       fun initViews()
        fun showRecyclerView()
    }

    interface IViewCountries
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
        fun addDataSignUpAuth(user: User, uid:String)
        fun addDataMedInfo(uid: String,medicalInfo: MedicalInfo)
        fun addEmergencyContact(uid:String,phone:String,emergencyContacts: EmergencyContacts)
        fun addReportEmergency(uid:String,emergencyInfo: EmergencyInfo)
        fun getQueryByEmail(email : String) : Query
        fun getQueryById(id: String) : Query
        fun getQueryEmergencyContact(id: String): Query
        fun getReportEmergency(): Query
        fun getQueryAuth(email: String,pass: String) : Query
        fun updateUserDocument(uid: String) : DocumentReference
        fun updateContactInfo(uid:String,phone:String) : DocumentReference
        fun getMedicalInfo(id: String) : Query
    }

    interface ILoginPresenter
    {
        fun validateFields(email: String,pass: String) : Boolean
        fun doLogin(email:String, pass: String)
    }

}