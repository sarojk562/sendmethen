package com.sendmethen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sendmethen.models.PublicMessages;
import com.sendmethen.retrofit.RestService;
import com.sendmethen.retrofitmodels.RequestOneFeed;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SingleMessageActivity extends AppCompatActivity {
    public String msg_id;
    public static final String ROOT_URL="http://sendmethen.feturtles.com/";
    public PublicMessages one_message;
    public RequestOneFeed requestOneFeed;
    public TextView msgTitle,msgText;
    public Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                msg_id= null;
            } else {
                msg_id= extras.getString("id");
            }
        } else {
            msg_id= (String) savedInstanceState.getSerializable("id");
        }

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();
        RestService api = adapter.create(RestService.class);
        requestOneFeed=new RequestOneFeed(msg_id);

        api.getOneMessages(requestOneFeed, new Callback<List<PublicMessages>>() {
            final ProgressDialog loading = ProgressDialog.show(context,"Fetching Letter","Please wait...",false,false);
            @Override
            public void success(List<PublicMessages> publicMessages, Response response) {
                if(publicMessages.size()>0){
                    one_message=publicMessages.get(0);
                };
//                Log.w("My app",new Gson().toJson(one_message));
                msgTitle=(TextView) findViewById(R.id.msgTitle);
                msgText=(TextView) findViewById(R.id.msgText);
                msgTitle.setText(one_message.subject);
                msgText.setText(one_message.text);

                loading.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("My app","App could not fetch the data");
                error.printStackTrace();
                loading.dismiss();
            }
        });


    }
}
