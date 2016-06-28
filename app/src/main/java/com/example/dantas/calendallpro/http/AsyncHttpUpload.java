package com.example.dantas.calendallpro.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dantas.calendallpro.utils.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncHttpUpload extends AsyncTask<Bitmap, Void, Void>{

    private ProgressDialog progressDialog;

    private Context context;

    public AsyncHttpUpload(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (Utils.isConnected(context)) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Inicio!");
            progressDialog.show();
        } else {
            Toast.makeText(context, "Favor conectar a internet!", Toast.LENGTH_LONG).show();
            super.cancel(true);
        }
    }

    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        try {
            URL url = new URL("http://192.168.0.13:8080/teste/rest/usuario/upload");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type","multipart/form-data");

            connection.setRequestProperty("user", "diego");
            connection.setRequestProperty("pass", "123");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            OutputStream os = connection.getOutputStream();
            os.write( byteArray );
            os.close();

            os.flush();


        }catch(Exception e) {
            Log.e("ERRO:", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }
}
