package com.example.jon.fangreader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by jon on 2017/1/2.
 */

public class WelcomeActivity extends Activity {
    @BindView(R.id.iv_welcome)
    ImageView mIVWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mIVWelcome.animate().scaleX(1.1f).scaleY(1.1f).setDuration(2000).setStartDelay(100).start();
        Observable.timer(2500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                       goHome();
                    }
                });
        App.getInstance().addActivity(this);
    }

    private void goHome() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().removeActivity(this);
    }
}
