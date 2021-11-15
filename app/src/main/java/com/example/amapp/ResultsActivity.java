package com.example.amapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {
    //TODO: style view
    //TODO: add more result fields to view
    //TODO: add unit test
    TextView tvArtistName, tvArtistId;
    String aName;
    int aId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvArtistName = findViewById(R.id.tvArtistName);
        tvArtistId = findViewById(R.id.tvArtistId);
        aName = getIntent().getStringExtra("artistName");
        tvArtistName.setText(aName);
        //tvArtistId.setText(aId);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.action_bar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
