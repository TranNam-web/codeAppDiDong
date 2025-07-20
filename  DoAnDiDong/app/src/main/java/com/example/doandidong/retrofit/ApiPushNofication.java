package com.example.doandidong.retrofit;

import com.example.doandidong.model.MessageData;
import com.example.doandidong.model.NotiResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                    "Content-Type: application/json",

            }

    )
    @POST("projects/appbanhang-5cc7f/messages:send")
    Observable<NotiResponse> sendNofitication(@Body MessageData data);

}
