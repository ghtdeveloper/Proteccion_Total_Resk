package savemenow.es.protecciontotalresk.android.model.contacts


/**
 ****Created by: Edison Martinez,
 ****Date:23,Tuesday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class Contacts {

    var name:String? = null
    var phoneNumber : String? = null


    @JvmName("setname")
    fun setName(name:String)
    {
        this.name = name
    }

    @JvmName("setPhone")
    fun setPhone(phone:String)
    {
        this.phoneNumber = phone
    }

    @JvmName("getNumbers")
    fun getNumbers(): String{
        return phoneNumber.toString()
    }

    @JvmName("getName1")
    fun getName(): String{
        return name.toString()
    }

}