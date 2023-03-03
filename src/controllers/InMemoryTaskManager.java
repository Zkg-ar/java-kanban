package controllers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import model.*;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private Map<Integer,Task> prioritizedTasks = new TreeMap<>();
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
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Iterator<Map.Entry<Integer, Subtask>> it = subtasks.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Subtask> entry = it.next();
                if (entry.getValue().getEpicId() == id) {
                    it.remove();
                    historyManager.remove(entry.getValue().getId());
                }
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public int generateId() {
        return ++id;
    }

    @Override
    public void clearTasksMap() {
        for (Integer key : tasks.keySet()) {
            historyManager.remove(tasks.get(key).getId());
        }
        tasks.clear();
    }

    @Override
    public void clearSubtasksMap() {
        for (Integer key : subtasks.keySet()) {
            historyManager.remove(subtasks.get(key).getId());
        }
        subtasks.clear();

    }

    @Override
    public void clearEpicsMap() {
        for (Integer key : epics.keySet()) {
            historyManager.remove(epics.get(key).getId());
        }
        epics.clear();
        clearSubtasksMap();
    }

    @Override
    public void addTask(Task task) {
        try {
            tasks.put(id, task);
            prioritizedTasks.put(task.getId(),task);
            checkingTheIntersection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            subtasks.put(id, subtask);
            prioritizedTasks.put(subtask.getId(),subtask);
            checkingTheIntersection();
            updateSubtasksMapForEpic(subtask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.put(subtask.getId(), subtask);
        updateSubtasksMapForEpic(subtask);
    }

    @Override
    public void updateSubtasksMapForEpic(Subtask subtask) {
        Map<Integer, Subtask> subtasksMap = epics.get(subtask.getEpicId()).getSubtasks();
        subtasksMap.put(subtask.getId(), subtask);
    }


    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {

        List<Task> sortedByDateList = prioritizedTasks.values()
                .stream()
                .sorted(Comparator.comparing(task -> task.getStartTime().orElse(null), Comparator.nullsLast(LocalDateTime::compareTo)))
                .collect(Collectors.toList());

        return sortedByDateList;
    }



    private void checkingTheIntersection() throws Exception {
        List<Task>list = getPrioritizedTasks();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getStartTime().orElse(LocalDateTime.MAX).isBefore(list.get(i - 1).getEndTime())) {
                throw new Exception(list.get(i - 1).getName() + " " + list.get(i - 1).getEndTime() + " по времени выполнения пересекается с  "
                        + list.get(i).getName() + " " + list.get(i).getEndTime());
            }
        }
    }
}
