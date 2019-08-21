package jinye.demo.mcustomwaterripple;

/**
 * Created by 今夜犬吠 on 2018/1/18.
 * 自定义水波-基类
 * 封装一些公共属性
 */

public abstract class BaseWaterWave {

  /*水波持续时间*/public long mWaterWaveDuration=2000;
  /*水波原始半径*/public float mWaterWaveInitRadius=80f;



  public long getmWaterWaveDuration() {
    return mWaterWaveDuration;
  }

  public void setmWaterWaveDuration(long mWaterWaveDuration) {
    this.mWaterWaveDuration = mWaterWaveDuration;
  }

  public float getmWaterWaveInitRadius() {
    return mWaterWaveInitRadius;
  }

  public void setmWaterWaveInitRadius(float mWaterWaveInitRadius) {
    this.mWaterWaveInitRadius = mWaterWaveInitRadius;
  }

  /**
   * 获取水波半径
   * @return
   */
  protected abstract float toolGetWaterWaveRadius(float mMaxRadius);

  /**
   * 获取水波透明度
   */
  protected abstract int toolGetWaterWaveAlpha();
}
