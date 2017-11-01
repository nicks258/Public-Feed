package model;

/**
 * Created by shashank on 5/23/2017.
 */

public class CommentModel {

    String author,author_image,content,author_id;

    public String getAuth_id() {
        return author_id;
    }

    public void setAuth_id(String auth_id) {
        this.author_id = auth_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
