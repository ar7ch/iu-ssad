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

    public String createTable(){
        AtomicReference<UUID> idx = new AtomicReference<>();
        final String[] table = {""};
        dataCollection.posts.forEach((k,v)->{
            idx.set(k);
            UUID index = idx.get();
            ArrayList<RatedPostSnapshot> list = dataCollection.postsWithHistory.get(index);
            String tabel = "<table><tr><th>Date</th><th>Evaluation</th></tr>";
            for (RatedPostSnapshot item : list) {
                ZonedDateTime time = item.getCreationDate();
                Opinion opinion = item.getOpinion();
                tabel += "<tr><td>"+time.toLocalDateTime().toString().split("T")[0].replace("-","/")+"</td><td>"+opinion.toString()+"</td>";
            }
            tabel += "</table>";
            table[0] += tabel;
        });

        return table[0];
    }

    @Override
    public void createHTML() {
        //Create the HTML template
        htmlText = "<!DOCTYPE html><html lang=\"en\"><head> <meta name=\"description\"" +
                " content=\"Report\" /> <meta charset=\"utf-8\"> " +
                "<title>Report</title></head><body><div class=\"container\"> " +
                "<pre id=0></pre></div><style></style></body></html>";
        //Fill the HTML template

        htmlText = htmlText.replace("<pre id=0></pre>", createTable());
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

    @Override
    public void loadData(RatedPostSnapshotSupport data) {
//        dataCollection.add(data);
        dataCollection = data;
    }

    private static void openWebpage(String urlString) throws Exception {
        Desktop.getDesktop().browse(new URL(urlString).toURI());
    }
}