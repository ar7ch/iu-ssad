# In our project, we have implemented the system for opinion mining in social networks. 

#### The main feature of our project is the post ranking system. The idea is that any user can create or comment a post. The posts are evaluated by the system. The ranking works in this way: a user creates a post that is their opinion on some topic, some users comment the post that is their reaction on the opinion, the Analysis System looks for  keywords in the text, if there any, it calculates the rank of the post. The project has a single keyword database, which allows the Analysis System to make uniform calculations of any post.
## ***To make it we use singleton OOP pattern.***
---![photo_2021-10-07_20-31-13](https://user-images.githubusercontent.com/70723894/136434964-6f92c8aa-c557-42a4-9975-b233a486bd21.jpg)

# Why do we use singleton?
Singleton allows us to have exactly one instance of the database, enabling data consistency across that system.
It is necessary for providing the feature of calculating the rank of some posts. Since the Keywords database is uniform, the rank will be the same for the whole site. 
## How we implemented singleton? 

In the ConcreteKeywordDataBase the constructor of DataBase is private and is calling only by the public getKDBConnection() method. This method checks if the DataBase instance exists, if so getKDBConnection() returns a pointer of the instance, in another case, calls the DataBase constructor. In this way, the uniqueness of the singleton entity is supported.


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
Abstract user is a base class of other particular types of users.
### User
A class that represents an ordinary user who can post his views on some subject, read and comment on any posts they liked or disliked.
### Admin
An entity that has the same methods as the User, but also they can put new keywords in that database.
* # Text-based classes:
### Text Entity
This is a data class that acts as a base for other particular text entities.
### Post
Text entity that has a list of comments attached to it.
### Comment 
Text entity that represents some user's opinion.
* # DataBase parts:
### ConcreteKeywordDataBase
A class that holds keywords and allows to manage them. Represented as a singleton in our system.
### Keyword 
A data class that describes a single keyword and its respective opinions.
* # AnalysisSystem

The class allows calculating the total post positivity score. Represented as a singleton. 

