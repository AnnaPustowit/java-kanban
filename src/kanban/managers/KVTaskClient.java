package kanban.managers;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private URI  url;
    private HttpClient client = HttpClient.newBuilder().build();;


    public KVTaskClient(URI  url) throws IOException, InterruptedException {
        this.url = url;
       // client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(this.url+"/register")).build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri1 = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        //final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().GET()/*POST(body)*/.uri(uri1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200) {
            throw new IOException("Код ошибки - " + response.statusCode());
        } else {
            System.out.println(response.statusCode());
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        String managerStatus = null;
        URI uri1 = URI.create(url + "/load/" + key + "?API_TOKEN=" +apiToken);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            managerStatus = response.body();
        } else {
            throw new IOException("Код ошибки - " + response.statusCode());
        }
        return managerStatus;
    }

}
