package com.fakap.blurt.model;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend {
    private CircleImageView friendProfilePic;
    private String friendName;
    private boolean online;

    public Friend(CircleImageView friendProfilePic, String friendName) {
        this.friendProfilePic = friendProfilePic;
        this.friendName = friendName;
        this.online = false;
    }

    public boolean isOnline() {
        return online;
    }

    public String getFriendName() {
        return friendName;
    }

    public CircleImageView getFriendProfilePic() {
        return friendProfilePic;
    }
}
