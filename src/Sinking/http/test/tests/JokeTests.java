package Sinking.http.test.tests;

import Sinking.http.client.Client;
import Sinking.http.client.Request;
import Sinking.http.test.ITestResult;
import Sinking.http.test.Test;

public class JokeTests {
    @Test(name = "win test")
    public void winTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/win");
        client.get(req, response -> resolve.returnResult(response.getStatusCode() == 402), error -> resolve.returnResult(false));
    }

    @Test(name = "give up test")
    public void giveUpTest(Client client, ITestResult resolve) {
        Request req = client.newRequest("/giveUp");
        client.get(req, response -> resolve.returnResult(response.getStatusCode() == 202), error -> resolve.returnResult(false));
    }
}
