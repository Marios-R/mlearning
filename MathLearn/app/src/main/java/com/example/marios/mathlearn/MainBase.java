package com.example.marios.mathlearn;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainBase extends AppCompatActivity {

    protected Menu optionsMenu;
    protected MenuItem refreshItem;
    //protected SyncWithRemote syncWithRemote;
    protected String ids, url;
    protected DataBaseHelper dbHelper;
    //protected Strategy strategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        dbHelper=new DataBaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        refreshItem = optionsMenu.findItem(R.id.menuRefresh);
        return super.onCreateOptionsMenu(menu);
    }



    protected interface Strategy {
        //public static String url = null;
        void syncedDB(String s);
        //public void populateList();
        //public String provideIds();
        //public String provideURL();
        //public void updateids();
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    protected class SyncWithRemote extends AsyncTask<String, Void, String> {

        protected Strategy strategy;

        SyncWithRemote(Strategy s){
            this.strategy=s;
        }

        @Override
        protected void onPreExecute() {
            setRefreshActionButtonState(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String response ="UNDEFINED";
            try {
                java.net.URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Uri.Builder build = new Uri.Builder().appendQueryParameter("ids", strings[1]);
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
                response = builder.toString();

                urlConnection.disconnect();
            } catch (Exception e) {
                Toast.makeText(MainBase.this, "Αδυναμία σύνδεσης...", Toast.LENGTH_LONG).show();
                //e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String temp) {
            strategy.syncedDB(temp);
            setRefreshActionButtonState(false);
        }
    }
}
