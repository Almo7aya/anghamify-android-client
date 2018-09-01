package com.alialmohaya.anghamifydemo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class PermissionChecker {

    public static final int PERMISSION_REQ_CODE = 1240;

    public static boolean checkIfPermissionWrite(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requiestWritePermissiom(context);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkIfThereIsAnSDCardAndWriteable() throws Exception {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            throw new Exception("SDCard is read only");
        } else {
            throw new Exception("SDCard is not there");
        }
    }

    private static void requiestWritePermissiom(Context context) {
        Activity songActivity = (Activity) context;
        Toast.makeText(context, "Creating a Req", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(songActivity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
    }

}
