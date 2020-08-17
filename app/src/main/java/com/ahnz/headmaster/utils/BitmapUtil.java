package com.ahnz.headmaster.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

/**
 * @author xzb
 * @description: 图片编辑 工具
 * @date :2020/7/22 14:12
 */
public class BitmapUtil {
    /**
     * 把一个View的对象转换成bitmap
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        // 能画缓存就返回false
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("BitmapUtil", "failed getViewBitmap(" + v + ")",
                    new RuntimeException());
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }
}
