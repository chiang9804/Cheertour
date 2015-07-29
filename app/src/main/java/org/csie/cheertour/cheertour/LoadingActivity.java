package org.csie.cheertour.cheertour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by rose-pro on 2015/7/14.
 */
public class LoadingActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }
}
