package http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateTimeAdapter;
import model.Epic;
import model.Subtask;
import model.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;


public class Handler implements HttpHandler {

    private TaskManager manager;


    public Handler(TaskManager manager) {
        this.manager = manager;
    }

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

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
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    //GET
    private void handleGetRequest(HttpExchange exchange) {
        String[] pathArray = exchange.getRequestURI().getPath().split("/");
        try {
            if (pathArray.length == 3) {
                if (pathArray[2].equals("task")) {
                    handleGetTaskRequest(exchange);
                } else if (pathArray[2].equals("subtask")) {
                    handleGetSubtaskRequest(exchange);
                } else if (pathArray[2].equals("epic")) {
                    handleGetEpicRequest(exchange);
                } else if (pathArray[2].equals("history")) {
                    handleGetHistoryRequest(exchange);
                }
            } else if (pathArray.length == 2) {
                writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
            }
        } catch (IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handleGetTaskRequest(HttpExchange exchange) {
        try {
            if (exchange.getRequestURI().getQuery() == null) {
                writeResponse(exchange, gson.toJson(manager.getTasks()), 200);
            } else {
                String[] query = exchange.getRequestURI().getQuery().split("=");
                int id = Integer.parseInt(query[1].trim());
                if (!manager.getTasks().containsKey(id)) {
                    writeResponse(exchange, "Задача с id = " + id + "отсутсвует", 204);
                } else {
                    writeResponse(exchange, gson.toJson(manager.getTaskById(id)), 200);
                }
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handleGetSubtaskRequest(HttpExchange exchange) {
        try {
            if (exchange.getRequestURI().getQuery() == null) {
                writeResponse(exchange, gson.toJson(manager.getSubtasks()), 200);
            } else {
                String[] query = exchange.getRequestURI().getQuery().split("=");
                int id = Integer.parseInt(query[1].trim());
                if (!manager.getSubtasks().containsKey(id)) {
                    writeResponse(exchange, "Задача с id = " + id + "отсутсвует", 204);
                } else {
                    writeResponse(exchange, gson.toJson(manager.getSubtaskById(id)), 200);
                }
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handleGetEpicRequest(HttpExchange exchange) {
        try {
            if (exchange.getRequestURI().getQuery() == null) {
                writeResponse(exchange, gson.toJson(manager.getEpics()), 200);
            } else {
                String[] query = exchange.getRequestURI().getQuery().split("=");
                int id = Integer.parseInt(query[1].trim());
                if (!manager.getEpics().containsKey(id)) {
                    writeResponse(exchange, "Задача с id = " + id + "отсутсвует", 204);
                } else {
                    writeResponse(exchange, gson.toJson(manager.getEpicById(id)), 200);
                }
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handleGetHistoryRequest(HttpExchange exchange) {
        try {
            writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
        } catch (IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    //POST
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String[] pathArray = exchange.getRequestURI().getPath().trim().split("/");
        if (pathArray.length == 3) {
            if (pathArray[2].equals("task")) {
                handlePostTaskRequest(exchange);
            } else if (pathArray[2].equals("subtask")) {
                handlePostSubtaskRequest(exchange);
            } else if (pathArray[2].equals("epic")) {
                handlePostEpicRequest(exchange);
            }
        }
    }


    private void handlePostTaskRequest(HttpExchange exchange) {
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
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handlePostSubtaskRequest(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            if (!manager.getSubtasks().containsKey(subtask.getId())) {
                if (manager.getEpics().containsKey(subtask.getEpicId())) {
                    manager.addSubtask(subtask);
                    writeResponse(exchange, "Подзадача " + subtask.getId() + " создана.\n" + body, 200);
                } else {
                    writeResponse(exchange, "Подзадача не может быть создана,поскольку нет эпика с id =  " + subtask.getEpicId(), 204);
                }
            } else {
                manager.updateSubtask(subtask);
                writeResponse(exchange, "Подзадача " + subtask.getId() + " обновлена.\n" + body, 200);
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }

    private void handlePostEpicRequest(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);

            if (!manager.getSubtasks().containsKey(epic.getId())) {
                manager.addEpic(epic);
                writeResponse(exchange, "Эпик " + epic.getId() + " создан.\n" + body, 200);
            } else {
                manager.updateEpic(epic);
                writeResponse(exchange, "Эпик " + epic.getId() + " обновлен.\n" + body, 200);
            }
        } catch (RuntimeException | IOException exp) {
            throw  new JsonIOException(exp.getMessage());
        }
    }


    //DELETE
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String[] pathArray = exchange.getRequestURI().getPath().trim().split("/");
        if (pathArray.length == 3) {
            if (pathArray[2].equals("task")) {
                handleDeleteTaskRequest(exchange);
            } else if (pathArray[2].equals("subtask")) {
                handleDeleteSubtaskRequest(exchange);
            } else if (pathArray[2].equals("epic")) {
                handleDeleteEpicRequest(exchange);
            }
        }
    }

    private void handleDeleteTaskRequest(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            try {
                manager.clearTasksMap();
                writeResponse(exchange, "Все задачи успешно удалены", 200);
            } catch (IllegalArgumentException | IOException exp) {
                throw  new JsonIOException(exp.getMessage());
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

    private void handleDeleteSubtaskRequest(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            try {
                manager.clearSubtasksMap();
                writeResponse(exchange, "Все подзадачи успешно удалены", 200);
            } catch (IllegalArgumentException | IOException exp) {
                throw  new JsonIOException(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (manager.getSubtasks().containsKey(id)) {
                manager.removeSubtaskById(id);
                writeResponse(exchange, "Подзадача с id = " + id + " успешно удалена", 200);
            } else {
                writeResponse(exchange, "Подздача с id = " + id + " отсутсвтует", 204);
            }
        }
    }

    private void handleDeleteEpicRequest(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            try {
                manager.clearEpicsMap();
                writeResponse(exchange, "Все эпики успешно удалены", 200);
            } catch (IllegalArgumentException | IOException exp) {
                throw  new JsonIOException(exp.getMessage());
            }
        } else {
            String[] query = exchange.getRequestURI().getQuery().split("=");
            int id = Integer.parseInt(query[1].trim());
            if (manager.getEpics().containsKey(id)) {
                manager.removeEpicById(id);
                writeResponse(exchange, "Эпики с id = " + id + " успешно удалена", 200);
            } else {
                writeResponse(exchange, "Эпики с id = " + id + " отсутсвтует", 204);
            }
        }
    }


    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

}

enum Methods {
    POST,
    GET,
    DELETE;
}
