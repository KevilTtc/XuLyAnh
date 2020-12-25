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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.edit.R;
import com.example.edit.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import cn.jarlen.photoedit.operate.OperateUtils;
import cn.jarlen.photoedit.utils.FileUtils;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * main
 *
 * @author jarlen
 */
public class MainActivity extends Activity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener, EasyPermissions.PermissionCallbacks {

    private LinearLayout content_layout;
    private Button addPictureFromPhotoBtn;
    private Button addPictureFromCameraBtn;
    private ImageView pictureShow;
    private Button testBtn;
    private Class<?> intentClass;
    private int intentType = 0;

    /* Được sử dụng để xác định yêu cầu đối với chức năng camera activity */
    private static final int CAMERA_WITH_DATA = 3023;

    /*Được sử dụng để xác định yêu cầu gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;

    /* khung */
    private static final int PHOTO_FRAME_WITH_DATA = 3024;

    /* Khảm */
    private static final int PHOTO_MOSAIC_WITH_DATA = 3025;

    /* Vẽ tranh lên tường */
    private static final int PHOTO_DRAW_WITH_DATA = 3026;

    /* Cắt  */
    private static final int PHOTO_CROP_WITH_DATA = 3027;

    /*Bộ lọc  */
    private static final int PHOTO_FILTER_WITH_DATA = 3028;

    /* Nâng cao */
    private static final int PHOTO_ENHANCE_WITH_DATA = 3029;

    /* Quay */
    private static final int PHOTO_REVOLVE_WITH_DATA = 3030;

    /* Sự biến dạng hình ảnh */
    private static final int PHOTO_WARP_WITH_DATA = 3031;

    /* Thêm hình mờ vào hình ảnh */
    private static final int PHOTO_ADD_WATERMARK_DATA = 3032;
    /*Nhập kí tự  */
    private static final int PHOTO_ADD_TEXT_DATA = 3033;

    /* Giao diện thử nghiệm  */
    private static final int PHOTO_TEST_TEXT_DATA = 3034;

    /* Ảnh chụp bằng máy ảnh */
    private File mCurrentPhotoFile;
    private String photoPath = null, tempPhotoPath, camera_path;

    private int scale = 2;
    int width = 0;

