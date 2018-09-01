package com.alialmohaya.anghamifydemo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

public class PermissionManger {

    public static int WRITE_PERMISSION_CODE = 123;

    public static boolean checkWritePremission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !=
                    PackageManager.PERMISSION_GRANTED) {
                Activity myActivityFormContext = (Activity) context;
                myActivityFormContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PermissionManger.WRITE_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
