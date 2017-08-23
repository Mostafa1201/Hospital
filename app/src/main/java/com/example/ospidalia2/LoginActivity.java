package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText mailEdit ;
    EditText passEdit;
    DatabaseReference  dret;
    public static final String EXTRA_MESSAGE = "com.pop.hospital.ospidalia.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }
    public void signin(View view){
        mailEdit =(EditText)findViewById(R.id.editEmail);
        passEdit =(EditText)findViewById(R.id.editPass);

       final ArrayList <Patient> arr=new <Patient> ArrayList();
        dret= FirebaseDatabase.getInstance().getReference("Users");
        dret.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Patient patient = postSnapshot.getValue(Patient.class);
                    //adding artist to the list
                    arr.add(patient);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        boolean b=false;
        int index=0;
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getMail().equals(mailEdit.getText().toString())){
                if(arr.get(i).getPassword().equals(passEdit.getText().toString())){
                    b=true;
                    index=i;
                    break;
                }
            }
        }
        if(b==true){
            Intent intent = new Intent(this, HomeActivity.class);
            String message= "Congratulation " + arr.get(index).getName();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        else{
            Toast.makeText(getBaseContext(),"Mail or password is incorrect", Toast.LENGTH_SHORT).show();
        }

    }
    public void signup(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



}
