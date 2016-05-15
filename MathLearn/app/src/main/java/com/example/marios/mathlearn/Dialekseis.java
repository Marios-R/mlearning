package com.example.marios.mathlearn;

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

public class Dialekseis extends AppCompatActivity {

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/videos.php");
    private ListView listView;
    //SQLiteDatabase db;
    Video[] videos;
    DataBaseHelper dbHelper;
    String codess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialekseis);
        listView=(ListView) findViewById(R.id.listView);
        dbHelper = new DataBaseHelper(this);
        videos = dbHelper.getVideos();
        StringBuilder sb= new StringBuilder();
        String ids;
        for (int i=0; i < videos.length; i++){
            sb.append( "'"+videos[i].videoID+"'," );
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length()-1);
        new GetVideosTask(listView,ids,this).execute(URL);

    }

    private class GetVideosTask extends AsyncTask<String, Void, String> {
        private ListView listView;
        private ProgressDialog dialog = new ProgressDialog(Dialekseis.this);
        private String ids;
        private Context context;

        public GetVideosTask(ListView listView, String c, Context con) {
            this.listView = listView;
            this.ids=c;
            this.context=con;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("ΦΟΡΤΩΣΗ ΔΙΑΛΕΞΕΩΝ");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String videos = "UNDEFINED";
            try {
                DataOutputStream printout;
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Uri.Builder build = new Uri.Builder()
                        .appendQueryParameter("ids", this.ids);
                String query = build.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
                videos = builder.toString();

                urlConnection.disconnect();
                Toast.makeText(Dialekseis.this, videos, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(String temp) {
            Toast.makeText(Dialekseis.this, temp, Toast.LENGTH_LONG).show();
            try{
                JSONObject topLevel = new JSONObject(temp);
                JSONArray vidjson = (JSONArray) topLevel.get("videos");
                //StringBuilder sb= new StringBuilder();
                String nonexistentvideos = (String) topLevel.get("nonexistentvideos");
                //JSONObject nonexistentvideos = topLevel.getJSONObject("nonexistentvideos");
                //String ids;
                for (int i=0; i < vidjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = vidjson.getJSONObject(i);
                        // Pulling items from the array
                        int id = oneObject.getInt("ID");
                        //sb.append( "'"+Integer.toString(id)+"'," );
                        //codes[i]=code;
                        //if (!dbHelper.existInVideos(code)){
                        String code = oneObject.getString("Code");
                            String title = oneObject.getString("Title");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInVids(new Video(title, code, false, false, sequence, id));
                       // }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
                //ids = sb.toString();
                //ids = ids.substring(0, ids.length()-1);
                dbHelper.deleteFromVidsIn(nonexistentvideos);

            }catch(JSONException e){
                //No connectivity?
            }

            //java.util.Arrays.toString(codes).replace('[', '(').replace(']', ')');

            videos = dbHelper.getVideos();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            CustomAdapter adapter = new CustomAdapter(Dialekseis.this);
            adapter.setVideos(videos);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Dialekseis.this, ProvolhDialekshs.class);
                    intent.putExtra("video", videos[position].videoLink);
                    intent.putExtra("id",videos[position].videoID);
                    startActivity(intent);
                    dbHelper.updateSelectedinVids(videos[position].videoID);
                }
            });

        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
