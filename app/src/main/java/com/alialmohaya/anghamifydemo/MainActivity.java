package com.alialmohaya.anghamifydemo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alialmohaya.anghamifydemo.Activities.SearchActivity;
import com.alialmohaya.anghamifydemo.Activities.SongViewActivity;
import com.alialmohaya.anghamifydemo.Utils.PermissionManger;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PermissionManger.checkWritePremission(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        finish();
    }
}
