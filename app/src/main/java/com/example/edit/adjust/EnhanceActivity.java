
package com.example.edit.adjust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.edit.R;

import cn.jarlen.photoedit.enhance.PhotoEnhance;
import cn.jarlen.photoedit.utils.FileUtils;

/**
 * Nâng cao
 * @author jarlen
 */
public class EnhanceActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    private ImageButton cancelBtn, okBtn;

    private ImageView pictureShow;

    private SeekBar saturationSeekBar, brightnessSeekBar, contrastSeekBar;

    private String imgPath;
    private Bitmap bitmapSrc;

    private PhotoEnhance photoEnhance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enhance);

        Intent intent = getIntent();

        imgPath = intent.getStringExtra("camera_path");
        bitmapSrc = BitmapFactory.decodeFile(imgPath);

        initView();
        pictureShow.setImageBitmap(bitmapSrc);
    }

    private void initView()
    {
        cancelBtn = (ImageButton) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        okBtn = (ImageButton) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);

        pictureShow = (ImageView) findViewById(R.id.enhancePicture);

        saturationSeekBar = (SeekBar) findViewById(R.id.saturation);
        saturationSeekBar.setMax(255);
        saturationSeekBar.setProgress(128);
        saturationSeekBar.setOnSeekBarChangeListener(this);

        brightnessSeekBar = (SeekBar) findViewById(R.id.brightness);
        brightnessSeekBar.setMax(255);
        brightnessSeekBar.setProgress(128);
        brightnessSeekBar.setOnSeekBarChangeListener(this);

        contrastSeekBar = (SeekBar) findViewById(R.id.contrast);
        contrastSeekBar.setMax(255);
        contrastSeekBar.setProgress(128);
        contrastSeekBar.setOnSeekBarChangeListener(this);

        photoEnhance = new PhotoEnhance(bitmapSrc);

    }

    private int pregress = 0;
    private Bitmap bit = null;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser)
    {

        pregress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        // TODO Auto-generated method stub

        int type = 0;

        switch (seekBar.getId())
        {
            // bão hòa
            case R.id.saturation :
                photoEnhance.setSaturation(pregress);
                type = photoEnhance.Enhance_Saturation;

                break;
            //độ sáng
            case R.id.brightness :
                photoEnhance.setBrightness(pregress);
                type = photoEnhance.Enhance_Brightness;

                break;
            //tương phản
            case R.id.contrast :
                photoEnhance.setContrast(pregress);
                type = photoEnhance.Enhance_Contrast;

                break;

            default :
                break;
        }

        bit = photoEnhance.handleImage(type);
        pictureShow.setImageBitmap(bit);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.btn_ok :

                FileUtils.writeImage(bit, imgPath, 100);
                Intent okData = new Intent();
                okData.putExtra("camera_path", imgPath);
                setResult(RESULT_OK, okData);
                recycle();
                this.finish();
                break;

            case R.id.btn_cancel :

                Intent cancelData = new Intent();
                setResult(RESULT_CANCELED, cancelData);
                recycle();
                this.finish();
                break;

            default :
                break;
        }

    }

    private void recycle()
    {
        if (bitmapSrc != null)
        {
            bitmapSrc.recycle();
            bitmapSrc = null;
        }

        if (bit != null)
        {
            bit.recycle();
            bit = null;
        }
    }
}
