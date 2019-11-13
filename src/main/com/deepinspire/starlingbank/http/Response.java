package main.com.deepinspire.starlingbank.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Response {
    private int status;
    private String message;
    private Map<String, List<String>> headers;
    private String content;

    public Response(int responseCode, String responseMessage, Map<String, List<String>> headerFields, String content) {
        this.status = responseCode;
        this.message = responseMessage;
        this.headers = headerFields;
        this.content = content;
    }

    public int getStatusCode() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public String getContent() {
        return this.content;
    }

    /**
     * Returns a string representation of the object.
     * @return  a string representation of the object.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Response status code
        builder
            .append(this.getStatusCode())
            .append(" ")
            .append(this.getMessage())
            .append("\n");

        // Response headers
        for (Map.Entry<String, List<String>> entry : this.getHeaders().entrySet()) {
            if (entry.getKey() == null) continue;
            builder.append(entry.getKey()).append(": ");

            List<String> headerValues = entry.getValue();
            Iterator<String> it = headerValues.iterator();
            if (it.hasNext()) {
                builder.append(it.next());
                while (it.hasNext()) {
                    builder.append(", ").append(it.next());
                }
            }
            builder.append("\n");
        }

        // Response content
        builder
            .append("\n")
            .append(this.getContent())
            .append("\n")
            .append("\n");

        return builder.toString();
    }
}
