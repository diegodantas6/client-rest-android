package com.example.dantas.calendallpro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dantas.calendallpro.http.AsyncHttpInterface;
import com.example.dantas.calendallpro.http.AsyncHttpTask;
import com.google.gson.Gson;

import java.math.BigDecimal;

import com.example.dantas.calendallpro.model.Produto;

public class LoginActivity extends Activity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {

//        String user = mEmailView.getText().toString();
//        String pass = mPasswordView.getText().toString();
//
//        Usuario usuario = new Usuario(user, pass);
//
//        Gson gson = new Gson();
//        String jsonInString = gson.toJson(usuario);

        Produto produto = new Produto();

        produto.setId(1L);
        produto.setNome("Nome novo");
        produto.setQuantidade(202L);

        Gson gson = new Gson();
        String json = gson.toJson(produto);

        AsyncHttpTask asyncHttpTask = new AsyncHttpTask(this, new AsyncHttpInterface() {
            @Override
            public void postHttp(String string) {
                onPost(string);
            }
        });
        //asyncHttpTask.execute("produto", "PUT", json);
        //asyncHttpTask.execute("produto", "DELETE", json);
        asyncHttpTask.execute("produto/3", "GET", null);
    }

    public void onPost(String string) {
        Gson gson = new Gson();
        Produto produto = gson.fromJson(string, Produto.class);
//        Produto[] produtos = gson.fromJson(string, Produto[].class);

        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

}
