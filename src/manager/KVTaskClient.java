package manager;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private URI uri; // Строка с адресом ресурса.
    private String API_TOKEN; // Ключ авторизации.
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private HttpClient client;

    // В конструкторе производится авторизация на сервере хранения и получение ключа.
    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        try {
            uri = URI.create(url + "/register");
            HttpRequest.Builder rb = HttpRequest.newBuilder();
            HttpRequest request = rb // создаем объект описывающий запрос
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, handler);
            this.API_TOKEN = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Метод для загрузеи данных о состоянии HttpTaskManager на сервере-хранилише.
    public void put(String key, String json) {
        try {
            uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=DEBUG");
            HttpRequest.Builder rb = HttpRequest.newBuilder();
            HttpRequest request = rb // создаем объект описывающий запрос
                    .uri(uri)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, handler);
            System.out.println("Код добавления/обновления по ключу " + key + " "
                    + response.statusCode());
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Метод выгрузки данных из хранилиша для восстановления состояния менеджера.
    HttpResponse<String> load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=DEBUG");
        HttpResponse<String> response = null;
        try {
            HttpRequest.Builder rb = HttpRequest.newBuilder();
            HttpRequest request = rb
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }
}