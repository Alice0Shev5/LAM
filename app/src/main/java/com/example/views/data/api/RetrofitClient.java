package com.example.views.data.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;


// lt --port 8080
public class RetrofitClient {
    private static final String BASE_URL = "https://quiet-peaches-travel.loca.lt"; // http://192.168.56.1:8080/
    private static Retrofit retrofit = null;

    public static EntertainmentApiService getApiService() {
        if (retrofit == null) {
            // Создаем и настраиваем OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Таймаут соединения
                    .readTimeout(30, TimeUnit.SECONDS)     // Таймаут чтения
                    .writeTimeout(30, TimeUnit.SECONDS)    // Таймаут записи
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)  // Передаем настроенный клиент
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(EntertainmentApiService.class);
    }
}