package com.ahnz.headmaster.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.view.activity_webview.WebViewActivity;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author xzb
 * @description: 隐私协议&使用条款
 * @date :2020/7/31 10:22
 */
public class PolicyAgreementDialog extends Dialog {

    private Context context;

    public PolicyAgreementDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        this.context = context;
        //按空白处 不消失
        setCanceledOnTouchOutside(false);
        //按返回键 不消失
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_policy_agreement);

        //初始化界面控件
        initView();
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText("政策&协议&权限说明");

        QMUISpanTouchFixTextView privacy = findViewById(R.id.privacy);
        String string1 = "《头像制作神器》是由安徽哪吒互娱信息技术有限公司（以下简称我们）研发和运营的产品。" + "\n" + "我们非常重视您的个人信息和隐私保护。为了更好的保障您的个人权益，在您使用我们的产品前，请您认真阅读";
        String string2 = "《隐私政策》";
        String string3 = "和";
        String string4 = "《使用协议》";
        String string5 = "的全部内容，同意并接受全部条款后开始使用我们的产品和服务。" + "\n" + "若选择不同意，将无法使用我们的产品和服务。";

        privacy.setMovementMethodDefault();
        privacy.setNeedForceEventToParent(true);
        privacy.setText(generateSp(privacy, string1 + string2 + string3 + string4 + string5));

        QMUISpanTouchFixTextView clause = findViewById(R.id.clause);
        String s = "我们申请了存储权限、手机状态权限、拍照权限，分别用于存放图片、分享到微信QQ、拍摄图片制作头像，请您同意上述权限，否则无法使用我们的产品和服务。";
        clause.setText(s);


        //点击不同意
        findViewById(R.id.no_agree_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post("no_agree");
                dismiss();
            }
        });

        //点击同意
        findViewById(R.id.agree_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post("agree");
                dismiss();
            }
        });
    }

    private SpannableString generateSp(TextView tv, String text) {
        String highlight1 = "《隐私政策》";
        String highlight2 = "《使用协议》";
        SpannableString sp = new SpannableString(text);
        int start = 0, end;
        int index;
        while ((index = text.indexOf(highlight1, start)) > -1) {
            end = index + highlight1.length();
            sp.setSpan(new QMUITouchableSpan(tv,
                    R.attr.app_skin_span_normal_text_color,
                    R.attr.app_skin_span_pressed_text_color,
                    R.attr.app_skin_span_normal_bg_color,
                    R.attr.app_skin_span_pressed_bg_color) {
                @Override
                public void onSpanClick(View widget) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.TITLE, "隐私政策");
                    intent.putExtra(WebViewActivity.URL, "http://hd.qianr.com/xieyi/yszc.html");
                    context.startActivity(intent);
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }

        start = 0;
        while ((index = text.indexOf(highlight2, start)) > -1) {
            end = index + highlight2.length();
            sp.setSpan(new QMUITouchableSpan(tv,
                    R.attr.app_skin_span_normal_text_color,
                    R.attr.app_skin_span_pressed_text_color,
                    R.attr.app_skin_span_normal_bg_color,
                    R.attr.app_skin_span_pressed_bg_color) {
                @Override
                public void onSpanClick(View widget) {
                    Intent use_agreement = new Intent(context, WebViewActivity.class);
                    use_agreement.putExtra(WebViewActivity.TITLE, "使用协议");
                    use_agreement.putExtra(WebViewActivity.URL, "http://hd.qianr.com/xieyi/syxy.html");
                    context.startActivity(use_agreement);
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }
        return sp;
    }
}
