public class Main {

    static void FormatEvaluation(AnalysisSystem system, Post post) {
        System.out.printf("Post %s is %s%n", "\"" + post.text + "\"", system.evaluatePost(post));
    }

    public static void main(String[] args) {

        Admin admin = new Admin("admin","admin");

        admin.addKeywordWrapper(new Keyword("good", Opinion.Good));
        admin.addKeywordWrapper(new Keyword("cool", Opinion.Good));
        admin.addKeywordWrapper(new Keyword("valuable", Opinion.Good));

        admin.addKeywordWrapper(new Keyword("bad", Opinion.Bad));
        admin.addKeywordWrapper(new Keyword("sad", Opinion.Bad));
        admin.addKeywordWrapper(new Keyword("poor", Opinion.Bad));

        admin.addKeywordWrapper(new Keyword("worst", Opinion.Worst));
        admin.addKeywordWrapper(new Keyword("awful", Opinion.Worst));
        admin.addKeywordWrapper(new Keyword("disgusting", Opinion.Worst));

        AnalysisSystem system = AnalysisSystem.getInstance();

        User u = new User("John", "qwerty", "Life's good", "somelink.com");
        User u2 = new User("Kate", "123123", "Wazzup?", "somelink2.com");
        User u3 = new User("Sasha", "98765", "", "somelink3.com");
        User u4 = new User("Troll", "5rr123", "lmao", "somelink4.com");

        Post post = u.createPost("Ma dudes, I think that the first spider-man movie was great!");

        u2.createComment("Although the graphics was poor, the plot is cool :)", post);
        u2.createComment("BTW, hav u seen da 2nd chapter?", post);
        u3.createComment("Man, the movie is really cool, just watched it on the last weekends", post);
        Comment c4 = u3.createComment("The actors play good and the message behind is valuable", post);
        Comment c5 =u4.createComment("Nah, I personally think all superhero movies are awful :(", post);

        c5.likes = 2;
        c4.likes = 7;

        Main.FormatEvaluation(system, post);
    }

}
