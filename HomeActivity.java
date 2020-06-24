package com.example.mydemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private String urlString = "http://gdown.baidu.com/data/wisegame/283e9789be54e63c/weixin_1560.apk";
    private ProgressBar progressBar;
    public static final int APPLY_PERMISSIONS_CODE = 0x11;
    private ArrayList<String> needApplyPermissions = new ArrayList<>();
    /**
     * 需要进行检测的权限数组  外部存储必须授权 其他选择授权
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        init();
    }

    public void download(View view){
        new Thread(){
            @Override
            public void run() {
                doDownload();
            }
        }.start();
    }

    public void doDownload(){
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();
            // download the file
            input = connection.getInputStream();
            String savePAth=Environment.getExternalStorageDirectory()+"/ww_test";
            File file1=new File(savePAth);
            if (!file1.exists()) {
                file1.mkdir();
            }
            String savePathString= Environment.getExternalStorageDirectory()+"/ww_test/"+"weixin.apk";
            File file =new File(savePathString);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            output = new FileOutputStream(file);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                total += count;
                // publishing the progress....
                if (fileLength > 0){
                    Log.e("HomeActivity",(int) (total * 100 / fileLength)+"");
                    progressBar.setProgress((int) (total * 100 / fileLength));
                }
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.e("HomeActivity","download has Exception");
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
    }

    public void init() {
        applyPermissions();
    }

    public void applyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < needPermissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(this,
                        needPermissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    needApplyPermissions.add(needPermissions[i]);
                }
            }
            if (needApplyPermissions.contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                String[] toBeStored = new String[needApplyPermissions.size()];
                ActivityCompat.requestPermissions(this,
                        needApplyPermissions.toArray(toBeStored), APPLY_PERMISSIONS_CODE
                );
                needApplyPermissions.clear();
                return;
            } else {

            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APPLY_PERMISSIONS_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //外部存储同意授权
                Log.e("homeactivity","权限允许");
            } else {
                Log.e("homeactivity","权限拒绝");
            }

        }
    }
}
