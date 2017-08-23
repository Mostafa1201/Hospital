package com.example.ospidalia2;

import android.content.Intent;
import android.support.v7.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ospidalia2.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.CongView);
        textView.setText(message);

    }
    public void signout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
