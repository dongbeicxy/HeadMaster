package com.ahnz.headmaster.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /*
     获取  文件名字 + 后缀名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    //https://blog.csdn.net/u010937230/article/details/73303034

    /**
     * @创建者 邢志标
     * @创建时间 2018/9/13  9:58
     * @修改者 邢志标
     * @方法描述 创建 APP 专属文件夹 . 文件     并返回路径
     */
    public static String getFolderOrFilePath(Context context, String folderName, boolean isCreateFile, String fileName) {
        String path = "";
        //这个函数用来获取SD卡的挂载状态
        //如果传入参数path则是获取该路径的的挂载状态
        //如果这个目录被用户的PC挂载，或者从设备中移除，或者其他问题发生，状态的返回是不一样的

        //外部存储可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (isCreateFile) {
                //Context.getExternalFilesDir()一般放一些长时间保存的数据
                //Context.getExternalCacheDir()一般存放临时缓存数据
                //(会随着应用的卸载一起删除掉) 【外部存储】
                path = context.getExternalFilesDir(folderName).getAbsolutePath() + File.separator + fileName;
            } else {
                path = context.getExternalFilesDir(folderName).getAbsolutePath();
            }
        } else {
            //使用内部存储
            if (isCreateFile) {
                //(会随着应用的卸载一起删除掉)【内部存储】
                path = context.getFilesDir() + File.separator + folderName + File.separator + fileName;
            } else {
                path = context.getFilesDir() + File.separator + folderName;
            }
        }
        File file = new File(path);
        //如果 是一个 文件夹
        if (file.isDirectory() && !file.exists()) {
            //为什么用mkdirs()呢？因为这个方法可以在不知道偶没有父类文件夹的情况下，创建文件夹，而mkdir（）必须在有父类的文件夹下创建文件
            file.mkdirs();
        }
        //如果是一个 文件
        if (file.isFile() && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public interface PicSaveStateListener {
        void notifyState(String msg);
    }

    private PicSaveStateListener picSaveStateListener;

    /**
     * 保存 Bitmap 到本地
     */
    public void saveBitmap(String name, String folderName, Bitmap bitmap, Context context, PicSaveStateListener picSSListener) {
        this.picSaveStateListener = picSSListener;
        String path = "";
        //外部存储可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //这个方法是获取外部存储的根路径
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName;
        }
        //使用内部存储
        else {
            //这个方法是获取内部存储的根路径
            path = Environment.getDataDirectory().getAbsolutePath() + File.separator + folderName;
        }
        //判断 指定文件夹 的路径是否存在
        if (!fileOrFolderIsExist(path)) {
            Log.e("", "");
            picSaveStateListener.notifyState("创建文件夹失败");
        } else {
            //如果 指定文件夹 创建成功 ，那么我们 则需要 进行 图片存储操作
            File saveFile = new File(path, name);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                // compress 压缩的意思
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                //存储完成后 需要清理相关的进程
                fileOutputStream.flush();
                fileOutputStream.close();
                //TODO
                //通知系统相册刷新  Uri.fromFile 在7.0以上 居然不报错？  很奇怪
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(saveFile));
                context.sendBroadcast(intent);
                picSaveStateListener.notifyState("保存成功");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                picSaveStateListener.notifyState("文件异常");
            } catch (IOException e) {
                e.printStackTrace();
                picSaveStateListener.notifyState("IO异常");
            } catch (Exception e) {
                e.printStackTrace();
                picSaveStateListener.notifyState("发生异常");
            }
        }
    }

    /**
     * 判断 文件夹 或 文件 是否存在，如果不存在 创建文件夹 或 文件
     */
    public static boolean fileOrFolderIsExist(String fileOrFolderName) {
        File file = new File(fileOrFolderName);
        if (file.exists()) {
            return true;
        } else {
            if (file.isFile()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return file.mkdirs();
            }
        }
        return false;
    }

    /**
     * 创建 本地存储 根路径下的 图片文件夹 或文件
     */
    public static String createRootPicsFolderOrFile(String folderName, boolean isCreateFile, String fileName) {
        String path = "";
        //外部存储可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //这个方法是获取外部存储的根路径
            if (isCreateFile) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName + File.separator + fileName;
            } else {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName;
            }
        }
        //使用内部存储
        else {
            //这个方法是获取内部存储的根路径
            if (isCreateFile) {
                path = Environment.getDataDirectory().getAbsolutePath() + File.separator + folderName + File.separator + fileName;
            } else {
                path = Environment.getDataDirectory().getAbsolutePath() + File.separator + folderName;
            }
        }
        File file = new File(path);
        //如果 是一个 文件夹
        if (file.isDirectory() && !file.exists()) {
            //为什么用mkdirs()呢？因为这个方法可以在不知道偶没有父类文件夹的情况下，创建文件夹，而mkdir（）必须在有父类的文件夹下创建文件
            file.mkdirs();
        }
        //如果是一个 文件
        if (file.isFile() && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
