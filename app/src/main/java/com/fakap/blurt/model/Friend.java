package com.fakap.blurt.model;

import android.graphics.Bitmap;

public class Friend {
    private Bitmap profilePic;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public Friend(String id, String name, Bitmap profilePic) {
        this.id = id;
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    @Override
    public String toString() {
        return "name: " + name + " id: " + id;
    }
}
