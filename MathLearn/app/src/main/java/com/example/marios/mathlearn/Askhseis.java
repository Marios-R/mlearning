package com.example.marios.mathlearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class Askhseis extends MainBase {

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/assignments.php");
    String ids;
    Assignment[] assignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assignments=dbHelper.getAssignments();
        StringBuilder sb= new StringBuilder();
        for (int i=0; i < assignments.length; i++){
            sb.append( "'"+assignments[i].assignID+"'," );
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
        Toast.makeText(Askhseis.this, ids, Toast.LENGTH_LONG).show();
        adapter = new CustomAdapter(Askhseis.this);
        adapter.setAssignmts(assignments);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.updateSelectedinAssignments(assignments[position].assignID);
                String url = assignments[position].assignLink;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                new GetAssignmentsTask().execute(URL,ids);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetAssignmentsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            setRefreshActionButtonState(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            //Toast.makeText(Askhseis.this, strings[1], Toast.LENGTH_LONG).show();

            return connectToRemote(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(String temp) {
            //Toast.makeText(Askhseis.this, temp, Toast.LENGTH_LONG).show();
            try{
                JSONObject topLevel = new JSONObject(temp);
                JSONArray  assignjson = (JSONArray) topLevel.get("assignments");
                String nonexistentassignments = (String) topLevel.get("nonexistentassignments");
                //StringBuilder sb= new StringBuilder();
                //String ids;
                if (assignjson.length()!=0) {
                for (int i=0; i < assignjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = assignjson.getJSONObject(i);
                        // Pulling items from the array
                        int id = oneObject.getInt("ID");
                        //sb.append( "'"+id+"'," );
                        String title = oneObject.getString("Title");
                        String link = oneObject.getString("Link");
                        int sequence = oneObject.getInt("Sequence");
                        dbHelper.insertInAssignments(new Assignment(title, link, false, id, sequence));
                    } catch (JSONException e) {
                        // Oops
                    }
                }
                }
                if (!nonexistentassignments.equals("")){
                    dbHelper.deleteFromAssignments(nonexistentassignments);
                }
                if (assignjson.length()==0 && nonexistentassignments.equals("") ){
                    Toast.makeText(Askhseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
                }else{
                    assignments = dbHelper.getAssignments();
                    StringBuilder sb= new StringBuilder();
                    for (int i=0; i < assignments.length; i++){
                        sb.append( "'"+assignments[i].assignID+"'," );
                    }
                    ids=sb.toString();
                    ids = ids.substring(0, ids.length() - 1);
                    adapter.setAssignmts(assignments);
                    listView.setAdapter(adapter);
                    Toast.makeText(Askhseis.this, "Οι ασκήσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                //No connectivity?
            }
            setRefreshActionButtonState(false);
        }
    }


    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
