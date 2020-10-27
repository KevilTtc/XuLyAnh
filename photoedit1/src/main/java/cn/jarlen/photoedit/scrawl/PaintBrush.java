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

import android.graphics.Bitmap;

/**
 * chải
 * @author jarlen
 *
 */
public class PaintBrush
{
	/**
	 * Bàn chải hình ảnh
	 */
	private Bitmap paintBitmap;
	
	/**
	 * Độ dày của bàn chải
	 * 
	 */
	private int paintSize;
	
	/**
	 * Màu bút
	 * 
	 */
	private int paintColor;
	
	/**
	 * Loại độ dày của bàn chải
	 * 1，2，3...
	 */
	private int paintSizeTypeNo;
	

	/**
	 * ********************************
	 * 
	 * *********
	 * 	   *
	 * 	   *  
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *************************************/
	
	
	
	/**
	 * Nhận hình ảnh bàn chải
	 * @return
	 */
	public Bitmap getPaintBitmap()
	{
		return paintBitmap;
	}

	/**
	 * Đặt hình ảnh bàn chải
	 * Bàn chải hình ảnh
	 * @param paintBitmap
	 *
	 * 
	 */
	public void setPaintBitmap(Bitmap paintBitmap)
	{
		this.paintBitmap = paintBitmap;
	}

	/**
	 *
	 * Nhận kích thước bàn chải
	 * @return
	 */
	public int getPaintSize()
	{
		return paintSize;
	}

	/**
	 * Đặt kích thước bàn chải
	 * 
	 * @param paintSize
	 *
	 * Kích thước bàn chải
	 */
	public void setPaintSize(int paintSize)
	{
		if(paintSize >= paintSizeTypeNo)
		{
			this.paintSize = paintSizeTypeNo;
		}
		else if(paintSize <= 0)
		{
			this.paintSize = 1;
		}
		else
		{
			this.paintSize = paintSize;
		}
		
	}

	/**
	 *
	 * Nhận màu bút
	 * @return
	 */
	public int getPaintColor()
	{
		return paintColor;
	}

	/**
	 * Đặt màu bút
	 * Màu bút
	 * @param paintColor
	 *
	 * 
	 */
	public void setPaintColor(int paintColor)
	{
		this.paintColor = paintColor;
	}

	/**
	 * Nhận loại độ dày của bàn chải
	 * 
	 * @return
	 * 
	 */
	public int getPaintSizeTypeNo()
	{
		return paintSizeTypeNo;
	}

	/**
	 * Đặt loại độ dày của bàn chải
	 * 
	 * @param paintSizeTypeNo
	 * Loại độ dày của bàn chải
	 * 
	 */
	public void setPaintSizeTypeNo(int paintSizeTypeNo)
	{
		this.paintSizeTypeNo = paintSizeTypeNo;
	}
	
	
}
