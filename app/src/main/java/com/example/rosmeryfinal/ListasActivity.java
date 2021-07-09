package com.example.rosmeryfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rosmeryfinal.More.Pokemon;
import com.example.rosmeryfinal.More.PokemonAdapter;
import com.example.rosmeryfinal.More.PokemonService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);

        RecyclerView rv = findViewById(R.id.rvPokemons);
        rv.setHasFixedSize(true); findViewById(R.id.rvPokemons);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokemonService service = retrofit.create(PokemonService.class);
        Call<List<Pokemon>> pokemon = service.getAll();

        pokemon.enqueue(new Callback<List<Pokemon>>() {
            @Override
            public void onResponse(Call<List<Pokemon>> call, Response<List<Pokemon>> response) {
                if(response.code() == 200){
                    List<Pokemon> pokemons  = response.body();
                    PokemonAdapter adapter = new PokemonAdapter(pokemons);
                    adapter.OnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),
                                    "Seleccion: " + pokemons
                                            .get(rv.getChildAdapterPosition(v)).getNombre(),Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ListasActivity.this, DetalleActivity.class);
                            String clase = new Gson().toJson(pokemons.get(rv.getChildAdapterPosition(v)));
                            intent.putExtra("clase",clase);
                            startActivity(intent);
                        }
                    });
                    rv.setAdapter(adapter);
                }else {
                    Log.i("Web","Conexion mal");
                }
            }
            @Override
            public void onFailure(Call<List<Pokemon>> call, Throwable t) {
                Log.i("Web","No conexion");
            }
        });
    }
}