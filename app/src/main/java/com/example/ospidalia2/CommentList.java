package com.example.ospidalia2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ESC on 8/28/2017.
 */

public class CommentList extends ArrayAdapter<Comment> {
    private Activity context;
    List<Comment> commentsList;

    public CommentList(Activity context,List<Comment> commentsList){
        super(context,R.layout.comments_list,commentsList);
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.comments_list,null,true);
        final TextView patient = (TextView) listViewItem.findViewById(R.id.patient);
        final TextView patientText = (TextView) listViewItem.findViewById(R.id.patientText);

        Comment comment = commentsList.get(position);
        patient.setText(comment.getSender());
        patientText.setText(comment.getText());

        return listViewItem;
    }
}
