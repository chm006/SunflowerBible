package com.chm006.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.chm006.library.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * bitmap处理工具类
 * Created by oracleen on 2016/7/5.
 */
public class BitmapUtil {

    /**
     * Drawable 转换成 Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将 Bitmap 保存到指定目录
     *
     * @param bitmap
     * @param dir          保存图片的目录
     * @param name         保存的图片文件名
     * @param isShowPhotos 如果为 true ，将图片文件保存到系统图库中
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String dir, String name, boolean isShowPhotos) {
        File path = new File(dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 其次把文件插入到系统图库
        if (isShowPhotos) {
            try {
                MediaStore.Images.Media.insertImage(BaseApplication.getIntstance().getContentResolver(),
                        file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            BaseApplication.getIntstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        }
        return true;
    }

    /**
     * 获取图片显示到ImageView（缩小到ImageView控件的大小）（如果获取拍摄的照片，该方法在onActivityResult方法中使用）
     *
     * @param imageView
     * @param file_img
     * @param png_quality 压缩图片质量（0-100）
     */
    public static void setPic2ImageView(ImageView imageView, File file_img, int png_quality) {
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file_img.getPath(), factoryOptions);

        // 照片的原来大小
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // 图片缩小的比例
        int scaleFactor = Math.min(imageWidth / width, imageHeight / height);

        // Decode the image file into a Bitmap sized to fill the View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(file_img.getPath(), factoryOptions);
        bitmap.compress(Bitmap.CompressFormat.PNG, png_quality, new ByteArrayOutputStream());// (0-100)压缩文件
        // 缩小后图片的大小（KB）
        //int byteCount = bitmap.getByteCount()/1024;
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 从（手机相册选中的相片）URI获得文件路径
     * 例：/external/images/media/8835  >>>  /storage/emulated/0/DCIM/Camera/xxx.jpg
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File showExternalImagesMedia(Activity activity, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        return new File(img_path);
    }

    /**
     * 裁剪图片（手指滑动截图框的大小）
     *
     * @param uri
     * @param activity
     * @param requestcode
     */
    public static void startPhotoZoom(int requestcode, Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestcode);
    }

    /**
     * 获取裁剪之后的图片
     *
     * @param picdata
     */
    public static void setPicToView(Intent picdata, ImageView imageView) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
        }

    }
}
