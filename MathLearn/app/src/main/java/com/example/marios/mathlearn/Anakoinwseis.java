package com.example.marios.mathlearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class Anakoinwseis extends MainBase implements Strategy{

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/announcements.php");
    private Announcement[] announcements;
    private String ids;
    private ListView listView;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //url = String.format("http://mlearning-projectmr.rhcloud.com/announcements.php");
        strategy=this;
        listView=(ListView) findViewById(R.id.listView);
        dbHelper = new DataBaseHelper(this);
        populateList();
    }

    @Override
    public void syncedDB(String s) {
        try {
            JSONObject topLevel = new JSONObject(s);
            JSONArray annjson = (JSONArray) topLevel.get("announcements");
            String nonexistentannouncements = (String) topLevel.get("nonexistentannouncements");
            if (annjson.length() != 0) {
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
            if (!nonexistentannouncements.equals("")) {
                dbHelper.deleteFromAnnouncements(nonexistentannouncements);
            }
            if (annjson.length() == 0 && nonexistentannouncements.equals("")) {
                Toast.makeText(Anakoinwseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
            } else {
                populateList();
                Toast.makeText(Anakoinwseis.this, "Οι ανακοινώσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            //No connectivity?
        }
    }

    @Override
    public void populateList() {
        announcements = dbHelper.getAnnouncements();
        AnnouncementsAdapter adapter = new AnnouncementsAdapter();
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
    }

    @Override
    public String provideIds() {
        return ids;
    }

    @Override
    public String provideURL() {
        return URL;
    }

    private class AnnouncementsAdapter extends BaseAdapter{

        private LayoutInflater inflater=null;

        @Override
        public int getCount() {
            return announcements.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = ( LayoutInflater )Anakoinwseis.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview, null);
            TextView tv=(TextView) rowView.findViewById(R.id.textView1);
            tv.setText(announcements[position].annDate);
            if (announcements[position].annSelected){
                rowView.setBackgroundColor(1426063360);
            }
            return rowView;
        }
    }

}
