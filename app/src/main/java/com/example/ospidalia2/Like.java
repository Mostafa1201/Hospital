package com.example.ospidalia2;

/**
 * Created by ESC on 8/28/2017.
 */

public class Like {
    private String id;
    private String liker;

    public Like(){}

    public Like(String id, String liker) {
        this.id = id;
        this.liker = liker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiker() {
        return liker;
    }

    public void setLiker(String liker) {
        this.liker = liker;
    }
}
