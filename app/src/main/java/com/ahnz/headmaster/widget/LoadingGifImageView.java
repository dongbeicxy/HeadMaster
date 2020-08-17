package com.ahnz.headmaster.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.ahnz.headmaster.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * @author xzb
 * @description: Gif ImageView
 * @date :2020/7/13 13:20
 */
@SuppressLint("AppCompatCustomView")
public class LoadingGifImageView extends ImageView {
    public LoadingGifImageView(Context context) {
        super(context);
    }

    public LoadingGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Glide.with(context).load(R.drawable.loading_gif).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable) resource).setLoopCount(100000);
                }
                return false;
            }
        }).into(this);
    }
}
