package adam.krapso.fetr.spamcallignore.Services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import adam.krapso.fetr.spamcallignore.Constants;

/**
 * Created by krapso on 28-Jan-18.
 */

public class PermissionService {

    public static boolean checkPermissions(Activity activity){
        boolean result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)+
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)+
                ContextCompat.checkSelfPermission(activity, Manifest.permission.PROCESS_OUTGOING_CALLS)+
                ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED;
        if (result) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.PROCESS_OUTGOING_CALLS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.INTERNET
                }, Constants.MULTIPLE_PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.INTERNET
                }, Constants.MULTIPLE_PERMISSIONS_REQUEST);
            }
        }
        return !result;
    }
}
