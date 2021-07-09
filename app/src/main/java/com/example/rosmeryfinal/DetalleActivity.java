package com.example.rosmeryfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosmeryfinal.More.Pokemon;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        ImageView imagen = findViewById(R.id.imagenPokemon);
        TextView nombre = findViewById(R.id.nombreP);
        TextView tipo = findViewById(R.id.tipoP);
        Button irA = findViewById(R.id.goP);

        Intent intent = getIntent();
        String clase = intent.getStringExtra("clase");
        Pokemon pokemon = new Gson().fromJson(clase,Pokemon.class);

        nombre.setText(pokemon.getNombre());
        tipo.setText(pokemon.getTipo());

        Picasso.get().load( "https://upn.lumenes.tk/" + pokemon.getUrl_imagen()).into(imagen);

        irA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Dirigue al mapa no implemento",Toast.LENGTH_SHORT).show();
            }
        });
    }
}