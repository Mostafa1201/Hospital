package com.example.ospidalia2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ospidalia2.Patient;
import com.example.ospidalia2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText mailEdit ;
    EditText passEdit;
    EditText nameEdit;
    EditText yearEdit;
    DatabaseReference user;
    Spinner Day;
    Spinner Month;
    List<Patient> arr = new ArrayList<>();
    Button signUp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Day=(Spinner)findViewById(R.id.spinner3);
        String compareValue = "1";
        List<String> Dayitems = new ArrayList<>();
        for(int i=1;i<32;i++) {
            Dayitems.add(Integer.toString(i));
        }
        ArrayAdapter<String> Dayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Dayitems);
        Day.setAdapter(Dayadapter);

        int spinnerPosition = Dayadapter.getPosition(compareValue);
        Day.setSelection(spinnerPosition);

        Month=(Spinner)findViewById(R.id.spinner);
        List<String> Monthitems = new ArrayList<>();
        for(int i=1;i<32;i++) {
            Monthitems.add(Integer.toString(i));
        }
        ArrayAdapter<String> Monthdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Monthitems);
        Month.setAdapter(Monthdapter);

        int spinnerPosition2 = Dayadapter.getPosition(compareValue);
        Month.setSelection(spinnerPosition2);
        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
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

    public void signup(View view){
        mailEdit =(EditText)findViewById(R.id.editMail);
        passEdit =(EditText)findViewById(R.id.editPassword);
        nameEdit =(EditText)findViewById(R.id.editName);
        yearEdit =(EditText)findViewById(R.id.YearText);


        if(passEdit.getText().toString().equals("") || mailEdit.getText().toString().equals("") || nameEdit.getText().toString().equals("")|| yearEdit.getText().toString().equals("")){
            Toast.makeText(getBaseContext(),"Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean b=false;
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getMail().equals(mailEdit.getText().toString())){
                b=true;
                break;
            }
        }
        if(b==true){
            Toast.makeText(getBaseContext(),"Sorry this E-mail is taken already", Toast.LENGTH_SHORT).show();
        }
        else{
            String dat=Day.getSelectedItem().toString()+"/"+Month.getSelectedItem().toString()+"/"+yearEdit.getText().toString();
            Patient patient =new Patient(mailEdit.getText().toString(),passEdit.getText().toString(),nameEdit.getText().toString(),dat);
            user.child(mailEdit.getText().toString()).setValue(patient);
            Toast.makeText(getBaseContext(),"Added", Toast.LENGTH_SHORT).show();
        }
    }
}
