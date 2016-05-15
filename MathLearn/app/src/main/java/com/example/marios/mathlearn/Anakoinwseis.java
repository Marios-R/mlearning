package com.example.marios.mathlearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Anakoinwseis extends MainBase {

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/announcements.php");
    Announcement[] announcements;
    String ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = dbHelper.getAnnouncements();
        adapter = new CustomAdapter(Anakoinwseis.this);
        adapter.setAnnouncements(announcements);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!announcements[position].annSelected){
                    dbHelper.updateSelectedinAnnouncements(announcements[position].annID);
                }
                Intent intent = new Intent(Anakoinwseis.this, ProvolhAnakoinwshs.class);
                intent.putExtra("str2", announcements[position].annBody);
                intent.putExtra("title", announcements[position].annDate);
                startActivity(intent);
            }
        });

        StringBuilder sb= new StringBuilder();
        for (int i=0; i < announcements.length; i++){
            sb.append( "'"+announcements[i].annID+"'," );
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
        Toast.makeText(Anakoinwseis.this, ids, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                new GetAnnouncementsTask().execute(URL,ids);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetAnnouncementsTask extends AsyncTask<String, Void, String> {

        /*private ListView listView;
        private String ids;

        public GetAnnouncementsTask(ListView listView, String c) {
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
            //Toast.makeText(Anakoinwseis.this, this.ids, Toast.LENGTH_LONG).show();
            /*String announcements = "UNDEFINED";
            try {
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
                announcements = builder.toString();

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return announcements;*/
        }

        @Override
        protected void onPostExecute(String temp) {
            //Toast.makeText(Anakoinwseis.this, temp, Toast.LENGTH_LONG).show();
            try {
                JSONObject topLevel = new JSONObject(temp);
                JSONArray annjson = (JSONArray) topLevel.get("announcements");
                String nonexistentannouncements = (String) topLevel.get("nonexistentannouncements");
                if (annjson.length()!=0) {
                    for (int i = 0; i < annjson.length(); i++) {
                        try {
                            JSONObject oneObject = annjson.getJSONObject(i);
                            // Pulling items from the array
                            int id = oneObject.getInt("ID");
                            String date = oneObject.getString("Date");
                            String body = oneObject.getString("Body");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInAnnouncements(new Announcement(date, body, false, id, sequence));
                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                }
                if (!nonexistentannouncements.equals("")){
                    dbHelper.deleteFromAnnouncements(nonexistentannouncements);
                }
                if (annjson.length()==0 && nonexistentannouncements.equals("") ){
                    Toast.makeText(Anakoinwseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
                }else{
                    announcements = dbHelper.getAnnouncements();
                    StringBuilder sb= new StringBuilder();
                    for (int i=0; i < announcements.length; i++){
                        sb.append( "'"+announcements[i].annID+"'," );
                    }
                    ids=sb.toString();
                    ids = ids.substring(0, ids.length() - 1);
                    //adapter = new CustomAdapter(Anakoinwseis.this);
                    adapter.setAnnouncements(announcements);
                    listView.setAdapter(adapter);
                    Toast.makeText(Anakoinwseis.this, "Οι ανακοινώσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //No connectivity?
            }
            setRefreshActionButtonState(false);

        }
    }

}
