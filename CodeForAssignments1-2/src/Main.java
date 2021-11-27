import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    static void FormatEvaluation(Post post) { //Auxiliary method to format and print the post evaluation
        System.out.printf("Post of %s: \"%s\" evaluated as %s%n", post.author, post.text,
                //Here we calculate whether a post is Good, Bad or Worst, basing on keywords in reactions (comments) to the post
                //The AnalysisSystem and the KeywordDatabase it uses are follow singleton pattern
                AnalysisSystem.getConnection().evaluatePost(post));
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        //Adding some users to demonstrate the typical use-case of the Opinion-Mining feature
        Admin admin = new Admin("admin", "admin");
        User u = new User("John", "qwerty", "Life's good", "somelink.com");
        User u2 = new User("Kate", "123123", "Wazzup?", "somelink2.com");
        User u3 = new User("Sasha", "98765", "", "somelink3.com");
        User u4 = new User("Troll", "5rr123", "lmao", "somelink4.com");


        //Filling the DB with some keywords
        admin.addKeywordWrapper(new Keyword("good", Opinion.Good));
        admin.addKeywordWrapper(new Keyword("cool", Opinion.Good));
        admin.addKeywordWrapper(new Keyword("valuable", Opinion.Good));

        admin.addKeywordWrapper(new Keyword("bad", Opinion.Bad));
        admin.addKeywordWrapper(new Keyword("sad", Opinion.Bad));
        admin.addKeywordWrapper(new Keyword("poor", Opinion.Bad));

        admin.addKeywordWrapper(new Keyword("worst", Opinion.Worst));
        admin.addKeywordWrapper(new Keyword("awful", Opinion.Worst));
        admin.addKeywordWrapper(new Keyword("disgusting", Opinion.Worst));

        RatedPostSnapshotSupport RPSS = new RatedPostSnapshotSupport();

        //A post representing an opinion on some topic
        Post post = u.createPost("Ma dudes, I think that the first spider-man movie was great!");

        //Some comments represent reaction on some opinion (post). Comments may contain keywords
        //Simulate different times for the comments
        //Likes act like multiplier of reactions. When one likes a comment it means they agree with that reaction

        Comment c1 = u2.createComment("Although the graphics was poor, the plot is cool :)", post);
        c1.date.setTime(post.date.getTime() + (10 * 60 * 1000));
        RPSS.doSomething(post);
        Thread.sleep(700);

        Comment c2 = u2.createComment("BTW, hav u seen da 2nd chapter?", post);
        c2.date.setTime(post.date.getTime() + (21 * 60 * 1000));
        RPSS.doSomething(post);
        Thread.sleep(700);

        Comment c3 = u3.createComment("Man, the movie is really cool, just watched it on the last weekends", post);
        c3.date.setTime(post.date.getTime() + (42 * 60 * 1000));
        RPSS.doSomething(post);
        Thread.sleep(700);

        Files.write(Paths.get("output1.txt"),RPSS.save().getBytes(StandardCharsets.UTF_8));

        Comment c4 = u3.createComment("The actors play good and the message behind is valuable", post);
        c4.date.setTime(post.date.getTime() + (7123 * 60 * 1000));
        c4.likes = 7;
        RPSS.doSomething(post);
        Thread.sleep(700);

        Comment c5 = u4.createComment("Nah, I personally think all superhero movies are awful :(", post);
        c5.date.setTime(post.date.getTime() + (8123 * 60 * 1000));
        c5.likes = 2;
        RPSS.doSomething(post);

        Files.write(Paths.get("output2.txt"),RPSS.save().getBytes(StandardCharsets.UTF_8));
        RPSS.undoSomething(post.id);
        Files.write(Paths.get("output3.txt"),RPSS.save().getBytes(StandardCharsets.UTF_8));

        RPSS.restore(Files.readString(Paths.get("output2.txt")));






        //Print the evaluation to HTML file
        Main.FormatEvaluation(post);
        admin.generateHtmlReport(RPSS);
    }

}
