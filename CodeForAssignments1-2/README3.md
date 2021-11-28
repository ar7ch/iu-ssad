# Report on extension of the opinion mining system
## Introduction

As it was before, the evaluation works in the following way:

1. A user creates a post that is their opinion on some topic.
2. Some users comment on the post that is their reaction to the opinion.
3. Likes on comments are multipliers of reactions. When one likes a comment, it means they agree with that reaction.
4. Based on the keywords in the comments, the analysis system evaluates whether the post topic is Good, Bad, or Worst. The project has a single keyword database, which allows the Analysis System to make uniform calculations of any post.
5. The evaluations can be seen in a web browser.
6. **From now on, HTML reports show the dynamics of the opinions.**

### New feature: Show Opinion Dynamics over time

![assignment3_usecase](https://github.com/ar7ch/iu-ssad/blob/master/assignment3/uml/assignment3_usecase.png?raw=true)


New use cases are grayed out.

Recently, a customer of the opinion mining system representing a well-known advertising company came to our team.
They asked for a possibility to see how people's opinions on a topic have changed over time.
So with this data, they can build the right advertising strategy and get more profit.

As our system already had the feature to generate reports on mined opinions, we decided to expand this notion with the usage of snapshots. Those snapshots represent the state of people's opinion on some topic at some moment in time.  

While implementing the new feature, our team met a challenge - the opinion mining system could not save states of the mined opinions straightaway.

Hence, we applied **the Memento pattern**. This pattern allows our system to save the state of the mined opinion, and restore data from that state. Therefore, our clients can see the dynamics of mined opinions over time.

## Example of an HTMLReport
The picture below is the part of a generated report that uses our implementation of the Memento pattern. This report shows the dynamics of the mined opinions. Our customers are fully satisfied with this output of our system since it allows them to track people's opinions on actual trends.  
The data for this report is fully fictional. We respect the privacy of our clients and do not reveal real information. 
![ReportExample](https://github.com/ar7ch/iu-ssad/blob/master/assignment3/NewReport.jpg?raw=true)

## How did we implement the Memento pattern?

![assignment2UML](ps://user-images.githubusercontent.com/37394070/140531284-c97d1c79-52a0-483c-b600-a953770ffdde.png)

New and updated entities are grayed out. The full class diagram is attached to the submission in .png format.

HTMLAnalysisAdapter has a reference to RatedPostSnapshotSupport which is a caretaker of memento and keeps the history of snapshots. The system loads snapshots of mined opinions of different time moments from that history, formats them in a human-readable way, and outputs them to HTML.
## Description of UML Diagram entities

Below is the description of the most important classes for the implementation of the Memento pattern. 

**RatedPostSnapshot** - is an implementation for the Memento class, it stores the state of an evaluated post.  

**RatedPost** - is an originator class for RatedPostSnapshot, it produces the snapshots and may restore state from one of them. Moreover, it extends the Post class keeping the evaluated value.  

**RatedPostSnapshotSupport** - is a caretaker class, it keeps the history of the snapshots.  

**Pair<L, R>** - is an auxiliary class for convenient data transfer.

**Web Browser** - is an external client of the system.

**HTMLAnalysisAdapter** - is the class that proceeds system data into human-readable HTML page format. 

**AnalysisSystem** - is the main entity of our project that implements the technical task of the customer.  
  
  <br>

## Code

Below is the example of memento pattern usage from the testing class Main.java. The code saves the state of the mined opinion. When needed, the code restores the desired state, converts it to the HTML format then passes it to a Web Browser.
```java
 Comment c4 = u3.createComment("The actors play good and the message behind is valuable", post);
 c4.likes = 7;
 RPSS.makeSnapshot(post); //save state of the mined opinion
 Thread.sleep(1000);

 Comment c5 = u4.createComment("Although the graphics was poor, the plot is cool :)", post);
 c5.likes = 2;
 RPSS.makeSnapshot(post); //save state of the mined opinion

 //export history to a second file
 Files.write(Paths.get("output2.json"),RPSS.saveToJson().getBytes(StandardCharsets.UTF_8));
 RPSS.undoSnapshot(post.id); // undo last snapshot of the post so state changed
 // ... some operations on that state may happen here
 //export history to a third file
 Files.write(Paths.get("output3.json"),RPSS.saveToJson().getBytes(StandardCharsets.UTF_8));
 //restoring snapshot data from the second file
 RPSS.restoreFromJson(Files.readString(Paths.get("output2.json")));

 admin.generateHtmlReport(RPSS); //Output the evaluation of the restored state to an HTML file
```

