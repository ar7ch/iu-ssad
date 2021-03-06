import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public abstract class TextEntity {
    public UUID id;
    public String text;
    public Date date;
    public UUID author;

    public TextEntity(String text, UUID author) {
        this.text = text;
        this.author = author;
        this.date = new Date();
        this.id = UUID.randomUUID();
    }
}

class Comment extends TextEntity {
    public int likes;

    public Comment(String text, UUID author) {
        super(text, author);
        this.likes = 0;
    }
}

class Post extends TextEntity {
    public ArrayList<Comment> comments;

    public Post(String text, UUID author) {
        super(text, author);
        this.comments = new ArrayList<>();
    }

}