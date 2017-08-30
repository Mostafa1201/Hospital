package com.example.ospidalia2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ESC on 8/23/2017.
 */

public class PatientsList extends ArrayAdapter<Post> {
    private Activity context;
    private List<Post> postsList;
    private Button likeButton;
    TextView likesCount;
    TextView commentsCount;
    private List<String> patientsLiked;
    boolean patientLikedPost;
    String patientName;
    Map<String,List<Like>> likers = new HashMap<>();
    Map<String,List<Comment>> commenters = new HashMap<>();
    int noOfLikes;
    int noOfComments;

    public static final String PATIENT_NAME = "patientName";
    public static final String PATIENT_ID = "patientID";
    boolean first;
    int count;

    public PatientsList(Activity context,List<Post> postsList){
        super(context, R.layout.post_body,postsList);
        this.context = context;
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.post_body,null,false);
        final TextView patient = (TextView) listViewItem.findViewById(R.id.UserName);
        final TextView patientText = (TextView) listViewItem.findViewById(R.id.UserDescription);
        final TextView comment = (TextView) listViewItem.findViewById(R.id.commentButton);
        likesCount = (TextView) listViewItem.findViewById(R.id.likes_count);
        commentsCount = (TextView) listViewItem.findViewById(R.id.comments_count);
        likeButton = (Button) listViewItem.findViewById(R.id.likeButton);
        final Post p = postsList.get(position);
        patient.setText(p.getSender());
        patientText.setText(p.getText());

        if(context instanceof home_page){
            patientName = ((home_page)context).getPatientName();
        }

        if(context instanceof home_page){
            likers = (((home_page)context).getLikes());
        }


        if(context instanceof home_page){
            commenters = (((home_page)context).getComments());
        }
        Log.d("LIKERSSIZE",Integer.toString(likers.size()));
        Log.d("CommentSSIZE",Integer.toString(commenters.size()));


        List<Like> postLikes;
        postLikes = likers.get(p.getId());

        if(postLikes!=null){
            noOfLikes = postLikes.size();
            Log.d("NoOFLikes",Integer.toString(noOfLikes));
            likesCount.setText(Integer.toString(noOfLikes));
        }

        List<Comment> postComments;
        postComments = commenters.get(p.getId());

        if(postComments!=null){
            noOfComments = postComments.size();
            commentsCount.setText(Integer.toString(noOfComments));
        }


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof home_page){
                   boolean liked = ((home_page)context).addLike(p.getId());
                   if(liked==false){
                       int newLikeCount = noOfLikes+1;
                       likesCount = (TextView) listViewItem.findViewById(R.id.likes_count);
                       likesCount.setText(Integer.toString(newLikeCount));
                   }
                }
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CommentsActivity.class);
                intent.putExtra(PATIENT_NAME,patientName);
                intent.putExtra(PATIENT_ID,p.getId());
                context.startActivity(intent);
            }
        });

        Log.d("ENDOFBUTTON","TRUE");
        return listViewItem;
    }
}
