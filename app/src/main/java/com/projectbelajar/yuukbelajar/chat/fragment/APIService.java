package com.projectbelajar.yuukbelajar.chat.fragment;

import com.projectbelajar.yuukbelajar.chat.Notifications.MyResponse;
import com.projectbelajar.yuukbelajar.chat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADqh8OCM:APA91bGRbpkgAZYIhQlDZn1JQ1ef3WPGK6b4LZhqcaDc-B9OBI4TZR7eTdyebYgl1WShnCVEjTsgbxutlMWwI3uKY6eO_Nd9Vhl68wXknIte2EB82meTZmMInx0QEVfn8C9-VzT2F4_4"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
