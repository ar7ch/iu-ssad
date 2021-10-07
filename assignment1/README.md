# In our project, we  have implemented some parts of the system for opinion mining in social networking sites. 

#### The main feature of our project is the post evaluation system. 

The evaluation works in this way: 
1. A user creates a post that is their opinion on some topic.
2. Some users comment on the post that is their reaction to the opinion.
3. Likes on comments are multipliers of reactions, when one likes a comment it means they agree with that reaction.
4. Basing on the keywords in the comments, the analysis system evaluates whether the post topic is Good, Bad, or Worst.
The project has a single keyword database, which allows the Analysis System to make uniform calculations of any post.
## ***To make it we use the singleton OOP pattern.***
![assignment1_usecase drawio](https://user-images.githubusercontent.com/37394070/136443806-338b4af8-246a-4591-907f-7eca2630586a.png)

# Why do we use singleton?
Singleton allows us to have exactly one instance of the database, enabling data consistency across the system.
It is necessary for providing the adequate feature of posts evaluation. Since the Keywords database is uniform, the process of evaluation will be the same for the whole social networking site. 

## How we implemented singleton? 

In the KeywordDataBase the constructor is private and is called only by the public getKDBConnection() method. This method checks if the dbInstance exists, if so getKDBConnection() returns a reference to the instance, otherwise calls the constructor. In this way, the uniqueness of the singleton entity is supported.


    private KeywordDatabase(){};
    public static KeywordDatabase getKDBConnection() {
        if (dbInstance==null)
            dbInstance = new KeywordDatabase();
        return dbInstance;
    }
    
# UML-diagramm
![assignment1 drawio](https://user-images.githubusercontent.com/70723894/136433320-c500ab7d-ab74-4b64-bafd-8fcc70776510.png)

# The main entities we have:
* # Actors:
### Abstract User
Abstract user is a base class for other particular types of users.
### User
A class that represents an ordinary user of a social networking site who can post his views on some subject, read and comment on any posts they liked or disliked.
### Admin
An entity that has the same methods as the User, but also they can add new keywords in the keyword database.
* # Text-based classes:
### Text Entity
This is a data class that acts as a base for other particular text entities.
### Post
Represents opinion of some user on some topic. A post has a list of comments attached to it.
### Comment 
Represents reaction of some user on someone's opinion (post). Comment has a counter of likes.
* # DataBase parts:
### ConcreteKeywordDataBase
A class that holds keywords and allows to manage them. Represented as a singleton in our system.
### Keyword 
A data class that describes a single keyword and its positivity weight. The weight is chosen from the enumeration Opinion : (Worst, Bad, or Good).
* # AnalysisSystem

The class provides the main feature - evaluation of a post by keywords in comments. The Analysis system is singleton to provide users with one and only adequate evaluation.  

