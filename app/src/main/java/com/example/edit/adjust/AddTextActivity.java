
package com.example.edit.adjust;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.edit.R;
import com.example.edit.colorpicker.SelectColorPopup;
import com.example.edit.utils.Constants;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.jarlen.photoedit.operate.OperateConstants;
import cn.jarlen.photoedit.operate.OperateUtils;
import cn.jarlen.photoedit.operate.OperateView;
import cn.jarlen.photoedit.operate.TextObject;

/**
 * Nhập kí tự
 *
 * @author jarlen
 */
public class AddTextActivity extends Activity implements View.OnClickListener {

    private LinearLayout content_layout;
    private OperateView operateView;
    private String camera_path;
    private String mPath = null;
    OperateUtils operateUtils;
    private ImageButton btn_ok, btn_cancel;
    private Button add, color, family, moren, faceby, facebygf;
    private LinearLayout face_linear;
    private SelectColorPopup menuWindow;
    private String typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtext);
        Intent intent = getIntent();
        camera_path = intent.getStringExtra("camera_path");
        operateUtils = new OperateUtils(this);
        initView();
        // Trì hoãn mỗi lần trì hoãn 10 mili giây và thực hiện cứ sau 1 giây
        timer.schedule(task, 10, 1000);
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
                    fillContent();
                }
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    private void initView() {
        content_layout = (LinearLayout) findViewById(R.id.mainLayout);
        face_linear = (LinearLayout) findViewById(R.id.face_linear);
        btn_ok = (ImageButton) findViewById(R.id.btn_ok);
        btn_cancel = (ImageButton) findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        add = (Button) findViewById(R.id.addtext);
        color = (Button) findViewById(R.id.color);
        family = (Button) findViewById(R.id.family);
        add.setOnClickListener(this);
        color.setOnClickListener(this);
        family.setOnClickListener(this);

        moren = (Button) findViewById(R.id.moren);
        moren.setTypeface(Typeface.DEFAULT);
        faceby = (Button) findViewById(R.id.faceby);
        faceby.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"
                + OperateConstants.FACE_BY + ".ttf"));
        facebygf = (Button) findViewById(R.id.facebygf);
        facebygf.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/"
                + OperateConstants.FACE_BYGF + ".ttf"));
        moren.setOnClickListener(this);
        faceby.setOnClickListener(this);
        facebygf.setOnClickListener(this);

    }

    private void fillContent() {
        Bitmap resizeBmp = BitmapFactory.decodeFile(camera_path);
        operateView = new OperateView(AddTextActivity.this, resizeBmp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                resizeBmp.getWidth(), resizeBmp.getHeight());
        operateView.setLayoutParams(layoutParams);
        content_layout.addView(operateView);
        operateView.setMultiAdd(true); //Đặt tham số này, bạn có thể thêm nhiều văn bản
    }

    private void btnSave() {
        operateView.save();
        Bitmap bmp = getBitmapByView(operateView);
        if (bmp != null) {
            mPath = saveBitmap(bmp, "saveTemp");
            Intent okData = new Intent();
            okData.putExtra("camera_path", mPath);
            setResult(RESULT_OK, okData);
            this.finish();
        }
    }

    // Chuyển đổi hình ảnh của Chế độ xem mẫu thành Bitmap
    public Bitmap getBitmapByView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    // Lưu ảnh đã tạo vào bộ nhớ
    public String saveBitmap(Bitmap bitmap, String name) {
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

    private void addfont() {
        final EditText editText = new EditText(AddTextActivity.this);
        new AlertDialog.Builder(AddTextActivity.this).setView(editText)
                .setPositiveButton("mục đích", new DialogInterface.OnClickListener() {
                    @Override
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        String string = editText.getText().toString();
                        // TextObject textObj =
                        // operateUtils.getTextObject(string);
                        // if (textObj != null) {
                        // textObj.setTypeface(OperateConstants.FACE_BY);
                        // textObj.commit();
                        // }

                        TextObject textObj = operateUtils.getTextObject(string,
                                operateView, 5, 150, 100);
                        if (textObj != null) {
                            if (menuWindow != null) {
                                textObj.setColor(menuWindow.getColor());
                            }
                            textObj.setTypeface(typeface);
                            textObj.commit();
                            operateView.addItem(textObj);
                            operateView.setOnListener(new OperateView.MyListener() {
                                public void onClick(TextObject tObject) {
                                    alert(tObject);
                                }
                            });
                        }
                    }
                }).show();
    }

    private void alert(final TextObject tObject) {

        final EditText editText = new EditText(AddTextActivity.this);
        new AlertDialog.Builder(AddTextActivity.this).setView(editText)
                .setPositiveButton("mục đích", new DialogInterface.OnClickListener() {
                    @Override
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        String string = editText.getText().toString();
                        tObject.setText(string);
                        tObject.commit();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color:
                menuWindow = new SelectColorPopup(AddTextActivity.this,
                        AddTextActivity.this);
                // Cửa sổ hiển thị
                menuWindow.showAtLocation(
                        AddTextActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.submit:
                menuWindow.dismiss();
                break;
            case R.id.family:
                if (face_linear.getVisibility() == View.GONE) {
                    face_linear.setVisibility(View.VISIBLE);
                } else {
                    face_linear.setVisibility(View.GONE);
                }
                break;
            case R.id.addtext:
                addfont();
                break;
            case R.id.btn_ok:
                btnSave();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.moren:
                typeface = null;
                face_linear.setVisibility(View.GONE);
                break;
            case R.id.faceby:
                typeface = OperateConstants.FACE_BY;
                face_linear.setVisibility(View.GONE);
                break;
            case R.id.facebygf:
                typeface = OperateConstants.FACE_BYGF;
                face_linear.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
}
