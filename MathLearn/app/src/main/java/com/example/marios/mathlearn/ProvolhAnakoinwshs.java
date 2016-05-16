package com.example.marios.mathlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class ProvolhAnakoinwshs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provolh_anakoinwshs);
        TextView tv1 = (TextView) findViewById(R.id.anakoinwsh);
        Bundle bundle = this.getIntent().getExtras();
        setTitle(bundle.getString("title"));
        tv1.setMovementMethod(new ScrollingMovementMethod());
        tv1.setText(bundle.getString("str2"));
    }

}
