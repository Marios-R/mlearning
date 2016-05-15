package com.example.marios.mathlearn;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

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

    protected ListView listView;
    protected Menu optionsMenu;
    protected DataBaseHelper dbHelper;
    protected MenuItem refreshItem;
    protected CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        dbHelper = new DataBaseHelper(this);
        listView=(ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        refreshItem = optionsMenu.findItem(R.id.menuRefresh);
        //MenuItem item = menu.findItem(R.id.menuTitle);
        //item.setTitle(R.string.title_activity_dialekseis);
        return super.onCreateOptionsMenu(menu);
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

    public String connectToRemote(String link, String ids){
        String response ="UNDEFINED";
        try {
            //DataOutputStream printout;
            java.net.URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder build = new Uri.Builder().appendQueryParameter("ids", ids);
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
            e.printStackTrace();
        }
        return response;
    }
}
