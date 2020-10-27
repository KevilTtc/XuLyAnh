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
	 * Tạo một bàn chải
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
