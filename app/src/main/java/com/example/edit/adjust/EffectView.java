package com.example.edit.adjust;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;


/**
 *
 */

public class EffectView extends AppCompatImageView {
    // Debug Logcat
    private static final String TAG = "Effect View";


    private static Matrix matrix = new Matrix();
    private static Matrix savedMatrix = new Matrix();
    private final float[] valuesMatrix = new float[9];

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final int DRAW = 3;

    private static  final  float MAX_ZOOM = 5.0f;
    private static  final  float MIN_ZOOM =0.5f;

    private int mode = NONE;

    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;

    // true ->cho phép zoom và drag trên view
    // false -> ko cho phép ( drawing )
    private boolean enableZoomDrag;
    private boolean enableDraw;

    //
    private double mRotatedWidth;
    private double mRotatedHeight;

    // ------- test draw undo redo

    private Path mPath;
    private Paint mPaint;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> undonePaths = new ArrayList<>();
    private float mX = 0, mY = 0;
    private static final float TOUCH_TOLERANCE = 4;

    // canvas ve len man hinh
    private Canvas mCanvas;
    // drawable tu view -> get height witdh config
    private BitmapDrawable drawableBitmap = null;
    // bitmap -> view
    private Bitmap originalBitmap = null;
    // bitamp ve len view
    private Bitmap drawingBitmap = null;

    public EffectView(Context context) {
        super(context);
        init();
    }

    public EffectView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public BitmapDrawable getDrawableBitmap() {
        return drawableBitmap;
    }

    // ko su dung
    public void setDrawableBitmap(BitmapDrawable drawableBitmap) {
        this.drawableBitmap = drawableBitmap;
    }

    public boolean isEnableDraw() {
        return enableDraw;
    }

    public void setEnableDraw(boolean enableDraw) {
        this.enableDraw = enableDraw;
        // khi chuyen ve mode zoom drad -> save lai bitmap ( save lun cac path da ve len man hinh )
        if(!enableDraw && this.getDrawable()!=null && mode==DRAW ){
            drawableBitmap = (BitmapDrawable) this.getDrawable();
            //originalBitmap = drawableBitmap.getBitmap();
            drawingBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());

            Log.d("draw", String.valueOf(drawableBitmap.getBitmap().getWidth()));
            Log.d("draw", String.valueOf(drawableBitmap.getBitmap().getHeight()));
            mCanvas = new Canvas(drawingBitmap);

            // ve bitmap va cac path lai
            mCanvas.drawBitmap(originalBitmap,getImageMatrix(),null);

            for (Path p : paths){
                mCanvas.drawPath(p, mPaint);
            }


            setOriginalBitmap(drawingBitmap);
            paths = new ArrayList<>();
            undonePaths = new ArrayList<>();
            paths.add(mPath);
            mode = NONE;

        }






    }

    public boolean isEnableZoomDrag() {
        return enableZoomDrag;
    }

    public void setEnableZoomDrag(boolean enableZoomDrag) {
        this.enableZoomDrag = enableZoomDrag;

    }

    public Bitmap getOriginalBitmap() {
        return originalBitmap;
    }

    public void setOriginalBitmap(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
        setImageBitmap(originalBitmap);

        if(originalBitmap!=null) {

            drawableBitmap = (BitmapDrawable) getDrawable();
            drawingBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
            mCanvas = new Canvas(drawingBitmap);

            //  chinh lai image center vie\
                Drawable image = getDrawable();
                RectF rectfView = new RectF(0, 0, this.getWidth(), this.getHeight());
                RectF rectfImage = new RectF(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                matrix.setRectToRect(rectfImage, rectfView, Matrix.ScaleToFit.CENTER);

                setImageMatrix(matrix);


        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (enableDraw) {
            super.onDraw(canvas);
            // hien thi cac path da ve
            for (Path p : paths) {
                canvas.drawPath(p, mPaint);
            }

        } else
            super.onDraw(canvas);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if(originalBitmap == null) return false;

        if(enableZoomDrag  && !enableDraw){
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                // khi cham vao man hinh
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                //Khi cham vao 1 diem
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist >10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:

                    mode = NONE;

                    break;

                // ngon tay di chuyen tren man hinh
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {

                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
//                    limitDrag(matrix);


//                    matrix.set(savedMatrix);
//
//                    matrix.getValues(matrixValues);
//                    matrixX = matrixValues[2];
//                    matrixY = matrixValues[5];
//                    width = matrixValues[0] * getDrawable().getIntrinsicWidth();
//                    height = matrixValues[4] * getDrawable().getIntrinsicHeight();
//
//                    dx = event.getX() - start.x;
//                    dy = event.getY() - start.y;
//
//                    //if image will go outside left bound
//                    if (matrixX + dx < 0) {
//
//                        dx = -matrixX;
//                    }
//                    //if image will go outside right bound
//                    if (matrixX + dx + width < getWidth()) {
//                        dx = getWidth() - matrixX - width;
//                    }
//                    //if image will go oustside top bound
//                    if (matrixY + dy > 0) {
//                        dy = -matrixY;
//                    }
//                    //if image will go outside bottom bound
//                    if (matrixY + dy + height < getHeight()) {
//                        dy = getHeight() - matrixY - height;
//                    }
//                    matrix.postTranslate(dx, dy);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f ) {
                            float scale = newDist / oldDist;
                            matrix.set(savedMatrix);
                            float[] values = new float[9];
                            matrix.getValues(values);
                            float currentScale = values[Matrix.MSCALE_X];

                            if(scale * currentScale >=   MAX_ZOOM)
                                scale = MAX_ZOOM / currentScale;
                            else if (scale * currentScale <= MIN_ZOOM)
                                scale = MIN_ZOOM / currentScale;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;



            }
            //limitZoom(matrix);

            this.setImageMatrix(matrix);
            return true;

        }else if ( !enableZoomDrag && enableDraw){

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mode = DRAW;
                    touch_start(event.getX(), event.getY());
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mode = DRAW;
                    touch_move(event.getX(), event.getY());
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    mode = DRAW;
                    touch_up();
                    invalidate();
                    break;
            }

            return true;
        }
        else return false;

    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return  (float) Math.sqrt(x * x + y * y);
    }

    private void init(){
        // set mode
        enableZoomDrag = true;
        enableDraw = false;



        // draw path
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mCanvas = new Canvas();
        mPath = new Path();
        paths.add(mPath);


       // Log.d(TAG,"viewWidth : " + viewWidth);
       // Log.d(TAG,"viewHeight : " + viewHeight);


    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath = new Path();
        paths.add(mPath);
    }

    public void onClickUndo () {
        if (paths.size()>0)  {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        } else  {
            Log.i("undo", "Undo elsecondition");
        }
    }

    public void onClickRedo (){
        if (undonePaths.size()>0)  {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
        else  {
            Log.i("undo", "Redo elsecondition");
        }
    }



   
}
