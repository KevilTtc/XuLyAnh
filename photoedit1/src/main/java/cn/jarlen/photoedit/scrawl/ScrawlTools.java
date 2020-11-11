
package cn.jarlen.photoedit.scrawl;

import android.content.Context;
import android.graphics.Bitmap;

import cn.jarlen.photoedit.utils.FileUtils;

/**
 * @author jarlen
 */
public class ScrawlTools
{
	private DrawingBoardView drawView;
	private Context context;
	
	private int mBrushColor;
	

	
	public ScrawlTools(Context context, DrawingBoardView drawView,
			Bitmap bitmap)
	{
		this.drawView = drawView;
		this.context = context;
		drawView.setBackgroundBitmap(bitmap);
	}

	/**
	 * Tạo một bút màu
	 * 
	 * @param drawStatus
	 * Loại tranh
	 * 
	 * @param paintBitmap
	 * Tranh vẽ cọ bản
	 * 
	 * @param color
	 * Màu bút
	 * 
	 */
	public void creatDrawPainter(DrawAttribute.DrawStatus drawStatus,
			Bitmap paintBitmap, int color)
	{
		drawView.setBrushBitmap(drawStatus, paintBitmap, color);
	}

	
	/**
	 *
	 * Tạo một bàn chải
	 * @param drawStatus
	 * Loại tranh
	 * 
	 * @param paintBrush
	 * Loại bàn chải
	 * (Bao gồm hình ảnh cọ vẽ, màu cọ, độ dày của cọ, loại độ dày của cọ)
	 *
	 * 
	 */
	public void creatDrawPainter(DrawAttribute.DrawStatus drawStatus,
			PaintBrush paintBrush)
	{
		int color = paintBrush.getPaintColor();
		int size = paintBrush.getPaintSize();
		int num = paintBrush.getPaintSizeTypeNo();
		Bitmap bitmap = paintBrush.getPaintBitmap();

		Bitmap paintBitmap = FileUtils.ResizeBitmap(bitmap, num - (size - 1));
		drawView.setBrushBitmap(drawStatus, paintBitmap, color);
	}
	
	
	public void creatStampPainter(DrawAttribute.DrawStatus drawStatus,int[] res,int color)
	{
		drawView.setStampBitmaps(drawStatus, res, color);
	}
	

	/**
	 * Nhận bức tranh
	 * @return
	 */
	public Bitmap getBitmap()
	{
		return drawView.getDrawBitmap();
	}

	public int getBrushColor()
	{
		return mBrushColor;
	}

	/**
	 *
	 * Đặt màu bàn chải
	 * @param mBrushColor
	 * Màu cọ (vô dụng)
	 * 
	 */
	
	public void setBrushColor(int mBrushColor)
	{
		this.mBrushColor = mBrushColor;
	}
}
