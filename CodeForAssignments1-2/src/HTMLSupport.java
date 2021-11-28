import java.awt.*;
import java.io.FileWriter;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public interface HTMLSupport<T> {
    void createHTML();

    void loadData(T data);
}

class HTMLAnalysisAdapter implements HTMLSupport<RatedPostSnapshotSupport> {
    private String htmlText;
    private RatedPostSnapshotSupport rpssInstance;

    public String createTable(UUID postIndex) {
        ArrayList<RatedPostSnapshot> list = rpssInstance.getPostsWithHistory().get(postIndex);
        String table = "<table><tr><th>Date</th><th>Value</th><th>Evaluation</th></tr>";
        for (RatedPostSnapshot item : list) {
            Date date = item.getCreationDate();
            Opinion opinion = item.getOpinion();
            Integer opinionValue = item.getOpinionValue();
            table += "<tr><td>" + date + "</td><td>" + opinionValue.toString() + "</td><td>" + opinion.toString() + "</td></tr>";
        }
        table += "</table>";
        return table;
    }

    public String DescribePosts() {
        String out = "";
        for (UUID postIndex : rpssInstance.getPosts().keySet()) {
            var post = rpssInstance.getPosts().get(postIndex);
            out += "<h2>Post by <small>"+post.author+"</small> from <small>"+post.date+"</small> <br><small><i>-\""+post.text+"\"</i></small> </h2>";
            var snapArr = rpssInstance.getPostsWithHistory().get(postIndex);
            out += "<h3>The Table of Opinion Dynamics</h3>";
            out+=createTable(postIndex);
            for (RatedPostSnapshot ratedPostSnapshot : snapArr) {
                out += DescribePost(rpssInstance.getPosts().get(postIndex), ratedPostSnapshot);
            }
        }
        return out;
    }

    @Override
    public void createHTML() {
        //Create the HTML template
        htmlText = """
                <!DOCTYPE html><html lang="en"><head> <meta name="description" content="Report" /> <meta charset="utf-8"> <title>Report</title> <style>
                table, th, td {
                  border: 1px solid black;
                }
                table{ border-collapse: collapse; text-align: center;}</style></head><body><div class="container"> <h1>Mined opinions</h1><pre id=0></pre></div><style></style></body></html>""";
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
        text.append(String.format("<h3> <u>Snapshot</u> from %s</h3>" +
                        "<h4>The post evaluated as: %s</h4>" +
                        "<p><strong>Comments: </strong></p>"
                , postSnapshot.getCreationDate(), postSnapshot.getOpinion()));
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
        rpssInstance = data;
    }

    private static void openWebpage(String urlString) throws Exception {
        Desktop.getDesktop().browse(new URL(urlString).toURI());
    }
}