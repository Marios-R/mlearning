package com.example.marios.mathlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProvolhAnakoinwshs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provolh_anakoinwshs);
        TextView tv1 = (TextView) findViewById(R.id.anakoinwsh);
        Bundle bundle = this.getIntent().getExtras();
        tv1.setText(bundle.getString("str2"));
    }

    public void goToAnakoinwseis(View view){
        Intent intent = new Intent(this, Anakoinwseis.class);
        startActivity(intent);
    }
}
