public class AnalysisSystem {
    private static AnalysisSystem systemInstance;

    private AnalysisSystem() {
    }

    public static AnalysisSystem getConnection() {
        if (systemInstance == null)
            systemInstance = new AnalysisSystem();

        return systemInstance;
    }

    public Pair<Integer, Opinion> evaluatePost(Post post) {
        KeywordDatabase KWD = KeywordDatabase.getKDBConnection();
        int value = 0;
        int count = 0;
        for (Comment c : post.comments) {
            String s = c.text;
            String[] words = s.split("\\W+");
            for (int i = 0; i < words.length; i++) {
                Keyword word = KWD.getKeyword(words[i]);
                if (word != null) {
                    count += (1 + c.likes);
                    value += (word.opinion.ordinal() - 1) * (1 + c.likes);
                }
            }
        }
        float valuef = ((float) value) / count;
        if (valuef < -0.5) return new Pair<>((int)(valuef*100), Opinion.Worst);
        if (valuef < 0.5) return new Pair<>((int)(valuef*100), Opinion.Bad);
        if (valuef >= 0.5) return new Pair<>((int)(valuef*100), Opinion.Good);

        return null;
    }
}
