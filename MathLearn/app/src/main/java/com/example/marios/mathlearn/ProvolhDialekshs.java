package com.example.marios.mathlearn;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class ProvolhDialekshs extends AppCompatActivity {

    private String url;
    DataBaseHelper dbHelper;
    ProgressDialog progDialog = null;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provolh_dialekshs2);
        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Bundle bundle = this.getIntent().getExtras();
        url=bundle.getString("video");
        id=bundle.getInt("id");
        dbHelper = new DataBaseHelper(ProvolhDialekshs.this);
        //String vidAddress = "http://mlearning-projectmr.rhcloud.com/videos/mathhma1.mp4";
        Uri vidUri = Uri.parse(url);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();

        progDialog = ProgressDialog.show(this, "ΠΑΡΑΚΑΛΩ ΠΕΡΙΜΕΝΕΤΕ", "ΦΟΡΤΩΝΕΙ...", true);
        progDialog.setCancelable(false);
        progDialog.setCanceledOnTouchOutside(false);

        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progDialog.dismiss();
                //mp.start();
            }
        });

        vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                dbHelper.updateViewedinVids(id);
                //mp.release();
                // TODO RETURN TO LAST SCREEN
            }
        });
    }
}
