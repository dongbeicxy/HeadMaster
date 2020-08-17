package com.ahnz.headmaster.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.ahnz.headmaster.R;

import cn.tseeey.justtext.JustTextView;

/**
 * @author xzb
 * @description: 免责声明
 * @date :2020/7/13 9:44
 */
public class DisclaimerDialog extends Dialog {

    public DisclaimerDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_disclaimer);

        //初始化界面控件
        initView();
    }

    private void initView() {

        //JustTextView justTextView = findViewById(R.id.text);

        findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
