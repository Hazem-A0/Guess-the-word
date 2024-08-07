package com.example.guesstheword;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("word?number=1")
    Call<String[]> getRandomWord();
}
