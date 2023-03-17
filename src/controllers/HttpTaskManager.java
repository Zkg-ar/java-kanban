package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateAdapter;
import http.adapters.LocalDateTimeAdapter;
import http.adapters.LocalTimeAdapter;
import http.client.KVTaskClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;



public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);

    }

    public void load() {
        client.load("tasks");
        client.load("epics");
        client.load("subtasks");
        client.load("history");
    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(tasks));
        client.put("epics", gson.toJson(epics));
        client.put("subtasks", gson.toJson(subtasks));
        client.put("history",gson.toJson(getHistory()));
    }
}
