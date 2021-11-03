import java.awt.*;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;


public interface HTMLSupport<T> {
    void createHTML();

    void loadData(T data);
}

class HTMLAnalysisAdapter implements HTMLSupport<Pair<Post, Opinion>> {
    private String htmlText;
    private ArrayList<Pair<Post, Opinion>> dataCollection = new ArrayList<>();

    @Override
    public void createHTML() {
        htmlText = "<!DOCTYPE html><html lang=\"en\"><head> <meta name=\"description\" content=\"Report\" /> <meta charset=\"utf-8\"> <title>Report</title></head><body><div class=\"container\"> <pre id=0></pre></div><style></style></body></html>";
        StringBuilder text = new StringBuilder();
        for (Pair<Post, Opinion> data : dataCollection) {
            Post post = data.getLeft();
            Opinion opinion = data.getRight();
            text.append("<h1 >Report on post by ").append(post.author.username).append("  from ").append(post.date).append("</h1>");
            text.append("<h2 >The post evaluated as ").append(opinion).append("</h2>");
            text.append("<p><strong>Post: </strong>").append(post.text).append("</p>");
            text.append("<p><strong>Comments: </strong></p>");
            for (int e = 0; e < post.comments.size(); e++) {
                Comment comment = post.comments.get(e);
                text.append("<p style = \"margin-left: 50px;\"><strong>").append(comment.author.username).append("</strong> on <strong>").append(comment.date).append(": </strong>").append(comment.text).append("<p>");
            }
        }
        htmlText = htmlText.replace("<pre id=0></pre>", text.toString());

        try {
            FileWriter myWriter = new FileWriter("report.html");
            myWriter.write(htmlText);
            myWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        String pathString = System.getProperty("user.dir") + "/report.html";
        System.out.printf("Generated HTML report at %s%n", pathString);
        try {
            openWebpage("file:///" + pathString);
        } catch (UnsupportedOperationException e) {
            System.out.println("Your system doesn't support browser integration, please open report manually");
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }


    private static void openWebpage(String urlString) throws Exception {
        Desktop.getDesktop().browse(new URL(urlString).toURI());
    }

    @Override
    public void loadData(Pair<Post, Opinion> data) {
        dataCollection.add(data);
    }
}