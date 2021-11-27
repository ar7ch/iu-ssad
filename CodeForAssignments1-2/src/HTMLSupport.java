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
        //Create the HTML template
        htmlText = "<!DOCTYPE html><html lang=\"en\"><head> <meta name=\"description\"" +
                " content=\"Report\" /> <meta charset=\"utf-8\"> " +
                "<title>Report</title></head><body><div class=\"container\"> " +
                "<pre id=0></pre></div><style></style></body></html>";
        StringBuilder text = new StringBuilder();
        //Fill the HTML template
        for (Pair<Post, Opinion> data : dataCollection) {
            Post post = data.getLeft();
            text.append(String.format("<h1 >Report on post by %s from %s</h1>" +
                            "<h2>The post evaluated as %s</h2>" +
                            "<p><strong>Post: %s</strong></p><p><strong>Comments: </strong></p>"
                    , post.author, post.date.toString(), data.getRight(), post.text));
            for (Comment comment : post.comments)
                text.append(String.format("<p style = \"margin-left: 50px;\"><strong>%s</strong> on" +
                                " <strong>%s</strong>: %s<p>", comment.author,
                        comment.date.toString(), comment.text));
        }
        htmlText = htmlText.replace("<pre id=0></pre>", text.toString());
        //Export the HTML page
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
        //Open the HTML in a Web Browser
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