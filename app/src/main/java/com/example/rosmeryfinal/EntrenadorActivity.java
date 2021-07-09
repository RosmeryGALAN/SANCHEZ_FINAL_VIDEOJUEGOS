package com.example.rosmeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rosmeryfinal.More.Entrenador;
import com.example.rosmeryfinal.More.EntrenadorService;
import com.example.rosmeryfinal.More.PokemonService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntrenadorActivity extends AppCompatActivity {
    ImageView imagenes;
    String imgDecodableString;
    EditText imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenador);

        EditText nombre = findViewById(R.id.nombres);
        EditText pueblos = findViewById(R.id.pueblo);
        imagen = (EditText) findViewById(R.id.image);
        Button crear = findViewById(R.id.crear);
        Button galeria = findViewById(R.id.galeria);
        imagenes = (ImageView) findViewById(R.id.imagen);

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent, "Seleccione aplicacion"), 5);
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = nombre.getText().toString().trim();
                String pueblo = pueblos.getText().toString().trim();
                String image = imagen.getText().toString().trim();

                Log.i("DFFUIH", image);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://upn.lumenes.tk")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EntrenadorService service = retrofit.create(EntrenadorService.class);

                Entrenador entrenador = new Entrenador();
                entrenador.setNombres(name);
                entrenador.setPueblo(pueblo);
                entrenador.setImagen(image);

                Call<Entrenador> call = service.createE(entrenador);

                call.enqueue(new Callback<Entrenador>() {
                    @Override
                    public void onResponse(Call<Entrenador> call, Response<Entrenador> response) {
                        if (response.code() == 200) {
                            Log.i("Web", "Conexiomn correcta ");
                        } else {
                            Log.i("Web", "Conexion incorrecta");
                        }
                    }

                    @Override
                    public void onFailure(Call<Entrenador> call, Throwable t) {
                        Log.i("Web", "NO pudimos conectarnos al servidor");
                    }
                });
                Intent intent = new Intent(EntrenadorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                Uri path = data.getData();
                imagenes.setImageURI(path);

                final InputStream imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgDecodableString = encodeImage(selectedImage);

            } else {
                Toast.makeText(this, "No seleccionaste una imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Algo salio mal", Toast.LENGTH_LONG)
                    .show();
        }
        Log.i("MY_APP", String.valueOf(resultCode));

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);
        imagen.setText(imgDecodableString);
        return imgDecodableString;
    }
}