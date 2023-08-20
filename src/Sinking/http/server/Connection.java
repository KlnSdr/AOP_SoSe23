package Sinking.http.server;

import Sinking.common.Json;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Connection implements IConnection {
    private final HttpExchange httpExchange;

    private final Map<String, List<String>> uriParams;
    private final String path;
    private final Headers headers;
    private final Json requestBody;
    private final Charset ENCODING = StandardCharsets.UTF_8;
    private ResponseCode responseCode = ResponseCode.SUCCESS;

    public Connection(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;

        this.path = getRequestPath(httpExchange.getRequestURI());
        this.uriParams = getRequestParams(httpExchange.getRequestURI());

        this.requestBody = extractBody();

        this.headers = httpExchange.getRequestHeaders();
    }

    public Headers getHeaders() {
        return this.headers;
    }

    public boolean hasHeader(String key) {
        return this.headers.containsKey(key);
    }

    public Optional<List<String>> getHeader(String key) {
        return Optional.ofNullable(this.headers.get(key));
    }

    public String getPath() {
        return path;
    }

    public void setResponseCode(ResponseCode code) {
        this.responseCode = code;
    }

    public Json getRequestBody() {
        return this.requestBody;
    }

    public Map<String, List<String>> getUriParams() {
        return this.uriParams;
    }

    private Json extractBody() throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();

        InputStream istream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(istream);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            bodyBuilder.append(data);
        }
        scanner.close();
        istream.close();
        return Json.parse(bodyBuilder.toString());
    }

    public void sendResponse() throws IOException {
        this.send("");
    }

    public void sendResponse(Json jsonPayload) throws IOException {
        String responseBody = jsonPayload.toString();
        this.send(responseBody);
    }

    private void send(String responseBody) throws IOException {
        int resCodeValue = this.responseCode.getValue();
        httpExchange.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", ENCODING));
        final byte[] rawResponseBody = responseBody.getBytes(ENCODING);
        httpExchange.sendResponseHeaders(resCodeValue, resCodeValue == 204 ? -1 : rawResponseBody.length);

        if (resCodeValue != 204) {
            httpExchange.getResponseBody().write(rawResponseBody);
        }

        httpExchange.close();
    }

    private String getRequestPath(URI uri) {
        return uri.getPath();
    }

    private Map<String, List<String>> getRequestParams(URI uri) {
        Map<String, List<String>> result = new HashMap<>();
        String requestQuery = uri.getRawQuery();
        if (requestQuery != null) {
            String[] splittedQuery = requestQuery.split("&");
            for (String param : splittedQuery) {
                String[] splittedParam = param.split("=", 2);
                String paramName = decodeUrlComponent(splittedParam[0]);
                result.putIfAbsent(paramName, new ArrayList<>());

                if (splittedParam.length > 1) {
                    result.get(paramName).add(decodeUrlComponent(splittedParam[1]));
                }
            }
        }
        return result;
    }

    private String decodeUrlComponent(String component) {
        try {
            return URLDecoder.decode(component, ENCODING);
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }
}
