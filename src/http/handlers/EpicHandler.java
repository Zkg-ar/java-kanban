package http.handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import controllers.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;

public class EpicHandler extends Handler{
    private TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }


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
                writeResponse(exchange, gson.toJson(manager.getEpics()), 200);
            } catch (RuntimeException | IOException exp) {
                System.out.println(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (!manager.getSubtasks().containsKey(id)) {
                writeResponse(exchange, "Эпик с id = " + id + "отсутсвует", 204);
            } else {
                writeResponse(exchange, gson.toJson(manager.getEpicById(id)), 200);
            }
        }
    }

    @Override
    public void handlePostRequest(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

            Epic epic = gson.fromJson(body, Epic.class);
            System.out.println(epic.getId());
            if (!manager.getEpics().containsKey(epic.getId())) {
                manager.addEpic(epic);
                writeResponse(exchange, "Эпик " + epic.getId() + " создана.\n" + body, 200);
            } else {
                manager.updateEpic(epic);
                writeResponse(exchange, "Эпик " + epic.getId() + " обновлена.\n" + body, 200);
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
                manager.clearEpicsMap();
                writeResponse(exchange, "Все эпики успешно удалены", 200);
            } catch (IllegalArgumentException | IOException exp) {
                System.out.println(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (manager.getEpics().containsKey(id)) {
                manager.removeEpicById(id);
                writeResponse(exchange, "Эпик с id = " + id + " успешно удалена", 200);
            } else {
                writeResponse(exchange, "Эпик с id = " + id + " отсутсвтует", 204);
            }
        }
    }
}

