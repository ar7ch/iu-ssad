import java.util.UUID;

public abstract class AbstractUser {
    public String username;
    public UUID id;
    private String passwordHash;

    public AbstractUser(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.id = UUID.randomUUID();
    }

    public Post createPost(String text) {
        return new Post(text, this);
    }

    public Comment createComment(String text, Post post) {
        Comment comment = new Comment(text, this);
        post.comments.add(comment);
        return comment;
    }

    public Post getPost(UUID id_post) {
        return null;
    }
}

class User extends AbstractUser {
    public String status;
    public String profilePictureLink;

    public User(String username, String password, String status, String profilePictureLink) {
        super(username, String.valueOf(password.hashCode()));
        this.status = status;
        this.profilePictureLink = profilePictureLink;
    }

}

class Admin extends AbstractUser {

    public Admin(String username, String password) {
        super(username, String.valueOf(password.hashCode()));
    }

    public void addKeywordWrapper(Keyword newKeyword) {
        KeywordDatabase.getKDBConnection().addKeyword(newKeyword);
    }

}
