package com.tanlong.exercise.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadPoolExecutor;

public class ImageLoader {

    private final int MSG_POST_RESULT = 1;

    private static final int TAG_KEY_URI = R.id.image_loader_uri;
    /**
     * 磁盘缓存大小
     */
    private final long DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private final int IO_BUFFER_SIZE = 8 * 1024;
    private final int DISK_CACHE_INDEX = 0;
    /**
     * 是否已创建磁盘缓存
     */
    private boolean mIsDiskLrcCacheCreated = true;

    /**
     * Bitmap的内存缓存,Key为图片加载URL
     */
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * Bitmap的磁盘缓存
     */
    private DiskLruCache mDiskCache;
    /**
     * 加载图片使用的线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = ThreadPoolUtil.getNormalThreadPool();

    private Context mContext;
    private ImageResizer imageResizer;

    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        imageResizer = new ImageResizer();
        //JVM可用的最大内存, 单位为KB
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //返回每一个value的大小,单位应该和cacheSize统一
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return bitmap.getAllocationByteCount() / 1024;
                } else {
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext, mContext.getPackageName() + "_bitmapCache");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        //是否有足够空间可以创建磁盘缓存
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLrcCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    /**
     * 将Bitmap加入内存缓存
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取Bitmap
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void bindBitmap(String url, ImageView imageView) {
        bindBitmap(url, imageView, 0, 0);
    }

    /**
     * 异步加载图片
     * 1.从内存缓存中加载图片
     * 1.1 如果有对应图片,直接设置,结束
     * 1.2 如果没有对应图片,在线程池中运行同步加载图片方法
     * 2.线程池中同步加载图片成功,发送消息给主线程设置ImageView
     * @param url -- 图片URL
     * @param imageView -- 加载ImageView
     * @param reqWidth -- 期望宽度
     * @param reqHeight -- 期望高度
     */
    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        Logger.e("异步加载图片");
        //配置ImageView 应加载的 图片URL
        imageView.setTag(TAG_KEY_URI, url);
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    Message message = Message.obtain();
                    message.what = MSG_POST_RESULT;
                    message.obj = result;
                    mMainHandler.sendMessage(message);
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 同步加载图片
     * 1.从内存缓存中加载图片
     * 1.1 如果有对应图片,直接返回
     * 1.2 没有对应图片,从磁盘缓存中获取
     * 2.从磁盘缓存中加载图片
     * 2.1 如果有对应图片,直接返回
     * 2.2 没有对应图片,从网络中获取
     * 3.从网络中加载图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Logger.e("同步加载图片");
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对应没有磁盘缓存的情况
        if (bitmap == null && !mIsDiskLrcCacheCreated) {
            bitmap = downloadBitmapFromUrl(url);
        }

        return bitmap;
    }

    /**
     * 从内存缓存获取Bitmap
     * @param url -- 加载图片的URL
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        Logger.e("从内存缓存获取Bitmap");
        //统一 内存缓存和磁盘缓存 使用的Key
        String key = hashKeyFromUrl(url);
        return getBitmapFromMemCache(key);
    }

    /**
     * 从磁盘缓存加载图片
     * 1.是否有对应图片磁盘缓存
     * 1.1 没有返回空
     * 2.有对应图片磁盘缓存
     * 2.1 获取图片对应的FileInputStream
     * 2.2 根据FileInputStream获取FileDescriptor
     * 2.3 根据要求宽高对图片进行压缩
     * 2.4 将压缩后Bitmap存入内存缓存
     * 2.5 返回压缩后的Bitmap
     * @param url -- 图片URL
     * @param reqWidth -- 要求宽度
     * @param reqHeight -- 要求高度
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException{
        Logger.e("从磁盘缓存加载图片");
        if (mDiskCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
        if (snapshot != null) {
            //有对应图片的磁盘缓存
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = imageResizer.decodeSimpledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemCache(key, bitmap);
            }
        }

        return bitmap;
    }

    /**
     * 从网络中加载图片,如果加载成功,则将图片存入磁盘缓存和内存缓存
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException{
        Logger.e("从网络中加载图片,如果加载成功,则将图片存入磁盘缓存和内存缓存");
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread");
        }
        if (mDiskCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 将URL指向的图片写入OutputStream
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 从网络获取图片
     * @param urlString
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Logger.e("从网络获取图片");

        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取磁盘缓存文件夹路径
     *
     * @param context
     * @param dirName -- 文件夹名称,确保唯一
     * @return
     */
    private File getDiskCacheDir(Context context, String dirName) {
        boolean externalStorageEnable = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageEnable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dirName);
    }

    /**
     * 获取指定路径的可用字节数
     *
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        return path.getUsableSpace();
    }

    /**
     * 将图片URL地址转换为URL的MD5值
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    /**
     * 图片加载结果
     */
    private static class LoaderResult {
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
        }
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_POST_RESULT:
                    LoaderResult result = (LoaderResult) msg.obj;
                    ImageView imageView = result.imageView;
                    //获取ImageView绑定的图片URL
                    String url = (String) imageView.getTag(TAG_KEY_URI);
                    if (url.equals(result.url)) {
                        imageView.setImageBitmap(result.bitmap);
                    } else {
                        //加载出来的Bitmap 不是 ImageView 应该设置的Bitmap
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
