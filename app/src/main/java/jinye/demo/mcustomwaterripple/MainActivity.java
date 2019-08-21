package jinye.demo.mcustomwaterripple;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

  private MWaterWaveCustomView mWaterWaveCustomView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolInitView();
    toolSetListener();
  }

  private void toolInitView() {
    mWaterWaveCustomView = findViewById(R.id.View_MianActivity_WaterWaveCustomView);
  }

  @SuppressLint("CheckResult")
  private void toolSetListener() {
    /*各种点击事件*/
    Observable mObservable = RxView.clicks(mWaterWaveCustomView).share();

    mObservable
        .buffer(mObservable.debounce(200, TimeUnit.MILLISECONDS))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Object>>() {
          @Override
          public void accept(List<Object> voids) throws Exception {
            if (voids.size() == 2) {
              if (mWaterWaveCustomView.ismStopCreateCircle()) {
                mWaterWaveCustomView.setmStopCreateCircle(false);
              } else {
                mWaterWaveCustomView.setmStopCreateCircle(true);
              }
            } else if (voids.size() == 3) {
              if (MWaterWaveCustomView.MWATERWAVE_TYPE == 0) {
                mWaterWaveCustomView.toolSetCircleType(MWaterWaveCustomView.MWATERWAVE_TYPE_STROKE);
              } else {
                mWaterWaveCustomView.toolSetCircleType(MWaterWaveCustomView.MWATERWAVE_TYPE_FILL);
              }

            }
          }
        });

  }
}
