package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView location = (TextView) findViewById(R.id.location_button1);
        TextView doctor = (TextView) findViewById(R.id.doctor_button1);
        TextView search = (TextView) findViewById(R.id.search_button1);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LocationIntent = new Intent(home_page.this, location_page.class);
                startActivity(LocationIntent);
            }
        });


        doctor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent DoctorIntent = new Intent(home_page.this, doctor_page.class);
                startActivity(DoctorIntent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SearchIntent = new Intent(home_page.this, search_page.class);
                startActivity(SearchIntent);
            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
}

