package com.ahnz.headmaster.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.widget.PercentCircle;

/**
 * @author xzb
 * @description: 下载图片 Dialog
 * @date :2020/7/13 13:45
 */
public class DownLoadPicDialog extends Dialog {

//    private PercentCircle percentCircle;

    public DownLoadPicDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download_pic);

        //初始化界面控件
        initView();
    }

    private void initView() {
//        percentCircle = (PercentCircle) findViewById(R.id.percent_circle);
    }

    //设置 进度圈 进度
    public void setTargetPercent(int progress) {
//        percentCircle.setProgressRate(1);//设置进度条前进频率，单位微妙，此处是10微妙前进1%
//        percentCircle.setTargetPercent(progress);//设置进度条最大可以到达的百分比，此处设置是到达100%
    }
}
