# Report on extension of the opinion mining system
## Introduction

As it was before, the evaluation works in the following way:

1. A user creates a post that is their opinion on some topic.
2. Some users comment on the post that is their reaction to the opinion.
3. Likes on comments are multipliers of reactions. When one likes a comment, it means they agree with that reaction.
4. Based on the keywords in the comments, the analysis system evaluates whether the post topic is Good, Bad, or Worst. The project has a single keyword database, which allows the Analysis System to make uniform calculations of any post.
5. The evaluations can be seen in a web browser.
6. **From now on, HTML reports show the dynamics of the opinions.**

### New feature: Opinion Dynamics over time

![assignment3_usecase](ps://user-images.githubusercontent.com/37394070/140531213-33286c6c-ff3b-486c-adc6-b2973f16f235.png)


New use cases are grayed out.

Recently, a customer of the opinion mining system representing a well-known advertising company came to our team.
They asked for a possibility to see how people's opinions on a topic have changed over time.
So with this data, they can build the right advertising strategy and get more profit.

As our system already had the feature to generate reports on mined opinions, we decided to expand this notion with the usage of snapshots. Those snapshots represent the state of people's opinion on some topic at some moment in time.
While implementing the feature, our team met a challenge - the opinion mining system could not save states of the mined opinions straightaway.

Hence, we applied **the Memento pattern** that allows our system to save the state of the mined opinion, and restore data from that state. Therefore, our clients are able to see the dynamics of mined opinions over time.

## How did we implement the Memento pattern?

![assignment2UML](ps://user-images.githubusercontent.com/37394070/140531284-c97d1c79-52a0-483c-b600-a953770ffdde.png)

New and updated entities are grayed out. The full class diagram is attached to the submission in .png format.

HTMLAnalysisAdapter has a reference to RatedPostSnapshotSupport which is a caretaker of memento and keeps the history of snapshots. The system loads snapshots of mined opinions of different time moments from that history, formats them in a human-readable way, and outputs them to HTML.
## Description of UML Diagram entities

**Web Browser** - is an external client of the system.

**HTMLSupport** - is an interface to work with HTML files.

**HTMLAnalysisAdapter** - is the class implementing the adapter pattern.

**Pair<L, R>** - is an auxiliary class for convenient data transfer.

**AnalysisSystem** - is the main entity of our project that implements the technical task of the customer.  
  
  <br>

## Code

Below is the example of memento pattern usage from the testing class Main.java. The code saves the state of the mined opinion. When needed, the code restores the desired state, converts it to the HTML format then passes it to a Web Browser.
```java
 Comment c4 = u3.createComment("The actors play good and the message behind is valuable", post);
 c4.date.setTime(post.date.getTime() + (7123 * 60 * 1000));
 c4.likes = 7;
 RPSS.makeSnapshot(post); //save state of the mined opinion
 Thread.sleep(700);

 Comment c5 = u4.createComment("Although the graphics was poor, the plot is cool :)", post);
 c5.date.setTime(post.date.getTime() + (8123 * 60 * 1000));
 c5.likes = 2;
 RPSS.makeSnapshot(post); //save state of the mined opinion

 //export history to a second file
 Files.write(Paths.get("output2.txt"),RPSS.saveToJson().getBytes(StandardCharsets.UTF_8));
 RPSS.undoSnapshot(post.id); // undo last snapshot of the post so state changed
 // ... some operations on that state may happen here
 //export history to a third file
 Files.write(Paths.get("output3.txt"),RPSS.saveToJson().getBytes(StandardCharsets.UTF_8));
 //restoring snapshot data from the second file
 RPSS.restoreFromJson(Files.readString(Paths.get("output2.txt")));

 admin.generateHtmlReport(RPSS); //Output the evaluation of the restored state to an HTML file
```
