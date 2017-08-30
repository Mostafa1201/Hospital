package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ospidalia2.Patient;
import com.example.ospidalia2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mnameView;
    private Spinner spin;

    private FirebaseAuth mAuth;
    boolean clickedon=false;
    DatabaseReference databaseUsers;
    TextView star;
    TextView warrning;
    ProgressBar p1;
    private String auth_failed="Sorry e-mail is already used";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailView = (EditText) findViewById(R.id.editText3);
        mPasswordView = (EditText) findViewById(R.id.editText4);
        mnameView = (EditText) findViewById(R.id.editText5);
        star=(TextView)findViewById(R.id.textView6);
        warrning=(TextView)findViewById(R.id.textView10);
        spin=(Spinner)findViewById(R.id.spinner);
        String[] Monthitems = new String[]{"Male","Female"};
        ArrayAdapter<String> Monthdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Monthitems);
        spin.setAdapter(Monthdapter);
        String compareValue="Male";
        if (!compareValue.equals(null)) {
            int spinnerPosition = Monthdapter.getPosition(compareValue);
            spin.setSelection(spinnerPosition);
        }
        warrning.setVisibility(View.GONE);
        star.setVisibility(View.GONE);

        mPasswordView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                clickedon=true;
                return false;
            }
        });
        mEmailView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                if(clickedon==true && mPasswordView.getText().toString().length()<6){
                    star.setVisibility(View.VISIBLE);
                    warrning.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });
        mnameView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                if(clickedon==true && mPasswordView.getText().toString().length()<6){
                    star.setVisibility(View.VISIBLE);
                    warrning.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });

    }
    public void Signup(View view){
        if(mPasswordView.getText().toString().equals("") || mEmailView.getText().toString().equals("") || mnameView.getText().toString().equals("")){
            Toast.makeText(getBaseContext(),"Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mPasswordView.getText().toString().length()<6){
            Toast.makeText(getBaseContext(),"Please follow instruction of Password", Toast.LENGTH_SHORT).show();
            return;
        }


        p1=(ProgressBar)findViewById(R.id.progressBar2);
        p1.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        // Set up the login form.
        mAuth.createUserWithEmailAndPassword(mEmailView.getText().toString(),mPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Cong", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,auth_failed,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Log.d("Cong", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            sendEmailVerfication();
                            p1.setVisibility(View.GONE);
                            FirebaseUser user=mAuth.getCurrentUser();
                            String id = user.getUid();
                            mAuth.signOut();
                            Toast.makeText(RegisterActivity.this,"Created successfully",Toast.LENGTH_SHORT).show();
                            databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
                            Patient patient = new Patient(mEmailView.getText().toString(),mPasswordView.getText().toString(),mnameView.getText().toString(),spin.getSelectedItem().toString());
                            databaseUsers.child(id).setValue(patient);
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        }
                    }
                });

    }

    private void sendEmailVerfication() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Nice", "Email sent.");
                    Toast.makeText(RegisterActivity.this,"A verification e-mail has bee sent to you",Toast.LENGTH_SHORT).show();

                }

                else{
                    Log.d("Bad", "Verification ERROR");

                }
            }
        });
    }

}
