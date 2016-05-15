package com.example.marios.mathlearn;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class Dialekseis extends MainBase {

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/videos.php");
    Video[] videos;
    String ids;
    int[] idarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videos = dbHelper.getVideos();
        StringBuilder sb= new StringBuilder();
        idarray = new int[videos.length];
        for (int i=0; i < videos.length; i++){
            sb.append( "'"+videos[i].videoID+"'," );
            idarray[i]=videos[i].videoID;
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);

        adapter = new CustomAdapter(Dialekseis.this);
        adapter.setVideos(videos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!videos[position].videoSelected){
                    dbHelper.updateSelectedinVids(videos[position].videoID);
                }
                Intent intent = new Intent(Dialekseis.this, ProvolhDialekshs.class);
                intent.putExtra("video", videos[position].videoLink);
                intent.putExtra("position",position);
                intent.putExtra("idarray",idarray);
                intent.putExtra("title",videos[position].videoTitle);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                new GetVideosTask().execute(URL,ids);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetVideosTask extends AsyncTask<String, Void, String> {
        /*private ListView listView;
        private String ids;

        public GetVideosTask(ListView listView, String c) {
            this.listView = listView;
            this.ids=c;
        }*/

        @Override
        protected void onPreExecute() {
            setRefreshActionButtonState(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            return connectToRemote(strings[0],strings[1]);
            /*String vids = "UNDEFINED";
            try {
                DataOutputStream printout;
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Uri.Builder build = new Uri.Builder().appendQueryParameter("ids", this.ids);
                String query = build.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                vids = builder.toString();

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vids;*/
        }

        @Override
        protected void onPostExecute(String temp) {
            try{
                JSONObject topLevel = new JSONObject(temp);
                JSONArray vidjson = (JSONArray) topLevel.get("videos");
                String nonexistentvideos = (String) topLevel.get("nonexistentvideos");
                if (vidjson.length()!=0) {
                    for (int i = 0; i < vidjson.length(); i++) {
                        try {
                            JSONObject oneObject = vidjson.getJSONObject(i);
                            // Pulling items from the array
                            int id = oneObject.getInt("ID");
                            String code = oneObject.getString("Code");
                            String title = oneObject.getString("Title");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInVids(new Video(title, code, false, false, sequence, id));
                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                }
                if (!nonexistentvideos.equals("")){
                    dbHelper.deleteFromVidsIn(nonexistentvideos);
                }
                if (vidjson.length()==0 && nonexistentvideos.equals("") ){
                    Toast.makeText(Dialekseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
                }else{
                    videos = dbHelper.getVideos();
                    StringBuilder sb= new StringBuilder();
                    idarray = new int[videos.length];
                    for (int i=0; i < videos.length; i++){
                        sb.append( "'"+videos[i].videoID+"'," );
                        idarray[i]=videos[i].videoID;
                    }
                    ids=sb.toString();
                    ids = ids.substring(0, ids.length() - 1);
                    //adapter = new CustomAdapter(Dialekseis.this);
                    adapter.setVideos(videos);
                    listView.setAdapter(adapter);
                    Toast.makeText(Dialekseis.this, "Οι διαλέξεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                //No connectivity?
            }
            setRefreshActionButtonState(false);

        }
    }

}
