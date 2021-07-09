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

import com.example.rosmeryfinal.More.Pokemon;
import com.example.rosmeryfinal.More.PokemonService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NuevoPokemonActivity extends AppCompatActivity {

    ImageView imagenes;
    String imgDecodableString;
    EditText imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pokemon);

        EditText nombre = findViewById(R.id.nombrePokemon);
        EditText tipo = findViewById(R.id.tipoPokemon);
        imagen = (EditText) findViewById(R.id.image);
        EditText latitud = findViewById(R.id.latitud);
        EditText longitud = findViewById(R.id.longitud);
        Button crear = findViewById(R.id.CrearnewPokemon);
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
                String tipos = tipo.getText().toString().trim();
                String image = imagen.getText().toString().trim();
                String latitude = latitud.getText().toString().trim();
                String longitude = longitud.getText().toString().trim();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://upn.lumenes.tk")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PokemonService service = retrofit.create(PokemonService.class);

                Pokemon pokemon = new Pokemon();
                pokemon.setNombre(name);
                pokemon.setTipo(tipos);
                pokemon.setUrl_imagen(image);
                pokemon.setLatitude(latitude);
                pokemon.setLongitude(longitude);

                Call<Pokemon> call = service.create(pokemon);

                call.enqueue(new Callback<Pokemon>() {
                    @Override
                    public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                        if (response.code() == 200) {
                            Log.i("Web", "ok");
                        } else {
                            Log.i("Web", "Mal");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pokemon> call, Throwable t) {
                        Log.i("Web", "No conexion servidor");
                    }
                });
                Intent intent = new Intent(NuevoPokemonActivity.this, MainActivity.class);
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
                Toast.makeText(this, "No haz escogido una imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Algo salio mal", Toast.LENGTH_LONG)
                    .show();
        }
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