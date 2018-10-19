package com.tanlong.exercise.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 图片压缩工具
 */
public class ImageResizer {

    public static final String TAG = "ImageResizer";

    /**
     * 从资源文件中加载Bitmap
     * @param res
     * @param resId
     * @param reqWidth -- 要求宽度
     * @param reqHeight -- 要求高度
     * @return
     */
    public Bitmap decodeSimpledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //先测量宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //重新加载
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 从文件描述符中加载Bitmap
     * @param fd -- 文件描述符
     * @param reqWidth -- 要求宽度
     * @param reqHeight -- 要求高度
     * @return
     */
    public Bitmap decodeSimpledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 根据Bitmap宽高 与 要求图片宽高 计算采样率
     * 根据最新编码规范,采样率应该是2的指数(1,2,4,8...)
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth <= 0 || reqHeight <= 0) {
            return 1;
        }
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width >= reqWidth || height >= reqHeight) {
            inSampleSize = Math.min(width / reqWidth, height / reqHeight);
        }

        return inSampleSize;
    }
}
