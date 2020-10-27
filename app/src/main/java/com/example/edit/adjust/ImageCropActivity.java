/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.edit.adjust;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.edit.R;

import cn.jarlen.photoedit.crop.CropImageType;
import cn.jarlen.photoedit.crop.CropImageView;
import cn.jarlen.photoedit.utils.FileUtils;

/**
 * Cắt
 * @author jarlen
 */
public class ImageCropActivity extends Activity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private Toolbar mToolbar;

    private CropImageView cropImage;

    private String mPath = null;

    private ImageButton cancleBtn, okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_crop);
        mToolbar.setOnMenuItemClickListener(this);

        Intent intent = getIntent();

        mPath = intent.getStringExtra("camera_path");
        Bitmap bit = BitmapFactory.decodeFile(mPath);

        cropImage = (CropImageView) findViewById(R.id.cropmageView);

        cancleBtn = (ImageButton) findViewById(R.id.btn_cancel);
        cancleBtn.setOnClickListener(this);
        okBtn = (ImageButton) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);

        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        cropImage.setCropOverlayCornerBitmap(hh);
        cropImage.setImageBitmap(bit);

        // Bitmap bit =
        // BitmapFactory.decodeResource(this.getResources(),R.drawable.hi0);

        cropImage.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// Hiển thị lưới khi chạm vào

        cropImage.setFixedAspectRatio(false);// Cắt miễn phí

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_freedom:
                cropImage.setFixedAspectRatio(false);
                break;
            case R.id.action_1_1:
                cropImage.setFixedAspectRatio(true);
                cropImage.setAspectRatio(10, 10);
                break;
            case R.id.action_3_2:
                cropImage.setFixedAspectRatio(true);
                cropImage.setAspectRatio(30, 20);
                break;

            case R.id.action_4_3:
                cropImage.setFixedAspectRatio(true);
                cropImage.setAspectRatio(40, 30);
                break;
            case R.id.action_16_9:
                cropImage.setFixedAspectRatio(true);
                cropImage.setAspectRatio(160, 90);
                break;
            case R.id.action_rotate:
                cropImage.rotateImage(90);
                break;
            case R.id.action_up_down:
                cropImage.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);
                break;
            case R.id.action_left_right:
                cropImage.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);
                break;
            case R.id.action_crop:
                Bitmap cropImageBitmap = cropImage.getCroppedImage();
                Toast.makeText(
                        this,
                        "Đã lưu vào album; kích thước cắt là " + cropImageBitmap.getWidth() + " x "
                                + cropImageBitmap.getHeight(),
                        Toast.LENGTH_SHORT).show();
                FileUtils.saveBitmapToCamera(this, cropImageBitmap, "crop.jpg");
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_cancel:
                Intent cancelData = new Intent();
                setResult(RESULT_CANCELED, cancelData);
                this.finish();
                break;
            case R.id.btn_ok:
                Bitmap bit = cropImage.getCroppedImage();
                FileUtils.writeImage(bit, mPath, 100);

                Intent okData = new Intent();
                okData.putExtra("camera_path", mPath);
                setResult(RESULT_OK, okData);
                this.finish();
                break;
            default:

                break;
        }
    }
}
