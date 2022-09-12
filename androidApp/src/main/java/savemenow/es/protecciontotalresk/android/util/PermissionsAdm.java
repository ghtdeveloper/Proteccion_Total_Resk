
package savemenow.es.protecciontotalresk.android.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class PermissionsAdm
{

    /**
     *
     * @param activity = actividad
     * @param requestCode = codigo solicitud
     * @param permisos = listo de permisos requeridos
     * @return = flag
     */
    public static boolean requestPermission(Activity activity, int requestCode, String... permisos)
    {
        boolean otorgado = true;
        ArrayList<String> permisosRequeridos = new ArrayList<>();

        for(String s : permisos)
        {
            int permisosVerificado = ContextCompat.checkSelfPermission(activity,s);
            boolean hasPermission = (permisosVerificado == PackageManager.PERMISSION_GRANTED);
            otorgado &= hasPermission;
            if(!hasPermission)
            {
                permisosRequeridos.add(s);
            }
        }//Fin del for

        if(otorgado)
        {
            return true;
        }else{
            ActivityCompat.requestPermissions(activity,permisosRequeridos.toArray(new String[0]),requestCode);
            return false;
        }
    }//Fin del metodo requestPermission


    /**
     *
     * @param requestCode = codigo solicitud
     * @param permisoCode = codigo del permiso
     * @param grantResuelt = resultado de los permisos otorgodas
     * @return = return flag
     */

    public  static  boolean grantedPermission(int requestCode, int permisoCode, int[] grantResuelt)
    {

        return requestCode == permisoCode && grantResuelt.length > 0 && grantResuelt[0] ==
                PackageManager.PERMISSION_GRANTED;

    }//Fin del metodopermisosOtorgados


}//Fin de la class PermisosUtil
