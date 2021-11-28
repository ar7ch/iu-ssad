public record Pair<L, R>(L Left, R Right) {

    public Pair {
        assert Left != null;
        assert Right != null;
    }

    public L getLeft() {
        return Left;
    }

    public R getRight() {
        return Right;
    }

    @Override
    public int hashCode() {
        return Left.hashCode() ^ Right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair pair)) return false;
        return this.Left.equals(pair.getLeft()) &&
                this.Right.equals(pair.getRight());
    }

}