package com.projectbelajar.yuukbelajar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import static com.projectbelajar.yuukbelajar.SessionManager.password;
public class Belajar extends AppCompatActivity {

     private static final String pref_name = "crudpref";
    Context context;
    private String JSON_STRING;

    SessionManager sessionManager;
    private String eml, linkvideo, kdchatt,emlku, pssx, emlx, emltujuan;

    ProgressDialog pDialog;
    VideoView videoView;
    MediaController mediaController;
    Uri video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belajar);

        SharedPreferences prefs = getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        emlku = prefs.getString(password, null);
        Intent intent = getIntent();
        linkvideo = intent.getStringExtra(konfigurasi.EMP_KDCHATT);
        //Toast.makeText(getApplicationContext(), linkvideo, Toast.LENGTH_LONG).show();

        videoView = (VideoView) findViewById(R.id.video_view);
        videoStream();
    }

    private void videoStream() {
        // membuat progressbar
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buffering ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            // Memulai MediaController
            mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            // Video URL
            //Toast.makeText(getApplicationContext(), linkvideo, Toast.LENGTH_LONG).show();
            video = Uri.parse(linkvideo);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Menutup pDialog dan play video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoView.start();
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

}