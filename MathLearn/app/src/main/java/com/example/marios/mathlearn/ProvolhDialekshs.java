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

    private String url;
    DataBaseHelper dbHelper;
    ProgressDialog progDialog = null;
    private int id;
    //private int nextvid=-1;
    private Dialog dialog;
    String title="";
    private int position;
    private int[] videoids;
    private Video currentvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provolh_dialekshs2);
        VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Bundle bundle = this.getIntent().getExtras();
        title=bundle.getString("title");
        setTitle(title);
        url=bundle.getString("video");
        position=bundle.getInt("position");
        videoids=bundle.getIntArray("idarray");
        id=videoids[position];
        //try{
        //    nextvid=bundle.getInt("nextvid");
       // }catch(MissingResourceException e){
        //    }
        dbHelper = new DataBaseHelper(ProvolhDialekshs.this);
        currentvideo = dbHelper.getVideo(id);
        //String vidAddress = "http://mlearning-projectmr.rhcloud.com/videos/mathhma1.mp4";
        Uri vidUri = Uri.parse(url);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        try {
        vidView.start();
        } catch (Exception e){
            Toast.makeText(ProvolhDialekshs.this, "Η διάλεξη αυτή δεν φαίνεται πλέον να υπάρχει. Παρακαλώ ανανεώστε την λίστα των διαλέξεων. Αν το πρόβλημα συμβαίνει ενώ έχετε πρόσφατα ανανεώσει τη λίστα των διαλέξεων το πιο πιθανό είναι να έχετε πρόβλημα σύνδεσης αυτή την στιγμή.", Toast.LENGTH_LONG).show();
        }

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
                if (!currentvideo.videoViewed){
                    dbHelper.updateViewedinVids(id);
                }
                ProvolhDialekshs.this.dialog = new Dialog(ProvolhDialekshs.this);
                dialog.setContentView(R.layout.light);
                TextView tv=(TextView)dialog.findViewById(R.id.textView);
                Button yes=(Button)dialog.findViewById(R.id.buttonyes);
                Button no=(Button)dialog.findViewById(R.id.buttonno);
                dialog.setTitle("ΣΥΓΧΑΡΗΤΗΡΙΑ!");
                if (position-1>=0){
                    final Video nextVideo = dbHelper.getVideo(videoids[position-1]);
                    tv.setText("Τελειώσατε τη διάλεξη με τίτλο '" + title + "'. Θέλετε να συνεχίσετε στην επόμενη διάλεξη?");
                    yes.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) { // κουμπί που κλείνει το παράθυρο
                            dialog.dismiss();
                            Intent intent = new Intent(ProvolhDialekshs.this, ProvolhDialekshs.class);
                            intent.putExtra("video", nextVideo.videoLink);
                            intent.putExtra("position", position - 1);
                            intent.putExtra("idarray", videoids);
                            intent.putExtra("title", nextVideo.videoTitle);
                            startActivity(intent);
                            dbHelper.updateSelectedinVids(nextVideo.videoID);
                            //mSensorManager.registerListener(LightSensor.this, mLight, SensorManager.SENSOR_DELAY_NORMAL); //ξεκινάει πάλι η λήψη δεδομένων από τον αισθητήρα
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) { // κουμπί που κλείνει το παράθυρο
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    tv.setText("Τελειώσατε τη διάλεξη με τίτλο '" + title + "'. Θέλετε να γυρήσετε πίσω στις διαλέξεις?");
                    yes.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) { // κουμπί που κλείνει το παράθυρο
                            dialog.dismiss();
                            Intent intent = new Intent(ProvolhDialekshs.this, Dialekseis.class);
                            startActivity(intent);
                            //mSensorManager.registerListener(LightSensor.this, mLight, SensorManager.SENSOR_DELAY_NORMAL); //ξεκινάει πάλι η λήψη δεδομένων από τον αισθητήρα
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) { // κουμπί που κλείνει το παράθυρο
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                //mp.release();
                // TODO RETURN TO LAST SCREEN
            }
        });
    }
}
