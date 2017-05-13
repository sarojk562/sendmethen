package com.sendmethen;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sendmethen.adapters.PublicMessagesAdapter;
import com.sendmethen.models.PublicMessages;
import com.sendmethen.retrofit.RestService;
import com.sendmethen.retrofitmodels.SendTextMsg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TextSendActivity extends AppCompatActivity {
    private static final String ROOT_URL ="http://sendmethen.feturtles.com/" ;
    private Button datePickerbtn;
    private EditText futureDateinp;
    private Toolbar toolbar;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private int year, month, day;
    private EditText msgSubject,msgEmail,msgText;
    private Boolean isPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_send);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        futureDateinp=(EditText) findViewById(R.id.futureDateinp);
        datePickerbtn= (Button) findViewById(R.id.datePickerbtn);
        futureDateinp= (EditText) findViewById(R.id.futureDateinp);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton1) {
                    isPublic = true;
                } else if(checkedId == R.id.radioButton2) {
                    isPublic = false;
                }
            }
        });

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);


        datePickerbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            setDate();


            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sendmethen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            // Something else
            case R.id.doneBtn:
                Log.w("My app","Submitted");
                sendTextMsg();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendTextMsg() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();
        RestService api = adapter.create(RestService.class);
        SendTextMsg textmsg= new SendTextMsg();
        msgText=(EditText) findViewById(R.id.msgText);
        msgEmail=(EditText) findViewById(R.id.msgEmail);
        msgSubject=(EditText) findViewById(R.id.msgSubject);

        textmsg.type="text";
        textmsg.text=msgText.getText().toString();
        textmsg.subject=msgSubject.getText().toString();
        textmsg.email=msgEmail.getText().toString();
        textmsg.is_public=isPublic;

        api.sendTextMsg(textmsg,new Callback<HashMap>() {
            int return_obj;
            @Override
            public void success(HashMap responseVar, Response response) {
                Log.w("My app succeded",new Gson().toJson(responseVar));
//                return_obj=1;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("My APp","App could not post the data");
                error.printStackTrace();

            }

        });

    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int i, int day) {
        futureDateinp.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
