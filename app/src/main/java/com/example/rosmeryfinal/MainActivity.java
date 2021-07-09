package com.example.rosmeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rosmeryfinal.More.Entrenador;
import com.example.rosmeryfinal.More.EntrenadorService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button listar = findViewById(R.id.Pokemones);
        Button atrapar = findViewById(R.id.Capturados);
        Button registrar = findViewById(R.id.Crear);
        Button entrenador = findViewById(R.id.CrearEntrenador);
        ImageView image = findViewById(R.id.EntrenadorIMG);
        TextView nombres = findViewById(R.id.Nombres);
        TextView pueblos = findViewById(R.id.Pueblo);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EntrenadorService service = retrofit.create(EntrenadorService.class);
        Call<Entrenador> entrenador2 = service.getOne();

        entrenador2.enqueue(new Callback<Entrenador>() {
            @Override
            public void onResponse(Call<Entrenador> call, Response<Entrenador> response) {
                if(response.code() == 200){
                    Entrenador entrenador3  = response.body();
                    nombres.setText(entrenador3.getNombres());
                    pueblos.setText(entrenador3.getPueblo());

                    Picasso.get().load( entrenador3.getImagen()).into(image);

                }else {
                    Log.i("Web","F");
                }
            }

            @Override
            public void onFailure(Call<Entrenador> call, Throwable t) {
                Log.i("Web","Sin conexion");
            }
        });

        entrenador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EntrenadorActivity.class);
                startActivity(intent);
            }
        });

        listar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListasActivity.class);
                startActivity(intent);
            }
        });
        registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NuevoPokemonActivity.class);
                startActivity(intent);
            }
        });

        atrapar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }
}