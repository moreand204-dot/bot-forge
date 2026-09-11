package com.lordescanor.botforge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            SharedPreferences sp = getSharedPreferences("botforge_auth", MODE_PRIVATE);
            if (sp.getBoolean("logged_in", false)) {
                openDashboard();
            } else {
                new LoginDialog(this).show();
            }
        }, 700);
    }

    public void openDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
