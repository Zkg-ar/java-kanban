package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {


    int generateId();

    Map<Integer, Task> getTasks();

    List<Subtask> getEpicsSubtasks(Epic epic);

    Map<Integer, Subtask> getSubtasks();

    Map<Integer, Epic> getEpics();

    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    void clearTasksMap();

    void clearSubtasksMap();

    void clearEpicsMap();

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateSubtasksMapForEpic(Subtask subtask);

    void updateEpic(Epic epic);

    Subtask getSubtaskById(int id);
    Task getTaskById(int id);
    Epic getEpicById(int id);

    List<Task> getHistory();

    
}
