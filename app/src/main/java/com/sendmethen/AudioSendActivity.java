package com.sendmethen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.opengl.Visibility;
import android.os.Environment;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.sendmethen.com.sendmethen.fragments.ProgressRecordFragment;
import com.sendmethen.com.sendmethen.fragments.StartRecordFragment;
import com.sendmethen.com.sendmethen.fragments.StopRecordFragment;
import com.sendmethen.retrofit.RestService;
import com.sendmethen.retrofitmodels.SendTextMsg;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AudioSendActivity extends AppCompatActivity {
    private static final String ROOT_URL ="http://sendmethen.feturtles.com" ;
    private Toolbar toolbar;
    private Fragment startRecordFragment,stopRecordFragment,progressRecordFragment;
    private ImageButton startRecordBtn,stopRecordBtn,playAudioBtn;
    private Button recordAgainBtn;
    private LinearLayout startRecordLayout,stopRecordLayout,playRecordLayout;
    private EditText msgSubject,msgEmail,msgText;


    public MediaRecorder mediaRecorder=new MediaRecorder();
    public MediaPlayer mediaPlayer= new MediaPlayer();

    int RECORDER_SAMPLERATE=44100;
    int RECORDER_CHANNELS= AudioFormat.CHANNEL_IN_MONO;
    int RECORDER_AUDIO_ENCODING= AudioFormat.ENCODING_PCM_16BIT;
    int  buffersize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    int PLAYER_CHANNELS=AudioFormat.CHANNEL_OUT_MONO;
    Boolean isRecording;
    int bytesPerElement=2;
    private AudioRecord instaRecorder;
    private String mFileName;
    private SendTextMsg textmsg= new SendTextMsg();
    private RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(ROOT_URL)
            .build();
    private RestService api = adapter.create(RestService.class);
    private Context context=this;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sendmethen, menu);
        MenuItem item = menu.findItem(R.id.doneBtn);
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
                sendAudioMsg();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_send);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        FragmentManager fragmentManager = getFragmentManager();
//        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        startRecordFragment= new StartRecordFragment();
//        stopRecordFragment = new StopRecordFragment();
//        progressRecordFragment = new ProgressRecordFragment();

//        fragmentTransaction.replace(R.id.fragment_container,startRecordFragment);


        startRecordBtn= (ImageButton) findViewById(R.id.startRecordBtn);
        stopRecordBtn = (ImageButton) findViewById(R.id.stopRecordBtn);
        playAudioBtn = (ImageButton) findViewById(R.id.playAudioBtn);

        recordAgainBtn = (Button) findViewById(R.id.recordAgainBtn);


        startRecordLayout= (LinearLayout) findViewById(R.id.startRecordLayout);
        stopRecordLayout= (LinearLayout) findViewById(R.id.stopRecordLayout);
        playRecordLayout= (LinearLayout) findViewById(R.id.playRecordLayout);

        final MenuItem doneBtn =(MenuItem) findViewById(R.id.doneBtn);

        startRecordLayout.setVisibility(View.VISIBLE);



        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecorder();
                startRecordLayout.setVisibility(View.GONE);
                stopRecordLayout.setVisibility(View.VISIBLE);
//                fragmentTransaction.replace(R.id.fragment_container,progressRecordFragment);
            }
        });

        stopRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecorder();
                stopRecordLayout.setVisibility(View.GONE);
                playRecordLayout.setVisibility(View.VISIBLE);
//                fragmentTransaction.replace(R.id.fragment_container,stopRecordFragment);
//                doneBtn.setVisible(true);
            }
        });

        playAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });
        recordAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();
                playRecordLayout.setVisibility(View.GONE);
                startRecordLayout.setVisibility(View.VISIBLE);
//                doneBtn.setVisible(false);
            }
        });



    }

    private void stopRecorder() {
        mediaRecorder.stop();
        mediaRecorder.reset();
//        mediaRecorder.release();
    }

    private void startRecorder() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

//        instaRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                RECORDER_SAMPLERATE,RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING,buffersize*bytesPerElement);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(mFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playAudio(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("My App Failed", "prepare() failed");
            e.printStackTrace();
        }
    }
    private void stopPlayer(){
        mediaPlayer.reset();
    }
    private void sendAudioMsg() {

        final ProgressDialog loading = ProgressDialog.show(context,"Fetching Letter","Please wait...",false,false);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";





        TypedFile typedFile = new TypedFile("multipart/form-data", new File(mFileName));


        msgEmail=(EditText) findViewById(R.id.msgEmail);
        msgSubject=(EditText) findViewById(R.id.msgSubject);


        textmsg.subject=msgSubject.getText().toString();
        textmsg.email=msgEmail.getText().toString();
        textmsg.is_public=true;
        textmsg.type="audio";

        api.upload_audio(typedFile, new Callback<HashMap>() {
            @Override
            public void success(HashMap hashMap, Response response) {
                Log.w("My App",new Gson().toJson(hashMap));
                String audio_file=hashMap.get("audio").toString();
                textmsg.audio=audio_file;
                sendTextmsg();
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("My App error","Could not upload the audio file");
                loading.dismiss();
                error.printStackTrace();
            }
        });

    }

    private void sendTextmsg() {
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

}
