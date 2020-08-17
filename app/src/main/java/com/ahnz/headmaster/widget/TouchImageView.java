package com.ahnz.headmaster.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author xzb
 * @description: 自定义ImageView类 支持拖动缩放旋转
 * @date :2020/7/22 14:57
 * <p>
 * https://blog.csdn.net/qq_15762823/article/details/49633157
 */
@Deprecated
@SuppressLint("AppCompatCustomView")
public class TouchImageView extends ImageView {

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM_ROTATE = 2;
    //当前操作模式
    public int mode = NONE;

    private double degflag = 0;
    //起始点
    PointF start = new PointF();
    PointF start1 = new PointF();
    //两点触摸时中心点
    PointF mid = new PointF();
    //view中心点
    PointF center = new PointF();
    float oldDist = 1f;

    public TouchImageView(Context context) {
        super(context);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 计算间距
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 计算中点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        center.set(this.getWidth() / 2, this.getHeight() / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //第一个点按下 为拖动模式
            case MotionEvent.ACTION_DOWN:
                matrix.set(getImageMatrix());
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            //一个以上点按下取第一第二两点计算角度和距离  分别实现旋转和缩放功能
            case MotionEvent.ACTION_POINTER_DOWN:

                start1.set(event.getX(1), event.getY(1));
                oldDist = this.spacing(event);
                //如果两点距离大于10 则变更操作模式
                if (oldDist > 10f) {
                    //操作模式发生改变 重新计算起点
                    start.set(event.getX(), event.getY());
                    savedMatrix.set(matrix);
                    //计算两点中心点  以其为中心进行缩放
                    midPoint(mid, event);
                    mode = ZOOM_ROTATE;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                degflag = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                //图片拖动功能
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }
                // 图片缩放功能
                else if (mode == ZOOM_ROTATE) {
                    float newDist = spacing(event);
                    if (newDist > 10) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                        //旋转
                        Rotate(event);
                    }

                }
                break;
        }
        setImageMatrix(matrix);
        return true;
    }

    public void Rotate(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);
        //旋转功能的实现关键
        //按下时的两点表示一条直线，当前移动至的两点表示一条直线
        //然后计算两线交点，并求出角度

        PointF pi = intersection(new PointF(start.x, start.y), new PointF(start1.x, start1.y), new PointF(x, y), new PointF(x1, y1));
        if (pi == null) {
            return;
        }
        double deg = getActionDegrees(pi.x, pi.y, start.x, start.y, x, y);

        //在0度临界值 旋转角度会翻转  通过计算上相邻两次旋转的角度差是否过大（大于90）来进行矫正。
        if (Math.abs(degflag - deg) > 90) {
            if (deg < 0) {
                deg += 180;
            } else {
                deg -= 180;
            }
        }
        degflag = deg;
        /*
         * double deg = 0; double deg1 = getActionDegrees(center.x, center.y,
         * start.x, start.y, x, y); double deg2 = getActionDegrees(center.x,
         * center.y, start1.x, start1.y, x1, y1); deg = (deg1 + deg2) / 2;
         */

        matrix.postRotate((float) deg, mid.x, mid.y);
        // setImageMatrix(matrix);
    }

    /*
     * 计算两线的夹角，点1，点2。注：坐标系起原点在左上角
     */
    private double getActionDegrees(float x, float y, float x1, float y1, float x2, float y2) {

        double a = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        double b = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        double c = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
        if ((2 * b * c) == 0) {
            return 0;
        }
        // 余弦定理
        double cosA = (b * b + c * c - a * a) / (2 * b * c);
        // 反余弦求角度
        double arcA = Math.acos(cosA);
        double degree = arcA * 180 / Math.PI;

        // 起点终点在相邻象限
        // 第1、2象限
        if (y1 < y && y2 < y) {
            if (x1 < x && x2 > x) {// 由2象限向1象限滑动
                return degree;
            }
            // 由1象限向2象限滑动
            else if (x1 > x && x2 < x) {
                return -degree;
            }
        }
        // 第3、4象限
        if (y1 > y && y2 > y) {
            // 由3象限向4象限滑动
            if (x1 < x && x2 > x) {
                return -degree;
            }
            // 由4象限向3象限滑动
            else if (x1 > x && x2 < x) {
                return degree;
            }
        }
        // 第2、3象限
        if (x1 < x && x2 < x) {
            // 由2象限向3象限滑动
            if (y1 < y && y2 > y) {
                return -degree;
            }
            // 由3象限向2象限滑动
            else if (y1 > y && y2 < y) {
                return degree;
            }
        }
        // 第1、4象限
        if (x1 > x && x2 > x) {
            // 由4向1滑动
            if (y1 > y && y2 < y) {
                return -degree;
            }
            // 由1向4滑动
            else if (y1 < y && y2 > y) {
                return degree;
            }
        }
        // 起点终点在一个象限内
        // 顺时针或逆时针。 tanB>tanC 逆时针
        float tanB = (y1 - y) / (x1 - x);
        float tanC = (y2 - y) / (x2 - x);
        if ((x1 > x && y1 > y && x2 > x && y2 > y && tanB > tanC)// 第一象限
                || (x1 > x && y1 < y && x2 > x && y2 < y && tanB > tanC)// 第四象限
                || (x1 < x && y1 < y && x2 < x && y2 < y && tanB > tanC)// 第三象限
                || (x1 < x && y1 > y && x2 < x && y2 > y && tanB > tanC))// 第二象限
        {
            return -degree;
        }
        // 起点终点在对称象限内
        if ((x1 > x && y1 > y && x2 < x && y2 < y && tanB < tanC)// 第一象限
                || (x1 > x && y1 < y && x2 < x && y2 > y && tanB < tanC)// 第四象限
                || (x1 < x && y1 < y && x2 > x && y2 > y && tanB < tanC)// 第三象限
                || (x1 < x && y1 > y && x2 > x && y2 < y && tanB < tanC))// 第二象限
        {
            return -degree;
        }
        return degree;
    }

    /**
     * 计算两条直线的交点
     *
     * @param a 直线A上一点
     * @param b 直线A另一点
     * @param c 直线B上一点
     * @param d 直线B上另一点
     * @return
     */
    private PointF intersection(PointF a, PointF b, PointF c, PointF d) {

        double denominator = (b.y - a.y) * (d.x - c.x) - (a.x - b.x) * (c.y - d.y);
        if (denominator == 0) {
            return null;
        }
        double x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y) + (b.y - a.y) * (d.x - c.x) * a.x - (d.y - c.y) * (b.x - a.x) * c.x) / denominator;
        double y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x) + (b.x - a.x) * (d.y - c.y) * a.y - (d.x - c.x) * (b.y - a.y) * c.y) / denominator;
        return new PointF((float) x, (float) y);
    }

}