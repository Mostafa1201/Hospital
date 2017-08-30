package com.example.ospidalia2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    EditText patientComment;
    Button commentButton = null;
    ListView listViewComments;
    DatabaseReference databaseComments;
    String patientName;
    List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        patientComment = (EditText) findViewById(R.id.patientComment);
        commentButton = (Button) findViewById(R.id.commentButton);
        listViewComments = (ListView) findViewById(R.id.listViewComments);
        commentList = new ArrayList<>();

        Intent intent = getIntent();
        patientName = intent.getStringExtra(PatientsList.PATIENT_NAME);
        String patientID = intent.getStringExtra(PatientsList.PATIENT_ID);
        databaseComments = FirebaseDatabase.getInstance().getReference("Comments").child(patientID);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                for(DataSnapshot commentSnapShot :dataSnapshot.getChildren()){
                    Comment comment = commentSnapShot.getValue(Comment.class);
                    commentList.add(comment);
                }
                CommentList commentListAdapter = new CommentList(CommentsActivity.this,commentList);
                listViewComments.setAdapter(commentListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveComment(){
        String commentText = patientComment.getText().toString();
        if(commentText.equals("")){
            Toast.makeText(getApplicationContext(),"Comment should not be Empty",Toast.LENGTH_LONG).show();
        }else{
            String id = databaseComments.push().getKey();
            Comment comment = new Comment(id,patientName,commentText);
            databaseComments.child(id).setValue(comment);
            Toast.makeText(getApplicationContext(),"Comment Saved",Toast.LENGTH_LONG).show();
        }
    }

}
