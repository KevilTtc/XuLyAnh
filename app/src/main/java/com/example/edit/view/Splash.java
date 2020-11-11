package com.example.edit.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edit.R;
import com.example.edit.adjust.FirtActivity;
import com.example.edit.adjust.MainActivity;

public class Splash extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView txtLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        addControls();
        addEvents();

    }


    private void addEvents() {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.transition);

        txtLogo.startAnimation(anim);
        imgLogo.startAnimation(anim);

        final Intent i = new Intent(this, FirtActivity.class);
        Thread timer = new Thread(){
            public void run ()
            {
                try{
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }

        };
        timer.start();
    }

    private void addControls() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
    }
}
