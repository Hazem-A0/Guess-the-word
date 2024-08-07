package com.example.guesstheword;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://random-word-api.herokuapp.com/"; // Base URL of the API
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set the base URL for the API
                    .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
                    .build();
        }
        return retrofit;
    }
}