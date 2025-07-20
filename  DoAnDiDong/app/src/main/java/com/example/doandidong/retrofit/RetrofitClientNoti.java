package com.example.doandidong.retrofit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientNoti {
    private static Retrofit instance;

    // Phương thức synchronized để đảm bảo thread-safe
    public static synchronized Retrofit getInstance(OkHttpClient client) {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/v1/")  // Base URL không bao gồm endpoint cụ thể
                    .addConverterFactory(GsonConverterFactory.create())  // Gson converter cho API response
                    .client(client)  // Sử dụng OkHttpClient đã cấu hình
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // RxJava3 adapter cho calls bất đồng bộ
                    .build();
        }
        return instance;
    }
}
