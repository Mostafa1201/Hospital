package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText email ;
    private EditText password ;
    private Button sign_in;
    DatabaseReference user;
    List<Patient> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sign_in = (Button)findViewById(R.id.sign_in_button);
        email = (EditText) findViewById(R.id.first_name);
        password = (EditText) findViewById(R.id.password);
        sign_in.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View view) {
            if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                Toast.makeText(getBaseContext(),"Please fill all data", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean found = false;
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getMail().equals(email.getText().toString()) &&
                        arr.get(i).getPassword().equals(password.getText().toString())){
                    found=true;
                    break;
                }
            }
            if(found==true){
                Intent intent = new Intent(MainActivity.this, home_page.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(),"User Not Found please try again", Toast.LENGTH_SHORT).show();
            }

        }

        });

        TextView signUP = (TextView)findViewById(R.id.signUP);
        signUP.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent SignIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(SignIntent);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user= FirebaseDatabase.getInstance().getReference("Users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = postSnapshot.getValue(Patient.class);
                    arr.add(patient);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
