package com.example.dantas.calendallpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginRegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
    }

    public void goLogin(View v) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void goRegister(View v) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }
}
