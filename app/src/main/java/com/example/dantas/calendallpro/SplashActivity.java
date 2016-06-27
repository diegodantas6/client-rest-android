package com.example.dantas.calendallpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void goLoginRegister(View v) {
        Intent loginRegister = new Intent(this, LoginRegisterActivity.class);
        startActivity(loginRegister);
        finish();
    }

}
