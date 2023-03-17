package http.servers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KVServer {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Map<String, String> data = new HashMap<>();
    public static final int PORT = 8078;
    private final HttpServer server;
    private final String token;

    public KVServer() throws IOException {

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
        token = generateApiToken();

    }

    public void stop() {

        server.stop(0);
        System.out.println("KVServer остановлен на " + PORT + " порту!");

    }

    private void load(HttpExchange h) throws IOException {

        try {
            System.out.println("\n/load");
            if (!hasAuth(h)) {
                System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением API ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }

            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Ключ(key) для загрузки пустой. Ключ(key) указывается в пути: /load/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = data.get(key);
                if (value.isEmpty()) {

                    System.out.println("Значение(value) для загрузки пустое. Значение(value) указывается в теле запроса");

                    h.sendResponseHeaders(400, 0);
                    return;
                }

                sendText(h, value);

                System.out.println("Значение для ключа " + key + " успешно обновлено!");

                h.sendResponseHeaders(200, 0);

            } else {
                System.out.println("/load ждёт GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }

    }

    private void save(HttpExchange h) throws IOException {

        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением API ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Ключ(key) для сохранения пустой. Ключ(key) указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;

                }

                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Значение(value) для сохранения пустое. Значение(value) указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);

            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, token);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();

        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + token);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null
                && (rawQuery.contains("API_TOKEN=" + token)
                || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}