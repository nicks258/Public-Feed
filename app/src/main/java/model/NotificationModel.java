package model;

/**
 * Created by shash on 9/29/2017.
 */

public class NotificationModel {
    private String user_id,post_id,title,image,time;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_image() {
        return image;
    }

    public void setAuthor_image(String author_image) {
        this.image = author_image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
