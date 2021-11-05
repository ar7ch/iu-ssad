public record Pair<L, R>(L Post, R Opinion) {

    public Pair {
        assert Post != null;
        assert Opinion != null;

    }

    public L getLeft() {
        return Post;
    }

    public R getRight() {
        return Opinion;
    }

    @Override
    public int hashCode() {
        return Post.hashCode() ^ Opinion.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair pair)) return false;
        return this.Post.equals(pair.getLeft()) &&
                this.Opinion.equals(pair.getRight());
    }

}