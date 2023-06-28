public class Request {
    private String word;

    public Request(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "Request { " +
                "word = '" + word + '\'' +
                " } ";
    }
}
