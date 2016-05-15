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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class Askhseis extends AppCompatActivity {

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/assignments.php");
    //private String[] askhseis;
    //private String[] askhseiscodes;
    private ListView listViewAsk;
    //SQLiteDatabase db;
    Assignment[] assignments;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askhseis);

        listViewAsk=(ListView) findViewById(R.id.askhseis);
        dbHelper = new DataBaseHelper(this);
        new GetAssignmentsTask(listViewAsk).execute(URL);

        /*askhseis = new String[3];
        askhseis[0]="ΠΡΩΤΟ ΦΥΛΛΑΔΙΟ";
        askhseis[1]="ΔΕΥΤΕΡΟ ΦΥΛΛΑΔΙΟ";
        askhseis[2]="ΕΠΙΣΤΡΟΦΗ";

        askhseiscodes = new String[2];
        askhseiscodes[0]="https://www.dropbox.com/s/2qy5oln2zclnamm/%CE%91%CF%83%CE%BA%CE%AE%CF%83%CE%B5%CE%B9%CF%82.docx?dl=0";
        askhseiscodes[1]="https://www.dropbox.com/s/opk0382doo2c8tg/epanaliptikotest1.pdf?dl=0";

        listViewAsk=(ListView) findViewById(R.id.askhseis);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, askhseis );
        listViewAsk.setAdapter(adapter);

        listViewAsk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url=askhseiscodes[position];
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

        });*/
    }

    private class GetAssignmentsTask extends AsyncTask<String, Void, String> {
        private ListView listView;
        private ProgressDialog dialog = new ProgressDialog(Askhseis.this);

        public GetAssignmentsTask(ListView listView) {
            this.listView = listView;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("ΦΟΡΤΩΣΗ ΑΣΚΗΣΕΩΝ");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String assignments = "UNDEFINED";
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
                assignments = builder.toString();

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return assignments;
        }

        @Override
        protected void onPostExecute(String temp) {
            try{
                JSONObject topLevel = new JSONObject(temp);
                JSONArray  assignjson = (JSONArray) topLevel.get("assignments");
                StringBuilder sb= new StringBuilder();
                String ids;
                for (int i=0; i < assignjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = assignjson.getJSONObject(i);
                        // Pulling items from the array
                        int id = oneObject.getInt("ID");
                        sb.append( "'"+id+"'," );
                        if (!dbHelper.existInAssignments(id)){
                            String title = oneObject.getString("Title");
                            String link = oneObject.getString("Link");
                            int sequence = oneObject.getInt("Sequence");
                            dbHelper.insertInAssignments(new Assignment(title, link, false, id, sequence));
                        }
                    } catch (JSONException e) {
                        // Oops
                    }
                }
                ids = sb.toString();
                ids = ids.substring(0, ids.length()-1);
                dbHelper.deleteFromAssignmentsNotIn(ids);
            }catch(JSONException e){
                //No connectivity?
            }

            assignments = dbHelper.getAssignments();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            CustomAdapter adapter = new CustomAdapter(Askhseis.this);
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
    }


    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
