public interface HTMLSupport<T> {
    public void createHTML();

    public void loadData(T data);
}

class HTMLAnalysisAdapter<T> implements HTMLSupport {
    private String htmlText;

    @Override
    public void createHTML() {
        return;
    }

    @Override
    public void loadData(T data) {
        return;
    }
}