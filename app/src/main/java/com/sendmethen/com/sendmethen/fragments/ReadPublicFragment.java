package com.sendmethen.com.sendmethen.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.sendmethen.R;
import com.sendmethen.SingleMessageActivity;
import com.sendmethen.adapters.PublicMessagesAdapter;
import com.sendmethen.models.PublicMessages;
import com.sendmethen.retrofit.RestService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by adam on 5/7/16.
 */
public class ReadPublicFragment extends Fragment implements ListView.OnItemClickListener{
    public ListView publicmsgListView;
    public LinearLayout msgRow;
    public ArrayList<PublicMessages> messages;
    public static final String ROOT_URL="http://sendmethen.feturtles.com/";
    public ReadPublicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_read_public, container, false);
        publicmsgListView=(ListView) rootview.findViewById(R.id.publicMsgList);
        getPublicMessages();
        Log.e("My error","Just testing");
        publicmsgListView.setOnItemClickListener(this);
        // Attacheventlistener to the list item
        msgRow=(LinearLayout) rootview.findViewById(R.id.msgRow);

        return rootview;
    }
    public void getPublicMessages(){

        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Fetching Data","Please wait...",false,false);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();
        RestService api = adapter.create(RestService.class);
        api.getPublicMessages(new Callback<ArrayList<PublicMessages>>() {
            int return_obj;
            @Override
            public void success(ArrayList<PublicMessages> publicMessages, Response response) {
                messages=publicMessages;
                Log.w("My App",new Gson().toJson(publicMessages));
                PublicMessagesAdapter adapter=new PublicMessagesAdapter(getActivity(),R.layout.public_message_item,publicMessages);
                publicmsgListView.setAdapter(adapter);
                loading.dismiss();
                return_obj=1;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("My retrofit error", "Could not fetch the data");
                error.printStackTrace();
                loading.dismiss();
                return_obj=0;

            }

        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w("My App", Integer.toString(position));
        PublicMessages message= messages.get(position);
        Log.w("My App", new Gson().toJson(message));
        Intent intent=new Intent(getActivity(), SingleMessageActivity.class);
        intent.putExtra("id",message._id);
        startActivity(intent);
    }
}
