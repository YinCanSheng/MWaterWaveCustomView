package jinye.demo.mcustomwaterripple;


/**
 * Created by 今夜犬吠 on 2018/1/17.
 * 圆-自定义
 */

public class MCircleBean extends BaseWaterWave {

  /*透明度*/private int mAlpha;
  /*半径*/private float mRadius;
  /*创建时间*/private long mCreateTime;

  public long getmCreateTime() {
    return mCreateTime;
  }

  public void setmCreateTime(long mCreateTime) {
    this.mCreateTime = mCreateTime;
  }

  /**
   * 构造
   *
   * @return
   */
  public MCircleBean() {
    mCreateTime = System.currentTimeMillis();
  }

  public int getmAlpha() {
    return mAlpha;
  }

  public void setmAlpha(int mAlpha) {
    this.mAlpha = mAlpha;
  }

  public float getmRadius() {
    return mRadius;
  }

  public void setmRadius(float mRadius) {
    this.mRadius = mRadius;
  }


  /**
   * 获取水波半径
   *
   * @param mMaxRadius
   * @return
   */
  @Override
  public float toolGetWaterWaveRadius(float mMaxRadius) {

    return getmWaterWaveInitRadius() + (mMaxRadius - getmWaterWaveInitRadius()) * ((float) Math.pow((double) (System.currentTimeMillis() - mCreateTime) / getmWaterWaveDuration(), 3));
  }

  /**
   * 获取水波透明度
   * 255-0
   */
  @Override
  public int toolGetWaterWaveAlpha() {
    return (int) (255 - 255 * Math.sqrt((double)(System.currentTimeMillis() - mCreateTime) / getmWaterWaveDuration()));
  }
}
