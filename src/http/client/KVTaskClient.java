package http.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;

public class KVTaskClient {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8078;
    private final String url;
    private String token;

    public KVTaskClient(String url) {

        this.url = url;
        token = registerAPIToken(url);

    }

    private String registerAPIToken(String url) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            token = response.body();

        } catch (InterruptedException | IOException e) {

            System.out.println("Регистрация API токена закончилась неудачно. Причина:" + e.getMessage());

        }

        return token;

    }

    public void put(String key, String value) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value, DEFAULT_CHARSET);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {

            System.out.println("Сохранение закончилось неудачно. Причина:" + e.getMessage());

        }

    }

    public String load(String key) {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {

            System.out.println("Загрузка закончилась неудачно. Причина:" + e.getMessage());

        }

        return response != null ? response.body() : "Успешно";

    }

}