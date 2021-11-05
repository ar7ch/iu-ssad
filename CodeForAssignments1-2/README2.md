#Report on extension of the opinion mining system
## Introduction

As it was before, the evaluation works in the following way:

1.  A user creates a post that is their opinion on some topic.
2.  Some users comment on the post that is their reaction to the opinion.
3.  Likes on comments are multipliers of reactions, when one likes a comment it means they agree with that reaction.
4.  Based on the keywords in the comments, the analysis system evaluates whether the post topic is Good, Bad, or Worst. The project has a single keyword database, which allows the Analysis System to make uniform calculations of any post.
5.  From now on, the evaluations can be seen in a web browser.

### New feature: Convert report of the evaluated post into an HTML page

![uml/assignment2_usecase.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f42399f2-5775-4876-9da3-7261f6813c45/assignment2_usecase.png)

New use cases are grayed out.

With our new feature, admins can make "HTML Reports" which are mined opinions in the format of an HTML page.

Both users and admins interact with a social networking site via some Web browser. Therefore, it's convenient to watch mined opinions in the same web browser. Those reports may be then sold to some advertising agency.

While implementing the feature, our team met a challenge - the opinion mining system had no possibility to output the evaluation just into the browser and the browser could not work with our java evaluations straightaway.

Hence, we decided to use **the Adapter pattern** that wraps up our system for the browser, so we can view the mined opinions with the Web Browser in the convenient format of the HTML page

## How did we implement the Adapter pattern?

![assignment222.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/078eb0ba-a000-46b2-b312-6b58fd82090d/assignment222.png)

New entities are grayed out, and the full class diagram is attached in the archive in .PNG format

HTMLAnalysisAdapter loads a Pair that has instances of Post and Opinion evaluated by our system. Then adapter formats the data to the format readable by a Web browser.

## Code

Below is the most interesting method of the adapter. The method accepts some data, converts it to the HTML format then passes it to a Web Browser.

```java
public void createHTML() {
    //Create the HTML template
    htmlText = "<!DOCTYPE html><html lang=\\"en\\"><head> <meta name=\\"description\\"" +
            " content=\\"Report\\" /> <meta charset=\\"utf-8\\"> " +
            "<title>Report</title></head><body><div class=\\"container\\"> " +
            "<pre id=0></pre></div><style></style></body></html>";
    StringBuilder text = new StringBuilder();
    //Fill the HTML template
    for (Pair<Post, Opinion> data : dataCollection) {
        Post post = data.getLeft();
        text.append(String.format("<h1 >Report on post by %s from %s</h1>" +
                    "<h2>The post evaluated as %s</h2>" +
                    "<p><strong>Post: %s</strong></p><p><strong>Comments: </strong></p>"
             , post.author.username, post.date.toString(), data.getRight(), post.text));
        for (Comment comment : post.comments)
            text.append(String.format("<p style = \\"margin-left: 50px;\\"><strong>%s</strong> on <strong>%s</strong>: %s<p>", comment.author.username,
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

```

## Description of UML Diagram entities

**Web Browser** - is an external client of the system.

**HTMLSupport** - is an interface to work with HTML files.

**HTMLAnalysisAdapter** - is the class implementing the adapter pattern.

**Pair<L, R>** - is an auxiliary class for convenient data transfer.

**AnalysisSystem** - is the main entity of our project that implements the technical task of the customer.
