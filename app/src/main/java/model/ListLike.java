package model;

/**
 * Created by shash on 7/22/2017.
 */

public class ListLike {

    public ListLike(String liker_id, String liker_name, String liker_image) {
        this.liker_id = liker_id;
        this.liker_name = liker_name;
        this.liker_image = liker_image;
    }

    public ListLike() {
    }

    String liker_id,liker_name,liker_image;

    public String getLikers_id() {
        return liker_id;
    }

    public void setLikers_id(String likers_id) {
        this.liker_id = likers_id;
    }

    public String getLikers_name() {
        return liker_name;
    }

    public void setLikers_name(String likers_name) {
        this.liker_name = likers_name;
    }

    public String getLikers_image() {
        return liker_image;
    }

    public void setLikers_image(String likers_image) {
        this.liker_image = likers_image;
    }
}
