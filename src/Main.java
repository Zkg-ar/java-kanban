import controllers.FileBackedTaskManager;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import controllers.TaskManager;
import model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        TaskManager manager =new  FileBackedTaskManager(new File("ManagerFile.csv"));
        Task task1 = new Task(manager.generateId(), "Задача 1",
                "Задача 1", Status.NEW);
        manager.addTask(task1);

        Task task2 = new Task(manager.generateId(), "Задача 2",
                "Задача 2", Status.NEW);
        manager.addTask(task2);

        Epic epic1 = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи 1",
                Status.NEW,epic1.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(manager.generateId(), "Подзадача 2", "Описание подзадачи 2",
                Status.DONE,epic1.getId());
        manager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(manager.generateId(), "Подзадача 3", "Описание подзадачи 3",
                Status.IN_PROGRESS,epic1.getId());
        manager.addSubtask(subtask3);

        Epic epic2 = new Epic(manager.generateId(), "Эпик2", "Описание эпика");
        manager.addEpic(epic2);



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




        System.out.println("История просмотров:" + "\n" + manager.getHistory());



        //System.out.println("История просмотров после удаления :" + "\n" + manager.getHistory());

        //System.out.println(manager.getTaskById(1));

    }


}

