import controllers.InMemoryTaskManager;
import controllers.Managers;
import controllers.TaskManager;
import model.*;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        TaskManager manager =  new InMemoryTaskManager();
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
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getSubtaskById(3);
        manager.getSubtaskById(3);

        System.out.println("История просмотров:" + "\n" + manager.getHistory());

        //Пример
        isRemoved(manager.removeTaskById(5));

    }

    public static void isRemoved(boolean isRemoved){
        if(!isRemoved){
            System.out.println("Запрошенная по Id задача/эпик/подзадача не существует");
        }
    }
}

