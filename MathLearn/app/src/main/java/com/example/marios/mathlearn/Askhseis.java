package com.example.marios.mathlearn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Askhseis extends AppCompatActivity {

    String[] askhseis;
    String[] askhseiscodes;
    ListView listViewAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askhseis);

        askhseis = new String[3];
        askhseis[0]="ΠΡΩΤΟ ΦΥΛΛΑΔΙΟ";
        askhseis[1]="ΔΕΥΤΕΡΟ ΦΥΛΛΑΔΙΟ";
        askhseis[2]="ΕΠΙΣΤΡΟΦΗ";

        askhseiscodes = new String[2];
        askhseiscodes[0]="https://www.dropbox.com/s/2qy5oln2zclnamm/%CE%91%CF%83%CE%BA%CE%AE%CF%83%CE%B5%CE%B9%CF%82.docx?dl=0";
        askhseiscodes[1]="https://www.dropbox.com/s/opk0382doo2c8tg/epanaliptikotest1.pdf?dl=0";

        listViewAsk=(ListView) findViewById(R.id.askhseis);
        Button btnBack = new Button(this);
        btnBack.setText("ΕΠΙΣΤΡΟΦΗ");
        btnBack.setBackgroundResource(R.drawable.rouned_corner_shadow);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Askhseis.this, MainActivity.class);
                startActivity(intent);
            }
        });
        listViewAsk.addFooterView(btnBack);

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

        });
    }

}
