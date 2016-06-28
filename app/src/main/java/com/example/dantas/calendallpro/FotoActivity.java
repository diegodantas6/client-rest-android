package com.example.dantas.calendallpro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dantas.calendallpro.http.AsyncHttpInterface;
import com.example.dantas.calendallpro.http.AsyncHttpTask;
import com.example.dantas.calendallpro.http.AsyncHttpUpload;
import com.example.dantas.calendallpro.model.Produto;
import com.example.dantas.calendallpro.model.Usuario;
import com.example.dantas.calendallpro.utils.GsonHelper;
import com.example.dantas.calendallpro.utils.RoundImage;
import com.example.dantas.calendallpro.utils.Utils;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.NoSuchElementException;

public class FotoActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private RoundImage roundImage;
    private ImageView foto;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        foto = (ImageView) findViewById(R.id.foto);

        photo = BitmapFactory.decodeResource(getResources(),R.drawable.user);
        roundImage = new RoundImage(photo);
        foto.setImageDrawable(roundImage);
    }

    public void openCamera(View v) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),CAMERA_REQUEST);

        foto.setImageDrawable(null);
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            try {
//                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                roundImage = new RoundImage(photo);
//                foto.setImageDrawable(roundImage);

                if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                    beginCrop(data.getData());
                } else if (requestCode == Crop.REQUEST_CROP) {
                    handleCrop(resultCode, data);
                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            //resultView.setImageURI(Crop.getOutput(result));
            //foto.setImageURI(Crop.getOutput(result));

            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                roundImage = new RoundImage(photo);
                foto.setImageDrawable(roundImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void upload(View v) {
        Usuario usuario = new Usuario();

        usuario.setNome("Android 2");
        usuario.setUser("and2");
        usuario.setPass("3332");
        usuario.setFoto(Utils.bitmapToByte(photo));

        //Gson gson = GsonHelper.customGson;
        Gson gson = new Gson();

        String json = gson.toJson(usuario);

        AsyncHttpTask asyncHttpTask = new AsyncHttpTask(this, new AsyncHttpInterface() {
            @Override
            public void postHttp(String string) {
                onPost(string);
            }
        });

        asyncHttpTask.execute("usuario", "POST", json);

//        String json2 = "{\"nome\":\"Marreta\",\"user\":\"mar\",\"pass\":\"333\",\"foto\":\"iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAY1BMVEXp6ekyicju7Orv7eosh8cjhMcbgsbz7+vm6OnS3eVqo9GlwNpZm86FsdWtx923zd/D1OE6jcnd4+ePttdhn8/K2eO0y95Hk8va4eaWuth7q9PG1uKCr9VHkst2qdJTmM2pw9zjWxOzAAAHyElEQVR4nO2d25aqMAyGIU05iXLwBKLi+z/lLqgz6lbk0KbtLL6bvfZcjP6T0KYhTRxnZmZmZmZmZmZmZmZmZmZmZmZmZmZmZmZmZsYyAIABaxH/iP/p/kIyAeYHYborl+foynG9SKpCKP4LMoWxtrtjnLmcc/yh+U+2j8o0t1wl+HlyOGErzX2lFerGZSVsqfuLjgSC3cblb7Q9yRTGXG9t1Aj+9vhN3l0l5/UqsEwjsHTTT95dJC5zpvtb9wcg2Xv95d0NucxtsSNU8VB9LZyvrfBVyM+j9LUas5X5mwesXD5SnwC9TWi2RCg2ow1404gL3SK6gJ07TV+DFxm84qwnGvBmxiw1c+OAYDPhCXySyBcmWhHCkwwDXvGW5klkqYRH8Bce6Rb0CiQDYrR+EgPdmp6ARKoFW4mxSRKZfIFmOSpsFQgUEs+mLDdQSFxFnySasqIGsRqBQqIZ+yKcJW30/4OYGiCRLZQJbAK4XLc+scpI3ghfJOpfUIO9SoEGPIqwVOijDYh6j8RQKRYoJG50CnScWq2PNvCVRiPCxVMuUKynGgPUPFNvQmHEtTYjQqn8KWxALHQpzJVuhb9oi0+JTKjRiIGSM9M7ND2JoDIgfQZdLcspKI7XHuEXDUaElGAvvIO1hhwxO9CZ0HW9rQYjkj2FDRrWGkhIFWLmkys8UzqpDjf1aQW6vCRWCBXhStqANbGbEm73NzxagQ7bEHupyxNaN/WJjhUPCokfxJDaSV2MSMMa4t2wVeiSLjVkR8MHOGn6m3q/bxVWlA8iU/a6qUPhjlKhT5Jke1FIm9+nFyiOF5SLaa5BIR4obRjqUBhRKtxqUUjppVoUkuZqZoVKFO7/vML4zyskXUuLP69Qx47Pl5ReSn/Epz7k+4RvZX4UklYssEiDQtIaN6a6UOidwpBQoAMrDQpJj4eQ0meiiJPeAb3CI+1LUl9R4fNnqIu/2JJaoUe60DS31IjdFF3qN/kB9ds18qsJPkHd5SO02dIG6rw+p68ZKkjdlPbodIWRBt86CoVp33MjuT6HNqzBo47iRMqyLy1FX46zJVtrcKOnSBjIjsEecR3Gj0Kq+kuMdRXrA1FRDXUpzYNC9XeCGjDWpM+hKligTUG9EBLkTfVeeCaIv9HVdmOmJVB+iNJ9xRJSxX6q+/qhcj/FzICuSko3Ra3XK29AobA+yoyuCgofRdyY0d9E2VkYT6b0xGJrJRLRNWCVuaGkNYYZTTHuQCRdInKzeppJl4j6jkwfkOyoyCujLNjgy3zxja6e1FM3/oXL2hf5qTBQYNOUTk50gzwytVMrFLEET0Wkvog3iPXkCI7vSe9VDAbSetLTiGh8v2QIyvGtFpDXZm3z72HheZyrIs8Wpi4xzwA07cpH6Fub7qC/gJNEQ1qyX/UVFjjoL+BUB7evIRG9emGP/e4AFIvY+y4SkbuH1I7n7z/ACcuYd7irUOdl51VgfiP2z4CTr44199oBJU/akHPPjcrKanlXhIKguqyj2vV+4Fl8Lneh83cG6kCDiFvDK8HtB1ZgydccC0CwUv0RgcYZNCJ2OXterHQjYyse7zQ5CsCubgYFoJsoi0UgOHgiHNASrt71tQHXUtEXgGrPr5/gltQaIX0cJaPm0ApO+RMRibD1QumrUJyfozEVZoSqfkyJiNCVLAUOzuX/UTL8JHc9YPnxNaRFfqCJziGM3oXT6G3kuSoEi+xNTotnFHlwWH1KUoi/sZx3ReJ0uX9/JlG3qP1+ePCf8zz+jXEZTt6fhb7482fwWm0yHMLuhChyPGwnxZssWHXoaz4iU1m0D9+T2uJUtEmCkSEAQFievp2ZUeFdUlj1HPZ3Wo8wpIhxk6hX4oMfVKhz2tf1ffMuHOtFOGT+JjAnPZ76Jq8UzaCBQS/rG5Fl6vRRCcwvducMByQgeaxgSR1ejdDM34wWaeB/POs2A3RZmKzrnsMuH373XvrrN39UuUUzZRT3h0USBn47CPgOE4ZjebVbR8J2Q+W1v/kkufPXOIE3le1sXLeOjsvFjXJ53pyuPx/7jkNy4bC/mFyzfpsCfOPt+Nxh8Fqin7IV8WXDXnB5PYdIuz4PgB8l3e6GQkMTjF54ksqHVU+SmYAn5VDM5Bd0SUNKlTvT0OWyP1hPFqi8Un0i04uIc3MfwitT73grnKkmCcwmPYpMQ5OWoUy75a2yDF8aUy7VmO+jDZiN7glC3t1jJOMvtxm/jt4Zu54qumOgADyNytsAxe1JSYybYEJ3F306o4YkaugrP4FR/XmIO+xMZHjDdjAycfGZEYkpW3aKO0MbS9hmwhFGtM2EQ41onwmHGhHIewZKYIgRIbHPhMPa9OiYzyGB/q2WgK49klT6j4dgGobISKH3rJ3cpoj0kb41DPQj1WSBWT+FzMat4kq/DUNDE2Rp9BtjAqQTVCXj9Vpr7Ele/E+f3KktKcT34P77a2EdHfMl0qM1fWC1wB5bot1O2sdNrY3Y7nx3UxveNnXxbTWF1HKB4pTY7abDSixNBN3udxjMrjzwO7507inG90Mwhe5BkHa9rHhP9wA6e14Zfqa7TsrSFNQznS1rc9t3w4auwA2qPyCw8xisY4qTfPD02Ybs+Cds6H4OTRlRB3LFdPRbNLgaeAj88lGhFWVs3+kYBaljuqgCXhbTfwt5jqa5kvl2AAAAAElFTkSuQmCC\"}";
//        asyncHttpTask.execute("usuario", "POST", json2);
    }

    public void onPost(String string) {
        //Gson gson = new Gson();
        Gson gson = GsonHelper.customGson;

        Usuario usuario = gson.fromJson(string, Usuario.class);

        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

}
