package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.ManagerSaveException;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateTimeAdapter;
import http.client.KVTaskClient;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;

    private final Gson gson = new GsonBuilder()
            //.serializeNulls()
            //.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            //.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);

    }

    public void load() {
        Map<Integer, Task> tasks = gson.fromJson(
                client.load("tasks"),
                new TypeToken<HashMap<Integer, Task>>() {
                }.getType()
        );
        Map<Integer, Epic> epics = gson.fromJson(
                client.load("epics"),
                new TypeToken<HashMap<Integer, Epic>>() {
                }.getType()
        );
        Map<Integer, Subtask> subtasks = gson.fromJson(
                client.load("subtasks"),
                new TypeToken<HashMap<Integer, Subtask>>() {
                }.getType()
        );

        int startId = Integer.parseInt(client.load("startId"));

        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
        this.prioritizedTasks.addAll(tasks.values());
        this.prioritizedTasks.addAll(epics.values());
        this.prioritizedTasks.addAll(subtasks.values());
        this.id = startId;
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(tasks));
        client.put("epics", gson.toJson(epics));
        client.put("subtasks", gson.toJson(subtasks));
        client.put("startId", gson.toJson(id));
    }
}
