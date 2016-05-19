package com.example.marios.mathlearn;

import android.net.Uri;
import android.os.AsyncTask;

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
 * Created by Marios on 5/17/2016.
 */
/*public class GetResources extends AsyncTask<String, Void, String> {

    private Strategy strategy;

    public GetResources(Strategy s){
        this.strategy=s;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response ="UNDEFINED";
        try {
            java.net.URL url = new URL(strategy.provideURL());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder build = new Uri.Builder().appendQueryParameter("ids", strategy.provideIds());
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

    @Override
    protected void onPostExecute(String temp) {
        strategy.syncedDB(temp);
    }
}*/
