package Sinking.http.client;

public class Consistency {
    private static Consistency instance = null;
    private int requestCount = 0;
    private int failedRequests = 0;

    private Consistency() {}

    public static Consistency getInstance() {
        if (instance == null) {
            instance = new Consistency();
        }
        return instance;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public void reportFailedRequest() {
        this.failedRequests++;
    }

    public void checkConnection(Runnable success, Runnable failure) {
        Client client = new Client("https://google.com", 5000);
        Request req = client.newRequest("/");
        System.out.println("requesting");
        client.get(req, res -> success.run(), err -> failure.run());
    }
}
