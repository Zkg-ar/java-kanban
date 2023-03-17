package tests.controllersTest;


import controllers.HttpTaskManager;
import exception.ManagerSaveException;
import http.client.KVTaskClient;

import http.servers.KVServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest{

    KVServer kvServer;

    KVTaskClient client;
    private Task task;
    private Subtask subtask;
    private Epic epic;

    private  HttpTaskManager manager;

    private final static String url = "http://localhost:8078";


    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager(url);
        client = new KVTaskClient(url);

        task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        manager.addTask(task);

        epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        subtask = new Subtask(manager.generateId(),
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 10, 15, 0),
                Duration.ofMinutes(13));

        manager.addSubtask(subtask);

    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }
    @Test

    void loadFromServerWhenItEmpty(){
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    client.load("task");
                    client.load("epic");
                    client.load("subtask");
                });
        assertEquals("Загрузка закончилась неудачно. Причина:HTTP/1.1 header parser received no bytes",
                exception.getMessage());
    }

    @Test
    void loadFromServer() {

        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());


        manager.load();

        Map<Integer,Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        List<Task> history = manager.getHistory();

        assertNotNull(history);
        assertEquals(3, history.size());

    }
}

