package jinye.demo.mcustomwaterripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by 今夜犬吠 on 2018/1/17.
 * 水波-自定义
 */

public class MWaterWaveCustomView extends View {
  /*获取手机屏幕像素密度*/private float mPixelDensity = getContext().getResources().getDisplayMetrics().density;
  /*圆环集合*/private List<MCircleBean> mCircleBeanList;
  /*初始半径*/private float mInitRadius = 60f * mPixelDensity;
  /*最大半径*/private float mMaxRadius = 200f * mPixelDensity;
  /*递增半径*/private float mIncrementRadius = 20f * mPixelDensity;
  /*初始圆心*/private Point mPoint = new Point();
  /*上下文*/private Context mContext;

  /*主圆画笔*/private Paint mMainPaint;
  /*文字画笔*/private Paint mTextPaint;
  /*水波画笔*/private Paint mWaterWavePaint;

  /*主圆颜色*/private int mMainColor = R.color.colorAccent;
  /*文字颜色*/private int mTextColor = R.color.TextcolorAccent;
  /*波纹颜色*/private int mWaterWaveColor = R.color.colorPrimaryDark;

  ///*画笔集合-波纹*/private List<Paint> mCirclePaintList;

  /*计划日期*/private String mPlanDate = "2017.09.03";
  private String getHMSS(long timestamp) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
    String time = null;
    try {
      return sdf.format(new Date(timestamp));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return time.trim();
  }
  /**
   * 构造-New
   *
   * @param context
   */
  public MWaterWaveCustomView(Context context) {
    super(context);
    this.mContext = context;
    mCircleBeanList = new ArrayList<MCircleBean>();
    toolInitWaterCircle();
    toolInitPaint();
    toolBuildWaterWave();
  }

