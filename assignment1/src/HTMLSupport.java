import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Stream;



public interface HTMLSupport<T> {
    public void createHTML();

    public void loadData(T data);
}

class HTMLAnalysisAdapter implements HTMLSupport<Pair<Post,Opinion>> {
    private String htmlText;
    private ArrayList<Pair<Post,Opinion>> dataCollection = new ArrayList<Pair<Post,Opinion>>();

    @Override
    public void createHTML() {

        var html = "";
        FileReader fileReader = null;
        try {
            html = "<!DOCTYPE html><html lang=\"en\"><head> <meta name=\"description\" content=\"Report\" /> <meta charset=\"utf-8\"> <title>Report</title></head><body><div class=\"container\"> <pre id=0></pre></div><style></style></body></html>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        String text = "";
        for(int i=0;i< dataCollection.size();i++)
        {
            var data = dataCollection.get(i);
            var post = data.getLeft();
            var opinion = data.getRight();
            text+="<h1 >Report on post by "+post.author.username+"  from "+post.date+"</h1>";
            text+="<h2 >The post evaluated as "+opinion+"</h2>";
            text+="<p><strong>Post: </strong>"+post.text+"</p>";
            text+="<p><strong>Comments: </strong></p>";
            for(int e=0;e<post.comments.size();e++)
            {
                var comment = post.comments.get(e);
                text+="<p style = \"margin-left: 50px;\"><strong>"+comment.author.username+"</strong> on <strong>"+comment.date+": </strong>"+comment.text+"<p>";
            }
        }
        html = html.replace("<pre id=0></pre>",text);
        try {
            FileWriter myWriter = new FileWriter("report.html");
            myWriter.write(html);
            myWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String pathString = System.getProperty("user.dir")+"/report.html";
        System.out.println(String.format("Generated HTML report at %s", pathString));
        try {
            openWebpage("file:///"+pathString);
        }
        catch (Exception e) {
            System.out.println("Your system doesn't support browser integration, please open report manually");
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