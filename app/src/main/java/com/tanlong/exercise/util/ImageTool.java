package com.tanlong.exercise.util;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 * Created by Administrator on 2015/8/12.
 */
public class ImageTool {

    private static final String TAG = "ImageTool";

    /**
     * 从资源文件中加载Bitmap
     *
     * @param res       -- 资源文件
     * @param resId     -- 图片ID
     * @param reqWidth  -- 图片期望宽度
     * @param reqHeight -- 图片期望高度
     * @return 压缩后的Bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小, 此时不会将Bitmap加载到内存中
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 从文件中加载Bitmap
     *
     * @param filePathName -- 文件路径
     * @param reqWidth     -- 图片期望宽度
     * @param reqHeight    -- 图片期望高度
     * @return 压缩后的Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePathName, options);
        // 计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePathName, options);
    }

    /**
     * 计算图片压缩比例
     *
     * @param options   -- 加载图片的BitmapFactory的Options
     * @param reqWidth  -- 图片期望宽度
     * @param reqHeight -- 图片期望高度
     * @return 压缩比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1; // 默认压缩比例
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);//返回最接近参数的long
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 保存图片为文件, 应该异步执行
     * @param context  -- 上下文
     * @param fileName  -- 保存的文件
     * @param saveImage -- 保存的图片
     * @return 保存的文件
     */
    public static File saveBitmapToFile(Context context, String fileName, Bitmap saveImage) {

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//        boolean sdCardExist = false;
        File imageFile = null;
        String fileDirPath = null;
        if (sdCardExist) {
            fileDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "Employee" + File.separator + "Signature";
            LogTool.e(TAG, "fileDirPath is " + fileDirPath);
        } else {
            // 无SD卡,将文件保存到APP文件夹
            fileDirPath = context.getFilesDir().getAbsolutePath() + File.separator + "Signature";
            LogTool.e(TAG, "fileDirPath is " + fileDirPath);
        }

        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        /** 保存文件*/
        imageFile = new File(fileDir, fileName + ".png");
        try {
            imageFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            saveImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            LogTool.e(TAG, "已保存文件" + imageFile.getName());

        } catch (IOException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        return imageFile;
    }

    /**
     * 展示网络图片
     *
     * @param view        图片控件
     * @param imageUrl    图片加载地址
     * @param placeHolder 图片占位符
     * @param error       图片失败的占位符
     */
    @BindingAdapter({"imageUrl", "placeHolder", "errorHolder"})
    public static void loadImage(ImageView view, String imageUrl, Drawable placeHolder, Drawable error) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(placeHolder)
                .error(error)
                .centerCrop()
                .into(view);
    }
}
