package com.ahnz.headmaster.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ahnz.headmaster.R;

/**
 * @author xzb
 * @description:意见反馈
 * @date :2020/8/4 10:52
 */
public class FeedBackDialog extends Dialog {

    private Context context;

    public FeedBackDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        this.context = context;
        //按空白处 不消失
        setCanceledOnTouchOutside(false);
        //按返回键 消失
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        //初始化界面控件
        initView();
    }

    private void initView() {
        EditText editText = findViewById(R.id.edit_text);
        TextView commit_tv = findViewById(R.id.commit_tv);
        commit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = editText.getText().toString().trim();
                if (string.equals("")) {
                    Toast.makeText(context, "请输入反馈内容", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "反馈成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        findViewById(R.id.cancle_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
