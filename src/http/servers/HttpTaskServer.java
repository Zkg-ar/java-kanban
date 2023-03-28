package http.servers;

import com.sun.net.httpserver.HttpServer;
import controllers.TaskManager;
import http.handlers.EpicHandler;
import http.handlers.SubtaskHandler;
import http.handlers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final HttpServer server;
    private static final int PORT = 8080;


    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task", new TaskHandler(manager));
        server.createContext("/tasks/subtask", new SubtaskHandler(manager));
        server.createContext("/tasks/epic", new EpicHandler(manager));
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop(0);
    }
}

