import java.util.ArrayList;

public class KeywordDatabase {

    static private final ArrayList<Keyword> keywords = new ArrayList<>();
    static private KeywordDatabase dbInstance;

    private KeywordDatabase() {
    }

    public static KeywordDatabase getKDBConnection() {
        if (dbInstance == null)
            dbInstance = new KeywordDatabase();
        return dbInstance;
    }

    public void addKeyword(Keyword newKeyword) {
        this.keywords.add(newKeyword);
    }

    public Keyword getKeyword(String word) {
        for (Keyword keyword : this.keywords) {
            if (keyword.word.equals(word)) {
                return keyword;
            }
        }
        return null;
    }
}

class Keyword {
    public String word;
    public Opinion opinion;

    public Keyword(String word, Opinion opinion) {
        this.word = word;
        this.opinion = opinion;
    }
}

enum Opinion {
    Worst,
    Bad,
    Good,
}
