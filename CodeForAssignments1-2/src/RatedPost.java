import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RatedPost extends Post{
    private Opinion opinion;
    private Integer opinionValue;
    public RatedPost(Post post, Pair<Integer,Opinion> opinionPair) {
        super(post.text,post.author);
        this.opinion = opinionPair.getRight();
        this.opinionValue = opinionPair.getLeft();
        this.date = post.date;
        this.id = post.id;
        this.comments = new ArrayList<>(post.comments);
    }

    public void restoreState(RatedPostSnapshot memento) {
        this.comments = memento.getComments();
        this.opinion = memento.getOpinion();
        // доделать
    }

    public RatedPostSnapshot saveState() {
        RatedPostSnapshot snapshot = new RatedPostSnapshot(this.comments, AnalysisSystem.getConnection().evaluatePost(this)  );
        return snapshot;
    }
}

class RatedPostSnapshot {
    private ZonedDateTime creationDate;
    private Opinion opinion;

    public Integer getOpinionValue() {
        return opinionValue;
    }

    private Integer opinionValue;
    private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return new ArrayList<Comment>(comments);
    }

    public RatedPostSnapshot(ArrayList<Comment> comments, Pair<Integer, Opinion> opinionPair) {
        this.opinionValue = opinionPair.getLeft();
        this.comments = new ArrayList<Comment>(comments);
        this.opinion = opinionPair.getRight();
        this.creationDate = ZonedDateTime.now();
    }
    public Opinion getOpinion(){return this.opinion;}

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

}

class RatedPostSnapshotSupport {
    //this block allows us to serialize and deserialize ZonedDataTime
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.value(value.toString());
                }
                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return ZonedDateTime.parse(in.nextString());
                }
            })
            .enableComplexMapKeySerialization()
            .create();

    public HashMap<UUID, RatedPost> getPosts() {
        return posts;
    }

    public HashMap<UUID, ArrayList<RatedPostSnapshot>> getPostsWithHistory() {
        return postsWithHistory;
    }

    private HashMap<UUID, RatedPost> posts;
    private HashMap<UUID, ArrayList<RatedPostSnapshot>> postsWithHistory;
    RatedPostSnapshotSupport()
    {
        posts = new HashMap<UUID, RatedPost>();
        postsWithHistory = new HashMap<UUID, ArrayList<RatedPostSnapshot>>();
    }
    public void makeSnapshot(RatedPost post) {
        UUID idx = post.id;
        posts.put(idx, post);
        if(postsWithHistory.get(idx)==null) {postsWithHistory.put(idx, new ArrayList<RatedPostSnapshot>());}
        postsWithHistory.get(idx).add(post.saveState());
    }
    public void makeSnapshot(Post post) {
        makeSnapshot(new RatedPost(post, AnalysisSystem.getConnection().evaluatePost(post)));
    }

    public void undoSnapshot(UUID idx) {
        ArrayList<RatedPostSnapshot> postHistory = this.postsWithHistory.get(idx);
        this.posts.get(idx).restoreState(postHistory.get(postHistory.size()-1));
    }

    public String saveToJson() {
        return GSON.toJson(this);
    }

    public void restoreFromJson(String json) {
        this.posts = GSON.fromJson(json, RatedPostSnapshotSupport.class).posts;
        this.postsWithHistory = GSON.fromJson(json, RatedPostSnapshotSupport.class).postsWithHistory;
    }
}