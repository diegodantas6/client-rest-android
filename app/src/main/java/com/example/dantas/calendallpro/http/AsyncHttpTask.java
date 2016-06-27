package com.example.dantas.calendallpro.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dantas.calendallpro.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncHttpTask extends AsyncTask<String, Void, String>{

    private ProgressDialog progressDialog;

    private Context context;
    private AsyncHttpInterface asyncHttpInterface;

    public AsyncHttpTask(Context context, AsyncHttpInterface asyncHttpInterface) {
        this.context = context;
        this.asyncHttpInterface = asyncHttpInterface;
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
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://192.168.0.13:8080/teste/rest/".concat(strings[0]));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            //connection.setRequestMethod("GET");
            //connection.setRequestMethod("POST");
            connection.setRequestMethod(strings[1]);

            connection.setRequestProperty("Content-Type","application/json");

            connection.setRequestProperty("user", "diego");
            connection.setRequestProperty("pass", "123");

            if (strings[2] != null) {
                byte[] outputInBytes = strings[2].getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write( outputInBytes );
                os.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();

            String tmp = "";

            while((tmp=reader.readLine()) != null)
                json.append(tmp);

            reader.close();

            return json.toString();
        }catch(Exception e) {
            Log.e("ERRO:", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncHttpInterface.postHttp(s);
        progressDialog.dismiss();
    }
}