    OperateUtils operateUtils;

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_launcher);
        toolbar.setTitle("Photo Editor");
        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // Chiều rộng màn hình (pixel)
        pictureShow = (ImageView) findViewById(R.id.pictureShow);
        testBtn = (Button) findViewById(R.id.testBtn);
        testBtn.setOnClickListener(this);
        content_layout = (LinearLayout) findViewById(R.id.mainLayout);
        addPictureFromPhotoBtn = (Button) findViewById(R.id.addPictureFromPhoto);
        addPictureFromPhotoBtn.setOnClickListener(this);
        addPictureFromCameraBtn = (Button) findViewById(R.id.addPictureFromCamera);
        addPictureFromCameraBtn.setOnClickListener(this);
        operateUtils = new OperateUtils(this);
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 1, permissions).build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPictureFromCamera:
                getPictureFormCamera();
                break;
            case R.id.addPictureFromPhoto:
                getPictureFromPhoto();
                break;
            case R.id.testBtn:
                if (photoPath == null) {
                    Toast.makeText(MainActivity.this, "Hãy chọn một bức tranh",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (intentClass == null) {
                    Toast.makeText(MainActivity.this, "Vui lòng hình ảnh loại hoạt động",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // Chuyển đường dẫn hình ảnh photoPath đến mô-đun cần fix
                Intent photoFrameIntent = new Intent(MainActivity.this,
                        intentClass);
                photoFrameIntent.putExtra("camera_path", camera_path);
                MainActivity.this.startActivityForResult(photoFrameIntent,
                        intentType);
                break;
            default:
                break;
        }

    }

    /* Lấy ảnh từ album */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    /* Lấy ảnh từ máy ảnh */
    private void getPictureFormCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


         intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        tempPhotoPath = FileUtils.DCIMCamera_PATH + FileUtils.getNewFileName()
                + ".jpg";

        mCurrentPhotoFile = new File(tempPhotoPath);

        if (!mCurrentPhotoFile.exists()) {
            try {
                mCurrentPhotoFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        startActivityForResult(intent, CAMERA_WITH_DATA);

    }

    private void compressed() {
        Bitmap resizeBmp = operateUtils.compressionFiller(photoPath,
                content_layout);
        pictureShow.setImageBitmap(resizeBmp);
        camera_path = SaveBitmap(resizeBmp, "saveTemp");
    }

    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (content_layout.getWidth() != 0) {
                    Log.i("LinearLayoutW", content_layout.getWidth() + "");
                    Log.i("LinearLayoutH", content_layout.getHeight() + "");
                    // Hủy hẹn giờ
                    timer.cancel();
                    compressed();
                }
            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    // Lưu ảnh đã tạo vào bộ nhớ
    public String SaveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(Constants.filePath);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(Constants.filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CAMERA_WITH_DATA:

                Bitmap image = (Bitmap) data.getExtras().get("data");
                pictureShow.setImageBitmap(image);

                break;

            case PHOTO_PICKED_WITH_DATA:

                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();

                // Trì hoãn mỗi lần trì hoãn 10 mili giây và thực hiện cứ sau 1 giây một lần
                if (content_layout.getWidth() == 0) {
                    timer.schedule(task, 10, 1000);
                } else {
                    compressed();
                }

                break;
            case PHOTO_FRAME_WITH_DATA:
            case PHOTO_MOSAIC_WITH_DATA:
            case PHOTO_DRAW_WITH_DATA:
            case PHOTO_CROP_WITH_DATA:
            case PHOTO_FILTER_WITH_DATA:
            case PHOTO_ENHANCE_WITH_DATA:
            case PHOTO_REVOLVE_WITH_DATA:
            case PHOTO_WARP_WITH_DATA:
            case PHOTO_ADD_WATERMARK_DATA:
            case PHOTO_ADD_TEXT_DATA:
            case PHOTO_TEST_TEXT_DATA:

                String resultPath = data.getStringExtra("camera_path");
                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
                pictureShow.setImageBitmap(resultBitmap);
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (photoPath == null) {
            Toast.makeText(MainActivity.this, "Hãy chọn một bức tranh", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_filter:
                //Lọc filter
                intentClass = ImageFilterActivity.class;
                intentType = PHOTO_FILTER_WITH_DATA;

                break;
            case R.id.action_wrap:
                //uốn,kéo img
                intentClass = WarpActivity.class;
                intentType = PHOTO_WARP_WITH_DATA;
                break;
            case R.id.action_crop:
                // cắt ảnh
                intentClass = ImageCropActivity.class;
                intentType = PHOTO_CROP_WITH_DATA;
                break;
            case R.id.action_draw:
                intentClass = DrawBaseActivity.class;
                intentType = PHOTO_DRAW_WITH_DATA;
                break;
//            case R.id.action_frame:
//                intentClass = PhotoFrameActivity.class;
//                intentType = PHOTO_FRAME_WITH_DATA;
//                break;
            case R.id.action_addtv:
                intentClass = AddTextActivity.class;
                intentType = PHOTO_ADD_TEXT_DATA;
                break;
//            case R.id.action_addwm:
//                intentClass = AddWatermarkActivity.class;
//                intentType = PHOTO_ADD_WATERMARK_DATA;
//                break;
//            case R.id.action_mosaic:
//                intentClass = MosaicActivity.class;
//                intentType = PHOTO_MOSAIC_WITH_DATA;
//                break;

            case R.id.action_enchance:
                intentClass = EnhanceActivity.class;
                intentType = PHOTO_ENHANCE_WITH_DATA;
                break;
            case R.id.action_rotate:
                intentClass = RevolveActivity.class;
                intentType = PHOTO_REVOLVE_WITH_DATA;
                break;
            default:
                intentClass = null;
                intentType = 0;
                break;
        }

        Toast.makeText(MainActivity.this, "Vui lòng nhấp vào nút kiểm tra", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(MainActivity.this, "Vui lòng cấp các quyền cần thiết", Toast.LENGTH_SHORT).show();
        finish();
    }
}
