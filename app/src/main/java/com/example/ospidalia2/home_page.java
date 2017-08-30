package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class home_page extends AppCompatActivity {

    EditText patientPost;
    Button postButton = null;
    Button likeButton = null;
    ListView listViewPosts;
    DatabaseReference databasePosts;
    DatabaseReference databaseLikes;
    DatabaseReference databaseComments;
    List<Post> postsList;

    Map<String,List<Like>> likeMap;
    Map<String,List<Comment>> commentMap;
    String patientName;

    public static final String POSTS = "posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        final TextView location = (TextView) findViewById(R.id.location_button1);
        TextView doctor = (TextView) findViewById(R.id.doctor_button1);
        TextView search = (TextView) findViewById(R.id.search_button1);

        patientPost = (EditText) findViewById(R.id.patientPost);
        postButton = (Button) findViewById(R.id.postButton);
        listViewPosts = (ListView) findViewById(R.id.listViewPatients);
        likeMap = new HashMap<>();
        commentMap = new HashMap<>();
        postsList = new ArrayList<>();
        databasePosts = FirebaseDatabase.getInstance().getReference("Posts");
        databaseLikes = FirebaseDatabase.getInstance().getReference("Likes");
        databaseComments = FirebaseDatabase.getInstance().getReference("Comments");
        Intent intent = getIntent();
        patientName = intent.getStringExtra(MainActivity.PATIENT_NAME);


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

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

        /*
        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Post is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        */

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home_page.this,MainActivity.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //final List<String> likes = new ArrayList<>();
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postsList.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Post post= postSnapshot.getValue(Post.class);
                    //likes.add(post.getPatientsLiked().get(counter));
                    postsList.add(post);
                }
                PatientsList adapter = new PatientsList(home_page.this,postsList);
                listViewPosts.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //Log.d("Likes",Integer.toString(likes.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot likeSnapshot:dataSnapshot.getChildren()) {
                    List<Like> likesList = new ArrayList<Like>();
                    String id = likeSnapshot.getKey();
                    for (DataSnapshot likeSnapshot2 : likeSnapshot.getChildren()) {
                        Like like = likeSnapshot2.getValue(Like.class);
                        likesList.add(like);
                    }
                    likeMap.put(id,likesList);
                    Log.d("LikeListSize",Integer.toString(likesList.size()));
                }
                //PatientsList adapter = new PatientsList(home_page.this,likes);
                //listViewPosts.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //Log.d("Likes",Integer.toString(likes.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot commentSnapshot:dataSnapshot.getChildren()) {
                    List<Comment> commentsList = new ArrayList<Comment>();
                    String id = commentSnapshot.getKey();
                    for (DataSnapshot likeSnapshot2 : commentSnapshot.getChildren()) {
                        Comment comment = likeSnapshot2.getValue(Comment.class);
                        commentsList.add(comment);
                    }
                    commentMap.put(id,commentsList);
                    Log.d("LikeListSize",Integer.toString(commentsList.size()));
                }
                //PatientsList adapter = new PatientsList(home_page.this,likes);
                //listViewPosts.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //Log.d("Likes",Integer.toString(likes.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean addLike(String id){
        boolean liked = false;

        List<Like> postLikes = likeMap.get(id);
        if(postLikes!=null){
            for(int i=0;i<postLikes.size();i++){
                if(postLikes.get(i).getLiker().equals(patientName)){
                    liked = true;
                    break;
                }
            }
        }

        if(liked == true){
            Toast.makeText(getApplicationContext(),"You had already liked this post",Toast.LENGTH_LONG).show();
        }else{
            DatabaseReference databaseLikes2 = FirebaseDatabase.getInstance().getReference().child("Likes");
            String likeID = databaseLikes.push().getKey();
            Like like = new Like(likeID,patientName);
            databaseLikes.child(id).child(likeID).setValue(like);
            Toast.makeText(getApplicationContext(),"Like Saved",Toast.LENGTH_LONG).show();
        }
        return liked;
    }

    private void addPost(){
        String post = patientPost.getText().toString();
        if(patientPost.getText().toString().equals("")){
            Toast.makeText(this,"Type anything to send",Toast.LENGTH_LONG).show();
        }else{
            String id = databasePosts.push().getKey();
            Post p = new Post(id,post,patientName);
            databasePosts.child(id).setValue(p);
            Toast.makeText(this,"Post Added",Toast.LENGTH_LONG).show();
            patientPost.setText("");
        }
    }
    String getPatientName(){
        return patientName;
    }
    Map<String,List<Like>> getLikes(){ return likeMap;}
    Map<String,List<Comment>> getComments(){ return commentMap;}
}

