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
package cn.jarlen.photoedit.filters;

/**
 * DESCRIBE:
 * // Filter
 */
public class NativeFilter {
    static {
        System.loadLibrary("nativefilter");
    }

    public native String test();


    /**
     * gray
     * @param pixels Bộ pixel hình ảnh
     * @param width  Chiều rộng pixel hình ảnh
     * @param height Chiều cao pixel hình ảnh
     * @param factor Bộ lọc hình ảnh thay đổi mức độ( 0 < factor < 1)
     * @return
     */
    public native int[] gray(int[] pixels, int width, int height, float factor);


    /**
     * Khảm
     * @param pixels
     *              Bộ pixel hình ảnh
     * @param width
     *              Chiều rộng pixel hình ảnh
     * @param height
     *              Chiều cao pixel hình ảnh
     * @param factor
     *              Mosaic độ (0 <factor <30), phạm vi giá trị được xác định bởi hiệu quả của thuật toán, càng nhỏ, hiệu quả càng thấp.
     * @return
     */
    public native int[] mosatic(int[] pixels, int width, int height,
                                  int factor);

    /**
     * LOMO
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] lomo(int[] pixels, int width, int height, float factor);


    /**
     *Hiệu ứng nỗi nhớ
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return Hiệu ứng nỗi nhớ
     */
    public native int[] nostalgic(int[] pixels, int width, int height,
                                    float factor);

    /**
     *Hiệu ứng truyện tranh
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return 漫画效果
     */
    public native int[] comics(int[] pixels, int width, int height,
                                 float factor);

    /**
     *Phong cách thoáng qua
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] brown(int[] pixels, int width, int height,
                                float factor);

    /**
     * Hiệu ứng phác thảo --- bản vẽ bút chì
     * @param pixels
     * @param width
     * @param height
     * @param factor
     * @return
     */
    public native int[] sketchPencil(int[] pixels, int width, int height,
                                       float factor);
}
