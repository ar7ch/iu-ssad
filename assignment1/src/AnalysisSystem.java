public class AnalysisSystem {
    private static AnalysisSystem systemInstance;

    private AnalysisSystem() {
    }

    public static AnalysisSystem getInstance() {
        if (systemInstance == null)
            systemInstance = new AnalysisSystem();

        return systemInstance;
    }

    public Opinion evaluatePost(Post post) {
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
        float valuef = (float) value / count;
        if (valuef < -0.5) return Opinion.Worst;
        if (valuef < 0.5) return Opinion.Bad;
        if (valuef >= 0.5) return Opinion.Good;

        return null;
    }
}
