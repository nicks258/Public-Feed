package model;

/**
 * Created by Atreyee Ray on 6/1/2017.
 */

public class GridItem {
    public String title,image,authID,postID;

    public String getPostID()
    {
        return postID;
    }
    public void setPostID(String postID)
    {
        this.postID = postID;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthID()
    {
        return authID;
    }
    public void setAuthID(String authID)
    {
        this.authID = authID;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
