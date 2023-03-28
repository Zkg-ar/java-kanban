package http.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;

public class TaskHandler extends Handler {

    private TaskManager manager;


    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange httpExchange) {
        System.out.println("Соединение установлено!");

        Methods methods = Methods.valueOf(httpExchange.getRequestMethod());
        try {

            switch (methods) {
                case GET:
                    handleGetRequest(httpExchange);
                    break;
                case POST:
                    handlePostRequest(httpExchange);
                    break;
                case DELETE:
                    handleDeleteRequest(httpExchange);
                    break;
                default:
                    httpExchange.sendResponseHeaders(405,0);
            }
        } catch (JsonParseException | IOException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void handleGetRequest(HttpExchange exchange) throws IllegalArgumentException, IOException {

        if (exchange.getRequestURI().getQuery() == null) {
            try {
                writeResponse(exchange, gson.toJson(manager.getTasks()), 200);
            } catch (RuntimeException | IOException exp) {
                System.out.println(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (!manager.getTasks().containsKey(id)) {
                writeResponse(exchange, "Задача с id = " + id + "отсутсвует", 204);
            } else {
                writeResponse(exchange, gson.toJson(manager.getTaskById(id)), 200);
            }
        }
    }

    @Override
    public void handlePostRequest(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

            Task task = gson.fromJson(body, Task.class);
            System.out.println(task.getId());
            if (!manager.getTasks().containsKey(task.getId())) {
                manager.addTask(task);
                writeResponse(exchange, "Задача " + task.getId() + " создана.\n" + body, 200);
            } else {
                manager.updateTask(task);
                writeResponse(exchange, "Задача " + task.getId() + " обновлена.\n" + body, 200);
            }
        } catch (RuntimeException | IOException exp) {
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    @Override

    public void handleDeleteRequest(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            try {
                manager.clearTasksMap();
                writeResponse(exchange, "Все задачи успешно удалены", 200);
            } catch (IllegalArgumentException | IOException exp) {
                System.out.println(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (manager.getTasks().containsKey(id)) {
                manager.removeTaskById(id);
                writeResponse(exchange, "Задача с id = " + id + " успешно удалена", 200);
            } else {
                writeResponse(exchange, "Задача с id = " + id + " отсутсвтует", 204);
            }
        }
    }
}

