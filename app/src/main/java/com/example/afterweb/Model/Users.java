package com.example.afterweb.Model;

@SuppressWarnings("unused")
public class Users {
    private String id   ;
    private String username;
    private String imageURl;

    public Users(final String id, final String username, final String imageURl) {
        this.id = id;
        this.username = username;
        this.imageURl = imageURl;
    }

    public Users() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(final String imageURl) {
        this.imageURl = imageURl;
    }
}
