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
package cn.jarlen.photoedit.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 *
 * @author jarlen
 *Hoạt động hình ảnh cơ bản
 */

public class PhotoUtils
{

	/**
	 * Xoay hình ảnh
	 * @param bit
	 *
	 * Xoay hình ảnh gốc
	 * @param degrees
	 * Mức độ luân chuyển
	 * 
	 * @return
	 * Hình ảnh sau khi xoay
	 * 
	 */
	public static Bitmap rotateImage(Bitmap bit, int degrees)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
				bit.getHeight(), matrix, true);
		return tempBitmap;
	}
	
	/**
	 *
	 * Lật hình
	 * @param bit
	 * Lật hình ảnh gốc
	 * 
	 * @param x
	 * Lật trục X
	 * 
	 * @param y
	 * Lật trục Y
	 * 
	 * @return
	 * Hình ảnh sau khi lật
	 * Sự miêu tả:
	 * (1, -1) lật lên và xuống
	 * (-1,1) Lật trái và phải
	 *
	 *
	 *
	 * 
	 */
	public static Bitmap reverseImage(Bitmap bit,int x,int y)
	{
		Matrix matrix = new Matrix();
		matrix.postScale(x, y);
		
		Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
				bit.getHeight(), matrix, true);
		return tempBitmap;
	}
}
