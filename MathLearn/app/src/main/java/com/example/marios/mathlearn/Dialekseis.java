package com.example.marios.mathlearn;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Dialekseis extends AppCompatActivity {

    private static String url = String.format("http://mlearning-projectmr.rhcloud.com/videos.php");
    private ListView listView;
    SQLiteDatabase db;
    Video[] videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialekseis);

        listView=(ListView) findViewById(R.id.listView);

        db = openOrCreateDatabase("Local_DB", Context.MODE_PRIVATE, null); //ανοίγει τη βάση δεδομένων - την δημιουργεί αν δεν υπάρχει
        db.execSQL("CREATE TABLE IF NOT EXISTS videos(vTitle VARCHAR,vCode VARCHAR, vViewed VARCHAR, vSelected VARCHAR, vSequence INT);"); // δημιουργεί τον πίνακα

        new GetVideosTask(listView).execute(url);

        /*Video[] videos = new Video[10];
        videos[0]=new Video("ΔΙΑΛΕΞΗ 1","gAkXyU5dJjg",false, false, 1);
        videos[1]=new Video("ΔΙΑΛΕΞΗ 2","icLMHernP3k",false, true, 2);
        videos[2]=new Video("ΔΙΑΛΕΞΗ 3","oaA0U_hLJo0",true, false, 3);
        videos[3]=new Video("ΔΙΑΛΕΞΗ 4","XtbLkYxyosg",false, true, 4);
        videos[4]=new Video("ΔΙΑΛΕΞΗ 5","jIu5UFvoFUE",true, false, 5);
        videos[5]=new Video("ΔΙΑΛΕΞΗ 6","dTAFvfpMPY8",false, true, 6);
        videos[6]=new Video("ΔΙΑΛΕΞΗ 7","tT38yhN3vIs",true, false, 7);
        videos[7]=new Video("ΔΙΑΛΕΞΗ 8","D2Xu33b1xGA",false, true, 8);
        videos[8]=new Video("ΔΙΑΛΕΞΗ 9","BjAeL0DdeO0",true, false, 9);
        videos[9]=new Video("ΔΙΑΛΕΞΗ 10","YXDZ73pZp3U",false, false, 10);*/


    }

    private class GetVideosTask extends AsyncTask<String, Void, String> {
        private ListView listView;

        public GetVideosTask(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String videos = "UNDEFINED";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                videos = builder.toString();

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(String temp) {
            try{
                JSONObject topLevel = new JSONObject(temp);
                JSONArray vidjson = (JSONArray) topLevel.get("videos");
                //String[] vids = new String[vidjson.length()];
                for (int i=0; i < vidjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = vidjson.getJSONObject(i);
                        // Pulling items from the array
                        //String title = oneObject.getString("Title");
                        //vids[i]=title;
                        String code = oneObject.getString("Code");
                        Cursor cu=db.rawQuery("SELECT * FROM videos WHERE vCode='" + code + "'", null);
                        if (cu.getCount()==0){
                            db.execSQL("INSERT INTO videos VALUES('"+oneObject.getString("Title")+"','"+code+"','false','false','"+oneObject.getInt("Sequence")+"');");
                        }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(Dialekseis.this, android.R.layout.simple_list_item_1, vids );
                listView.setAdapter(adapter);*/
            }catch(JSONException e){
                //No connectivity?
            }

            Cursor c=db.rawQuery("SELECT * FROM videos ORDER BY vSequence DESC", null);
            if (c.getCount()==0){
                videos = new Video[1];
                videos[0]= new Video("ΔΕΝ ΥΠΑΡΧΕΙ ΔΙΑΛΕΞΗ", "", false, false,1);
            }
            else {
                videos= new Video[c.getCount()];
                int i = 0;
                while (c.moveToNext()) {
                    String videoTitle = c.getString(0);
                    String videoCode = c.getString(1);
                    boolean videoSelected = false;
                    boolean videoViewed = false;
                    int videoSequence = c.getInt(4);
                    if (c.getString(2).equals("true")){
                        videoSelected=true;
                        if (c.getString(3).equals("true")){
                            videoViewed=true;
                        }
                    }
                    videos[i]= new Video(videoTitle,videoCode,videoSelected,videoViewed, videoSequence);
                    i++;
                }
            }

            //listView=(ListView) findViewById(R.id.listView);
            listView.setAdapter(new CustomAdapter(Dialekseis.this, videos));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //ContentValues cv = new ContentValues();
                    //cv.put("vSelected", "true");
                    //db.update("videos", cv, "vCode=" + videos[position].videoCode, null);
                    //db.execSQL("UPDATE videos SET vSelected = 'true' WHERE vCode = '"+videos[position].videoCode+"';");//INSERT INTO viewedvideos VALUES("+position+");");
                    //view.setBackgroundColor(1426063360);
                    Intent intent = new Intent(Dialekseis.this, VideoViewActivity.class);
                    intent.putExtra("video", videos[position].videoCode);
                    startActivity(intent);
                }
            });

        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
