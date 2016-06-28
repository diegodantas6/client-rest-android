package com.example.dantas.calendallpro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dantas.calendallpro.http.AsyncHttpInterface;
import com.example.dantas.calendallpro.http.AsyncHttpTask;
import com.example.dantas.calendallpro.model.Produto;
import com.example.dantas.calendallpro.model.Usuario;
import com.example.dantas.calendallpro.utils.GsonHelper;
import com.example.dantas.calendallpro.utils.RoundImage;
import com.example.dantas.calendallpro.utils.Utils;
import com.google.gson.Gson;

public class TesteActivity extends Activity {

    private TextView textView;
    private EditText idUsuario;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        textView = (TextView) findViewById(R.id.textView);
        idUsuario = (EditText) findViewById(R.id.idUsuario);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void buscar(View v) {
        buscaDados(idUsuario.getText().toString());
    }

    private void buscaDados(String id) {
        AsyncHttpTask asyncHttpTask = new AsyncHttpTask(this, new AsyncHttpInterface() {
            @Override
            public void postHttp(String string) {
                onPost(string);
            }
        });

        asyncHttpTask.execute("usuario/".concat(id), "GET", null);
    }

    private void onPost(String s) {
//        Gson gson = new Gson();
//        Usuario usuario = gson.fromJson(s, Usuario.class);

        Gson gson = GsonHelper.customGson;
        Usuario usuario = gson.fromJson(s, Usuario.class);

        textView.setText(usuario.getNome());

        if (usuario.getFoto() != null) {
            Bitmap bitmap = Utils.byteToBitmap(usuario.getFoto());
            RoundImage roundImage = new RoundImage(bitmap);
            imageView.setImageDrawable(roundImage);
        }

    }

}
