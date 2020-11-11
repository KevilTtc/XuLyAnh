package com.example.edit.adjust;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edit.R;
import com.example.edit.model.AbsRuntimePermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirtActivity extends AbsRuntimePermission implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 10;
    ImageButton btnCamera,btnEffect,btnInfo,btnExit;
    TextView txtCamera,txtEffect,txtInfo,txtExit;
    Typeface font;
    Dialog popupInfo;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firt_activity);

        initPermission();
        addControls();
        addEvents();

    }

    private void addEvents() {
        btnInfo.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnEffect.setOnClickListener(this);
        btnExit.setOnClickListener(this);

    }

    private void addControls() {
        popupInfo = new Dialog(FirtActivity.this);

        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
        btnEffect = (ImageButton) findViewById(R.id.btnEffect);
        btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnInfo = (ImageButton) findViewById(R.id.btnInfo);

        txtExit = (TextView) findViewById(R.id.txtExit);
        txtCamera = (TextView) findViewById(R.id.txtCamera);
        txtEffect = (TextView) findViewById(R.id.txtEffect);
        txtInfo = (TextView) findViewById(R.id.txtInfo);



        // font = Typeface.createFromAsset(this.getAssets(),"fonts/amita-regular.ttf");
        // txtCamera.setTypeface(font);
        //  txtInfo.setTypeface(font);
        // txtEffect.setTypeface(font);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        // Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(FirtActivity.this, MainActivity.class);
                intent.setData(imageUri);
                startActivity(intent);

            }
        }
    }

    private void initPermission() {
        requestAppPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                R.string.request_permission,REQUEST_PERMISSION);

    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    private  void exitApp(){
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    private void doOpenEffectActivity() {
        Intent intent=new Intent(FirtActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void showPopup(View v) {
        TextView txtClosePop;

        popupInfo.setContentView(R.layout.info_popup);
        txtClosePop =(TextView) popupInfo.findViewById(R.id.txtClosePopup);

        txtClosePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupInfo.dismiss();
            }
        });
        popupInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupInfo.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnEffect:
                doOpenEffectActivity();
                break;
            case  R.id.btnCamera:
              //  takePicture(v);
                break;
            case  R.id.btnInfo:
                showPopup(v);
                break;
            case  R.id.btnExit:
                exitApp();
                break;
            default:
                break;
        }
    }
}

