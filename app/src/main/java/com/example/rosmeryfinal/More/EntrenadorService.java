package com.example.rosmeryfinal.More;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EntrenadorService {
    @GET("entrenador/N00033835")
    Call<Entrenador> getOne();
    @POST("entrenador/N00033835")
    Call<Entrenador> createE(@Body Entrenador entrenador);
}
