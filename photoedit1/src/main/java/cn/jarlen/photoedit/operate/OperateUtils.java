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
package cn.jarlen.photoedit.operate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;


import cn.jarlen.photoedit1.R;

/**
 * Thêm công cụ văn bản và hình ảnh
 */
public class OperateUtils {
    private Activity activity;
    private int screenWidth;// Chiều rộng của màn hình điện thoại (pixel)
    private int screenHeight;//  Chiều cao của màn hình điện thoại (pixel)

    public static final int LEFTTOP = 1;
    public static final int RIGHTTOP = 2;
    public static final int LEFTBOTTOM = 3;
    public static final int RIGHTBOTTOM = 4;
    public static final int CENTER = 5;

    public OperateUtils(Activity activity) {
        this.activity = activity;
        if (screenWidth == 0) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels; // Chiều rộng màn hình (pixel)
            screenHeight = metric.widthPixels; //  Chiều rộng màn hình (pixel)
        }
    }

    /**
     * Lấy hình ảnh theo đường dẫn và nén nó để thích ứng với chế độ xem
     *
     * @param filePath    Đường dẫn hình ảnh
     * @param contentView Chế độ xem đã điều chỉnh
     * @return Bitmap        Ảnh nén
     */
    public Bitmap compressionFiller(String filePath, View contentView) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if (scale != 0) {
            int bitmapheight = bitmap.getHeight();
            int bitmapwidth = bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // Tỷ lệ chiều dài và chiều rộng phóng to và thu nhỏ
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
                    bitmapheight, matrix, true);
        } else {
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }

    /**
     * Theo nén hình ảnh và thích ứng với chế độ xem
     *
     * @param bitmap      Nén ảnh
     * @param contentView Chế độ xem đã điều chỉnh
     * @return               Ảnh nén
     */
    public Bitmap compressionFiller(Bitmap bitmap, View contentView) {
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if (scale != 0) {
            int bitmapheight = bitmap.getHeight();
            int bitmapwidth = bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // Tỷ lệ chiều dài và chiều rộng phóng to và thu nhỏ
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
                    bitmapheight, matrix, true);
        } else {
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }

    /**
     *
     *Thêm phương thức văn bản
     * @param text        Đã thêm văn bản
     * @return TextObject Đối tượng hình ảnh phông chữ được trả lại
     */
    public TextObject getTextObject(String text) {
        TextObject textObj = null;
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(activity, "Vui lòng thêm văn bản", Toast.LENGTH_SHORT).show();
            return null;
        }

        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.delete);

        textObj = new TextObject(activity, text, 150, 150, rotateBm, deleteBm);
        textObj.setTextObject(true);
        return textObj;
    }

    /**
     *
     *Cách thêm hình ảnh
     * @param text        Nội dung văn bản
     * @param operateView Đối tượng Chế độ xem vùng chứa
     * @param quadrant    Khu vực hình ảnh cần được hiển thị (1, phía trên bên trái, 2, phía trên bên phải, 3, phía dưới bên trái, 4, phía dưới bên phải, 5, chính giữa)
     * @param x           Tọa độ X từ ranh giới
     * @param y           Tọa độ Y từ ranh giới
     * @return
     */
    public TextObject getTextObject(String text, OperateView operateView,
                                    int quadrant, int x, int y) {
        TextObject textObj = null;
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(activity, "Vui lòng thêm văn bản", Toast.LENGTH_SHORT).show();
            return null;
        }
        int width = operateView.getWidth();
        int height = operateView.getHeight();
        switch (quadrant) {
            case LEFTTOP:
                break;
            case RIGHTTOP:
                x = width - x;
                break;
            case LEFTBOTTOM:
                y = height - y;
                break;
            case RIGHTBOTTOM:
                x = width - x;
                y = height - y;
                break;
            case CENTER:
                x = width / 2;
                y = height / 2;
                break;
            default:
                break;
        }
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.delete);
        textObj = new TextObject(activity, text, x, y, rotateBm, deleteBm);
        textObj.setTextObject(true);
        return textObj;
    }

    /**
     *
     *Thêm phương pháp hình ảnh
     * @param srcBmp    Hình ảnh đang được chế tác
     * @return
     */

    public ImageObject getImageObject(Bitmap srcBmp) {
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.delete);
        ImageObject imgObject = new ImageObject(srcBmp, rotateBm, deleteBm);
        Point point = new Point(20, 20);
        imgObject.setPoint(point);
        return imgObject;
    }

    /**
     * Thêm phương pháp hình ảnh
     *
     * @param srcBmp      Hình ảnh đang được chế tác
     * @param operateView Đối tượng Chế độ xem vùng chứa
     * @param quadrant    Khu vực hình ảnh cần được hiển thị (1, phía trên bên trái, 2, phía trên bên phải, 3, phía dưới bên trái, 4, phía dưới bên phải, 5, chính giữa)
     * @param x           Tọa độ X từ ranh giới
     * @param y           Tọa độ Y từ ranh giới
     * @return
     */

    public ImageObject getImageObject(Bitmap srcBmp, OperateView operateView,
                                      int quadrant, int x, int y) {
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.delete);
        int width = operateView.getWidth();
        int height = operateView.getHeight();
//		int srcWidth = srcBmp.getWidth();
//		int srcHeight = srcBmp.getHeight();
//		if (height > width)
//		{
//			if (srcHeight > srcWidth)
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, height / 3 * srcWidth
//						/ srcHeight, height / 3);
//			} else
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, width / 3, width / 3
//						* srcHeight / srcWidth);
//			}
//		} else
//		{
//			if (srcHeight > srcWidth)
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, height / 2 * srcWidth
//						/ srcHeight, height / 2);
//			} else
//			{
//				srcBmp = ImageUtils.ResizeBitmap(srcBmp, width / 3, width / 3
//						* srcHeight / srcWidth);
//			}
//		}
        switch (quadrant) {
            case LEFTTOP:
                break;
            case RIGHTTOP:
                x = width - x;
                break;
            case LEFTBOTTOM:
                y = height - y;
                break;
            case RIGHTBOTTOM:
                x = width - x;
                y = height - y;
                break;
            case CENTER:
                x = width / 2;
                y = height / 2;
                break;
            default:
                break;
        }
        ImageObject imgObject = new ImageObject(srcBmp, x, y, rotateBm,
                deleteBm);
        Point point = new Point(20, 20);
        imgObject.setPoint(point);
        return imgObject;
    }

}
