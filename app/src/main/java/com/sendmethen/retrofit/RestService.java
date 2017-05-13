package com.sendmethen.retrofit;

import com.sendmethen.models.PublicMessages;
import com.sendmethen.retrofitmodels.RequestOneFeed;
import com.sendmethen.retrofitmodels.SendTextMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by adam on 25/6/16.
 */


public abstract interface RestService {

    @GET("/messages/read_public")
    public abstract void getPublicMessages(Callback<ArrayList<PublicMessages>> response);

    @POST("/messages/read_public/one_post")
    public abstract void getOneMessages(@Body RequestOneFeed request, Callback<List<PublicMessages>> result);

    @POST("/messages/new/")
    public abstract void sendTextMsg(@Body SendTextMsg request, Callback<HashMap> result);

    @Multipart
    @POST("/messages/upload_voice")
    void upload_audio(@Part("myfile") TypedFile file,
                Callback<HashMap> result);
}



