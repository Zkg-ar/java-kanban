package http.servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.FileBackedTaskManager;
import controllers.HistoryManager;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import http.Handler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer server;
    private static final int PORT = 8080;


    public HttpTaskServer(TaskManager manager) throws IOException {

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new Handler(manager));
        server.start();
    }
}

