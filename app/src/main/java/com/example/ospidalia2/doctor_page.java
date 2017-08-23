package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class doctor_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);

        TextView home = (TextView) findViewById(R.id.home_button1);
        TextView location = (TextView) findViewById(R.id.location_button1);
        TextView search = (TextView) findViewById(R.id.search_button1);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(doctor_page.this, home_page.class);
                startActivity(HomeIntent);
            }
        });


        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent LocationIntent = new Intent(doctor_page.this, location_page.class);
                startActivity(LocationIntent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SearchIntent = new Intent(doctor_page.this, search_page.class);
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
