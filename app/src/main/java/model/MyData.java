package model;

/**
 * Created by Atreyee Ray on 4/24/2017.
 */

public class MyData {

    private String link;

    private String post_id;

    private String comments;

    private int count;

    private String title,author_id,author_image,author_name,followers,following,likes,isfollow,islike;

    public String getlink()
    {
        return link;
    }
    public void setlink(String tales_link)
    {
        this.link = tales_link;
    }
    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }
    private String[]image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFollowers()
{
    return followers;
}
    public String getFollowing()
    {
        return following;
    }
    public void setFollowers(String followers)
{
    this.followers = followers;
}
    public void setFollowing(String following)
    {
        this.following = following;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
