package com.example.marios.mathlearn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.MissingResourceException;

public class ProvolhDialekshs extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private ProgressDialog progDialog = null;
    private int id;
    private int position;
    private Video currentvideo;
    private int[] videoids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provolh_dialekshs2);

        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        Bundle bundle = this.getIntent().getExtras();
        position=bundle.getInt("position");
        videoids=bundle.getIntArray("idarray");
        id=videoids[position];
        dbHelper = new DataBaseHelper(ProvolhDialekshs.this);
        currentvideo = dbHelper.getVideo(id);
        setTitle(currentvideo.videoTitle);
        String vidAddress = currentvideo.videoLink;
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();

        progDialog = ProgressDialog.show(this, "ΠΑΡΑΚΑΛΩ ΠΕΡΙΜΕΝΕΤΕ", "ΦΟΡΤΩΝΕΙ...", true);

        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progDialog.dismiss();
            }
        });

        if (position-1>=0)
            vidView.setOnCompletionListener(new NextVideo(videoids[position-1]));
        else
            vidView.setOnCompletionListener(new NoNextVideo());

    }

    public class NextVideo implements MediaPlayer.OnCompletionListener{

        private Video nextVideo;
        private Dialog dialog;

        public NextVideo(int id){
            nextVideo=dbHelper.getVideo(id);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (!currentvideo.videoViewed){
                dbHelper.updateViewedinVids(id);
            }
            dialog = new Dialog(ProvolhDialekshs.this);
            dialog.setContentView(R.layout.light);
            TextView tv=(TextView)dialog.findViewById(R.id.textView);
            Button yes=(Button)dialog.findViewById(R.id.buttonyes);
            Button no=(Button)dialog.findViewById(R.id.buttonno);
            dialog.setTitle("ΣΥΓΧΑΡΗΤΗΡΙΑ!");
            tv.setText("Τελειώσατε τη διάλεξη με τίτλο '" + currentvideo.videoTitle + "'. Θέλετε να συνεχίσετε στην επόμενη διάλεξη?");
            yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (!nextVideo.videoSelected) {
                        dbHelper.updateSelectedinVids(nextVideo.videoID);
                    }
                    Intent intent = new Intent(ProvolhDialekshs.this, ProvolhDialekshs.class);
                    intent.putExtra("position", position - 1);
                    intent.putExtra("idarray", videoids);
                    startActivity(intent);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public class NoNextVideo implements MediaPlayer.OnCompletionListener{

        private Dialog dialog;

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (!currentvideo.videoViewed){
                dbHelper.updateViewedinVids(id);
            }
            dialog = new Dialog(ProvolhDialekshs.this);
            dialog.setContentView(R.layout.light);
            TextView tv=(TextView)dialog.findViewById(R.id.textView);
            Button yes=(Button)dialog.findViewById(R.id.buttonyes);
            Button no=(Button)dialog.findViewById(R.id.buttonno);
            dialog.setTitle("ΣΥΓΧΑΡΗΤΗΡΙΑ!");
            tv.setText("Τελειώσατε τη διάλεξη με τίτλο '" + currentvideo.videoTitle + "'. Θέλετε να γυρήσετε πίσω στις διαλέξεις?");
            yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(ProvolhDialekshs.this, Dialekseis.class);
                    startActivity(intent);
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
