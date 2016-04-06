package com.example.marios.mathlearn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Dialekseis extends AppCompatActivity {

    String[] dialekseis;
    String[] videocodes;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialekseis);

        dialekseis = new String[10];
        dialekseis[0]="ΔΙΑΛΕΞΗ 1";
        dialekseis[1]="ΔΙΑΛΕΞΗ 2";
        dialekseis[2]="ΔΙΑΛΕΞΗ 3";
        dialekseis[3]="ΔΙΑΛΕΞΗ 4";
        dialekseis[4]="ΔΙΑΛΕΞΗ 5";
        dialekseis[5]="ΔΙΑΛΕΞΗ 6";
        dialekseis[6]="ΔΙΑΛΕΞΗ 7";
        dialekseis[7]="ΔΙΑΛΕΞΗ 8";
        dialekseis[8]="ΔΙΑΛΕΞΗ 9";
        dialekseis[9]="ΔΙΑΛΕΞΗ 10";

        videocodes= new String[10];
        videocodes[0]="gAkXyU5dJjg";
        videocodes[1]="icLMHernP3k";
        videocodes[2]="oaA0U_hLJo0";//https://youtu.be/oaA0U_hLJo0
        videocodes[3]="XtbLkYxyosg";//https://youtu.be/XtbLkYxyosg
        videocodes[4]="jIu5UFvoFUE";//https://youtu.be/jIu5UFvoFUE
        videocodes[5]="dTAFvfpMPY8";//https://youtu.be/dTAFvfpMPY8
        videocodes[6]="tT38yhN3vIs";//https://youtu.be/tT38yhN3vIs
        videocodes[7]="D2Xu33b1xGA";//https://youtu.be/D2Xu33b1xGA
        videocodes[8]="BjAeL0DdeO0";//https://youtu.be/BjAeL0DdeO0
        videocodes[9]="YXDZ73pZp3U";//https://youtu.be/YXDZ73pZp3U

        listView=(ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dialekseis );
        listView.setAdapter(adapter);
    }

}
