package com.example.marios.mathlearn;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.Toast;

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

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/videos.php");
    private ListView listView;
    //SQLiteDatabase db;
    Video[] videos;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialekseis);
        listView=(ListView) findViewById(R.id.listView);
        dbHelper = new DataBaseHelper(this);
        new GetVideosTask(listView).execute(URL);

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
                for (int i=0; i < vidjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = vidjson.getJSONObject(i);
                        // Pulling items from the array
                        String code = oneObject.getString("Code");
                        if (!dbHelper.existInVideos(code)){
                            String title = oneObject.getString("Title");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInVids(new Video(title, code, false, false, sequence));
                        }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            }catch(JSONException e){
                //No connectivity?
            }

            videos = dbHelper.getVideos();
            CustomAdapter adapter = new CustomAdapter(Dialekseis.this);
            adapter.setVideos(videos);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dbHelper.updateSelectedinVids(videos[position].videoCode);
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