  /**
   * 构造-XML
   *
   * @param context
   * @param attrs
   */
  public MWaterWaveCustomView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    mCircleBeanList = new ArrayList<MCircleBean>();
    toolInitWaterCircle();
    toolInitPaint();
    toolBuildWaterWave();
  }

  /**
   * 动态获取圆心的坐标
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mPoint.set(getWidth() / 2, getHeight() / 2);
  }

  /**
   * 绘制
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    /*绘制波纹圆环*/
    if (!mCircleBeanList.isEmpty()) {
      for (int i = 0; i < mCircleBeanList.size(); i++) {
        MCircleBean mCircleBean = mCircleBeanList.get(i);
        if (System.currentTimeMillis() - mCircleBean.getmCreateTime() < mCircleBean.getmWaterWaveDuration()) {
          mWaterWavePaint.setAlpha(mCircleBean.toolGetWaterWaveAlpha());
          canvas.drawCircle(mPoint.x, mPoint.y, mCircleBean.toolGetWaterWaveRadius(mMaxRadius), mWaterWavePaint);
        } else {
          if (mCircleBean.toolGetWaterWaveAlpha() == 0) {
            mCircleBeanList.remove(i);
          }
        }

      }
    }
    /*绘制主圆*/
    canvas.drawCircle(mPoint.x, mPoint.y, mInitRadius, mMainPaint);

    /*绘制文本*/
    /*字体控制*/
    Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
    canvas.drawText(getHMSS(System.currentTimeMillis()), mPoint.x, mPoint.y - fm.descent + (fm.descent - fm.ascent) / 2 - 20 * mPixelDensity, mTextPaint);
    canvas.drawText("水波纹", mPoint.x, mPoint.y - fm.descent + (fm.descent - fm.ascent) / 2 + 20 * mPixelDensity, mTextPaint);

    if (!mStopCreateCircle) {
      postInvalidateDelayed(20);
    }
  }


  /**
   * 初始化波纹圆环
   */
  private void toolInitWaterCircle() {

    for (int i = 0; i < 3; i++) {
      MCircleBean mCircleBean = new MCircleBean();
      mCircleBean.setmWaterWaveDuration(4000);
      mCircleBean.setmWaterWaveInitRadius(mInitRadius + (i + 1) * mIncrementRadius);
      mCircleBeanList.add(mCircleBean);
    }
  }

  /**
   * 初始化画笔
   */
  private void toolInitPaint() {
    ///**初始化波纹画笔集合*/mCirclePaintList = new ArrayList<Paint>();

    /**初始化主圆画笔*/mMainPaint = new Paint();
    /**画笔颜色*/mMainPaint.setColor(ContextCompat.getColor(mContext, mMainColor));
    /**画笔样式*/mMainPaint.setStyle(Paint.Style.FILL);
    /**画笔粗细*/mMainPaint.setStrokeWidth(1f);
    /**抗锯齿*/mMainPaint.setAntiAlias(true);
    /**防抖动*/mMainPaint.setDither(true);
    /**设置笔触样式-圆*/mMainPaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mMainPaint.setStrokeJoin(Paint.Join.ROUND);

    /**初始化文字画笔*/mTextPaint = new Paint();
    /**画笔颜色*/mTextPaint.setColor(ContextCompat.getColor(mContext, mTextColor));
    /**画笔样式*/mTextPaint.setStyle(Paint.Style.FILL);
    /**设置文本大小*/mTextPaint.setTextSize(15f * mPixelDensity);
    /**字符中心*/mTextPaint.setTextAlign(Paint.Align.CENTER);
    /**画笔粗细*/mTextPaint.setStrokeWidth(0.2f);
    /**抗锯齿*/mTextPaint.setAntiAlias(true);
    /**防抖动*/mTextPaint.setDither(true);
    /**设置笔触样式-圆*/mTextPaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mTextPaint.setStrokeJoin(Paint.Join.ROUND);

    /**初始化波纹圆环画笔*/mWaterWavePaint = new Paint();
    /**画笔颜色*/mWaterWavePaint.setColor(ContextCompat.getColor(mContext, mWaterWaveColor));
    /**设置画笔透明度*/mWaterWavePaint.setAlpha(255);
    /**画笔样式*/mWaterWavePaint.setStyle(Paint.Style.FILL);
    /**画笔粗细*/mWaterWavePaint.setStrokeWidth(1f);
    /**抗锯齿*/mWaterWavePaint.setAntiAlias(true);
    /**防抖动*/mWaterWavePaint.setDither(true);
    /**设置笔触样式-圆*/mWaterWavePaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mWaterWavePaint.setStrokeJoin(Paint.Join.ROUND);

//    if (mCircleBeanList != null) {
//      MLogUtils.e("绘制波纹-添加画笔-外层", "" + mCircleBeanList.size());
//      for (int i = 0; i < mCircleBeanList.size(); i++) {
//        Paint mPaint = new Paint();
//        MLogUtils.e("绘制波纹-添加画笔", "" + mCircleBeanList.get(i).getmRadius() + "主圆半径" + mInitRadius);
//        /**画笔颜色*/mPaint.setColor(ContextCompat.getColor(mContext, R.color.sign_btn_normal));
//        /**设置画笔透明度*/mPaint.setAlpha(mCircleBeanList.get(i).getmAlpha());
//        /**画笔样式*/mPaint.setStyle(Paint.Style.FILL);
//        /**画笔粗细*/mPaint.setStrokeWidth(1f);
//        /**抗锯齿*/mPaint.setAntiAlias(true);
//        /**防抖动*/mPaint.setDither(true);
//        /**设置笔触样式-圆*/mPaint.setStrokeCap(Paint.Cap.ROUND);
//        /**设置结合处为圆弧*/mPaint.setStrokeJoin(Paint.Join.ROUND);
//        /**添加画笔*/mCirclePaintList.add(mPaint);
//      }
//    }
  }


  /**
   * 设置计划日期
   *
   * @param mPlanDate
   */
  public void setmPlanDate(String mPlanDate) {
    this.mPlanDate = mPlanDate;
  }

  /*圆产生间隔-毫秒*/private long mCircleCreateinterval = 300;
  /*是否停止创建波纹*/private boolean mStopCreateCircle;
  /*波纹持续时间-毫秒*/private long mCircleDuration = 3000;
  /*Rx-定时器*/private Disposable mTimerRx;

  /**
   * 构建水波动画-每个指定毫秒创建一个新的波纹
   */
  private void toolBuildWaterWave() {
    mTimerRx = Observable.interval(mCircleCreateinterval, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Long>() {
          @Override
          public void accept(Long aLong) throws Exception {
            if (mStopCreateCircle) {
              if (mTimerRx != null) {
                mTimerRx.dispose();
              }
            } else {
              /*创建波纹*/
              MCircleBean mCircleBean = new MCircleBean();
              mCircleBean.setmWaterWaveDuration(3500);
              mCircleBean.setmWaterWaveInitRadius(mInitRadius);
              if (mCircleBeanList != null) {
                mCircleBeanList.add(mCircleBean);
              }
            }
            invalidate();
          }
        });
  }


  public boolean ismStopCreateCircle() {
    return mStopCreateCircle;
  }

  /**
   * 控制波纹-开启停止
   *
   * @return
   */
  public void setmStopCreateCircle(boolean mStopCreateCircle) {
    this.mStopCreateCircle = mStopCreateCircle;
    if (!mStopCreateCircle) {
      toolBuildWaterWave();
    }
  }


  /**
   * 设置波纹产生速度
   *
   * @param mCircleCreateinterval
   */
  public MWaterWaveCustomView setmCircleCreateinterval(long mCircleCreateinterval) {
    this.mCircleCreateinterval = mCircleCreateinterval;
    return this;
  }


  /**
   * 设置主圆颜色
   *
   * @param mMainColor
   */
  public MWaterWaveCustomView setmMainColor(int mMainColor) {
    this.mMainColor = mMainColor;
    return this;
  }


  /**
   * 设置文本颜色
   *
   * @param mTextColor
   */
  public MWaterWaveCustomView setmTextColor(int mTextColor) {
    this.mTextColor = mTextColor;
    return this;
  }


  /**
   * 设置波纹颜色
   *
   * @param mWaterWaveColor
   */
  public MWaterWaveCustomView setmWaterWaveColor(int mWaterWaveColor) {
    this.mWaterWaveColor = mWaterWaveColor;
    return this;
  }


  /**
   * 设置波纹持续时间
   *
   * @param mCircleDuration
   */
  public MWaterWaveCustomView setmCircleDuration(long mCircleDuration) {
    this.mCircleDuration = mCircleDuration;
    return this;
  }

  /*波纹样式*/public static int MWATERWAVE_TYPE = 0;
  /*波纹样式-实心*/public static int MWATERWAVE_TYPE_FILL = 0;
  /*波纹样式-空心*/public static int MWATERWAVE_TYPE_STROKE = 1;

  /**
   * 修改波纹样式
   */
  public MWaterWaveCustomView toolSetCircleType(int type) {
    MWATERWAVE_TYPE = type;
    if (mWaterWavePaint != null) {
      mWaterWavePaint.setStyle(MWATERWAVE_TYPE == MWATERWAVE_TYPE_FILL ? Paint.Style.FILL : Paint.Style.STROKE);
      mWaterWavePaint.setStrokeWidth(MWATERWAVE_TYPE == MWATERWAVE_TYPE_FILL ? 1f : 5f);
    }
    return this;
  }

}
