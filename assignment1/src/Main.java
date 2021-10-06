import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


enum Opinion {
    Worst,
    Bad,
    Good,
}

class Keyword {
    public String word;
    public Opinion opinion;

    public Keyword(String word, Opinion opinion) {
        this.word = word;
        this.opinion = opinion;
    }
}

abstract class TextEntity {
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
    public KeywordDatabase db;

    public Admin(String username, String password) {
        super(username, String.valueOf(password.hashCode()));
        this.db = new ConcreteKeywordDatabase();
    }
}

class AnalysisSystem {
    private static AnalysisSystem systemInstance;
    private KeywordDatabase KWD;

    private AnalysisSystem(KeywordDatabase KWD) {
        this.KWD = KWD;
    }

    public static AnalysisSystem getConnection(KeywordDatabase KWD) {
        if (systemInstance == null)
            systemInstance = new AnalysisSystem(KWD);

        return systemInstance;
    }

    public Opinion evaluatePost(Post post) {
        int value = 0;
        int count = 0;
        for (Comment c: post.comments) {
            String s = c.text;
            String[] words = s.split("\\W+");
            for (int e = 0; e < words.length; e++) {
                Keyword word = KWD.getKeyword(words[e]);
                if (word != null) {
                    count+=(1+c.likes);
                    value += (word.opinion.ordinal() - 1)*(1+c.likes);
                }
            }
        }
        float valuef = (float) value / count;
        if (valuef < -0.5) return Opinion.Worst;
        if (valuef < 0.5) return Opinion.Bad;
        if (valuef >= 0.5) return Opinion.Good;

        return null;
    }
}

public class Main {
    static String EVALUATION = "Post %s is %s";

    static void FormatEvaluation(AnalysisSystem system, Post post) {
        System.out.println(String.format(Main.EVALUATION, "\"" + post.text + "\"", system.evaluatePost(post)));
    }

    public static void main(String[] args) {
        ConcreteKeywordDatabase kwDB = new ConcreteKeywordDatabase();

        kwDB.addKeyword(new Keyword("good", Opinion.Good));
        kwDB.addKeyword(new Keyword("cool", Opinion.Good));
        kwDB.addKeyword(new Keyword("valuable", Opinion.Good));

        kwDB.addKeyword(new Keyword("bad", Opinion.Bad));
        kwDB.addKeyword(new Keyword("sad", Opinion.Bad));
        kwDB.addKeyword(new Keyword("poor", Opinion.Bad));

        kwDB.addKeyword(new Keyword("worst", Opinion.Worst));
        kwDB.addKeyword(new Keyword("awful", Opinion.Worst));
        kwDB.addKeyword(new Keyword("disgusting", Opinion.Worst));

        AnalysisSystem system = AnalysisSystem.getConnection(kwDB);

        User u = new User("John", "qwerty", "Life's good", "somelink.com");
        User u2 = new User("Kate", "123123", "Wazzup?", "somelink2.com");
        User u3 = new User("Sasha", "98765", "", "somelink3.com");
        User u4 = new User("Troll", "5rr123", "lmao", "somelink4.com");

        Post post = u.createPost("Ma dudes, I think that the first spider-man movie was great!");

        Comment c = u2.createComment("Although the graphics was poor, the plot is cool :)", post);
        Comment c2 = u2.createComment("BTW, hav u seen da 2nd chapter?", post);
        Comment c3 = u3.createComment("Man, the movie is really cool, just watched it on last weekends", post);
        Comment c4 = u3.createComment("The actors play good and the message behind is valuable", post);
        Comment c5 =u4.createComment("Nah, I personally think all superhero movies are awful :(", post);

        c5.likes = 2;
        c4.likes = 7;


        Main.FormatEvaluation(system, post);
    }

}
