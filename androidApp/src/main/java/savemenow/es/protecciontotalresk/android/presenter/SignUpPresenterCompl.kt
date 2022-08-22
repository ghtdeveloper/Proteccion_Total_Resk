package savemenow.es.protecciontotalresk.android.presenter

import savemenow.es.protecciontotalresk.android.contract.Contract
import savemenow.es.protecciontotalresk.android.model.User


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class SignUpPresenterCompl(
    var view: Contract.ISignUpView
) : Contract.ISignUpPresenter
{

    override fun onClearText() {
        view.onClearText()
    }

    override fun doSignUp(user: User) {
       view.doSignUp(user)
    }

    override fun validatePassword(pass: String, confirmPass: String): Boolean {
        return pass != confirmPass
    }

    override fun valideFields(email: String, fullname: String, pass: String, confirmPass: String)
            : Boolean
    {
        return (email.isEmpty() || fullname.isEmpty() || pass.isEmpty() || confirmPass.isEmpty())
    }

}