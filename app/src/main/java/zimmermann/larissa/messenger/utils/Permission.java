package zimmermann.larissa.messenger.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laris on 17/01/2018.
 */

public class Permission {

    private static final int MARSHMALLOW_VERSION = 23;

    public static boolean checkPermissions(Activity activity, String[] permissions, int requestCode) {
        if(Build.VERSION.SDK_INT >= MARSHMALLOW_VERSION) {
            List<String> permissionList = new ArrayList<String>();

            for(String permission : permissions) {
                boolean checkPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if(checkPermission == false) {
                    permissionList.add(permission);
                }
            }

            if(permissionList.isEmpty()) return true;

            String[] remainPermissions = new String[permissionList.size()];
            permissionList.toArray(remainPermissions);

            ActivityCompat.requestPermissions(activity, remainPermissions, requestCode);
        }
        return true;
    }

}
