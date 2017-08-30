package com.example.ospidalia2;

import java.util.List;

/**
 * Created by ESC on 8/24/2017.
 */

public class Post {
    private String id;
    private String text;
    private String sender;

    public Post(){}

    public Post(String id, String text, String sender) {
        this.id = id;
        this.text = text;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
