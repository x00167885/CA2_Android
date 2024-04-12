package com.example.eventplanner.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://ead2-ca2-api.azurewebsites.net/"; // Our API hosted on Azure.
    public static APIService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Using GSON to parse response.
                    .build();
        }
        return retrofit.create(APIService.class);
    }
}
