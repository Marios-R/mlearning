package com.example.marios.mathlearn;

import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Created by Marios on 5/19/2016.
 */
class SyncWithRemote extends AsyncTask<String, Void, String> {

    private Strategy strategy;

    public SyncWithRemote(Strategy s){
        this.strategy=s;
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
            //Toast.makeText(MainBase.this, "Αδυναμία σύνδεσης...", Toast.LENGTH_LONG).show();
            //e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String temp) {
        strategy.syncedDB(temp);
        //setRefreshActionButtonState(false);
    }
}