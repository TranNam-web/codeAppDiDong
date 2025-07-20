package com.example.doandidong.retrofit;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationIntercetor implements Interceptor {
    private String authoToken;

    public AuthorizationIntercetor(String authoToken) {
        this.authoToken = authoToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();  // Đảm bảo 'original' được khai báo đúng
        Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + authoToken)  // Thêm dấu cách giữa "Bearer" và token
                .method(original.method(), original.body());
        Request request = builder.build();
        return chain.proceed(request);
    }
}
