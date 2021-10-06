import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;



enum Opinion {
    Good,
    Bad,
    Worst,
}

class Keyword {
    public String word;
    public Opinion opinion;
}

class TextEntity {
    public UUID id;
    public String text;
    public Date date;
    public AbstractUser author;

    public TextEntity(String text, AbstractUser author) {
        this.text = text;
        this.author = author;
        this.date = new Date();
        this.id = UUID.randomUUID();
    }
}

class Comment extends TextEntity {
    public int likes;

    public Comment(String text, AbstractUser author) {
        super(text, author);
        this.likes = 0;
    }
}

class Post extends TextEntity {
    public ArrayList<Comment> comments;

    public Post(String text, AbstractUser author) {
        super(text, author);
        this.comments = new ArrayList<>();
    }


}

interface KeywordDatabase {
    public KeywordDatabase getKDBConnection();

    public void addKeyword(Keyword newKeyword);

    public Keyword getKeyword(String word);
}

class ConcreteKeywordDatabase implements KeywordDatabase {

    private final ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    private KeywordDatabase dbInstance;

    @Override
    public KeywordDatabase getKDBConnection() {
        return null;
    }

    @Override
    public void addKeyword(Keyword newKeyword) {
        this.keywords.add(newKeyword);
    }

    @Override
    public Keyword getKeyword(String word) {
        for (Keyword keyword : this.keywords) {
            if (keyword.word.equals(word)) {
                return keyword;
            }
        }
        return null;
    }
}

abstract class AbstractUser {
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

    public createComment(String text, Post post) {
        post.comments.add(new Comment(text, author));
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
    public KeywordDatabase db;

    public Admin(String username, String password) {
        super(username, String.valueOf(password.hashCode()));
        this.db = new ConcreteKeywordDatabase();
    }
}

class static AnalysisSystem {
    private static AnalysisSystem systemInstance;
    private KeywordDatabase KWD;
    private AnalysisSystem(KeywordDatabase KWD)
    {
        this.KWD = KWD;
    }
    public static getConnection(KeywordDatabase KWD)
    {
        if (systemInstance == null)
            systemInstance = new AnalysisSystem(KWD);

        return systemInstance;
    }

    public Opinion evaluatePost(Post post) {
        int value = 0;
        int count = 0;
        for (int i = 0; i < post.comments.length; i++) {
            String s = post.comments[i].text;
            String[] words = s.split("\\W+");
            for (int e = 0; e < words.length; e++) {
                var word = KWD.getKeyword(word);
                if(word != null)
                {
                    count++;
                    value += word.opinion - 1;
                }
            }
        }
        float valuef = (float)value/count;
        if(valuef < -0.5) return Opinion.Worst;
        if(valuef < 0.5) return Opinion.Bad;
        if(valuef >= 0.5) return Opinion.Good;

        return null;
    }
}

public class Main {

    public static void main(String[] args) {
    }

}
