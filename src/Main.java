import controllers.FileBackedTaskManager;

import http.servers.HttpTaskServer;
import http.servers.KVServer;
import model.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;



public class Main {

    public static void main(String[] args) throws IOException {

        //TaskManager manager = new InMemoryTaskManager();
        //new KVServer().start();
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("src/resources/ManagerFile.csv"));

        Task task1 = new Task(manager.generateId(), "Задача 1",
                "Задача 1", Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0), Duration.ofMinutes(15));

        manager.addTask(task1);

        Task task2 = new Task(manager.generateId(), "Задача 2",
                "Задача 2", Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0), Duration.ofMinutes(9));

        manager.addTask(task2);

        Epic epic1 = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи 1",
                Status.NEW, epic1.getId(),LocalDateTime.of(2022, Month.APRIL, 2, 15, 0), Duration.ofMinutes(24));
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(manager.generateId(), "Подзадача 2", "Описание подзадачи 2",
                Status.DONE, epic1.getId(),LocalDateTime.of(2022, Month.APRIL, 3, 12, 30), Duration.ofMinutes(15));
        manager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask(manager.generateId(), "Подзадача 3", "Описание подзадачи 3",
                Status.IN_PROGRESS, epic1.getId(),LocalDateTime.of(2022, Month.MAY, 5, 13, 0), Duration.ofMinutes(9));
        manager.addSubtask(subtask3);

        Epic epic2 = new Epic(manager.generateId(), "Эпик2", "Описание эпика",LocalDateTime.of(2022, Month.MAY, 5, 14, 0), Duration.ofMinutes(9));
        manager.addEpic(epic2);


        Task task3 = new Task(manager.generateId(), "Тестовая задача",
                "Описание тестовой задачи", Status.DONE);


        manager.addTask(task3);


        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getEpicById(7);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(8);

        manager.loadFromFile(new File("src/resources/ManagerFile.csv"));


        HttpTaskServer server = new HttpTaskServer(manager);
    }


}

