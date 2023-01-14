package controllers;

import java.util.*;

import model.*;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int id = 0;
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public int getId() {
        return id;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public List<Subtask> getEpicsSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        int epicId = epic.getId();
        for (Integer key : subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == epicId) {
                epicsSubtasks.add(subtasks.get(key));
            }
        }
        return epicsSubtasks;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            updateSubtasksMapForEpic(subtasks.get(id));
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Iterator<Map.Entry<Integer, Subtask>> it = subtasks.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Subtask> entry = it.next();
                if (entry.getValue().getEpicId() == id) {
                    it.remove();
                }
            }
            epics.remove(id);
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }

    @Override
    public int generateId() {
        return ++id;
    }

    @Override
    public void clearTasksMap() {
        tasks.clear();
    }

    @Override
    public void clearSubtasksMap() {
        subtasks.clear();

    }

    @Override
    public void clearEpicsMap() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void addTask(Task task) {
        tasks.put(id, task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(id, subtask);
        updateSubtasksMapForEpic(subtask);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(id, epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Subtask getSubtaskById(int id){
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getTaskById(int id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Epic getEpicById(int id){
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateSubtasksMapForEpic(subtask);
    }

    @Override
    public void updateSubtasksMapForEpic(Subtask subtask) {
        Map<Integer, Subtask> subtasksMap = epics.get(subtask.getEpicId()).getSubtasks();
        subtasksMap.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtasks(subtasksMap);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
