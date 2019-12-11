package com.ww.commonlibrary.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.ww.commonlibrary.base.BaseObserver;
import com.ww.commonlibrary.view.ToastView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wang.wei on 2017/11/29.
 */

public class FileUtils {
    public static final String APP_NAME = "TodayLife";

    /**
     * 判断sd卡是否挂载
     *
     * @return boolean
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /***
     * 获取sd卡根目录
     * @return String
     */
    public static String getSDPath() {
        if (isSdCardExist()) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return null;
        }
    }

    public static void createAppFile() {
        File file = new File(getSDPath() + "/" + APP_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取asset中文件内容
     *
     * @param fileName
     * @return
     */
    public static String getAssetFile(Context context, String fileName) {
        String result = "";
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取项目缓存目录sd卡存在返回SDCard/Android/data/你的应用包名/cache目录
     * 否则返回/data/data/<application package>/cache目录
     *
     * @param context
     * @return
     */
    public static String getAPPCachePath(Context context) {
        if (isSdCardExist()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 获取项目file目录sd卡存在返回SDCard/Android/data/你的应用包名/files目录
     * 否则返回/data/data/<application package>/files目录
     *
     * @param context
     * @return
     */
    public static String getAPPFilePath(Context context) {
        if (isSdCardExist()) {
            return context.getExternalFilesDir(null).getPath();
        } else {
            return context.getFilesDir().getPath();
        }
    }

    public static void downImage(final Context context, final Uri uri) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                FutureTarget<Bitmap> futureBitmap = Glide.with(context).asBitmap()
                        .load(uri)
                        .submit();
                emitter.onNext(futureBitmap.get());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Bitmap>() {
                    @Override
                    public void success(Bitmap bitmap) {
                        saveBitmap(bitmap, String.valueOf(System.currentTimeMillis()), context);
                    }

                    @Override
                    public void failure() {

                    }
                });
    }

    /**
     * bitmap以jpg格式保存到本地APPNAME目录
     *
     * @param bitmap
     * @param fileName
     * @param context
     */
    public static void saveBitmap(Bitmap bitmap, String fileName, Context context) {
        File appDir = new File(getSDPath() + "/" + APP_NAME);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, fileName + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            UiUtils.showShortToast(context,"已保存至sd卡TodayLife文件夹下", ToastView.TYPE_SUCCESS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            file.delete();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 迭代法删除文件夹
     *
     * @param file
     */
    public static void deleteFolder(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {//遍历目录下所有的文件
                    deleteFolder(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        }
    }

    /**
     * 递归法获得文件大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.isFile()) {
            return file.length();
        } else {
            File[] fileList = file.listFiles();
            if(fileList==null){
                return size;
            }
            for (File value : fileList) {
                size = size + getFileSize(value);
            }
            return size;
        }
    }

    /**
     * object保存到/data/data/<application package>/files目录
     * mmn
     *
     * @param fileName
     * @param object
     * @param context
     */
    public static void saveObject(String fileName, Object object, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     *  /data/data/<application package>/files目录获取保存到的object
     * @param context
     * @return
     */
    public static Object getObject(String fileName, Context context) {
        Object object = null;
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void deleteObject(String fileName, Context context) {
        File file = new File(context.getFilesDir().getPath(), fileName);
        if (file.exists()) {
            file.delete();
        }
    }
    /**
     * 根据图片文件类型获取图片文件的后缀名
     *
     * @param filePath
     * @return
     */
    public static String getImageFileExt(String filePath) {
        HashMap<String, String> mFileTypes = new HashMap<String, String>();
        mFileTypes.put("FFD8FF", ".jpg");
        mFileTypes.put("89504E47", ".png");
        mFileTypes.put("474946", ".gif");
        mFileTypes.put("49492A00", ".tif");
        mFileTypes.put("424D", ".bmp");

        String value = mFileTypes.get(getFileHeader(filePath));
        String ext = TextUtils.isEmpty(value) ? ".jpg" : value;
        return ext;
    }

    /**
     * 获取文件头信息
     *
     * @param filePath
     * @return
     */
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }
    /**
     * 将byte字节转换为十六进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        String header = builder.toString();
        return header;
    }
}
