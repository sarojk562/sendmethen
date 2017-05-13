package com.sendmethen.com.sendmethen.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sendmethen.AudioSendActivity;
import com.sendmethen.R;
import com.sendmethen.TextSendActivity;

/**
 * Created by adam on 5/7/16.
 */
public class SendMeFragment extends Fragment {
    private Button sendTextBtn,sendAudioBtn;

    public SendMeFragment() {
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
        View rootView= inflater.inflate(R.layout.fragment_send_me, container, false);
        sendTextBtn= (Button) rootView.findViewById(R.id.sendTextBtn);
        sendAudioBtn= (Button) rootView.findViewById(R.id.sendAudioBtn);

        sendTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), TextSendActivity.class);
                startActivity(intent);
            }
        });

        sendAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AudioSendActivity.class);
                startActivity(intent);
            }
        });




        return rootView;
    }
}
