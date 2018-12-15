package com.example.megam.medispachecliente.fragments;

import com.example.megam.medispachecliente.Notifications.MyResponse;
import com.example.megam.medispachecliente.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAq1gQ5O8:APA91bGHUyrGpUgK5OHN2UoQD7AEAxk6uqgJvnLbZsGCrmtLv6uf9eH6hBjEA58r13r_3SKLz7U6bUdobPkvFhgMIvVE61d3SCCdHRw8Y0g1xpKXoDpee5Hnrs8cgVTgXNyDUePyNMsE"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
