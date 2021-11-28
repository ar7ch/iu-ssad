import java.awt.*;
import java.io.FileWriter;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


public interface HTMLSupport<T> {
    void createHTML();

    void loadData(T data);
}

class HTMLAnalysisAdapter implements HTMLSupport<RatedPostSnapshotSupport> {
    private String htmlText;
    private RatedPostSnapshotSupport dataCollection;

    public String createTable(UUID index) {
        ArrayList<RatedPostSnapshot> list = dataCollection.postsWithHistory.get(index);
        String table = "<table><tr><th>Date</th><th>Value</th><th>Evaluation</th></tr>";
        for (RatedPostSnapshot item : list) {
            ZonedDateTime time = item.getCreationDate();
            Opinion opinion = item.getOpinion();
            Integer opinionvalue = item.getOpinionValue();
            table += "<tr><td>" + cutDateTime(time) + "</td><td>" + opinionvalue.toString() + "</td><td>" + opinion.toString() + "</td></tr>";
        }
        table += "</table>";
        return table;
    }

    public String DescribePosts() {
        String out = "";
        for (UUID index : dataCollection.posts.keySet()) {
            var post =dataCollection.posts.get(index);
            out += "<h2>Post by "+post.author+" from "+post.date+" <br>\""+post.text+"\" </h2>";
            var snapArr = dataCollection.postsWithHistory.get(index);
            out += "<p>Opinion dynamics</p>";
            out+=createTable(index);
            for (int i = 0; i < snapArr.size(); i++) {
                out += DescribePost(dataCollection.posts.get(index), snapArr.get(i));
            }
        }
        return out;
    }

    String cutDateTime(ZonedDateTime zdt) {
        return zdt.toLocalDateTime().toString().split("\\.")[0].replace("-", "/").replace("T", " ");
    }

    @Override
    public void createHTML() {
        //Create the HTML template
        htmlText = "<!DOCTYPE html><html lang=\"en\"><head> <meta name=\"description\"" +
                " content=\"Report\" /> <meta charset=\"utf-8\"> " +
                "<title>Report</title> <style>\n" +
                "table, th, td {\n" +
                "  border: 1px solid black;\n" +
                "}\n" +
                "table{ border-collapse: collapse; text-align: center;}" +
                "</style></head><body><div class=\"container\"> <h1>Mined opinions</h1>" +
                "<pre id=0></pre></div><style></style></body></html>";
        //Fill the HTML template
        htmlText = htmlText.replace("<pre id=0></pre>", DescribePosts());
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

    public String DescribePost(RatedPost post, RatedPostSnapshot postSnapshot) {
        //Create the HTML template
        String out = "<pre id=0></pre>";
        StringBuilder text = new StringBuilder();
        //Fill the HTML template
        text.append(String.format("<h3 >Snapshot on post by %s from %s</h3>" +
                        "<h4>The post evaluated as %s</h4>" +
                        "<p><strong>Comments: </strong></p>"
                , post.author, cutDateTime(postSnapshot.getCreationDate()), postSnapshot.getOpinion()));
        for (Comment comment : postSnapshot.getComments())
            text.append(String.format("<p style = \"margin-left: 50px;\"><strong>%s</strong> on" +
                            " <strong>%s</strong>: %s<p>", comment.author,
                    comment.date.toString(), comment.text));

        out = out.replace("<pre id=0></pre>", text.toString());
        //Export the HTML page
        return out;
    }

    @Override
    public void loadData(RatedPostSnapshotSupport data) {
//        dataCollection.add(data);
        dataCollection = data;
    }

    private static void openWebpage(String urlString) throws Exception {
        Desktop.getDesktop().browse(new URL(urlString).toURI());
    }
}