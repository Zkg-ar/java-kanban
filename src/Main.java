import controllers.InMemoryTaskManager;
import model.*;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task(manager.generateId(), "Задача 1",
                "Задача 1", Status.NEW);
        manager.addTask(task1);

        Epic epic1 = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic1);
        Subtask subtask = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи 1",
                Status.NEW,epic1.getId());
        manager.addSubtask(subtask);

        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        System.out.println("История просмотров:" + "\n" + manager.getHistory());

    }
}

